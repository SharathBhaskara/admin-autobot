package com.novicehacks.autobot;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadManager {

	private ExecutorService		executorService		= Executors.newCachedThreadPool ();
	private final ReentrantLock	executorServiceLock	= new ReentrantLock (true);
	private Logger				logger				= LogManager.getLogger (ThreadManager.class);

	private ThreadManager () {
		Thread.setDefaultUncaughtExceptionHandler (genericExceptionHandler ());
	}

	public static ThreadManager getInstance() {
		return ThreadManagerSingleton.getInstance ();
	}

	private static class ThreadManagerSingleton {
		private static final ThreadManager	instance	= new ThreadManager ();

		public static ThreadManager getInstance() {
			return instance;
		}
	}

	/**
	 * Submits the asynchronous task to the managed ThreadPool.
	 * 
	 * @param task
	 * @return
	 * @throws NullPointerException
	 *         if the thread pool is not active.
	 */
	public Future<?> submitTaskToThreadPool(Runnable task) {
		Future<?> future;
		executorServiceLock.lock ();
		future = executorService.submit (task);
		executorServiceLock.unlock ();
		return future;
	}

	/**
	 * No matter what the status of the ThreadPool is the executor will be given
	 * a shutdown and assigned to null after this call. (Avoids any further
	 * submission of new tasks to the umanaged thread pool)
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean DismissThreadPool() throws InterruptedException {
		return destroyExecutorService ();
	}

	/**
	 * When forced to create a new thread pool, the current thread pool if
	 * available will be given a shutdown command and a new thread pool will be
	 * initiated
	 * 
	 * @param forced
	 *        when true returns false if the previous thread pool cannot be
	 *        terminated or a new thread pool cannot be created.
	 * @return true if new thread pool is created otherwise false
	 * @throws InterruptedException
	 */
	public boolean InitiateThreadPool(boolean forced) throws InterruptedException {
		if (forced) {
			executorServiceLock.lock ();
			destroyExecutorService ();
			executorServiceLock.unlock ();
		}
		return initiateExecutorService ();
	}

	/**
	 * If the thread pool is not available then a new thread pool is created.
	 * 
	 * @return true if new thread pool created false otherwise
	 * @throws InterruptedException
	 */
	public boolean InitiateThreadPool() throws InterruptedException {
		if (isExecutorServiceAvailable ()) {
			return false;
		} else {
			return initiateExecutorService ();
		}
	}

	private boolean initiateExecutorService() throws InterruptedException {
		boolean status = false;
		if (tryLock (executorServiceLock)) {
			executorService = Executors.newCachedThreadPool ();
			executorServiceLock.unlock ();
			status = true;
		} else {
			logger.warn ("Lock not acquired for Iniating Executor Service");
			status = false;
		}
		return status;
	}

	private boolean destroyExecutorService() throws InterruptedException {
		boolean status = false;
		if (isExecutorServiceAvailable ())
			status = destroyThreadPool ();
		return status;
	}

	private boolean destroyThreadPool() throws InterruptedException {
		boolean status = false;
		if (tryLock (executorServiceLock)) {
			executorService.shutdown ();
			executorService = null;
			status = true;
			executorServiceLock.unlock ();
		} else {
			logger.warn ("Lock not acquired for Destroying Executor Service");
			status = false;
		}
		return status;
	}

	private boolean isExecutorServiceAvailable() throws InterruptedException {
		boolean status = false;
		if (tryLock (executorServiceLock)) {
			if (executorService != null && !executorService.isShutdown ()) {
				status = true;
			}
			executorServiceLock.unlock ();
		}
		return status;
	}

	private boolean tryLock(ReentrantLock reentrantLock) throws InterruptedException {
		if (reentrantLock.tryLock () || reentrantLock.tryLock (60, TimeUnit.SECONDS))
			return true;
		else
			return false;
	}

	public ExecutorService shutDownThreadPool() throws InterruptedException {
		ExecutorService temp;
		temp = this.executorService;
		executorServiceLock.lockInterruptibly ();
		destroyThreadPool ();
		InitiateThreadPool ();
		executorServiceLock.unlock ();
		return temp;
	}

	public static UncaughtExceptionHandler genericExceptionHandler() {
		UncaughtExceptionHandler eh;
		eh = new UncaughtExceptionHandler () {
			private Logger	logger	= LogManager.getLogger (this);

			@Override
			public void uncaughtException(Thread t, Throwable e) {
				try {
					logger.error ("Uncaught Exception On Thread {} ", t.getName (), e);
				} catch (Throwable error) {
					logger.error (error);
				}
			}
		};
		return eh;
	}
}
