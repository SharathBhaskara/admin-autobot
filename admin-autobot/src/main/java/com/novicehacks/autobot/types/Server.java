package com.novicehacks.autobot.types;

/**
 * A server object is used to locate a machine in the network, for
 * authentication it uses an array of credentials.
 * 
 * <p>
 * As of this version only UnixServer is using these objects
 * </p>
 * <ul>
 * <li>{@link UnixServer}</li>
 * <li>{@link DBServer}</li>
 * </ul>
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public interface Server extends Comparable<Server>, Mappable {

	public ServerCredential[] credentials();
	
	public String[] initCommands();

	public String ipaddress();

	public String name();

	public String id();

}
