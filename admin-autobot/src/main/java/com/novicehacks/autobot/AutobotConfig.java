package com.novicehacks.autobot;

import java.util.Set;

import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Monitor;
import com.novicehacks.autobot.types.Server;
import com.novicehacks.autobot.types.SysConfig;

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
public final class AutobotConfig implements Cloneable {

	private static boolean configLoaded = false;
	private Set<Server> servers;
	private Set<Command> commands;
	private Set<String> executables;
	private Set<Monitor> monitors;

	private AutobotConfig() {
	}

	/**
	 * Singleton implementation using an inner class.
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks
	 *
	 */
	private static class AutobotConfigSingleton {
		private static AutobotConfig instance = new AutobotConfig();

		public static AutobotConfig getInstance() {
			return instance;
		}
	}

	/**
	 * Loads the configurations for this application. Tyring to invoke this
	 * method more than once will throw IllegalStateException.
	 * <p>
	 * <em>Throwing the exception is being reviewed and might be modified in the next releases</em>
	 * </p>
	 * 
	 * @param config
	 *            The System Configuration object containing system properties
	 * @param serverSet
	 *            Set of servers where the commands to be executed
	 * @param command
	 *            Set of command to execute on the servers
	 * @param executables
	 *            Set of commands that has to be executed in a scheduled
	 *            interval
	 * @param monitors
	 *            Set of commands to be monitored and reported if crossing the
	 *            threshold
	 */
	public void loadConfig(SysConfig config, Set<Server> serverSet,
			Set<Command> command, Set<String> executables, Set<Monitor> monitors) {
		if (!configLoaded) {

		} else {
			throw new IllegalStateException(
					"Load Config cannot be called multiple times");
		}
	}

	/**
	 * Returns the singleton instance of the AutobotConfig object.
	 * 
	 * @return Configuration Object will the system and resource configurations.
	 */
	public static AutobotConfig getInstance() {
		return AutobotConfigSingleton.getInstance();
	}

}
