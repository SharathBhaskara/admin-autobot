package test.com.novicehacks.autobot;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.com.novicehacks.autobot.ssh.SSHAllTests;

@RunWith (Suite.class)
@SuiteClasses (SSHAllTests.class)
public class AllTests {

}
