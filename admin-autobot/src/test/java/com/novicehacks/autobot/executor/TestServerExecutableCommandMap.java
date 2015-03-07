package com.novicehacks.autobot.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.core.types.Command;
import com.novicehacks.autobot.core.types.SSHServer;
import com.novicehacks.autobot.core.types.Server;
import com.novicehacks.autobot.core.types.ShellCommand;

public class TestServerExecutableCommandMap {

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testInitialization() {
		ServerExecutableCommandMap map = new ServerExecutableCommandMap ();
		assertFalse (map.containsKey (null));
	}

	@Test
	@Category (UnitTest.class)
	public void testPutWithNullKey() {
		ServerExecutableCommandMap map = new ServerExecutableCommandMap ();
		map.put (null, new ShellCommand (null));
		assertTrue (map.containsKey (null));
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testPutMultipleValuesCheckSize() {
		Server server = new SSHServer ("123");
		ServerExecutableCommandMap map = new ServerExecutableCommandMap ();
		map.put (server, new ShellCommand ("abc"));
		map.put (server, new ShellCommand ("def"));
		assertTrue (map.containsKey (server));
		assertEquals (1, map.size ());
		assertEquals (2, map.get (server).size ());
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testPutWithMultipleValue() {
		Collection<Command> collection;
		Server server = new SSHServer ("123");
		Command command1 = new ShellCommand ("abc");
		Command command2 = new ShellCommand ("def");
		Command command3 = new ShellCommand ("ghi");
		ServerExecutableCommandMap map = new ServerExecutableCommandMap ();
		collection = map.put (server, command1);
		assertEquals (1, collection.size ());
		collection = map.put (server, command2);
		assertEquals (2, collection.size ());
		collection = map.put (server, command3);
		System.out.println (collection);
		Collection<Command> expected;
		expected = new HashSet<Command> ();
		expected.add (command1);
		expected.add (command2);
		expected.add (command3);
		Assert.assertEquals (expected, collection);
	}
}
