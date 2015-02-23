package test.learning.ssh;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class ExecuteCommand {
	private static final String server = "sdf.org";
	private static final String username = "novicehacks";
	private static final String password = "novicehacks";
	Connection connection;

	@Before
	public void setup() throws IOException {
		this.connection = new Connection (server);
		this.connection.connect ();
		this.connection.authenticateWithPassword (username, password);
	}

	@Test
	public void executeCommandOnSession() throws IOException {
		Session session = this.connection.openSession ();
		session.execCommand ("df -k");
		session.close ();
	}

	@Test
	public void executeMultipleCommandOnSession() throws IOException {
		Session session = this.connection.openSession ();
		session.execCommand ("df -k");
		session.execCommand ("ls -lrt");
		session.close ();
	}
}
