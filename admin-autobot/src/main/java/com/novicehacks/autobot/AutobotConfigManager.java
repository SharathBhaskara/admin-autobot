package com.novicehacks.autobot;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.parser.CommandParser;
import com.novicehacks.autobot.parser.ConfigParser;
import com.novicehacks.autobot.parser.ExecutableParser;
import com.novicehacks.autobot.parser.MonitorParser;
import com.novicehacks.autobot.parser.Parser;
import com.novicehacks.autobot.parser.ServerParser;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Executable;
import com.novicehacks.autobot.types.Monitor;
import com.novicehacks.autobot.types.Server;
import com.novicehacks.autobot.types.SysConfig;

/**
 * This is the Acutal API exposed to the clients, which will load all the
 * resources for the application. From the autobot.properties file.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class AutobotConfigManager {
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
	 * 
	 * First call to this method will initialize the ResourceConfig object, so
	 * no need to call this method every time. Since ResourceConfig is a
	 * singleton the state of this will be retained until otherwise deleted.
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
		SysConfig config;
		ExecutorService executor;
		ConfigParser parser;
		logger.entry();
		parser = ConfigParser.getIntance();
		executor = Executors.newCachedThreadPool();
		config = parser.systemConfig();
		serverParser = new ServerParser(parser.serverResource());
		commandParser = new CommandParser(parser.commandResource());
		executableParser = new ExecutableParser(parser.executableResource());
		monitorParser = new MonitorParser(parser.monitorResource());
		logger.debug("Submitting the parsers to the executor");
		serverInfo = executor.submit(serverParser);
		commandInfo = executor.submit(commandParser);
		executableInfo = executor.submit(executableParser);
		monitorInfo = executor.submit(monitorParser);
		executor.shutdown();
		logger.debug("Awaiting the completion of threads after shutdown.");
		servers = serverInfo.get(1, TimeUnit.MINUTES);
		commands = commandInfo.get(1, TimeUnit.MINUTES);
		executables = executableInfo.get(1, TimeUnit.MINUTES);
		monitors = monitorInfo.get(1, TimeUnit.MINUTES);
		logger.debug("Setting the data into the Config Object");

		ResourceConfig.getInstance().loadConfig(config, servers, commands,
				executables, monitors);
		logger.exit();
		return ResourceConfig.getInstance();
	}
}
