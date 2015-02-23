package test.learning.ssh;

import java.io.IOException;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

public class SimpleSessionTests {
	private static final String server = "sdf.org";
	private static final String username = "novicehacks";
	private static final String password = "novicehacks";
	private static final String RemoteExecutionStartedMsg = "A remote execution has already started";
	private static final String CannotRequestTerminalMsg = "Cannot request PTY at this stage anymore, a remote execution has already started.";
	private static final String TerminalAlreadyRequested = "A PTY was already requested";
	private static final String SessionClosedMsg = "This session is closed";
	Connection connection;
	Session session;

	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@Before
	public void setup() throws IOException {
		this.connection = new Connection (server);
		this.connection.connect ();
		this.connection.authenticateWithPassword (username, password);
	}

	@After
	public void tearDown() {
		if (this.session != null)
			this.session.close ();
		if (this.connection != null)
			this.connection.close ();
	}

	@Test
	public void executeCommandOnSession() throws IOException {
		this.session = this.connection.openSession ();
		this.session.execCommand ("df -k");
		this.session.close ();
	}

	@Test
	public void executeMultipleCommandOnSession() throws IOException {
		this.session = this.connection.openSession ();
		this.session.execCommand ("df -k");
		this.exception.expect (IOException.class);
		this.exception.expectMessage (CoreMatchers.containsString (RemoteExecutionStartedMsg));
		this.session.execCommand ("ls -lrt");
		this.session.close ();
	}

	@Test
	public void executeCommandAfterSessionClosed() throws IOException {
		// given
		this.session = this.connection.openSession ();
		// when
		this.session.close ();
		// then
		this.exception.expect (IOException.class);
		this.exception.expectMessage (CoreMatchers.containsString (SessionClosedMsg));
		this.session.execCommand ("df -k");
	}

	@Test
	public void executeCommandAfterStartingAShell() throws IOException {
		this.session = this.connection.openSession ();
		this.session.startShell ();
		this.exception.expect (IOException.class);
		this.exception.expectMessage (CoreMatchers.containsString (RemoteExecutionStartedMsg));
		this.session.execCommand ("df -k");
		this.session.close ();
	}

	@Test
	public void startShellAfterCommandExecute() throws IOException {
		// given
		this.session = this.connection.openSession ();
		// when
		this.session.execCommand ("df -k");
		// then
		this.exception.expect (IOException.class);
		this.exception.expectMessage (CoreMatchers.containsString (RemoteExecutionStartedMsg));
		this.session.startShell ();
	}

	@Test
	public void startShellAfterSessionClosed() throws IOException {
		// given
		this.session = this.connection.openSession ();
		// when
		this.session.close ();
		// then
		this.exception.expect (IOException.class);
		this.exception.expectMessage (CoreMatchers.containsString (SessionClosedMsg));
		this.session.startShell ();
	}

	@Test
	public void startShellMultipleTimes() throws IOException {
		// given
		this.session = this.connection.openSession ();
		// when
		this.session.startShell ();
		// then
		this.exception.expect (IOException.class);
		this.exception.expectMessage (CoreMatchers.containsString (RemoteExecutionStartedMsg));
		this.session.startShell ();
	}

	@Test
	public void requestTerminalBeforeStartingShell() throws IOException {
		this.session = this.connection.openSession ();
		this.session.requestDumbPTY ();
		this.session.startShell ();
		this.session.close ();
	}

	@Test
	public void requestTerminalAfterStartingShell() throws IOException {
		this.session = this.connection.openSession ();
		this.session.startShell ();
		this.exception.expect (IOException.class);
		this.exception.expectMessage (CoreMatchers.containsString (CannotRequestTerminalMsg));
		this.session.requestDumbPTY ();
		this.session.close ();
	}

	@Test
	public void requestTerminalBeforeAndAfterStartingShell() throws IOException {
		this.session = this.connection.openSession ();
		this.session.requestDumbPTY ();
		this.session.startShell ();
		this.exception.expect (IOException.class);
		this.exception.expectMessage (CoreMatchers.containsString (TerminalAlreadyRequested));
		this.session.requestDumbPTY ();
		this.session.close ();
	}

	@Test
	public void requestTerminalMultipleTimesBeforeStartingShell() throws IOException {
		this.session = this.connection.openSession ();
		this.session.requestDumbPTY ();
		this.exception.expect (IOException.class);
		this.exception.expectMessage (CoreMatchers.containsString (TerminalAlreadyRequested));
		this.session.requestDumbPTY ();
		this.session.close ();
	}

	@Test
	public void requestTerminalAfterSessionClosed() throws IOException {
		// given
		this.session = this.connection.openSession ();
		// when
		this.session.close ();
		// then
		this.exception.expect (IOException.class);
		this.exception.expectMessage (CoreMatchers.containsString (SessionClosedMsg));
		this.session.requestDumbPTY ();
	}

}
