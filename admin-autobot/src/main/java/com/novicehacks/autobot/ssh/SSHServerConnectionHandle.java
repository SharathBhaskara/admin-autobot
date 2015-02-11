package com.novicehacks.autobot.ssh;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.config.SysConfig;
import com.novicehacks.autobot.ssh.exception.ServerConnectionException;
import com.novicehacks.autobot.types.Server;
import com.novicehacks.autobot.types.ServerCredential;

/**
 * This is used to create and revive connections to the SSH servers
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public final class SSHServerConnectionHandle {

	Server					server;
	CustomizedSSHConnection	connection;
	Logger					logger	= LogManager.getLogger (SSHServerConnectionHandle.class);

	protected SSHServerConnectionHandle (Server unixServer) {
		this.server = unixServer;
	}

	/**
	 * 
	 * @return {@link ch.ethz.ssh2.Connection Connection} Authenticated
	 *         connection, ready to
	 *         to execute commands or start a shell.
	 * @throws ServerConnectionException
	 *         if connection / authentication is failed on the server
	 */
	public CustomizedSSHConnection getAuthenticatedConnection() {
		connectToServerAndAuthenticate ();
		return this.connection;
	}

	private void connectToServerAndAuthenticate() {
		logger.entry ();
		try {
			initiateConnectionToServer ();
			authenticateServerConnection ();
		} catch (IOException ex) {
			throw new ServerConnectionException ("Unable to connect / authenticate server: "
					+ server.id (), ex);
		}
		logger.exit ();
	}

	private void initiateConnectionToServer() throws IOException {
		int keyExchangeTimeout = 10 * 30 * 1000;
		int connectionTimeout = SysConfig.getInstance ().serverConnectionTimeout ();
		// HostKeyVerifier set to null to accept any server host key
		logger.debug ("Connecting to : {}", this.server.ipaddress ());
		connection = new CustomizedSSHConnection (this.server.ipaddress ());
		connection.connect (keyExchangeTimeout, connectionTimeout);
	}

	private void authenticateServerConnection() {
		Boolean authenticated = false;
		authenticated = authenticateWithServerCredentials ();
		if (!authenticated) {
			throw new ServerConnectionException ("Authentication failed for Server : "
					+ server.id (), new RuntimeException ());
		}
	}

	private Boolean authenticateWithServerCredentials() {
		boolean authenticated = false;
		for (ServerCredential credential : this.server.credentials ()) {
			authenticated = authenticateConnection (credential);
			if (authenticated)
				break;
		}
		return authenticated;
	}

	private Boolean authenticateConnection(ServerCredential credential) {
		boolean authenticated = false;
		String username = credential.getLoginid ();
		String password = credential.getPassword ();
		try {
			authenticated = authenticateWithUsernamePassword (username, password);
		} catch (IOException ex) {
			logger.error ("Authentication with {} on server {} Failed", credential.getLoginid (),
					this.server.id (), ex);
		}
		return authenticated;
	}

	private Boolean authenticateWithUsernamePassword(String username, String password)
			throws IOException {
		boolean authenticated = false;
		logger.debug ("Trying Authentication on server ({}) with : {}", this.server.id (), username);
		authenticated = connection.authenticateConnectionWithUsernameAndPassword (username,
				password);
		return authenticated;
	}

	/**
	 * Closes SSH connection passed to it. If different with its own connection,
	 * then closes both for preventing any memory leaks.
	 * 
	 * @param connection
	 * @throws ServerConnectionException
	 *         if unable to close the connection.
	 */
	public void disconnect(CustomizedSSHConnection connection) {
		logger.debug ("Closing the connection");
		if (connection == null)
			throw new ServerConnectionException ("Invalid connection passed to disconnect",
					new NullPointerException ());
		else
			closeConnection ();
	}

	private void closeConnection() {
		if (this.connection != null)
			if (connection.equals (this.connection)) {
				connection.disconnect ();
			} else {
				connection.disconnect ();
				this.connection.disconnect ();
			}
		else
			connection.disconnect ();
	}
}
