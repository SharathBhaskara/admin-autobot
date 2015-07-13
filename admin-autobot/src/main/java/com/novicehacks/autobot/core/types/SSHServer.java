package com.novicehacks.autobot.core.types;

import com.novicehacks.autobot.executor.CommandExecutorService;

/**
 * SSHServer has capability of executing commands on SSH terminal.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks
 * @see ShellCommand
 * @see ShellExecutor
 */
public class SSHServer extends DefaultServer {

	public SSHServer (String line) {
		super (line);
	}

	@Override
	public int compareTo(Server object) {
		return id ().compareTo (object.id ());
	}

	@Override
	public String mapKey() {
		return id ();
	}

	@Override
	public CommandExecutorService commandExecutorService() {
		return null;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof SSHServer) {
			SSHServer temp = (SSHServer) object;
			if (temp.parserdServerLine ().equals (this.parserdServerLine ())) {
				return true;
			}
		}
		return false;
	}

}
