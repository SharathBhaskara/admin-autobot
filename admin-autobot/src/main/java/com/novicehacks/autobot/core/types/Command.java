package com.novicehacks.autobot.core.types;

/**
 * A Command is a unit of work that is needed to be executed on a server.
 * 
 * <p>
 * All Known implementations of this type are
 * <ul>
 * <li>{@link ShellCommand}</li>
 * <li>{@link SQLCommand}</li>
 * </ul>
 * </p>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see Server
 *
 */
public interface Command extends Comparable<Command>, Mappable {

	public String commandTxt();

	public String id();

	public String description();

}
