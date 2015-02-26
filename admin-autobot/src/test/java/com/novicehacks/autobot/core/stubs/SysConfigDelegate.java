package com.novicehacks.autobot.core.stubs;

import java.util.Properties;

import com.novicehacks.autobot.config.SysConfig;

public class SysConfigDelegate {

	private SysConfig config;
	private Properties properties;

	public SysConfigDelegate () {
		this.config = SysConfig.getInstance ();
		this.properties = new Properties ();
	}

	public SysConfigDelegate shellConsoleFolder(String folderPath) {
		this.properties.put ("ShellConsoleFolder", folderPath);
		return this;
	}

	public void build() {
		this.config.loadConfig (this.properties);
	}

}
