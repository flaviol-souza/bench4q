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

import org.bench4Q.common.UncheckedInterruptedException;

/**
 * A simple thread pool.
 */
public final class ThreadPool {

	private final ThreadGroup m_threadGroup;
	private final Thread[] m_threads;
	private boolean m_started = false;
	private boolean m_stopped = false;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            A name for the thread pool.
	 * @param numberOfThreads
	 *            Number of threads.
	 * @param runnableFactory
	 *            Factory which defines what our threads should do.
	 */
	public ThreadPool(String name, int numberOfThreads,
			InterruptibleRunnableFactory runnableFactory) {

		m_threadGroup = new ThreadGroup(name);
		m_threadGroup.setDaemon(true);

		m_threads = new Thread[numberOfThreads];

		for (int i = 0; i < m_threads.length; ++i) {
			final Runnable runnable = new InterruptibleRunnableAdapter(
					runnableFactory.create());

			m_threads[i] = new Thread(m_threadGroup, runnable, name
					+ " thread " + i);
			m_threads[i].setDaemon(true);
		}
	}

	/**
	 * Starts the threads.
	 * 
	 * @throws IllegalStateException
	 *             If the thread pool has already been started, or has been
	 *             stopped.
	 */
	public void start() {
		synchronized (this) {
			if (m_stopped) {
				throw new IllegalStateException("Stopped");
			}

			if (m_started) {
				throw new IllegalStateException("Already started");
			}

			m_started = true;
		}

		for (int i = 0; i < m_threads.length; ++i) {
			m_threads[i].start();
		}
	}

	/**
	 * Shut down the thread pool.
	 */
	public void stop() {
		synchronized (this) {
			m_stopped = true;
		}

		m_threadGroup.interrupt();
	}

	/**
	 * Shut down the thread pool and wait until all the threads have stopped.
	 */
	public void stopAndWait() {

		stop();

		InterruptedException interrupted = null;

		for (int i = 0; i < m_threads.length; ++i) {
			while (m_threads[i] != Thread.currentThread()
					&& m_threads[i].isAlive()) {
				try {
					m_threads[i].join();
				} catch (InterruptedException e) {
					interrupted = e;
					// Try again.
				}
			}
		}

		if (interrupted != null) {
			throw new UncheckedInterruptedException(interrupted);
		}
	}

	/**
	 * Return whether stop has been called for this thread pool.
	 * 
	 * @return <code>true</code> => stop has been called.
	 */
	public boolean isStopped() {
		synchronized (this) {
			return m_stopped;
		}
	}

	/**
	 * Return the thread group used for our threads.
	 * 
	 * @return The thread group.
	 */
	public ThreadGroup getThreadGroup() {
		return m_threadGroup;
	}

	/**
	 * Factory that is called to create an {@link InterruptibleRunnable} for
	 * each thread.
	 */
	public interface InterruptibleRunnableFactory {

		/**
		 * @return The <code>InterruptibleRunnable</code>.
		 */
		InterruptibleRunnable create();
	}
}
