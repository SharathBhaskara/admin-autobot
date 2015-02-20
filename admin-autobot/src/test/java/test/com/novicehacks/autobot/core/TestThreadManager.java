package test.com.novicehacks.autobot.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;

import test.com.novicehacks.autobot.categories.UnitTest;

import com.novicehacks.autobot.ThreadManager;

public class TestThreadManager {

	@Rule
	public ExpectedException	exception	= ExpectedException.none ();

	@Before
	public void setUp() throws InterruptedException {
		ThreadManager.getInstance ().createThreadPool (true);
	}

	@After
	public void tearDown() throws InterruptedException {
		ThreadManager.getInstance ().shutdownThreadPool ();
	}

	@Test
	public void testGetInstance() {
		// given
		ThreadManager tm1;
		ThreadManager tm2;
		// when
		tm1 = ThreadManager.getInstance ();
		tm2 = ThreadManager.getInstance ();
		// then
		assertEquals (tm1, tm2);
	}

	@Test
	@Category (UnitTest.class)
	public void submitRunnableTaskToThreadPool() throws InterruptedException, ExecutionException {
		// given
		String message = "Thread Executed";
		ThreadManager tm = ThreadManager.getInstance ();
		tm.createThreadPool ();
		final Runnable task = getRunnableTask (message);
		// when
		Future<?> f = tm.submitTaskToThreadPool (task);
		// then
		this.exception.expect (ExecutionException.class);
		this.exception
				.expectMessage (Matchers.contains (RuntimeException.class.toGenericString ()));
		this.exception.expectMessage (Matchers.contains (message));
		f.get ();
	}

	private Runnable getRunnableTask(final String message) {
		Runnable task = new Runnable () {
			@Override
			public void run() {
				throw new RuntimeException (message);
			}
		};
		return task;
	}

	@Test
	@Category (UnitTest.class)
	public void submitCallableTaskToThreadPool() throws InterruptedException, ExecutionException {
		// given
		String initiated = "Initiated";
		String completed = "Completed";
		ThreadManager tm = ThreadManager.getInstance ();
		tm.createThreadPool ();
		final Callable<String> task = getCallableTask (initiated, completed);
		// when
		Future<String> f = tm.submitTaskToThreadPool (task);
		// then
		assertEquals (completed, f.get ());
	}

	private Callable<String> getCallableTask(final String initiated, final String completed) {
		Callable<String> task = new Callable<String> () {
			public String	value	= initiated;

			@Override
			public String call() {
				this.value = completed;
				return this.value;
			}
		};
		return task;
	}

	@Test
	@Category (UnitTest.class)
	public void waitForTaskCompletionWhenTimedout() throws InterruptedException,
			ExecutionException, TimeoutException {
		// given
		int timeout = 2;
		int longTimeoutInMillis = 100;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		ThreadManager tm = ThreadManager.getInstance ();
		Runnable task = getWaitingTask (longTimeoutInMillis);
		// when
		Future<?> f = tm.submitTaskToThreadPool (task);
		Thread.sleep (timeout);
		boolean status = tm.terminateAndWaitForTaskCompletion (timeout, unit);
		// then
		assertFalse ("Task execution should not have completed", status);
		this.exception.expect (TimeoutException.class);
		f.get (0, unit);
	}

	private Runnable getWaitingTask(final int longTimeoutInMillis) {
		Runnable task = new Runnable () {
			@Override
			public void run() {
				try {
					Thread.sleep (longTimeoutInMillis);
				} catch (InterruptedException e) {
					e.printStackTrace ();
				}
			}
		};
		return task;
	}

	@Test
	@Category (UnitTest.class)
	public void waitForTaskCompletionWhenTaskComplete() throws InterruptedException,
			ExecutionException, TimeoutException {
		// given
		int timeout = 2;
		int longTimeoutInMillis = 2;
		TimeUnit unit = TimeUnit.MILLISECONDS;
		ThreadManager tm = ThreadManager.getInstance ();
		Runnable task = getWaitingTask (longTimeoutInMillis);
		// when
		Future<?> f = tm.submitTaskToThreadPool (task);
		Thread.sleep (timeout);
		boolean status = tm.terminateAndWaitForTaskCompletion (timeout, unit);
		// then
		assertTrue ("Task execution should have been completed", status);
		assertNull (f.get (0, unit));
	}

	@Test
	@Category (UnitTest.class)
	public void initiateThreadPoolAfterDismiss() throws InterruptedException {
		// given
		ThreadManager tm = ThreadManager.getInstance ();
		tm.shutdownThreadPool ();
		// when
		boolean status = tm.createThreadPool ();
		// then
		assertTrue ("New Thread Pool Should be created", status);
	}

	@Test
	@Category (UnitTest.class)
	public void initiateThreadPoolForcedAfterInstanceCreation() throws InterruptedException {
		// given
		ThreadManager tm = ThreadManager.getInstance ();
		// when
		boolean status = tm.createThreadPool (true);
		// then
		assertTrue ("New Thread Pool Should be created", status);
	}

	@Test
	@Category (UnitTest.class)
	public void initiateThreadPoolUnForcedAfterInstanceCreation() throws InterruptedException {
		// given
		ThreadManager tm = ThreadManager.getInstance ();
		// when
		boolean status = tm.createThreadPool (false);
		// then
		assertFalse ("New Thread Pool Should not be created", status);
	}

	@Test
	@Category (UnitTest.class)
	public void initiateThreadPoolAfterInstanceCreation() throws InterruptedException {
		// given
		ThreadManager tm = ThreadManager.getInstance ();
		// when
		boolean status = tm.createThreadPool ();
		// then
		assertFalse ("New Thread Pool Should not be created", status);
	}
}
