package com.novicehacks.autobot.executor.ssh.sequential;

import java.io.PrintStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.executor.ssh.exception.CommandExecutionCompleteException;

/**
 * Command Executor
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class ShellSessionExecutor {
	private Server server;
	private Command[] executableCommands;
	private ShellSessionController sessionController;
	private PrintStream remoteCommandWriter;

	private Logger logger = LogManager.getLogger (ShellSessionExecutor.class);

	protected ShellSessionExecutor (Server server,
									Command[] executableCommands,
									ShellSessionController controller) {
		this.server = server;
		this.executableCommands = executableCommands;
		this.sessionController = controller;
		this.remoteCommandWriter = new PrintStream (this.sessionController.getRemoteOutputStream ());
	}

	protected void startExecution() throws InterruptedException {
		this.logger.entry ();
		this.sessionController.acquireLockOnShell ();
		this.logger.trace ("Shell Lock acquired by Command Executor");
		executeInitCommands ();
		waitUntilOutputConsumed ();
		executeExecutableCommands ();
		waitUntilOutputConsumed ();
		this.sessionController.releaseLockOnShell ();
		this.logger.trace ("Shell Lock released by Command Executor");
		this.logger.exit ();
	}

	private void executeInitCommands() throws InterruptedException {
		for (String initCommand : this.server.initCommands ()) {
			this.logger.trace ("Executing Init Command {}", initCommand);
			sendCommandToRemoteShell (initCommand, true, false);
			waitUntilOutputConsumed ();
			this.logger.trace ("Execution of Init Command ({}) Completed ", initCommand);
		}
	}

	private void waitUntilOutputConsumed() throws InterruptedException {
		try {
			waitUntilTheRemoteOutputIsConsumed ();
		} catch (CommandExecutionCompleteException e) {
			this.logger.debug ("Command Execution Complete : {}", e);
		}
	}

	private void executeExecutableCommands() throws InterruptedException {
		this.logger.entry ();
		processExecutableCommands ();
		this.logger.exit ();
	}

	private void processExecutableCommands() throws InterruptedException {
		for (Command command : this.executableCommands) {
			this.logger.trace ("Executing Command {} With Id : {}", command.commandTxt (),
					command.id ());
			this.sessionController.appendHeaderToOutput (command);
			sendCommandToRemoteShell (command.commandTxt (), false, true);
			waitUntilOutputConsumed ();
			this.sessionController.appendFooterToOutput ();
			this.logger.trace ("Execution Completed For Command {} With Id : {}",
					command.commandTxt (), command.id ());
		}
	}

	private void sendCommandToRemoteShell(	String command,
											boolean isInitCommand,
											boolean isExecutable) throws InterruptedException {
		this.logger.entry ();
		setCommandExecutionFlag (isInitCommand, isExecutable);
		sendCommand (command);
		this.logger.exit ();
	}

	private void sendCommand(String command) throws InterruptedException {
		this.logger.entry ();
		this.sessionController.blockUntilSendInputSignalled ();
		writeDataToRemoteStreamAndWait (command);
		this.sessionController.signalInputComplete ();
		this.logger.exit ();
	}

	private void writeDataToRemoteStreamAndWait(final String command) throws InterruptedException {
		char[] data = command.toCharArray ();
		this.remoteCommandWriter.println (data);
		waitForCommandToBeProcessed ();
	}

	private void waitForCommandToBeProcessed() throws InterruptedException {
		// TODO optimal time to wait before executing the next command. If
		// reduced the output will be garbled with faulty header and footer
		// alignments.
		Thread.sleep (500);
	}

	private void setCommandExecutionFlag(boolean isInitCommand, boolean isExecutable) {
		if (isInitCommand)
			this.sessionController.setInitCommandExecutionStarted (true);
		if (isExecutable)
			this.sessionController.setCommandExecutionStarted (true);
	}

	private void waitUntilTheRemoteOutputIsConsumed() throws InterruptedException,
			CommandExecutionCompleteException {
		this.logger.entry ();
		this.sessionController.acquireLockOnShell ();
		this.logger.trace ("Shell Lock Acquired by WaitForFinalOutput");
		this.sessionController.blockUntilSendInputSignalled ();
		this.logger.debug ("Final Output from the remote received, hence interrupting");
		this.sessionController.releaseLockOnShell ();
		this.logger.trace ("Shell Lock Relased by WaitForFinalOutput");
		this.logger.exit ();
		throw new CommandExecutionCompleteException ();

	}
}
