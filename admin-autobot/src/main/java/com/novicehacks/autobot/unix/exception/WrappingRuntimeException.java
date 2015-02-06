package com.novicehacks.autobot.unix.exception;

public abstract class WrappingRuntimeException extends RuntimeException {

	private static final long	serialVersionUID	= 2151031716892685230L;

	private String				description;
	private Throwable			reason;

	public WrappingRuntimeException (String message, Throwable reason) {
		super (message, reason);
		this.description = message;
		this.reason = reason;
	}

	public WrappingRuntimeException (String message) {
		super (message);
	}

	@Override
	public String toString() {
		String reason = this.reason == null ? "" : this.reason.toString ();
		return description + "( " + super.toString () + " )" + " {" + reason + "}";
	}
}
