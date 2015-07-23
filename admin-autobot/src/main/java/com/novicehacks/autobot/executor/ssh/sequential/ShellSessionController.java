package com.novicehacks.autobot.executor.ssh.sequential;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.executor.ssh.logger.ShellOutputLoggerTaskHelper;

/**
 * SharedObject for controlling Session Input and Output
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class ShellSessionController {
	private Server server;
	private InputStream remoteInputStream;
	private OutputStream remoteOutputStream;
	private volatile boolean initCommandExecutionStarted = false;
	private volatile boolean commandExecutionStarted = false;
	private Lock shellLock = new ReentrantLock (true);
	private Condition SendInput = this.shellLock.newCondition ();
	private Condition InputComplete = this.shellLock.newCondition ();
	private StringBuilder commandOutput = new StringBuilder ();

	private Logger logger = LogManager.getLogger (ShellSessionController.class);

	protected ShellSessionController (Server server) {
		this.server = server;
	}

	void acquireLockOnShell() throws InterruptedException {
		this.shellLock.lockInterruptibly ();
	}

	void releaseLockOnShell() {
		this.shellLock.unlock ();
	}

	void blockUntilSendInputSignalled() {
		this.logger.trace ("Waiting for SendInput Signal");
		this.SendInput.awaitUninterruptibly ();
		this.logger.trace ("SendInput Signal Received");
	}

	void signalInputComplete() {
		this.logger.trace ("Signalling InputComplete");
		this.InputComplete.signal ();
	}

	void signalSendInputAndWaitForInputComplete() throws InterruptedException {
		this.logger.trace ("Signalling SendInput");
		this.SendInput.signal ();
		this.logger.trace ("Waiting for InputComplete Signal");
		this.InputComplete.await (1, TimeUnit.SECONDS);
		this.logger.trace ("InputComplete Signal Received or Timedout");
	}

	void appendHeaderToOutput(Command command) {
		String data;
		data = ShellOutputLoggerTaskHelper.getInstance ().headerService (this.server, command)
				.header ();
		this.commandOutput.append (data);
	}

	void appendFooterToOutput() {
		String data;
		data = ShellOutputLoggerTaskHelper.getInstance ().footerService ().footer ();
		this.commandOutput.append (BotUtils.newLine ());
		this.commandOutput.append (data);
	}

	void appendCommandOutput(byte[] buff) {
		String data;
		data = byteArrayToString (buff);
		this.commandOutput.append (data.trim ());
	}

	String byteArrayToString(byte[] buff) {
		String data;
		data = new String (buff);
		return data;
	}

	public boolean isInitCommandExecutionStarted() {
		return this.initCommandExecutionStarted;
	}

	public void setInitCommandExecutionStarted(boolean initCommandExecutionStarted) {
		this.initCommandExecutionStarted = initCommandExecutionStarted;
	}

	public boolean isCommandExecutionStarted() {
		return this.commandExecutionStarted;
	}

	public void setCommandExecutionStarted(boolean commandExecutionStarted) {
		this.commandExecutionStarted = commandExecutionStarted;
	}

	/**
	 * @return the remoteInputStream
	 */
	public InputStream getRemoteInputStream() {
		return this.remoteInputStream;
	}

	/**
	 * @param remoteInputStream
	 *        the remoteInputStream to set
	 */
	public void setRemoteInputStream(InputStream remoteInputStream) {
		this.remoteInputStream = remoteInputStream;
	}

	/**
	 * @return the remoteOutputStream
	 */
	public OutputStream getRemoteOutputStream() {
		return this.remoteOutputStream;
	}

	/**
	 * @param remoteOutputStream
	 *        the remoteOutputStream to set
	 */
	public void setRemoteOutputStream(OutputStream remoteOutputStream) {
		this.remoteOutputStream = remoteOutputStream;
	}

	protected StringBuilder getCommandOutput() {
		return this.commandOutput;
	}

}
