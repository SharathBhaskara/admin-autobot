package test.com.novicehacks.autobot;

import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
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

import test.com.novicehacks.autobot.categories.IntegrationTest;

import com.novicehacks.autobot.ThreadManager;
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
		commandProcessor = new SSHServerCommandProcessor (server, unixCommand1, unixCommand2);
		ThreadManager.getInstance ().InitiateThreadPool (true);
	}

	private void setupStubOfServer() {
		when (server.initCommands ())
				.thenReturn (new String[] { "\b", "\r", "\r", "\r", "whoami" });
		when (server.id ()).thenReturn ("S001");
		when (server.ipaddress ()).thenReturn ("sdf.org");
		when (server.name ()).thenReturn ("test server");
		when (server.credentials ()).thenReturn (getCredentials ());
	}

	private void setupStubOfCommands() {
		when (unixCommand1.command ()).thenReturn ("df -k");
		when (unixCommand1.id ()).thenReturn ("0001");
		when (unixCommand1.description ()).thenReturn ("Disk Utilization");
		when (unixCommand2.command ()).thenReturn ("ls -lrt");
		when (unixCommand2.id ()).thenReturn ("0002");
		when (unixCommand2.description ()).thenReturn ("Show Files");
	}

	private ServerCredential[] getCredentials() {
		ServerCredential credential = new ServerCredential ();
		credential.setLoginid ("novicehacks");
		credential.setPassword ("novicehacks");
		return new ServerCredential[] { credential };
	}

	private void executeAndWait(Thread task) throws InterruptedException {
		task.setUncaughtExceptionHandler (ThreadManager.genericExceptionHandler ());
		task.start ();
		task.join ();
		ExecutorService service = ThreadManager.getInstance ().shutDownThreadPool ();
		service.awaitTermination (5, TimeUnit.MINUTES);
	}

	@Test
	@Category (IntegrationTest.class)
	public void testSequentialExection() throws InterruptedException {
		Thread task = new Thread (commandProcessor);
		executeAndWait (task);
	}

	@Test
	@Category (IntegrationTest.class)
	public void testParallelExecution() throws InterruptedException {
		when (server.initCommands ()).thenReturn (new String[] { });
		when (server.ipaddress ()).thenReturn ("192.168.40.133");
		when (server.id ()).thenReturn ("S002");
		Thread task = new Thread (commandProcessor);
		executeAndWait (task);
	}
}
