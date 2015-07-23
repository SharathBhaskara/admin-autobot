package com.novicehacks.autobot.executor;

import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.executor.ssh.SSHCommandExecutorService;
import com.novicehacks.autobot.services.Service;

/**
 * A service interface that defines the contract of CommandExecutors.
 * 
 * Currently we have only one Implementation of this
 * {@link SSHCommandExecutorService}
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public interface CommandExecutorService extends Service {

	public void executeCommands();

	public Command[] commandArray();

	public Server server();
}
