package com.novicehacks.autobot.executor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;

public class ServerExecutableCommandMap extends HashMap<Server, Collection<Command>> {

	private static final long	serialVersionUID	= 1L;

	/**
	 * Adds the command into the Collection if the entry already exist,
	 * otherwise will create a new entry.
	 * 
	 * @param server
	 * @param command
	 * @return
	 */
	public Collection<Command> put(Server server, Command command) {
		if (super.containsKey (server))
			addCommandToExistingEntry (server, command);
		else
			addNewEntry (server, command);
		return super.get (server);

	}

	private void addCommandToExistingEntry(Server server, Command command) {
		Collection<Command> commands;
		commands = super.get (server);
		commands.add (command);
	}

	private void addNewEntry(Server server, Command command) {
		Collection<Command> commands;
		commands = new HashSet<Command> ();
		commands.add (command);
		super.put (server, commands);
	}

}
