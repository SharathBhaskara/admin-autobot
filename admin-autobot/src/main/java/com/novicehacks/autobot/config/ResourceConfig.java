package com.novicehacks.autobot.config;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.management.monitor.Monitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Executable;
import com.novicehacks.autobot.core.types.Server;

/**
 * Holds the command, server and executable configurations specified by the
 * user.
 * <p>
 * Uses {@link ResourceConfigLoader} to parse the configurations and saves the
 * formatted contents.
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see ResourceConfigLoader
 * @see Server
 * @see Command
 * @see Executable
 */

public final class ResourceConfig {

	private Set<Server> servers;
	private Set<Command> commands;
	private Set<Executable> executables;
	@SuppressWarnings ("unused")
	private Set<Monitor> monitors;
	private Map<String, Server> serverMap;
	private Map<String, Command> commandMap;

	private Logger logger = LogManager.getLogger (ResourceConfig.class);

	/**
	 * Singleton implementation
	 */
	private ResourceConfig () {}

	private static class ResourceConfigSingleton {
		private static ResourceConfig instance = new ResourceConfig ();

		public static ResourceConfig getInstance() {
			return instance;
		}
	}

	public static ResourceConfig getInstance() {
		return ResourceConfigSingleton.getInstance ();
	}

	/**
	 * Saves the state of the bean with unmodifiable collections.
	 * 
	 * <p>
	 * <strong>Note:</strong> Also creates some helper collections (map) for
	 * server and commands, and also convert them to unmodifiable maps.
	 * </p>
	 * 
	 * @param serverSet
	 * @param command
	 * @param executables
	 */
	protected void loadConfig(	Set<Server> servers,
								Set<Command> commands,
								Set<Executable> executables) {
		this.logger.entry ();
		this.servers = Collections.unmodifiableSet (servers);
		this.commands = Collections.unmodifiableSet (commands);
		this.executables = Collections.unmodifiableSet (executables);
		this.commandMap = BotUtils.CreateMap (commands);
		this.serverMap = BotUtils.CreateMap (servers);
		this.commandMap = Collections.unmodifiableMap (this.commandMap);
		this.serverMap = Collections.unmodifiableMap (this.serverMap);
		this.logger.exit ();
	}

	public Set<Server> servers() {
		return this.servers;
	}

	public Set<Command> commands() {
		return this.commands;
	}

	public Set<Executable> executables() {
		return this.executables;
	}

	public Map<String, Server> serverMap() {
		return this.serverMap;
	}

	public Map<String, Command> commandMap() {
		return this.commandMap;
	}

}
