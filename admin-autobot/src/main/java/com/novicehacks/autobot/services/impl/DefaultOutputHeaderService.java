package com.novicehacks.autobot.services.impl;

import java.time.Instant;

import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.services.OutputHeaderService;

/**
 * Default implementation of the {@link OutputHeaderService}
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public abstract class DefaultOutputHeaderService implements OutputHeaderService {
	private Server server;
	private Command command;
	private StringBuilder buffer;
	
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
		this.buffer.append (headerSeparator ());
		this.buffer.append (getHeading ());
		this.buffer.append (headerSeparator ());
		return this.buffer.toString ();
	}

	private String getHeading() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		buffer.append (getServerInfo ());
		buffer.append (BotUtils.newLine ());
		buffer.append (getCommandInfo ());
		buffer.append (BotUtils.newLine ());
		buffer.append (executionTimestamp ());
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
		buffer.append (this.command.commandTxt ());
		buffer.append (")");
		return buffer.toString ();
	}

	private Object executionTimestamp() {
		StringBuilder buffer;
		buffer = new StringBuilder ();
		Instant time = getInstantTime ();
		buffer.append ("Execution Timestamp : ");
		buffer.append (time.getEpochSecond ());
		buffer.append (" (");
		buffer.append (time.toString ());
		buffer.append (")");
		return buffer.toString ();
	}

	Instant getInstantTime() {
		return Instant.now ();
	}

}
