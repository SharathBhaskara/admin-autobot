package com.novicehacks.autobot.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.novicehacks.autobot.categories.UnitTest;

public class TestGenericUncaughtExceptionHandler {

	@Test
	@Category (UnitTest.class)
	public void testUncaughtExceptionWhenThreadIsNull() {
		// given
		GenericUncaughtExceptionHandler handler;
		handler = new GenericUncaughtExceptionHandler ();
		// when
		handler.uncaughtException (null, new RuntimeException ("None"));
		// then
		assertTrue ("No Exceptions to be raised", true);
	}

	@Test
	@Category (UnitTest.class)
	public void testUncaughtExceptionWhenThrowableIsNull() {
		// given
		GenericUncaughtExceptionHandler handler;
		handler = new GenericUncaughtExceptionHandler ();
		// when
		handler.uncaughtException (new Thread (), null);
		// then
		assertTrue ("No Exceptions to be raised", true);
	}

	@Test
	@Category (UnitTest.class)
	public void testUncaughtException() {
		// given
		GenericUncaughtExceptionHandler handler;
		handler = new GenericUncaughtExceptionHandler ();
		// when
		handler.uncaughtException (new Thread (), new RuntimeException ("None"));
		// then
		assertTrue ("No Exceptions to be raised", true);
	}
}
