package com.novicehacks.autobot.ssh;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.IgnoredTest;
import com.novicehacks.autobot.categories.UnitTest;

public class ServerCommandProcessorTaskTest {

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void instantiateWithNullServer() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void instantiateWithNullCommandCollection() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void instantiateWithNullCommandInCollection() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void instantiateWithCommandArray() {

	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class, IgnoredTest.class })
	public void connectServer() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void connectServerWhenAuthenticateException() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void connectServerWhenConnectException() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void disconnectWhenCommandExecutionFailed() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void disconnetWhenCommandExecutionPassed() {

	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class, IgnoredTest.class })
	public void parallelCommandExecutionInterrupted() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void parallelCommandExecutionStart() {

	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class, IgnoredTest.class })
	public void sequentialCommandExecutionStart() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void sequentialCommandExecutionInterrupted() {

	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class, IgnoredTest.class })
	public void waitForParallelCommandExecutionToComplete() {

	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class, IgnoredTest.class })
	public void wrapExceptionFromParallelCommandExecution() {

	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class, IgnoredTest.class })
	public void wrapExcpetionFromSequentialCommandExecution() {

	}

}
