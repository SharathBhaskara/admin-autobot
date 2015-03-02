package com.novicehacks.autobot.ssh.logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.logger.DefaultOutputFooterService;
import com.novicehacks.autobot.logger.OutputFooterService;

public class ShellOutputFooterServiceTest {

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testEquals() {
		// given
		OutputFooterService service1 = new ShellOutputFooterService ();
		OutputFooterService service2 = new ShellOutputFooterService ();
		// when
		boolean status = service1.equals (service2);
		// then
		assertTrue ("Footers should have been equal", status);
	}

	@Test
	@Category (UnitTest.class)
	public void testEqualsWithDifferentInstances() {
		// given
		OutputFooterService service1 = new ShellOutputFooterService ();
		OutputFooterService service2 = new DefaultOutputFooterService ();
		// when
		boolean status = service1.equals (service2);
		// then
		assertFalse ("Footers should not have been equal", status);
	}
}
