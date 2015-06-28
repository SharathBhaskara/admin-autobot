package com.novicehacks.autobot.executor;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.config.ResourceConfig;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Executable;
import com.novicehacks.autobot.core.types.Server;

public final class ServerExecutableMapGenerator {

	private Set<Executable> executables;
	private Map<String, Server> servers;
	private Map<String, Command> commands;
	private ServerExecutableMap executableMap;
	private Lock generatorLock = new ReentrantLock ();
	private static int GeneratorTimeoutInSeconds = 15;
	private Logger logger = LogManager.getLogger (ServerExecutableMapGenerator.class);

	/**
	 * Singleton implementation
	 */
	ServerExecutableMapGenerator () {
		ResourceConfig resourceConfig;
		resourceConfig = getResourceConfig ();
		this.servers = resourceConfig.serverMap ();
		this.commands = resourceConfig.commandMap ();
		this.executables = resourceConfig.executables ();
		this.executableMap = new ServerExecutableMap ();
	}

	private static class ServerExecutableCommandGeneratorSingleton {
		private static ServerExecutableMapGenerator instance = new ServerExecutableMapGenerator ();

		private static ServerExecutableMapGenerator getInstance() {
			return instance;
		}
	}

	public static ServerExecutableMapGenerator getSharedInstance() {
		return ServerExecutableCommandGeneratorSingleton.getInstance ();
	}

	ResourceConfig getResourceConfig() {
		return ResourceConfig.getInstance ();
	}

	/**
	 * Same as calling {@link #generateServerCommandMap(false)}.
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public ServerExecutableMap generateServerCommandMap() throws InterruptedException {
		return generateServerCommandMap (false);
	}

	/**
	 * Creates a new map of server executable map or returns if one exist
	 * already.
	 * 
	 * @param forced
	 *        if set to true will create the map if when one exist already.
	 * @return
	 * @throws InterruptedException
	 */
	public ServerExecutableMap generateServerCommandMap(boolean forced)
			throws InterruptedException {
		this.logger.entry ();
		this.generatorLock.tryLock (GeneratorTimeoutInSeconds, TimeUnit.SECONDS);
		populateServerCommandMapIfNeeded (forced);
		this.generatorLock.unlock ();
		this.logger.exit (this.executableMap);
		return this.executableMap;
	}

	private void populateServerCommandMapIfNeeded(boolean forced) {
		if (forced || this.executableMap == null || this.executableMap.size () == 0)
			populateServerCommandMap ();
	}

	private void populateServerCommandMap() {
		String serverId;
		String commandId;
		for (Executable executable : this.executables) {
			serverId = executable.getServerId ();
			commandId = executable.getCommandId ();
			addCommandToMap (serverId, commandId);
		}
	}

	private void addCommandToMap(String serverId, String commandId) {
		Server server;
		Command command;
		server = this.servers.get (serverId);
		command = this.commands.get (commandId);
		this.executableMap.put (server, command);
	}
}
