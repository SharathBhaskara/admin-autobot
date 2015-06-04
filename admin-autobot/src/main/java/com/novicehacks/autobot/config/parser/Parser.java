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
 * Creates a map of lines with tokens, after reading them from a file.
 * 
 * <p>
 * Based on the resource path passed will a map with the line and its tokens
 * split by the token seperator in {@link ApplicationConfig}.
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 * @param <T>
 * @see ServerParser
 * @see CommandParser
 * @see ExecutableParser
 */
public abstract class Parser<T> implements Callable<Set<T>> {
	private Logger logger = LogManager.getLogger (Parser.class);
	String filePath;

	protected Parser (String path) {
		this.filePath = path;
	}

	Parser () {}

	void setFilePath(String path) {
		this.filePath = path;
	}

	/**
	 * Loops for all the lines of a resource, and creates tokens for each line
	 * based on the <em>token separator</em> property in the
	 * {@link ApplicationConfig}
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
		logger.trace ("Reading the resource file : {} and using '{}' as token seperator", filePath,
				tokenSeperator);
		try (
				InputStream is = ClassLoader.getSystemResourceAsStream (filePath);
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
