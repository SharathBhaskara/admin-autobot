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

import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Executable;
import com.novicehacks.autobot.core.types.Server;

/**
 * Loads the resouce configuration to the {@link ResourceConfig} bean.
 * 
 * <p>
 * uses the config loaders {@link ServerConfigLoader},
 * {@link CommandConfigLoader} and {@link ExecutableConfigLoader} to load the
 * user configuration into {@link ResourceConfig}. <strong>Note:</strong> Before
 * loading the {@link ResourceConfig} the {@link ApplicationConfig} needs to be
 * loaded
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see ResourceConfigParser
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

	ResourceConfigLoader () {}

	/**
	 * Loads the resource configurations into {@link ResouceConfig} after
	 * parsing the configuration files.
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	ResourceConfig loadResourceConfig() throws InterruptedException, ExecutionException,
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
		executorService = createThreadPool ();

		startParsingServerConfiguration ();
		startParsingCommandConfiguration ();
		startParsingExecutableConfiguration ();
	}

	ExecutorService createThreadPool() {
		return Executors.newCachedThreadPool ();
	}

	private void startParsingServerConfiguration() {
		ServerConfigLoader serverParser;
		serverParser = getServerConfigLoader ();
		serverConfigFuture = executorService.submit (serverParser);
	}

	ServerConfigLoader getServerConfigLoader() {
		return new ServerConfigLoader ();
	}

	private void startParsingCommandConfiguration() {
		CommandConfigLoader commandParser;
		commandParser = getCommandConfigLoader ();
		commandConfigFuture = executorService.submit (commandParser);
	}

	CommandConfigLoader getCommandConfigLoader() {
		return new CommandConfigLoader ();
	}

	private void startParsingExecutableConfiguration() {
		ExecutableConfigLoader executableParser;
		executableParser = getExecutableConfigLoader ();
		executableConfigFuture = executorService.submit (executableParser);
	}

	ExecutableConfigLoader getExecutableConfigLoader() {
		return new ExecutableConfigLoader ();
	}

	private void waitForParsingToComplete() throws InterruptedException {
		executorService.shutdown ();
		executorService.awaitTermination (TimeDelay.largeDelayInMins.delay (), TimeUnit.MINUTES);
	}

	private void collectConfigFromParsers() throws InterruptedException, ExecutionException,
			TimeoutException {
		this.serverConfig = getFutureObjectConfig (serverConfigFuture);
		this.commandConfig = getFutureObjectConfig (commandConfigFuture);
		this.executableConfig = getFutureObjectConfig (executableConfigFuture);
	}

	<T> Set<T> getFutureObjectConfig(Future<Set<T>> futureObject) throws InterruptedException,
			ExecutionException, TimeoutException {
		Set<T> setOfConfigurations;
		setOfConfigurations = futureObject.get (ParsingTimeout, TimeUnit.MINUTES);
		return setOfConfigurations;
	}

	Set<Server> getServerConfig() throws InterruptedException, ExecutionException, TimeoutException {
		Set<Server> setOfConfigurations;
		setOfConfigurations = serverConfigFuture.get (ParsingTimeout, TimeUnit.MINUTES);
		return setOfConfigurations;
	}

	Set<Command> getCommandConfig() throws InterruptedException, ExecutionException,
			TimeoutException {
		Set<Command> setOfConfigurations;
		setOfConfigurations = commandConfigFuture.get (ParsingTimeout, TimeUnit.MINUTES);
		return setOfConfigurations;
	}

	Set<Executable> getExecutableConfig() throws InterruptedException, ExecutionException,
			TimeoutException {
		Set<Executable> setOfConfigurations;
		setOfConfigurations = executableConfigFuture.get (ParsingTimeout, TimeUnit.MINUTES);
		return setOfConfigurations;
	}

	private void loadParsedConfigurations() {
		resourceConfig.loadConfig (serverConfig, commandConfig, executableConfig);
	}

}
