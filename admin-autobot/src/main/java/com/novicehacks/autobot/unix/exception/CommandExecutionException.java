package com.novicehacks.autobot.unix.exception;

import java.util.Collection;

public class CommandExecutionException extends WrappingRuntimeException {

	private static final long		serialVersionUID	= 461409950894065199L;
	private Collection<Throwable>	multipleReasons;

	public CommandExecutionException (String message, Throwable reason) {
		super (message, reason);
	}

	public CommandExecutionException (String message) {
		this (message, null);
	}

	public void setMultipleReasons(Collection<Throwable> failureReasons) {
		this.multipleReasons = failureReasons;
	}

	public Collection<Throwable> getMultipleReasons() {
		return this.multipleReasons;
	}

}
