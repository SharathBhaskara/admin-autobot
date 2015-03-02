package com.novicehacks.autobot.ssh;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

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

	@Test (expected = CommandExecutionException.class)
	public void nullCommandToTask() {
		// given
		this.command = null;
		// when
		this.commandExecutor = new ParallelCommandExecutorTask (this.connection, this.server,
				this.command);
		// then
		fail ("Invalid Parameters");
	}

	@Test (expected = CommandExecutionException.class)
	public void nullConnectionToTask() {
		// given
		this.connection = null;
		// when
		this.commandExecutor = new ParallelCommandExecutorTask (this.connection, this.server,
				this.command);
		// then
		fail ("Invalid Parameters");
	}

	@Test (expected = CommandExecutionException.class)
	public void nullServerToTask() throws IOException {
		// given
		this.server = null;
		// when
		this.commandExecutor = new ParallelCommandExecutorTask (this.connection, this.server,
				this.command);
		// then
		fail ("Invalid Parameters");
	}
}
