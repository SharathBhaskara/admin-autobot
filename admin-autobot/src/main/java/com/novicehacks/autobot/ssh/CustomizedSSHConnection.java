package com.novicehacks.autobot.ssh;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import com.novicehacks.autobot.BotUtils;

/**
 * Single point of entry to the 3rd SSH library for creating SSH connections.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
final class CustomizedSSHConnection {
	private Connection	connection;
	private String		IPAddress;
	private Logger		logger	= LogManager.getLogger (CustomizedSSHConnection.class);

	protected CustomizedSSHConnection (String ipAddress) {
		validateParams (ipAddress);
		this.IPAddress = ipAddress;
	}

	private void validateParams(String ipAddress) {
		if (BotUtils.HasNullReferences (ipAddress)) {
			throw new IllegalArgumentException ("IP Address cannot be null");
		}
		if (ipAddress.equals ("") || ipAddress.length () == 0) {
			throw new IllegalArgumentException ("IP Address cannot be empty String");
		}
		if (!ipAddress.matches ("[a-zA-Z0-9]*.[a-zA-Z0-9]*|([0-9]{1,3}.){4}")) {
			throw new IllegalArgumentException ("Invalid IP Address Format");
		}
	}

	public void connect() throws IOException {
		connect (0, 0);
	}

	public void connect(int keyExchangeTimeoutInMillis, int connectionTimeoutInMillis)
			throws IOException {
		logger.entry ();
		connection = new Connection (this.IPAddress);
		connection.connect (null, connectionTimeoutInMillis, keyExchangeTimeoutInMillis);
		logger.exit ();
	}

	public void disconnect() {
		logger.entry ();
		checkForValidConnection ();
		this.connection.close ();
		logger.exit ();
	}

	public boolean authenticateConnectionWithUsernameAndPassword(String username, String password)
			throws IOException {
		boolean status;
		logger.entry (username, password);
		checkForValidConnection ();
		status = authenticateConnection (username, password);
		logger.exit (status);
		return status;
	}

	public CustomizedSSHSession openSession() throws IOException {
		Session session;
		CustomizedSSHSession sessionWrapper;

		logger.entry ();
		checkForValidConnection ();
		session = this.connection.openSession ();
		sessionWrapper = new CustomizedSSHSession (session);
		return sessionWrapper;
	}

	private boolean authenticateConnection(String username, String password) throws IOException {
		return this.connection.authenticateWithPassword (username, password);
	}

	private void checkForValidConnection() {
		if (connectionNotAvailable ())
			throw new IllegalStateException (
					"Connect method should be called before authentication");
	}

	private boolean connectionNotAvailable() {
		if (connection == null) {
			return true;
		}
		return false;
	}

}
