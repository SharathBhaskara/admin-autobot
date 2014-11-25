package com.novicehacks.autobot.types;

/**
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

}
