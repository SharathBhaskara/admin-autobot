package com.novicehacks.autobot.executor.ssh;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.executor.ssh.commandexecutor.DefaultSSHConnectionUtilTest;
import com.novicehacks.autobot.executor.ssh.commandexecutor.SSHCommandExecutorServiceTaskFunctionalTest;
import com.novicehacks.autobot.executor.ssh.commandexecutor.SSHCommandExecutorServiceTaskTest;
import com.novicehacks.autobot.executor.ssh.commandexecutor.parallel.ParallelCommandExecutorTaskTest;
import com.novicehacks.autobot.executor.ssh.commandexecutor.sequential.SequentialCommandExecutorTaskTest;
import com.novicehacks.autobot.executor.ssh.logger.ShellOutputFooterServiceTest;
import com.novicehacks.autobot.executor.ssh.logger.ShellOutputHeaderServiceTest;
import com.novicehacks.autobot.executor.ssh.logger.ShellOutputLoggerTaskTest;

@RunWith (Suite.class)
@SuiteClasses ({ DefaultSSHConnectionStringParameterizedTest.class, DefaultSSHConnectionTest.class,
		DefaultSSHConnectionTestWithoutMocking.class, DefaultSSHSessionTest.class,
		DefaultSSHSessionTestWithoutMocking.class, ParallelCommandExecutorTaskTest.class,
		SequentialCommandExecutorTaskTest.class, SSHCommandExecutorServiceTaskFunctionalTest.class,
		SSHCommandExecutorServiceTaskTest.class, DefaultSSHConnectionUtilTest.class,
		ShellOutputLoggerTaskTest.class, ShellOutputHeaderServiceTest.class,
		ShellOutputFooterServiceTest.class })
public class AllSSHTests {

}
