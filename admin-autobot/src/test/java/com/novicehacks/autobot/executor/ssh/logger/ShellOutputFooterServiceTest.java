package com.novicehacks.autobot.executor.ssh.logger;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.services.OutputFooterService;

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
	@Category ({ UnitTest.class })
	public void testWrappedFooter() {
		OutputFooterService service = new ShellOutputFooterService ();
		service = spy (service);
		final String footerSeperator = "-";
		when (service.footerSeparator ()).thenReturn (footerSeperator);

		String footer = service.footer ();

		assertTrue (footer.startsWith (footerSeperator));
		assertTrue (footer.endsWith (footerSeperator));
	}
}
