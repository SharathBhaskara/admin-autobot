package com.novicehacks.autobot.executor.ssh.exception;

public class UnixOutputLoggingException extends WrappingRuntimeException {

	private static final long	serialVersionUID	= -3358380347642856909L;

	public UnixOutputLoggingException (String message, Throwable reason) {
		super (message, reason);
	}

	public UnixOutputLoggingException (String message) {
		this (message, null);
	}

}
