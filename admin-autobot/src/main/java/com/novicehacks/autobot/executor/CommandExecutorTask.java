package com.novicehacks.autobot.executor;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.ThreadManager;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.RunnableTask;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.executor.ssh.SSHCommandExecutorServiceTask;

/**
 * It will execute the commmands on servers from the {@link ServerExecutableMap}
 * asynchronously.
 * 
 * <p>
 * Uses executable map generator to create the {@link ServerExecutableMap} from
 * {@link ResourceConfig}. And executes commands on each server in a separate
 * thread.
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * @see ServerExecutableMapGenerator
 * @see SSHCommandExecutorServiceTask
 */
public class CommandExecutorTask implements RunnableTask {
	private static final long ServerExecutionTimeoutInMinutes = 10;
	private Collection<Future<?>> executableFutures;
	private ServerExecutableMap executableMap;
	private RuntimeException errorCollector;
	private boolean threadStarted = false;
	private Logger logger = LogManager.getLogger (CommandExecutorTask.class);

	public CommandExecutorTask () {
		this.executableFutures = new LinkedList<Future<?>> ();
		this.errorCollector = new RuntimeException ("Exception while executing commands on server");
	}

	@Override
	public void run() {
		this.threadStarted = true;
		loadExecutables ();
		startExecution ();
		waitForCompletion ();
		alarmIfExceptionsCaught ();
	}

	private void loadExecutables() {
		logger.entry ("Started loading server executable map");
		try {
			ServerExecutableMapGenerator executableCommandGenerator = getExecutableGeneratorInstance ();
			this.executableMap = executableCommandGenerator.generateServerCommandMap ();
		} catch (InterruptedException ex) {
			this.logger.error ("Thread Interrupted while creating server executable map", ex);
			BotUtils.PropogateInterruptIfExist (ex);
		}
		logger.exit ();
	}

	private ServerExecutableMapGenerator getExecutableGeneratorInstance() {
		return ServerExecutableMapGenerator.getSharedInstance ();
	}

	private void startExecution() {
		logger.entry ("Server executable execution started");
		for (Server server : this.executableMap.keySet ())
			executeCommandsOnServerAndSaveFutures (server);
		logger.exit ();
	}

	private void executeCommandsOnServerAndSaveFutures(Server server) {
		logger.entry (server);
		Collection<Command> commands = this.executableMap.get (server);
		Future<?> executableFuture = createAndSubmitCommandProcessor (server, commands);
		this.executableFutures.add (executableFuture);
		logger.exit ();
	}

	private Future<?> createAndSubmitCommandProcessor(Server server, Collection<Command> commands) {
		RunnableTask commandProcessorTask = getCommandExecutorTaskInstance (server, commands);
		Future<?> executableFuture = getThreadManagerInstance ().submitTaskToThreadPool (
				commandProcessorTask);
		return executableFuture;
	}

	RunnableTask getCommandExecutorTaskInstance(Server server, Collection<Command> commands) {
		return new SSHCommandExecutorServiceTask (server, commands);
	}

	ThreadManager getThreadManagerInstance() {
		return ThreadManager.getInstance ();
	}

	private void waitForCompletion() {
		logger.entry ();
		Iterator<Future<?>> executableFutureIterator;
		executableFutureIterator = this.executableFutures.iterator ();
		while (executableFutureIterator.hasNext ()) {
			waitForFutureCompletion (executableFutureIterator.next ());
		}
		logger.exit ();
	}

	private void waitForFutureCompletion(Future<?> taskFuture) {
		this.logger.entry (taskFuture);
		try {
			taskFuture.get (ServerExecutionTimeoutInMinutes, TimeUnit.MINUTES);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			handleEaskExecutiaonExceptions (e);
		}
		this.logger.exit ();
	}

	private void handleEaskExecutiaonExceptions(Exception e) {
		this.logger.error (e);
		BotUtils.PropogateInterruptIfExist (e);
		this.errorCollector.addSuppressed (e);
		logger.exit ();
	}

	private void alarmIfExceptionsCaught() {
		logger.entry ("Count of suppressed exception: {}",
				this.errorCollector.getSuppressed ().length);
		if (this.errorCollector.getSuppressed ().length > 0)
			throw this.errorCollector;
		logger.exit ();
	}

	@Override
	public final boolean isThreadStarted() {
		return this.threadStarted;
	}
}
