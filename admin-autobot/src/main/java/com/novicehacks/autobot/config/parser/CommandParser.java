package com.novicehacks.autobot.config.parser;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.ShellCommand;

/**
 * CommandParser is a Callable that will parse the bot commands from the
 * resource and returns a collection of bot commands.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class CommandParser extends Parser<Command> {
	private Logger logger = LogManager.getLogger();
	/**
	 * Count of tokens in a correct Command.
	 * 
	 * <pre>
	 * description;command;commandid
	 * </pre>
	 * 
	 * Where ";" is used as delimeter
	 * 
	 */
	private static final int TokenCount = 3;

	public CommandParser(String resourcePath) {
		super(resourcePath);
	}

	/**
	 * Creates the commands from the resource file and adds them in a TreeSet
	 * 
	 * @return set of commands that can be executed
	 */
	public Set<Command> call() throws Exception {
		Map<String, String[]> tokenList;
		Set<Command> commandSet;
		ShellCommand command;
		logger.entry();
		tokenList = getTokensFromFile();
		logger.debug("Count of Commands in the ResourceFile : {}",
				tokenList.size());
		commandSet = new TreeSet<Command>();
		for (String line : tokenList.keySet()) {
			String[] tokens = tokenList.get(line);
			if (tokens.length != TokenCount) {
				logger.warn("Invalid Command with tokens : {}", line);
			} else {
				command = new ShellCommand(line);
				command.setDescription(tokens[0]);
				command.setCommandTxt(tokens[1]);
				command.setId(tokens[2]);
				commandSet.add(command);
			}
		}
		if (commandSet.size() != tokenList.size()) {
			logger.warn(
					"Difference in defined and created commands : Created [{}], Defined[{}]",
					commandSet.size(), tokenList.size());
		}
		logger.exit();
		return commandSet;
	}
}
