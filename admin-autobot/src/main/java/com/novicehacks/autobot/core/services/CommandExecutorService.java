package com.novicehacks.autobot.core.services;

import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.Server;

public interface CommandExecutorService {

	public void executeCommands();

	public Command[] commandArray();

	public Server server();
}
