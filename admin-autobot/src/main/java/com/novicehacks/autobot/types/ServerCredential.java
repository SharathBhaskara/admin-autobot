package com.novicehacks.autobot.types;

/**
 * Login Id and Password Credentials used for logging in to the server. <br/>
 * <br/>
 * <em><strong>Note:</strong> sshKey authentication is for future use.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @See Server
 */
public class ServerCredential {

	private String loginid;
	private String password;
	@Deprecated
	private String sshKey;

	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSshKey() {
		return sshKey;
	}

	public void setSshKey(String sshKey) {
		this.sshKey = sshKey;
	}

	@Override
	public String toString() {
		return loginid + "-" + password;
	}

}
