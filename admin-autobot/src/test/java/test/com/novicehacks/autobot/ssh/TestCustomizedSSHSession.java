package test.com.novicehacks.autobot.ssh;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
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
	public void testStandardInputStream() {
		// given
		// when
		this.sshSession.stdInputStream ();
		// then
		verify (this.remoteSession).getStdin ();
	}

	@Test
	@Category (UnitTest.class)
	public void testStandardOutputStream() {
		// given
		// when
		this.sshSession.stdOutputStream ();
		// then
		verify (this.remoteSession).getStdout ();
	}

	@Test
	@Category (UnitTest.class)
	public void testStandardErrorStream() {
		// given
		// when
		this.sshSession.stdErrorStream ();
		// then
		verify (this.remoteSession).getStderr ();
	}

	@Test
	@Category (UnitTest.class)
	public void testStartShell() throws IOException {
		// given
		// when
		this.sshSession.startShell ();
		// then
		verify (this.remoteSession).startShell ();
	}

	@Test
	@Category (UnitTest.class)
	public void testRequestTerminal() throws IOException {
		// given
		// when
		this.sshSession.getTerminal ();
		// then
		verify (this.remoteSession).requestDumbPTY ();
	}

	@Test
	@Category (UnitTest.class)
	public void testExecuteCommand() throws IOException {
		// given
		String command = "abc";
		// when
		this.sshSession.execCommand (command);
		// then
		verify (this.remoteSession).execCommand (command);
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void testExecuteCommandMultipleTimes() throws IOException {
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
	public void testExecuteCommandAfterSessionClosed() throws IOException {
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
	public void testSessionClose() throws IOException {
		// given
		// when
		this.sshSession.closeSession ();
		// then
		verify (this.remoteSession).close ();
	}

	@Test (expected = IllegalStateException.class)
	@Category (UnitTest.class)
	public void testSessionCloseMultipleTimes() throws IOException {
		// given
		this.sshSession.closeSession ();
		// when
		this.sshSession.closeSession ();
		// then
		fail ("Exception should be thrown prior");
	}

}
