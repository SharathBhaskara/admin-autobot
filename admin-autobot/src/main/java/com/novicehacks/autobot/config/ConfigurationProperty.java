
package com.novicehacks.autobot.config;

/**
 * Property definitions and default values for the Autobot application.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public enum ConfigurationProperty {
	ResourceFolder ("ResourceFolder", "."),
	CommandFileName ("CommandFileName", "commands.txt"),
	ServerFileName ("ServerFileName", "servers.txt"),
	ExecutableFileName ("ExecutableFileName", "executables.txt"),
	MonitorsFileName ("MonitorsFileName", "monitors.txt"),
	TokenSeperator ("TokenSeperator", ":"),
	ShellConsoleFolder ("ShellConsoleFolder", "."),
	ServerConnectionTimeout ("ServerConnectionTimeout", "60"),
	ExecutableDelay ("ExecutableDelay", "6"),
	MonitoringEnabled ("MonitoringEnabled", "false"),
	ExecutableTimeout ("ExecutableTimeout", "30");

	private String key;
	private String defaultValue;

	private ConfigurationProperty (String name, String value) {
		this.key = name;
		this.defaultValue = value;
	}

	public String key() {
		return this.key;
	}

	public String defaultValue() {
		return this.defaultValue;
	}

	public static ConfigurationProperty fromKey(String configKey) {
		for (ConfigurationProperty prop : ConfigurationProperty.values ()) {
			if (prop.key.equalsIgnoreCase (configKey))
				return prop;
		}
		return null;
	}
}
