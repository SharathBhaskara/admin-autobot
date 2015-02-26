package com.novicehacks.autobot.ssh;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.categories.EnvironmentDependent;

@RunWith (Categories.class)
@IncludeCategory (EnvironmentDependent.class)
@SuiteClasses ({ DefaultSSHConnectionTest.class, DefaultSSHConnectionTestWithoutMocking.class,
		DefaultSSHSessionTest.class, DefaultSSHSessionTestWithoutMocking.class,
		ParallelCommandExecutorTaskTest.class, SequentialCommandExecutorTaskTest.class,
		SingleSessionCommandOutputGobblerTaskTest.class, SingleSessionCommandExecutorTest.class,
		ServerCommandProcessorTest.class, ServerConnectionHandleTest.class })
public class SSHEnvironmentDependantTestSuite {

}
