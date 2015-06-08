package com.novicehacks.autobot.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.UnitTest;

public class TestConfigurationManager {
	ApplicationConfigParser parser;
	ApplicationConfigLoader loader;

	@Before
	public void setup() {
		parser = mock (ApplicationConfigParser.class);
		loader = mock (ApplicationConfigLoader.class);
		ApplicationConfig.getInstance ().unload ();
		ResourceConfig.getInstance ().unload ();
	}

	private Properties wrapIntoConfigProperties(String key, String value) {
		Properties props = new Properties ();
		props.put (key, value);
		return props;
	}

	@Test
	@Category (UnitTest.class)
	public void testResourceFolderConfiguration() {
		// given
		Properties mockConfig = wrapIntoConfigProperties ("ResourceFolder", "abcd");
		ConfigurationManager manager = mockedConfigurationManager (mockConfig);
		// when
		manager.loadSystemConfig ();
		// then
		ApplicationConfig config = ApplicationConfig.getInstance ();
		assertEquals ("Resource Folder property not loaded correctly", "abcd",
				config.resourceFolder ());
	}

	private ConfigurationManager mockedConfigurationManager(Properties mockConfig) {
		ConfigurationManager manager = mock (ConfigurationManager.class);
		when (manager.loadSystemConfig ()).thenCallRealMethod ();
		when (manager.getConfigParser ()).thenReturn (parser);
		when (parser.getConfigProperties ()).thenReturn (mockConfig);
		return manager;
	}

	@Test
	@Category (UnitTest.class)
	public void testCommandFileNameConfiguration() {
		// given
		Properties mockConfig = wrapIntoConfigProperties ("CommandFileName", "abcd");
		ConfigurationManager manager = mockedConfigurationManager (mockConfig);
		// when
		manager.loadSystemConfig ();
		// then
		ApplicationConfig config = ApplicationConfig.getInstance ();
		assertEquals ("Command Filename property not loaded correctly", "abcd",
				config.commandFilename ());
	}

	@Test
	@Category (UnitTest.class)
	public void testServerFileNameConfiguration() {
		// given
		Properties mockConfig = wrapIntoConfigProperties ("ServerFileName", "abcd");
		ConfigurationManager manager = mockedConfigurationManager (mockConfig);
		// when
		manager.loadSystemConfig ();
		// then
		ApplicationConfig config = ApplicationConfig.getInstance ();
		assertEquals ("Server Filename property not loaded correctly", "abcd",
				config.serverFilename ());
	}

	@Test
	@Category (UnitTest.class)
	public void testExecutableFileNameConfiguration() {
		// given
		Properties mockConfig = wrapIntoConfigProperties ("ExecutableFileName", "abcd");
		ConfigurationManager manager = mockedConfigurationManager (mockConfig);
		// when
		manager.loadSystemConfig ();
		// then
		ApplicationConfig config = ApplicationConfig.getInstance ();
		assertEquals ("Executable Filenameproperty not loaded correctly", "abcd",
				config.executableFilename ());
	}

	@Test
	@Category (UnitTest.class)
	public void testMonitorsFileNameConfiguration() {
		// given
		Properties mockConfig = wrapIntoConfigProperties ("MonitorsFileName", "abcd");
		ConfigurationManager manager = mockedConfigurationManager (mockConfig);
		// when
		manager.loadSystemConfig ();
		// then
		ApplicationConfig config = ApplicationConfig.getInstance ();
		assertEquals ("Monitors Filename property not loaded correctly", "abcd",
				config.monitorFilename ());
	}

	@Test
	@Category (UnitTest.class)
	public void testTokenSeperatorConfiguration() {
		// given
		Properties mockConfig = wrapIntoConfigProperties ("TokenSeperator", "abcd");
		ConfigurationManager manager = mockedConfigurationManager (mockConfig);
		// when
		manager.loadSystemConfig ();
		// then
		ApplicationConfig config = ApplicationConfig.getInstance ();
		assertEquals ("Token Seperator property not loaded correctly", "abcd",
				config.tokenSeperator ());
	}

	@Test
	@Category (UnitTest.class)
	public void testShellConsoleFolderConfiguration() {
		// given
		Properties mockConfig = wrapIntoConfigProperties ("ShellConsoleFolder", "abcd");
		ConfigurationManager manager = mockedConfigurationManager (mockConfig);
		// when
		manager.loadSystemConfig ();
		// then
		ApplicationConfig config = ApplicationConfig.getInstance ();
		assertEquals ("Shell Console Folder property not loaded correctly", "abcd",
				config.shellConsoleFolder ());
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConnectionTimeoutConfiguration() {
		// given
		Properties mockConfig = wrapIntoConfigProperties ("ServerConnectionTimeout", "abcd");
		ConfigurationManager manager = mockedConfigurationManager (mockConfig);
		// when
		manager.loadSystemConfig ();
		// then
		ApplicationConfig config = ApplicationConfig.getInstance ();
		assertEquals ("Server Connection Timeout property not loaded correctly", "abcd",
				config.serverConnectionTimeoutInMins ());
	}

	@Test
	@Category (UnitTest.class)
	public void testExecutableDelayConfiguration() {
		// given
		Properties mockConfig = wrapIntoConfigProperties ("ExecutableDelay", "abcd");
		ConfigurationManager manager = mockedConfigurationManager (mockConfig);
		// when
		manager.loadSystemConfig ();
		// then
		ApplicationConfig config = ApplicationConfig.getInstance ();
		assertEquals ("Executable Delay property not loaded correctly", "abcd",
				config.executableDelayInHours ());
	}

	@Test
	@Category (UnitTest.class)
	public void testMonitoringEnabledConfiguration() {
		// given
		Properties mockConfig = wrapIntoConfigProperties ("MonitoringEnabled", "abcd");
		ConfigurationManager manager = mockedConfigurationManager (mockConfig);
		// when
		manager.loadSystemConfig ();
		// then
		ApplicationConfig config = ApplicationConfig.getInstance ();
		assertEquals ("Monitoring Enabled property not loaded correctly", "abcd",
				config.monitoringEnabled ());
	}

	@Test
	@Category (UnitTest.class)
	public void testExecutableTimeoutConfiguration() {
		// given
		Properties mockConfig = wrapIntoConfigProperties ("ExecutableTimeout", "abcd");
		ConfigurationManager manager = mockedConfigurationManager (mockConfig);
		// when
		manager.loadSystemConfig ();
		// then
		ApplicationConfig config = ApplicationConfig.getInstance ();
		assertEquals ("Executable Timeout property not loaded correctly", "abcd",
				config.executableTimeoutInMins ());
	}

}
