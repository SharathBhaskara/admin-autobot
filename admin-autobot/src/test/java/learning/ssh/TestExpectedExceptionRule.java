package learning.ssh;

import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;

import com.novicehacks.autobot.ssh.DefaultSSHConnection;

// @RunWith (PowerMockRunner.class)
@PowerMockIgnore ("*")
@PrepareForTest (DefaultSSHConnection.class)
public class TestExpectedExceptionRule {

	private Connection			connection;
	private ConnectionInfo		connectionInfo;
	@ClassRule
	public static PowerMockRule	rule		= new PowerMockRule ();
	@Rule
	public ExpectedException	exception	= ExpectedException.none ();

	@Test
	public void testExcepitonWithPowerMockRule() throws Exception {
		Connection connection = mock (Connection.class);
		whenNew (Connection.class).withAnyArguments ().thenReturn (connection);
		DefaultSSHConnection sshConnection = DefaultSSHConnection.getNewInstance ("sdf.org");
		sshConnection.authenticateConnectionWithUsernameAndPassword ("novicehacks", "novicehacks");
		exception.expect (NullPointerException.class);
		exception.expectMessage ("Image is null");
		throw new NullPointerException ("Image is null");
	}
}
