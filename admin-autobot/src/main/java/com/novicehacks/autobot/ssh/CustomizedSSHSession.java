package com.novicehacks.autobot.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ethz.ssh2.Session;

/**
 * Single point of entry for working on SSH sessions of 3rd party SSH library.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public final class CustomizedSSHSession {

	private Session			session;
	private InputStream		remoteOutputStream;
	private InputStream		remoteErrorStream;
	private OutputStream	remoteInputStream;

	private Logger			logger	= LogManager.getLogger (CustomizedSSHSession.class);

	protected CustomizedSSHSession (Session session) {
		this.session = session;
		this.remoteOutputStream = session.getStdout ();
		this.remoteInputStream = session.getStdin ();
		this.remoteErrorStream = session.getStderr ();
	}

	public OutputStream getStdIn() {
		return this.remoteInputStream;
	}

	public InputStream getStdOut() {
		return this.remoteOutputStream;
	}

	public InputStream getStdErr() {
		return this.remoteErrorStream;
	}

	public void startShell() throws IOException {
		logger.entry ();
		this.session.startShell ();
		logger.exit ();
	}

	public void requestDumbPTY() throws IOException {
		logger.entry ();
		this.session.requestDumbPTY ();
		logger.exit ();
	}

	public void execCommand(String command) throws IOException {
		logger.entry ();
		this.session.execCommand (command);
		logger.exit ();
	}

	public void disconnect() {
		logger.entry ();
		closeIOStreams ();
		this.session.close ();
		logger.exit ();
	}

	private void closeIOStreams() {
		try {
			this.remoteErrorStream.close ();
			this.remoteInputStream.close ();
			this.remoteOutputStream.close ();
		} catch (IOException ex) {
			logger.warn ("Exception while closing IOStreams on session : ", ex);
		}
	}

}
