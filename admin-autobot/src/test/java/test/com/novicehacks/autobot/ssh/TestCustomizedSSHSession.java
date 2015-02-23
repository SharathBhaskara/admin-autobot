package test.com.novicehacks.autobot.ssh;

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
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import test.com.novicehacks.autobot.categories.UnitTest;
import ch.ethz.ssh2.Session;

import com.novicehacks.autobot.ssh.CustomizedSSHSession;

@PowerMockIgnore ("*")
@PrepareForTest ({ StubOfCustomizedSSHSession.class, CustomizedSSHSession.class, Session.class })
public class TestCustomizedSSHSession {
	private static final String SecondExecuteCommand = "SecondExecuteCommand";
	private StubOfCustomizedSSHSession sshSession;
	private Session remoteSession;

	@Rule
	public PowerMockRule rule = new PowerMockRule ();

	@Before
	public void setUp() throws IOException {
		mockRemoteSession ();
		this.sshSession = new StubOfCustomizedSSHSession (this.remoteSession);
	}

	private void mockRemoteSession() throws IOException {
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

	@Test
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
	public void startShellMultipleTimes() {

	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void startShellAfterExecutingCommand() {

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
		// given
		// when
		this.sshSession.getTerminal ();
		// then
		verify (this.remoteSession).requestDumbPTY ();
	}

	@Test (expected = IllegalAccessException.class)
	@Category (UnitTest.class)
	public void requestTerminalAfterShellStart() throws IOException {

	}

	@Test
	@Category (UnitTest.class)
	public void requestTerminalBeforeExecuteCommand() throws IOException {
		fail ("Unimplemented");
	}

	@Test (expected = IllegalAccessException.class)
	@Category (UnitTest.class)
	public void requestTerminalAfterExecuteCommand() throws IOException {

	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void requestMultipleTerminalBeforeShellStart() throws IOException {

	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void requestMultipleTerminalBeforeExecuteCommand() throws IOException {

	}

	@Test
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
		String command = "abc";
		// when
		this.sshSession.execCommand (command);
		// then
		verify (this.remoteSession).execCommand (command);
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void executeCommandMultipleTimes() throws IOException {
		// given
		String command = "abc";
		// when
		this.sshSession.execCommand (command);
		this.sshSession.execCommand (command);
		// then
		verify (this.remoteSession).execCommand (command);
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
