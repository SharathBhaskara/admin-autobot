package com.novicehacks.autobot.ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.ThreadManager;
import com.novicehacks.autobot.ssh.exception.CommandExecutionException;
import com.novicehacks.autobot.ssh.logger.ShellOutputLoggerTask;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

/**
 * Executes each command on a server in a seperate session and in seperate
 * thread.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class ParallelCommandExecutorTask implements Runnable {

	private SSHConnection connection;
	private SSHSession session;
	private Server server;
	private Command command;
	private StringBuilder commandOutputBuffer;
	private Future<?> commandOutputLoggerTaskFuture;
	private Logger logger = LogManager.getLogger (ParallelCommandExecutorTask.class);

	/**
	 * 
	 * @param shellConnection
	 * @param unixCommand
	 * @throws CommandExecutionException
	 */
	protected ParallelCommandExecutorTask (	SSHConnection shellConnection,
											Server server,
											Command unixCommand) {
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
		createSession ();
		executeCommandAndDisconnectSession ();
		logCommandOutputAsynchronously ();
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
		String unixCommand = this.command.command ();
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

	private void endSession() {
		this.session.closeSession ();
	}

	private void executeCommandInSessionAndCollectOutput(String unixCommand) throws IOException {
		this.session.execCommand (unixCommand);
		collectOutputFromSession ();
	}

	private void collectOutputFromSession() throws IOException {
		this.commandOutputBuffer = new StringBuilder ();
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

	private void logCommandOutputAsynchronously() {
		ShellOutputLoggerTask loggerTask;
		String commandOutput = this.commandOutputBuffer.toString ();
		Server unixServer = this.server;
		Command unixCommand = this.command;
		loggerTask = new ShellOutputLoggerTask (unixServer, unixCommand, commandOutput);
		this.commandOutputLoggerTaskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (
				loggerTask);
	}

	public Future<?> commandOutputLoggerTaskFuture() {
		return this.commandOutputLoggerTaskFuture;
	}

}
