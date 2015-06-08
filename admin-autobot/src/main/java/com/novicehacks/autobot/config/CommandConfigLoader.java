package com.novicehacks.autobot.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.ShellCommand;

/**
 * This will load the command configurations from user defined resources.
 * 
 * <p>
 * It uses {@linkp ResourceConfigParser} to read the configurations from the
 * resources.
 * </p>
 * <p>
 * The config definitions will be in this format <strong>
 * <em>description:command:id</em></strong> where ":" is the delimiter.
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see ShellCommand
 * @see ResourceConfigLoader
 */
public class CommandConfigLoader implements Callable<Set<Command>> {
	private Logger logger = LogManager.getLogger ();
	public static final int CommandIdTokenIndex = 2;
	public static final int CommandStrTokenIndex = 1;
	public static final int CommandDescriptionTokenIndex = 0;
	private static final int MinConfigTokenCount = 3;
	private static final String IncorrectCommandConfigTokenMsg = "Command Config token count is invalid";
	private Set<Command> commandConfigSet;

	CommandConfigLoader () {
		commandConfigSet = new HashSet<Command> ();
	}

	public void loadCommandConfig() throws IOException {
		ResourceConfigParser configParser = getResourceConfigParser ();
		Set<String> userCommandConfigStringSet = configParser.getConfigFromFile ();
		for (String commandConfigStr : userCommandConfigStringSet) {
			Command command = createCommandConfigFromUserString (commandConfigStr);
			commandConfigSet.add (command);
		}
	}

	private Command createCommandConfigFromUserString(String commandConfigStr) {
		String[] configTokens = commandConfigStr.split (getTokenSeperator ());
		checkAndRaiseExceptionForMinimumTokens (configTokens);
		String commandDescription = configTokens[CommandDescriptionTokenIndex];
		String commandString = configTokens[CommandStrTokenIndex];
		String commandId = configTokens[CommandIdTokenIndex];

		ShellCommand command = createCommand (commandConfigStr);
		command.setCommandTxt (commandString);
		command.setDescription (commandDescription);
		command.setId (commandId);
		return command;
	}

	private ShellCommand createCommand(String commandConfigStr) {
		return new ShellCommand (commandConfigStr);
	}

	private void checkAndRaiseExceptionForMinimumTokens(String[] configTokens) {
		if (configTokens.length != MinConfigTokenCount)
			throw new ResourceLoadingException (IncorrectCommandConfigTokenMsg + ":"
					+ configTokens.length);
	}

	Set<Command> getCommandConfig() {
		return this.commandConfigSet;
	}

	ResourceConfigParser getResourceConfigParser() {
		String configFilename = ApplicationConfigParser.getIntance ().absoluteCommandResourcePath ();
		ResourceConfigParser configParser = new ResourceConfigParser (configFilename);
		return configParser;
	}

	String getTokenSeperator() {
		return ApplicationConfig.getInstance ().tokenSeperator ();
	}

	/**
	 * Creates the commands from the resource file and adds them in a TreeSet
	 * 
	 * @return set of commands that can be executed
	 */
	public Set<Command> call() throws Exception {
		logger.entry ();
		loadCommandConfig ();
		logger.exit ();
		return getCommandConfig ();
	}

}
