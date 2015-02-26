package com.novicehacks.autobot.ssh.logger;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.core.stubs.SysConfigDelegate;
import com.novicehacks.autobot.ssh.exception.UnixOutputLoggingException;
import com.novicehacks.autobot.ssh.logger.ShellOutputLoggerTask;
import com.novicehacks.autobot.ssh.logger.ShellOutputLoggerTaskHelper;
import com.novicehacks.autobot.types.ShellCommand;
import com.novicehacks.autobot.types.UnixServer;

public class SSHOutputLoggerTaskTest {
	private final String newLine = System.lineSeparator ();
	private final String lineSeperator = ":-:";
	private static final String OutputFolder = "temp-output";
	private String commandOutput = "output";
	@Mock
	private UnixServer server;
	@Mock
	private ShellCommand command;
	@InjectMocks
	private ShellOutputLoggerTask outputLogger;

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
		Path outputFolderPath = Paths.get (OutputFolder);
		Files.deleteIfExists (outputFolderPath.resolve (ShellOutputLoggerTaskHelper.OutputFile));
		Files.deleteIfExists (outputFolderPath);
	}

	@Test
	@Category (UnitTest.class)
	public void instantiate() {
		this.outputLogger = new ShellOutputLoggerTask (this.server, this.command,
				this.commandOutput);
	}

	@Test
	@Category (UnitTest.class)
	public void instantiateWithNullServer() {
		// when
		this.server = null;
		// then
		this.exception.expect (UnixOutputLoggingException.class);
		this.outputLogger = new ShellOutputLoggerTask (this.server, this.command,
				this.commandOutput);
	}

	@Test
	@Category (UnitTest.class)
	public void instantiateWithNullCommand() {
		// when
		this.command = null;
		// then
		this.exception.expect (UnixOutputLoggingException.class);
		this.outputLogger = new ShellOutputLoggerTask (this.server, this.command,
				this.commandOutput);
	}

	@Test
	@Category (UnitTest.class)
	public void instantiateWithNullCommandOutput() {
		// when
		this.commandOutput = null;
		// then
		this.exception.expect (UnixOutputLoggingException.class);
		this.outputLogger = new ShellOutputLoggerTask (this.commandOutput);
	}

	@Test
	@Category (UnitTest.class)
	public void getHeader() {
		// given
		StringBuilder buffer = new StringBuilder ();
		buffer.append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator).append ("Server : null (null)")
				.append (this.lineSeperator).append ("Command : null (null)")
				.append (this.lineSeperator).append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator);
		String expected = buffer.toString ();
		// when
		String actual = ShellOutputLoggerTaskHelper.headerService (this.server, this.command).header ();
		actual = actual.replaceAll (this.newLine, this.lineSeperator);
		// then
		System.out.println ("Header Actual - " + actual);
		System.out.println ("Header expected - " + expected);
		Assert.assertThat (actual, CoreMatchers.containsString ("Server : null (null)"));
		Assert.assertThat (actual, CoreMatchers.containsString ("Command : null (null)"));
		assertEquals (expected, actual);
	}

	@Test
	@Category (UnitTest.class)
	public void getFooter() {
		// given
		StringBuilder buffer = new StringBuilder ();
		buffer.append ("**************************************************")
				.append (this.lineSeperator)
				.append ("**************************************************");
		String expected = buffer.toString ();
		// when
		String actual = ShellOutputLoggerTaskHelper.footerService ().footer ();
		actual = actual.replaceAll (this.newLine, this.lineSeperator);
		// then
		System.out.println ("Footer Actual - " + actual);
		System.out.println ("Footer expected - " + expected);
		assertEquals (expected, actual);
	}

	@Test
	@Category (UnitTest.class)
	public void getFormattedContent() {
		// given
		StringBuilder buffer = new StringBuilder ();
		buffer.append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator).append ("Server : null (null)")
				.append (this.lineSeperator).append ("Command : null (null)")
				.append (this.lineSeperator).append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator).append (this.commandOutput)
				.append (this.lineSeperator)
				.append ("**************************************************")
				.append (this.lineSeperator)
				.append ("**************************************************");
		String expected = buffer.toString ();

		// when
		this.outputLogger = new ShellOutputLoggerTask (this.server, this.command,
				this.commandOutput);
		String actual = this.outputLogger.getFormattedContent ();
		actual = actual.replaceAll (this.newLine, this.lineSeperator);
		// then
		System.out.println ("Formatted Content Actual - " + actual);
		System.out.println ("Formatted Content Expected - " + expected);
		assertEquals (expected, actual);
	}
}
