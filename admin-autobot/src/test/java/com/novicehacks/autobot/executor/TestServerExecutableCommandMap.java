package com.novicehacks.autobot.executor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

import com.novicehacks.autobot.executor.ServerExecutableCommandMap;
import com.novicehacks.autobot.types.Command;
import com.novicehacks.autobot.types.Server;
import com.novicehacks.autobot.types.ShellCommand;
import com.novicehacks.autobot.types.UnixServer;

public class TestServerExecutableCommandMap {

	@Test
	public void testInitialization() {
		ServerExecutableCommandMap map = new ServerExecutableCommandMap ();
		assertFalse (map.containsKey (null));
	}

	@Test
	public void testPutWithNullKey() {
		ServerExecutableCommandMap map = new ServerExecutableCommandMap ();
		map.put (null, new ShellCommand (null));
		assertTrue (map.containsKey (null));
	}

	@Test
	public void testPutMultipleValuesCheckSize() {
		Server server = new UnixServer ("123");
		ServerExecutableCommandMap map = new ServerExecutableCommandMap ();
		map.put (server, new ShellCommand ("abc"));
		map.put (server, new ShellCommand ("def"));
		assertTrue (map.containsKey (server));
		assertEquals (1, map.size ());
		assertEquals (2, map.get (server).size ());
	}

	@Test
	public void testPutWithMultipleValue() {
		Collection<Command> collection;
		Server server = new UnixServer ("123");
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
