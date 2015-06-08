package com.novicehacks.autobot.config;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.ShellCommand;

public class TestCommandConfigLoader {
	private CommandConfigLoader commandConfigLoader;
	private ResourceConfigParser configParser;
	@Rule
	public ExpectedException expectedException = ExpectedException.none ();
	private static final String[] DataSet = { "description:", "description:command:",
			"description:command:id", "description:command:id:extra" };

	@Before
	public void setup() {
		commandConfigLoader = spy (CommandConfigLoader.class);
		configParser = mock (ResourceConfigParser.class);
		when (commandConfigLoader.getResourceConfigParser ()).thenReturn (configParser);
		when (commandConfigLoader.getTokenSeperator ()).thenReturn (":");
	}

	@Test
	@Category (UnitTest.class)
	public void testCommandConfigWithOneToken() throws IOException {
		int dataSetIndex = 0;
		when (configParser.getConfigFromFile ()).thenReturn (mockedConfig (dataSetIndex));

		expectedException.expect (ResourceLoadingException.class);
		expectedException.expectMessage ("Command Config token count is invalid");
		commandConfigLoader.loadCommandConfig ();

		fail ("Command config loaded with incomplete data");
	}

	@Test
	@Category (UnitTest.class)
	public void testCommandConfigWithTwoTokens() throws IOException {
		int dataSetIndex = 1;
		when (configParser.getConfigFromFile ()).thenReturn (mockedConfig (dataSetIndex));

		expectedException.expect (ResourceLoadingException.class);
		expectedException.expectMessage ("Command Config token count is invalid");
		commandConfigLoader.loadCommandConfig ();

		fail ("Command config loaded with incomplete data");
	}

	@Test
	@Category (UnitTest.class)
	public void testCommandConfig() throws IOException {
		int dataSetIndex = 2;
		when (configParser.getConfigFromFile ()).thenReturn (mockedConfig (dataSetIndex));

		commandConfigLoader.loadCommandConfig ();

		Set<Command> commandConfigSet = commandConfigLoader.getCommandConfig ();
		assertCommandConfigurations (commandConfigSet, DataSet[dataSetIndex]);
	}

	@Test
	@Category (UnitTest.class)
	public void testCommandConfigWithExtraTokens() throws IOException {
		int dataSetIndex = 3;
		when (configParser.getConfigFromFile ()).thenReturn (mockedConfig (dataSetIndex));

		expectedException.expect (ResourceLoadingException.class);
		expectedException.expectMessage ("Command Config token count is invalid");
		commandConfigLoader.loadCommandConfig ();

		fail ("Command config loaded with incomplete data");
	}

	@Test
	@Category (UnitTest.class)
	public void testCommandConfigMultipleConfigurations() throws IOException {
		final String configData1 = "description1:command1:id1";
		final String configData2 = "description2:command2:id2";
		Set<String> mockedConfig = new HashSet<String> ();
		mockedConfig.add (configData1);
		mockedConfig.add (configData2);
		when (configParser.getConfigFromFile ()).thenReturn (mockedConfig);

		commandConfigLoader.loadCommandConfig ();

		Set<Command> commandConfigSet = commandConfigLoader.getCommandConfig ();
		assertCommandConfigurations (commandConfigSet, configData1, configData2);
	}

	@Test
	@Category (UnitTest.class)
	public void testResouceNotFoundInConfigParser() throws IOException {
		when (configParser.getConfigFromFile ()).thenThrow (new IOException ("Resource not found"));

		expectedException.expect (IOException.class);
		expectedException.expectMessage ("Resource not found");
		commandConfigLoader.loadCommandConfig ();
	}

	private void assertCommandConfigurations(	Set<Command> actualCommandConfigSet,
												String... expectedDataSet) {
		Assert.assertEquals ("Incorrect no. of config loaded", expectedDataSet.length,
				actualCommandConfigSet.size ());
		for (String expectedData : expectedDataSet) {
			Command expectedCommand = createCommand (expectedData);
			Assert.assertThat ("Command not found in loaded config", actualCommandConfigSet,
					CoreMatchers.hasItem (expectedCommand));
		}
	}

	private Command createCommand(String configData) {
		String[] configTokens = configData.split (":");
		ShellCommand command = new ShellCommand (configData);
		command.setCommandTxt (configTokens[1]);
		command.setDescription (configTokens[0]);
		command.setId (configTokens[2]);
		return command;
	}

	@Test
	@Category (UnitTest.class)
	public void testCommandConfigWithExtraToken() throws IOException {
		int dataSetIndex = 0;
		when (configParser.getConfigFromFile ()).thenReturn (mockedConfig (dataSetIndex));

		expectedException.expect (ResourceLoadingException.class);
		expectedException.expectMessage ("Command Config token count is invalid");
		commandConfigLoader.loadCommandConfig ();

		fail ("Command config loaded with incomplete data");
	}

	private Set<String> mockedConfig(int dataSetIndex) {
		String commandStr = DataSet[dataSetIndex];
		Set<String> mockedConfigSet = new HashSet<String> ();
		mockedConfigSet.add (commandStr);
		return mockedConfigSet;
	}
}
