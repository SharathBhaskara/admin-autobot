package com.novicehacks.autobot.config;

public class ConfigLoadingFailureException extends RuntimeException {

	private static final long serialVersionUID = -1058001592401919345L;

	public ConfigLoadingFailureException (String message, Throwable cause) {
		super (message, cause);
	}

}
