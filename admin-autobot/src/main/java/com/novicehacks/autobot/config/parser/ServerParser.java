package com.novicehacks.autobot.config.parser;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.types.Server;
import com.novicehacks.autobot.types.ServerCredential;
import com.novicehacks.autobot.types.UnixServer;

/**
 * ServerParser is a Callable that will parse the server types from the resource
 * and returns a collection of servers.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class ServerParser extends Parser<Server> {
	private static final String InitCommandPrefix = "init:";
	private Logger logger = LogManager.getLogger(ServerParser.class);
	/**
	 * Count of tokens in a correct Command.
	 * 
	 * <pre>
	 * id;name;ipaddress;username1;password1;username2;password2
	 * </pre>
	 * 
	 * Where ";" is used as delimeter
	 * 
	 */
	private static int MinTokenCount = 3;

	public ServerParser(String resourcePath) {
		super(resourcePath);
	}

	/**
	 * Creates the servers from the resource file and adds them in a TreeSet
	 * 
	 * @return set of Server where the commands can be executed
	 */
	public Set<Server> call() throws Exception {
		Map<String, String[]> tokenList;
		Set<Server> serverSet;
		UnixServer server;
		logger.entry();
		tokenList = getTokensFromFile();
		logger.debug("Count of Commands in the ResourceFile : {}",
				tokenList.size());
		serverSet = new HashSet<Server>();
		for (String line : tokenList.keySet()) {
			String[] tokens = tokenList.get(line);
			/**
			 * No of tokens should be greater than minimum count of tokens, and
			 * after the minimum count the parameters should be in multiple of 2
			 * representing the credentials
			 */
			if (tokens.length >= MinTokenCount) {
				server = new UnixServer(line);
				int tokenIndex = 0;
				server.setId(tokens[tokenIndex++]);
				server.setName(tokens[tokenIndex++]);
				server.setIpAddress(tokens[tokenIndex++]);
				/* Create the init commands */
				List<String> initCommands = new LinkedList<String>();

				while (tokens[tokenIndex] != null
						&& tokens[tokenIndex].startsWith(InitCommandPrefix)) {
					initCommands.add(tokens[tokenIndex].replace(
							InitCommandPrefix, ""));
					tokenIndex++;
				}
				server.setInitializationCommands(initCommands
						.toArray(new String[] {}));
				/* Identify the no. of credentials */
				int credentialCount = (tokens.length - tokenIndex + 1) / 2;
				logger.debug("Count of credentials for server ({}) : {}",
						server.name(), credentialCount);
				ServerCredential[] credentials = new ServerCredential[credentialCount];
				for (int i = 0; i < credentialCount
						&& tokenIndex < tokens.length; i++) {
					ServerCredential credential = new ServerCredential();
					/*
					 * Tokens[i+3] means starting from the first token after
					 * server info, Uses 2 tokens in every loop, for creating a
					 * credential with username and password
					 */
					credential.setLoginid(tokens[tokenIndex++]);
					credential.setPassword(tokens[tokenIndex++]);
					credentials[i] = credential;
				}
				server.setCredentials(credentials);
				serverSet.add(server);
			} else {
				logger.warn("Invalid Command with tokens : {}", line);
			}
		}
		if (serverSet.size() != tokenList.size()) {
			logger.warn(
					"Difference in defined and created servers : Created [{}], Defined[{}]",
					serverSet.size(), tokenList.size());
		}
		logger.exit();
		return serverSet;
	}
}
