package com.novicehacks.autobot.unix;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.BotUtils;
import com.novicehacks.autobot.config.SysConfig;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;
import com.novicehacks.autobot.unix.exception.UnixOutputLoggingException;

public class UnixCommandOutputLoggerTask implements Runnable {

	private Server				server;
	private Command				command;
	private String				commandOutput;
	private boolean				appendRawContent;
	private String				content			= "";
	private final int			seperatorLength	= 50;
	private String				headerSeparatorContent;
	private String				footerSeparatorContent;
	// private static final ReentrantLock OutputFileLock = new ReentrantLock ();
	private static Path			OutputFilePath;
	private static final String	OutputFile		= "botconsole.log";
	private Logger				logger			= LogManager
														.getLogger (UnixCommandOutputLoggerTask.class);

	public UnixCommandOutputLoggerTask (Server unixServer, Command unixCommand, String commandOutput) {
		this ();
		validateParams (unixServer, unixCommand, commandOutput);
		this.server = unixServer;
		this.command = unixCommand;
		this.commandOutput = commandOutput;
	}

	public UnixCommandOutputLoggerTask (String commandOutput, boolean appendRawContent) {
		this ();
		this.commandOutput = commandOutput;
		this.appendRawContent = appendRawContent;
	}

	private UnixCommandOutputLoggerTask (Server unixServer, Command unixCommand) {
		this ();
		this.server = unixServer;
		this.command = unixCommand;
	}

	private UnixCommandOutputLoggerTask () {
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
		logger.entry ();
		prepareContent ();
		logger.trace (content);
		writeContent ();
		logger.exit ();
	}

	private void writeContent() {
		StandardOpenOption[] Options = new StandardOpenOption[] { StandardOpenOption.WRITE,
				StandardOpenOption.SYNC, StandardOpenOption.APPEND };
		try (
				ByteChannel _byteChannel = Files.newByteChannel (OutputFilePath, Options) ) {
			writeToFile (_byteChannel);
		} catch (IOException e) {
			throw new UnixOutputLoggingException ("Unable to write output for command"
					+ command.id () + " on server" + server.id (), e);
		}
	}

	private void writeToFile(ByteChannel byteChannel) throws IOException {
		ByteBuffer contentBuffer;
		contentBuffer = ByteBuffer.wrap (content.toString ().getBytes ());
		byteChannel.write (contentBuffer);
	}

	private void prepareContent() {
		if (appendRawContent)
			content = commandOutput;
		else
			createFormattedContent ();
	}

	private void createFormattedContent() {
		appendHeaderToContent ();
		appendOutputToContent ();
		appendFooterToContent ();
	}

	private void appendHeaderToContent() {
		logger.entry ();
		String header;
		header = getHeaderContent ();
		content += header;
		logger.exit ();
	}

	private void appendOutputToContent() {
		logger.entry ();
		String contentBody;
		contentBody = getBodyContent ();
		content += contentBody;
		logger.exit ();
	}

	private void appendFooterToContent() {
		logger.entry ();
		String footer;
		footer = getFooterContent ();
		content += footer;
		logger.exit ();
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
		buffer.append (server.id ());
		buffer.append (" (");
		buffer.append (server.name ());
		buffer.append (")");
		return buffer.toString ();
	}

	private String getCommandInfo() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append ("Command : ");
		buffer.append (command.id ());
		buffer.append (" (");
		buffer.append (command.command ());
		buffer.append (")");
		return buffer.toString ();
	}

	private String getHeaderSeperator() {
		StringBuilder buffer = new StringBuilder ();
		if (headerSeparatorContent != null)
			return headerSeparatorContent;
		for (int count = 0; count < seperatorLength; count++) {
			buffer.append ("+");
		}
		headerSeparatorContent = buffer.toString ();
		return buffer.toString ();
	}

	private String getBodyContent() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append (commandOutput);
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
		if (footerSeparatorContent != null)
			return footerSeparatorContent;
		for (int count = 0; count < seperatorLength; count++) {
			buffer.append ("*");
		}
		footerSeparatorContent = buffer.toString ();
		return buffer.toString ();
	}

	static String newLine() {
		return System.lineSeparator ();
	}

	static String getHeader(Server server, Command command) {
		UnixCommandOutputLoggerTask task = new UnixCommandOutputLoggerTask (server, command);
		String content = task.getHeaderContent ();
		return content;
	}

	static String getFooter() {
		UnixCommandOutputLoggerTask task = new UnixCommandOutputLoggerTask ();
		String content = task.getFooterContent ();
		return content;
	}
}
