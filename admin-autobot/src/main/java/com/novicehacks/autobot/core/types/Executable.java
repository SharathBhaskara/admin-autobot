package com.novicehacks.autobot.core.types;

/**
 * Executable is a simple abstraction for server, command mapping that will be
 * scheduled for execution.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see ExecutableManager
 */
public class Executable implements Comparable<Executable> {
	private String line;
	private String serverId;
	private String commandId;

	public Executable (String line) {
		this.line = line;
	}

	public String getServerId() {
		return this.serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getCommandId() {
		return this.commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getLine() {
		return this.line;
	}

	@Override
	public String toString() {
		return this.line;
	}

	@Override
	public int compareTo(Executable o) {
		return o.getLine ().compareTo (getLine ());
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Executable) {
			Executable temp = (Executable) object;
			if (temp.getLine ().equals (getLine ())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode ();
	}
}
