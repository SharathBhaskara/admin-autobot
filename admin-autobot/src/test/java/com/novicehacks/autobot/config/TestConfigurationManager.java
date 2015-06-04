package com.novicehacks.autobot.config;

import java.util.Properties;

import  org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestConfigurationManager {
	ConfigParser parser;
	ConfigLoader loader;

	@Before
	public void setup() {
		parser = mock(ConfigParser.class);
		loader = mock (ConfigLoader.class);
	}

	private Properties wrapIntoConfigProperties(String key,String value) {
		Properties props = new Properties();
		props.put ("key", "value");
		return props;
	}
	
	@Test
	public void testResourceFolderConfiguration(){
		//given
		Properties mockConfig = wrapIntoConfigProperties("ResourceFolder", "abcd");
		when(parser.getConfigProperties ()).thenReturn (mockConfig);
		//when
		ConfigurationManager.loadSystemConfig ();
		ApplicationConfig config = ApplicationConfig.getInstance ();
		// then
		assertEquals("Monitors filename property not loaded correctly","abcd",config.resourceFolder ());
	}
	
//	ResourceFolder ("ResourceFolder", "."),
//	CommandFileName ("CommandFileName", "commands.txt"),
//	ServerFileName ("ServerFileName", "servers.txt"),
//	ExecutableFileName ("ExecutableFileName", "executables.txt"),
//	MonitorsFileName ("MonitorsFileName", "monitors.txt"),
//	TokenSeperator ("TokenSeperator", ":"),
//	ShellConsoleFolder ("ShellConsoleFolder", "."),
//	ServerConnectionTimeout ("ServerConnectionTimeout", "60"),
//	ExecutableDelay ("ExecutableDelay", "6"),
//	MonitoringEnabled ("MonitoringEnabled", "false"),
//	ExecutableTimeout ("ExecutableTimeout", "30");

}
