package com.novicehacks.autobot.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

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
	private AtomicBoolean	sessionClosed	= new AtomicBoolean (false);
	private AtomicBoolean	commandExecuted	= new AtomicBoolean (false);
	private InputStream		remoteOutputStream;
	private InputStream		remoteErrorStream;
	private OutputStream	remoteInputStream;

	private Logger			logger			= LogManager.getLogger (CustomizedSSHSession.class);

	protected CustomizedSSHSession (Session session) {
		this.session = session;
		this.remoteOutputStream = session.getStdout ();
		this.remoteInputStream = session.getStdin ();
		this.remoteErrorStream = session.getStderr ();
	}

	public OutputStream getStdIn() {
		throwExceptionIfSessionClosed ();
		return this.remoteInputStream;
	}

	public InputStream getStdOut() {
		throwExceptionIfSessionClosed ();
		return this.remoteOutputStream;
	}

	public InputStream getStdErr() {
		throwExceptionIfSessionClosed ();
		return this.remoteErrorStream;
	}

	public void startShell() throws IOException {
		logger.entry ();
		throwExceptionIfSessionClosed ();
		this.session.startShell ();
		logger.exit ();
	}

	public void requestDumbPTY() throws IOException {
		logger.entry ();
		throwExceptionIfSessionClosed ();
		this.session.requestDumbPTY ();
		logger.exit ();
	}

	public void execCommand(String command) throws IOException {
		logger.entry ();
		throwIfCommandExecutedBefore ();
		this.session.execCommand (command);
		commandExecuted.set (true);
		logger.exit ();
	}

	private void throwIfCommandExecutedBefore() {
		if (commandExecuted.get ())
			throw new IllegalStateException ("Onle one command can be executed on a session");
	}

	public void closeSession() {
		logger.entry ();
		throwExceptionIfSessionClosed ();
		closeIOStreams ();
		this.session.close ();
		sessionClosed.set (true);
		logger.exit ();
	}

	private void throwExceptionIfSessionClosed() {
		if (sessionClosed.get ())
			throw new IllegalStateException ("Session closed already");
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
