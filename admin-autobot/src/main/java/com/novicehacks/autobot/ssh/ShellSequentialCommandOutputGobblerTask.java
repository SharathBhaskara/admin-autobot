package com.novicehacks.autobot.ssh;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.BotUtils;

/**
 * Asynchronous Task to consume all the remote output.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class ShellSequentialCommandOutputGobblerTask implements Runnable {
	private InputStream				remoteInputStream;
	private ShellSessionController	sessionController;
	private Logger					logger	= LogManager
													.getLogger (ShellSequentialCommandOutputGobblerTask.class);

	protected ShellSequentialCommandOutputGobblerTask (ShellSessionController sessionController) {
		this.sessionController = sessionController;
		this.remoteInputStream = sessionController.getRemoteInputStream ();
	}

	@Override
	public void run() {
		this.logger.entry ();
		this.startConsumingOutput ();
		this.logger.exit ();
	}

	private void startConsumingOutput() {
		this.logger.entry ();
		this.blockUntilCommandExecutionStarted ();
		this.logger.trace ("Reading the remote output from the server");
		this.readRemoteOutput ();
		this.logger.exit ();
	}

	private void blockUntilCommandExecutionStarted() {
		this.logger.entry ();
		while (!this.sessionController.isInitCommandExecutionStarted ()) {}
		this.logger.debug ("Init Command Received on Shell");
		this.logger.exit ();
	}

	private void readRemoteOutput() {
		this.logger.entry ();
		try {
			while (true) {
				sessionController.acquireLockOnShell ();
				this.logger.trace ("Shell Lock Acquired By Remote Consumer");
				this.sendInputSignalAndWaitWhenRemoteOutputNotAvailable ();
				if (outputAvailable ())
					this.readAndLogCommandOutput ();
				sessionController.releaseLockOnShell ();
				this.logger.trace ("Shell Lock Released By Remote Consumer");
			}
		} catch (InterruptedException e) {
			this.logger.debug ("Remote Output Consumer Interrupted");
			BotUtils.propogateInterruptIfExist (e);
		} catch (Exception e) {
			this.logger.error ("Exception when reading remote server output", e);
		}
		this.logger.exit ();
	}

	private boolean outputAvailable() throws IOException {
		if (remoteInputStream.available () > 0)
			return true;
		return false;
	}

	private void readAndLogCommandOutput() {
		byte[] buff = new byte[8192];
		try {
			int len = this.remoteInputStream.read (buff);
			if (len == -1)
				return;
			this.logOutputWhenNeeded (buff);
		} catch (IOException ex) {
			this.logger.error ("Failed While reading from Remote Consumer", ex);
		}

	}

	private void logOutputWhenNeeded(byte[] buff) {
		this.logger.trace (sessionController.byteArrayToString (buff));
		if (this.sessionController.isCommandExecutionStarted ())
			sessionController.appendCommandOutput (buff);
	}

	private void sendInputSignalAndWaitWhenRemoteOutputNotAvailable() throws IOException,
			InterruptedException {
		this.logger.entry ();
		if (remoteInputStream.available () == 0) {
			sessionController.signalSendInputAndWaitForInputComplete ();
		}
		this.logger.exit ();
	}

}
