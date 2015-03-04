package com.novicehacks.autobot.ssh;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.IgnoredTest;
import com.novicehacks.autobot.categories.UnitTest;

public class SequentialCommandExecutorTaskTest {

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void instantiateWithNullServer() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void instantiateWithNullConnection() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void instantiateWithNullCommandArray() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void instantiateWithNullCommandInCommandArray() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void createSessionThrowsExcpetion() {

	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class, IgnoredTest.class })
	public void initiateSessionTest() {
		/* verify sequence of execution of methos on session */
	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void initaiteSessionRequestTerminalThrowsException() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void initiateSessionStartShellThrowsException() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void initiateSessionRequestInputStreamThrowsException() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void initiateSessionRequestOutputStreamThrowsException() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void initiateSessionRequestErrorStreamThrowsException() {

	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class, IgnoredTest.class })
	public void testConfiguringSessionController() {

	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class, IgnoredTest.class })
	public void remoteOutputConsumerThreadStart() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void commandExecutionStarted() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void stopRemoteOuputConsumer() {

	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class, IgnoredTest.class })
	public void closingSession() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void closingSessionWhenRemoteOutputConsumerThreadInterrupted() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void closingSessionWhenExecutingCommands() {

	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void loggingOuput() {

	}
}
