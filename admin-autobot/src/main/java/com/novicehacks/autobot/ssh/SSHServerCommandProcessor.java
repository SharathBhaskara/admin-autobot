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

import com.novicehacks.autobot.BotUtils;
import com.novicehacks.autobot.ThreadManager;
import com.novicehacks.autobot.config.SysConfig;
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
public final class SSHServerCommandProcessor implements Runnable {

	private Server						server;
	private Command[]					commands;
	private CustomizedSSHConnection		connection;
	private SSHServerConnectionHandle	serverHandle;
	private Future<?>					sequentialTaskFuture;
	private List<Future<?>>				commandFutureList;
	private boolean						isRunningInParallel;
	private Logger						logger	= LogManager
														.getLogger (SSHServerCommandProcessor.class);

	/**
	 * @param unixServer
	 * @param unixCommands
	 * @throws NullPointerException
	 *         if unixCommands parameter is null
	 * @throws IllegalArgumentException
	 *         if unixServer parameter is null
	 */
	public SSHServerCommandProcessor (	final Server unixServer,
										final Collection<Command> unixCommands) {
		this (unixServer, unixCommands.toArray (new Command[] { }));
	}

	/**
	 * @param unixServer
	 * @param unixCommands
	 * @throws IllegalArgumentException
	 *         if either of the parameters are having null values
	 */
	public SSHServerCommandProcessor (final Server unixServer, final Command... unixCommands) {
		validateParams (unixServer, unixCommands);
		this.server = unixServer;
		this.commands = unixCommands;
		this.serverHandle = new SSHServerConnectionHandle (unixServer);
	}

	private void validateParams(final Server unixServer, final Command[] unixCommands) {
		if (BotUtils.HasNullReferences (unixServer, unixCommands)) {
			throw new IllegalArgumentException (
					"Invalid server and command passed to ShellExecutor");
		}
	}

	@Override
	public void run() {
		logger.entry ();
		connectToServer ();
		executeCommandsAndDisconnectServer ();
		logger.exit ();
	}

	private void connectToServer() {
		this.connection = serverHandle.getAuthenticatedConnection ();
	}

	private void disconnetServer() {
		serverHandle.disconnect (this.connection);
	}

	private void executeCommandsAndDisconnectServer() {
		try {
			executeCommandsOnServer ();
			waitForCommandsCompletion ();
		} catch (Exception ex) {
			BotUtils.PropogateInterruptIfExist (ex);
			throw new CommandExecutionException ("Commands Execution Failed On Server: " + server,
					ex);
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
		logger.entry ();
		this.isRunningInParallel = false;
		executeCommandsSequentially ();
		logger.exit ();
	}

	private void executeCommandsSequentially() {
		SequentialCommandExecutorTask task;
		task = new SequentialCommandExecutorTask (connection, server, commands);
		this.sequentialTaskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (task);
	}

	private void executeParallely() {
		logger.entry ();
		this.isRunningInParallel = true;
		executeCommandsParallely ();
		logger.exit ();
	}

	private void executeCommandsParallely() {
		Future<?> taskFuture;
		List<Future<?>> taskFutureList = new LinkedList<Future<?>> ();

		for (Command command : commands) {
			taskFuture = submitCommandForExecution (command);
			taskFutureList.add (taskFuture);
		}
		this.commandFutureList = taskFutureList;

	}

	private Future<?> submitCommandForExecution(Command command) {
		Future<?> taskFuture;
		ParallelCommandExecutorTask task;
		task = new ParallelCommandExecutorTask (connection, server, command);
		taskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (task);
		return taskFuture;
	}

	private void waitForCommandsCompletion() {
		if (isRunningInParallel)
			waitForParallelExecutionCompletion ();
		else
			waitForSequentialExecitionCompletion ();
	}

	private void waitForSequentialExecitionCompletion() {
		try {
			this.sequentialTaskFuture.get (SysConfig.getInstance ().longTimeoutInMinutes (),
					TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.error ("Thread Interrupted", e);
			BotUtils.PropogateInterruptIfExist (e);
		} catch (ExecutionException e) {
			logger.error ("Sequential Command Execution on Server {} Failed: {}", server.id (), e,
					e);
			throw new CommandExecutionException ("Sequential Execution Failed on server : "
					+ server.id (), e);
		} catch (TimeoutException e) {
			logger.error ("Sequential execution on server {} unfinished", server.id (), e);
			throw new CommandExecutionException (
					"Sequential Execution Failed Due to Timeout on server : " + server.id (), e);
		}
	}

	private void waitForParallelExecutionCompletion() {
		List<Throwable> failureReasons = new LinkedList<Throwable> ();
		for (Future<?> future : commandFutureList) {
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
			BotUtils.PropogateInterruptIfExist (e);
		} catch (ExecutionException e) {
			throw new CommandExecutionException (
					"Command Execution Failed With Errors on Server : " + server.id (), e);
		} catch (TimeoutException e) {
			throw new CommandExecutionException ("Command Execution Unfinished on Server : "
					+ server.id (), e);
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
