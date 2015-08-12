package com.novicehacks.autobot.executor.ssh.logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.services.impl.DefaultOutputHeaderService;

public class ShellOutputHeaderService extends DefaultOutputHeaderService {
	private Server server;
	private Command command;
	private String headerSeparatorContent;
	private final int seperatorLength = 50;

	ShellOutputHeaderService (Server server, Command command) {
		super (server, command);
		this.server = server;
		this.command = command;
	}

	@Override
	public String headerSeparator() {
		StringBuilder buffer = new StringBuilder ();
		buffer.append (BotUtils.newLine ());
		if (this.headerSeparatorContent != null)
			return this.headerSeparatorContent;
		for (int count = 0; count < this.seperatorLength; count++) {
			buffer.append ("+");
		}
		buffer.append (BotUtils.newLine ());
		this.headerSeparatorContent = buffer.toString ();
		return buffer.toString ();
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

	@Override
	public int hashCode() {
		return super.hashCode ();
	}
}
