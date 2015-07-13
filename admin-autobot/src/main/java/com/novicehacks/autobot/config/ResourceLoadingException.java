package com.novicehacks.autobot.config;

import com.novicehacks.autobot.executor.ssh.exception.WrappingRuntimeException;

public class ResourceLoadingException extends WrappingRuntimeException {
	private static final long serialVersionUID = -1190713912144589949L;

	public ResourceLoadingException (String message) {
		super (message);
	}

}
