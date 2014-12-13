package com.novicehacks.autobot.types;

/**
 * The Command object for executing in a Unix Server.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @See UnixServer
 */
public class ShellCommand implements Command {

	private String command;
	private String id;
	private String description;
	private String commandString;

	public ShellCommand(String commandString) {
		this.commandString = commandString;
	}

	@Override
	public String command() {
		return this.command;
	}

	@Override
	public String id() {
		return this.id;
	}

	@Override
	public String description() {
		return this.description;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCommandString() {
		return commandString;
	}

	public void setCommandString(String commandString) {
		this.commandString = commandString;
	}

	@Override
	public int compareTo(Command object) {
		return this.id().compareTo(object.id());
	}

	@Override
	public String toString() {
		return commandString;
	}

	@Override
	public String mapKey() {
		return this.id;
	}
}
