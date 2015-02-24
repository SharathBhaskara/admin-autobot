package com.novicehacks.autobot.ssh;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import ch.ethz.ssh2.Session;

import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.ssh.CustomizedSSHSession;
import com.novicehacks.autobot.ssh.SSHSession;

@PowerMockIgnore ("*")
@PrepareForTest ({ StubOfCustomizedSSHSession.class, SSHSession.class, CustomizedSSHSession.class,
		Session.class })
public class TestCustomizedSSHSession {
	private static final String SecondExecuteCommand = "SecondExecuteCommand";
	private SSHSession sshSession;
	private Session remoteSession;

	@Rule
	public PowerMockRule rule = new PowerMockRule ();

	@Before
	public void setUp() throws Exception {
		mockRemoteSession ();
		this.sshSession = new StubOfCustomizedSSHSession (this.remoteSession);
	}

	private void mockRemoteSession() throws Exception {
		this.remoteSession = mock (Session.class);
		when (this.remoteSession.getStderr ()).thenReturn (SSHUtilities.tempInputStream ());
		when (this.remoteSession.getStdin ()).thenReturn (SSHUtilities.tempOutputStream ());
		when (this.remoteSession.getStdout ()).thenReturn (SSHUtilities.tempInputStream ());
		doThrow (new IOException ("Remote Execution already Started")).when (this.remoteSession)
				.execCommand (SecondExecuteCommand);
	}

	@Test
	@Category (UnitTest.class)
	public void getStdInputStream() {
		// given
		// when
		this.sshSession.stdInputStream ();
		// then
		verify (this.remoteSession).getStdin ();
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void getStdInputStreamAfterSessionClosed() {
		// given
		this.sshSession.closeSession ();
		// when
		this.sshSession.stdInputStream ();
		// then
		verify (this.remoteSession, times (0)).getStdin ();
		fail ("Exception should have raised before, or failed in verify");
	}

	@Test
	@Category (UnitTest.class)
	public void getStdOutputStream() {
		// given
		// when
		this.sshSession.stdOutputStream ();
		// then
		verify (this.remoteSession).getStdout ();
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void getStdOutputStreamAfterSessionClosed() {
		// given
		this.sshSession.closeSession ();
		// when
		this.sshSession.stdOutputStream ();
		// then
		verify (this.remoteSession, times (0)).getStdout ();
		fail ("Exception should have raised before, or failed in verify");
	}

	@Test
	@Category (UnitTest.class)
	public void getStdErrorStream() {
		// given
		// when
		this.sshSession.stdErrorStream ();
		// then
		verify (this.remoteSession).getStderr ();
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void getStdErrorStreamAfterSessionClosed() {
		// given
		this.sshSession.closeSession ();
		// when
		this.sshSession.stdErrorStream ();
		// then
		verify (this.remoteSession, times (0)).getStderr ();
		fail ("Exception should have raised before, or failed in verify");
	}

	@Test
	@Category (UnitTest.class)
	public void startShell() throws IOException {
		// given
		// when
		this.sshSession.startShell ();
		// then
		verify (this.remoteSession).startShell ();
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void startShellMultipleTimes() throws IOException {
		// given
		this.sshSession.startShell ();
		verify (this.remoteSession).startShell ();
		// when
		this.sshSession.startShell ();
		// then
		fail ("Should not start a new Shell As another Shell is already initiated on this session");
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void startShellAfterExecutingCommand() throws IOException {
		// given
		this.sshSession.execCommand ("df -k");
		verify (this.remoteSession).execCommand (Mockito.anyString ());
		// when
		this.sshSession.startShell ();
		// then
		fail ("Should not start a new Shell As Command already executed on session");
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void startShellAfterSessionClosed() throws IOException {
		// given
		this.sshSession.closeSession ();
		// when
		this.sshSession.startShell ();
		// then
		verify (this.remoteSession, times (0)).startShell ();
		fail ("Exception should have raised before, or failed in verify");
	}

	@Test
	@Category (UnitTest.class)
	public void requestTerminalBeforeShellStart() throws IOException {
		// when
		verify (this.remoteSession, times (0)).startShell ();
		this.sshSession.getTerminal ();
		this.sshSession.startShell ();
		// then
		verify (this.remoteSession).requestDumbPTY ();
		verify (this.remoteSession).startShell ();
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void requestTerminalAfterShellStart() throws IOException {
		// when
		this.sshSession.startShell ();
		verify (this.remoteSession).startShell ();
		// then
		this.sshSession.getTerminal ();
		verify (this.remoteSession).requestDumbPTY ();
		fail ("Get Terminal should have failed with exception");
	}

	@Test
	@Category (UnitTest.class)
	public void requestTerminalBeforeExecuteCommand() throws IOException {
		// when
		verify (this.remoteSession, times (0)).execCommand (Mockito.anyString ());
		this.sshSession.getTerminal ();
		this.sshSession.execCommand ("df -k");
		// then
		verify (this.remoteSession).requestDumbPTY ();
		verify (this.remoteSession).execCommand ("df -k");
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void requestTerminalAfterExecuteCommand() throws IOException {
		// when
		this.sshSession.execCommand ("df -k");
		verify (this.remoteSession).execCommand ("df -k");
		// then
		this.sshSession.getTerminal ();
		verify (this.remoteSession).requestDumbPTY ();
		fail ("Get Terminal should have failed with exception");
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void requestMultipleTerminalBeforeShellStartOrCommandExecute() throws IOException {
		// when
		verify (this.remoteSession, times (0)).execCommand (Mockito.anyString ());
		verify (this.remoteSession, times (0)).startShell ();
		this.sshSession.getTerminal ();
		// then
		verify (this.remoteSession).requestDumbPTY ();
		this.sshSession.getTerminal ();
		fail ("Get Terminal should have failed with exception");
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void requestTerminalAfterSessionClosed() throws IOException {
		// given
		this.sshSession.closeSession ();
		// when
		this.sshSession.getTerminal ();
		// then
		verify (this.remoteSession, times (0)).requestDumbPTY ();
		fail ("Exception should have raised before, or failed in verify");
	}

	@Test
	@Category (UnitTest.class)
	public void executeCommand() throws IOException {
		// given
		String command = "abc";
		// when
		this.sshSession.execCommand (command);
		// then
		verify (this.remoteSession).execCommand (command);
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void executeCommandAfterShellStart() throws IOException {
		// given
		this.sshSession.startShell ();
		verify (this.remoteSession).startShell ();
		// when
		this.sshSession.execCommand ("df -k");
		// then
		fail ("Command should not be executed, as Shell is already started on the session");
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void executeCommandMultipleTimes() throws IOException {
		// given
		String command = "abc";
		// when
		this.sshSession.execCommand (command);
		verify (this.remoteSession).execCommand (command);
		this.sshSession.execCommand (command);
		// then
		fail ("Multiple commands cannot be executed on same session");
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void executeCommandAfterSessionClosed() throws IOException {
		// given
		String command = "abc";
		this.sshSession.closeSession ();
		// when
		this.sshSession.execCommand (command);
		// then
		fail ("Exception should be thrown prior");
	}

	@Test
	@Category (UnitTest.class)
	public void sessionClose() throws IOException {
		// given
		// when
		this.sshSession.closeSession ();
		// then
		verify (this.remoteSession).close ();
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void sessionCloseMultipleTimes() throws IOException {
		// given
		this.sshSession.closeSession ();
		// when
		this.sshSession.closeSession ();
		// then
		fail ("Exception should be thrown prior");
	}

}
