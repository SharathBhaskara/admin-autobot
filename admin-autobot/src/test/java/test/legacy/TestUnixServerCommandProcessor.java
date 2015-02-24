package test.legacy;

import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.novicehacks.autobot.ThreadManager;
import com.novicehacks.autobot.categories.EnvironmentDependent;
import com.novicehacks.autobot.config.AutobotConfigManager;
import com.novicehacks.autobot.ssh.SSHServerCommandProcessor;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;
import com.novicehacks.autobot.types.ServerCredential;

public class TestUnixServerCommandProcessor {
	@Mock
	Server								server;
	@Mock
	Command								unixCommand1;
	@Mock
	Command								unixCommand2;

	private SSHServerCommandProcessor	commandProcessor;
	Logger								logger	= LogManager
														.getLogger (TestUnixServerCommandProcessor.class);

	@BeforeClass
	public static void loadConfig() throws InterruptedException, ExecutionException,
			TimeoutException {
		AutobotConfigManager.loadResourceConfig ();
	}

	@Before
	public void setUp() throws InterruptedException, ExecutionException, TimeoutException {
		MockitoAnnotations.initMocks (this);
		setupStubOfServer ();
		setupStubOfCommands ();
		this.commandProcessor = new SSHServerCommandProcessor (this.server, this.unixCommand1,
				this.unixCommand2);
		ThreadManager.getInstance ().createThreadPool (true);
	}

	private void setupStubOfServer() {
		when (this.server.initCommands ()).thenReturn (
				new String[] { "\b", "\r", "\r", "\r", "whoami" });
		when (this.server.id ()).thenReturn ("S001");
		when (this.server.ipaddress ()).thenReturn ("sdf.org");
		when (this.server.name ()).thenReturn ("test server");
		when (this.server.credentials ()).thenReturn (getCredentials ());
	}

	private void setupStubOfCommands() {
		when (this.unixCommand1.command ()).thenReturn ("df -k");
		when (this.unixCommand1.id ()).thenReturn ("0001");
		when (this.unixCommand1.description ()).thenReturn ("Disk Utilization");
		when (this.unixCommand2.command ()).thenReturn ("ls -lrt");
		when (this.unixCommand2.id ()).thenReturn ("0002");
		when (this.unixCommand2.description ()).thenReturn ("Show Files");
	}

	private ServerCredential[] getCredentials() {
		ServerCredential credential = new ServerCredential ();
		credential.setLoginid ("novicehacks");
		credential.setPassword ("novicehacks");
		return new ServerCredential[] { credential };
	}

	private void executeAndWait(Thread task) throws InterruptedException {
		task.start ();
		task.join ();
		ThreadManager.getInstance ().terminateAndWaitForTaskCompletion (5, TimeUnit.MINUTES);
	}

	@Test
	@Category (EnvironmentDependent.class)
	public void testSequentialExection() throws InterruptedException {
		Thread task = new Thread (this.commandProcessor);
		executeAndWait (task);
	}

	@Test
	@Category (EnvironmentDependent.class)
	public void testParallelExecution() throws InterruptedException {
		when (this.server.initCommands ()).thenReturn (new String[] { });
		when (this.server.ipaddress ()).thenReturn ("192.168.40.133");
		when (this.server.id ()).thenReturn ("S002");
		Thread task = new Thread (this.commandProcessor);
		executeAndWait (task);
	}
}
