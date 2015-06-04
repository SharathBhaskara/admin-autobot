package com.novicehacks.autobot.config;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.config.parser.TestParser;
import com.novicehacks.autobot.config.parser.TestServerParser;

@RunWith (Suite.class)
@SuiteClasses ({ TestConfigParser.class, TestConfigLoader.class, TestConfigurationManager.class,
		TestParser.class, TestServerParser.class })
public class AllConfigTests {

}
