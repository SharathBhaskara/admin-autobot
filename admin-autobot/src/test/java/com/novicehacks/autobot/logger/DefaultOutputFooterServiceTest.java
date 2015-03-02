package com.novicehacks.autobot.logger;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.UnitTest;

public class DefaultOutputFooterServiceTest {
	private final String newLine = System.lineSeparator ();
	private final String lineSeperator = ":-:";

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testFooter() {
		// given
		StringBuilder buffer = new StringBuilder ();
		buffer.append ("**************************************************")
				.append (this.lineSeperator)
				.append ("**************************************************");
		String expected = buffer.toString ();
		OutputFooterService footerService = new DefaultOutputFooterService ();
		// when
		String actual = footerService.footer ();
		actual = actual.replaceAll (this.newLine, this.lineSeperator);
		// then
		assertEquals (expected, actual);
	}

}
