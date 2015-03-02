package com.novicehacks.autobot.ssh.logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.novicehacks.autobot.logger.OutputHeaderService;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;
import com.novicehacks.autobot.types.ShellCommand;
import com.novicehacks.autobot.types.UnixServer;

public class ShellOutputHeaderServiceTest {
	@Mock
	Server server;
	@Mock
	Command command;
	ShellOutputHeaderService headerService;

	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks (this);
		this.headerService = new ShellOutputHeaderService (this.server, this.command);
	}

	@Test
	public void testEquals() {
		// given
		ShellOutputHeaderService temp = new ShellOutputHeaderService (this.server, this.command);
		// when
		boolean status = this.headerService.equals (temp);
		// then
		assertTrue ("Should be equals when their params are equals", status);
	}

	@Test
	public void testEqualsWithDifferentServer() {
		// given
		Server otherServer = new UnixServer ("");
		ShellOutputHeaderService temp = new ShellOutputHeaderService (otherServer, this.command);
		// when
		boolean status = this.headerService.equals (temp);
		// then
		assertFalse ("Should not be equal because of two different servers", status);
	}

	@Test
	public void testEqualsWithDifferentCommand() {
		// given
		Command otherCommand = new ShellCommand ("");
		ShellOutputHeaderService temp = new ShellOutputHeaderService (this.server, otherCommand);
		// when
		boolean status = this.headerService.equals (temp);
		// then
		assertFalse ("Should not be equal because of two different commands", status);
	}

	@Test
	public void testEqualsWithDifferentHeaderServiceImpl() {
		// given
		OutputHeaderService otherService = mock (OutputHeaderService.class);
		// when
		boolean status = this.headerService.equals (otherService);
		// then
		assertFalse ("Should not be equal because of two different commands", status);
	}
}
