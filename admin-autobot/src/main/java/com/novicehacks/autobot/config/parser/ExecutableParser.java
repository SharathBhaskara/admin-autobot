package com.novicehacks.autobot.config.parser;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.types.Executable;

/**
 * MonitorParser is a Callable that will parse the monitor commands from the
 * resource and returns a collection of monitor commands.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class ExecutableParser extends Parser<Executable> {

	private Logger logger = LogManager.getLogger();

	/**
	 * Count of tokens in a correct Executable Command.
	 * 
	 * <pre>
	 * &lt;commandid&gt;
	 * </pre>
	 */
	private static final int TokenCount = 2;

	public ExecutableParser(String resourcePath) {
		super(resourcePath);
	}

	/**
	 * Creates the executable commands from the resource file and adds them in a
	 * TreeSet
	 * 
	 * @return set of commands that are executed periodically.
	 */
	public Set<Executable> call() throws Exception {
		logger.entry();
		Map<String, String[]> tokenList;
		Set<Executable> executableSet;
		Executable executable;
		logger.entry();
		tokenList = getTokensFromFile();
		logger.debug("Count of Executable Commands in the ResourceFile : {}",
				tokenList.size());
		executableSet = new TreeSet<Executable>();
		for (String line : tokenList.keySet()) {
			String[] tokens = tokenList.get(line);
			if (tokens.length != TokenCount) {
				logger.warn("Invalid Command with tokens : {}", line);
			} else {
				executable = new Executable(line);
				executable.setServerId(tokens[0]);
				executable.setCommandId(tokens[1]);
				executableSet.add(executable);
			}
		}
		if (executableSet.size() != tokenList.size()) {
			logger.warn(
					"Difference in defined and created commands : Created [{}], Defined[{}]",
					executableSet.size(), tokenList.size());
		}
		logger.exit();
		return executableSet;
	}

}
