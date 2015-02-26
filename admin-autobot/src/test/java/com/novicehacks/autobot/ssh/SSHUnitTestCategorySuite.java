package com.novicehacks.autobot.ssh;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.categories.EnvironmentDependent;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.ssh.logger.SSHOutputLoggerTaskTest;

@RunWith (Categories.class)
@IncludeCategory (UnitTest.class)
@ExcludeCategory (EnvironmentDependent.class)
@SuiteClasses ({ DefaultSSHConnectionTest.class, DefaultSSHConnectionTestWithoutMocking.class,
		DefaultSSHSessionTest.class, DefaultSSHSessionTestWithoutMocking.class,
		ParallelCommandExecutorTaskTest.class, SequentialCommandExecutorTaskTest.class,
		SingleSessionCommandOutputGobblerTaskTest.class, SingleSessionCommandExecutorTest.class,
		ServerCommandProcessorTest.class, ServerConnectionHandleTest.class,
		SSHOutputLoggerTaskTest.class })
public class SSHUnitTestCategorySuite {

}
