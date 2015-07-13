package com.novicehacks.autobot.executor.ssh;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.Session;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.executor.ssh.DefaultSSHConnection;
import com.novicehacks.autobot.executor.ssh.SSHConnection;
import com.novicehacks.autobot.executor.ssh.SSHSession;

public class DefaultSSHConnectionTest {

	private Session session;
	private Connection connection;
	private ConnectionInfo connectionInfo;
	private SSHConnection sshConnection;
	private DefaultSSHConnection.ConnectionFactory connectionFactory;

	private final String username = "abc";
	private final String password = "def";

	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@Before
	public void setup() throws IOException {
		this.connection = mock (Connection.class);
		this.session = mock (Session.class);
		this.connectionInfo = mock (ConnectionInfo.class);
		this.connectionFactory = mock (DefaultSSHConnection.ConnectionFactory.class);
		this.sshConnection = new DefaultSSHConnection ("127.0.0.1", this.connectionFactory);
		when (this.connection.connect ()).thenReturn (this.connectionInfo);
		when (this.connection.connect (null, 0, 0)).thenReturn (this.connectionInfo);
		when (this.connection.openSession ()).thenReturn (this.session);
		when (this.connection.authenticateWithPassword (this.username, this.password)).thenReturn (
				true);
		when (this.connectionFactory.createConnection (Mockito.anyString ())).thenReturn (
				this.connection);
	}

	@Test
	@Category (UnitTest.class)
	public void invalidIPAddressWithDomainNameFormat() throws Exception {
		// when
		String ipAddress = "sf";
		// then Connection requested throws exception
		this.exception.expect (IllegalArgumentException.class);
		this.sshConnection = DefaultSSHConnection.getNewInstance (ipAddress);
	}

	@Test
	@Category (UnitTest.class)
	public void invalidIPAddressWitIPV6Format() throws Exception {
		// when
		String ipAddress = "1924.123.1.1";
		// then Connection requested throws exception
		this.exception.expect (IllegalArgumentException.class);
		this.sshConnection = DefaultSSHConnection.getNewInstance (ipAddress);
	}

	@Test
	@Category (UnitTest.class)
	public void invalidIPAddressWitIPV6Format2() throws Exception {
		// when
		String ipAddress = "192.123.1.1234";
		// then Connection requested throws exception
		this.exception.expect (IllegalArgumentException.class);
		this.sshConnection = DefaultSSHConnection.getNewInstance (ipAddress);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void connectTest() throws Exception {
		// when
		this.sshConnection.connect ();
		// then
		verify (this.connection).connect (null, 0, 0);
	}

	@Test
	@Category (UnitTest.class)
	public void connectWithTimeoutTest() throws Exception {
		// when
		this.sshConnection.connect (500, 500);
		// then
		verify (this.connection).connect (null, 500, 500);
	}

	@Test
	@Category (UnitTest.class)
	public void connectWhenRemoteExceptionTest() throws Exception {
		// given
		when (this.connection.connect (null, 0, 0)).thenThrow (new IOException ());
		// then
		this.exception.expect (IOException.class);
		this.sshConnection.connect ();
		verify (this.connection).connect (null, 0, 0);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void authenticateTest() throws Exception {
		// given
		this.sshConnection.connect ();
		verify (this.connection).connect (null, 0, 0);
		// when
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
		// then
		verify (this.connection).authenticateWithPassword (this.username, this.password);
	}

	@Test
	@Category (UnitTest.class)
	public void authenticateThrowsRemoteExceptionTest() throws Exception {
		// given
		this.sshConnection.connect ();
		when (this.connection.authenticateWithPassword (this.username, this.password)).thenThrow (
				new IOException ());
		// when connection available
		assertTrue ("Connection Should Not Exist", this.sshConnection.isConnectionAvailable ());
		// then
		this.exception.expect (IOException.class);
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
		verify (this.connection).authenticateWithPassword (this.username, this.password);
	}

	@Test
	@Category (UnitTest.class)
	public void authenticateBeforeConnectTest() throws Exception {
		// given
		verify (this.connection, never ()).connect (null, 0, 0);
		// when not connected,
		assertFalse ("Connection Should Not Exist", this.sshConnection.isConnectionAvailable ());
		// then authentication throws exception
		this.exception.expect (IllegalStateException.class);
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
	}

	@Test
	@Category (UnitTest.class)
	public void authenticateAfterDisconnectTest() throws Exception {
		// given
		this.sshConnection.connect ();
		this.sshConnection.disconnect ();
		// when
		verify (this.connection).connect (null, 0, 0);
		verify (this.connection).close ();
		assertFalse ("Connection Should Not Exist", this.sshConnection.isConnectionAvailable ());
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
		verify (this.connection, never ()).authenticateWithPassword (this.username, this.password);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void openSessionTest() throws Exception {
		SSHSession session;
		// given
		this.sshConnection.connect ();
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
		// when
		assertTrue ("Connection should have been available",
				this.sshConnection.isConnectionAvailable ());
		assertTrue ("Connection should have been available", this.sshConnection.isAuthenticated ());
		session = this.sshConnection.openSession ();
		// then
		verify (this.connection).openSession ();
		assertNotNull (session);
	}

	@Test
	@Category (UnitTest.class)
	public void openSessionBeforeConnectTest() throws Exception {
		SSHSession session;
		// when not connected
		verify (this.connection, never ()).connect (null, 0, 0);
		assertFalse ("Connection should have been available",
				this.sshConnection.isConnectionAvailable ());
		// then
		this.exception.expect (IllegalStateException.class);
		session = this.sshConnection.openSession ();
		verify (this.connection, never ()).openSession ();
		assertNull (session);
	}

	@Test
	@Category (UnitTest.class)
	public void openSessionBeforeAuthenticateTest() throws Exception {
		SSHSession session;
		// given
		// when not connected
		verify (this.connection, never ()).authenticateWithPassword (this.username, this.password);
		// then
		this.exception.expect (IllegalStateException.class);
		session = this.sshConnection.openSession ();
		verify (this.connection).openSession ();
		assertNotNull (session);
	}

	@Test
	@Category (UnitTest.class)
	public void openSessionWhenRemoteExceptionTest() throws Exception {

		// given
		SSHSession session;
		when (this.connection.openSession ()).thenThrow (new IOException ());
		// when not connected
		this.sshConnection.connect ();
		this.sshConnection.authenticateConnectionWithUsernameAndPassword (this.username,
				this.password);
		verify (this.connection).connect (null, 0, 0);
		verify (this.connection).authenticateWithPassword (this.username, this.password);
		// then
		this.exception.expect (IOException.class);
		session = this.sshConnection.openSession ();
		verify (this.connection).openSession ();
		assertNotNull (session);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void disconnectTest() throws Exception {
		// given
		this.sshConnection.connect ();
		assertTrue ("Connection Should Not Exist", this.sshConnection.isConnectionAvailable ());
		// when
		this.sshConnection.disconnect ();
		// then
		verify (this.connection).connect (null, 0, 0);
		verify (this.connection).close ();
	}

	@Test
	@Category (UnitTest.class)
	public void disconnectBeforeConnect() throws Exception {
		// when not connected,
		verify (this.connection, never ()).connect (null, 0, 0);
		assertFalse ("Connection Should Not Exist", this.sshConnection.isConnectionAvailable ());
		// then disconnect throws exception
		this.exception.expect (IllegalStateException.class);
		this.sshConnection.disconnect ();
		verify (this.connection, never ()).close ();
	}

}
