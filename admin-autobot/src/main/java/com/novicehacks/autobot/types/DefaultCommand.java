package com.novicehacks.autobot.types;

/**
 * Default Command implements the generic behavior for the {@link Command} API,
 * while leaving the implementation of {@link Mappable} and {@link Comparable}
 * interfaces to its children for customizing their own
 * logic.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 * @see Command
 */
public abstract class DefaultCommand implements Command {

	private String commandTxt;
	private String id;
	private String description;
	private String parsedCommandLine;

	public DefaultCommand (String commandString) {
		this.parsedCommandLine = commandString;
	}

	@Override
	public String commandTxt() {
		return this.commandTxt;
	}

	@Override
	public String id() {
		return this.id;
	}

	@Override
	public String description() {
		return this.description;
	}

	public void setCommandTxt(String command) {
		this.commandTxt = command;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParsedCommandLine() {
		return this.parsedCommandLine;
	}

}
