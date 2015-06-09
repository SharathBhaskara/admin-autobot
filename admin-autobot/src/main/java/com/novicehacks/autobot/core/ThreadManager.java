package com.novicehacks.autobot.core;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The single place where all the threads are executed, and handled in the admin
 * autobot application.
 * 
 * @author Sharath Chand Bhaskara for NoviceHacks!
 *
 */
public class ThreadManager {

	private ExecutorService executorService = Executors.newCachedThreadPool ();
	private final ReentrantLock executorServiceLock = new ReentrantLock (true);
	private Logger logger = LogManager.getLogger (ThreadManager.class);

	private ThreadManager () {
		Thread.setDefaultUncaughtExceptionHandler (new GenericUncaughtExceptionHandler ());
	}

	private static class ThreadManagerSingleton {
		private static final ThreadManager instance = new ThreadManager ();

		public static ThreadManager getInstance() {
			return instance;
		}
	}

	public static ThreadManager getInstance() {
		return ThreadManagerSingleton.getInstance ();
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
		this.executorServiceLock.lock ();
		future = this.executorService.submit (task);
		this.executorServiceLock.unlock ();
		return future;
	}

	/**
	 * Submits the asynchronous task to the managed ThreadPool.
	 * 
	 * @param <T>
	 * 
	 * @param task
	 * @return
	 * @throws NullPointerException
	 *         if the thread pool is not active.
	 */
	public <T> Future<T> submitTaskToThreadPool(Callable<T> task) {
		Future<T> future;
		this.executorServiceLock.lock ();
		future = this.executorService.submit (task);
		this.executorServiceLock.unlock ();
		return future;
	}

	/**
	 * Will Wait until the timeout expires or all the tasks submitted to the
	 * current executor service is completed.
	 * 
	 * @param timeout
	 * @param timeUnit
	 * @throws InterruptedException
	 * @return <strong>true</strong> if tasks completed successfully,
	 *         <strong>false</strong> if timed out.
	 * @see ExecutorService#awaitTermination
	 */
	public boolean terminateAndWaitForTaskCompletion(int timeout, TimeUnit timeUnit)
			throws InterruptedException {
		boolean status;
		this.logger.entry (timeout, timeUnit);
		ExecutorService service;
		service = this.executorService;
		shutdownThreadPool ();
		status = service.awaitTermination (timeout, timeUnit);
		this.logger.exit (status);
		return status;
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
	public boolean createThreadPool(boolean forced) throws InterruptedException {
		if (forced) {
			this.executorServiceLock.lock ();
			shutdownExecutorService ();
			this.executorServiceLock.unlock ();
		}
		return createThreadPool ();
	}

	/**
	 * If the thread pool is not available then a new thread pool is created.
	 * 
	 * @return true if new thread pool created false otherwise
	 * @throws InterruptedException
	 */
	public boolean createThreadPool() throws InterruptedException {
		if (isThreadPoolAvailable ()) {
			return false;
		} else {
			return assignNewCachedThreadPool ();
		}
	}

	/**
	 * 
	 * @return true if threadpool is available for task submission, otherwise
	 *         false.
	 * @throws InterruptedException
	 * @see {@link ExecutorService#shutdown()}
	 */
	public boolean isThreadPoolAvailable() throws InterruptedException {
		boolean status = false;
		if (tryLock (this.executorServiceLock)) {
			if (this.executorService != null && !this.executorService.isShutdown ()) {
				status = true;
			}
			this.executorServiceLock.unlock ();
		}
		return status;
	}

	private boolean assignNewCachedThreadPool() throws InterruptedException {
		boolean status = false;
		if (tryLock (this.executorServiceLock)) {
			this.executorService = Executors.newCachedThreadPool ();
			this.executorServiceLock.unlock ();
			status = true;
		} else {
			this.logger.warn ("Lock not acquired for Iniating Executor Service");
			status = false;
		}
		return status;
	}

	private boolean tryLock(ReentrantLock reentrantLock) throws InterruptedException {
		if (reentrantLock.tryLock () || reentrantLock.tryLock (60, TimeUnit.SECONDS))
			return true;
		else
			return false;
	}

	/**
	 * No matter what the status of the ThreadPool is the executor will be given
	 * a shutdown and assigned to null after this call. (Avoids any further
	 * submission of new tasks to the umanaged thread pool)
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public boolean shutdownThreadPool() throws InterruptedException {
		return shutdownExecutorService ();
	}

	private boolean shutdownExecutorService() throws InterruptedException {
		boolean status = false;
		if (isThreadPoolAvailable ())
			status = shutdown ();
		return status;
	}

	private boolean shutdown() throws InterruptedException {
		boolean status = false;
		if (tryLock (this.executorServiceLock)) {
			this.executorService.shutdown ();
			this.executorService = null;
			status = true;
			this.executorServiceLock.unlock ();
		} else {
			this.logger.warn ("Lock not acquired for Destroying Executor Service");
			status = false;
		}
		return status;
	}

}
