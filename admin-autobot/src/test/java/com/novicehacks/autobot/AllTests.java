package com.novicehacks.autobot;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.config.AllConfigTests;
import com.novicehacks.autobot.core.AllCoreTests;
import com.novicehacks.autobot.executor.AllExecutorTests;
import com.novicehacks.autobot.ssh.AllSSHTests;

/**
 * Includes both parameterized and unit tests.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
@RunWith (Suite.class)
@SuiteClasses ({ AllConfigTests.class, AllSSHTests.class, AllCoreTests.class,
		AllExecutorTests.class })
public class AllTests {

}
