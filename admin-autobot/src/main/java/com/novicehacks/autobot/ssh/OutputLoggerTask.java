package com.novicehacks.autobot.ssh;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.config.SysConfig;
import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.ssh.exception.UnixOutputLoggingException;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

public class OutputLoggerTask implements Runnable {

	private Server server;
	private Command command;
	private String commandOutput;
	private boolean appendRawContent;
	private String content = "";
	private final int seperatorLength = 50;
	private String headerSeparatorContent;
	private String footerSeparatorContent;
	private static Path OutputFilePath;
	private static final String OutputFile = "botconsole.log";
	private Logger logger = LogManager.getLogger (OutputLoggerTask.class);

	OutputLoggerTask (Server unixServer, Command unixCommand, String commandOutput) {
		this ();
		validateParams (unixServer, unixCommand, commandOutput);
		this.server = unixServer;
		this.command = unixCommand;
		this.commandOutput = commandOutput;
	}

	OutputLoggerTask (String commandOutput, boolean appendRawContent) {
		this ();
		this.commandOutput = commandOutput;
		this.appendRawContent = appendRawContent;
	}

	private OutputLoggerTask (Server unixServer, Command unixCommand) {
		this ();
		this.server = unixServer;
		this.command = unixCommand;
	}

	private OutputLoggerTask () {
		initializeOutputFileForWriting ();
	}

	private void validateParams(Server unixServer, Command unixCommand, String commandOutput) {
		if (BotUtils.HasNullReferences (unixServer, unixCommand, commandOutput)) {
			throw new UnixOutputLoggingException ("Invalid Parameters Passed",
					new IllegalArgumentException ());
		}

	}

	private void initializeOutputFileForWriting() {
		OutputFilePath = Paths.get (SysConfig.getInstance ().getShellConsoleFolder ()).resolve (
				OutputFile);
		if (Files.notExists (OutputFilePath))
			createFile ();
	}

	private void createFile() {
		try {
			Files.createDirectories (Paths.get (SysConfig.getInstance ().getShellConsoleFolder ()));
			Files.createFile (OutputFilePath);
		} catch (IOException e) {
			throw new UnixOutputLoggingException ("OutputFile Initialization Failed", e);
		}
	}

	@Override
	public void run() {
		this.logger.entry ();
		prepareContent ();
		this.logger.trace (this.content);
		writeContent ();
		this.logger.exit ();
	}

	protected static String newLine() {
		return System.lineSeparator ();
	}

	protected static String getHeader(Server server, Command command) {
		OutputLoggerTask task = new OutputLoggerTask (server, command);
		String content = task.getHeaderContent ();
		return content;
	}

	protected static String getFooter() {
		OutputLoggerTask task = new OutputLoggerTask ();
		String content = task.getFooterContent ();
		return content;
	}

	private void writeContent() {
		StandardOpenOption[] Options = new StandardOpenOption[] { StandardOpenOption.WRITE,
				StandardOpenOption.SYNC, StandardOpenOption.APPEND };
		try (
				ByteChannel _byteChannel = Files.newByteChannel (OutputFilePath, Options) ) {
			writeToFile (_byteChannel);
		} catch (IOException e) {
			throw new UnixOutputLoggingException ("Unable to write output for command"
					+ this.command.id () + " on server" + this.server.id (), e);
		}
	}

	private void writeToFile(ByteChannel byteChannel) throws IOException {
		ByteBuffer contentBuffer;
		contentBuffer = ByteBuffer.wrap (this.content.toString ().getBytes ());
		byteChannel.write (contentBuffer);
	}

	private void prepareContent() {
		if (this.appendRawContent)
			this.content = this.commandOutput;
		else
			createFormattedContent ();
	}

	private void createFormattedContent() {
		appendHeaderToContent ();
		appendOutputToContent ();
		appendFooterToContent ();
	}

	private void appendHeaderToContent() {
		this.logger.entry ();
		String header;
		header = getHeaderContent ();
		this.content += header;
		this.logger.exit ();
	}

	private void appendOutputToContent() {
		this.logger.entry ();
		String contentBody;
		contentBody = getBodyContent ();
		this.content += contentBody;
		this.logger.exit ();
	}

	private void appendFooterToContent() {
		this.logger.entry ();
		String footer;
		footer = getFooterContent ();
		this.content += footer;
		this.logger.exit ();
	}

	private String getHeaderContent() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append (newLine ());
		buffer.append (getHeaderSeperator ());
		buffer.append (newLine ());
		buffer.append (getHeading ());
		buffer.append (newLine ());
		buffer.append (getHeaderSeperator ());
		buffer.append (newLine ());
		return buffer.toString ();
	}

	private String getHeading() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append (getServerInfo ());
		buffer.append (newLine ());
		buffer.append (getCommandInfo ());
		buffer.append (newLine ());
		return buffer.toString ();
	}

	private String getServerInfo() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append ("Server : ");
		buffer.append (this.server.id ());
		buffer.append (" (");
		buffer.append (this.server.name ());
		buffer.append (")");
		return buffer.toString ();
	}

	private String getCommandInfo() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append ("Command : ");
		buffer.append (this.command.id ());
		buffer.append (" (");
		buffer.append (this.command.command ());
		buffer.append (")");
		return buffer.toString ();
	}

	private String getHeaderSeperator() {
		StringBuilder buffer = new StringBuilder ();
		if (this.headerSeparatorContent != null)
			return this.headerSeparatorContent;
		for (int count = 0; count < this.seperatorLength; count++) {
			buffer.append ("+");
		}
		this.headerSeparatorContent = buffer.toString ();
		return buffer.toString ();
	}

	private String getBodyContent() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append (this.commandOutput);
		buffer.append (newLine ());
		return buffer.toString ();
	}

	private String getFooterContent() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append (getFooterSeperator ());
		buffer.append (newLine ());
		buffer.append (getFooterSeperator ());
		return buffer.toString ();
	}

	private String getFooterSeperator() {
		StringBuilder buffer = new StringBuilder ();
		if (this.footerSeparatorContent != null)
			return this.footerSeparatorContent;
		for (int count = 0; count < this.seperatorLength; count++) {
			buffer.append ("*");
		}
		this.footerSeparatorContent = buffer.toString ();
		return buffer.toString ();
	}

}
