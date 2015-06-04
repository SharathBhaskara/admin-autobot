package com.novicehacks.autobot.services;

import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;
/**
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public interface CommandExecutorService extends Service {

	public void executeCommands();

	public Command[] commandArray();

	public Server server();
}
