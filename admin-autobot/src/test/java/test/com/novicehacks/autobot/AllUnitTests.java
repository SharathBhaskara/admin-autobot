package test.com.novicehacks.autobot;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import test.com.novicehacks.autobot.core.CoreUnitTestSuite;
import test.com.novicehacks.autobot.ssh.SSHUnitTestCategorySuite;

@RunWith (Categories.class)
@SuiteClasses ({ SSHUnitTestCategorySuite.class, CoreUnitTestSuite.class })
public class AllUnitTests {

}
