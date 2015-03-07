package com.novicehacks.autobot.ssh.commandexecutor.sequential;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.types.RunnableTask;

/**
 * Asynchronous Task to consume all the remote output.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class ShellSessionOutputGobblerTask implements RunnableTask {
	private boolean threadStarted = false;
	private InputStream remoteInputStream;
	private ShellSessionController sessionController;
	private Logger logger = LogManager.getLogger (ShellSessionOutputGobblerTask.class);

	protected ShellSessionOutputGobblerTask (ShellSessionController sessionController) {
		this.sessionController = sessionController;
		this.remoteInputStream = sessionController.getRemoteInputStream ();
	}

	@Override
	public void run() {
		this.logger.entry ();
		this.threadStarted = true;
		startConsumingOutput ();
		this.logger.exit ();
	}

	private void startConsumingOutput() {
		this.logger.entry ();
		blockUntilCommandExecutionStarted ();
		this.logger.trace ("Reading the remote output from the server");
		readRemoteOutput ();
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
				this.sessionController.acquireLockOnShell ();
				this.logger.trace ("Shell Lock Acquired By Remote Consumer");
				sendInputSignalAndWaitWhenRemoteOutputNotAvailable ();
				if (outputAvailable ())
					readAndLogCommandOutput ();
				this.sessionController.releaseLockOnShell ();
				this.logger.trace ("Shell Lock Released By Remote Consumer");
			}
		} catch (InterruptedException e) {
			this.logger.trace ("Remote Output consumer thread interrupted");
			BotUtils.DoNotPropogateInterrupt (e);
		} catch (Exception e) {
			this.logger.error ("Exception when reading remote server output", e);
		}
		this.logger.exit ();
	}

	private boolean outputAvailable() throws IOException {
		if (this.remoteInputStream.available () > 0)
			return true;
		return false;
	}

	private void readAndLogCommandOutput() {
		byte[] buff = new byte[8192];
		try {
			int len = this.remoteInputStream.read (buff);
			if (len == -1)
				return;
			logOutputWhenNeeded (buff);
		} catch (IOException ex) {
			this.logger.error ("Failed While reading from Remote Consumer", ex);
		}

	}

	private void logOutputWhenNeeded(byte[] buff) {
		this.logger.trace (this.sessionController.byteArrayToString (buff));
		if (this.sessionController.isCommandExecutionStarted ())
			this.sessionController.appendCommandOutput (buff);
	}

	private void sendInputSignalAndWaitWhenRemoteOutputNotAvailable() throws IOException,
			InterruptedException {
		this.logger.entry ();
		if (this.remoteInputStream.available () == 0) {
			this.sessionController.signalSendInputAndWaitForInputComplete ();
		}
		this.logger.exit ();
	}

	@Override
	public boolean isThreadStarted() {
		return this.threadStarted;
	}

}
