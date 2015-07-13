package com.novicehacks.autobot.executor.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SSHSession {
	public OutputStream stdInputStream();

	public InputStream stdOutputStream();

	public InputStream stdErrorStream();

	public void startShell() throws IOException;

	public void getTerminal() throws IOException;

	public void execCommand(String command) throws IOException;

	public void closeSession();
}
