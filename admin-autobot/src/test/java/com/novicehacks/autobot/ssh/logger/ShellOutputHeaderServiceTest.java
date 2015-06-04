package com.novicehacks.autobot.ssh.logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.SSHServer;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.core.types.ShellCommand;
import com.novicehacks.autobot.services.OutputHeaderService;

public class ShellOutputHeaderServiceTest {
	@Mock
	private Server server;
	@Mock
	private Command command;
	private ShellOutputHeaderService headerService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks (this);
		this.headerService = new ShellOutputHeaderService (this.server, this.command);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testEquals() {
		// given
		ShellOutputHeaderService temp = new ShellOutputHeaderService (this.server, this.command);
		// when
		boolean status = this.headerService.equals (temp);
		// then
		assertTrue ("Should be equals when their params are equals", status);
	}

	@Test
	@Category ({ UnitTest.class })
	public void testEqualsWithDifferentServer() {
		// given
		Server otherServer = new SSHServer ("");
		ShellOutputHeaderService temp = new ShellOutputHeaderService (otherServer, this.command);
		// when
		boolean status = this.headerService.equals (temp);
		// then
		assertFalse ("Should not be equal because of two different servers", status);
	}

	@Test
	@Category ({ UnitTest.class })
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
	@Category ({ UnitTest.class })
	public void testEqualsWithDifferentHeaderServiceImpl() {
		// given
		OutputHeaderService otherService = mock (OutputHeaderService.class);
		// when
		boolean status = this.headerService.equals (otherService);
		// then
		assertFalse ("Should not be equal because of two different commands", status);
	}
}
