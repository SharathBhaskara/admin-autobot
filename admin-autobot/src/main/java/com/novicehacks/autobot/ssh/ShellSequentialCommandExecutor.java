package com.novicehacks.autobot.ssh;

import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.ssh.exception.CommandExecutionCompleteException;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

/**
 * Command Executor
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
class ShellSequentialCommandExecutor {
	private Server					server;
	private Command[]				executableCommands;
	private ShellSessionController	sessionController;
	private PrintStream				remoteCommandWriter;

	private Logger					logger	= LogManager
													.getLogger (ShellSequentialCommandExecutor.class);

	protected ShellSequentialCommandExecutor (	Server server,
												Command[] executableCommands,
												ShellSessionController controller) {
		this.server = server;
		this.executableCommands = executableCommands;
		this.sessionController = controller;
		this.remoteCommandWriter = new PrintStream (this.sessionController.getRemoteOutputStream ());
	}

	protected void startExecution() throws InterruptedException {
		this.logger.entry ();
		sessionController.acquireLockOnShell ();
		this.logger.trace ("Shell Lock acquired by Command Executor");
		this.executeInitCommands ();
		this.waitUntilOutputConsumed ();
		this.executeExecutableCommands ();
		this.waitUntilOutputConsumed ();
		sessionController.releaseLockOnShell ();
		this.logger.trace ("Shell Lock released by Command Executor");
		this.logger.exit ();
	}

	private void executeInitCommands() throws InterruptedException {
		for (String initCommand : this.server.initCommands ()) {
			this.logger.trace ("Executing Init Command {}", initCommand);
			this.sendCommandToRemoteShell (initCommand, true, false);
			this.waitUntilOutputConsumed ();
			this.logger.trace ("Execution of Init Command ({}) Completed ", initCommand);
		}
	}

	private void waitUntilOutputConsumed() throws InterruptedException {
		try {
			this.waitUntilTheRemoteOutputIsConsumed ();
		} catch (CommandExecutionCompleteException e) {
			this.logger.debug ("Command Execution Complete");
		}
	}

	private void executeExecutableCommands() throws InterruptedException {
		this.logger.entry ();
		this.processExecutableCommands ();
		this.logger.exit ();
	}

	private void processExecutableCommands() throws InterruptedException {
		for (Command command : this.executableCommands) {
			this.logger.trace ("Executing Command {} With Id : {}", command.command (),
					command.id ());
			sessionController.appendHeaderToOutput (command);
			this.sendCommandToRemoteShell (command.command (), false, true);
			this.waitUntilOutputConsumed ();
			sessionController.appendFooterToOutput ();
			this.logger.trace ("Execution Completed For Command {} With Id : {}",
					command.command (), command.id ());
		}
	}

	private void sendCommandToRemoteShell(	String command,
											boolean isInitCommand,
											boolean isExecutable) throws InterruptedException {
		this.logger.entry ();
		this.setCommandExecutionFlag (isInitCommand, isExecutable);
		this.sendCommand (command);
		this.logger.exit ();
	}

	private void sendCommand(String command) throws InterruptedException {
		this.logger.entry ();
		sessionController.blockUntilSendInputSignalled ();
		this.writeDataToRemoteStreamAndWait (command);
		sessionController.signalInputComplete ();
		this.logger.exit ();
	}

	private void writeDataToRemoteStreamAndWait(final String command) throws InterruptedException {
		char[] data = command.toCharArray ();
		remoteCommandWriter.println (data);
		this.waitForCommandToBeProcessed ();
	}

	private void waitForCommandToBeProcessed() throws InterruptedException {
		// TODO optimal time to wait before executing the next command. If
		// reduced the output will be garbled with faulty header and footer
		// alignments.
		Thread.sleep (500);
	}

	private void setCommandExecutionFlag(boolean isInitCommand, boolean isExecutable) {
		if (isInitCommand)
			sessionController.setInitCommandExecutionStarted (true);
		if (isExecutable)
			sessionController.setCommandExecutionStarted (true);
	}

	private void waitUntilTheRemoteOutputIsConsumed() throws InterruptedException,
			CommandExecutionCompleteException {
		this.logger.entry ();
		sessionController.acquireLockOnShell ();
		this.logger.trace ("Shell Lock Acquired by WaitForFinalOutput");
		sessionController.blockUntilSendInputSignalled ();
		this.logger.debug ("Final Output from the remote received, hence interrupting");
		sessionController.releaseLockOnShell ();
		this.logger.trace ("Shell Lock Relased by WaitForFinalOutput");
		this.logger.exit ();
		throw new CommandExecutionCompleteException ();

	}
}
