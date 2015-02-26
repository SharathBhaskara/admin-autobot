package com.novicehacks.autobot.ssh.logger;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.DefaultFileWriter;
import com.novicehacks.autobot.core.FileWriter;
import com.novicehacks.autobot.logger.OutputFooterService;
import com.novicehacks.autobot.logger.OutputHeaderService;
import com.novicehacks.autobot.logger.OutputLoggerTask;
import com.novicehacks.autobot.ssh.exception.UnixOutputLoggingException;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

public class ShellOutputLoggerTask implements OutputLoggerTask {
	private Server server;
	private Command command;
	private String commandOutput;
	private String content = "";
	private boolean headerFooterRequired = true;
	private Logger logger = LogManager.getLogger (ShellOutputLoggerTask.class);

	public ShellOutputLoggerTask (Server unixServer, Command unixCommand, String commandOutput) {
		validateParams (unixServer, unixCommand);
		this.server = unixServer;
		this.command = unixCommand;
		this.commandOutput = commandOutput;
		this.headerFooterRequired = true;
	}

	public ShellOutputLoggerTask (String commandOutput) {
		this (commandOutput, false);
	}

	public ShellOutputLoggerTask (String commandOutput, boolean headerFooterRequired) {
		validateParams (commandOutput);
		this.commandOutput = commandOutput;
		this.headerFooterRequired = headerFooterRequired;
	}

	private void validateParams(String commandOutput) {
		if (BotUtils.HasNullReferences (commandOutput)) {
			throw new UnixOutputLoggingException ("Invalid Parameters Passed",
					new IllegalArgumentException ());
		}
	}

	private void validateParams(Server unixServer, Command unixCommand) {
		if (BotUtils.HasNullReferences (unixServer, unixCommand)) {
			throw new UnixOutputLoggingException ("Invalid Parameters Passed",
					new IllegalArgumentException ());
		}
	}

	@Override
	public OutputHeaderService headerService() {
		OutputHeaderService service;
		service = new ShellOuptutHeaderService (this.server, this.command);
		return service;
	}

	@Override
	public OutputFooterService footerService() {
		OutputFooterService service;
		service = new ShellOutputFooterService ();
		return service;
	}

	@Override
	public String getFormattedContent() {
		if (this.content == null || this.content.equals (""))
			prepareContent ();
		return this.content;
	}

	private void prepareContent() {
		if (this.headerFooterRequired)
			createFormattedContent ();
		else
			createRawContent ();
	}

	private void createRawContent() {
		this.content = this.commandOutput;
	}

	private void createFormattedContent() {
		this.content = headerService ().header ();
		this.content += getFormattedCommandOutput ();
		this.content += footerService ().footer ();
	}

	private String getFormattedCommandOutput() {
		this.logger.entry ();
		StringBuilder contentBuffer;
		contentBuffer = new StringBuilder ();
		contentBuffer.append (this.commandOutput);
		contentBuffer.append (BotUtils.newLine ());
		this.logger.exit ();
		return contentBuffer.toString ();
	}

	@Override
	public void run() {
		String formattedContent;
		this.logger.entry ();
		Path filePath = prepareToWriteContent ();
		formattedContent = getFormattedContent ();
		writeContent (filePath, formattedContent);
		this.logger.exit ();
	}

	private Path prepareToWriteContent() {
		return ShellOutputLoggerTaskHelper.createFileIfNeededAndGetPath ();
	}

	private void writeContent(Path filePath, String content) {
		FileWriter writerService = writerService (filePath);
		try {
			writerService.write (content);
		} catch (IOException e) {
			this.logger.error ("Content logging on server " + this.server.id () + " and command "
					+ this.command.id () + " failed.", e);
			throw new UnixOutputLoggingException ("Content logging on server " + this.server.id ()
					+ " and command " + this.command.id () + " failed.", e);
		}
	}

	private FileWriter writerService(Path filePath) {
		FileWriter service = new DefaultFileWriter (filePath);
		return service;
	}
}
