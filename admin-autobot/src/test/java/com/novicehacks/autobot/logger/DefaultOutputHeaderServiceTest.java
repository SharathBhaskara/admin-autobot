package com.novicehacks.autobot.logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;

public class DefaultOutputHeaderServiceTest {
	private final String newLine = System.lineSeparator ();
	private final String lineSeperator = ":-:";
	@Mock
	Server server;
	@Mock
	Command command;
	DefaultOutputHeaderService headerService;

	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks (this);
		this.headerService = new DefaultOutputHeaderService (this.server, this.command);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void headerTest() {
		// given
		when (this.server.id ()).thenReturn ("S001");
		when (this.server.name ()).thenReturn ("AppServer");
		when (this.command.id ()).thenReturn ("C001");
		when (this.command.commandTxt ()).thenReturn ("df -k");
		Instant instantTime = Instant.now ();
		this.headerService = spy (this.headerService);
		when (this.headerService.getInstantTime ()).thenReturn (instantTime);

		StringBuilder buffer = new StringBuilder ();
		buffer.append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator).append ("Server : S001 (AppServer)")
				.append (this.lineSeperator).append ("Command : C001 (df -k)")
				.append (this.lineSeperator).append ("Execution Timestamp : ")
				.append (instantTime.getEpochSecond ()).append (" (")
				.append (instantTime.toString ()).append (")").append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator);

		String expected = buffer.toString ();

		// when
		String actual = this.headerService.header ();
		actual = actual.replaceAll (this.newLine, this.lineSeperator);
		// then
		assertEquals ("Formatted output didnt match", expected, actual);
	}

	@Test
	@Category ({ UnitTest.class })
	public void headerTestWithNullValue() {
		// given
		Instant instantTime = Instant.now ();
		StringBuilder buffer = new StringBuilder ();
		buffer.append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator).append ("Server : null (null)")
				.append (this.lineSeperator).append ("Command : null (null)")
				.append (this.lineSeperator).append ("Execution Timestamp : ")
				.append (instantTime.getEpochSecond ()).append (" (")
				.append (instantTime.toString ()).append (")").append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator);
		String expected = buffer.toString ();

		this.headerService = spy (this.headerService);
		when (this.headerService.getInstantTime ()).thenReturn (instantTime);
		// when
		String actual = this.headerService.header ();
		actual = actual.replaceAll (this.newLine, this.lineSeperator);
		// then
		System.out.println ("Expected Header: " + expected);
		System.out.println ("Actual Header:   " + actual);
		assertEquals ("Formatted output didnt match", expected, actual);
	}

	@Test
	@Category ({ UnitTest.class })
	public void instantiationWithNullParam() {
		// given
		this.server = null;
		this.command = null;
		// then
		this.exception.expect (IllegalArgumentException.class);
		this.headerService = new DefaultOutputHeaderService (this.server, this.command);

		fail ("Invalid Parameters passed");
	}

}
