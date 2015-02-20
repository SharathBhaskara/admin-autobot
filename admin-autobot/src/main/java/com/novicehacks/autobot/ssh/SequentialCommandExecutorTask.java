package com.novicehacks.autobot.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.BotUtils;
import com.novicehacks.autobot.ThreadManager;
import com.novicehacks.autobot.ssh.exception.CommandExecutionException;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

/**
 * Executes all the commands on a server in one single thread.
 * <p>
 * Uses {@link ShellSequentialCommandExecutorTask CommandExecutor} and
 * {@link ShellSequentialCommandOutputGobblearTask OutputGobbler} to execute
 * commands sequentially in an open session.
 * </p>
 * <p>
 * Uses {@link ShellSessionController
 * SessionController} as shared object for controlling the execution of the
 * command executor and output gobbler
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * @see ShellSequentialCommandExecutor
 * @see ShellSequentialCommandOutputGobbler
 * @see ShellSessionController
 * 
 *
 */
public class SequentialCommandExecutorTask implements Runnable {

	private Server					server;
	private Command[]				executableCommands;
	private CustomizedSSHSession	session;
	private CustomizedSSHConnection	connection;
	private InputStream				remoteInputStream;
	private OutputStream			remoteOutputStream;
	private Thread					remoteConsumerThread;
	private Future<?>				commandOutputLoggerTaskFuture;
	private ShellSessionController	sessionController;
	private Logger					logger	= LogManager
													.getLogger (SequentialCommandExecutorTask.class);

	protected SequentialCommandExecutorTask (	CustomizedSSHConnection connection,
												Server unixServer,
												Command[] unixCommands) {
		validateParams (connection, unixServer, unixCommands);
		this.connection = connection;
		this.server = unixServer;
		this.executableCommands = unixCommands;
		this.sessionController = new ShellSessionController (unixServer);
	}

	private void validateParams(CustomizedSSHConnection connection,
								Server unixServer,
								Command[] unixCommands) {
		if (BotUtils.HasNullReferences (connection, unixServer, unixCommands)) {
			throw new CommandExecutionException ("Invalid paramters passed",
					new IllegalArgumentException ());
		}
	}

	@Override
	public void run() {
		this.logger.entry ();
		this.createSessionAndInitiate ();
		this.configureSessionController ();
		this.startRemoteOutputConsumer ();
		this.executeCommands ();
		this.stopRemoteOutputConsumer ();
		this.closeSessionAndShell ();
		this.logShellOutputAsynchronously ();
		this.logger.exit ();
	}

	private void createSessionAndInitiate() {
		this.logger.entry ();
		try {
			this.initiateSession ();

		} catch (IOException ex) {
			throw new CommandExecutionException ("Unable to create a session on server: "
					+ server.id (), ex);
		}
		this.logger.exit ();
	}

	private void initiateSession() throws IOException {
		this.session = this.connection.openSession ();
		this.session.requestDumbPTY ();
		this.session.startShell ();
		this.remoteInputStream = session.getStdOut ();
		this.remoteOutputStream = session.getStdIn ();

	}

	private void configureSessionController() {
		this.sessionController.setRemoteOutputStream (this.remoteOutputStream);
		this.sessionController.setRemoteInputStream (this.remoteInputStream);
	}

	/**
	 * Starts a new thread, for processing the remote output
	 */
	private void startRemoteOutputConsumer() {
		this.logger.entry ();
		Runnable remoteConsumerTask;
		remoteConsumerTask = this.getRemoteConsumerTask ();
		remoteConsumerThread = new Thread (remoteConsumerTask);
		remoteConsumerThread.setName ("RemoteConsumerThread-" + System.currentTimeMillis ());
		remoteConsumerThread.start ();
		this.logger.exit ();
	}

	private Runnable getRemoteConsumerTask() {
		Runnable remoteConsumerTask;
		remoteConsumerTask = new ShellSequentialCommandOutputGobblerTask (this.sessionController);
		return remoteConsumerTask;
	}

	private void executeCommands() {
		this.logger.entry ();
		try {
			this.startExecutingCommandsSequentially ();
		} catch (InterruptedException e) {
			this.logger.error ("Thread Interrupted", e);
			BotUtils.PropogateInterruptIfExist (e);
		}
		this.logger.exit ();
	}

	private void startExecutingCommandsSequentially() throws InterruptedException {
		ShellSequentialCommandExecutor executor;
		executor = new ShellSequentialCommandExecutor (server, executableCommands,
				sessionController);
		executor.startExecution ();
	}

	/**
	 * Kills the RemoteConsumer Thread by sending an interrupt to it.
	 */
	private void stopRemoteOutputConsumer() {
		this.remoteConsumerThread.interrupt ();
	}

	/**
	 * Closes the IO Streams and the session on the connection. To prevent
	 * memory leaks
	 */
	private void closeSessionAndShell() {
		this.closeInputAndOutputStreams ();
		this.session.closeSession ();
	}

	private void closeInputAndOutputStreams() {
		try {
			this.remoteOutputStream.close ();
			this.remoteInputStream.close ();
		} catch (IOException e) {
			this.logger.error ("IOException when closing Streams", e);
		}
	}

	/**
	 * Creates a new task to log the output buffer, and submit to the managed
	 * thread pool.
	 */
	private void logShellOutputAsynchronously() {
		SSHOutputLoggerTask loggerTask;
		String commandOutput = this.sessionController.getCommandOutput ().toString ();
		boolean appendRawContent = true;
		loggerTask = new SSHOutputLoggerTask (commandOutput, appendRawContent);
		this.commandOutputLoggerTaskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (
				loggerTask);
	}

	public Future<?> commandOutputLoggerTaskFuture() {
		return this.commandOutputLoggerTaskFuture;
	}

}
