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
public final class CustomizedSSHConnection implements SSHConnection {
	private Connection			connection;
	private String				IPAddress;
	private boolean				isAuthenticated				= false;
	public static final String	IPAddressRegex				= "\\w{1,}(\\.\\w{1,}){1,2}|((\\d{1,3})\\.){3}\\d{1,3}";
	public static final String	ConnectionUnavailableMsg	= "Connect method should be called before authentication";
	public static final String	IPAddressNullMsg			= "IP Address cannot be null";
	public static final String	IPAddressEmptyMsg			= "IP Address cannot be empty String";
	public static final String	IPAddressInvalidMsg			= "Invalid IP Address Format";
	private static final String	NotAuthenticatedMsg			= "Connection must be authenticated before creating Session";
	private Logger				logger						= LogManager
																	.getLogger (CustomizedSSHConnection.class);

	/**
	 * Alternate method for constructor call, returns a new instance everytime.
	 * 
	 * @param ipAddress
	 * @return
	 */
	public static CustomizedSSHConnection getNewInstance(String ipAddress) {
		return new CustomizedSSHConnection (ipAddress);
	}

	protected CustomizedSSHConnection (String ipAddress) {
		validateParams (ipAddress);
		this.IPAddress = ipAddress;
	}

	private void validateParams(String ipAddress) {
		if (BotUtils.HasNullReferences (ipAddress)) {
			throw new IllegalArgumentException (IPAddressNullMsg);
		}
		if (ipAddress.equals ("") || ipAddress.length () == 0) {
			throw new IllegalArgumentException ();
		}
		if (!ipAddress.matches (IPAddressRegex)) {
			throw new IllegalArgumentException (IPAddressInvalidMsg);
		}
	}

	@Override
	public void connect() throws IOException {
		connect (0, 0);
	}

	@Override
	public void connect(int keyExchangeTimeoutInMillis, int connectionTimeoutInMillis)
			throws IOException {
		this.logger.entry ();
		this.connection = new Connection (this.IPAddress);
		this.connection.connect (null, connectionTimeoutInMillis, keyExchangeTimeoutInMillis);
		this.logger.exit ();
	}

	@Override
	public void disconnect() {
		this.logger.entry ();
		checkForValidConnection ();
		this.connection.close ();
		this.connection = null;
		this.logger.exit ();
	}

	@Override
	public boolean authenticateConnectionWithUsernameAndPassword(String username, String password)
			throws IOException {
		boolean status;
		this.logger.entry (username, password);
		checkForValidConnection ();
		status = authenticateConnection (username, password);
		this.isAuthenticated = status;
		this.logger.exit (status);
		return status;
	}

	@Override
	public SSHSession openSession() throws IOException {
		Session session;
		CustomizedSSHSession sessionWrapper;

		this.logger.entry ();
		checkForValidConnection ();
		session = openSessionIfAuthenticated ();
		sessionWrapper = new CustomizedSSHSession (session);
		return sessionWrapper;
	}

	private Session openSessionIfAuthenticated() throws IOException {
		Session session;
		if (this.isAuthenticated)
			session = this.connection.openSession ();
		else
			throw new IllegalStateException (NotAuthenticatedMsg);
		return session;
	}

	private boolean authenticateConnection(String username, String password) throws IOException {
		return this.connection.authenticateWithPassword (username, password);
	}

	private void checkForValidConnection() {
		if (connectionNotAvailable ())
			throw new IllegalStateException (ConnectionUnavailableMsg);
	}

	@Override
	public boolean isConnectionAvailable() {
		if (connectionNotAvailable ())
			return false;
		else
			return true;
	}

	private boolean connectionNotAvailable() {
		if (this.connection == null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isAuthenticated() {
		return this.isAuthenticated;
	}

}
