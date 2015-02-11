package test.com.novicehacks.autobot.ssh;

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

import test.com.novicehacks.autobot.categories.EnvironmentDependent;

import com.novicehacks.autobot.ssh.CustomizedSSHConnection;
import com.novicehacks.autobot.ssh.CustomizedSSHSession;

@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class TestCustomizedSSHConnectionWithoutMocking {
	private CustomizedSSHConnection	sshConnection;
	private final String			connectionStr	= "sdf.org";
	private final String			password		= "novicehacks";
	private final String			username		= "novicehacks";

	@Before
	public void initializeConnectionMock() throws Exception {
		sshConnection = CustomizedSSHConnection.getNewInstance (connectionStr);
	}

	@After
	public void closeConnection() {
		if (sshConnection != null && sshConnection.isConnectionAvailable ())
			sshConnection.disconnect ();
	}

	@Test
	@Category (EnvironmentDependent.class)
	public void connectTest() throws Exception {
		// given
		// when
		assertFalse ("Connection should not exist", sshConnection.isConnectionAvailable ());
		sshConnection.connect ();
		// then
		assertTrue ("Connection made", sshConnection.isConnectionAvailable ());
		// finally
		sshConnection.disconnect ();
	}

	@Test (expected = IOException.class)
	@Category (EnvironmentDependent.class)
	public void connectWhenRemoteExceptionTest() throws Exception {
		// given
		String ipAddress = "192.168.0.1";
		sshConnection = CustomizedSSHConnection.getNewInstance (ipAddress);
		// when
		sshConnection.connect (10, 10);
		// then
		sshConnection.connect ();
	}

	@Test
	@Category (EnvironmentDependent.class)
	public void authenticationTest() throws Exception {
		// given
		boolean authStatus;
		// when
		sshConnection.connect ();
		authStatus = sshConnection.authenticateConnectionWithUsernameAndPassword (username,
				password);
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
		sshConnection.connect ();
		// then
		authStatus = sshConnection.authenticateConnectionWithUsernameAndPassword (invalidUsername,
				invalidPassword);
		assertFalse ("Authentication Should Fail", authStatus);
	}

	@Test
	@Category (EnvironmentDependent.class)
	public void openSessionTest() throws Exception {
		CustomizedSSHSession session;
		// given
		initializeConnectionMock ();
		// when
		sshConnection.connect ();
		sshConnection.authenticateConnectionWithUsernameAndPassword (username, password);
		session = sshConnection.openSession ();
		// then
		assertNotNull (session);
	}

	@Test (expected = IllegalStateException.class)
	@Category (EnvironmentDependent.class)
	public void openSessionBeforeAuthenticateTest() throws Exception {
		CustomizedSSHSession session;
		// when not authenticated
		sshConnection.connect ();
		assertTrue ("Connection is not available", sshConnection.isConnectionAvailable ());
		assertFalse ("Connection should not be authenticated", sshConnection.isAuthenticated ());
		// then
		session = sshConnection.openSession ();
		assertNull (session);
	}

	@Test
	@Category (EnvironmentDependent.class)
	public void disconnectionTest() throws Exception {
		// given
		initializeConnectionMock ();
		// when
		sshConnection.connect ();
		sshConnection.disconnect ();
		// then
		boolean status = sshConnection.isConnectionAvailable ();
		assertFalse (status);
	}

	/*
	 * public void initializeBasicConnectionMock() throws Exception {
	 * sshConnection = CustomizedSSHConnection.getNewInstance ("127.0.0.1");
	 * connection = mock (Connection.class);
	 * session = mock (Session.class);
	 * connectionInfo = mock (ConnectionInfo.class);
	 * when (connection.connect ()).thenReturn (connectionInfo);
	 * when (connection.connect (null, 0, 0)).thenReturn (connectionInfo);
	 * when (connection.openSession ()).thenReturn (session);
	 * when (connection.authenticateWithPassword (username,
	 * password)).thenReturn (true);
	 * whenNew (Connection.class).withAnyArguments ().thenReturn (connection);
	 * }
	 */
}
