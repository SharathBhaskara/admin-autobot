package com.novicehacks.autobot.config;

/**
 * TimeDelay is used as a constant definition for consistently using the time
 * delay, across the application.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public enum TimeDelay {
	delayInMins (3l),
	delayInSecs (90l),
	smallDelayInMins (1l),
	smallDelayInSecs (30l),
	largeDelayInMins (10),
	largeDelayInSecs (300);
	long delay;

	private TimeDelay (long value) {
		this.delay = value;
	}

	public long delay() {
		return this.delay;
	}
}
