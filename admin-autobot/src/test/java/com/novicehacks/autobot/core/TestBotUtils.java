package com.novicehacks.autobot.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;

import com.novicehacks.autobot.categories.FunctionalTest;
import com.novicehacks.autobot.categories.IgnoredTest;
import com.novicehacks.autobot.categories.UnitTest;
import com.novicehacks.autobot.core.stubs.MappableStub;

public class TestBotUtils {
	private Logger logger = LogManager.getLogger (TestBotUtils.class);
	@Rule
	public ExpectedException exception = ExpectedException.none ();

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testHasNullReferencesWithNullParam() {
		// given
		Object[] params = null;
		// when
		boolean status = BotUtils.HasNullReferences (params);
		// then
		assertTrue ("NullPointer should not be thrown", status);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testHasNullReferencesWithStringArrayParamHavingNull() {
		// given
		String[] data = { "abc", null, "def" };
		// when
		boolean status = BotUtils.HasNullReferences (data);
		// then
		assertTrue ("Null value Passed but Ignored", status);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testHasNullReferencesWithStringArrayParam() {
		// given
		String[] data = { "abc", "123", "def" };
		// when
		boolean status = BotUtils.HasNullReferences (data);
		// then
		assertFalse ("Null value not exists, but failed", status);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testHasNullReferencesWithMultipleArrayParameters() {
		// given
		String[] data1 = { "abc", "123", "def" };
		String[] data2 = { "abc", "123", "def" };
		// when
		boolean status = BotUtils.HasNullReferences (data1, data2);
		// then
		assertFalse ("Null value not exists, but failed", status);
	}

	@Test
	@Category ({ UnitTest.class, IgnoredTest.class })
	public void testHasNullReferencesWithMultipleArrayWithNullInArray() {
		// given
		String[] data1 = { "abc", "123", null };
		String[] data2 = { "abc", "123", "def" };
		// when
		boolean status = BotUtils.HasNullReferences (data1, data2);
		// then
		// assertTrue ("Null value exists, but failed", status);
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testCreateMap() {
		// given
		String item1 = "item1";
		String item2 = "item2";
		Collection<MappableStub> elements = getMappableStubCollection (item1, item2);
		// when
		Map<String, MappableStub> elementMap = BotUtils.CreateMap (elements);
		// then
		assertEquals ("Created Map size should match collection size", elements.size (),
				elementMap.size ());
		assertThat ("Map having elements not in collection", elementMap.keySet (),
				CoreMatchers.hasItems (item1, item2));
	}

	private Collection<MappableStub> getMappableStubCollection(String... items) {
		List<MappableStub> stubs;
		stubs = new LinkedList<MappableStub> ();
		if (items == null)
			return stubs;
		MappableStub stub;
		for (String item : items) {
			stub = new MappableStub (item);
			stubs.add (stub);
		}
		return stubs;
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testCreteMapWithNullCollection() {
		// given
		Collection<MappableStub> elements = null;
		// when
		Map<String, MappableStub> elementMap = BotUtils.CreateMap (elements);
		// then
		assertNotNull ("Created Map cannot be null", elementMap);
		assertEquals ("Created Map should be empty", 0, elementMap.size ());
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testCreteMapWithEmptyCollection() {
		// given
		Collection<MappableStub> elements = getMappableStubCollection ();
		// when
		Map<String, MappableStub> elementMap = BotUtils.CreateMap (elements);
		// then
		assertNotNull ("Created Map cannot be null", elementMap);
		assertEquals ("Created Map should be empty", 0, elementMap.size ());
	}

	@Test
	@Category ({ UnitTest.class, FunctionalTest.class })
	public void testPropogateInterrupt() throws ExecutionException, InterruptedException {
		// given
		Runnable task = getInterruptingTask ();
		Runnable parentTask = getRunnableTask (task);
		ThreadManager.getInstance ().createThreadPool (true);
		// when
		Future<?> f = ThreadManager.getInstance ().submitTaskToThreadPool (parentTask);
		// then
		this.exception.expect (InterruptedException.class);
		Object any = f.get ();
		System.out.println (any);
	}

	private Runnable getRunnableTask(final Runnable task) {
		Runnable parentTask = new Runnable () {
			@Override
			public void run() {
				Future<?> f = ThreadManager.getInstance ().submitTaskToThreadPool (task);
				try {
					Object any = f.get ();
					TestBotUtils.this.logger.trace ("In Parent task : {}", any);
				} catch (ExecutionException | InterruptedException ex) {
					TestBotUtils.this.logger.trace ("Caught in ParenTask {}", ex);
					BotUtils.PropogateInterruptIfExist (ex);
				}
			}
		};
		return parentTask;
	}

	private Runnable getInterruptingTask() {
		Runnable task = new Runnable () {
			@Override
			public void run() {
				int counter = 0;
				try {
					while (true) {
						Thread.sleep (10);
						if (counter++ < 2)
							Thread.currentThread ().interrupt ();
					}
				} catch (InterruptedException e) {
					TestBotUtils.this.logger.trace ("Caught in Task", e);
					BotUtils.PropogateInterruptIfExist (e);
				}
			}
		};
		return task;
	}
}
