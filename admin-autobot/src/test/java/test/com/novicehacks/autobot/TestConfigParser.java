package test.com.novicehacks.autobot;

import static org.junit.Assert.assertNotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.novicehacks.autobot.config.AutobotConfigManager;
import com.novicehacks.autobot.config.ResourceConfig;
import com.novicehacks.autobot.config.parser.ConfigParser;

public class TestConfigParser {
	Logger logger = LogManager.getLogger();

	@Test
	@Ignore
	public void testConfigLoading() {
		ConfigParser parser;
		parser = ConfigParser.getIntance();
		logger.debug("Command Resource: [{}]", parser.commandResource());
		assertNotNull(parser.commandResource());
		logger.debug("Executable Resource: [{}]", parser.executableResource());
		assertNotNull(parser.executableResource());
		logger.debug("Monitor Resource: [{}]", parser.monitorResource());
		assertNotNull(parser.monitorResource());
		logger.debug("Server Resource: [{}]", parser.serverResource());
		assertNotNull(parser.serverResource());
	}

	@Test
	public void testResourceLoading() throws InterruptedException,
			ExecutionException, TimeoutException {
		AutobotConfigManager.loadSystemConfig();
		ResourceConfig resourceConfig = AutobotConfigManager
				.loadResourceConfig();
		logger.info("Commands : {}", resourceConfig.commands());
		logger.info("Servers : {}", resourceConfig.servers());
		logger.info("Monitors : {}", resourceConfig.monitors());
		logger.info("Executable : {}", resourceConfig.executables());
	}

}
