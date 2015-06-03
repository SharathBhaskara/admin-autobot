package com.novicehacks.autobot.core.stubs;

import java.util.Properties;

import com.novicehacks.autobot.config.ConfigLoader;

public class ConfigManagerDelegate {

	private Properties properties;
	private ConfigLoader configManager;

	public ConfigManagerDelegate () {
		this.properties = new Properties ();
	}

	public ConfigManagerDelegate shellConsoleFolder(String folderPath) {
		this.properties.put ("ShellConsoleFolder", folderPath);
		return this;
	}

	public void build() {
		configManager.loadApplicationConfig (properties);
	}

}
