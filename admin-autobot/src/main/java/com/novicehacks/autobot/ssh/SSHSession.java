package com.novicehacks.autobot.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SSHSession {
	public OutputStream getStdIn();

	public InputStream getStdOut();

	public InputStream getStdErr();

	public void startShell() throws IOException;

	public void requestDumbPTY() throws IOException;

	public void execCommand(String command) throws IOException;

	public void closeSession();
}
