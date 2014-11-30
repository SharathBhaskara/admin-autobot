package com.novicehacks.autobot.types;

/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 *
 */
public interface Server extends Comparable<Server>, Mappable {

	public ServerCredential[] credentials();

	public String ipaddress();

	public String name();

	public String id();

}
