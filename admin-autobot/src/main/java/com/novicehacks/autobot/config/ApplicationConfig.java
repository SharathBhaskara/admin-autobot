package com.novicehacks.autobot.config;

/**
 * ApplicationConfig is a singleton bean loaded during the launch of the
 * application by {@link ConfigLoader}.
 * 
 * <p>
 * If the configuration for any of the property is not loaded or set to null
 * then this bean will return the default value specified in the
 * {@linkplain ConfigurationProperty}
 * </p>
 * 
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * @see ConfigurationProperty
 * @see ConfigLoader
 */
public class ApplicationConfig {

	private static class ApplicationConfigSingleton {
		private static final ApplicationConfig instance = new ApplicationConfig ();

		public static ApplicationConfig getInstance() {
			return instance;
		}
	}

	private ApplicationConfig () {}

	public static ApplicationConfig getInstance() {
		return ApplicationConfigSingleton.getInstance ();
	}

	private String resourceFolder;
	private String commandFilename;
	private String serverFilename;
	private String executableFilename;
	private String monitorFilename;
	private String tokenSeperator;
	private String shellConsoleFolder;
	private String serverConnectionTimeoutInMins;
	private String executableDelayInHours;
	private String monitoringEnabled;
	private String executableTimeoutInMins;

	/**
	 * unload method will reset all the configuration properties to null values.
	 * 
	 * <p>
	 * Any further call to properties will return defaults after calling this
	 * method until they are reloaded again
	 * </p>
	 */
	public void unload() {
		this.resourceFolder = null;
		this.commandFilename = null;
		this.serverFilename = null;
		this.executableFilename = null;
		this.monitorFilename = null;
		this.tokenSeperator = null;
		this.shellConsoleFolder = null;
		this.serverConnectionTimeoutInMins = null;
		this.executableDelayInHours = null;
		this.monitoringEnabled = null;
		this.executableTimeoutInMins = null;
	}

	public String resourceFolder() {
		return computedValue (this.resourceFolder, ConfigurationProperty.ResourceFolder);
	}

	private String computedValue(String actualValue, ConfigurationProperty property) {
		String computedValue;
		if (actualValue == null)
			computedValue = property.defaultValue ();
		else
			computedValue = actualValue;
		return computedValue;
	}

	public String commandFilename() {
		return computedValue (this.commandFilename, ConfigurationProperty.CommandFileName);
	}

	public String serverFilename() {
		return computedValue (this.serverFilename, ConfigurationProperty.ServerFileName);
	}

	public String executableFilename() {
		return computedValue (this.executableFilename, ConfigurationProperty.ExecutableFileName);
	}

	public String monitorFilename() {
		return computedValue (this.monitorFilename, ConfigurationProperty.MonitorsFileName);
	}

	public String tokenSeperator() {
		return computedValue (this.tokenSeperator, ConfigurationProperty.TokenSeperator);
	}

	public String executableDelayInHours() {
		return computedValue (this.executableDelayInHours, ConfigurationProperty.ExecutableDelay);
	}

	public String shellConsoleFolder() {
		return computedValue (this.shellConsoleFolder, ConfigurationProperty.ShellConsoleFolder);
	}

	public String serverConnectionTimeoutInMins() {
		return computedValue (this.serverConnectionTimeoutInMins,
				ConfigurationProperty.ServerConnectionTimeout);
	}

	public String executableTimeoutInMins() {
		return computedValue (this.executableTimeoutInMins, ConfigurationProperty.ExecutableTimeout);
	}

	public String monitoringEnabled() {
		return computedValue (this.monitoringEnabled, ConfigurationProperty.MonitoringEnabled);
	}

	final void setResourceFolder(String resourceFolder) {
		this.resourceFolder = resourceFolder;
	}

	final void setCommandFilename(String commandFilename) {
		this.commandFilename = commandFilename;
	}

	final void setServerFilename(String serverFilename) {
		this.serverFilename = serverFilename;
	}

	final void setExecutableFilename(String executableFilename) {
		this.executableFilename = executableFilename;
	}

	final void setMonitorFilename(String monitorFilename) {
		this.monitorFilename = monitorFilename;
	}

	final void setTokenSeperator(String tokenSeperator) {
		this.tokenSeperator = tokenSeperator;
	}

	final void setShellConsoleFolder(String shellConsoleFolder) {
		this.shellConsoleFolder = shellConsoleFolder;
	}

	final void setServerConnectionTimeoutInMins(String timeoutInMins) {
		this.serverConnectionTimeoutInMins = timeoutInMins;
	}

	final void setExecutableDelayInHours(String delayInHours) {
		this.executableDelayInHours = delayInHours;
	}

	final void setMonitoringEnabled(String monitoringEnabled) {
		this.monitoringEnabled = monitoringEnabled;
	}

	final void setExecutableTimeoutInMins(String timeoutInMins) {
		this.executableTimeoutInMins = timeoutInMins;
	}

}
