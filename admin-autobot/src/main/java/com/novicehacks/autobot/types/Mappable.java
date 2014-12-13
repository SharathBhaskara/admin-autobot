package com.novicehacks.autobot.types;

/**
 * <p>
 * Implementations of Mappable interface can be used in
 * {@link BotUtils.createMap} to create a map from a collection of these
 * entities.
 * </p>
 * <p>
 * Known Implementations
 * <ul>
 * <li>
 * <p>
 * Under {@link Command} interface
 * </p>
 * <ul>
 * <li>{@link ShellCommand}</li>
 * <li>{@link SQLCommand}</li>
 * </ul>
 * </li>
 * 
 * <li>
 * <p>
 * Under {@link Server} Interface
 * </p>
 * <ul>
 * <li>{@link UnixServer}</li>
 * <li>{@link DBServer}</li>
 * </ul>
 * </li>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public interface Mappable {

	public String mapKey();

}
