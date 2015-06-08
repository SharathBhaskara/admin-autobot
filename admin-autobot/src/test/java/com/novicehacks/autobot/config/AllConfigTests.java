package com.novicehacks.autobot.config;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({ TestApplicationConfig.class, TestCommandConfigLoader.class,
		TestApplicationConfigLoader.class, TestApplicationConfigParser.class, TestConfigurationManager.class,
		TestExecutableConfigLoader.class, TestResourceConfigLoader.class,
		TestResourceConfigParser.class, TestServerConfigLoader.class })
public class AllConfigTests {

}
