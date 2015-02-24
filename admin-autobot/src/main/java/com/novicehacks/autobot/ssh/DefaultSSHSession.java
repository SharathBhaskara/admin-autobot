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
public final class DefaultSSHSession implements SSHSession {

	private Session session;
	private AtomicBoolean sessionClosed = new AtomicBoolean (false);
	private AtomicBoolean commandExecuted = new AtomicBoolean (false);
	private AtomicBoolean terminalRequested = new AtomicBoolean (false);
	private AtomicBoolean shellStarted = new AtomicBoolean (false);
	private InputStream remoteOutputStream;
	private InputStream remoteErrorStream;
	private OutputStream remoteInputStream;

	private Logger logger = LogManager.getLogger (DefaultSSHSession.class);

	DefaultSSHSession (Session session) {
		this.session = session;
		this.remoteOutputStream = session.getStdout ();
		this.remoteInputStream = session.getStdin ();
		this.remoteErrorStream = session.getStderr ();
	}

	@Override
	public OutputStream stdInputStream() {
		throwExceptionIfSessionClosed ();
		return this.remoteInputStream;
	}

	@Override
	public InputStream stdOutputStream() {
		throwExceptionIfSessionClosed ();
		return this.remoteOutputStream;
	}

	@Override
	public InputStream stdErrorStream() {
		throwExceptionIfSessionClosed ();
		return this.remoteErrorStream;
	}

	@Override
	public void startShell() throws IOException {
		this.logger.entry ();
		synchronized (this.shellStarted) {
			checkAndThrowExceptionIfNeeded ();
			this.session.startShell ();
			this.shellStarted.set (true);
		}
		this.logger.exit ();
	}

	@Override
	public void getTerminal() throws IOException {
		this.logger.entry ();
		synchronized (this.terminalRequested) {
			checkAndThrowExceptionIfNeeded ();
			throwIfTerminalRequestedBefore ();
			this.session.requestDumbPTY ();
			this.terminalRequested.set (true);
		}
		this.logger.exit ();
	}

	private void throwIfTerminalRequestedBefore() {
		if (this.terminalRequested.get ())
			throw new IllegalStateException ("Onle one terminal can be requested on a session");
	}

	@Override
	public void execCommand(String command) throws IOException {
		this.logger.entry ();
		synchronized (this.commandExecuted) {
			checkAndThrowExceptionIfNeeded ();
			this.session.execCommand (command);
			this.commandExecuted.set (true);
		}
		this.logger.exit ();
	}

	private void checkAndThrowExceptionIfNeeded() {
		throwExceptionIfSessionClosed ();
		throwIfCommandExecutedBefore ();
		throwIfShellStartedBefore ();
	}

	private void throwIfCommandExecutedBefore() {
		if (this.commandExecuted.get ())
			throw new IllegalStateException ("Onle one command can be executed on a session");
	}

	private void throwIfShellStartedBefore() {
		if (this.shellStarted.get ())
			throw new IllegalStateException ("Onle one shell can be started on a session");
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
