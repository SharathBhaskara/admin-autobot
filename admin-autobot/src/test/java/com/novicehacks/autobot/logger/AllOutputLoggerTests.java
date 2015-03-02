package com.novicehacks.autobot.logger;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith (Suite.class)
@SuiteClasses ({ DefaultOutputFooterServiceTest.class, DefaultOutputHeaderServiceTest.class,
		DefaultOutputLoggerTaskTest.class })
public class AllOutputLoggerTests {

}
