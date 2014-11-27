package com.novicehacks.autobot.types;

/**
 * 
 * Executable is the command and server id combination, which will be executed
 * after every scheduled period of time.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
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
