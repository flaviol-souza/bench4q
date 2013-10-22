/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there. 
 * 
 * Distributed according to the GNU Lesser General Public Licence. 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by   
 * the Free Software Foundation; either version 2.1 of the License, or any
 * later version.
 * 
 * SEE Copyright.txt FOR FULL COPYRIGHT INFORMATION.
 * 
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * This version is a based on the implementation of TPC-W from University of Wisconsin. 
 * This version used some source code of The Grinder.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *  * Initial developer(s): Zhiquan Duan.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */

package org.bench4Q.common.util.thread;

import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.UncheckedInterruptedException;

/**
 * Work queue and worker threads.
 */
public final class Executor {

	private final ThreadSafeQueue m_workQueue;
	private final ThreadPool m_threadPool;

	/**
	 * Constructor.
	 * 
	 * @param numberOfThreads
	 *            Number of worker threads to use.
	 */
	public Executor(int numberOfThreads) {

		this(new ThreadSafeQueue(), numberOfThreads);
	}

	/**
	 * Constructor. Allows unit tests to provide different work queue
	 * implementation.
	 * 
	 * @param workQueue
	 *            Queue to use.
	 * @param numberOfThreads
	 *            Number of worker threads to use.
	 */
	Executor(ThreadSafeQueue workQueue, int numberOfThreads) {

		m_workQueue = workQueue;

		final ThreadPool.InterruptibleRunnableFactory runnableFactory = new ThreadPool.InterruptibleRunnableFactory() {
			public InterruptibleRunnable create() {
				return new ExecutorRunnable();
			}
		};

		m_threadPool = new ThreadPool("Executor", numberOfThreads,
				runnableFactory);
		m_threadPool.start();
	}

	/**
	 * Queue some work.
	 * 
	 * @param work
	 *            The work
	 * @throws ShutdownException
	 *             If the Executor has been stopped.
	 */
	public void execute(InterruptibleRunnable work) throws ShutdownException {
		if (m_threadPool.isStopped()) {
			throw new ShutdownException("Executor is stopped");
		}

		try {
			m_workQueue.queue(work);
		} catch (ThreadSafeQueue.ShutdownException e) {
			throw new ShutdownException("Executor is stopped", e);
		}
	}

	/**
	 * Shut down this executor, waiting for work to complete.
	 * 
	 */
	public void gracefulShutdown() {
		try {
			m_workQueue.gracefulShutdown();
		} finally {
			m_threadPool.stopAndWait();
		}
	}

	/**
	 * Shut down this executor, discarding any outstanding work.
	 */
	public void forceShutdown() {
		m_workQueue.shutdown();
		m_threadPool.stop();
	}

	private class ExecutorRunnable implements InterruptibleRunnable {

		public void interruptibleRun() {
			while (true) {
				final InterruptibleRunnable runnable;

				try {
					runnable = (InterruptibleRunnable) m_workQueue
							.dequeue(true);
				} catch (ThreadSafeQueue.ShutdownException e) {
					// We've been shut down, exit the thread cleanly.
					break;
				} catch (UncheckedInterruptedException e) {
					// We've been interrupted, exit the thread cleanly.
					forceShutdown();
					break;
				}

				runnable.interruptibleRun();
			}
		}
	}

	/**
	 * Exception that indicates <code>Executor</code> has been shutdown.
	 */
	public static final class ShutdownException extends Bench4QException {

		private ShutdownException(String s) {
			super(s);
		}

		private ShutdownException(String s, Exception e) {
			super(s, e);
		}
	}
}
