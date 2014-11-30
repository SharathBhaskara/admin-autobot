package com.novicehacks.autobot.types;

/**
 * A Command is a single unit that will be executed on a server.
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

	public String command();

	public String id();

	public String description();

}
