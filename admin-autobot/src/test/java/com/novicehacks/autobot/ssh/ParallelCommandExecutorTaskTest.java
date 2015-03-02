package com.novicehacks.autobot.ssh;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.ssh.exception.CommandExecutionException;
import com.novicehacks.autobot.ssh.logger.ShellOutputLoggerTask;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;
import com.novicehacks.autobot.types.ShellCommand;
import com.novicehacks.autobot.types.UnixServer;

public class ParallelCommandExecutorTaskTest {
	private ParallelCommandExecutorTask commandExecutor;
	private Server server;
	private Command command;
	private SSHConnection connection;
	private SSHSession session;
	private ShellOutputLoggerTask outputLoggerTask;

	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@Before
	public void setUp() throws IOException {
		this.connection = mock (SSHConnection.class);
		this.session = mock (SSHSession.class);
		this.server = mock (UnixServer.class);
		this.command = mock (ShellCommand.class);
		this.outputLoggerTask = mock (ShellOutputLoggerTask.class);

		when (this.connection.openSession ()).thenReturn (this.session);
		when (this.session.stdInputStream ()).thenReturn (SSHTestUtilities.tempOutputStream ());
		when (this.session.stdOutputStream ()).thenReturn (SSHTestUtilities.tempInputStream ());
		doNothing ().when (this.outputLoggerTask).run ();
	}

	@Test
	@Category (UnitTest.class)
	public void nullCommandToTask() {
		// when
		this.command = null;
		// then
		this.exception.expect (CommandExecutionException.class);
		this.commandExecutor = new ParallelCommandExecutorTask (this.connection, this.server,
				this.command);
		fail ("Command cannot be null");
	}

	@Test
	@Category (UnitTest.class)
	public void nullConnectionToTask() {
		// when
		this.connection = null;
		// then
		this.exception.expect (CommandExecutionException.class);
		this.commandExecutor = new ParallelCommandExecutorTask (this.connection, this.server,
				this.command);
		fail ("Connection cannot be null");
	}

	@Test
	@Category (UnitTest.class)
	public void nullServerToTask() throws IOException {
		// when
		this.server = null;
		// then
		this.exception.expect (CommandExecutionException.class);
		this.commandExecutor = new ParallelCommandExecutorTask (this.connection, this.server,
				this.command);
		fail ("Server cannot be null");
	}
}
