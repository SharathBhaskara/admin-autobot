package com.novicehacks.autobot.logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
	public void headerTest() {
		// given
		when (this.server.id ()).thenReturn ("S001");
		when (this.server.name ()).thenReturn ("AppServer");
		when (this.command.id ()).thenReturn ("C001");
		when (this.command.command ()).thenReturn ("df -k");

		StringBuilder buffer = new StringBuilder ();
		buffer.append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator).append ("Server : S001 (AppServer)")
				.append (this.lineSeperator).append ("Command : C001 (df -k)")
				.append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator);

		String expected = buffer.toString ();

		// when
		String actual = this.headerService.header ();
		actual = actual.replaceAll (this.newLine, this.lineSeperator);
		// then
		assertEquals (expected, actual);
	}

	@Test
	public void headerTestWithNullValue() {
		// given
		StringBuilder buffer = new StringBuilder ();
		buffer.append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator).append ("Server : null (null)")
				.append (this.lineSeperator).append ("Command : null (null)")
				.append (this.lineSeperator)
				.append ("++++++++++++++++++++++++++++++++++++++++++++++++++")
				.append (this.lineSeperator);
		String expected = buffer.toString ();
		// when
		String actual = this.headerService.header ();
		actual = actual.replaceAll (this.newLine, this.lineSeperator);
		// then
		assertEquals (expected, actual);
	}

	@Test
	public void instantiationWithNullParam() {
		// given
		this.server = null;
		this.command = null;
		// then
		this.exception.expect (IllegalArgumentException.class);
		this.headerService = new DefaultOutputHeaderService (this.server, this.command);
	}

}
