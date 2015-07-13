package com.novicehacks.autobot.executor.ssh;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.EnvironmentalTest;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.executor.ssh.DefaultSSHConnection;
import com.novicehacks.autobot.executor.ssh.SSHConnection;
import com.novicehacks.autobot.executor.ssh.SSHSession;

public class DefaultSSHSessionTestWithoutMocking {

	private SSHConnection connection;
	private SSHSession session;
	private final String connectionString = "sdf.org";
	private final String username = "novicehacks";
	private final String password = "novicehacks";

	@Before
	public void setUp() throws IOException {
		this.connection = DefaultSSHConnection.getNewInstance (this.connectionString);
		this.connection.connect ();
		this.connection
				.authenticateConnectionWithUsernameAndPassword (this.username, this.password);
		this.session = this.connection.openSession ();
	}

	@After
	public void tearDown() {
		this.session.closeSession ();
		this.connection.disconnect ();
	}

	@Test
	@Category ({ UnitTest.class, EnvironmentalTest.class })
	public void testExecuteCommand() throws IOException {
		// given
		String command = "df -k";
		// when
		this.session.execCommand (command);
		// then
		assertTrue ("Command Executed", true);
	}

	@Test
	@Category ({ UnitTest.class, EnvironmentalTest.class })
	public void testStartShell() throws IOException {
		// given
		String command = "df -k";
		// when
		this.session.startShell ();
		this.session.stdInputStream ().write (command.getBytes ());
		// then
		assertTrue ("Command Executed on Shell", true);
	}

}
