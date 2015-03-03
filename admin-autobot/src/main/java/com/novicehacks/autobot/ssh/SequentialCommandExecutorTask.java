package com.novicehacks.autobot.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.RunnableTask;
import com.novicehacks.autobot.core.ThreadManager;
import com.novicehacks.autobot.ssh.exception.CommandExecutionException;
import com.novicehacks.autobot.ssh.logger.ShellOutputLoggerTask;
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
 * Uses {@link SingleSessionCommandExecutionController
 * SessionController} as shared object for controlling the execution of the
 * command executor and output gobbler
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * @see SingleSessionCommandExecutor
 * @see ShellSequentialCommandOutputGobbler
 * @see SingleSessionCommandExecutionController
 * 
 *
 */
public class SequentialCommandExecutorTask implements RunnableTask {
	private boolean threadStarted;
	private Server server;
	private Command[] executableCommands;
	private SSHSession session;
	private SSHConnection connection;
	private InputStream remoteInputStream;
	private OutputStream remoteOutputStream;
	private Thread remoteConsumerThread;
	private Future<?> commandOutputLoggerTaskFuture;
	private SingleSessionCommandExecutionController sessionController;
	private Logger logger = LogManager.getLogger (SequentialCommandExecutorTask.class);

	protected SequentialCommandExecutorTask (	DefaultSSHConnection connection,
												Server unixServer,
												Command[] unixCommands) {
		validateParams (connection, unixServer, unixCommands);
		this.connection = connection;
		this.server = unixServer;
		this.executableCommands = unixCommands;
		this.sessionController = new SingleSessionCommandExecutionController (unixServer);
	}

	private void validateParams(DefaultSSHConnection connection,
								Server unixServer,
								Command[] unixCommands) {
		if (BotUtils.HasNullReferences (connection, unixServer, unixCommands)) {
			throw new CommandExecutionException ("Invalid paramters passed",
					new IllegalArgumentException ());
		}
	}

	@Override
	public void run() {
		this.threadStarted = true;
		this.logger.entry ();
		createSessionAndInitiate ();
		configureSessionController ();
		startRemoteOutputConsumer ();
		executeCommands ();
		stopRemoteOutputConsumer ();
		closeSessionAndShell ();
		logShellOutputAsynchronously ();
		this.logger.exit ();
	}

	private void createSessionAndInitiate() {
		this.logger.entry ();
		try {
			initiateSession ();
		} catch (IOException ex) {
			throw new CommandExecutionException ("Unable to create a session on server: "
					+ this.server.id (), ex);
		}
		this.logger.exit ();
	}

	private void initiateSession() throws IOException {
		this.session = this.connection.openSession ();
		this.session.getTerminal ();
		this.session.startShell ();
		this.remoteInputStream = this.session.stdOutputStream ();
		this.remoteOutputStream = this.session.stdInputStream ();

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
		remoteConsumerTask = getRemoteConsumerTask ();
		this.remoteConsumerThread = new Thread (remoteConsumerTask);
		this.remoteConsumerThread.setName ("RemoteConsumerThread-" + System.currentTimeMillis ());
		this.remoteConsumerThread.start ();
		this.logger.exit ();
	}

	Runnable getRemoteConsumerTask() {
		Runnable remoteConsumerTask;
		remoteConsumerTask = new SingleSessionCommandOutputGobblerTask (this.sessionController);
		return remoteConsumerTask;
	}

	private void executeCommands() {
		this.logger.entry ();
		try {
			startExecutingCommandsSequentially ();
		} catch (InterruptedException e) {
			this.logger.error ("Thread Interrupted", e);
			BotUtils.PropogateInterruptIfExist (e);
		}
		this.logger.exit ();
	}

	private void startExecutingCommandsSequentially() throws InterruptedException {
		SingleSessionCommandExecutor executor;
		executor = getSequentialCommandExecutor ();
		executor.startExecution ();
	}

	SingleSessionCommandExecutor getSequentialCommandExecutor() {
		return new SingleSessionCommandExecutor (this.server, this.executableCommands,
				this.sessionController);
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
		closeInputAndOutputStreams ();
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
		ShellOutputLoggerTask loggerTask;
		String commandOutput = this.sessionController.getCommandOutput ().toString ();
		loggerTask = new ShellOutputLoggerTask (commandOutput);
		this.commandOutputLoggerTaskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (
				loggerTask);
	}

	public Future<?> commandOutputLoggerTaskFuture() {
		return this.commandOutputLoggerTaskFuture;
	}

	@Override
	public final boolean isThreadStarted() {
		return this.threadStarted;
	}

}
