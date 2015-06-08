package com.novicehacks.autobot.config;

import static org.junit.Assert.*;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.UnitTest;
/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class TestApplicationConfigParser {
	private static ApplicationConfigParser parser;
	Logger logger = LogManager.getLogger ();

	@BeforeClass
	public static void setup() {
		parser = ApplicationConfigParser.getIntance ();
	}

	@Test
	@Category (UnitTest.class)
	public void testConfigLoading() {
		logger.debug ("Command Resource: [{}]", parser.absoluteCommandResourcePath ());
		assertNotNull (parser.absoluteCommandResourcePath ());
		logger.debug ("Executable Resource: [{}]", parser.absoluteExecutableResourcePath ());
		assertNotNull (parser.absoluteExecutableResourcePath ());
		logger.debug ("Monitor Resource: [{}]", parser.absoluteMonitorResourcePath ());
		assertNotNull (parser.absoluteMonitorResourcePath ());
		logger.debug ("Server Resource: [{}]", parser.absoluteServerResourcePath ());
		assertNotNull (parser.absoluteServerResourcePath ());
	}

	@Test
	@Category (UnitTest.class)
	public void testResourceFolderConfig() {
		Properties props;
		props = parser.getConfigProperties ();
		assertTrue (props.containsKey ("ResourceFolder"));
		assertNotNull (props.get ("ResourceFolder"));
	}

	@Test
	@Category (UnitTest.class)
	public void testCommandFileNameConfig() {
		Properties props;
		props = parser.getConfigProperties ();
		assertTrue (props.containsKey ("CommandFileName"));
		assertNotNull (props.get ("CommandFileName"));
	}

	@Test
	@Category (UnitTest.class)
	public void testServerFileNameConfig() {
		Properties props;
		props = parser.getConfigProperties ();
		assertTrue (props.containsKey ("ServerFileName"));
		assertNotNull (props.get ("ServerFileName"));
	}

	@Test
	@Category (UnitTest.class)
	public void testExecutableFileNameConfig() {
		Properties props;
		props = parser.getConfigProperties ();
		assertTrue (props.containsKey ("ExecutableFileName"));
		assertNotNull (props.get ("ExecutableFileName"));
	}

	@Test
	@Category (UnitTest.class)
	public void testMonitorsFileNameConfig() {
		Properties props;
		props = parser.getConfigProperties ();
		assertTrue (props.containsKey ("MonitorsFileName"));
		assertNotNull (props.get ("MonitorsFileName"));
	}

	@Test
	@Category (UnitTest.class)
	public void testTokenSeperatorConfig() {
		Properties props;
		props = parser.getConfigProperties ();
		assertTrue (props.containsKey ("TokenSeperator"));
		assertNotNull (props.get ("TokenSeperator"));
	}

	@Test
	@Category (UnitTest.class)
	public void testShellConsoleFolderConfig() {
		Properties props;
		props = parser.getConfigProperties ();
		assertTrue (props.containsKey ("ShellConsoleFolder"));
		assertNotNull (props.get ("ShellConsoleFolder"));
	}

	@Test
	@Category (UnitTest.class)
	public void testServerConnectionTimeoutConfig() {
		Properties props;
		props = parser.getConfigProperties ();
		assertTrue (props.containsKey ("ServerConnectionTimeout"));
		assertNotNull (props.get ("ServerConnectionTimeout"));
	}

	@Test
	@Category (UnitTest.class)
	public void testExecutableDelayConfig() {
		Properties props;
		props = parser.getConfigProperties ();
		assertTrue (props.containsKey ("ExecutableDelay"));
		assertNotNull (props.get ("ExecutableDelay"));
	}

	@Test
	@Category (UnitTest.class)
	public void testMonitoringEnabledConfig() {
		Properties props;
		props = parser.getConfigProperties ();
		assertTrue (props.containsKey ("MonitoringEnabled"));
		assertNotNull (props.get ("MonitoringEnabled"));
	}

	@Test
	@Category (UnitTest.class)
	public void testExecutableTimeoutConfig() {
		Properties props;
		props = parser.getConfigProperties ();
		assertTrue (props.containsKey ("ExecutableTimeout"));
		assertNotNull (props.get ("ExecutableTimeout"));
	}


}
