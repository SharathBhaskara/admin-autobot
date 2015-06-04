package com.novicehacks.autobot.ssh.commandexecutor.parallel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.core.ThreadManager;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.ssh.SSHConnection;
import com.novicehacks.autobot.ssh.SSHSession;
import com.novicehacks.autobot.ssh.commandexecutor.parallel.ParallelExecutorTask;
import com.novicehacks.autobot.ssh.exception.CommandExecutionException;
import com.novicehacks.autobot.ssh.logger.ShellOutputLoggerTask;

public class ParallelCommandExecutorTaskTest {
	private Server server;
	private Command command;
	private SSHConnection connection;
	private SSHSession session;
	private ShellOutputLoggerTask outputLoggerTask;

	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@Before
	public void setUp() throws IOException, InterruptedException {
		MockitoAnnotations.initMocks (this);
		ThreadManager.getInstance ().createThreadPool ();
	}

	@Test
	@Category (UnitTest.class)
	public void nullCommandToTask() throws IOException {
		// given
		this.server = mock (Server.class);
		this.connection = mock (SSHConnection.class);
		// when
		this.command = null;
		// then
		this.exception.expect (CommandExecutionException.class);
		new ParallelExecutorTask (this.connection, this.server,
				this.command);
		fail ("Command cannot be null");
	}

	@Test
	@Category (UnitTest.class)
	public void nullConnectionToTask() {
		// given
		this.server = mock (Server.class);
		this.command = mock (Command.class);
		// when connection is set to null;
		this.connection = null;
		// then
		this.exception.expect (CommandExecutionException.class);
		new ParallelExecutorTask (this.connection, this.server,
				this.command);
		fail ("Connection cannot be null");
	}

	@Test
	@Category (UnitTest.class)
	public void nullServerToTask() throws IOException {
		// given
		this.command = mock (Command.class);
		this.connection = mock (SSHConnection.class);
		// when
		this.server = null;
		// then
		this.exception.expect (CommandExecutionException.class);
		new ParallelExecutorTask (this.connection, this.server,
				this.command);
		fail ("Server cannot be null");
	}

	@Test
	@Category ({ UnitTest.class })
	public void remoteExceptionOnCreateSession() throws IOException {
		// given
		mockCollaboratorsWithBasicOperations ();

		ParallelExecutorTask commandExecutor;
		commandExecutor = new ParallelExecutorTask (this.connection, this.server,
				this.command);
		// when
		when (this.connection.openSession ()).thenThrow (new IOException ());
		// then
		this.exception.expect (CommandExecutionException.class);
		commandExecutor.run ();

		assertFalse ("Create Session Should Fail.", commandExecutor.isCreateSessionCompleted ());
		fail ("Should have thrown exception, and session should notbe created");
	}

	private void mockCollaboratorsWithBasicOperations() throws IOException {
		// server mocking
		this.server = mock (Server.class);
		when (this.server.id ()).thenReturn ("T001");

		// command mocking
		this.command = mock (Command.class);
		when (this.command.commandTxt ()).thenReturn ("df -k");
		when (this.command.id ()).thenReturn ("TC01");

		// outputLoggertask mocking
		this.outputLoggerTask = mock (ShellOutputLoggerTask.class);
		doCallRealMethod ().when (this.outputLoggerTask).run ();

		// session mocking
		this.session = mock (SSHSession.class);
		byte[] errorStringBytes = "sample error".getBytes ();
		byte[] outputStringBytes = "sample output".getBytes ();

		doReturn (new ByteArrayInputStream (errorStringBytes)).when (this.session)
				.stdErrorStream ();
		doReturn (new ByteArrayInputStream (outputStringBytes)).when (this.session)
				.stdOutputStream ();
		doReturn (new ByteArrayInputStream (errorStringBytes)).when (this.session)
				.stdErrorStream ();
		doNothing ().when (this.session).execCommand (Mockito.anyString ());

		// connection mocking
		this.connection = mock (SSHConnection.class);
		when (this.connection.openSession ()).thenReturn (this.session);
		doNothing ().when (this.session).closeSession ();
	}

	@Test
	@Category ({ UnitTest.class })
	public void executeCommandWithoutCommandString() throws IOException {
		// given
		mockCollaboratorsWithBasicOperations ();

		ParallelExecutorTask commandExecutor;
		commandExecutor = new ParallelExecutorTask (this.connection, this.server,
				this.command);
		// when command string is null
		when (this.command.commandTxt ()).thenReturn (null);

		// then
		this.exception.expect (CommandExecutionException.class);
		this.exception.expectMessage (CoreMatchers
				.startsWith ("Invalid Command String in Command :"));
		commandExecutor.run ();

		assertFalse ("Command Execution Should Fail", commandExecutor.isExecuteCommandCompleted ());
		fail ("Command should not be executed");
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void sessionKilledWhenExecuteCommandFails() throws IOException {
		// given
		mockCollaboratorsWithBasicOperations ();

		ParallelExecutorTask commandExecutor;
		commandExecutor = new ParallelExecutorTask (this.connection, this.server,
				this.command);

		doThrow (new IOException ()).when (this.session).execCommand (Mockito.anyString ());
		// when
		try {
			commandExecutor.run ();
		} catch (CommandExecutionException ex) {
			assertFalse ("Command Execution Should not complete",
					commandExecutor.isExecuteCommandCompleted ());
		}
		// then
		verify (this.session, times (1)).execCommand (Mockito.anyString ());
		verify (this.session, times (1)).closeSession ();
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void sessionKilledWhenExecuteCommandPass() throws IOException {
		// given
		mockCollaboratorsWithBasicOperations ();

		ParallelExecutorTask commandExecutor;
		commandExecutor = new ParallelExecutorTask (this.connection, this.server,
				this.command);
		commandExecutor = Mockito.spy (commandExecutor);
		doReturn (this.outputLoggerTask).when (commandExecutor).getOutputLoggerTask ();

		// when
		try {
			commandExecutor.run ();
		} catch (CommandExecutionException ex) {
			assertTrue ("Command Execution Should not complete",
					commandExecutor.isExecuteCommandCompleted ());
		}
		// then
		verify (this.session, times (1)).execCommand (Mockito.anyString ());
		verify (this.session, times (1)).closeSession ();
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void validateCollectedCommandOutput() throws IOException {
		// given
		mockCollaboratorsWithBasicOperations ();

		String expected = "command output";
		doReturn (new ByteArrayInputStream (expected.getBytes ())).when (this.session)
				.stdOutputStream ();

		ParallelExecutorTask commandExecutor;
		commandExecutor = new ParallelExecutorTask (this.connection, this.server,
				this.command);
		// when
		commandExecutor.run ();
		String actual = commandExecutor.commandOutputFromRemote ();
		actual = actual.trim ();
		// then
		verify (this.session, times (1)).execCommand (Mockito.anyString ());
		assertEquals ("Command output should be same", expected, actual);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void loggingCommandOutput() throws IOException, InterruptedException {
		// given
		mockCollaboratorsWithBasicOperations ();

		ParallelExecutorTask commandExecutor;
		commandExecutor = new ParallelExecutorTask (this.connection, this.server,
				this.command);
		commandExecutor = spy (commandExecutor);
		doReturn (this.outputLoggerTask).when (commandExecutor).getOutputLoggerTask ();
		doCallRealMethod ().when (this.outputLoggerTask).run ();

		// when
		commandExecutor.run ();
		ThreadManager.getInstance ().terminateAndWaitForTaskCompletion (5, TimeUnit.SECONDS);
		// then
		verify (this.outputLoggerTask, times (1)).run ();
		assertTrue ("Output Logger Thread not Started", this.outputLoggerTask.isThreadStarted ());
	}
}
