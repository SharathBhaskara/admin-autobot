package test.com.novicehacks.autobot;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import test.com.novicehacks.autobot.ssh.SSHIntegrationTestSuite;

@RunWith (Categories.class)
@SuiteClasses ({ SSHIntegrationTestSuite.class })
public class AllIntegrationTests {

}
