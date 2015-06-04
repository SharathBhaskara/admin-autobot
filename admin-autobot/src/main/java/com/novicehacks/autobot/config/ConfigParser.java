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
	public static final String ConfigFileName = "autobot.properties";
	private static final String ResourceFolderName = "ResourceFolder";
	private static final String CommandResourceFileName = "CommandFileName";
	private static final String ServerResourceFileName = "ServerFileName";
	private static final String ExecutableResourceFileName = "ExecutableFileName";
	private static final String MonitorResourceFileName = "MonitorsFileName";

	private Logger logger = LogManager.getLogger (ConfigParser.class);
	private Properties properties;

	private static class ConfigParserSingleton {
		private static ConfigParser instance = new ConfigParser ();

		public static ConfigParser instance() {
			return instance;
		}
	}

	/**
	 * Singleton object handler.
	 * 
	 * @return object instance
	 */
	public static ConfigParser getIntance() {
		return ConfigParserSingleton.instance ();
	}

	/**
	 * ConfigParser constructor will load the properties required for
	 * ApplicationConfig during intialization.
	 * 
	 * @throws IllegalStateException
	 *         when unable to load the properties from config file
	 */
	private ConfigParser () throws IllegalStateException {
		loadProperitesFromFile ();
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
	private void loadProperitesFromFile() {
		logger.entry ();
		logger.debug ("Loading the ApplicationConfig from : {}", ConfigFileName);
		InputStream _is = ClassLoader.getSystemResourceAsStream (ConfigFileName);

		this.properties = getPropertiesFromInputStram (_is);
		logger.trace ("Count of properties loaded from configFile: {}", this.properties.size ());
		logger.exit ();
	}

	private Properties getPropertiesFromInputStram(InputStream _is) {
		Properties properties;
		properties = new Properties ();
		try {
			properties.load (_is);
		} catch (IOException e) {
			logger.error ("Unable to load autobot properties.", e);
			throw new IllegalStateException ("Loading System Config Failed", e);
		}
		return properties;
	}

	/**
	 * Returns a cloned properties set to the calling method, so that the
	 * properties remain unchanged once loaded.
	 * 
	 * @return
	 */
	public Properties getConfigProperties() {
		Properties props;
		props = new Properties ();
		props.putAll (this.properties);
		return props;
	}

	/**
	 * Handler to return the location of server resource from config
	 * properties.
	 * 
	 * @return Absolute path of the server resource
	 * 
	 */
	public String serverResource() {
		String fileName = ServerResourceFileName;
		String filePath = getAbsolutePathAsString (fileName);
		return filePath;
	}

	private String getAbsolutePathAsString(String fileName) {
		String resourceFolder = this.properties.getProperty (ResourceFolderName);
		String resourceName = this.properties.getProperty (fileName);
		Path path = Paths.get (resourceFolder, resourceName);
		return path.toString ();
	}

	/**
	 * Handler to return the location of command resource from config
	 * properties.
	 * 
	 * @return Absolute path of the Command resource
	 * 
	 */
	public String commandResource() {
		String fileName = CommandResourceFileName;
		String filePath = getAbsolutePathAsString (fileName);
		return filePath;
	}

	/**
	 * Handler to return the location of executable resource from config
	 * properties.
	 * 
	 * @return Absolute path of the server resource
	 * 
	 */
	public String executableResource() {
		String fileName = ExecutableResourceFileName;
		String filePath = getAbsolutePathAsString (fileName);
		return filePath;
	}

	/**
	 * Handler to return the location of monitor resource from config
	 * properties.
	 * 
	 * @return Absolute path of the monitors resource
	 * 
	 */
	public String monitorResource() {
		String fileName = MonitorResourceFileName;
		String filePath = getAbsolutePathAsString (fileName);
		return filePath;
	}

}
