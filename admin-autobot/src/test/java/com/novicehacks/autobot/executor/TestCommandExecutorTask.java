package com.novicehacks.autobot.executor;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import com.novicehacks.autobot.categories.EnvironmentalTest;
import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.IgnoredTest;
import com.novicehacks.autobot.config.ConfigurationManager;

// TODO interaction testing to be tested.
public class TestCommandExecutorTask {
	@Rule
	public ExpectedException excepiton = ExpectedException.none ();

	@Before
	public void setup() {
		
	}

	@Test @Ignore
	@Category ({ EnvironmentalTest.class, FunctionalTest.class, IgnoredTest.class })
	public void testCommandExecutor() throws InterruptedException {
		CommandExecutorTask task;
		task = new CommandExecutorTask ();
		try {
			// ConfigurationManager.getSharedInstance ().loadSystemConfig ();
			ConfigurationManager.getSharedInstance ().loadResourceConfig ();
			task.run ();
		} catch(NullPointerException npe){
			//TODO this test needs to be ignored
			fail("NPE ignored");
		}catch (Exception ex) {
			ex.printStackTrace ();
			System.out.println (ex.getSuppressed ());
			fail ("Exception raised while command execution" + ex);
		}
	}
	
	@Test
	public void simpleTest(){
		assertTrue("Simple test to check the staus",true);
	}
}
