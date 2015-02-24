package com.novicehacks.autobot.ssh;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

import com.novicehacks.autobot.categories.EnvironmentDependent;
import com.novicehacks.autobot.ssh.CustomizedSSHConnection;
import com.novicehacks.autobot.ssh.SSHConnection;
import com.novicehacks.autobot.ssh.SSHSession;

@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class TestCustomizedSSHConnectionWithoutMocking {
	private SSHConnection	sshConnection;
	private final String	connectionStr	= "sdf.org";
	private final String	password		= "novicehacks";
	private final String	username		= "novicehacks";

	@Before
	public void initializeConnectionMock() throws Exception {
		this.sshConnection = CustomizedSSHConnection.getNewInstance (this.connectionStr);
	}

	@After
	public void closeConnection() {
		if (this.sshConnection != null && this.sshConnection.isConnectionAvailable ())
			this.sshConnection.disconnect ();
	}

	@Test
	@Category (EnvironmentDependent.class)
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

	@Test (expected = IOException.class)
	@Category (EnvironmentDependent.class)
	public void connectWhenRemoteExceptionTest() throws Exception {
		// given
		String ipAddress = "192.168.0.1";
		this.sshConnection = CustomizedSSHConnection.getNewInstance (ipAddress);
		// when
		this.sshConnection.connect (10, 10);
		// then
		this.sshConnection.connect ();
	}

	@Test
	@Category (EnvironmentDependent.class)
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
	@Category (EnvironmentDependent.class)
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
	@Category (EnvironmentDependent.class)
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

	@Test (expected = IllegalStateException.class)
	@Category (EnvironmentDependent.class)
	public void openSessionBeforeAuthenticateTest() throws Exception {
		SSHSession session;
		// when not authenticated
		this.sshConnection.connect ();
		assertTrue ("Connection is not available", this.sshConnection.isConnectionAvailable ());
		assertFalse ("Connection should not be authenticated",
				this.sshConnection.isAuthenticated ());
		// then
		session = this.sshConnection.openSession ();
		assertNull (session);
	}

	@Test
	@Category (EnvironmentDependent.class)
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
