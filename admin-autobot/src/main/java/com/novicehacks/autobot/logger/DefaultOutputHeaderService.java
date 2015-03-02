package com.novicehacks.autobot.logger;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

public class DefaultOutputHeaderService implements OutputHeaderService {
	private Server server;
	private Command command;
	private StringBuilder buffer;
	private final int seperatorLength = 50;
	private String headerSeparatorContent;

	public DefaultOutputHeaderService (Server server, Command command) {
		validateParams (server, command);
		this.server = server;
		this.command = command;
	}

	private void validateParams(Server server, Command command) {
		if (BotUtils.HasNullReferences (server, command))
			throw new IllegalArgumentException ("Invalid params with null values");
	}

	@Override
	public String header() {
		this.buffer = new StringBuilder ();
		this.buffer.append (BotUtils.newLine ());
		this.buffer.append (getHeaderSeperator ());
		this.buffer.append (BotUtils.newLine ());
		this.buffer.append (getHeading ());
		this.buffer.append (BotUtils.newLine ());
		this.buffer.append (getHeaderSeperator ());
		this.buffer.append (BotUtils.newLine ());
		return this.buffer.toString ();
	}

	private String getHeading() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append (getServerInfo ());
		buffer.append (BotUtils.newLine ());
		buffer.append (getCommandInfo ());
		return buffer.toString ();
	}

	private String getServerInfo() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append ("Server : ");
		buffer.append (this.server.id ());
		buffer.append (" (");
		buffer.append (this.server.name ());
		buffer.append (")");
		return buffer.toString ();
	}

	private String getCommandInfo() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append ("Command : ");
		buffer.append (this.command.id ());
		buffer.append (" (");
		buffer.append (this.command.command ());
		buffer.append (")");
		return buffer.toString ();
	}

	private String getHeaderSeperator() {
		StringBuilder buffer = new StringBuilder ();
		if (this.headerSeparatorContent != null)
			return this.headerSeparatorContent;
		for (int count = 0; count < this.seperatorLength; count++) {
			buffer.append ("+");
		}
		this.headerSeparatorContent = buffer.toString ();
		return buffer.toString ();
	}

}
