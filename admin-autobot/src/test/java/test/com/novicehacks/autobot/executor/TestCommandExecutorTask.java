package test.com.novicehacks.autobot.executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.novicehacks.autobot.ThreadManager;
import com.novicehacks.autobot.config.AutobotConfigManager;
import com.novicehacks.autobot.executor.CommandExecutorTask;

public class TestCommandExecutorTask {

	@Before
	public void setUp() throws InterruptedException, ExecutionException, TimeoutException {
		AutobotConfigManager.loadSystemConfig ();
		AutobotConfigManager.loadResourceConfig ();
	}

	@Test
	public void testCommandExecutor() throws InterruptedException {
		CommandExecutorTask task;
		task = new CommandExecutorTask ();
		task.run ();
		ThreadManager.getInstance ().terminateAndWaitForTaskCompletion (5, TimeUnit.MINUTES);
	}
}
