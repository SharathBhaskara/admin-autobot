package com.novicehacks.autobot.parser;

import java.io.File;

/**
 * ConfigParser is a singleton implementation. It is really of no use to create
 * multiple instances for loading the same configuration file, hence singleton
 * implementation.
 * 
 * It will load the configuration object with the "autobot.properties".
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class ConfigParser {
	public static final String ConfigFile = "autobot.properties";

	/**
	 * Singleton Implementation using static inner class
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks
	 *
	 */
	private static class ConfigParserSingleton {
		private static ConfigParser instance = new ConfigParser();

		public static ConfigParser instance() {
			return instance;
		}
	}

	/**
	 * Private Config Parser Constructor.
	 */
	private ConfigParser() {
	}

	/**
	 * Returns the singleton instance of the config parser object.
	 * 
	 * @return
	 */
	public static ConfigParser getIntance() {
		return ConfigParserSingleton.instance();
	}

	public void loadConfig() {
		File file = new File(ConfigFile);

	}
}
