package com.novicehacks.autobot.logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.core.stubs.SysConfigDelegate;
import com.novicehacks.autobot.ssh.exception.UnixOutputLoggingException;
import com.novicehacks.autobot.types.SSHServer;
import com.novicehacks.autobot.types.ShellCommand;

public class DefaultOutputLoggerTaskTest {
	private final String newLine = System.lineSeparator ();
	private final String lineSeperator = ":-:";
	private static final String OutputFolder = "temp-output";
	private String commandOutput = "output";
	@Mock
	private SSHServer server;
	@Mock
	private ShellCommand command;
	@InjectMocks
	private DefaultOutputLoggerTask outputLogger;

	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@BeforeClass
	public static void setupConfig() {
		SysConfigDelegate sysConfigDelegate = new SysConfigDelegate ();
		sysConfigDelegate.shellConsoleFolder (OutputFolder).build ();
	}

	@Before
	public void setup() {
		MockitoAnnotations.initMocks (this);
	}

	@After
	public void cleanUp() throws IOException {
		Path outputFolderPath = this.outputLogger.logLocation ();
		Files.deleteIfExists (outputFolderPath);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void instantiate() {
		this.outputLogger = new DefaultOutputLoggerTask (this.server, this.command,
				this.commandOutput);
	}

	@Test
	@Category (UnitTest.class)
	public void instantiateWithNullServer() {
		// when
		this.server = null;
		// then
		this.exception.expect (UnixOutputLoggingException.class);
		this.outputLogger = new DefaultOutputLoggerTask (this.server, this.command,
				this.commandOutput);
	}

	@Test
	@Category (UnitTest.class)
	public void instantiateWithNullCommand() {
		// when
		this.command = null;
		// then
		this.exception.expect (UnixOutputLoggingException.class);
		this.outputLogger = new DefaultOutputLoggerTask (this.server, this.command,
				this.commandOutput);
	}

	@Test
	@Category (UnitTest.class)
	public void instantiateWithNullCommandOutput() {
		// when
		this.commandOutput = null;
		// then
		this.exception.expect (UnixOutputLoggingException.class);
		this.outputLogger = new DefaultOutputLoggerTask (this.commandOutput);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void headerServiceTest() {
		// given
		this.outputLogger = new DefaultOutputLoggerTask (this.server, this.command, null);
		// when
		OutputHeaderService actual = this.outputLogger.headerService ();
		// then
		Assert.assertThat (actual, CoreMatchers.instanceOf (OutputHeaderService.class));
		Assert.assertThat (actual, CoreMatchers.instanceOf (DefaultOutputHeaderService.class));
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void getHeader() {
		// given
		Instant instantTime = Instant.now ();
		mockOutputLoggerWithHeaderServiceAndInstantTime (instantTime);
		StringBuilder expectedBuffer = new StringBuilder ();
		expectedBuffer.append (getExpectedHeader (instantTime));

		String expected = expectedBuffer.toString ();
		// when
		String actual = this.outputLogger.headerService ().header ();
		actual = actual.replaceAll (this.newLine, this.lineSeperator);
		// then
		System.out.println ("Expected : " + expected);
		System.out.println ("Actual   : " + actual);
		assertEquals (expected, actual);
	}

	private void mockOutputLoggerWithHeaderServiceAndInstantTime(Instant instantTime) {
		DefaultOutputHeaderService headerService;
		headerService = new DefaultOutputHeaderService (this.server, this.command);
		headerService = spy (headerService);
		when (headerService.getInstantTime ()).thenReturn (instantTime, instantTime, instantTime);

		this.outputLogger = spy (this.outputLogger);
		when (this.outputLogger.headerService ()).thenReturn (headerService);
	}

	private String getExpectedHeader(Instant instantTime) {
		StringBuilder expectedBuffer = new StringBuilder ();
		expectedBuffer.append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator).append ("Server : null (null)")
				.append (this.lineSeperator).append ("Command : null (null)")
				.append (this.lineSeperator).append ("Execution Timestamp : ")
				.append (instantTime.getEpochSecond ()).append (" (")
				.append (instantTime.toString ()).append (")").append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator);
		return expectedBuffer.toString ();
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void footerServiceTest() {
		// when
		OutputFooterService actual = this.outputLogger.footerService ();
		// then
		Assert.assertThat (actual, CoreMatchers.instanceOf (OutputFooterService.class));
		Assert.assertThat (actual, CoreMatchers.instanceOf (DefaultOutputFooterService.class));
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void getFooter() {
		// given
		StringBuilder expectedBuffer = new StringBuilder ();
		expectedBuffer.append (getExpectedFooter ());
		String expected = expectedBuffer.toString ();
		// when
		String actual = this.outputLogger.footerService ().footer ();
		actual = actual.replaceAll (this.newLine, this.lineSeperator);
		// then
		assertEquals (expected, actual);
	}

	private Object getExpectedFooter() {
		StringBuilder expectedBuffer = new StringBuilder ();
		expectedBuffer.append ("**************************************************")
				.append (this.lineSeperator)
				.append ("**************************************************");

		return expectedBuffer.toString ();
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void getFormattedContent() {
		// given
		String sampleOutput = "Sample Output";
		Instant instantTime = Instant.now ();
		this.outputLogger = new DefaultOutputLoggerTask (this.server, this.command, sampleOutput);

		mockOutputLoggerWithHeaderServiceAndInstantTime (instantTime);

		StringBuilder expectedBuffer = new StringBuilder ();
		expectedBuffer.append (getExpectedHeader (instantTime));
		expectedBuffer.append (sampleOutput).append (this.lineSeperator);
		expectedBuffer.append (getExpectedFooter ());

		String expected = expectedBuffer.toString ();

		// when
		String actual = this.outputLogger.getFormattedContent ();
		actual = actual.replaceAll (this.newLine, this.lineSeperator);
		// then
		System.out.println ("Expected : " + expected);
		System.out.println ("Actual   : " + actual);

		assertEquals (expected, actual);
	}

}
