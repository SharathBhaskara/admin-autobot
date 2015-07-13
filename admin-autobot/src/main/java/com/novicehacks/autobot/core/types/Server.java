package com.novicehacks.autobot.core.types;

import com.novicehacks.autobot.executor.CommandExecutorService;

/**
 * A Server is a remote machine, where the commands can be exeuted. It is
 * identified by its {@link #ipaddress()}.
 * For authenticating the connection to the server we can use multiple
 * {@link ServerCredential} objects as specified in {@link #credentials()}.
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

	public CommandExecutorService commandExecutorService();

}
