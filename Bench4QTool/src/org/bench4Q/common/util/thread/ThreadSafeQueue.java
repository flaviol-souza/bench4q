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

import java.util.LinkedList;

import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.UncheckedInterruptedException;

/**
 * Thread-safe queue of <code>Objects</code>.
 */
public final class ThreadSafeQueue {

	private final LinkedList m_messages = new LinkedList();
	private final Condition m_condition = new Condition();
	private volatile boolean m_shutdown = false;

	/**
	 * Constructor.
	 * 
	 */
	public ThreadSafeQueue() {
	}

	/**
	 * Queue the given Object.
	 * 
	 * @param item
	 *            The object.
	 * @exception ShutdownException
	 *                If the queue has been shutdown.
	 * @see #shutdown
	 */
	public void queue(Object item) throws ShutdownException {
		synchronized (getCondition()) {
			checkIfShutdown();
			m_messages.add(item);
			getCondition().notifyAll();
		}
	}

	/**
	 * Dequeue an Object.
	 * 
	 * @param block
	 *            <code>true</code> => block until message is available,
	 *            <code>false</code => return <code>null</code> if no message is
	 *            available.
	 * @exception ShutdownException
	 *                If the queue has been shutdown.
	 * @return The dequeued object.
	 * @see #shutdown
	 */
	public Object dequeue(boolean block) throws ShutdownException {
		synchronized (getCondition()) {
			while (!m_shutdown && block && m_messages.size() == 0) {
				try {
					getCondition().wait();
				} catch (InterruptedException e) {
					// Don't leave other threads dangling.
					shutdown();
					throw new UncheckedInterruptedException(e);
				}
			}

			checkIfShutdown();

			if (m_messages.size() == 0) {
				return null;
			} else {
				getCondition().notifyAll();
				return m_messages.removeFirst();
			}
		}
	}

	/**
	 * Shutdown the <code>ThreadSafeQueue</code>. Any <code>Objects</code> in
	 * the queue are discarded.
	 */
	public void shutdown() {
		synchronized (getCondition()) {
			m_shutdown = true;
			m_messages.clear();
			getCondition().notifyAll();
		}
	}

	/**
	 * Wait until the queue is empty.
	 */
	public void gracefulShutdown() {
		synchronized (getCondition()) {
			while (getSize() > 0) {
				getCondition().waitNoInterrruptException();
			}

			shutdown();
		}
	}

	/**
	 * Get the lock object which is used to control and notify changes to the
	 * queue.
	 * 
	 * @return The lock object.
	 */
	public Condition getCondition() {
		return m_condition;
	}

	/**
	 * The size of the queue.
	 * 
	 * @return The size of the queue.
	 */
	public int getSize() {
		synchronized (getCondition()) {
			return m_messages.size();
		}
	}

	/**
	 * Throw an ShutdownException if we are shutdown.
	 * 
	 * @throws ShutdownException
	 *             Thrown if the <code>ThreadSafeQueue</code> is shutdown.
	 */
	public void checkIfShutdown() throws ShutdownException {
		if (m_shutdown) {
			throw new ShutdownException("ThreadSafeQueue shutdown");
		}
	}

	/**
	 * Exception that indicates <code>ThreadSafeQueue</code> has been shutdown.
	 */
	public static final class ShutdownException extends Bench4QException {
		private ShutdownException(String s) {
			super(s);
		}
	}
}
