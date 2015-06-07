package com.novicehacks.autobot.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.novicehacks.autobot.config.ApplicationConfig;
import com.novicehacks.autobot.config.ResourceConfigParser;

public class TestResourceConfigParser {
	private ResourceConfigParser parserSpy;
	final String firstLineInTemplate = "abcd:defg:123,546";
	final String secondLineInTemplate = "gh	i:ab 123:def12";

	@Before
	public void setup() throws IOException {
		parserSpy = spy (ResourceConfigParser.class);
		Path path = Paths.get ("parser-template.txt");
		parserSpy.setFilePath (path.toString ());
		setUsageOfDefaultTokenSeperator ();
	}

	private void setUsageOfDefaultTokenSeperator() {
		ApplicationConfig.getInstance ().unload ();
	}

	@Test
	public void testReadingAllLines() throws IOException {
		// when
		Set<String> config = parserSpy.getConfigFromFile ();
		// then
		assertEquals ("Incorrect no. of lines read from parser-template.txt", 2, config.size ());
	}

	@Test
	public void testTokenizingLines() throws IOException {
		// when
		Set<String> configSet = parserSpy.getConfigFromFile ();
		// then
		assertTrue ("Incorrect Tokens when parsing first line",
				configSet.contains (firstLineInTemplate));
	}

	@Test
	public void testTokenizingLinesWithSpaces() throws IOException {
		// when
		Set<String> configSet = parserSpy.getConfigFromFile ();
		// then
		assertTrue ("Incorrect Tokens when parsing first line", 
				configSet.contains (secondLineInTemplate));
	}
}
