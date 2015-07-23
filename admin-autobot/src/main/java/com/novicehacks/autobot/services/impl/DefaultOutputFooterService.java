package com.novicehacks.autobot.services.impl;

import java.time.Instant;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.services.OutputFooterService;

public abstract class DefaultOutputFooterService implements OutputFooterService {

	@Override
	public String footer() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append (footerSeparator ());
		buffer.append (footerContent ());
		buffer.append (footerSeparator ());
		return buffer.toString ();
	}

	private Object footerContent() {
		StringBuilder buffer = new StringBuilder ();
		buffer.append (BotUtils.newLine ());
		buffer.append ("timestamp : ");
		buffer.append (timeStamp ().getEpochSecond ());
		buffer.append (BotUtils.newLine ());
		return buffer.toString ();
	}

	Instant timeStamp() {
		return Instant.now ();
	}

}
