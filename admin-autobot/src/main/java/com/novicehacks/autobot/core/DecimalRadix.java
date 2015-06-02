package com.novicehacks.autobot.core;

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
