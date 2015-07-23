package com.novicehacks.autobot.reporting;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.reporting.exception.InvalidCommandOutputFilePath;
import com.novicehacks.autobot.services.OutputFooterService;
import com.novicehacks.autobot.services.OutputHeaderService;

/**
 * Reads the command output from the command output file path specified.
 * 
 * It will generate a list of beans based on the {@link OutputHeaderService} for
 * header and {@link OutputFooterService}
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class CommandOutputReader {
	private Path commandOutputFilePath;
	private Logger logger = LogManager.getLogger (CommandOutputReader.class);
	private List<CommandOutputBean> outputBeanList;

	public CommandOutputReader (String filePath) {
		this (Paths.get (filePath));
	}

	public CommandOutputReader (Path filePath) {
		this.commandOutputFilePath = filePath;
	}

	public void parseOutputFile() {
		logger.entry (commandOutputFilePath);
		validatePath ();
		logger.exit ();
	}

	private void validatePath() {
		if (Files.notExists (commandOutputFilePath)) {
			throw new InvalidCommandOutputFilePath ("Invalid output file path",
					new FileNotFoundException (commandOutputFilePath.toString ()));
		}

	}

	public List<CommandOutputBean> parsedOutputBeans() {
		return this.outputBeanList;
	}

	OutputHeaderService outputHeaderService() {
		// TODO Auto-generated method stub
		return null;
	}

	OutputFooterService outputFooterService() {
		// TODO Auto-generated method stub
		return null;
	}
}
