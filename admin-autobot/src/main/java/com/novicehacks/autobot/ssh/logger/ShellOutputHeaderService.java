package com.novicehacks.autobot.ssh.logger;

import com.novicehacks.autobot.logger.DefaultOutputHeaderService;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

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
