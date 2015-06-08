package com.novicehacks.autobot.config;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.config.ResourceConfigParser;
import com.novicehacks.autobot.config.ResourceLoadingException;
import com.novicehacks.autobot.config.ServerConfigLoader;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.core.types.ServerCredential;

public class TestServerConfigLoader {
	private ResourceConfigParser parser;
	private ServerConfigLoader serverConfigLoader;
	@Rule
	public ExpectedException exception = ExpectedException.none ();

	private final String[] DataSet = new String[] {
			"serverid1:servername1:ipaddress1",//
			"serverid2:servername2:ipaddress2:username",//
			"serverid3:servername3:ipaddress3:username:password",//
			"serverid4:servername4:ipaddress4:username1:password1:username2",//
			"serverid5:servername5:ipaddress5:init#command1:username1",//
			"serverid6:servername6:ipaddress6:username1:password1:username2:password2",//
			"serverid7:servername8:ipaddress8:init#command1:init#command2",//
			"serverid8:servername8:ipaddress8:init#command1:username1:password1",//
			"serverid9:servername9:ipaddress9:init#command1:init#command2:init#command3:username:password",
			"serverid10:servername10:ipaddress10:init#command1:init#command2:username1:password1:username2:password2" };

	@Before
	public void setup() {
		parser = mock (ResourceConfigParser.class);
		serverConfigLoader = new ServerConfigLoader ();
		serverConfigLoader = spy (serverConfigLoader);
		when (serverConfigLoader.getResourceConfigParser ()).thenReturn (parser);
		when (serverConfigLoader.getTokenSeperator ()).thenReturn (":");
	}

	@Test
	@Category (UnitTest.class)
	public void testLoadingMultipleServerConfigBean() throws IOException {
		Set<String> configSet = new HashSet<String> ();
		configSet.add (DataSet[2]);
		configSet.add (DataSet[5]);
		configSet.add (DataSet[8]);
		when (parser.getConfigFromFile ()).thenReturn (configSet);

		serverConfigLoader.loadServerConfiguration ();

		assertEquals ("Not all ServerConfig loaded", 3, serverConfigLoader.getServerConfig ()
				.size ());
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConfigWithNoCredentials() throws IOException {
		final int datasetIndex = 0;
		when (parser.getConfigFromFile ()).thenReturn (mockedServerConfig (datasetIndex));

		exception.expect (ResourceLoadingException.class);
		exception.expectMessage ("Server configuration didn't meet minimum config requirement");
		serverConfigLoader.loadServerConfiguration ();

		fail ("Invalid server config loaded, with no credentials ");
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConfigWithInvalidFirstCredential() throws IOException {
		final int datasetIndex = 1;
		when (parser.getConfigFromFile ()).thenReturn (mockedServerConfig (datasetIndex));

		exception.expect (ResourceLoadingException.class);
		exception.expectMessage ("Server configuration didn't meet minimum config requirement");
		serverConfigLoader.loadServerConfiguration ();

		fail ("Invalid Server credential config being loaded, without password");
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConfigWithOneCredential() throws IOException {
		final int datasetIndex = 2;
		String username = "username";
		String password = "password";
		when (parser.getConfigFromFile ()).thenReturn (mockedServerConfig (datasetIndex));

		serverConfigLoader.loadServerConfiguration ();

		Server server = assertBasicServerConfigAndReturnInstance (datasetIndex);
		assertCredentialConfiguration (server, false, username);
		assertCredentialConfiguration (server, true, password);
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConfigWithInvalidSecondCredential() throws IOException {
		final int datasetIndex = 3;
		when (parser.getConfigFromFile ()).thenReturn (mockedServerConfig (datasetIndex));

		exception.expect (ResourceLoadingException.class);
		exception.expectMessage ("Server credential count is incorrect");
		serverConfigLoader.loadServerConfiguration ();

		fail ("Invalid Server credential config being loaded, without password for second credential");
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConfigWithInitCommandAndInvalidFirstCredential() throws IOException {
		final int datasetIndex = 4;
		when (parser.getConfigFromFile ()).thenReturn (mockedServerConfig (datasetIndex));

		exception.expect (ResourceLoadingException.class);
		exception.expectMessage ("Server credential count is incorrect");
		serverConfigLoader.loadServerConfiguration ();

		fail ("Invalid Server credential config being loaded, without password");
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConfigWithTwoSetsOfCredentials() throws IOException {
		int datasetIndex = 5;
		String usernames[] = { "username1", "username2" };
		String passwords[] = { "password1", "password2" };
		when (parser.getConfigFromFile ()).thenReturn (mockedServerConfig (datasetIndex));

		serverConfigLoader.loadServerConfiguration ();

		Server server = assertBasicServerConfigAndReturnInstance (datasetIndex);
		assertCredentialConfiguration (server, false, usernames);
		assertCredentialConfiguration (server, true, passwords);
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConfigWithInitCommandAndNoCredentials() throws IOException {
		final int datasetIndex = 6;
		when (parser.getConfigFromFile ()).thenReturn (mockedServerConfig (datasetIndex));

		exception.expect (ResourceLoadingException.class);
		exception.expectMessage ("Server credential count is incorrect");
		serverConfigLoader.loadServerConfiguration ();

		fail ("Invalid Server credential config being loaded, without password");
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConfigWithNoInitCommands() throws IOException {
		final int datasetIndex = 2;
		when (parser.getConfigFromFile ()).thenReturn (mockedServerConfig (datasetIndex));

		serverConfigLoader.loadServerConfiguration ();

		Server server = assertBasicServerConfigAndReturnInstance (datasetIndex);
		assertNotNull ("Empty array to be passed as init commands", server.initCommands ());
		assertEquals ("Empty array to be passed as init commands", 0, server.initCommands ().length);
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConfigWithSingleInitCommand() throws IOException {
		final int datasetIndex = 7;
		final String[] expectedInitCommands = { "command1" };
		when (parser.getConfigFromFile ()).thenReturn (mockedServerConfig (datasetIndex));

		serverConfigLoader.loadServerConfiguration ();

		Server server = assertBasicServerConfigAndReturnInstance (datasetIndex);
		assertInitCommands (server, expectedInitCommands);
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConfigWithMultipleInitCommand() throws IOException {
		final int datasetIndex = 8;
		final String[] expectedInitCommands = { "command1", "command2", "command3" };
		when (parser.getConfigFromFile ()).thenReturn (mockedServerConfig (datasetIndex));

		serverConfigLoader.loadServerConfiguration ();

		Server server = assertBasicServerConfigAndReturnInstance (datasetIndex);
		assertInitCommands (server, expectedInitCommands);
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConfigWithMultipleInitCommandsAndMultipleCredentials() throws IOException {
		final int datasetIndex = 9;
		final String[] expectedInitCommands = { "command1", "command2" };
		String usernames[] = { "username1", "username2" };
		String passwords[] = { "password1", "password2" };
		when (parser.getConfigFromFile ()).thenReturn (mockedServerConfig (datasetIndex));

		serverConfigLoader.loadServerConfiguration ();

		Server server = assertBasicServerConfigAndReturnInstance (datasetIndex);
		assertInitCommands (server, expectedInitCommands);
		assertCredentialConfiguration (server, false, usernames);
		assertCredentialConfiguration (server, true, passwords);
	}

	@Test
	@Category (UnitTest.class)
	public void testResouceNotFoundInConfigParser() throws IOException {
		when (parser.getConfigFromFile ()).thenThrow (new IOException ("Resource not found"));

		exception.expect (IOException.class);
		exception.expectMessage ("Resource not found");
		serverConfigLoader.loadServerConfiguration ();
	}

	private Set<String> mockedServerConfig(int datasetIndex) {
		String config = DataSet[datasetIndex];
		Set<String> configSet = new HashSet<String> ();
		configSet.add (config);
		return configSet;
	}

	private Server assertBasicServerConfigAndReturnInstance(int datasetIndex) {
		String expectedTokens[] = DataSet[datasetIndex].split (":");
		Iterator<Server> serversIterator = serverConfigLoader.getServerConfig ().iterator ();
		Server server = serversIterator.next ();
		assertEquals ("Server id not loaded correctly", expectedTokens[0], server.id ());
		assertEquals ("Server name not loaded correctly", expectedTokens[1], server.name ());
		assertEquals ("Server IPAddress not loaded correctly", expectedTokens[2],
				server.ipaddress ());
		return server;
	}

	private void assertCredentialConfiguration(	Server server,
												boolean isPassword,
												String... expectedCredentialss) {
		ServerCredential[] credentials = server.credentials ();
		assertEquals ("Incorrect no. of credentials loaded", expectedCredentialss.length,
				credentials.length);
		for (int index = 0; index < credentials.length; index++) {
			assertEachCredential (expectedCredentialss[index], credentials[index], isPassword);
		}
	}

	private void assertEachCredential(	String expected,
										ServerCredential actualCredential,
										boolean isPasswordCredential) {
		if (isPasswordCredential)
			assertEquals ("Incorrect login username", expected, actualCredential.getPassword ());
		else
			assertEquals ("Incorrect login username", expected, actualCredential.getLoginid ());
	}

	private void assertInitCommands(Server server, String[] expectedInitCommands) {
		String[] actualCommands = server.initCommands ();
		assertEquals ("Incorrect no. of commands loaded", expectedInitCommands.length,
				actualCommands.length);
		for (int index = 0; index < actualCommands.length; index++)
			assertEquals ("Command loaded incorrectly", expectedInitCommands[index],
					actualCommands[index]);
	}

}
