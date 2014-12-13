package com.novicehacks.autobot.shell;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.MonitorManager;
import com.novicehacks.autobot.ReportManager;
import com.novicehacks.autobot.config.SysConfig;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

/**
 * <p>
 * Will write the content into the temporary file in the folder specified by the
 * SysConfig.ShellConsoleFolder. The content written by this console is used in
 * Reporting and Monitoring
 * </p>
 * 
 * <p>
 * For Consistency, ShellConsole needs to be called in 2 ways.
 * <ol>
 * <li>once after executing all the commands on a server.</li>
 * <li>once after executing each command on the server.</li>
 * </ol>
 * So that we can capture all the data at once in the data file,
 * </p>
 * <p>
 * <strong>TODO: Make the log generation more configurable</strong>
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see MonitorManager
 * @see ReportManager
 */
public class ShellConsole implements Runnable {
	/**
	 * File name is not configurable, as other logics depend on this resource
	 * for correct functionality.
	 */
	public static final String ConsoleFile = "botconsole.log";
	private static final Path ConsolePath;

	static {
		ConsolePath = Paths
				.get(SysConfig.getInstance().getShellConsoleFolder()).resolve(
						ConsoleFile);
	}
	private String content;
	private Server server;
	private Command command;
	private Logger logger = LogManager.getLogger(ShellConsole.class);

	/**
	 * The logs will have a generic server definition, and command data attached
	 * by default.
	 * 
	 * Only to be used, if you are not planning to use the Monitor Logic.
	 * 
	 * @param fileName
	 * @param content
	 */
	public ShellConsole(String content) {
		this.content = content;
	}

	public ShellConsole(String content, Server server, Command command) {
		this.command = command;
		this.server = server;
		this.content = content;
	}

	@Override
	public void run() {
		logger.entry();
		try (ByteChannel _byteChannel = Files.newByteChannel(ConsolePath,
				StandardOpenOption.WRITE, StandardOpenOption.SYNC,
				StandardOpenOption.APPEND)) {
			ByteBuffer contentBuffer;
			ByteBuffer headerBuffer;
			ByteBuffer footerBuffer;
			contentBuffer = ByteBuffer.wrap(content.toString().getBytes());
			headerBuffer = ByteBuffer.wrap(getHeader().getBytes());
			footerBuffer = ByteBuffer.wrap(getFooter().getBytes());
			_byteChannel.write(headerBuffer);
			_byteChannel.write(contentBuffer);
			_byteChannel.write(footerBuffer);
		} catch (IOException e) {
			logger.error("Error While writing to Shell Console File : {} %n",
					e, e);
		}
		logger.exit();
	}

	private String getFooter() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("*******************************************************%n");
		buffer.append("Started processing Unix Commands On Server :");
		if (this.server != null) {
			buffer.append(this.server.ipaddress());
		} else {
			buffer.append("Unknown");
		}
		buffer.append("%n");
		buffer.append("*******************************************************%n");

		buffer.append("Executing :");
		if (this.command != null) {
			buffer.append(this.command.command());
		} else {
			buffer.append("Unknown Command");
		}
		buffer.append("%n");
		buffer.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++%n");

		return buffer.toString();
	}

	private String getHeader() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++%n");
		return buffer.toString();
	}
}
