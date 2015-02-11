package test.com.novicehacks.autobot.ssh;

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

import test.com.novicehacks.autobot.categories.UnitTest;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.Session;

import com.novicehacks.autobot.ssh.CustomizedSSHConnection;
import com.novicehacks.autobot.ssh.CustomizedSSHSession;

@PrepareForTest ({ CustomizedSSHConnection.class, CustomizedSSHSession.class })
@PowerMockIgnore ({ "*" })
@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class TestCustomizedSSHConnection {

	private Session					session;
	private Connection				connection;
	private ConnectionInfo			connectionInfo;
	private CustomizedSSHConnection	sshConnection;

	private final String			username		= "abc";
	private final String			password		= "def";

	@Rule
	public PowerMockRule			powermockRule	= new PowerMockRule ();

	@Before
	public void setup() {}

	@Test (expected = IllegalArgumentException.class)
	@Category (UnitTest.class)
	public void invalidIPAddressWithDomainNameFormat() throws Exception {
		// given
		String ipAddress = "sf";
		// when Connection requested throws exception
		sshConnection = CustomizedSSHConnection.getNewInstance (ipAddress);
	}

	@Test (expected = IllegalArgumentException.class)
	@Category (UnitTest.class)
	public void invalidIPAddressWitIPV6Format() throws Exception {
		// given
		String ipAddress = "1924.123.1.1";
		// when Connection requested throws exception
		sshConnection = CustomizedSSHConnection.getNewInstance (ipAddress);
	}

	@Test (expected = IllegalArgumentException.class)
	@Category (UnitTest.class)
	public void invalidIPAddressWitIPV6Format2() throws Exception {
		// given
		String ipAddress = "192.123.1.1234";
		// when Connection requested throws exception
		sshConnection = CustomizedSSHConnection.getNewInstance (ipAddress);
	}

	@Test
	@Category (UnitTest.class)
	public void connectTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		sshConnection.connect ();
		// then
		verify (connection).connect (null, 0, 0);
	}

	@Test
	@Category (UnitTest.class)
	public void connectWithTimeoutTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		sshConnection.connect (500, 500);
		// then
		verify (connection).connect (null, 500, 500);
	}

	@Test (expected = IOException.class)
	@Category (UnitTest.class)
	public void connectWhenRemoteExceptionTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		when (connection.connect (null, 0, 0)).thenThrow (new IOException ());
		// then
		sshConnection.connect ();
		verify (connection).connect (null, 0, 0);
	}

	@Test
	@Category (UnitTest.class)
	public void authenticateTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		sshConnection.connect ();
		sshConnection.authenticateConnectionWithUsernameAndPassword (username, password);
		// then
		verify (connection).connect (null, 0, 0);
		verify (connection).authenticateWithPassword (username, password);
	}

	@Test (expected = IOException.class)
	@Category (UnitTest.class)
	public void authenticateWhenRemoteExceptionTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		sshConnection.connect ();
		when (connection.authenticateWithPassword (username, password)).thenThrow (
				new IOException ());
		// then
		sshConnection.authenticateConnectionWithUsernameAndPassword (username, password);
		verify (connection).authenticateWithPassword (username, password);
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void authenticateBeforeConnectTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when not connected,
		verify (connection, never ()).connect (null, 0, 0);
		assertFalse ("Connection Should Not Exist", sshConnection.isConnectionAvailable ());
		// then authentication throws exception
		sshConnection.authenticateConnectionWithUsernameAndPassword (username, password);
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void authenticateAfterDisconnectTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		sshConnection.connect ();
		sshConnection.disconnect ();
		verify (connection).connect (null, 0, 0);
		verify (connection).close ();
		// then
		sshConnection.authenticateConnectionWithUsernameAndPassword (username, password);
		verify (connection, never ()).authenticateWithPassword (username, password);
	}

	@Test
	@Category (UnitTest.class)
	public void openSessionTest() throws Exception {
		CustomizedSSHSession session;
		// given
		initializeBasicConnectionMock ();
		// when
		sshConnection.connect ();
		sshConnection.authenticateConnectionWithUsernameAndPassword (username, password);
		session = sshConnection.openSession ();
		// then
		verify (connection).openSession ();
		assertNotNull (session);
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void openSessionBeforeConnectTest() throws Exception {
		CustomizedSSHSession session;
		// given
		initializeBasicConnectionMock ();
		// when not connected
		verify (connection, never ()).connect (null, 0, 0);
		// then
		session = sshConnection.openSession ();
		verify (connection).openSession ();
		assertNotNull (session);
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void openSessionBeforeAuthenticateTest() throws Exception {
		CustomizedSSHSession session;
		// given
		initializeBasicConnectionMock ();
		// when not connected
		verify (connection, never ()).authenticateWithPassword (username, password);
		// then
		session = sshConnection.openSession ();
		verify (connection).openSession ();
		assertNotNull (session);
	}

	@Test (expected = IOException.class)
	@Category (UnitTest.class)
	public void openSessionWhenRemoteExceptionTest() throws Exception {
		CustomizedSSHSession session;
		// given
		initializeBasicConnectionMock ();
		when (connection.openSession ()).thenThrow (new IOException ());

		// when not connected
		sshConnection.connect ();
		sshConnection.authenticateConnectionWithUsernameAndPassword (username, password);
		verify (connection).connect (null, 0, 0);
		verify (connection).authenticateWithPassword (username, password);
		// then
		session = sshConnection.openSession ();
		verify (connection).openSession ();
		assertNotNull (session);
	}

	@Test
	@Category (UnitTest.class)
	public void disconnectTest() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when
		sshConnection.connect ();
		sshConnection.disconnect ();
		// then
		verify (connection).connect (null, 0, 0);
		verify (connection).close ();
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void disconnectBeforeConnect() throws Exception {
		// given
		initializeBasicConnectionMock ();
		// when not connected,
		verify (connection, never ()).connect (null, 0, 0);
		assertFalse ("Connection Should Not Exist", sshConnection.isConnectionAvailable ());
		// then disconnect throws exception
		sshConnection.disconnect ();
		verify (connection, never ()).close ();
	}

	public void initializeBasicConnectionMock() throws Exception {
		sshConnection = CustomizedSSHConnection.getNewInstance ("127.0.0.1");
		connection = mock (Connection.class);
		session = mock (Session.class);
		connectionInfo = mock (ConnectionInfo.class);
		when (connection.connect ()).thenReturn (connectionInfo);
		when (connection.connect (null, 0, 0)).thenReturn (connectionInfo);
		when (connection.openSession ()).thenReturn (session);
		when (connection.authenticateWithPassword (username, password)).thenReturn (true);
		whenNew (Connection.class).withAnyArguments ().thenReturn (connection);
	}

}
