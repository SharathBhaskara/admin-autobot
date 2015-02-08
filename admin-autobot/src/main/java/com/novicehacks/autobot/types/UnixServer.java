package com.novicehacks.autobot.types;

/**
 * A Unix Server is used to store the credentials and execute the ShellCommands
 * on the server. Unix Server and Shell Command are used by the ShellExecutor
 * for executing on the physical machines identified by the IPAddress in this
 * instance.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see ShellCommand
 * @see ShellExecutor
 */
public class UnixServer implements Server {
	private String				serverLine;
	private String				description;
	private String				id;
	private String				ipAddress;
	private String				name;
	private ServerCredential[]	credentials;
	private String[]			initializationCommands;

	public UnixServer (String line) {
		this.serverLine = line;
	}

	@Override
	public ServerCredential[] credentials() {
		return this.credentials;
	}

	@Override
	public String ipaddress() {
		return this.ipAddress;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public String id() {
		return this.id;
	}

	@Override
	public int compareTo(Server object) {
		return this.id ().compareTo (object.id ());
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public ServerCredential[] getCredentials() {
		return credentials;
	}

	public void setCredentials(ServerCredential[] credentials) {
		this.credentials = credentials;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerLine() {
		return serverLine;
	}

	@Override
	public String toString() {
		return serverLine;
	}

	@Override
	public String mapKey() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * @see com.novicehacks.autobot.types.Server#initCommands()
	 */
	@Override
	public String[] initCommands() {
		return this.initializationCommands;

	}

	/**
	 * @return the initializationCommands
	 */
	public String[] getInitializationCommands() {
		return initializationCommands;
	}

	/**
	 * @param initializationCommands
	 *        the initializationCommands to set
	 */
	public void setInitializationCommands(String[] initializationCommands) {
		this.initializationCommands = initializationCommands;
	}

}
