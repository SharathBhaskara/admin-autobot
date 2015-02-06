package com.novicehacks.autobot.shell.refactored;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import com.novicehacks.autobot.BotUtils;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

/**
 * Executes all the commands on a server in one single thread.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class UnixSequentialCommandExecutorTask implements Runnable {

	private Connection			connection;
	private Server				server;
	private Command[]			executableCommands;
	private Session				session;
	private InputStream			remoteInputStream;
	private OutputStream		remoteOutputStream;
	private PrintStream			remoteCommandWriter;
	private Thread				remoteConsumerThread;
	private Future<?>			commandOutputLoggerTaskFuture;
	private StringBuilder		commandOutput				= new StringBuilder ();
	private volatile boolean	initCommandExecutionStarted	= false;
	private volatile boolean	commandExecutionStarted		= false;
	private Lock				shellLock					= new ReentrantLock (true);
	private Condition			SendInput					= shellLock.newCondition ();
	private Condition			InputComplete				= shellLock.newCondition ();
	private Logger				logger						= LogManager
																	.getLogger (UnixSequentialCommandExecutorTask.class);

	public UnixSequentialCommandExecutorTask (	Connection connection,
												Server unixServer,
												Command[] unixCommands) {
		validateParams (connection, unixServer, unixCommands);
		this.connection = connection;
		this.server = unixServer;
		this.executableCommands = unixCommands;
	}

	private void validateParams(Connection connection, Server unixServer, Command[] unixCommands) {
		if (BotUtils.HasNullReferences (connection, unixServer, unixCommands)) {
			throw new CommandExecutionException ("Invalid paramters passed",
					new IllegalArgumentException ());
		}
	}

	@Override
	public void run() {
		this.logger.entry ();
		this.createSessionAndInitiate ();
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
		this.remoteInputStream = session.getStdout ();
		this.remoteOutputStream = session.getStdin ();
		this.remoteCommandWriter = new PrintStream (remoteOutputStream);
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
		remoteConsumerTask = new UnixCommandOutputConsumerTask ();
		return remoteConsumerTask;
	}

	private void executeCommands() {
		this.logger.entry ();
		try {
			this.startExecutingCommandsSequentially ();
		} catch (InterruptedException e) {
			this.logger.error ("Thread Interrupted", e);
			BotUtils.propogateInterruptIfExist (e);
		}
		this.logger.exit ();
	}

	private void startExecutingCommandsSequentially() throws InterruptedException {
		this.logger.entry ();
		this.acquireLockOnShell ();
		this.logger.trace ("Shell Lock acquired by Command Executor");
		this.executeInitCommands ();
		this.waitUntilOutputConsumed ();
		this.executeExecutableCommands ();
		this.waitUntilOutputConsumed ();
		this.releaseLockOnShell ();
		this.logger.trace ("Shell Lock released by Command Executor");
		this.logger.exit ();
	}

	private void executeInitCommands() throws InterruptedException {
		for (String initCommand : server.initCommands ()) {
			this.logger.trace ("Executing Init Command {}", initCommand);
			this.sendCommandToRemoteShell (initCommand, true, false);
			this.waitUntilOutputConsumed ();
			this.logger.trace ("Execution of Init Command ({}) Completed ", initCommand);
		}
	}

	private void waitUntilOutputConsumed() throws InterruptedException {
		try {
			this.waitUntilTheRemoteOutputIsConsumed ();
		} catch (CommandExecutionCompleteException e) {
			this.logger.debug ("Command Execution Complete");
		}
	}

	/**
	 * Executes the executable commands and wait until all the output processed.
	 * 
	 * @throws InterruptedException
	 */
	private void executeExecutableCommands() throws InterruptedException {
		this.logger.entry ();
		this.processExecutableCommands ();
		this.logger.exit ();
	}

	/**
	 * Appends header and footer to the outputBuffer, before and after executing
	 * the command on remote shell.
	 * 
	 * @throws InterruptedException
	 */
	private void processExecutableCommands() throws InterruptedException {
		for (Command command : this.executableCommands) {
			this.logger.trace ("Executing Command {} With Id : {}", command.command (),
					command.id ());
			this.appendHeaderToOutput (command);
			this.sendCommandToRemoteShell (command.command (), false, true);
			this.waitUntilOutputConsumed ();
			this.appendFooterToOutput ();
			this.logger.trace ("Execution Completed For Command {} With Id : {}",
					command.command (), command.id ());
		}
	}

	/**
	 * Acquire the Lock on Shell before executing command.
	 * 
	 * @param command
	 * @param initCommand
	 * @throws InterruptedException
	 */
	private void sendCommandToRemoteShell(	String command,
											boolean isInitCommand,
											boolean isExecutable) throws InterruptedException {
		this.logger.entry ();
		this.setCommandExecutionFlag (isInitCommand, isExecutable);
		this.sendCommand (command);
		this.logger.exit ();
	}

	private void sendCommand(String command) throws InterruptedException {
		this.logger.entry ();
		this.blockUntilSendInputSignalled ();
		this.writeDataToRemoteStreamAndWait (command);
		this.signalInputComplete ();
		this.logger.exit ();
	}

	private void writeDataToRemoteStreamAndWait(final String command) throws InterruptedException {
		char[] data = command.toCharArray ();
		remoteCommandWriter.println (data);
		this.waitForCommandToBeProcessed ();
	}

	private void waitForCommandToBeProcessed() throws InterruptedException {
		Thread.sleep (1 * 1000);
	}

	private void setCommandExecutionFlag(boolean isInitCommand, boolean isExecutable) {
		if (isInitCommand)
			initCommandExecutionStarted = true;
		if (isExecutable)
			commandExecutionStarted = true;
	}

	/**
	 * Block until all the output from Remote Machine is processed
	 * 
	 * @throws InterruptedException
	 * @throws CommandExecutionCompleteException
	 */
	private void waitUntilTheRemoteOutputIsConsumed() throws InterruptedException,
			CommandExecutionCompleteException {
		this.logger.entry ();
		this.acquireLockOnShell ();
		this.logger.trace ("Shell Lock Acquired by WaitForFinalOutput");
		this.blockUntilSendInputSignalled ();
		this.logger.debug ("Final Output from the remote received, hence interrupting");
		this.releaseLockOnShell ();
		this.logger.trace ("Shell Lock Relased by WaitForFinalOutput");
		this.logger.exit ();
		throw new CommandExecutionCompleteException ();

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
		this.session.close ();
	}

	private void closeInputAndOutputStreams() {
		try {
			this.remoteCommandWriter.close ();
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
		UnixCommandOutputLoggerTask loggerTask;
		String commandOutput = this.commandOutput.toString ();
		boolean appendRawContent = true;
		loggerTask = new UnixCommandOutputLoggerTask (commandOutput, appendRawContent);
		this.commandOutputLoggerTaskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (
				loggerTask);
	}

	private void acquireLockOnShell() throws InterruptedException {
		this.shellLock.lockInterruptibly ();
	}

	private void releaseLockOnShell() {
		this.shellLock.unlock ();
	}

	private void blockUntilSendInputSignalled() {
		this.logger.trace ("Waiting for SendInput Signal");
		this.SendInput.awaitUninterruptibly ();
		this.logger.trace ("SendInput Signal Received");
	}

	private void signalInputComplete() {
		this.logger.trace ("Signalling InputComplete");
		this.InputComplete.signal ();
	}

	private void signalSendInputAndWaitForInputComplete() throws InterruptedException {
		this.logger.trace ("Signalling SendInput");
		this.SendInput.signal ();
		this.logger.trace ("Waiting for InputComplete Signal");
		this.InputComplete.await (1, TimeUnit.SECONDS);
		this.logger.trace ("InputComplete Signal Received or Timedout");
	}

	private void appendHeaderToOutput(Command command) {
		String data;
		data = UnixCommandOutputLoggerTask.getHeader (this.server, command);
		this.commandOutput.append (data);
	}

	private void appendFooterToOutput() {
		String data;
		data = UnixCommandOutputLoggerTask.getFooter ();
		this.commandOutput.append (UnixCommandOutputLoggerTask.newLine ());
		this.commandOutput.append (data);
	}

	public Future<?> commandOutputLoggerTaskFuture() {
		return this.commandOutputLoggerTaskFuture;
	}

	/**
	 * A thread to consume all the remote output.
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks!
	 *
	 */
	private class UnixCommandOutputConsumerTask implements Runnable {
		private Logger	logger	= LogManager.getLogger (UnixCommandOutputConsumerTask.class);

		@Override
		public void run() {
			this.logger.entry ();
			this.startConsumingOutput ();
			this.logger.exit ();
		}

		private void startConsumingOutput() {
			this.logger.entry ();
			this.blockUntilCommandExecutionStarted ();
			this.logger.trace ("Reading the remote output from the server");
			this.readRemoteOutput ();
			this.logger.exit ();
		}

		private void blockUntilCommandExecutionStarted() {
			this.logger.entry ();
			while (!initCommandExecutionStarted) {}
			this.logger.debug ("Init Command Received on Shell");
			this.logger.exit ();
		}

		private void readRemoteOutput() {
			this.logger.entry ();
			try {
				while (true) {
					acquireLockOnShell ();
					this.logger.trace ("Shell Lock Acquired By Remote Consumer");
					this.sendInputSignalAndWaitWhenRemoteOutputNotAvailable ();
					if (outputAvailable ())
						this.readAndLogCommandOutput ();
					releaseLockOnShell ();
					this.logger.trace ("Shell Lock Released By Remote Consumer");
				}
			} catch (InterruptedException e) {
				this.logger.debug ("Remote Output Consumer Interrupted");
				BotUtils.propogateInterruptIfExist (e);
			} catch (Exception e) {
				this.logger.error ("Exception when reading remote server output", e);
			}
			this.logger.exit ();
		}

		private boolean outputAvailable() throws IOException {
			if (remoteInputStream.available () > 0)
				return true;
			return false;
		}

		private void readAndLogCommandOutput() {
			byte[] buff = new byte[8192];
			try {
				int len = remoteInputStream.read (buff);
				if (len == -1)
					return;
				this.logOutputWhenNeeded (buff);
			} catch (IOException ex) {
				this.logger.error ("Failed While reading from Remote Consumer", ex);
			}

		}

		private void logOutputWhenNeeded(byte[] buff) {
			this.logger.debug (byteArrayToString (buff));
			if (commandExecutionStarted)
				this.appendCommandOutput (buff);
		}

		private void sendInputSignalAndWaitWhenRemoteOutputNotAvailable() throws IOException,
				InterruptedException {
			this.logger.entry ();
			if (remoteInputStream.available () == 0) {
				signalSendInputAndWaitForInputComplete ();
			}
			this.logger.exit ();
		}

		private void appendCommandOutput(byte[] buff) {
			String data;
			data = byteArrayToString (buff);
			commandOutput.append (data.trim ());
		}

		private String byteArrayToString(byte[] buff) {
			String data;
			data = new String (buff);
			return data;
		}

	}

}
