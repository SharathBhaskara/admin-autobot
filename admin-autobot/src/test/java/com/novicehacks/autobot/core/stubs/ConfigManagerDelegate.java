package com.novicehacks.autobot.core.stubs;

import java.util.Properties;

import com.novicehacks.autobot.config.ApplicationConfigLoader;

public class ConfigManagerDelegate {

	private Properties properties;
	private ApplicationConfigLoader configManager;

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
