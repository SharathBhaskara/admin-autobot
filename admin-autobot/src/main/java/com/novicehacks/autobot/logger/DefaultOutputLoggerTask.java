package com.novicehacks.autobot.logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.ContentWriterService;
import com.novicehacks.autobot.core.DefaultContentWriterService;
import com.novicehacks.autobot.core.RunnableTask;
import com.novicehacks.autobot.ssh.exception.UnixOutputLoggingException;
import com.novicehacks.autobot.ssh.logger.ShellOutputLoggerTask;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

public class DefaultOutputLoggerTask implements OutputLogger, RunnableTask {
	private Server server;
	private Command command;
	private String commandOutput;
	private String content = "";
	private boolean isThreadStarted = false;
	private boolean headerFooterRequired = true;
	private Logger logger = LogManager.getLogger (ShellOutputLoggerTask.class);

	protected DefaultOutputLoggerTask (Server unixServer, Command unixCommand, String commandOutput) {
		validateParams (unixServer, unixCommand);
		this.server = unixServer;
		this.command = unixCommand;
		this.commandOutput = commandOutput;
		this.headerFooterRequired = true;
	}

	protected DefaultOutputLoggerTask (String commandOutput) {
		this (commandOutput, false);
	}

	protected DefaultOutputLoggerTask (String commandOutput, boolean headerFooterRequired) {
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
	public void run() {
		this.isThreadStarted = true;
		this.logger.entry ();
		String formattedContent;
		formattedContent = getFormattedContent ();
		writeContent (formattedContent);
		this.logger.exit ();
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

	@Override
	public OutputHeaderService headerService() {
		OutputHeaderService service;
		service = new DefaultOutputHeaderService (this.server, this.command);
		return service;
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
	public OutputFooterService footerService() {
		OutputFooterService service;
		service = new DefaultOutputFooterService ();
		return service;
	}

	private void writeContent(String content) {
		ContentWriterService writerService = writerService ();
		try {
			createLogFileIfNeeded ();
			writerService.write (logLocation (), content);
		} catch (IOException e) {
			this.logger.error ("Content logging on server " + this.server.id () + " and command "
					+ this.command.id () + " failed.", e);
			throw new UnixOutputLoggingException ("Content logging on server " + this.server.id ()
					+ " and command " + this.command.id () + " failed.", e);
		}
	}

	private void createLogFileIfNeeded() throws IOException {
		Path path = logLocation ();
		if (Files.notExists (path)) {
			Files.createDirectories (path);
			Files.createFile (path);
		}
	}

	@Override
	public Path logLocation() {
		Path path;
		path = Paths.get (".").resolve ("temp-output").resolve ("logconsole.log");
		return path;
	}

	@Override
	public ContentWriterService writerService() {
		ContentWriterService service;
		service = new DefaultContentWriterService ();
		return service;
	}

	@Override
	public final boolean isThreadStarted() {
		return this.isThreadStarted;
	}

}
