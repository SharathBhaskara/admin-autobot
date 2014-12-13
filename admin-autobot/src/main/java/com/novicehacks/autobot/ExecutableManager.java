package com.novicehacks.autobot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.config.ResourceConfig;
import com.novicehacks.autobot.config.SysConfig;
import com.novicehacks.autobot.shell.ShellExecutor;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Executable;
import com.novicehacks.autobot.types.Server;

/**
 * Executable Manager thread is a singleton instance, will load the executables
 * once, and will be used in a scheduled thread pool to execute the commands in
 * a given delay.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class ExecutableManager implements Runnable {
	private Map<Server, Set<Command>> executableMap;
	private Logger logger = LogManager.getLogger(ExecutableManager.class);

	private ExecutableManager() {
		loadExecutables();
	}

	/**
	 * static Inner class for Singleton implementation.
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks
	 *
	 */
	private static class ExecutableManagerSingleton {
		private final static ExecutableManager instance = new ExecutableManager();

		private static ExecutableManager getInstance() {
			return instance;
		}
	}

	public static ExecutableManager getInstance() {
		return ExecutableManagerSingleton.getInstance();
	}

	/**
	 * Parse the Executable Set from the ResourceConfig, and create a map of
	 * server and commands to be executed. Will be loaded in the object
	 * initialization and used in every execution.
	 */
	public void loadExecutables() {
		ResourceConfig config;
		Set<Executable> executables;
		Map<String, Command> commands;
		Map<String, Server> servers;
		Map<String, Set<String>> executableIdMap;
		Map<Server, Set<Command>> executableMap;

		logger.entry();
		config = ResourceConfig.getInstance();
		executables = config.executables();
		commands = config.commandMap();
		servers = config.serverMap();
		/*
		 * TreeMap is used, as containsKey is used for almost every insert, and
		 * no. of inserts will be less than the no. of searches.
		 */
		executableIdMap = new TreeMap<String, Set<String>>();
		for (Executable executable : executables) {
			String serverId;
			String commandId;
			Set<String> commandIdSet;
			serverId = executable.getServerId();
			commandId = executable.getCommandId();
			if (executableIdMap.containsKey(serverId)) {
				commandIdSet = new HashSet<String>();
				commandIdSet.add(commandId);
			} else {
				commandIdSet = executableIdMap.get(serverId);
				commandIdSet.add(commandId);
			}
			executableIdMap.put(serverId, commandIdSet);
		}
		/*
		 * Create Server and Command Map from the executableMap.
		 */
		executableMap = new HashMap<Server, Set<Command>>();
		for (String serverid : executableIdMap.keySet()) {
			Server server = servers.get(serverid);
			Set<Command> commandSet = new TreeSet<Command>();
			for (String commandId : executableIdMap.get(serverid)) {
				Command command = commands.get(commandId);
				commandSet.add(command);
			}
			executableMap.put(server, commandSet);
		}
		this.executableMap = executableMap;
		logger.exit();
	}

	@Override
	public void run() {
		/*
		 * Run the Executables in Seperate Threads.
		 */
		logger.entry();
		ExecutorService service = Executors.newCachedThreadPool();
		for (Server server : executableMap.keySet()) {
			ShellExecutor executor;
			executor = new ShellExecutor(server, executableMap.get(server));
			service.submit(executor);
		}
		service.shutdown();
		try {
			boolean status = service.awaitTermination(SysConfig.getInstance()
					.MaxExecutionPeriod(), TimeUnit.MINUTES);
			if (!status) {
				logger.warn("Executables not completed, after the specified time, hence killing them");
				service.shutdownNow();
			}
		} catch (InterruptedException e) {
			logger.error("Interrupted while waiting for the Executables to complete");
			if (Thread.interrupted()) {
				Thread.currentThread().interrupt();
			}
		}
		logger.exit();
	}
}
