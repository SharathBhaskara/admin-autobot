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

	private Logger logger = LogManager.getLogger (SysConfig.class);

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

	/**
	 * Private Constructor for the singleton implementation.
	 */
	private SysConfig () {}

	/**
	 * Private static inner class for Creating a Singleton object.
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks
	 *
	 */
	private static class SysConfigSingleton {
		private static SysConfig instance = new SysConfig ();

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
		return SysConfigSingleton.getInstance ();
	}

	/**
	 * Loads the configuration from the properties, if and only if the
	 * configuration is not already loaded.
	 * 
	 * @param properties
	 */
	public void loadConfig(Properties properties) {
		this.logger.entry ();
		if (!ConfigLoaded) {
			this.logger.debug ("Loading the SysConfig object");
			ConfigLoaded = true;
			SysConfig config;
			config = SysConfig.getInstance ();
			config.setResourceFolder (properties.getProperty (ResourceFolder));
			config.setExecutableFileLocation (properties.getProperty (ExecutableResource));
			config.setMonitorFileLocation (properties.getProperty (MonitorResource));
			config.setServerFileLocation (properties.getProperty (ServerResource));
			config.setCommandFileLocation (properties.getProperty (CommandResource));
			config.setTokenSeperator (properties.getProperty (TokenSeperator));
			config.setExecutableDelay (properties.getProperty (ExecutableDelay));
			config.setShellConsoleFolder (properties.getProperty (ShellConsoleFolder));
			config.setServerConnectionTimeout (properties.getProperty (ConnectionTimout));
			config.setExecutableTimeout (properties.getProperty (MaxExecutableTimeout));
			config.setMonitoringEnabled (properties.getProperty (MonitoringEnabled));
		} else {
			this.logger.warn ("Overrding the SysConfig Object is disabled");
		}
		this.logger.exit ();
	}

	/**
	 * Returns the Path to the folder where Resources can be found
	 * 
	 * @return
	 */
	public String getResourceFolder() {
		return this.resourceFolder;
	}

	/**
	 * Returns the path to the file where the Autobot Commands can be found
	 * 
	 * @return
	 */
	public String getCommandFileLocation() {
		return this.commandFileLocation;
	}

	/**
	 * Returns the path to the file where the Autobot Servers can be found
	 * 
	 * @return
	 */
	public String getServerFileLocation() {
		return this.serverFileLocation;
	}

	/**
	 * Returns the path to the file where the Autobot Monitors can be found
	 * 
	 * @return
	 */
	public String getMonitorFileLocation() {
		return this.monitorFileLocation;
	}

	/**
	 * Returns the path to the file where the Executable command information can
	 * be found
	 * 
	 * @return
	 */
	public String getExecutableFileLocation() {
		return this.executableFileLocation;
	}

	/**
	 * Sets the path of the resource folder where the resources can be found.
	 * 
	 * @param resourceFolder
	 */
	private void setResourceFolder(String resourceFolder) {
		this.resourceFolder = resourceFolder;
	}

	/**
	 * Sets the path of the Command File for the autobot application.
	 * 
	 * @param commandFileLocation
	 */
	private void setCommandFileLocation(String commandFileLocation) {
		this.commandFileLocation = commandFileLocation;
	}

	/**
	 * Sets the path of the Servers file for the Autobot Application.
	 * 
	 * @param serverFileLocation
	 */
	private void setServerFileLocation(String serverFileLocation) {
		this.serverFileLocation = serverFileLocation;
	}

	/**
	 * Sets the path of the Monitors file for the Autobot application.
	 * 
	 * @param monitorFileLocation
	 */
	private void setMonitorFileLocation(String monitorFileLocation) {
		this.monitorFileLocation = monitorFileLocation;
	}

	/**
	 * Sets the path of the Executable commands file for the Autobot
	 * Application.
	 * 
	 * @param executableFileLocation
	 */
	private void setExecutableFileLocation(String executableFileLocation) {
		this.executableFileLocation = executableFileLocation;
	}

	/**
	 * Returns the token separator property used in compiling the tokens from
	 * the resource files.
	 * 
	 * @return
	 */
	public String getTokenSeperator() {
		return this.tokenSeperator;
	}

	/**
	 * Sets the token separator to be used for compiling the Autobot
	 * Configuration objects, from the resource files
	 * 
	 * @param tokenSeperator
	 */
	private void setTokenSeperator(String tokenSeperator) {
		this.tokenSeperator = tokenSeperator;
	}

	/**
	 * Returns the time delay set in the configuration, for executing the
	 * executable commands
	 * 
	 * @return
	 */
	public String getExecutableDelay() {
		return this.executableDelay;
	}

	/**
	 * Sets the time delay for executing the executable commands by the Autobot
	 * application.
	 * 
	 * @param executableDelay
	 */
	private void setExecutableDelay(String executableDelay) {
		this.executableDelay = executableDelay;
	}

	/**
	 * Returns the path of the folder where the output from the remote boxes
	 * will be logged.
	 * 
	 * @return
	 */
	public String getShellConsoleFolder() {
		return this.shellConsoleFolder;
	}

	/**
	 * Sets the path of the folder where the output from the remote boxes to be
	 * logged by the autobot application
	 * 
	 * @param shellConsoleFolder
	 */
	private void setShellConsoleFolder(String shellConsoleFolder) {
		this.shellConsoleFolder = shellConsoleFolder;
	}

	/**
	 * Returns the connection time out delay, used when connecting to the remote
	 * box using SSH / Telnet.
	 * 
	 * @return
	 */
	public String getServerConnectionTimeout() {
		return this.serverConnectionTimeout;
	}

	/**
	 * Sets the connection timeout delay, used when connecting to the remote box
	 * using SSH/ Telnet
	 * 
	 * @param serverConnectionTimeout
	 */
	private void setServerConnectionTimeout(String serverConnectionTimeout) {
		this.serverConnectionTimeout = serverConnectionTimeout;
	}

	/**
	 * Parses the property, if any problem then we will return a default timeout
	 * of 180 seconds
	 * 
	 * @return
	 */
	public int serverConnectionTimeout() {
		Integer timeout;
		try {
			timeout = Integer.parseInt (this.serverConnectionTimeout);
		} catch (Exception ex) {
			if (Thread.interrupted ()) {
				Thread.currentThread ().interrupt ();
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
			timeout = Integer.parseInt (this.executableTimeout);
		} catch (Exception ex) {
			if (Thread.interrupted ()) {
				Thread.currentThread ().interrupt ();
			}
			timeout = 60;
		}
		return timeout;
	}

	/**
	 * Returns The amount of time that the thread needs to wait for the
	 * executable to be complete.
	 * 
	 * @return
	 */
	public String getExecutableTimeout() {
		return this.executableTimeout;
	}

	/**
	 * Sets the amount of time that the thread needs to wait for the executable
	 * to be complete.
	 * 
	 * @param executableTimeout
	 */
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
			timeout = Integer.parseInt (this.executableTimeout);
		} catch (Exception ex) {
			if (Thread.interrupted ()) {
				Thread.currentThread ().interrupt ();
			}
			timeout = 6;
		}
		return timeout;
	}

	/**
	 * Returns whether monitoring feature of the autobot applications is enabled
	 * or not.
	 * 
	 * @return
	 */
	public String getMonitoringEnabled() {
		return this.monitoringEnabled;
	}

	/**
	 * Set the monitoring feature of the application to enabled
	 * 
	 * @param monitoringEnabled
	 *        true if needed to enabled false otherwise
	 */
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
			status = new Boolean (this.monitoringEnabled);
		} catch (NullPointerException ex) {
			status = false;
		}
		return status;
	}

	/**
	 * DefaultTimeDelay has default values for the time delays to be used across
	 * the application, to maintain consistencies.
	 * 
	 * @author Sharath Chand Bhaskara for NoviceHacks!
	 *
	 */
	public enum DefaultTimeDelay {
		LongTimeInMinutes (10),
		ShortTimeInMinutes (3),
		TinyTimeInMinutes (1),
		LongTimeInSeconds (300),
		ShortTimeInSeconds (120),
		TinyTimeInSeconds (30);

		private int delay;

		DefaultTimeDelay (int time) {
			this.delay = time;
		}

		public int delay() {
			return this.delay;
		}

	}

	/**
	 * Returns LongTimeInMinutes
	 * 
	 * @return
	 */
	public long longTimeoutInMinutes() {
		return DefaultTimeDelay.LongTimeInMinutes.delay ();
	}

	/**
	 * Returns ShortTimeoutInSeconds
	 * 
	 * @return
	 */
	public long shortTimeoutInSeconds() {
		return DefaultTimeDelay.ShortTimeInSeconds.delay ();
	}

}
