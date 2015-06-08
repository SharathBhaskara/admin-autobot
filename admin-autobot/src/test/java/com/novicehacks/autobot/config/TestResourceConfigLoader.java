package com.novicehacks.autobot.config;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Executable;
import com.novicehacks.autobot.core.types.SSHServer;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.core.types.ShellCommand;

public class TestResourceConfigLoader {

	private ServerConfigLoader serverConfigLoader;
	private CommandConfigLoader commandConfigLoader;
	private ExecutableConfigLoader executableConfigLoader;
	private ResourceConfigLoader resourceConfigLoader;

	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@Before
	public void setup() throws Exception {
		resourceConfigLoader = spy (ResourceConfigLoader.class);
		serverConfigLoader = mock (ServerConfigLoader.class);
		commandConfigLoader = mock (CommandConfigLoader.class);
		executableConfigLoader = mock (ExecutableConfigLoader.class);
		setUpStubs ();
	}

	private void setUpStubs() throws Exception {
		when (serverConfigLoader.call ()).thenReturn (mockedServerConfig ());
		when (commandConfigLoader.call ()).thenReturn (mockedCommandConfig ());
		when (executableConfigLoader.call ()).thenReturn (mockedExecutableConfig ());
		when (resourceConfigLoader.getServerConfigLoader ()).thenReturn (serverConfigLoader);
		when (resourceConfigLoader.getCommandConfigLoader ()).thenReturn (commandConfigLoader);
		when (resourceConfigLoader.getExecutableConfigLoader ())
				.thenReturn (executableConfigLoader);
	}

	@Test
	@Category (UnitTest.class)
	public void testExceptionInReadingServerConfigFiles() throws IOException, InterruptedException,
			ExecutionException, TimeoutException {
		when (serverConfigLoader.call ()).thenThrow (
				new IOException ("Server resource file not found"));

		exception.expect (ExecutionException.class);
		exception.expectCause (isA (IOException.class));
		exception.expectMessage ("Server resource file not found");
		resourceConfigLoader.loadResourceConfig ();
	}

	@Test
	@Category (UnitTest.class)
	public void testExceptionInReadingCommandConfigFiles() throws Exception {
		when (commandConfigLoader.call ()).thenThrow (
				new IOException ("Command resource file not found"));

		exception.expect (ExecutionException.class);
		exception.expectCause (isA (IOException.class));
		exception.expectMessage ("Command resource file not found");
		resourceConfigLoader.loadResourceConfig ();
	}

	@Test
	@Category (UnitTest.class)
	public void testExceptionInReadingExecutableConfigFiles() throws IOException,
			InterruptedException, ExecutionException, TimeoutException {
		when (executableConfigLoader.call ()).thenThrow (
				new IOException ("Command resource file not found"));

		exception.expect (ExecutionException.class);
		exception.expectCause (isA (IOException.class));
		exception.expectMessage ("Command resource file not found");
		resourceConfigLoader.loadResourceConfig ();
	}

	@Test
	@Category (UnitTest.class)
	public void testLoadingResourceConfig() throws InterruptedException, ExecutionException,
			TimeoutException {
		resourceConfigLoader.loadResourceConfig ();
		assertCommandConfig ();
		assertServerConfig ();
		assertExecutableConfig ();
	}

	private void assertExecutableConfig() {
		ResourceConfig config = ResourceConfig.getInstance ();
		Set<Executable> execuables = config.executables ();
		assertEquals ("Incorrect no. of executable config loaded", 1, execuables.size ());
		for (Executable executable : execuables)
			assertEquals ("Executable Config loaded incorrectly", mockExecutable (), executable);
	}

	private void assertServerConfig() {
		ResourceConfig config = ResourceConfig.getInstance ();
		Set<Server> servers = config.servers ();
		assertEquals ("Incorrect no. of server config loaded", 1, servers.size ());
		for (Server server : servers)
			assertEquals ("Server Config loaded incorrectly", mockServer (), server);

	}

	private void assertCommandConfig() {
		ResourceConfig config = ResourceConfig.getInstance ();
		Set<Command> commands = config.commands ();
		assertEquals ("Incorrect no. of command config loaded", 1, commands.size ());
		for (Command command : commands)
			assertEquals ("Command Config loaded incorrectly", mockCommand (), command);
	}

	private Set<Server> mockedServerConfig() {
		Server server = mockServer ();
		Set<Server> serverConfig = new HashSet<Server> ();
		serverConfig.add (server);
		return serverConfig;
	}

	private Server mockServer() {
		SSHServer server = new SSHServer ("id:name:ip:user:pass");
		server.setId ("id");
		server.setName ("name");
		server.setIpAddress ("ip");
		return server;
	}

	private Set<Command> mockedCommandConfig() {
		Command command = mockCommand ();
		Set<Command> commandConfig = new HashSet<Command> ();
		commandConfig.add (command);
		return commandConfig;
	}

	private Command mockCommand() {
		ShellCommand command = new ShellCommand ("description:command:id");
		command.setCommandTxt ("command");
		command.setDescription ("description");
		command.setId ("id");
		return command;
	}

	private Set<Executable> mockedExecutableConfig() {
		Executable executable = mockExecutable ();
		Set<Executable> executableConfig = new HashSet<Executable> ();
		executableConfig.add (executable);
		return executableConfig;
	}

	private Executable mockExecutable() {
		Executable executable = new Executable ("serverId:commandId");
		executable.setServerId ("servreId");
		executable.setCommandId ("commandId");
		return executable;
	}
}
