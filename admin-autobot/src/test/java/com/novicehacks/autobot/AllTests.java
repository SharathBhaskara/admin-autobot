package com.novicehacks.autobot;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.core.CoreAllTests;
import com.novicehacks.autobot.ssh.SSHAllTests;

/**
 * Includes both parameterized and unit tests.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
@RunWith (Suite.class)
@SuiteClasses ({ SSHAllTests.class, CoreAllTests.class })
public class AllTests {

}
