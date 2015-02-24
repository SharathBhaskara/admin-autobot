package com.novicehacks.autobot.ssh;

import com.novicehacks.autobot.ssh.ParallelCommandExecutorTask;
import com.novicehacks.autobot.ssh.SSHConnection;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

public final class StubOfParallelCommandExecutorTask extends ParallelCommandExecutorTask {

	StubOfParallelCommandExecutorTask (SSHConnection connection, Server server, Command command) {
		super (connection, server, command);
	}
}
