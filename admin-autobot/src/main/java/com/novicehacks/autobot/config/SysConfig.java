package com.novicehacks.autobot.config;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * SysConfig is a singleton, and the properties will be loaded from
 * autobot.properties using the ConfigParser.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public final class SysConfig {

	private Logger logger = LogManager.getLogger(SysConfig.class);

	public static final String ConfigFile = "autobot.properties";
	private static final String ResourceFolder = "ResourceFolder";
	private static final String CommandResource = "CommandFileName";
	private static final String ServerResource = "ServerFileName";
	private static final String ExecutableResource = "ExecutableFileName";
	private static final String MonitorResource = "MonitorsFileName";
	private static final String TokenSeperator = "TokenSeperator";
	private static final String ExecutableDelay = "ExecutableDelay";
	private static final String ShellConsoleFolder = "ShellConsoleFolder";
	private static final String ConnectionTimout = "ServerConnectionTimeout";
	private static final String MaxExecutableTimeout = "MaxExecutableTimeout";
	private static final String MonitoringEnabled = "MonitoringEnabled";

	private String resourceFolder;
	private String commandFileLocation;
	private String serverFileLocation;
	private String monitorFileLocation;
	private String executableFileLocation;
	private String tokenSeperator;
	private String executableDelay;
	private String shellConsoleFolder;
	private String serverConnectionTimeout;
	private String executableTimeout;
	private String monitoringEnabled;
	private static boolean ConfigLoaded = false;

	private SysConfig() {
	}

	/**
	 * Private static inner class for Creating a Singleton object.
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks
	 *
	 */
	private static class SysConfigSingleton {
		private static SysConfig instance = new SysConfig();

		public static SysConfig getInstance() {
			return instance;
		}
	}

	/**
	 * Returns the Singleton instance of the SysConfig object.
	 * 
	 * @return
	 */
	public static SysConfig getInstance() {
		return SysConfigSingleton.getInstance();
	}

	public void loadConfig(Properties properties) {
		logger.entry();
		if (!ConfigLoaded) {
			logger.debug("Loading the SysConfig object");
			ConfigLoaded = true;
			SysConfig config;
			config = SysConfig.getInstance();
			config.setResourceFolder(properties.getProperty(ResourceFolder));
			config.setExecutableFileLocation(properties
					.getProperty(ExecutableResource));
			config.setMonitorFileLocation(properties
					.getProperty(MonitorResource));
			config.setServerFileLocation(properties.getProperty(ServerResource));
			config.setCommandFileLocation(properties
					.getProperty(CommandResource));
			config.setTokenSeperator(properties.getProperty(TokenSeperator));
			config.setExecutableDelay(properties.getProperty(ExecutableDelay));
			config.setShellConsoleFolder(properties
					.getProperty(ShellConsoleFolder));
			config.setServerConnectionTimeout(properties
					.getProperty(ConnectionTimout));
			config.setExecutableTimeout(properties
					.getProperty(MaxExecutableTimeout));
			config.setMonitoringEnabled(properties
					.getProperty(MonitoringEnabled));
		} else {
			logger.warn("Overrding the SysConfig Object is disabled");
		}
		logger.exit();
	}

	public String getResourceFolder() {
		return resourceFolder;
	}

	public String getCommandFileLocation() {
		return commandFileLocation;
	}

	public String getServerFileLocation() {
		return serverFileLocation;
	}

	public String getMonitorFileLocation() {
		return monitorFileLocation;
	}

	public String getExecutableFileLocation() {
		return executableFileLocation;
	}

	private void setResourceFolder(String resourceFolder) {
		this.resourceFolder = resourceFolder;
	}

	private void setCommandFileLocation(String commandFileLocation) {
		this.commandFileLocation = commandFileLocation;
	}

	private void setServerFileLocation(String serverFileLocation) {
		this.serverFileLocation = serverFileLocation;
	}

	private void setMonitorFileLocation(String monitorFileLocation) {
		this.monitorFileLocation = monitorFileLocation;
	}

	private void setExecutableFileLocation(String executableFileLocation) {
		this.executableFileLocation = executableFileLocation;
	}

	public String getTokenSeperator() {
		return tokenSeperator;
	}

	private void setTokenSeperator(String tokenSeperator) {
		this.tokenSeperator = tokenSeperator;
	}

	public String getExecutableDelay() {
		return executableDelay;
	}

	private void setExecutableDelay(String executableDelay) {
		this.executableDelay = executableDelay;
	}

	public String getShellConsoleFolder() {
		return shellConsoleFolder;
	}

	private void setShellConsoleFolder(String shellConsoleFolder) {
		this.shellConsoleFolder = shellConsoleFolder;
	}

	public String getServerConnectionTimeout() {
		return serverConnectionTimeout;
	}

	private void setServerConnectionTimeout(String serverConnectionTimeout) {
		this.serverConnectionTimeout = serverConnectionTimeout;
	}

	/**
	 * Parses the property, if any problem then we will return a default timeout
	 * of 3 minutes.
	 * 
	 * @return
	 */
	public int serverConnectionTimeout() {
		Integer timeout;
		try {
			timeout = Integer.parseInt(this.serverConnectionTimeout);
		} catch (Exception ex) {
			if (Thread.interrupted()) {
				Thread.currentThread().interrupt();
			}
			timeout = 3 * 60;
		}
		return timeout;
	}

	/**
	 * Maximum Execution Period for all the executables defined, Can be
	 * configured in autobot.properties. Defaults to 60 minutes if not
	 * specified.
	 * 
	 * @return maximum execution period for all the executables.
	 */
	public int MaxExecutionPeriod() {
		Integer timeout;
		try {
			timeout = Integer.parseInt(this.executableTimeout);
		} catch (Exception ex) {
			if (Thread.interrupted()) {
				Thread.currentThread().interrupt();
			}
			timeout = 60;
		}
		return timeout;
	}

	public String getExecutableTimeout() {
		return executableTimeout;
	}

	private void setExecutableTimeout(String executableTimeout) {
		this.executableTimeout = executableTimeout;
	}

	/**
	 * Execution delay for all executables defined, Can be configured in
	 * autobot.properties. Defaults to 6 hours if not specified.
	 * 
	 * @return Delay of execution of executables.
	 */
	public long ExecutableDelay() {
		Integer timeout;
		try {
			timeout = Integer.parseInt(this.executableTimeout);
		} catch (Exception ex) {
			if (Thread.interrupted()) {
				Thread.currentThread().interrupt();
			}
			timeout = 6;
		}
		return timeout;
	}

	public String getMonitoringEnabled() {
		return monitoringEnabled;
	}

	private void setMonitoringEnabled(String monitoringEnabled) {
		this.monitoringEnabled = monitoringEnabled;
	}

	/**
	 * Defaults to false
	 * 
	 * @return
	 */
	public boolean MonitoringEnabled() {
		Boolean status;
		try {
			status = new Boolean(monitoringEnabled);
		} catch (NullPointerException ex) {
			status = false;
		}
		return status;
	}

}
