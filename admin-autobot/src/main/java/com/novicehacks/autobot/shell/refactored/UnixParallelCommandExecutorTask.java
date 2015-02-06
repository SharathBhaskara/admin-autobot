package com.novicehacks.autobot.shell.refactored;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import com.novicehacks.autobot.BotUtils;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

/**
 * Executes each command on a server in a seperate session and in seperate
 * thread.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class UnixParallelCommandExecutorTask implements Runnable {

	private Connection		connection;
	private Server			server;
	private Command			command;
	private Session			session;
	private StringBuilder	commandOutputBuffer;
	private Future<?>		commandOutputLoggerTaskFuture;
	private Logger			logger	= LogManager.getLogger (UnixParallelCommandExecutorTask.class);

	/**
	 * 
	 * @param shellConnection
	 * @param unixCommand
	 * @throws CommandExecutionException
	 */
	UnixParallelCommandExecutorTask (Connection shellConnection, Server server, Command unixCommand) {
		validateParams (shellConnection, unixCommand);
		this.connection = shellConnection;
		this.server = server;
		this.command = unixCommand;
	}

	private void validateParams(Connection shellConnection, Command unixCommand) {
		logger.entry (shellConnection, unixCommand);
		if (BotUtils.HasNullReferences (shellConnection, unixCommand))
			throw new CommandExecutionException ("Invalid Connection / Command Passed",
					new IllegalArgumentException ());
		logger.exit ();
	}

	/**
	 * @throws CommandExecutionException
	 *         if unable to open session / execute command on the connection.
	 */
	@Override
	public void run() {
		createSession ();
		executeCommand ();
		endSession ();
		logCommandOutputAsynchronously ();
	}

	private void createSession() {
		try {
			this.session = this.connection.openSession ();
		} catch (IOException ex) {
			throw new CommandExecutionException ("Unable to create a session on Connection", ex);
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
			logger.error ("Failed to execute command on the session : {}", this.command, ex);
			throw new CommandExecutionException ("Command Execution Failed : " + unixCommand, ex);
		} finally {
			endSession ();
		}
	}

	private void endSession() {
		this.session.close ();
	}

	private void executeCommandInSessionAndCollectOutput(String unixCommand) throws IOException {
		this.session.execCommand (unixCommand);
		collectOutputFromSession ();
	}

	private void collectOutputFromSession() throws IOException {
		commandOutputBuffer = new StringBuilder ();
		InputStream inputStream = session.getStdout ();
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
				commandOutputBuffer.append (_outputLine);
				commandOutputBuffer.append (System.lineSeparator ());
			}
		}

	}

	private void logCommandOutputAsynchronously() {
		UnixCommandOutputLoggerTask loggerTask;
		String commandOutput = this.commandOutputBuffer.toString ();
		Server unixServer = server;
		Command unixCommand = command;
		loggerTask = new UnixCommandOutputLoggerTask (unixServer, unixCommand, commandOutput);
		commandOutputLoggerTaskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (
				loggerTask);
	}

	public Future<?> commandOutputLoggerTaskFuture() {
		return this.commandOutputLoggerTaskFuture;
	}

}
