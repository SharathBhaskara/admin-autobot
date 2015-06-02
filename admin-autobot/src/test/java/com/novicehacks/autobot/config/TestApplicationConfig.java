package com.novicehacks.autobot.config;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.UnitTest;

public class TestApplicationConfig {
	private ApplicationConfig config;

	@Before
	public void setup() {
		this.config = ApplicationConfig.getInstance ();
	}

	@Test
	@Category ({ UnitTest.class })
	public void singletonTest() {
		ApplicationConfig config1;
		ApplicationConfig config2;

		config1 = ApplicationConfig.getInstance ();
		config2 = ApplicationConfig.getInstance ();

		Assert.assertSame ("Two instances should be same, but are different", config1, config2);
	}

	@Test
	@Category ({ UnitTest.class })
	public void setResourceFolderValue() {
		final String expectedResourceFolder = "temp";

		this.config.setResourceFolder (expectedResourceFolder);

		String resourceFolder = this.config.resourceFolder ();
		assertEquals ("Resource folder value not loaded correctly", resourceFolder,
				expectedResourceFolder);
	}

	@Test
	@Category ({ UnitTest.class })
	public void defaultResourceFolderValue() {
		final String expectedResourceFolder = ConfigurationProperty.ResourceFolder.defaultValue ();

		this.config.setResourceFolder (null);

		String actualResourceFolder = this.config.resourceFolder ();
		assertEquals (expectedResourceFolder, actualResourceFolder);
	}

	@Test
	@Category ({ UnitTest.class })
	public void setCommandFilenameValue() {
		final String expectedCommandFilename = "c.txt";

		this.config.setCommandFilename (expectedCommandFilename);

		String commandFilename = this.config.commandFilename ();
		assertEquals ("Command filename not loaded correctly", commandFilename,
				expectedCommandFilename);
	}

	@Test
	@Category ({ UnitTest.class })
	public void defaultCommandFilenameValue() {
		final String expectedCommandFileName = ConfigurationProperty.CommandFileName
				.defaultValue ();

		this.config.setCommandFilename (null);

		String actualCommandFileName = this.config.commandFilename ();
		assertEquals ("CommandFilename default not loaded", expectedCommandFileName,
				actualCommandFileName);
	}

	@Test
	@Category ({ UnitTest.class })
	public void setServerFilenameValue() {
		final String expectedServerFilename = "s.txt";

		this.config.setServerFilename (expectedServerFilename);

		String serverFilename = this.config.serverFilename ();
		assertEquals ("Server filename not loaded correctly", serverFilename,
				expectedServerFilename);
	}

	@Test
	@Category ({ UnitTest.class })
	public void defaultServerFilenameValue() {
		final String expectedServerFileName = ConfigurationProperty.ServerFileName.defaultValue ();

		this.config.setServerFilename (null);

		String actualServerFileName = this.config.serverFilename ();
		assertEquals ("ServerFilename default not loaded", expectedServerFileName,
				actualServerFileName);
	}

	@Test
	@Category ({ UnitTest.class })
	public void setExecutableFilenameValue() {
		final String expectedExecutableFilename = "e.txt";

		this.config.setExecutableFilename (expectedExecutableFilename);

		String executableFilename = this.config.executableFilename ();
		assertEquals ("Executable filename not loaded correctly", executableFilename,
				expectedExecutableFilename);
	}

	@Test
	@Category ({ UnitTest.class })
	public void defaultExecutableFilenameValue() {
		final String expectedExecutableFileName = ConfigurationProperty.ExecutableFileName
				.defaultValue ();

		this.config.setExecutableFilename (null);

		String actualExecutableFileName = this.config.executableFilename ();
		assertEquals ("ExecutableFilename default not loaded", expectedExecutableFileName,
				actualExecutableFileName);
	}

	@Test
	@Category ({ UnitTest.class })
	public void setMonitorFilenameValue() {
		final String expectedMonitorFilename = "m.txt";

		this.config.setMonitorFilename (expectedMonitorFilename);

		String monitorFilename = this.config.monitorFilename ();
		assertEquals ("Monitor filename not loaded correctly", monitorFilename,
				expectedMonitorFilename);
	}

	@Test
	@Category ({ UnitTest.class })
	public void defaultMonitorFilenameValue() {
		final String expectedMonitorFileName = ConfigurationProperty.MonitorsFileName
				.defaultValue ();

		this.config.setMonitorFilename (null);

		String actualMonitorFileName = this.config.monitorFilename ();
		assertEquals ("MonitorFilename default not loaded", expectedMonitorFileName,
				actualMonitorFileName);
	}

	@Test
	@Category ({ UnitTest.class })
	public void setTokenSeperatorValue() {
		final String expectedTokenSeperator = "-";

		this.config.setTokenSeperator (expectedTokenSeperator);

		String tokenSeperator = this.config.tokenSeperator ();
		assertEquals ("TokenSeperator value not loaded correctly", tokenSeperator,
				expectedTokenSeperator);
	}

	@Test
	@Category ({ UnitTest.class })
	public void defaultTokenSeperator() {
		final String expectedTokenSeperator = ConfigurationProperty.TokenSeperator.defaultValue ();

		this.config.setTokenSeperator (null);

		String tokenSeperator = this.config.tokenSeperator ();
		assertEquals ("TokenSeperator default not loaded", expectedTokenSeperator, tokenSeperator);
	}

	@Test
	@Category ({ UnitTest.class })
	public void setShellConsoleFolderValue() {
		final String expectedShellConsoleFolder = "temp";

		this.config.setShellConsoleFolder (expectedShellConsoleFolder);

		String shellConsoleFolder = this.config.shellConsoleFolder ();
		assertEquals ("Shell console folder value not loaded correctly", shellConsoleFolder,
				expectedShellConsoleFolder);
	}

	@Test
	@Category ({ UnitTest.class })
	public void defaultShellConsoleFolder() {
		final String expectedConsoleFolder = ConfigurationProperty.ShellConsoleFolder
				.defaultValue ();

		this.config.setShellConsoleFolder (null);

		String shellConsoleFolder = this.config.shellConsoleFolder ();
		assertEquals ("ShellConsoleFolder default not loaded", expectedConsoleFolder,
				shellConsoleFolder);
	}

	@Test
	@Category ({ UnitTest.class })
	public void setServerConnectionTimeoutValue() {
		final String expectedConnectionTimeout = "45";

		this.config.setServerConnectionTimeoutInMins (expectedConnectionTimeout);

		String connectionTimeout = this.config.serverConnectionTimeoutInMins ();
		assertEquals ("Server Connection Timeout not loaded correctly", connectionTimeout,
				expectedConnectionTimeout);
	}

	@Test
	@Category ({ UnitTest.class })
	public void defaultServerConnectionTimeout() {
		final String expectedConnectionTimeout = ConfigurationProperty.ServerConnectionTimeout
				.defaultValue ();

		this.config.setServerConnectionTimeoutInMins (null);

		String serverConnectionTimeout = this.config.serverConnectionTimeoutInMins ();
		assertEquals ("ServerConnectionTimout default not loaded", expectedConnectionTimeout,
				serverConnectionTimeout);
	}

	@Test
	@Category ({ UnitTest.class })
	public void setExecutableTimeoutValue() {
		final String expectedExecutableTimeout = "30";

		this.config.setExecutableTimeoutInMins (expectedExecutableTimeout);

		String executableTimeout = this.config.executableTimeoutInMins ();
		assertEquals ("Executable timeout not loaded correctly", executableTimeout,
				expectedExecutableTimeout);
	}

	@Test
	@Category ({ UnitTest.class })
	public void defaultExecutableTimeout() {
		final String expectedExecutableTimeout = ConfigurationProperty.ExecutableTimeout
				.defaultValue ();

		this.config.setExecutableTimeoutInMins (null);

		String executableTimeout = this.config.executableTimeoutInMins ();
		assertEquals ("Executable timeout default not loaded", expectedExecutableTimeout,
				executableTimeout);
	}

	@Test
	@Category ({ UnitTest.class })
	public void setExecutableDelayValue() {
		final String expectedExecutableDelay = "1";

		this.config.setExecutableDelayInHours (expectedExecutableDelay);

		String executableDelay = this.config.executableDelayInHours ();
		assertEquals ("Exectable delay not being loaded correctly", executableDelay,
				expectedExecutableDelay);
	}

	@Test
	@Category ({ UnitTest.class })
	public void defaultExecutableDelay() {
		final String expectedExecutableDelay = ConfigurationProperty.ExecutableDelay
				.defaultValue ();

		this.config.setExecutableDelayInHours (null);

		String executableDelay = this.config.executableDelayInHours ();
		assertEquals ("Executable delay default not loaded", executableDelay,
				expectedExecutableDelay);
	}

	@Test
	@Category ({ UnitTest.class })
	public void setMonitoringEnabledValue() {
		final String expectedMonitoringEnabledFlag = "true";

		this.config.setMonitoringEnabled (expectedMonitoringEnabledFlag);

		String monitoringEnabled = this.config.monitoringEnabled ();
		assertEquals ("Monitoring enabled flag not loaded correctly", monitoringEnabled,
				expectedMonitoringEnabledFlag);
	}

	@Test
	@Category ({ UnitTest.class })
	public void defaultMonitoringEnabledValue() {
		final String expectedMonitoringFlag = ConfigurationProperty.MonitoringEnabled
				.defaultValue ();

		this.config.setMonitoringEnabled (null);

		String monitoringEnabled = this.config.monitoringEnabled ();
		assertEquals ("Monitoring flag default value not loaded", monitoringEnabled,
				expectedMonitoringFlag);
	}

	@Test
	@Category ({ UnitTest.class })
	public void unloadCommandFilename() {
		this.config.setCommandFilename ("c.txt");
		this.config.unload ();
		testDefaultValue (ConfigurationProperty.CommandFileName);
	}

	@Test
	@Category ({ UnitTest.class })
	public void unloadExecutableDelay() {
		this.config.setExecutableDelayInHours ("4");
		this.config.unload ();
		testDefaultValue (ConfigurationProperty.ExecutableDelay);
	}

	@Test
	@Category ({ UnitTest.class })
	public void unloadExecutableFilename() {
		this.config.setExecutableFilename ("e.txt");
		this.config.unload ();
		testDefaultValue (ConfigurationProperty.ExecutableFileName);

	}

	@Test
	@Category ({ UnitTest.class })
	public void unloadExecutableTimeout() {
		this.config.setExecutableTimeoutInMins ("15");
		this.config.unload ();
		testDefaultValue (ConfigurationProperty.ExecutableTimeout);
	}

	@Test
	@Category ({ UnitTest.class })
	public void unloadMonitorFilename() {
		this.config.setMonitorFilename ("m.txt");
		this.config.unload ();
		testDefaultValue (ConfigurationProperty.MonitorsFileName);
	}

	@Test
	@Category ({ UnitTest.class })
	public void unloadMonitoringEnabledFlag() {
		this.config.setMonitoringEnabled ("true");
		this.config.unload ();
		testDefaultValue (ConfigurationProperty.MonitoringEnabled);
	}

	@Test
	@Category ({ UnitTest.class })
	public void unloadResourceFolder() {
		this.config.setResourceFolder ("temp");
		this.config.unload ();
		testDefaultValue (ConfigurationProperty.ResourceFolder);
	}

	@Test
	@Category ({ UnitTest.class })
	public void unloadServerConnectionTimeout() {
		this.config.setServerConnectionTimeoutInMins ("5");
		this.config.unload ();
		testDefaultValue (ConfigurationProperty.ServerConnectionTimeout);
	}

	@Test
	@Category ({ UnitTest.class })
	public void unloadServerFilename() {
		this.config.setServerFilename ("s.txt");
		this.config.unload ();
		testDefaultValue (ConfigurationProperty.ServerFileName);
	}

	@Test
	@Category ({ UnitTest.class })
	public void unloadShellConsoleFolder() {
		this.config.setShellConsoleFolder ("temp");
		this.config.unload ();
		testDefaultValue (ConfigurationProperty.ShellConsoleFolder);
	}

	@Test
	@Category ({ UnitTest.class })
	public void unloadTokenSeperator() {
		this.config.setTokenSeperator ("-");
		this.config.unload ();
		testDefaultValue (ConfigurationProperty.TokenSeperator);
	}

	private void testDefaultValue(ConfigurationProperty property) {

		switch (property) {
		case TokenSeperator:
			assertEquals ("Token Seperator did not reset", this.config.tokenSeperator (),
					property.defaultValue ());
			break;
		case ShellConsoleFolder:
			assertEquals (" Shell console folder did not reset", this.config.shellConsoleFolder (),
					property.defaultValue ());
			break;
		case CommandFileName:
			assertEquals ("Command filename  did not reset", this.config.commandFilename (),
					property.defaultValue ());
			break;
		case ExecutableDelay:
			assertEquals (" Executable delay did not reset", this.config.executableDelayInHours (),
					property.defaultValue ());
			break;
		case ExecutableFileName:
			assertEquals (" Executable filename did not reset", this.config.executableFilename (),
					property.defaultValue ());
			break;
		case ExecutableTimeout:
			assertEquals (" Executable timeout did not reset",
					this.config.executableTimeoutInMins (), property.defaultValue ());
			break;
		case MonitoringEnabled:
			assertEquals (" Monitoring enabled flag did not reset",
					this.config.monitoringEnabled (), property.defaultValue ());
			break;
		case MonitorsFileName:
			assertEquals (" Monitors filename did not reset", this.config.monitorFilename (),
					property.defaultValue ());
			break;
		case ResourceFolder:
			assertEquals (" Resource folder did not reset", this.config.resourceFolder (),
					property.defaultValue ());
			break;
		case ServerConnectionTimeout:
			assertEquals (" Server connection timeout did not reset",
					this.config.serverConnectionTimeoutInMins (), property.defaultValue ());
			break;
		case ServerFileName:
			assertEquals (" Server filename did not reset", this.config.serverFilename (),
					property.defaultValue ());
			break;
		default:
			Assert.fail ("Invalid Property passed by test method");
		}

	}
}
