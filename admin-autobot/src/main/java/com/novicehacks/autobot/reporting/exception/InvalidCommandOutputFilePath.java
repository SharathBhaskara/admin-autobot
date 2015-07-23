package com.novicehacks.autobot.reporting.exception;

import com.novicehacks.autobot.executor.ssh.exception.WrappingRuntimeException;

public class InvalidCommandOutputFilePath extends WrappingRuntimeException {

	private static final long serialVersionUID = -6925282343945723400L;

	public InvalidCommandOutputFilePath (String message, Throwable reason) {
		super (message, reason);
	}

	public InvalidCommandOutputFilePath (String message) {
		this (message, null);
	}

}
