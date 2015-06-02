package com.novicehacks.autobot.config;

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
