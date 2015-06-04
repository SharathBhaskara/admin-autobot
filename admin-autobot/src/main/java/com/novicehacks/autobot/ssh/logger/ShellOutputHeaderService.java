package com.novicehacks.autobot.ssh.logger;

import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.services.impl.DefaultOutputHeaderService;

public final class ShellOutputHeaderService extends DefaultOutputHeaderService {
	private Server server;
	private Command command;

	ShellOutputHeaderService (Server server, Command command) {
		super (server, command);
		this.server = server;
		this.command = command;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null)
			if (obj instanceof ShellOutputHeaderService) {
				ShellOutputHeaderService temp = (ShellOutputHeaderService) obj;
				if (this.server.equals (temp.server) && this.command.equals (temp.command))
					return true;
			}
		return false;
	}
}
