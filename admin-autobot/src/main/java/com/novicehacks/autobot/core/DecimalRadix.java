package com.novicehacks.autobot.core;

/**
 * Used by {@link BotUtils} to convert a string to integer.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * @see BotUtils
 */
public enum DecimalRadix {
	Binary (2), Octal (8), Decimal (10), Hexadecimal (16);
	private int value;

	private DecimalRadix (int v) {
		this.value = v;
	}

	public int value() {
		return this.value;
	}
}
