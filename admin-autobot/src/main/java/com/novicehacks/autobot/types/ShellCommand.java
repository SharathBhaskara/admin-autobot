package com.novicehacks.autobot.types;

/**
 * The Command object for executing in a Unix Server.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @See SSHServer
 */
public class ShellCommand extends DefaultCommand {

	public ShellCommand (String commandString) {
		super (commandString);
	}

	@Override
	public int compareTo(Command object) {
		return id ().compareTo (object.id ());
	}

	@Override
	public String mapKey() {
		return id ();
	}
}
