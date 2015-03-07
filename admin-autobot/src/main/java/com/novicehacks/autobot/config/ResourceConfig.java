package com.novicehacks.autobot.config;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.management.monitor.Monitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Executable;
import com.novicehacks.autobot.core.types.Server;

/**
 * <p>
 * Will hold the configuration object, and other object for using across the
 * application where ever needed. This is a singleton instance and also an
 * immutable object. Hence once the data is loaded, there will be no
 * modifications for this object.
 * </p>
 * <p>
 * Below are some of the configurations that this Config bean hold
 * <ul>
 * <li>Configuration (System Configuration)</li>
 * <li>Collection of Servers</li>
 * <li>Collection of Commands</li>
 * <li>Collection of Executables</li>
 * <li>Collection of Monitors</li>
 * </ul>
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public final class ResourceConfig {

	private static AtomicBoolean ConfigLoaded = new AtomicBoolean (false);
	private Set<Server> servers;
	private Map<String, Server> serverMap;
	private Map<String, Command> commandMap;
	private Set<Command> commands;
	private Set<Executable> executables;
	private Set<Monitor> monitors;

	private Logger logger = LogManager.getLogger (ResourceConfig.class);

	/**
	 * Private Constructor, will make no instance created outside this class.
	 */
	private ResourceConfig () {}

	/**
	 * Loads the configurations for this application. Tyring to invoke this
	 * method more than once will throw IllegalStateException.
	 * <p>
	 * <em>Throwing the exception is being reviewed and might be modified in the next releases</em>
	 * </p>
	 * 
	 * @param serverSet
	 *        Set of servers where the commands to be executed
	 * @param command
	 *        Set of command to execute on the servers
	 * @param executables
	 *        Set of commands that has to be executed in a scheduled
	 *        interval
	 * @param monitors
	 *        Set of commands to be monitored and reported if crossing the
	 *        threshold
	 */
	protected void loadConfig(	Set<Server> servers,
								Set<Command> commands,
								Set<Executable> executables) {
		this.logger.entry ();
		if (!ConfigLoaded.get ()) {
			ConfigLoaded.set (true);
			this.servers = servers;
			this.commands = commands;
			this.executables = executables;
			this.commandMap = BotUtils.CreateMap (commands);
			this.serverMap = BotUtils.CreateMap (servers);
		} else {
			// do nothing
			// throw new IllegalStateException
			// ("Load Config cannot be called multiple times");
		}
		this.logger.exit ();
	}

	/**
	 * Returns the singleton instance of the AutobotConfig object.
	 * 
	 * @return Configuration Object will the system and resource configurations.
	 */
	public static ResourceConfig getInstance() {
		return ResourceConfigSingleton.getInstance ();
	}

	/**
	 * A Set of Servers parsed from the resource file.
	 * 
	 * @return
	 */
	public Set<Server> servers() {
		return this.servers;
	}

	/**
	 * A Set of Commands parsed from the resource file
	 * 
	 * @return
	 */
	public Set<Command> commands() {
		return this.commands;
	}

	/**
	 * A Set of Executables parsed from the resource file
	 * 
	 * @return
	 */
	public Set<Executable> executables() {
		return this.executables;
	}

	/**
	 * A Set of Monitors parsed from the resource file.
	 * 
	 * @return
	 */
	public Set<Monitor> monitors() {
		return this.monitors;
	}

	/**
	 * Singleton implementation using an inner class.
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks
	 *
	 */
	private static class ResourceConfigSingleton {
		private static ResourceConfig instance = new ResourceConfig ();

		public static ResourceConfig getInstance() {
			return instance;
		}
	}

	/**
	 * A Map of Servers identified by serverId as key.
	 * 
	 * @return
	 */
	public Map<String, Server> serverMap() {
		return this.serverMap;
	}

	/**
	 * A Map of Commands identified by commandId as key.
	 * 
	 * @return
	 */
	public Map<String, Command> commandMap() {
		return this.commandMap;
	}

}
