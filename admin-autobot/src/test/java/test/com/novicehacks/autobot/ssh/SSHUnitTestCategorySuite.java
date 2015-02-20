package test.com.novicehacks.autobot.ssh;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import test.com.novicehacks.autobot.categories.EnvironmentDependent;
import test.com.novicehacks.autobot.categories.UnitTest;

@RunWith (Categories.class)
@IncludeCategory (UnitTest.class)
@ExcludeCategory (EnvironmentDependent.class)
@SuiteClasses ({ TestCustomizedSSHConnection.class,
		TestCustomizedSSHConnectionWithoutMocking.class, TestCustomizedSSHSession.class,
		TestCustomizedSSHSessionWithoutMocking.class, TestParallelCommandExecutorTask.class,
		TestSequentialCommandExecutorTask.class, TestShellCommandOutputGlobberTask.class,
		TestShellSequentialCommandExecutorTask.class, TestSSHOutputLoggerTask.class,
		TestSSHServerCommandProcessor.class, TestSSHServerConnectioHandle.class })
public class SSHUnitTestCategorySuite {

}
