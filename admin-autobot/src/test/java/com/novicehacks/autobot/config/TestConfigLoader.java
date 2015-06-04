package com.novicehacks.autobot.config;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.UnitTest;

/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class TestConfigLoader {

	@Before
	public void setup() {
		ApplicationConfig.getInstance ().unload ();
	}

	@After
	public void tearDown() {
		ApplicationConfig.getInstance ().unload ();
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingResourceFolderConfigThroughConstructor() {
		// given
		Properties props = wrapIntoProperites ("ResourceFolder", "abcd");
		ConfigLoader loader = new ConfigLoader (props);
		// when
		loader.loadApplicationConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Resource folder config not loaded through constructor", "abcd",
				config.resourceFolder ());
	}

	private Properties wrapIntoProperites(String key, String value) {
		Properties props = new Properties ();
		props.put (key, value);
		return props;
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingServerFileNameThroghConstructor() {
		// given
		Properties props = wrapIntoProperites ("ServerFileName", "abcd");
		ConfigLoader loader = new ConfigLoader (props);
		// when
		loader.loadApplicationConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Server filename config not loaded through constructor", "abcd",
				config.serverFilename ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingExecutableFileNameThroghConstructor() {
		// given
		Properties props = wrapIntoProperites ("ExecutableFileName", "abcd");
		ConfigLoader loader = new ConfigLoader (props);
		// when
		loader.loadApplicationConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Executable filename config not loaded through constructor", "abcd",
				config.executableFilename ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingMonitorsFileNameThroghConstructor() {
		// given
		Properties props = wrapIntoProperites ("MonitorsFileName", "abcd");
		ConfigLoader loader = new ConfigLoader (props);
		// when
		loader.loadApplicationConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Monitors filename config not loaded through constructor", "abcd",
				config.monitorFilename ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingTokenSeperatorThroghConstructor() {
		// given
		Properties props = wrapIntoProperites ("TokenSeperator", "abcd");
		ConfigLoader loader = new ConfigLoader (props);
		// when
		loader.loadApplicationConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Token Seperator config not loaded through constructor", "abcd",
				config.tokenSeperator ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingShellConsoleFolderThroghConstructor() {
		// given
		Properties props = wrapIntoProperites ("ShellConsoleFolder", "abcd");
		ConfigLoader loader = new ConfigLoader (props);
		// when
		loader.loadApplicationConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Shell Console Folder config not loaded through constructor", "abcd",
				config.shellConsoleFolder ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingServerConnectionTimeoutThroghConstructor() {
		// given
		Properties props = wrapIntoProperites ("ServerConnectionTimeout", "abcd");
		ConfigLoader loader = new ConfigLoader (props);
		// when
		loader.loadApplicationConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Server Connection Timeout config not loaded through constructor", "abcd",
				config.serverConnectionTimeoutInMins ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingExecutableDelayThroghConstructor() {
		// given
		Properties props = wrapIntoProperites ("ExecutableDelay", "abcd");
		ConfigLoader loader = new ConfigLoader (props);
		// when
		loader.loadApplicationConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Executable Delay config not loaded through constructor", "abcd",
				config.executableDelayInHours ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingMonitoringEnabledThroghConstructor() {
		// given
		Properties props = wrapIntoProperites ("MonitoringEnabled", "abcd");
		ConfigLoader loader = new ConfigLoader (props);
		// when
		loader.loadApplicationConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Monitoring Enabled config not loaded through constructor", "abcd",
				config.monitoringEnabled ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingExecutableTimeoutThroghConstructor() {
		// given
		Properties props = wrapIntoProperites ("ExecutableTimeout", "abcd");
		ConfigLoader loader = new ConfigLoader (props);
		// when
		loader.loadApplicationConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Executable Timeout config not loaded through constructor", "abcd",
				config.executableTimeoutInMins ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingCommandFilenameThroghConstructor() {
		// given
		Properties props = wrapIntoProperites ("CommandFileName", "abcd");
		ConfigLoader loader = new ConfigLoader (props);
		// when
		loader.loadApplicationConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Command Filename config not loaded through constructor", "abcd",
				config.commandFilename ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingResourceFolderConfig() {
		// given
		Properties props = wrapIntoProperites ("ResourceFolder", "abcd");
		ConfigLoader loader = new ConfigLoader ();
		// when
		loader.loadApplicationConfig (props);
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Resource folder config not loaded through method", "abcd",
				config.resourceFolder ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingServerFileName() {
		// given
		Properties props = wrapIntoProperites ("ServerFileName", "abcd");
		ConfigLoader loader = new ConfigLoader ();
		// when
		loader.loadApplicationConfig (props);
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Server filename config not loaded through method", "abcd",
				config.serverFilename ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingExecutableFileName() {
		// given
		Properties props = wrapIntoProperites ("ExecutableFileName", "abcd");
		ConfigLoader loader = new ConfigLoader ();
		// when
		loader.loadApplicationConfig (props);
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Executable filename config not loaded through method", "abcd",
				config.executableFilename ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingMonitorsFileName() {
		// given
		Properties props = wrapIntoProperites ("MonitorsFileName", "abcd");
		ConfigLoader loader = new ConfigLoader ();
		// when
		loader.loadApplicationConfig (props);
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Monitors filename config not loaded through method", "abcd",
				config.monitorFilename ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingTokenSeperator() {
		// given
		Properties props = wrapIntoProperites ("TokenSeperator", "abcd");
		ConfigLoader loader = new ConfigLoader ();
		// when
		loader.loadApplicationConfig (props);
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Token Seperator config not loaded through method", "abcd",
				config.tokenSeperator ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingShellConsoleFolder() {
		// given
		Properties props = wrapIntoProperites ("ShellConsoleFolder", "abcd");
		ConfigLoader loader = new ConfigLoader ();
		// when
		loader.loadApplicationConfig (props);
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Shell Console Folder config not loaded through method", "abcd",
				config.shellConsoleFolder ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingServerConnectionTimeout() {
		// given
		Properties props = wrapIntoProperites ("ServerConnectionTimeout", "abcd");
		ConfigLoader loader = new ConfigLoader ();
		// when
		loader.loadApplicationConfig (props);
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Server Connection Timeout config not loaded through method", "abcd",
				config.serverConnectionTimeoutInMins ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingExecutableDelay() {
		// given
		Properties props = wrapIntoProperites ("ExecutableDelay", "abcd");
		ConfigLoader loader = new ConfigLoader ();
		// when
		loader.loadApplicationConfig (props);
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Executable Delay config not loaded through method", "abcd",
				config.executableDelayInHours ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingMonitoringEnabled() {
		// given
		Properties props = wrapIntoProperites ("MonitoringEnabled", "abcd");
		ConfigLoader loader = new ConfigLoader ();
		// when
		loader.loadApplicationConfig (props);
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Monitoring Enabled config not loaded through method", "abcd",
				config.monitoringEnabled ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingExecutableTimeout() {
		// given
		Properties props = wrapIntoProperites ("ExecutableTimeout", "abcd");
		ConfigLoader loader = new ConfigLoader ();
		// when
		loader.loadApplicationConfig (props);
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Executable Timeout config not loaded through method", "abcd",
				config.executableTimeoutInMins ());
	}

	@Test
	@Category ({ UnitTest.class })
	public void loadingCommandFilename() {
		// given
		Properties props = wrapIntoProperites ("CommandFileName", "abcd");
		ConfigLoader loader = new ConfigLoader ();
		// when
		loader.loadApplicationConfig (props);
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals ("Command Filename config not loaded through method", "abcd",
				config.commandFilename ());
	}

}
