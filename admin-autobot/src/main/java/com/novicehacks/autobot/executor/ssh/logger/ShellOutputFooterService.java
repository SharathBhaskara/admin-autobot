package com.novicehacks.autobot.executor.ssh.logger;

import com.novicehacks.autobot.services.impl.DefaultOutputFooterService;

public class ShellOutputFooterService extends DefaultOutputFooterService {
	private final int seperatorLength = 50;
	private String footerSeparatorContent = "";

	@Override
	public String footerSeparator() {
		StringBuilder buffer = new StringBuilder ();
		if (this.footerSeparatorContent != null)
			return this.footerSeparatorContent;
		for (int count = 0; count < this.seperatorLength; count++) {
			buffer.append ("*");
		}
		this.footerSeparatorContent = buffer.toString ();
		return buffer.toString ();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null)
			if (obj instanceof ShellOutputFooterService)
				return true;
		return false;
	}

	@Override
	public int hashCode() {
		return this.footerSeparatorContent.hashCode () + 432 - 500;
	}
}
