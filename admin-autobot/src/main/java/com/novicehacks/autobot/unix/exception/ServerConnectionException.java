package com.novicehacks.autobot.unix.exception;

/**
 * Wrapper for exceptions raised when trying to connect to the server.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class ServerConnectionException extends RuntimeException {
	private static final long	serialVersionUID	= -1128071325823113188L;
	private String				description;
	private Throwable			reason;

	public ServerConnectionException (String message, Throwable reason) {
		super (message, reason);
		this.description = message;
		this.reason = reason;
	}

	public ServerConnectionException (String message) {
		this (message, null);
	}

	@Override
	public String toString() {
		String reason = this.reason == null ? "" : this.reason.toString ();
		return description + "( " + super.toString () + " )" + " {" + reason + "}";
	}
}
