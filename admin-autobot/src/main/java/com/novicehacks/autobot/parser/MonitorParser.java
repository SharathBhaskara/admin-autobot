package com.novicehacks.autobot.parser;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.types.Monitor;
import com.novicehacks.autobot.types.Monitor.Type;

/**
 * MonitorParser is a Callable that will parse the monitor commands from the
 * resource and returns a collection of monitor commands.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class MonitorParser extends Parser<Monitor> {

	private Logger logger = LogManager.getLogger(MonitorParser.class);
	/**
	 * Count of tokens in a correct Command.
	 * 
	 * <pre>
	 * Description;CommandPattern;ContainsHeader;Row;Column;Threshold;MonitorType
	 * Ex: Disk Monitoring;df -k;true;5;4;85;DISK
	 * </pre>
	 * 
	 * Where ";" is used as delimeter
	 * 
	 */
	private static final int TokenCount = 7;

	public MonitorParser(String resourceFile) {
		super(resourceFile);
	}

	/**
	 * Creates the commands from the resource file and adds them in a TreeSet
	 * 
	 * @return set of commands that can be executed
	 */
	public Set<Monitor> call() throws Exception {
		Map<String, String[]> tokenList;
		Set<Monitor> monitorSet;
		Monitor monitor;
		logger.entry();
		tokenList = getTokensFromFile();
		logger.debug("Count of Commands in the ResourceFile : {}",
				tokenList.size());
		monitorSet = new TreeSet<Monitor>();
		for (String line : tokenList.keySet()) {
			String[] tokens = tokenList.get(line);
			if (tokens.length != TokenCount) {
				logger.warn("Invalid Command with tokens : {}", line);
			} else {
				monitor = new Monitor(line);
				monitor.setDescription(tokens[0]);
				monitor.setCommandPattern(tokens[1]);
				monitor.setHasHeader(Boolean.valueOf(tokens[2]));
				monitor.setRow(Integer.parseInt(tokens[3]));
				monitor.setColumn(Integer.parseInt(tokens[4]));
				monitor.setThreshold(Integer.parseInt(tokens[5]));
				monitor.setMonitorType(Type.fromValue(tokens[6]));
				monitorSet.add(monitor);
			}
		}
		if (monitorSet.size() != tokenList.size()) {
			logger.warn(
					"Difference in defined and created commands : Created [{}], Defined[{}]",
					monitorSet.size(), tokenList.size());
		}
		logger.exit();
		return monitorSet;
	}

}
