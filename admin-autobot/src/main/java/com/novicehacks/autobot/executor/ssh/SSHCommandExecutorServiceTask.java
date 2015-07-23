package com.novicehacks.autobot.executor.ssh;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.RunnableTask;
import com.novicehacks.autobot.core.types.Server;

/**
 * Runnable wrapper implementation of {@link SSHCommandExecutorService} so that
 * it can run in multiple threads.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * @see SSHCommandExecutorService
 */
public final class SSHCommandExecutorServiceTask extends SSHCommandExecutorService implements
		RunnableTask {

	private boolean threadStarted = false;
	private Logger logger = LogManager.getLogger (SSHCommandExecutorServiceTask.class);

	/**
	 * @param unixServer
	 * @param unixCommands
	 * @throws NullPointerException
	 *         if unixCommands parameter is null
	 * @throws IllegalArgumentException
	 *         if unixServer parameter is null
	 */
	public SSHCommandExecutorServiceTask (	final Server unixServer,
											final Collection<Command> unixCommands) {
		super (unixServer, unixCommands);
	}

	/**
	 * @param unixServer
	 * @param unixCommands
	 * @throws IllegalArgumentException
	 *         if either of the parameters are having null values
	 */
	public SSHCommandExecutorServiceTask (final Server unixServer, final Command... unixCommands) {
		super (unixServer, unixCommands);
	}

	@Override
	public void run() {
		this.threadStarted = true;
		this.logger.entry ();
		executeCommands ();
		this.logger.exit ();
	}

	@Override
	public final boolean isThreadStarted() {
		return this.threadStarted;
	}

}
