package com.novicehacks.autobot.ssh;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import ch.ethz.ssh2.Session;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.IgnoredTest;
import com.novicehacks.autobot.categories.NewFeature;
import com.novicehacks.autobot.categories.UnitTest;

public class DefaultSSHSessionTest {
	private static final String SecondExecuteCommand = "SecondExecuteCommand";
	private SSHSession sshSession;
	private Session remoteSession;

	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@Before
	public void setUp() throws Exception {
		mockRemoteSession ();
		this.sshSession = new DefaultSSHSession (this.remoteSession);
	}

	private void mockRemoteSession() throws Exception {
		this.remoteSession = mock (Session.class);
		when (this.remoteSession.getStderr ()).thenReturn (SSHTestUtilities.tempInputStream ());
		when (this.remoteSession.getStdin ()).thenReturn (SSHTestUtilities.tempOutputStream ());
		when (this.remoteSession.getStdout ()).thenReturn (SSHTestUtilities.tempInputStream ());
		doThrow (new IOException ("Remote Execution already Started")).when (this.remoteSession)
				.execCommand (SecondExecuteCommand);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void getStdInputStream() {
		// given
		// when
		this.sshSession.stdInputStream ();
		// then
		verify (this.remoteSession).getStdin ();
	}

	@Test
	@Category (UnitTest.class)
	public void getStdInputStreamAfterSessionClosed() {
		// when session closed
		this.sshSession.closeSession ();
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.stdInputStream ();

		verify (this.remoteSession, times (0)).getStdin ();
		fail ("Input Stream cannot be obtained after closing session");
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void getStdOutputStream() {
		// when
		this.sshSession.stdOutputStream ();
		// then
		verify (this.remoteSession).getStdout ();
	}

	@Test
	@Category (UnitTest.class)
	public void getStdOutputStreamAfterSessionClosed() {
		// when session closed
		this.sshSession.closeSession ();
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.stdOutputStream ();

		verify (this.remoteSession, times (0)).getStdout ();
		fail ("Output Stream cannot be obtained after closing session");
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void getStdErrorStream() {
		// given
		// when
		this.sshSession.stdErrorStream ();
		// then
		verify (this.remoteSession).getStderr ();
	}

	@Test
	@Category (UnitTest.class)
	public void getStdErrorStreamAfterSessionClosed() {
		// when session closed
		this.sshSession.closeSession ();
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.stdErrorStream ();

		verify (this.remoteSession, times (0)).getStderr ();
		fail ("Error Stream cannot be obtained after closing session");
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void startShell() throws IOException {
		// when
		this.sshSession.startShell ();
		// then
		verify (this.remoteSession).startShell ();
	}

	@Test
	@Category (UnitTest.class)
	public void startShellMultipleTimes() throws IOException {
		// given
		this.sshSession.startShell ();
		// when
		verify (this.remoteSession, times (1)).startShell ();
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.startShell ();

		verify (this.remoteSession, times (1)).startShell ();
		fail ("Shell can be initiated only once per session");
	}

	@Test
	@Category (UnitTest.class)
	public void startShellAfterExecutingCommand() throws IOException {
		// when command executed on session
		this.sshSession.execCommand ("df -k");
		verify (this.remoteSession).execCommand (Mockito.anyString ());
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.startShell ();

		verify (this.remoteSession, never ()).startShell ();
		fail ("Shell cannot be started after Command Execution in same session");
	}

	@Test
	@Category (UnitTest.class)
	public void startShellAfterSessionClosed() throws IOException {
		// when session closed
		this.sshSession.closeSession ();
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.startShell ();

		verify (this.remoteSession, never ()).startShell ();
		fail ("shell cannot be started after closing session");
	}

	@Test
	@Category ({ UnitTest.class, NewFeature.class, IgnoredTest.class })
	public void startShellAndExecuteCommandParallely() throws IOException {

	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void executeCommand() throws IOException {
		// given
		String command = "abc";
		// when
		this.sshSession.execCommand (command);
		// then
		verify (this.remoteSession, times (1)).execCommand (command);
	}

	@Test
	@Category (UnitTest.class)
	public void executeCommandAfterShellStart() throws IOException {
		// when shell started on session
		this.sshSession.startShell ();
		verify (this.remoteSession, atLeast (1)).startShell ();
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.execCommand ("df -k");

		verify (this.remoteSession, never ()).execCommand (Mockito.anyString ());
		fail ("Command cannot be executed after starting shell on same session");
	}

	@Test
	@Category (UnitTest.class)
	public void executeCommandMultipleTimes() throws IOException {
		// given
		String command = "abc";
		// when command executed
		this.sshSession.execCommand (command);
		verify (this.remoteSession, atLeast (1)).execCommand (command);
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.execCommand (command);

		fail ("Execute command cannot be run multiple times on same session");
	}

	@Test
	@Category (UnitTest.class)
	public void executeCommandAfterSessionClosed() throws IOException {
		// given
		String command = "abc";
		// when session closed
		this.sshSession.closeSession ();
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.execCommand (command);

		fail ("Execute command cannot run after closing session");
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void requestTerminalBeforeShellStart() throws IOException {
		// given
		verify (this.remoteSession, never ()).startShell ();
		// when shell not started
		this.sshSession.getTerminal ();
		// then
		verify (this.remoteSession, times (1)).requestDumbPTY ();
	}

	@Test
	@Category (UnitTest.class)
	public void requestTerminalAfterShellStart() throws IOException {
		// when shell started
		this.sshSession.startShell ();
		verify (this.remoteSession, atLeast (1)).startShell ();
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.getTerminal ();

		verify (this.remoteSession, never ()).requestDumbPTY ();
		fail ("Terminal cannot be obtained after starting shell on a session");
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void requestTerminalBeforeExecuteCommand() throws IOException {
		// given
		verify (this.remoteSession, never ()).execCommand (Mockito.anyString ());
		// when no commands executed
		this.sshSession.getTerminal ();
		// then
		verify (this.remoteSession, times (1)).requestDumbPTY ();
	}

	@Test
	@Category (UnitTest.class)
	public void requestTerminalAfterExecuteCommand() throws IOException {
		// when command executed
		this.sshSession.execCommand ("df -k");
		verify (this.remoteSession, atLeast (1)).execCommand ("df -k");
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.getTerminal ();

		verify (this.remoteSession, never ()).requestDumbPTY ();
		fail ("Terminal cannot be requested after command execution on a session");
	}

	@Test
	@Category (UnitTest.class)
	public void requestMultipleTerminalBeforeShellStartOrCommandExecute() throws IOException {
		// given
		verify (this.remoteSession, never ()).execCommand (Mockito.anyString ());
		verify (this.remoteSession, never ()).startShell ();
		// when terminal already requested
		this.sshSession.getTerminal ();
		verify (this.remoteSession, atLeast (1)).requestDumbPTY ();
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.getTerminal ();

		fail ("Multiple Terminals cannot be requested on a session");
	}

	@Test
	@Category (UnitTest.class)
	public void requestTerminalAfterSessionClosed() throws IOException {
		// when session closed
		this.sshSession.closeSession ();
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.getTerminal ();

		verify (this.remoteSession, never ()).requestDumbPTY ();
		fail ("Terminal cannot be requested after closing the session");
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void sessionClose() throws IOException {
		// when
		this.sshSession.closeSession ();
		// then
		verify (this.remoteSession, times (1)).close ();
	}

	@Test
	@Category (UnitTest.class)
	public void sessionCloseMultipleTimes() throws IOException {
		// when session closed
		this.sshSession.closeSession ();
		// then
		this.exception.expect (IllegalStateException.class);
		this.sshSession.closeSession ();

		verify (this.remoteSession, times (1)).close ();
		fail ("Session cannot be closed multiple times");
	}

}
