/**
 * 
 */
package com.novicehacks.autobot.shell.refactored;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ethz.ssh2.Connection;

import com.novicehacks.autobot.BotUtils;
import com.novicehacks.autobot.config.SysConfig;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * 
 */
public final class UnixServerCommandProcessor implements Runnable {

	private Server				server;
	private Command[]			commands;
	private Connection			connection;
	private UnixServerHandle	serverHandle;
	private List<Future<?>>		commandFutureList;
	private boolean				isRunningInParallel;
	private Logger				logger	= LogManager.getLogger (UnixServerCommandProcessor.class);

	/**
	 * @param unixServer
	 * @param unixCommands
	 * @throws NullPointerException
	 *         if unixCommands parameter is null
	 * @throws IllegalArgumentException
	 *         if unixServer parameter is null
	 */
	public UnixServerCommandProcessor (	final Server unixServer,
										final Collection<Command> unixCommands) {
		this (unixServer, unixCommands.toArray (new Command[] { }));
	}

	/**
	 * @param unixServer
	 * @param unixCommands
	 * @throws IllegalArgumentException
	 *         if either of the parameters are having null values
	 */
	public UnixServerCommandProcessor (final Server unixServer, final Command... unixCommands) {
		validateParams (unixServer, unixCommands);
		this.server = unixServer;
		this.commands = unixCommands;
		this.serverHandle = new UnixServerHandle (unixServer);
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
		executeCommands ();
		disconnetServer ();
		logger.exit ();
	}

	private void connectToServer() {
		this.connection = serverHandle.getAuthenticatedConnection ();
	}

	private void disconnetServer() {
		serverHandle.disconnect (this.connection);
	}

	private void executeCommands() {
		boolean exceptionRaised = false;
		try {
			executeCommandsOnServer ();
			waitForCommandsCompletion ();
		} catch (Exception ex) {
			exceptionRaised = true;
			BotUtils.propogateInterruptIfExist (ex);
			throw new CommandExecutionException ("Commands Execution Failed On Server: " + server,
					ex);
		} finally {
			if (exceptionRaised)
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
		executeCommandsSequentiallyAndWaitForCompletion ();
		logger.exit ();
	}

	private void executeCommandsSequentiallyAndWaitForCompletion() {
		UnixSequentialCommandExecutorTask task;
		task = new UnixSequentialCommandExecutorTask (connection, server, commands);
		Future<?> taskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (task);
		waitForSequentialExecitionCompletion (taskFuture);
	}

	private void waitForSequentialExecitionCompletion(Future<?> taskFuture) {
		try {
			taskFuture.get (SysConfig.getInstance ().longTimeoutInMinutes (), TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.error ("Thread Interrupted", e);
			BotUtils.propogateInterruptIfExist (e);
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
		UnixParallelCommandExecutorTask task;
		task = new UnixParallelCommandExecutorTask (connection, server, command);
		taskFuture = ThreadManager.getInstance ().submitTaskToThreadPool (task);
		return taskFuture;
	}

	private void waitForCommandsCompletion() {
		if (isRunningInParallel)
			checkForCommandsCompletion ();
		else
			return;
	}

	private void checkForCommandsCompletion() {
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

	private void wrapFailuresAndThrowIfNeeded(List<Throwable> failureReasons) {
		if (failureReasons.size () > 0) {
			CommandExecutionException exception = new CommandExecutionException (
					"Command Execution Uncussessful with multiple failures5");
			exception.setMultipleReasons (failureReasons);
			throw exception;
		}
	}

	private void handleCommandExecutorTaskFuture(Future<?> future) {
		try {
			future.get (SysConfig.getInstance ().longTimeoutInMinutes (), TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			BotUtils.propogateInterruptIfExist (e);
		} catch (ExecutionException e) {
			throw new CommandExecutionException (
					"Command Execution Failed With Errors on Server : " + server.id (), e);
		} catch (TimeoutException e) {
			throw new CommandExecutionException ("Command Execution Unfinished on Server : "
					+ server.id (), e);
		}
	}

}
