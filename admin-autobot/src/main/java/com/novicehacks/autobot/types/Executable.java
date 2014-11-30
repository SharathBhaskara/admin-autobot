package com.novicehacks.autobot.types;

import com.novicehacks.autobot.ExecutableManager;

/**
 * 
 * Executable is the command and server id combination, which will be executed
 * after every scheduled period of time. {@link ExecutableManager} will read all
 * the executables and will execute the commands on corresponding server.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see ExecutableManager
 */
public class Executable implements Comparable<Executable> {

	private String line;
	private String serverId;
	private String commandId;

	public Executable(String line) {
		this.line = line;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getLine() {
		return line;
	}

	@Override
	public String toString() {
		return line;
	}

	@Override
	public int compareTo(Executable o) {
		return o.getLine().compareTo(this.getLine());
	}
}
