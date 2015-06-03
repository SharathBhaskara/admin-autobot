package com.novicehacks.autobot.core.types;

/**
 * ExitStatusCode will be used, when the application needs to exit due to erros.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public enum ExitStatusCode {
	ResourceLoadingFailed (1000);
	private int statusCode;

	private ExitStatusCode (int code) {
		statusCode = code;
	}

	public int value() {
		return statusCode;
	}
}
