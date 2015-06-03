package com.novicehacks.autobot.config;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * loads the {@link ApplicationConfig} bean from the properties
 * 
 * <p>
 * The properties will be supplied by the {@link ConfigParser}, after reading the
 * content from the property files.
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * @see ConfigParser
 */
public class ConfigLoader {
	private Logger logger = LogManager.getLogger (ConfigLoader.class);
	private ApplicationConfig config = ApplicationConfig.getInstance ();
	private Properties props;

	/**
	 * Will use the properties, to load the application config, to be used along
	 * with {@link #loadApplicationConfig()}
	 * 
	 * @param props
	 * @see #loadApplicationConfig()
	 */
	protected ConfigLoader (Properties properties) {
		this.props = properties;
	}

	/**
	 * Creates a dummy property set, to be used along with
	 * {@link #loadApplicationConfig(Properties)}
	 * 
	 * @see #loadApplicationConfig(Properties)
	 */
	protected ConfigLoader () {
		this.props = new Properties ();
	}

	/**
	 * Loads the {@link ApplicationConfig} frmo the properties defined during
	 * initialization.
	 */
	public void loadApplicationConfig() {
		for (String configKey : props.stringPropertyNames ()) {
			setConfigForKey (configKey);
		}
	}

	/**
	 * Loads the {@link ApplicationConfig} from the properties passed.
	 * 
	 * @param props
	 */
	public void loadApplicationConfig(Properties props) {
		for (String configKey : props.stringPropertyNames ()) {
			setConfigForKey (configKey);
		}
	}

	protected void setConfigForKey(String configKey) {
		ConfigurationProperty configProperty = ConfigurationProperty.fromKey (configKey);
		if (configProperty == null)
			logger.warn ("Unknown property defined in the system config properties {}", configKey);
		else
			identifyAndSetConfigProperty (configProperty, props.getProperty (configKey));
	}

	private void identifyAndSetConfigProperty(ConfigurationProperty configProperty, String value) {
		switch (configProperty) {
		case CommandFileName:
			config.setCommandFilename (value);
			break;
		case ExecutableDelay:
			config.setExecutableDelayInHours (value);
			break;
		case ExecutableFileName:
			config.setExecutableFilename (value);
			break;
		case ExecutableTimeout:
			config.setExecutableTimeoutInMins (value);
			break;
		case MonitoringEnabled:
			config.setMonitoringEnabled (value);
			break;
		case MonitorsFileName:
			config.setMonitorFilename (value);
			break;
		case ResourceFolder:
			config.setResourceFolder (value);
			break;
		case ServerConnectionTimeout:
			config.setServerConnectionTimeoutInMins (value);
			break;
		case ServerFileName:
			config.setServerFilename (value);
			break;
		case ShellConsoleFolder:
			config.setShellConsoleFolder (value);
			break;
		case TokenSeperator:
			config.setTokenSeperator (value);
			break;
		}
	}

}
