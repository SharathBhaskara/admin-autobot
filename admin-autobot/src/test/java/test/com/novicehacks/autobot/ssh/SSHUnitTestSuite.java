package test.com.novicehacks.autobot.ssh;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import test.com.novicehacks.autobot.categories.UnitTest;

@RunWith (Categories.class)
@IncludeCategory (UnitTest.class)
@SuiteClasses ({ TestCustomizedSSHConnection.class })
public class SSHUnitTestSuite {

}
