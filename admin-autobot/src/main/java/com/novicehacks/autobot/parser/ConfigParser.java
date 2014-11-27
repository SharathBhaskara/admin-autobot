package com.novicehacks.autobot.parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.SysConfig;

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
	private static final String ResourceFolder = "ResourceFolder";
	private static final String CommandResource = "CommandFileName";
	private static final String ServerResource = "ServerFileName";
	private static final String ExecutableResource = "ExecutableFileName";
	private static final String MonitorResource = "MonitorsFileName";
	private static final String TokenSeperator = "TokenSeperator";
	private static final String ExecutableDelay = "ExecutableDelay";

	private Logger logger = LogManager.getLogger(ConfigParser.class);
	private Properties properties;

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
	 * 
	 * Will call the loadConfig method to load the Basic configurations at the
	 * time of instantiation.
	 * 
	 * @throws IllegalStateException
	 *             when unable to load the configurations
	 */
	private ConfigParser() throws IllegalStateException {
		loadConfig();
	}

	/**
	 * Returns the singleton instance of the config parser object.
	 * 
	 * @return
	 */
	public static ConfigParser getIntance() {
		return ConfigParserSingleton.instance();
	}

	/**
	 * Loads the properties object, with the configuration in the
	 * autobot.properties file.
	 * 
	 * throws IllegalStateException when unable to create the properties from
	 * the resource file
	 */
	private void loadConfig() {
		logger.entry();
		logger.debug("Loading the system config from : {}", ConfigFile);
		InputStream _is = ClassLoader.getSystemResourceAsStream(ConfigFile);
		Properties properties;
		properties = new Properties();
		try {
			properties.load(_is);
		} catch (IOException e) {
			logger.error("Unable to load autobot properties.", e);
			throw new IllegalStateException("Loading System Config Failed", e);
		}
		this.properties = properties;
		logger.exit();
	}

	/**
	 * The absoulute path of the file where server information is defined.
	 * 
	 * @return Absolute path of the server resource
	 * 
	 */
	public String serverResource() {
		Path path;
		path = Paths.get(this.properties.getProperty(ResourceFolder),
				properties.getProperty(ServerResource));
		return path.toString();
	}

	/**
	 * The absoulute path of the file where Command information is defined.
	 * 
	 * @return Absolute path of the Command resource
	 * 
	 */
	public String commandResource() {
		Path path;
		path = Paths.get(this.properties.getProperty(ResourceFolder),
				properties.getProperty(CommandResource));
		return path.toString();
	}

	/**
	 * The absoulute path of the file where Executable information is defined.
	 * 
	 * @return Absolute path of the server resource
	 * 
	 */
	public String executableResource() {
		Path path;
		path = Paths.get(this.properties.getProperty(ResourceFolder),
				properties.getProperty(ExecutableResource));
		return path.toString();
	}

	/**
	 * The absoulute path of the file where Monitors information is defined.
	 * 
	 * @return Absolute path of the monitors resource
	 * 
	 */
	public String monitorResource() {
		Path path;
		path = Paths.get(this.properties.getProperty(ResourceFolder),
				properties.getProperty(MonitorResource));
		return path.toString();
	}

	/**
	 * 
	 * @return
	 */
	public SysConfig systemConfig() {
		SysConfig config;
		config = SysConfig.getInstance();
		config.setResourceFolder(this.properties.getProperty(ResourceFolder));
		config.setExecutableFileLocation(this.properties
				.getProperty(ExecutableResource));
		config.setMonitorFileLocation(this.properties
				.getProperty(MonitorResource));
		config.setServerFileLocation(this.properties
				.getProperty(ServerResource));
		config.setCommandFileLocation(this.properties
				.getProperty(CommandResource));
		config.setTokenSeperator(this.properties.getProperty(TokenSeperator));
		config.setExecutableDelay(this.properties.getProperty(ExecutableDelay));
		return config;
	}
}
