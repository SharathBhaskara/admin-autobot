package com.novicehacks.autobot.types;

/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class UnixServer implements Server {
	private String serverLine;
	private String description;
	private String id;
	private String ipAddress;
	private String name;
	private ServerCredential[] credentials;

	public UnixServer(String line) {
		this.serverLine = line;
	}

	public ServerCredential[] credentials() {
		return this.credentials;
	}

	public String ipaddress() {
		return this.ipAddress;
	}

	public String name() {
		return this.name;
	}

	public String id() {
		return this.id;
	}

	@Override
	public int compareTo(Server object) {
		return this.id().compareTo(object.id());
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

}
