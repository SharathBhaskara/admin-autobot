package com.novicehacks.autobot;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.core.AllTCoreTests;
import com.novicehacks.autobot.executor.AllExecutorTests;
import com.novicehacks.autobot.logger.AllOutputLoggerTests;
import com.novicehacks.autobot.ssh.AllSSHTests;

/**
 * Includes both parameterized and unit tests.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
@RunWith (Suite.class)
@SuiteClasses ({ AllSSHTests.class, AllTCoreTests.class, AllOutputLoggerTests.class,
		AllExecutorTests.class })
public class AllTests {

}
