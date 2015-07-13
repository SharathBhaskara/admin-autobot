package com.novicehacks.autobot.executor.ssh;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import com.novicehacks.autobot.categories.EnvironmentalTest;
import com.novicehacks.autobot.executor.ssh.DefaultSSHConnection;
import com.novicehacks.autobot.executor.ssh.SSHConnection;
import com.novicehacks.autobot.executor.ssh.SSHSession;

@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class DefaultSSHConnectionTestWithoutMocking {
	private SSHConnection sshConnection;
	private final String connectionStr = "sdf.org";
	private final String password = "novicehacks";
	private final String username = "novicehacks";

	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@Before
	public void initializeConnectionMock() throws Exception {
		this.sshConnection = DefaultSSHConnection.getNewInstance (this.connectionStr);
	}

	@After
	public void closeConnection() {
		if (this.sshConnection != null && this.sshConnection.isConnectionAvailable ())
			this.sshConnection.disconnect ();
	}

	@Test
	@Category (EnvironmentalTest.class)
	public void connectTest() throws Exception {
		// given
		// when
		assertFalse ("Connection should not exist", this.sshConnection.isConnectionAvailable ());
		this.sshConnection.connect ();
		// then
		assertTrue ("Connection made", this.sshConnection.isConnectionAvailable ());
		// finally
		this.sshConnection.disconnect ();
	}

	@Test
	@Category (EnvironmentalTest.class)
	public void connectWhenRemoteExceptionTest() throws Exception {
		// given
		String incorrectIPAddress = "192.168.0.1";
		// when
		this.sshConnection = DefaultSSHConnection.getNewInstance (incorrectIPAddress);
		// then
		this.exception.expect (IOException.class);
		this.sshConnection.connect (10, 10);
	}

	@Test
	@Category (EnvironmentalTest.class)
	public void authenticationTest() throws Exception {
		// given
		boolean authStatus;
		// when
		this.sshConnection.connect ();
		authStatus = this.sshConnection.authenticateConnectionWithUsernameAndPassword (
				this.username, this.password);
		// then
		assertTrue ("Authentication Failed", authStatus);
	}

	@Test
	@Category (EnvironmentalTest.class)
	public void authenticationWithInvalidCredentialsTest() throws Exception {
		// given
		initializeConnectionMock ();
		boolean authStatus;
		String invalidPassword = "invalid";
		String invalidUsername = "invalid";
		// when
		this.sshConnection.connect ();
		// then
		authStatus = this.sshConnection.authenticateConnectionWithUsernameAndPassword (
				invalidUsername, invalidPassword);
		assertFalse ("Authentication Should Fail", authStatus);
	}

	@Test
	@Category (EnvironmentalTest.class)
	public void openSessionTest() throws Exception {
		SSHSession session;
		// given
		initializeConnectionMock ();
		// when
		this.sshConnection.connect ();
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
		session = this.sshConnection.openSession ();
		// then
		assertNotNull (session);
	}

	@Test
	@Category (EnvironmentalTest.class)
	public void openSessionBeforeAuthenticateTest() throws Exception {
		SSHSession session;
		// when not authenticated
		this.sshConnection.connect ();
		assertTrue ("Connection is not available", this.sshConnection.isConnectionAvailable ());
		assertFalse ("Connection should not be authenticated",
				this.sshConnection.isAuthenticated ());
		// then
		this.exception.expect (IllegalStateException.class);
		session = this.sshConnection.openSession ();
		assertNull (session);
	}

	@Test
	@Category (EnvironmentalTest.class)
	public void disconnectionTest() throws Exception {
		// given
		initializeConnectionMock ();
		// when
		this.sshConnection.connect ();
		this.sshConnection.disconnect ();
		// then
		boolean status = this.sshConnection.isConnectionAvailable ();
		assertFalse (status);
	}
}
