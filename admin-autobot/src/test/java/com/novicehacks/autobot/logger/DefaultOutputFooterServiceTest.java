package com.novicehacks.autobot.logger;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DefaultOutputFooterServiceTest {
	private final String newLine = System.lineSeparator ();
	private final String lineSeperator = ":-:";

	@Test
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
