package com.novicehacks.autobot.config;

/**
 * SysConfig is a singleton, and the properties will be loaded from
 * autobot.properties using the ConfigParser.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public final class SysConfig {

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

	private SysConfig() {
	}

	private static class SysConfigSingleton {
		private static SysConfig instance = new SysConfig();

		public static SysConfig getInstance() {
			return instance;
		}
	}

	public static SysConfig getInstance() {
		return SysConfigSingleton.getInstance();
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

	public void setResourceFolder(String resourceFolder) {
		this.resourceFolder = resourceFolder;
	}

	public void setCommandFileLocation(String commandFileLocation) {
		this.commandFileLocation = commandFileLocation;
	}

	public void setServerFileLocation(String serverFileLocation) {
		this.serverFileLocation = serverFileLocation;
	}

	public void setMonitorFileLocation(String monitorFileLocation) {
		this.monitorFileLocation = monitorFileLocation;
	}

	public void setExecutableFileLocation(String executableFileLocation) {
		this.executableFileLocation = executableFileLocation;
	}

	public String getTokenSeperator() {
		return tokenSeperator;
	}

	public void setTokenSeperator(String tokenSeperator) {
		this.tokenSeperator = tokenSeperator;
	}

	public String getExecutableDelay() {
		return executableDelay;
	}

	public void setExecutableDelay(String executableDelay) {
		this.executableDelay = executableDelay;
	}

	public String getShellConsoleFolder() {
		return shellConsoleFolder;
	}

	public void setShellConsoleFolder(String shellConsoleFolder) {
		this.shellConsoleFolder = shellConsoleFolder;
	}

	public String getServerConnectionTimeout() {
		return serverConnectionTimeout;
	}

	public void setServerConnectionTimeout(String serverConnectionTimeout) {
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

	public void setExecutableTimeout(String executableTimeout) {
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

	public void setMonitoringEnabled(String monitoringEnabled) {
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
