package com.novicehacks.autobot.config;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.management.monitor.Monitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.config.parser.CommandParser;
import com.novicehacks.autobot.config.parser.ExecutableParser;
import com.novicehacks.autobot.config.parser.Parser;
import com.novicehacks.autobot.config.parser.ServerParser;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Executable;
import com.novicehacks.autobot.core.types.Server;

/**
 * Loads the resouce configuration to the {@link ResourceConfig} bean.
 * 
 * <p>
 * Uses the corresponding &lt;Resource&gt; {@link Parser} for reading the
 * configurations and loads them in the bean. <strong>Note:</strong> Uses
 * {@link ConfigParser} to get the information about the location of the
 * resource configuration file
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see Parser
 * @see ResourceConfig
 * @see ConfigParser
 *
 */
public class ResourceConfigLoader {
	public static final long ParsingTimeout = TimeDelay.delayInMins.delay ();
	private ExecutorService executorService;
	private ResourceConfig resourceConfig = ResourceConfig.getInstance ();
	private Set<Server> serverConfig;
	private Set<Command> commandConfig;
	private Set<Executable> executableConfig;
	@SuppressWarnings ("unused")
	private Set<Monitor> monitorsConfig;
	private Future<Set<Server>> serverConfigFuture;
	private Future<Set<Command>> commandConfigFuture;
	private Future<Set<Executable>> executableConfigFuture;
	@SuppressWarnings ("unused")
	private Future<Set<Monitor>> monitorsConfigFuture;

	private static Logger logger = LogManager.getLogger (ResourceConfigLoader.class);

	protected ResourceConfigLoader () {}

	/**
	 * Loads the resource configurations into {@link ResouceConfig} after
	 * parsing the configuration files.
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public ResourceConfig loadResourceConfig() throws InterruptedException, ExecutionException,
			TimeoutException {
		logger.entry ();
		startParsingConfigurations ();
		waitForParsingToComplete ();
		collectConfigFromParsers ();
		loadParsedConfigurations ();
		logger.exit ();
		return resourceConfig;
	}

	private void startParsingConfigurations() {
		executorService = Executors.newCachedThreadPool ();

		startParsingServerConfiguration ();
		startParsingCommandConfiguration ();
		startParsingExecutableConfiguration ();
	}

	private void startParsingServerConfiguration() {
		String serverConfigPath;
		Parser<Server> serverParser;
		serverConfigPath = ConfigParser.getIntance ().serverResource ();
		serverParser = new ServerParser (serverConfigPath);
		serverConfigFuture = executorService.submit (serverParser);
	}

	private void startParsingCommandConfiguration() {
		String commandConfigPath;
		Parser<Command> commandParser;
		commandConfigPath = ConfigParser.getIntance ().serverResource ();
		commandParser = new CommandParser (commandConfigPath);
		commandConfigFuture = executorService.submit (commandParser);
	}

	private void startParsingExecutableConfiguration() {
		String executableConfigPath;
		Parser<Executable> executableParser;
		executableConfigPath = ConfigParser.getIntance ().serverResource ();
		executableParser = new ExecutableParser (executableConfigPath);
		executableConfigFuture = executorService.submit (executableParser);
	}

	private void waitForParsingToComplete() throws InterruptedException {
		executorService.shutdown ();
		executorService.awaitTermination (TimeDelay.largeDelayInMins.delay (), TimeUnit.MINUTES);
	}

	private void collectConfigFromParsers() throws InterruptedException, ExecutionException,
			TimeoutException {
		collectServerConfigurations ();
		collectCommandConfigurations ();
		collectExecutableConfigurations ();
	}

	private void collectServerConfigurations() throws InterruptedException, ExecutionException,
			TimeoutException {
		Set<Server> setOfConfigurations;
		setOfConfigurations = serverConfigFuture.get (ParsingTimeout, TimeUnit.MINUTES);
		this.serverConfig = setOfConfigurations;
	}

	private void collectCommandConfigurations() throws InterruptedException, ExecutionException,
			TimeoutException {
		Set<Command> setOfConfigurations;
		setOfConfigurations = commandConfigFuture.get (ParsingTimeout, TimeUnit.MINUTES);
		this.commandConfig = setOfConfigurations;
	}

	private void collectExecutableConfigurations() throws InterruptedException, ExecutionException,
			TimeoutException {
		Set<Executable> setOfConfigurations;
		setOfConfigurations = executableConfigFuture.get (ParsingTimeout, TimeUnit.MINUTES);
		this.executableConfig = setOfConfigurations;
	}

	private void loadParsedConfigurations() {
		resourceConfig.loadConfig (serverConfig, commandConfig, executableConfig);
	}
}
