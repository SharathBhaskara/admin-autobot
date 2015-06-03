package com.novicehacks.autobot.config;

import static org.junit.Assert.assertNotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.EnvironmentalTest;
import com.novicehacks.autobot.config.ResourceConfigLoader;
import com.novicehacks.autobot.config.ResourceConfig;

public class TestConfigParser {
	Logger	logger	= LogManager.getLogger ();

	@Test
	@Category (EnvironmentalTest.class)
	public void testConfigLoading() {
		ConfigParser parser;
		parser = ConfigParser.getIntance ();
		logger.debug ("Command Resource: [{}]", parser.commandResource ());
		assertNotNull (parser.commandResource ());
		logger.debug ("Executable Resource: [{}]", parser.executableResource ());
		assertNotNull (parser.executableResource ());
		logger.debug ("Monitor Resource: [{}]", parser.monitorResource ());
		assertNotNull (parser.monitorResource ());
		logger.debug ("Server Resource: [{}]", parser.serverResource ());
		assertNotNull (parser.serverResource ());
	}

	@Test
	@Category (EnvironmentalTest.class)
	public void testResourceLoading() throws InterruptedException, ExecutionException,
			TimeoutException {
		ConfigurationManager.loadSystemConfig ();
		ResourceConfig resourceConfig = new ResourceConfigLoader().loadResourceConfig ();
		logger.info ("Commands : {}", resourceConfig.commands ());
		logger.info ("Servers : {}", resourceConfig.servers ());
		logger.info ("Executable : {}", resourceConfig.executables ());
	}

}
