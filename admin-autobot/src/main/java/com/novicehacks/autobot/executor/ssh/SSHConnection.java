package com.novicehacks.autobot.executor.ssh;

import java.io.IOException;

public interface SSHConnection {
	public void connect() throws IOException;

	public void connect(int keyExchangeTimeoutInMillis, int connectionTimeoutInMillis)
			throws IOException;

	public void disconnect();

	public boolean authenticateConnectionWithUsernameAndPassword(String username, String password)
			throws IOException;

	public SSHSession openSession() throws IOException;

	public boolean isConnectionAvailable();

	public boolean isAuthenticated();
}
