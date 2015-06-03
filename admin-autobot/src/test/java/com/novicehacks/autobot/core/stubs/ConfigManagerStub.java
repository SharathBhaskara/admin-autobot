package com.novicehacks.autobot.core.stubs;

import java.util.Properties;

import com.novicehacks.autobot.config.ConfigManager;

public class ConfigManagerStub extends ConfigManager {

	private Properties properties;

	public ConfigManagerStub () {
		super (null);
		this.properties = new Properties ();
	}

	public ConfigManagerStub shellConsoleFolder(String folderPath) {
		this.properties.put ("ShellConsoleFolder", folderPath);
		return this;
	}

	public void build() {
		this.loadApplicationConfig ();
	}

	@Override
	public void loadApplicationConfig() {
		for (String propKey : properties.stringPropertyNames ())
			setConfigForKey (propKey);
	}

}
