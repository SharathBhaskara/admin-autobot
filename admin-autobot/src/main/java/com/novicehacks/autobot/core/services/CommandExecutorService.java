package com.novicehacks.autobot.core.services;

import java.util.concurrent.Future;

import com.novicehacks.autobot.core.annotations.Incomplete;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

public interface CommandExecutorService {

	@Incomplete
	public default void executeCommands() {

	};

	public Future<?> executeCommandsAsynchonously();

	public Command[] commandArray();

	public Server server();
}
