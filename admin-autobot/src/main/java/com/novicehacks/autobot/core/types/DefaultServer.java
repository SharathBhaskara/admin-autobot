package com.novicehacks.autobot.core.types;

/**
 * Default Server implements the generic behavior for the {@link Server} API,
 * while leaving the implementation of {@link Mappable} and {@link Comparable}
 * interfaces to its children for customizing their own
 * logic.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * @see Server
 */
public abstract class DefaultServer implements Server {
	private String parsedServerLine;
	private String id;
	private String ipAddress;
	private String name;
	private ServerCredential[] credentials;
	private String[] initializationCommands;

	public DefaultServer (String line) {
		this.parsedServerLine = line;
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
	public String[] initCommands() {
		return this.initializationCommands;

	}

	public String parserdServerLine() {
		return this.parsedServerLine;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCredentials(ServerCredential[] credentials) {
		this.credentials = credentials;
	}

	public void setInitializationCommands(String[] initializationCommands) {
		this.initializationCommands = initializationCommands;
	}

	@Override
	public String toString() {
		return this.parsedServerLine;
	}

}
