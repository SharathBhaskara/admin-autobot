package com.novicehacks.autobot.core.exception;

import java.io.IOException;

public class WriteException extends IOException {

	private static final long serialVersionUID = 7443285309503057496L;
	private String description;
	private Throwable reason;

	public WriteException (String message, Throwable reason) {
		super (message, reason);
		this.description = message;
		this.reason = reason;
	}

	public WriteException (String message) {
		this (message, null);
	}

	public WriteException () {
		this ("Write Exception");
	}

	@Override
	public String toString() {
		String reason = this.reason == null ? "" : this.reason.toString ();
		return this.description + "( " + this + " )" + " {" + reason + "}";
	}
}
