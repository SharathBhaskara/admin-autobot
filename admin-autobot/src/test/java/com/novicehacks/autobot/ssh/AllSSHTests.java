package com.novicehacks.autobot.ssh;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.ssh.logger.ShellOutputFooterServiceTest;
import com.novicehacks.autobot.ssh.logger.ShellOutputHeaderServiceTest;
import com.novicehacks.autobot.ssh.logger.ShellOutputLoggerTaskTest;

@RunWith (Suite.class)
@SuiteClasses ({ DefaultSSHConnectionStringParameterizedTest.class, DefaultSSHConnectionTest.class,
		DefaultSSHConnectionTestWithoutMocking.class, DefaultSSHSessionTest.class,
		DefaultSSHSessionTestWithoutMocking.class, ParallelCommandExecutorTaskTest.class,
		SequentialCommandExecutorTaskTest.class, ServerCommandProcessorTaskTest.class,
		ServerConnectionHandleTest.class, ShellOutputLoggerTaskTest.class,
		ShellOutputHeaderServiceTest.class, ShellOutputFooterServiceTest.class })
public class AllSSHTests {

}
