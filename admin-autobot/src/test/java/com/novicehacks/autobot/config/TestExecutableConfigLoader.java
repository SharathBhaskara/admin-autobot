package com.novicehacks.autobot.config;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.config.ExecutableConfigLoader;
import com.novicehacks.autobot.config.ResourceConfigParser;
import com.novicehacks.autobot.config.ResourceLoadingException;
import com.novicehacks.autobot.core.types.Executable;

public class TestExecutableConfigLoader {
	private static final String[] Dataset = { "command", "command:server", "command:server:extra",
			"command2:server2" };
	private ExecutableConfigLoader executableConfigLoader;
	private ResourceConfigParser mockParser;
	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@Before
	public void setUp() {
		mockParser = Mockito.mock (ResourceConfigParser.class);
		executableConfigLoader = Mockito.spy (ExecutableConfigLoader.class);
		Mockito.when (executableConfigLoader.getConfigParser ()).thenReturn (mockParser);
	}

	@Test
	@Category (UnitTest.class)
	public void testLoadingConfigWithOneToken() throws IOException {
		int dataSetIndex = 0;
		Mockito.when (mockParser.getConfigFromFile ()).thenReturn (mockedConfig (dataSetIndex));

		exception.expect (ResourceLoadingException.class);
		exception.expectMessage ("Invalid count of tokens for executable:");
		executableConfigLoader.loadExecutableConfig ();

		fail ("Executable config loaded with invalid no. of tokens");
	}

	@Test
	@Category (UnitTest.class)
	public void testLoadingConfigWithExtraTokens() throws IOException {
		int dataSetIndex = 2;
		Mockito.when (mockParser.getConfigFromFile ()).thenReturn (mockedConfig (dataSetIndex));

		exception.expect (ResourceLoadingException.class);
		exception.expectMessage ("Invalid count of tokens for executable:");
		executableConfigLoader.loadExecutableConfig ();

		fail ("Executable config loaded with invalid no. of tokens");
	}

	@Test
	@Category (UnitTest.class)
	public void testLoadingConfig() throws IOException {
		int dataSetIndex = 1;
		Mockito.when (mockParser.getConfigFromFile ()).thenReturn (mockedConfig (dataSetIndex));

		executableConfigLoader.loadExecutableConfig ();

		Set<Executable> loadedConfig = executableConfigLoader.getExecutableConfig ();
		assertLoadedConfig (loadedConfig, dataSetIndex);
	}

	@Test
	@Category (UnitTest.class)
	public void testLoadingMultipleConfig() throws IOException {
		int[] dataSetIndex = { 1, 3 };
		Mockito.when (mockParser.getConfigFromFile ()).thenReturn (mockedConfig (dataSetIndex));

		executableConfigLoader.loadExecutableConfig ();

		Set<Executable> loadedConfig = executableConfigLoader.getExecutableConfig ();
		assertLoadedConfig (loadedConfig, dataSetIndex);
	}

	private void assertLoadedConfig(Set<Executable> loadedConfig, int... dataSetIndex) {
		assertEquals ("Count of config loaded is incorrect", dataSetIndex.length,
				loadedConfig.size ());
		for (int index : dataSetIndex) {
			Executable expected = createExpectedExecutable (index);
			assertThat ("Expected Command config not loaded", loadedConfig,
					CoreMatchers.hasItem (expected));
		}
	}

	private Executable createExpectedExecutable(int index) {
		String userConfig = Dataset[index];
		String[] expectedTokens = userConfig.split (":");
		Executable expected = new Executable (userConfig);
		expected.setCommandId (expectedTokens[0]);
		expected.setServerId (expectedTokens[1]);
		return expected;
	}

	private Set<String> mockedConfig(int... dataSetIndex) {
		Set<String> mockConfig = new HashSet<String> ();
		for (int index : dataSetIndex)
			mockConfig.add (Dataset[index]);
		return mockConfig;
	}
}
