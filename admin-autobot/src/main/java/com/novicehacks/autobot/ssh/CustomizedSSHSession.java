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
public final class CustomizedSSHSession implements SSHSession {

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

	@Override
	public OutputStream getStdIn() {
		throwExceptionIfSessionClosed ();
		return this.remoteInputStream;
	}

	@Override
	public InputStream getStdOut() {
		throwExceptionIfSessionClosed ();
		return this.remoteOutputStream;
	}

	@Override
	public InputStream getStdErr() {
		throwExceptionIfSessionClosed ();
		return this.remoteErrorStream;
	}

	@Override
	public void startShell() throws IOException {
		this.logger.entry ();
		throwExceptionIfSessionClosed ();
		this.session.startShell ();
		this.logger.exit ();
	}

	@Override
	public void requestDumbPTY() throws IOException {
		this.logger.entry ();
		throwExceptionIfSessionClosed ();
		this.session.requestDumbPTY ();
		this.logger.exit ();
	}

	@Override
	public void execCommand(String command) throws IOException {
		this.logger.entry ();
		throwIfCommandExecutedBefore ();
		this.session.execCommand (command);
		this.commandExecuted.set (true);
		this.logger.exit ();
	}

	private void throwIfCommandExecutedBefore() {
		if (this.commandExecuted.get ())
			throw new IllegalStateException ("Onle one command can be executed on a session");
	}

	@Override
	public void closeSession() {
		this.logger.entry ();
		throwExceptionIfSessionClosed ();
		closeIOStreams ();
		this.session.close ();
		this.sessionClosed.set (true);
		this.logger.exit ();
	}

	private void throwExceptionIfSessionClosed() {
		if (this.sessionClosed.get ())
			throw new IllegalStateException ("Session closed already");
	}

	private void closeIOStreams() {
		try {
			this.remoteErrorStream.close ();
			this.remoteInputStream.close ();
			this.remoteOutputStream.close ();
		} catch (IOException ex) {
			this.logger.warn ("Exception while closing IOStreams on session : ", ex);
		}
	}

}
