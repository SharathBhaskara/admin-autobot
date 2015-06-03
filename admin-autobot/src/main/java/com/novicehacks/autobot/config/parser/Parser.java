package com.novicehacks.autobot.config.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.config.ApplicationConfig;

/**
 * Basic Implementation for all the parsers.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 * @param <T>
 * @see ServerParser
 * @see CommandParser
 * @see ExecutableParser
 */
public abstract class Parser<T> implements Callable<Set<T>> {
	private String path;
	private Logger logger = LogManager.getLogger (Parser.class);

	public Parser (String resourcePath) {
		this.path = resourcePath;
	}

	/**
	 * Loops for all the lines of a resource, and creates tokens for each line
	 * based on the token separator property in the sysconfig
	 * 
	 * @return A List of String tokens in the file.
	 * @throws IOException
	 */
	public Map<String, String[]> getTokensFromFile() throws IOException {
		String line;
		String tokenSeperator;
		logger.entry ();
		tokenSeperator = ApplicationConfig.getInstance ().tokenSeperator ();
		Map<String, String[]> tokenList = new HashMap<String, String[]> ();

		try (
				InputStream is = ClassLoader.getSystemResourceAsStream (path);
				Scanner scanner = new Scanner (is) ) {
			while (scanner.hasNextLine ()) {
				line = scanner.nextLine ();
				logger.debug ("Creation token for Line : {}", line);
				String[] tokens = line.split (tokenSeperator);
				tokenList.put (line, tokens);
			}
		}
		logger.exit ();
		return tokenList;
	}

}
