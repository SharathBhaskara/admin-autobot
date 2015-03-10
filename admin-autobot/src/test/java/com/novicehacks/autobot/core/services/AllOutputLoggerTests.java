package com.novicehacks.autobot.core.services;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.core.services.impl.DefaultOutputFooterServiceTest;
import com.novicehacks.autobot.core.services.impl.DefaultOutputHeaderServiceTest;
import com.novicehacks.autobot.core.services.impl.DefaultOutputLoggerTaskTest;

@RunWith (Suite.class)
@SuiteClasses ({ DefaultOutputFooterServiceTest.class, DefaultOutputHeaderServiceTest.class,
		DefaultOutputLoggerTaskTest.class })
public class AllOutputLoggerTests {

}
