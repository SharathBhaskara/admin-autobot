package com.novicehacks.autobot.ssh.logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.config.SysConfig;
import com.novicehacks.autobot.core.services.OutputFooterService;
import com.novicehacks.autobot.core.services.OutputHeaderService;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.ssh.exception.UnixOutputLoggingException;

public class ShellOutputLoggerTaskHelper {

	public static final String OutputFile = "botconsole.log";
	private static Logger logger = LogManager.getLogger (ShellOutputLoggerTask.class);
	private static OutputFooterService footerService;

	private static class ShellOutputLoggerTaskHelperSingleton {
		private final static ShellOutputLoggerTaskHelper instance = new ShellOutputLoggerTaskHelper ();

		public static ShellOutputLoggerTaskHelper getInstance() {
			return instance;
		}
	}

	private ShellOutputLoggerTaskHelper () {}

	public static ShellOutputLoggerTaskHelper getInstance() {
		return ShellOutputLoggerTaskHelperSingleton.getInstance ();
	}

	public Path createFileIfNeededAndGetPath() {
		logger.entry ();
		String shellConsoleFolder = SysConfig.getInstance ().getShellConsoleFolder ();
		Path outputFolderPath = Paths.get (shellConsoleFolder);
		Path outputFilePath = outputFolderPath.resolve (OutputFile);
		if (Files.notExists (outputFilePath))
			createFile (outputFolderPath, outputFilePath);
		logger.exit (outputFilePath);
		return outputFilePath;
	}

	private void createFile(Path outputFolderPath, Path outputFilePath) {
		try {
			Files.createDirectories (outputFolderPath);
			Files.createFile (outputFilePath);
		} catch (IOException e) {
			throw new UnixOutputLoggingException ("OutputFile Initialization Failed", e);
		}
	}

	public OutputHeaderService headerService(Server server, Command command) {
		OutputHeaderService headerService;
		headerService = new ShellOutputHeaderService (server, command);
		return headerService;
	}

	public OutputFooterService footerService() {
		if (footerService == null)
			footerService = new ShellOutputFooterService ();
		return footerService;
	}

}
