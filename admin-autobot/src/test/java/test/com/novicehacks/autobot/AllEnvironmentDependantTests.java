package test.com.novicehacks.autobot;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import test.com.novicehacks.autobot.ssh.SSHEnvironmentDependantTestSuite;

@RunWith (Categories.class)
@SuiteClasses ({ SSHEnvironmentDependantTestSuite.class })
public class AllEnvironmentDependantTests {

}
