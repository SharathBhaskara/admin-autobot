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
import com.novicehacks.autobot.ssh.ServerCommandProcessor;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

public class CommandExecutorTask implements Runnable {
	private static final long			ServerExecutionTimeoutInMinutes	= 10;
	private Collection<Future<?>>		executableFutures;
	private ServerExecutableCommandMap	executableMap;
	private RuntimeException			errorCollector;
	private Logger						logger							= LogManager
																				.getLogger (CommandExecutorTask.class);

	public CommandExecutorTask () {
		executableFutures = new LinkedList<Future<?>> ();
		errorCollector = new RuntimeException ();
	}

	@Override
	public void run() {
		loadExecutables ();
		startExecution ();
		waitForCompletion ();
		alarmIfExceptionsCaught ();
	}

	private void loadExecutables() {
		try {
			loadExecutableMap ();
		} catch (InterruptedException ex) {
			logger.error (ex);
			BotUtils.PropogateInterruptIfExist (ex);
		}
	}

	private void loadExecutableMap() throws InterruptedException {
		ServerExecutableCommandGenerator executableCommandGenerator;
		executableCommandGenerator = ServerExecutableCommandGenerator.getInstance ();
		executableMap = executableCommandGenerator.generateServerCommandMap ();
	}

	private void startExecution() {
		for (Server server : executableMap.keySet ())
			executeCommandsOnServerAndSaveFutures (server);
	}

	private void executeCommandsOnServerAndSaveFutures(Server server) {
		Future<?> executableFuture;
		Collection<Command> commands;
		commands = executableMap.get (server);
		executableFuture = createAndSubmitCommandProcessor (server, commands);
		this.executableFutures.add (executableFuture);
	}

	private Future<?> createAndSubmitCommandProcessor(Server server, Collection<Command> commands) {
		Future<?> executableFuture;
		ServerCommandProcessor serverCommandProcessor;
		serverCommandProcessor = new ServerCommandProcessor (server, commands);
		executableFuture = ThreadManager.getInstance ().submitTaskToThreadPool (
				serverCommandProcessor);
		return executableFuture;
	}

	private void waitForCompletion() {
		Iterator<Future<?>> executableFutureIterator;
		executableFutureIterator = executableFutures.iterator ();
		while (executableFutureIterator.hasNext ()) {
			waitForFutureCompletion (executableFutureIterator.next ());
		}
	}

	private void waitForFutureCompletion(Future<?> next) {
		logger.entry ();
		try {
			next.get (ServerExecutionTimeoutInMinutes, TimeUnit.MINUTES);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			handleFutureExceptions (e);
		}
		logger.exit ();
	}

	private void handleFutureExceptions(Exception e) {
		logger.error (e);
		BotUtils.PropogateInterruptIfExist (e);
		errorCollector.addSuppressed (e);
	}

	private void alarmIfExceptionsCaught() {
		if (errorCollector.getSuppressed ().length > 0)
			throw errorCollector;
	}
}
