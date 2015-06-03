package com.novicehacks.autobot.config;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * AppConfigManager loads the ApplicationConfig property from the properties
 * object.
 * 
 * <p>
 * The properties will be supplied by the ConfigParser, after reading the
 * content from the property files.
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * @see ConfigParser
 */
public class AppConfigManager {
	private Logger logger = LogManager.getLogger (AppConfigManager.class);
	private ApplicationConfig config = ApplicationConfig.getInstance ();
	private Properties props;

	protected AppConfigManager (Properties props) {
		this.props = props;
	}

	public void loadApplicationConfig() {
		for (String configKey : props.stringPropertyNames ()) {
			setConfigForKey (configKey);
		}
	}

	private void setConfigForKey(String configKey) {
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
