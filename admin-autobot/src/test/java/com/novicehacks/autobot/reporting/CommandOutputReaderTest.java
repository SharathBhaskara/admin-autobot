package com.novicehacks.autobot.reporting;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.core.BotUtils;
import com.novicehacks.autobot.reporting.exception.InvalidCommandOutputFilePath;
import com.novicehacks.autobot.services.OutputFooterService;
import com.novicehacks.autobot.services.OutputHeaderService;

/**
 * Test cases for CommandOutputReader, used in parsing the command output file
 * and generating output beans.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class CommandOutputReaderTest {
	private String testOutputFilePath = "test-command-output.txt";
	private CommandOutputReader outputReader;
	@Rule
	public ExpectedException exception = ExpectedException.none ();
	private OutputHeaderService headerService;
	private OutputFooterService footerService;

	@Before
	public void setUp() {
		outputReader = new CommandOutputReader (testOutputFilePath);
		outputReader = spy (outputReader);

		headerService = mock (OutputHeaderService.class);
		footerService = mock (OutputFooterService.class);
		when (headerService.headerSeparator ()).thenReturn (testResourceHeaderSeparator ());
		when (footerService.footerSeparator ()).thenReturn (testResourceFooterSeparator ());
		when (outputReader.outputHeaderService ()).thenReturn (headerService);
		when (outputReader.outputFooterService ()).thenReturn (footerService);
	}

	private String testResourceFooterSeparator() {
		return "=" + BotUtils.newLine ();
	}

	private String testResourceHeaderSeparator() {
		return "-" + BotUtils.newLine ();
	}

	@Test
	@Category ({ UnitTest.class })
	public void invalidPath() throws Exception {
		outputReader = new CommandOutputReader ("invalid path");

		exception.expect (InvalidCommandOutputFilePath.class);
		exception.expectCause (isA (FileNotFoundException.class));
		exception.expectMessage ("Invalid output file path");
		outputReader.parseOutputFile ();

		fail ("invalid path should throw FNFException expected");
	}

	@Test
	public void parseContent() throws Exception {
		String[][] expectedTokens = new String[][] { { "header", "body", "footer" },
				{ "header2", "body2", "footer2" }, { "header3", "body3", "footer3" } };

		outputReader.parseOutputFile ();
		List<CommandOutputBean> actualTokens = outputReader.parsedOutputBeans ();

		assertParsedOutput ("Content should be parsed without any errors:", expectedTokens,
				actualTokens);

	}

	private void assertParsedOutput(String message,
									String[][] expectedTokens,
									List<CommandOutputBean> actualTokens) {
		assertNotNull (message + "null was sent for list", actualTokens);
		assertEquals (message + "incorect no. of parsed tokens", expectedTokens.length,
				actualTokens.size ());
		assertContent (message, expectedTokens, actualTokens);
	}

	private void assertContent(	String message,
								String[][] expectedTokens,
								List<CommandOutputBean> actualTokens) {
		for (int i = 0; i < expectedTokens.length; i++)
			assertCommandOutputBean (message, expectedTokens[i], actualTokens.get (i));
	}

	private void assertCommandOutputBean(	String message,
											String[] expectedTokens,
											CommandOutputBean bean) {
		assertEquals (message, expectedTokens[0], bean.getHeader ());
		assertEquals (message, expectedTokens[1], bean.getBody ());
		assertEquals (message, expectedTokens[2], bean.getFooter ());
	}

}
