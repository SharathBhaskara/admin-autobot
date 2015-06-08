package com.novicehacks.autobot.config;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.types.Executable;

/**
 * It loads the executable beans from the user config resources.
 * 
 * <p>
 * Uses ResourceConfigLoader to parse the contents of the config files.
 * </p>
 * <p>
 * The configurations are defined in this format <strong>
 * <em>serverId:commandId</em></strong> where ":" is the delimiter.
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class ExecutableConfigLoader implements Callable<Set<Executable>> {
	private static final int ServerIdTokenIndex = 0;
	private static final int CommaindIdTokenIndex = 1;

	private static final int MinTokenCount = 2;
	private Set<Executable> executableConfigSet;

	private Logger logger = LogManager.getLogger (ExecutableConfigLoader.class);

	private static final String InvalidTokenCountMsg = "Invalid count of tokens for executable";

	ExecutableConfigLoader () {

	}

	Set<Executable> getExecutableConfig() {
		return this.executableConfigSet;
	}

	public Set<Executable> call() throws IOException {
		logger.entry ();
		loadExecutableConfig ();
		logger.exit ();
		return getExecutableConfig ();
	}

	public void loadExecutableConfig() throws IOException {
		ResourceConfigParser parser = getResourceConfigParser ();
		Set<String> userConfigSet = parser.getConfigFromFile ();
		executableConfigSet = new HashSet<Executable> ();
		for (String userConfig : userConfigSet) {
			Executable executable = createExecutableFromUserConfig (userConfig);
			executableConfigSet.add (executable);
		}
	}

	private Executable createExecutableFromUserConfig(String userConfig) {
		String tokenSeperator = getTokenSeperator ();
		String[] configTokens = userConfig.split (tokenSeperator);
		checkAndRaiseExceptionForMinTokens (configTokens);
		Executable executable = new Executable (userConfig);
		executable.setCommandId (configTokens[CommaindIdTokenIndex]);
		executable.setServerId (configTokens[ServerIdTokenIndex]);
		return executable;
	}

	private void checkAndRaiseExceptionForMinTokens(String[] configTokens) {
		int tokenCount = configTokens.length;
		if (tokenCount != MinTokenCount)
			throw new ResourceLoadingException (InvalidTokenCountMsg + ":" + configTokens.length);
	}

	String getTokenSeperator() {
		return ApplicationConfig.getInstance ().tokenSeperator ();
	}

	ResourceConfigParser getResourceConfigParser() {
		String configFile = getConfigFilePath ();
		ResourceConfigParser parser = new ResourceConfigParser (configFile);
		return parser;
	}

	String getConfigFilePath() {
		return ApplicationConfigParser.getIntance ().absoluteExecutableResourcePath ();
	}

}
