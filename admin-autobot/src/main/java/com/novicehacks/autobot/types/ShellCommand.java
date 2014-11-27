package com.novicehacks.autobot.types;

/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public class ShellCommand implements Command {

	private String command;
	private String id;
	private String description;
	private String commandString;

	public ShellCommand(String commandString) {
		this.commandString = commandString;
	}

	public String command() {
		return this.command;
	}

	public String commandId() {
		return this.id;
	}

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
		return this.commandId().compareTo(object.commandId());
	}

	
	@Override
	public String toString() {
		return commandString;
	}
}
