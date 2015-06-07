package com.novicehacks.autobot.config;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({ TestConfigParser.class, TestConfigLoader.class, TestConfigurationManager.class,
		TestServerConfigLoader.class, TestCommandConfigLoader.class,
		TestExecutableConfigLoader.class })
public class AllConfigTests {

}
