package com.novicehacks.autobot.types;

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

}
