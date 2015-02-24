package com.novicehacks.autobot.ssh;

import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;
import com.novicehacks.autobot.types.ShellCommand;
import com.novicehacks.autobot.types.UnixServer;

@PowerMockIgnore ("*")
@PrepareForTest ({ ParallelCommandExecutorTask.class, ParallelCommandExecutorTask.class,
		SSHConnection.class, SSHSession.class, Server.class, UnixServer.class, ShellCommand.class,
		Command.class, OutputLoggerTask.class })
public class ParallelCommandExecutorTaskTest {
	private ParallelCommandExecutorTask commandExecutor;
	private Server server;
	private Command command;
	private SSHConnection connection;
	private SSHSession session;
	private OutputLoggerTask outputLoggerTask;

	@Rule
	public PowerMockRule rule = new PowerMockRule ();

	@Before
	public void setUp() throws IOException {
		this.connection = mock (SSHConnection.class);
		this.session = mock (SSHSession.class);
		this.server = mock (UnixServer.class);
		this.command = mock (ShellCommand.class);
		this.outputLoggerTask = mock (OutputLoggerTask.class);

		when (this.connection.openSession ()).thenReturn (this.session);
		when (this.session.stdInputStream ()).thenReturn (SSHTestUtilities.tempOutputStream ());
		when (this.session.stdOutputStream ()).thenReturn (SSHTestUtilities.tempInputStream ());
		doNothing ().when (this.outputLoggerTask).run ();
	}

	@Test (expected = IllegalArgumentException.class)
	public void nullCommandToTask() {
		// given
		this.command = null;
		// when
		this.commandExecutor = new ParallelCommandExecutorTask (this.connection, this.server,
				this.command);
		// then
		fail ("Invalid Parameters");
	}

	@Test (expected = IllegalArgumentException.class)
	public void nullConnectionToTask() {
		// given
		this.connection = null;
		// when
		this.commandExecutor = new ParallelCommandExecutorTask (this.connection, this.server,
				this.command);
		// then
		fail ("Invalid Parameters");
	}

	@Test
	public void nullServerToTask() throws IOException {
		// given
		when (this.connection.openSession ()).thenThrow (new IOException ());
		// when
		this.commandExecutor = new ParallelCommandExecutorTask (this.connection, this.server,
				this.command);
		// then
		fail ("Invalid Parameters");
	}
}
