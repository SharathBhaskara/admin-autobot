package com.novicehacks.autobot.config.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.novicehacks.autobot.config.ApplicationConfig;
import com.novicehacks.autobot.config.parser.Parser;

public class TestParser {
	private Parser<String> parserSpy;
	final String firstLineInTemplate = "abcd;defg;123,546";
	final String secondLineInTemplate = "gh	i;ab 123;def12";

	@SuppressWarnings ("unchecked")
	@Before
	public void setup() throws IOException {
		parserSpy = spy (Parser.class);
		Path path = Paths.get ("parser-template.txt");
		parserSpy.setFilePath (path.toString ());
		setUsageOfDefaultTokenSeperator();
	}

	private void setUsageOfDefaultTokenSeperator() {
		ApplicationConfig.getInstance ().unload ();		
	}

	@Test
	public void testReadingAllLines() throws IOException {
		//when
		Map<String, String[]> tokens = parserSpy.getTokensFromFile ();
		//then
		assertEquals ("Incorrect no. of lines read from parser-template.txt", 2, tokens.size ());
	}
	
	@Test
	public void testTokenizingLines() throws IOException {
		//when
		Map<String, String[]> tokens = parserSpy.getTokensFromFile ();
		//then
		assertEquals("Incorrect Tokens when parsing first line",3,tokens.get (firstLineInTemplate).length);
	}
	
	@Test
	public void testTokenizingLinesWithSpaces() throws IOException {
		//when
		Map<String, String[]> tokens = parserSpy.getTokensFromFile ();
		//then
		assertEquals("Incorrect Tokens when parsing first line",3,tokens.get (secondLineInTemplate).length);
	}

}
