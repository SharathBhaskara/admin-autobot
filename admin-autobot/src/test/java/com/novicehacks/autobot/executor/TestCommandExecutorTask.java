package com.novicehacks.autobot.executor;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.EnvironmentalTest;
import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.IgnoredTest;
import com.novicehacks.autobot.config.ConfigurationManager;

public class TestCommandExecutorTask {

	@Test
	@Category ({ EnvironmentalTest.class, FunctionalTest.class, IgnoredTest.class })
	public void testCommandExecutor() throws InterruptedException {
		CommandExecutorTask task;
		task = new CommandExecutorTask ();
		try {
			ConfigurationManager.getSharedInstance ().loadSystemConfig ();
			ConfigurationManager.getSharedInstance ().loadResourceConfig ();
			task.run ();
		} catch (Exception ex) {
			ex.printStackTrace ();
			System.out.println (ex.getSuppressed ());
			fail ("Exception raised while command execution" + ex);
		}
	}
}
