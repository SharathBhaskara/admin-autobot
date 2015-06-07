package com.novicehacks.autobot.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
 * @see ServerConfigLoader
 * @see CommandConfigLoader
 * @see ExecutableConfigLoader
 */
public class ResourceConfigParser {
	private Logger logger = LogManager.getLogger (ResourceConfigParser.class);
	String filePath;

	protected ResourceConfigParser (String path) {
		this.filePath = path;
	}

	ResourceConfigParser () {}

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
	public Set<String> getConfigFromFile() throws IOException {
		String line;
		logger.entry ();
		Set<String> configSet = new HashSet<String> ();
		logger.trace ("Reading the resource file : {} ", filePath);
		try (
				InputStream is = ClassLoader.getSystemResourceAsStream (filePath);
				Scanner scanner = new Scanner (is) ) {
			while (scanner.hasNextLine ()) {
				line = scanner.nextLine ();
				logger.trace ("Reading line {} from filepath [{}]", line, filePath);
				configSet.add (line);
			}
		}
		logger.exit ();
		return configSet;
	}

}
