package test.com.novicehacks.autobot.ssh;

import ch.ethz.ssh2.Session;

import com.novicehacks.autobot.ssh.CustomizedSSHSession;

public class StubOfCustomizedSSHSession extends CustomizedSSHSession {

	protected StubOfCustomizedSSHSession (Session session) {
		super (session);
	}

}
