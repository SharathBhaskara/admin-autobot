package com.novicehacks.autobot.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	/**
	 * All the below constants define the property names in autobot.properties,
	 * and are used to populate the state of SysConfig
	 */
	public static final String ConfigFile = "autobot.properties";
	private static final String ResourceFolder = "ResourceFolder";
	private static final String CommandResource = "CommandFileName";
	private static final String ServerResource = "ServerFileName";
	private static final String ExecutableResource = "ExecutableFileName";
	private static final String MonitorResource = "MonitorsFileName";

	private Logger logger = LogManager.getLogger (ConfigParser.class);
	private Properties properties;

	/**
	 * Singleton Implementation using static inner class
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks
	 *
	 */
	private static class ConfigParserSingleton {

		private static ConfigParser instance = new ConfigParser ();

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
	 *         when unable to load the configurations
	 */
	private ConfigParser () throws IllegalStateException {
		loadConfig ();
	}

	/**
	 * Returns the singleton instance of the com.novicehacks.autobot.config
	 * parser object.
	 * 
	 * @return
	 */
	public static ConfigParser getIntance() {
		return ConfigParserSingleton.instance ();
	}

	/**
	 * <p>
	 * Loads the properties object, with the configuration in the
	 * autobot.properties file.
	 * </p>
	 * <p>
	 * Will also Load the SystemConfig object
	 * </p>
	 * throws IllegalStateException when unable to create the properties from
	 * the resource file
	 */
	private void loadConfig() {
		logger.entry ();
		logger.debug ("Loading the system com.novicehacks.autobot.config from : {}", ConfigFile);
		InputStream _is = ClassLoader.getSystemResourceAsStream (ConfigFile);
		Properties properties;
		properties = new Properties ();
		try {
			properties.load (_is);
		} catch (IOException e) {
			logger.error ("Unable to load autobot properties.", e);
			throw new IllegalStateException ("Loading System Config Failed", e);
		}
		this.properties = properties;
		/* Load System Config Object also from Properties */
		new ConfigManager(properties).loadApplicationConfig ();
		logger.exit ();
	}

	/**
	 * The absoulute path of the file where server information is defined.
	 * 
	 * @return Absolute path of the server resource
	 * 
	 */
	public String serverResource() {
		Path path;
		path = Paths.get (this.properties.getProperty (ResourceFolder),
				properties.getProperty (ServerResource));
		return path.toString ();
	}

	/**
	 * The absoulute path of the file where Command information is defined.
	 * 
	 * @return Absolute path of the Command resource
	 * 
	 */
	public String commandResource() {
		Path path;
		path = Paths.get (this.properties.getProperty (ResourceFolder),
				properties.getProperty (CommandResource));
		return path.toString ();
	}

	/**
	 * The absoulute path of the file where Executable information is defined.
	 * 
	 * @return Absolute path of the server resource
	 * 
	 */
	public String executableResource() {
		Path path;
		path = Paths.get (this.properties.getProperty (ResourceFolder),
				properties.getProperty (ExecutableResource));
		return path.toString ();
	}

	/**
	 * The absoulute path of the file where Monitors information is defined.
	 * 
	 * @return Absolute path of the monitors resource
	 * 
	 */
	public String monitorResource() {
		Path path;
		path = Paths.get (this.properties.getProperty (ResourceFolder),
				properties.getProperty (MonitorResource));
		return path.toString ();
	}

	/**
	 * Creates the SysConfig object from the properties loaded.
	 * 
	 * @return
	 */
	public ApplicationConfig applicationConfig() {
		/*
		 * Returns the singleton instance which is loaded already durint the
		 * class instantiation
		 */
		return ApplicationConfig.getInstance ();
	}
}
