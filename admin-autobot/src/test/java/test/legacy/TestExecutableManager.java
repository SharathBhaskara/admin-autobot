package test.legacy;

import org.junit.Test;

import com.novicehacks.autobot.ExecutableManager;

public class TestExecutableManager {

	@Test
	public void executeCommands() throws InterruptedException {
		ExecutableManager manager = ExecutableManager.getInstance();
		Thread t = new Thread(manager);
		t.start();
		t.join();
	}
}
