package com.novicehacks.autobot.types;

/**
 * Authentication credentials in the format of Login Id and Password
 * combinations, these objects are used by {@link Server} to authenticate
 * connection to the remote machines.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see Server
 */
public class ServerCredential {

	private String loginid;
	private String password;
	@Deprecated
	private String sshKey;

	public String getLoginid() {
		return this.loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSshKey() {
		return this.sshKey;
	}

	public void setSshKey(String sshKey) {
		this.sshKey = sshKey;
	}

	@Override
	public String toString() {
		return this.loginid + "-" + this.password;
	}

}
