package com.novicehacks.autobot.executor.ssh.commandexecutor.parallel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.ThreadManager;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.RunnableTask;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.executor.ssh.SSHConnection;
import com.novicehacks.autobot.executor.ssh.SSHSession;
import com.novicehacks.autobot.executor.ssh.exception.CommandExecutionException;
import com.novicehacks.autobot.executor.ssh.logger.ShellOutputLoggerTask;

/**
 * Executes each command on a server in a seperate session and in seperate
 * thread.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class ParallelExecutorTask implements RunnableTask {

	private boolean threadStarted = false;
	private boolean createSessionStepFlag = false;
	private boolean executeCommandStepFlag = false;
	private boolean outputLoggingStepFlag = false;
	private SSHConnection connection;
	private SSHSession session;
	private Server server;
	private Command command;

	private StringBuilder commandOutputBuffer = new StringBuilder ();
	private Future<?> commandOutputLoggerTaskFuture;
	private Logger logger = LogManager.getLogger (ParallelExecutorTask.class);

	/**
	 * 
	 * @param shellConnection
	 * @param unixCommand
	 * @throws CommandExecutionException
	 */
	public ParallelExecutorTask (SSHConnection shellConnection, Server server, Command unixCommand) {
		validateParams (shellConnection, server, unixCommand);
		this.connection = shellConnection;
		this.server = server;
		this.command = unixCommand;
	}

	private void validateParams(SSHConnection shellConnection, Server server, Command unixCommand) {
		if (BotUtils.HasNullReferences (shellConnection, server, unixCommand))
			throw new CommandExecutionException ("Invalid Connection / Command Passed",
					new IllegalArgumentException ());
	}

	/**
	 * @throws CommandExecutionException
	 *         if unable to open session / execute command on the connection.
	 */
	@Override
	public void run() {
		this.threadStarted = true;
		createSession ();
		this.createSessionStepFlag = true;
		executeCommandAndDisconnectSession ();
		this.executeCommandStepFlag = true;
		logCommandOutputAsynchronously ();
		this.outputLoggingStepFlag = true;
	}

	private void createSession() {
		try {
			this.session = this.connection.openSession ();
		} catch (IOException ex) {
			throw new CommandExecutionException ("Unable to create a session on Connection", ex);
		}
	}

	private void executeCommandAndDisconnectSession() {
		try {
			executeCommand ();
		} finally {
			endSession ();
		}
	}

	private void executeCommand() {
		String unixCommand = this.command.commandTxt ();
		if (unixCommand == null)
			throw new CommandExecutionException ("Invalid Command String in Command :"
					+ this.command);
		else
			executeCommandInSession (unixCommand);
	}

	/**
	 * Executes the command in the newly created session, and throws
	 * CommandExecutionException if failed to execute the command.
	 * 
	 * @param unixCommand
	 */
	private void executeCommandInSession(String unixCommand) {
		try {
			executeCommandInSessionAndCollectOutput (unixCommand);
		} catch (IOException ex) {
			this.logger.error ("Failed to execute command on the session : {}", this.command, ex);
			throw new CommandExecutionException ("Command Execution Failed : " + unixCommand, ex);
		}
	}

	private void executeCommandInSessionAndCollectOutput(String unixCommand) throws IOException {
		this.session.execCommand (unixCommand);
		collectOutputFromSession ();
	}

	private void collectOutputFromSession() throws IOException {
		InputStream inputStream = this.session.stdOutputStream ();
		parseAndPopulateOutput (inputStream);
	}

	private void parseAndPopulateOutput(InputStream inputStream) throws IOException {
		try (
				InputStreamReader _reader = new InputStreamReader (inputStream);
				BufferedReader _buffer = new BufferedReader (_reader) ) {
			String _outputLine;
			while (true) {
				_outputLine = _buffer.readLine ();
				if (_outputLine == null || _outputLine.equals (""))
					break;
				this.commandOutputBuffer.append (_outputLine);
				this.commandOutputBuffer.append (System.lineSeparator ());
			}
		}

	}

	private void endSession() {
		this.session.closeSession ();
	}

	private void logCommandOutputAsynchronously() {
		ShellOutputLoggerTask loggerTask;
		loggerTask = getOutputLoggerTask ();
		this.commandOutputLoggerTaskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (
				loggerTask);
	}

	public Future<?> commandOutputLoggerTaskFuture() {
		return this.commandOutputLoggerTaskFuture;
	}

	ShellOutputLoggerTask getOutputLoggerTask() {
		ShellOutputLoggerTask loggerTask;
		String commandOutput = this.commandOutputBuffer.toString ();
		loggerTask = new ShellOutputLoggerTask (this.server, this.command, commandOutput);
		return loggerTask;
	}

	final String commandOutputFromRemote() {
		return this.commandOutputBuffer.toString ();
	}

	final boolean isCreateSessionCompleted() {
		return this.createSessionStepFlag;
	}

	final boolean isExecuteCommandCompleted() {
		return this.executeCommandStepFlag;
	}

	final boolean isOutputLoggingCompleted() {
		return this.outputLoggingStepFlag;
	}

	@Override
	public final boolean isThreadStarted() {
		return this.threadStarted;
	}
}
