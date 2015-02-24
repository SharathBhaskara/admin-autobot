package com.novicehacks.autobot.ssh;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.io.IOException;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.Session;

import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.ssh.DefaultSSHConnection;
import com.novicehacks.autobot.ssh.DefaultSSHSession;
import com.novicehacks.autobot.ssh.SSHConnection;
import com.novicehacks.autobot.ssh.SSHSession;

@PrepareForTest ({ DefaultSSHConnection.class, DefaultSSHSession.class })
@PowerMockIgnore ({ "*" })
@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class DefaultSSHConnectionTest {

	private Session session;
	private Connection connection;
	private ConnectionInfo connectionInfo;
	private SSHConnection sshConnection;

	private final String username = "abc";
	private final String password = "def";

	@Rule
	public PowerMockRule powermockRule = new PowerMockRule ();

	@Before
	public void setup() {}

	@Test (expected = IllegalArgumentException.class)
	@Category (UnitTest.class)
	public void invalidIPAddressWithDomainNameFormat() throws Exception {
		// given
		String ipAddress = "sf";
		// when Connection requested throws exception
		this.sshConnection = DefaultSSHConnection.getNewInstance (ipAddress);
	}

	@Test (expected = IllegalArgumentException.class)
	@Category (UnitTest.class)
	public void invalidIPAddressWitIPV6Format() throws Exception {
		// given
		String ipAddress = "1924.123.1.1";
		// when Connection requested throws exception
		this.sshConnection = DefaultSSHConnection.getNewInstance (ipAddress);
	}

	@Test (expected = IllegalArgumentException.class)
	@Category (UnitTest.class)
	public void invalidIPAddressWitIPV6Format2() throws Exception {
		// given
		String ipAddress = "192.123.1.1234";
		// when Connection requested throws exception
		this.sshConnection = DefaultSSHConnection.getNewInstance (ipAddress);
	}

	@Test
	@Category (UnitTest.class)
	public void connectTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		this.sshConnection.connect ();
		// then
		verify (this.connection).connect (null, 0, 0);
	}

	private void initializeBasicConnectionMock() throws Exception {
		this.sshConnection = DefaultSSHConnection.getNewInstance ("127.0.0.1");
		this.connection = mock (Connection.class);
		this.session = mock (Session.class);
		this.connectionInfo = mock (ConnectionInfo.class);
		when (this.connection.connect ()).thenReturn (this.connectionInfo);
		when (this.connection.connect (null, 0, 0)).thenReturn (this.connectionInfo);
		when (this.connection.openSession ()).thenReturn (this.session);
		when (this.connection.authenticateWithPassword (this.username, this.password)).thenReturn (
				true);
		whenNew (Connection.class).withAnyArguments ().thenReturn (this.connection);
	}

	@Test
	@Category (UnitTest.class)
	public void connectWithTimeoutTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		this.sshConnection.connect (500, 500);
		// then
		verify (this.connection).connect (null, 500, 500);
	}

	@Test (expected = IOException.class)
	@Category (UnitTest.class)
	public void connectWhenRemoteExceptionTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		when (this.connection.connect (null, 0, 0)).thenThrow (new IOException ());
		// then
		this.sshConnection.connect ();
		verify (this.connection).connect (null, 0, 0);
	}

	@Test
	@Category (UnitTest.class)
	public void authenticateTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		this.sshConnection.connect ();
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
		// then
		verify (this.connection).connect (null, 0, 0);
		verify (this.connection).authenticateWithPassword (this.username, this.password);
	}

	@Test (expected = IOException.class)
	@Category (UnitTest.class)
	public void authenticateWhenRemoteExceptionTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		this.sshConnection.connect ();
		when (this.connection.authenticateWithPassword (this.username, this.password)).thenThrow (
				new IOException ());
		// then
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
		verify (this.connection).authenticateWithPassword (this.username, this.password);
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void authenticateBeforeConnectTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when not connected,
		verify (this.connection, never ()).connect (null, 0, 0);
		assertFalse ("Connection Should Not Exist", this.sshConnection.isConnectionAvailable ());
		// then authentication throws exception
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void authenticateAfterDisconnectTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		this.sshConnection.connect ();
		this.sshConnection.disconnect ();
		verify (this.connection).connect (null, 0, 0);
		verify (this.connection).close ();
		// then
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
		verify (this.connection, never ()).authenticateWithPassword (this.username, this.password);
	}

	@Test
	@Category (UnitTest.class)
	public void openSessionTest() throws Exception {
		SSHSession session;
		// given
		initializeBasicConnectionMock ();
		// when
		this.sshConnection.connect ();
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
		session = this.sshConnection.openSession ();
		// then
		verify (this.connection).openSession ();
		assertNotNull (session);
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void openSessionBeforeConnectTest() throws Exception {
		SSHSession session;
		// given
		initializeBasicConnectionMock ();
		// when not connected
		verify (this.connection, never ()).connect (null, 0, 0);
		// then
		session = this.sshConnection.openSession ();
		verify (this.connection).openSession ();
		assertNotNull (session);
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void openSessionBeforeAuthenticateTest() throws Exception {
		SSHSession session;
		// given
		initializeBasicConnectionMock ();
		// when not connected
		verify (this.connection, never ()).authenticateWithPassword (this.username, this.password);
		// then
		session = this.sshConnection.openSession ();
		verify (this.connection).openSession ();
		assertNotNull (session);
	}

	@Test (expected = IOException.class)
	@Category (UnitTest.class)
	public void openSessionWhenRemoteExceptionTest() throws Exception {
		SSHSession session;
		// given
		initializeBasicConnectionMock ();
		when (this.connection.openSession ()).thenThrow (new IOException ());

		// when not connected
		this.sshConnection.connect ();
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
		verify (this.connection).connect (null, 0, 0);
		verify (this.connection).authenticateWithPassword (this.username, this.password);
		// then
		session = this.sshConnection.openSession ();
		verify (this.connection).openSession ();
		assertNotNull (session);
	}

	@Test
	@Category (UnitTest.class)
	public void disconnectTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		this.sshConnection.connect ();
		this.sshConnection.disconnect ();
		// then
		verify (this.connection).connect (null, 0, 0);
		verify (this.connection).close ();
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void disconnectBeforeConnect() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when not connected,
		verify (this.connection, never ()).connect (null, 0, 0);
		assertFalse ("Connection Should Not Exist", this.sshConnection.isConnectionAvailable ());
		// then disconnect throws exception
		this.sshConnection.disconnect ();
		verify (this.connection, never ()).close ();
	}

}
