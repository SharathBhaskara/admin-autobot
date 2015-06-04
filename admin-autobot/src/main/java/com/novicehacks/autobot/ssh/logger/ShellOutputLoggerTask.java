package com.novicehacks.autobot.ssh.logger;

import java.nio.file.Path;

import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.services.OutputFooterService;
import com.novicehacks.autobot.services.OutputHeaderService;
import com.novicehacks.autobot.services.impl.DefaultOutputLoggerTask;

public class ShellOutputLoggerTask extends DefaultOutputLoggerTask {
	private Server server;
	private Command command;

	public ShellOutputLoggerTask (Server unixServer, Command unixCommand, String commandOutput) {
		super (unixServer, unixCommand, commandOutput);
		this.server = unixServer;
		this.command = unixCommand;
	}

	public ShellOutputLoggerTask (String commandOutput) {
		this (commandOutput, false);
	}

	public ShellOutputLoggerTask (String commandOutput, boolean headerFooterRequired) {
		super (commandOutput, headerFooterRequired);
	}

	@Override
	public OutputHeaderService headerService() {
		OutputHeaderService service;
		service = ShellOutputLoggerTaskHelper.getInstance ().headerService (this.server,
				this.command);
		return service;
	}

	@Override
	public OutputFooterService footerService() {
		OutputFooterService service;
		service = ShellOutputLoggerTaskHelper.getInstance ().footerService ();
		return service;
	}

	@Override
	public Path logLocation() {
		Path path;
		path = ShellOutputLoggerTaskHelper.getInstance ().createFileIfNeededAndGetPath ();
		return path;
	}
}
