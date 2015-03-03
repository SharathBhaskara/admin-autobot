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
import com.novicehacks.autobot.core.RunnableTask;
import com.novicehacks.autobot.core.ThreadManager;
import com.novicehacks.autobot.ssh.ServerCommandProcessorTask;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

public class CommandExecutorTask implements RunnableTask {
	private static final long ServerExecutionTimeoutInMinutes = 10;
	private Collection<Future<?>> executableFutures;
	private ServerExecutableCommandMap executableMap;
	private RuntimeException errorCollector;
	private boolean threadStarted = false;
	private Logger logger = LogManager.getLogger (CommandExecutorTask.class);

	public CommandExecutorTask () {
		this.executableFutures = new LinkedList<Future<?>> ();
		this.errorCollector = new RuntimeException ();
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
		try {
			loadExecutableMap ();
		} catch (InterruptedException ex) {
			this.logger.error ("Thread Interrupted", ex);
			BotUtils.PropogateInterruptIfExist (ex);
		}
	}

	private void loadExecutableMap() throws InterruptedException {
		ServerExecutableCommandGenerator executableCommandGenerator;
		executableCommandGenerator = ServerExecutableCommandGenerator.getInstance ();
		this.executableMap = executableCommandGenerator.generateServerCommandMap ();
	}

	private void startExecution() {
		for (Server server : this.executableMap.keySet ())
			executeCommandsOnServerAndSaveFutures (server);
	}

	private void executeCommandsOnServerAndSaveFutures(Server server) {
		Future<?> executableFuture;
		Collection<Command> commands;
		commands = this.executableMap.get (server);
		executableFuture = createAndSubmitCommandProcessor (server, commands);
		this.executableFutures.add (executableFuture);
	}

	private Future<?> createAndSubmitCommandProcessor(Server server, Collection<Command> commands) {
		Future<?> executableFuture;
		ServerCommandProcessorTask serverCommandProcessor;
		serverCommandProcessor = new ServerCommandProcessorTask (server, commands);
		executableFuture = ThreadManager.getInstance ().submitTaskToThreadPool (
				serverCommandProcessor);
		return executableFuture;
	}

	private void waitForCompletion() {
		Iterator<Future<?>> executableFutureIterator;
		executableFutureIterator = this.executableFutures.iterator ();
		while (executableFutureIterator.hasNext ()) {
			waitForFutureCompletion (executableFutureIterator.next ());
		}
	}

	private void waitForFutureCompletion(Future<?> next) {
		this.logger.entry ();
		try {
			next.get (ServerExecutionTimeoutInMinutes, TimeUnit.MINUTES);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			handleFutureExceptions (e);
		}
		this.logger.exit ();
	}

	private void handleFutureExceptions(Exception e) {
		this.logger.error (e);
		BotUtils.PropogateInterruptIfExist (e);
		this.errorCollector.addSuppressed (e);
	}

	private void alarmIfExceptionsCaught() {
		if (this.errorCollector.getSuppressed ().length > 0)
			throw this.errorCollector;
	}

	@Override
	public final boolean isThreadStarted() {
		return this.threadStarted;
	}
}
