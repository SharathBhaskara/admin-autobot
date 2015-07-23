package com.novicehacks.autobot.executor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.novicehacks.autobot.executor.ssh.DefaultSSHConnectionStringParameterizedTest;
import com.novicehacks.autobot.executor.ssh.DefaultSSHConnectionTest;
import com.novicehacks.autobot.executor.ssh.DefaultSSHConnectionTestWithoutMocking;
import com.novicehacks.autobot.executor.ssh.DefaultSSHConnectionUtilTest;
import com.novicehacks.autobot.executor.ssh.DefaultSSHSessionTest;
import com.novicehacks.autobot.executor.ssh.DefaultSSHSessionTestWithoutMocking;
import com.novicehacks.autobot.executor.ssh.SSHCommandExecutorServiceTaskFunctionalTest;
import com.novicehacks.autobot.executor.ssh.SSHCommandExecutorServiceTaskTest;
import com.novicehacks.autobot.executor.ssh.logger.ShellOutputFooterServiceTest;
import com.novicehacks.autobot.executor.ssh.logger.ShellOutputHeaderServiceTest;
import com.novicehacks.autobot.executor.ssh.logger.ShellOutputLoggerTaskTest;
import com.novicehacks.autobot.executor.ssh.parallel.ParallelCommandExecutorTaskTest;
import com.novicehacks.autobot.executor.ssh.sequential.SequentialCommandExecutorTaskTest;

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
