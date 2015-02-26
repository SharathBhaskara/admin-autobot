package com.novicehacks.autobot.ssh.logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.config.SysConfig;
import com.novicehacks.autobot.logger.OutputFooterService;
import com.novicehacks.autobot.logger.OutputHeaderService;
import com.novicehacks.autobot.ssh.exception.UnixOutputLoggingException;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

public class ShellOutputLoggerTaskHelper {

	protected static final String OutputFile = "botconsole.log";
	private static Logger logger = LogManager.getLogger (ShellOutputLoggerTask.class);

	public static Path createFileIfNeededAndGetPath() {
		logger.entry ();
		String shellConsoleFolder = SysConfig.getInstance ().getShellConsoleFolder ();
		Path outputFolderPath = Paths.get (shellConsoleFolder);
		Path outputFilePath = outputFolderPath.resolve (OutputFile);
		if (Files.notExists (outputFilePath))
			createFile (outputFolderPath, outputFilePath);
		logger.exit (outputFilePath);
		return outputFilePath;
	}

	private static void createFile(Path outputFolderPath, Path outputFilePath) {
		try {
			Files.createDirectories (outputFolderPath);
			Files.createFile (outputFilePath);
		} catch (IOException e) {
			throw new UnixOutputLoggingException ("OutputFile Initialization Failed", e);
		}
	}

	public static OutputHeaderService headerService(Server server, Command command) {
		OutputHeaderService headerService;
		headerService = new ShellOuptutHeaderService (server, command);
		return headerService;
	}

	public static OutputFooterService footerService() {
		OutputFooterService footerService;
		footerService = new ShellOutputFooterService ();
		return footerService;
	}

}
