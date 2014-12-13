package com.novicehacks.autobot.config;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.config.parser.CommandParser;
import com.novicehacks.autobot.config.parser.ConfigParser;
import com.novicehacks.autobot.config.parser.ExecutableParser;
import com.novicehacks.autobot.config.parser.MonitorParser;
import com.novicehacks.autobot.config.parser.Parser;
import com.novicehacks.autobot.config.parser.ServerParser;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Executable;
import com.novicehacks.autobot.types.Monitor;
import com.novicehacks.autobot.types.Server;

/**
 * This is the API exposed to the Autobot, which will load all the resources for
 * the application. From the autobot.properties file.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class AutobotConfigManager {
	/**
	 * ReadTimeOut in minutes is used to wait for the Resource Parsers to
	 * complete and return the Collection of objects.
	 */
	public static final int ReadTimeOut = 3;
	private static Logger logger = LogManager
			.getLogger(AutobotConfigManager.class);

	/**
	 * First call to this method will initialize the SysConfig object, so no
	 * need to call this method every time. Since SysConfig is a singleton the
	 * state of this will be retained until otherwise deleted.
	 * 
	 * @return
	 */
	public static SysConfig loadSystemConfig() {
		return ConfigParser.getIntance().systemConfig();
	}

	/**
	 * <p>
	 * First call to this method will initialize the ResourceConfig object, so
	 * no need to call this method every time. Since ResourceConfig is a
	 * singleton the state of this will be retained until otherwise deleted.
	 * </p>
	 * <p>
	 * <Strong>Note:</strong> Loading {@link ResourceConfig} will automatically
	 * load {@link SysConfig}
	 * </p>
	 * 
	 * @return Configuration instance with the loaded config. Is a singleton
	 *         instance
	 * @throws TimeoutException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public static ResourceConfig loadResourceConfig()
			throws InterruptedException, ExecutionException, TimeoutException {
		Parser<Server> serverParser;
		Parser<Command> commandParser;
		Parser<Executable> executableParser;
		Parser<Monitor> monitorParser;
		Future<Set<Server>> serverInfo;
		Future<Set<Command>> commandInfo;
		Future<Set<Executable>> executableInfo;
		Future<Set<Monitor>> monitorInfo;
		Set<Server> servers;
		Set<Command> commands;
		Set<Executable> executables;
		Set<Monitor> monitors;
		ExecutorService executor;
		ConfigParser parser;
		logger.entry();
		parser = ConfigParser.getIntance();

		serverParser = new ServerParser(parser.serverResource());
		commandParser = new CommandParser(parser.commandResource());
		executableParser = new ExecutableParser(parser.executableResource());
		monitorParser = new MonitorParser(parser.monitorResource());
		logger.debug("Submitting the parsers to the executor");
		executor = Executors.newCachedThreadPool();
		serverInfo = executor.submit(serverParser);
		commandInfo = executor.submit(commandParser);
		executableInfo = executor.submit(executableParser);
		monitorInfo = executor.submit(monitorParser);
		executor.shutdown();
		logger.debug("Awaiting the completion of threads after shutdown.");
		servers = serverInfo.get(ReadTimeOut, TimeUnit.MINUTES);
		commands = commandInfo.get(ReadTimeOut, TimeUnit.MINUTES);
		executables = executableInfo.get(ReadTimeOut, TimeUnit.MINUTES);
		monitors = monitorInfo.get(ReadTimeOut, TimeUnit.MINUTES);
		logger.debug("Setting the data into the Config Object");

		ResourceConfig.getInstance().loadConfig(servers, commands, executables,
				monitors);
		logger.exit();
		return ResourceConfig.getInstance();
	}
}
