package com.novicehacks.autobot.ssh.logger;

import com.novicehacks.autobot.services.impl.DefaultOutputFooterService;

public final class ShellOutputFooterService extends DefaultOutputFooterService {
	@Override
	public boolean equals(Object obj) {
		if (obj != null)
			if (obj instanceof ShellOutputFooterService)
				return true;
		return false;
	}
}
