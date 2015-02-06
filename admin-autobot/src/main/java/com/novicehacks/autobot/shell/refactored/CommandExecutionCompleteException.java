package com.novicehacks.autobot.shell.refactored;

public class CommandExecutionCompleteException extends Exception {

	private static final long	serialVersionUID	= -801815579414632844L;
	private String				description;
	private Throwable			reason;

	public CommandExecutionCompleteException (String message, Throwable reason) {
		super (message, reason);
		this.description = message;
		this.reason = reason;
	}

	public CommandExecutionCompleteException (String message) {
		this (message, null);
	}

	public CommandExecutionCompleteException () {
		this ("Command Execution Complete");
	}

	@Override
	public String toString() {
		String reason = this.reason == null ? "" : this.reason.toString ();
		return description + "( " + this + " )" + " {" + reason + "}";
	}
}
