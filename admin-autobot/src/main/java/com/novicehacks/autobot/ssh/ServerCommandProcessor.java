package com.novicehacks.autobot.ssh;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.config.SysConfig;
import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.ThreadManager;
import com.novicehacks.autobot.ssh.exception.CommandExecutionException;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

/**
 * The Master Task to execute the commands on the SSHserver.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * 
 * @see ParallelCommandExecutorTask
 * @see SequentialCommandExecutorTask
 * 
 */
public final class ServerCommandProcessor implements Runnable {

	private Server server;
	private Command[] commands;
	private DefaultSSHConnection connection;
	private ServerConnectionHandle serverHandle;
	private Future<?> sequentialTaskFuture;
	private List<Future<?>> commandFutureList;
	private boolean isRunningInParallel;
	private Logger logger = LogManager.getLogger (ServerCommandProcessor.class);

	/**
	 * @param unixServer
	 * @param unixCommands
	 * @throws NullPointerException
	 *         if unixCommands parameter is null
	 * @throws IllegalArgumentException
	 *         if unixServer parameter is null
	 */
	public ServerCommandProcessor (final Server unixServer, final Collection<Command> unixCommands) {
		this (unixServer, unixCommands.toArray (new Command[] { }));
	}

	/**
	 * @param unixServer
	 * @param unixCommands
	 * @throws IllegalArgumentException
	 *         if either of the parameters are having null values
	 */
	public ServerCommandProcessor (final Server unixServer, final Command... unixCommands) {
		validateParams (unixServer, unixCommands);
		this.server = unixServer;
		this.commands = unixCommands;
		this.serverHandle = new ServerConnectionHandle (unixServer);
	}

	private void validateParams(final Server unixServer, final Command[] unixCommands) {
		if (BotUtils.HasNullReferences (unixServer, unixCommands)) {
			throw new IllegalArgumentException (
					"Invalid server and command passed to ShellExecutor");
		}
	}

	@Override
	public void run() {
		this.logger.entry ();
		connectToServer ();
		executeCommandsAndDisconnectServer ();
		this.logger.exit ();
	}

	private void connectToServer() {
		this.connection = this.serverHandle.getAuthenticatedConnection ();
	}

	private void disconnetServer() {
		this.serverHandle.disconnect (this.connection);
	}

	private void executeCommandsAndDisconnectServer() {
		try {
			executeCommandsOnServer ();
			waitForCommandsCompletion ();
		} catch (Exception ex) {
			this.logger.error ("Thread Interrupted", ex);
			BotUtils.PropogateInterruptIfExist (ex);
			throw new CommandExecutionException ("Commands Execution Failed On Server: "
					+ this.server, ex);
		} finally {
			disconnetServer ();
		}
	}

	private void executeCommandsOnServer() {
		boolean serverHasInitCommands;
		serverHasInitCommands = checkForServerInitCommands ();
		if (serverHasInitCommands)
			executeSequentially ();
		else
			executeParallely ();
	}

	private boolean checkForServerInitCommands() {
		if (this.server.initCommands () == null || this.server.initCommands ().length == 0)
			return false;
		else
			return true;
	}

	private void executeSequentially() {
		this.logger.entry ();
		this.isRunningInParallel = false;
		executeCommandsSequentially ();
		this.logger.exit ();
	}

	private void executeCommandsSequentially() {
		SequentialCommandExecutorTask task;
		task = new SequentialCommandExecutorTask (this.connection, this.server, this.commands);
		this.sequentialTaskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (task);
	}

	private void executeParallely() {
		this.logger.entry ();
		this.isRunningInParallel = true;
		executeCommandsParallely ();
		this.logger.exit ();
	}

	private void executeCommandsParallely() {
		Future<?> taskFuture;
		List<Future<?>> taskFutureList = new LinkedList<Future<?>> ();

		for (Command command : this.commands) {
			taskFuture = submitCommandForExecution (command);
			taskFutureList.add (taskFuture);
		}
		this.commandFutureList = taskFutureList;

	}

	private Future<?> submitCommandForExecution(Command command) {
		Future<?> taskFuture;
		ParallelCommandExecutorTask task;
		task = new ParallelCommandExecutorTask (this.connection, this.server, command);
		// TODO get the output logger future and check for exceptions
		taskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (task);
		return taskFuture;
	}

	private void waitForCommandsCompletion() {
		if (this.isRunningInParallel)
			waitForParallelExecutionCompletion ();
		else
			waitForSequentialExecitionCompletion ();
	}

	private void waitForSequentialExecitionCompletion() {
		try {
			this.sequentialTaskFuture.get (SysConfig.getInstance ().longTimeoutInMinutes (),
					TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			this.logger.error ("Thread Interrupted", e);
			BotUtils.PropogateInterruptIfExist (e);
		} catch (ExecutionException e) {
			this.logger.error ("Sequential Command Execution on Server {} Failed: {}",
					this.server.id (), e, e);
			throw new CommandExecutionException ("Sequential Execution Failed on server : "
					+ this.server.id (), e);
		} catch (TimeoutException e) {
			this.logger
					.error ("Sequential execution on server {} unfinished", this.server.id (), e);
			throw new CommandExecutionException (
					"Sequential Execution Failed Due to Timeout on server : " + this.server.id (),
					e);
		}
	}

	private void waitForParallelExecutionCompletion() {
		List<Throwable> failureReasons = new LinkedList<Throwable> ();
		for (Future<?> future : this.commandFutureList) {
			try {
				handleCommandExecutorTaskFuture (future);
			} catch (CommandExecutionException e) {
				failureReasons.add (e);
			}
		}
		wrapFailuresAndThrowIfNeeded (failureReasons);
	}

	private void handleCommandExecutorTaskFuture(Future<?> future) {
		try {
			future.get (SysConfig.getInstance ().longTimeoutInMinutes (), TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			this.logger.error ("Thread Interrupted", e);
			BotUtils.PropogateInterruptIfExist (e);
		} catch (ExecutionException e) {
			throw new CommandExecutionException (
					"Command Execution Failed With Errors on Server : " + this.server.id (), e);
		} catch (TimeoutException e) {
			throw new CommandExecutionException ("Command Execution Unfinished on Server : "
					+ this.server.id (), e);
		}
	}

	private void wrapFailuresAndThrowIfNeeded(List<Throwable> failureReasons) {
		if (failureReasons.size () > 0) {
			CommandExecutionException exception = new CommandExecutionException (
					"Command Execution Uncussessful with multiple failures5");
			exception.setMultipleReasons (failureReasons);
			throw exception;
		}
	}

}
