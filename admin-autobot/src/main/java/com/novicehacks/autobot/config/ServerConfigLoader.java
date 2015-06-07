package com.novicehacks.autobot.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.types.SSHServer;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.core.types.ServerCredential;

/**
 * This will load the server configurations from user defined resources.
 * <p>
 * It uses {@link ResourceConfigParser} to read the configuration from the
 * resource files
 * </p>
 * <p>
 * The configuration format can be as given below <strong>
 * <em>id:name:ipaddress:[initcommands:]username:password</em></strong> where
 * ":" is delimiter. username and password can be specified multiple times.
 * </p>
 * 
 * <p>
 * <strong>Init commands should have the prefix: "init#" followed by command.
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see Server
 * @see SSHServer
 */
public class ServerConfigLoader implements Callable<Set<Server>> {
	private static int MinTokenCount = 5;
	public static final int ServerIdTokenIndex = 0;
	public static final int ServerNameTokenIndex = 1;
	public static final int ServerIPAddressTokenIndex = 2;
	public static final int CredentialOrInitCommandStartIndex = 3;
	public static final String InitCommandPrefix = "init#";
	private static final String MinTokenCountMsg = "Server configuration didn't meet minimum config requirement";
	private static final String ServerCredentialCountMsg = "Server credential count is incorrect with starting index";
	private Logger logger = LogManager.getLogger (ServerConfigLoader.class);
	private Set<Server> serverConfigSet = new HashSet<Server> ();

	ServerConfigLoader () {}

	Set<Server> getServerConfig() {
		return this.serverConfigSet;
	}

	@Override
	public Set<Server> call() throws IOException {
		logger.entry ();
		loadServerConfiguration ();
		logger.exit ();
		return getServerConfig ();
	}

	public void loadServerConfiguration() throws IOException {
		ResourceConfigParser parser = getResourceConfigParser ();
		Set<String> userServerConfig = parser.getConfigFromFile ();
		for (String configLine : userServerConfig) {
			Server server = createServerBeanFromConfig (configLine);
			serverConfigSet.add (server);
		}
	}

	private Server createServerBeanFromConfig(String configLine) {
		final String tokenSeperator = getTokenSeperator ();
		String[] configTokens = configLine.split (tokenSeperator);
		SSHServer server = new SSHServer (configLine);
		server = loadServerWithUserConfig (server, configTokens);
		return server;
	}

	private SSHServer loadServerWithUserConfig(final SSHServer server, final String[] configTokens) {
		checkAndRaiseExceptionForMinimumTokens (configTokens.length);
		String serverId = configTokens[ServerIdTokenIndex];
		String serverName = configTokens[ServerNameTokenIndex];
		String serverIpAddress = configTokens[ServerIPAddressTokenIndex];
		String[] serverInitCommands = getInitCommandsIfAny (configTokens);
		ServerCredential[] serverCredentials = getServerLoginCredentials (configTokens);

		server.setId (serverId);
		server.setName (serverName);
		server.setIpAddress (serverIpAddress);
		server.setInitializationCommands (serverInitCommands);
		server.setCredentials (serverCredentials);
		return server;
	}

	private ServerCredential[] getServerLoginCredentials(String[] configTokens) {
		int credentialIndex = skipInitCommandAndGetFirstCredentialsIndex (configTokens);
		checkAndRaiseExceptionForCredentialTokenCount (configTokens.length, credentialIndex);
		int countOfCredentials = getCredentialCount (configTokens.length, credentialIndex);
		ServerCredential[] credentials = new ServerCredential[countOfCredentials];
		ServerCredential loginCredential;
		for (int index = 0; credentialIndex < configTokens.length; index++, credentialIndex++) {
			loginCredential = new ServerCredential ();
			loginCredential.setLoginid (configTokens[credentialIndex]);
			credentialIndex++;
			loginCredential.setPassword (configTokens[credentialIndex]);
			credentials[index] = loginCredential;
		}
		return credentials;
	}

	private int skipInitCommandAndGetFirstCredentialsIndex(String[] configTokens) {
		int credentialIndex = CredentialOrInitCommandStartIndex;
		while (credentialIndex < configTokens.length
				&& configTokens[credentialIndex].startsWith (getInitCommandPrefix ()))
			credentialIndex++;
		return credentialIndex;
	}

	private void checkAndRaiseExceptionForCredentialTokenCount(int length, int credentialIndex) {
		int modOfCredentialCount = (length - credentialIndex) % 2;
		if (modOfCredentialCount == 1 || length <= credentialIndex)
			throw new ResourceLoadingException (ServerCredentialCountMsg + ":" + credentialIndex);
	}

	private int getCredentialCount(int length, int credentialIndex) {
		int credentialCount = (length - credentialIndex) / 2;
		return credentialCount;
	}

	private String[] getInitCommandsIfAny(String[] configTokens) {
		final String initCommandPrefix = getInitCommandPrefix ();
		int initCommandIndex = CredentialOrInitCommandStartIndex;
		List<String> initCommandList = new ArrayList<String> ();
		while (initCommandIndex < configTokens.length
				&& configTokens[initCommandIndex].startsWith (initCommandPrefix)) {
			String initCommand = configTokens[initCommandIndex];
			initCommand = initCommand.replaceFirst (initCommandPrefix, "");
			initCommandList.add (initCommand);
			initCommandIndex++;
		}
		return initCommandList.toArray (new String[] { });
	}

	private void checkAndRaiseExceptionForMinimumTokens(int countOfUserConfigTokens) {
		if (countOfUserConfigTokens >= MinTokenCount)
			return;
		else
			throw new ResourceLoadingException (MinTokenCountMsg+":"+countOfUserConfigTokens);
	}

	private String getInitCommandPrefix() {
		return InitCommandPrefix;
	}

	String getTokenSeperator() {
		return ApplicationConfig.getInstance ().tokenSeperator ();
	}

	ResourceConfigParser getResourceConfigParser() {
		String serverConfigFilename = ConfigParser.getIntance ().serverResource ();
		ResourceConfigParser configParser = new ResourceConfigParser (serverConfigFilename);
		return configParser;
	}

}
