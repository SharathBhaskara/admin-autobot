package com.novicehacks.autobot.types;

/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public interface Command extends Comparable<Command> {

	public String command();

	public String commandId();

	public String description();

}
