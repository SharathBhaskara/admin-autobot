package com.novicehacks.autobot.services.impl;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.services.OutputFooterService;

public class DefaultOutputFooterService implements OutputFooterService {
	private final int seperatorLength = 50;
	private String footerSeparatorContent;

	@Override
	public String footer() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append (getFooterSeperator ());
		buffer.append (BotUtils.newLine ());
		buffer.append (getFooterSeperator ());
		return buffer.toString ();
	}

	private String getFooterSeperator() {
		StringBuilder buffer = new StringBuilder ();
		if (this.footerSeparatorContent != null)
			return this.footerSeparatorContent;
		for (int count = 0; count < this.seperatorLength; count++) {
			buffer.append ("*");
		}
		this.footerSeparatorContent = buffer.toString ();
		return buffer.toString ();
	}
}
