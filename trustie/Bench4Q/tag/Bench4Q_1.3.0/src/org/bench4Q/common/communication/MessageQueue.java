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

package org.bench4Q.common.communication;

import org.bench4Q.common.util.thread.ThreadSafeQueue;

/**
 * Thread-safe queue of {@link Message}s.
 * 
 */
final class MessageQueue {

	private final ThreadSafeQueue m_queue = new ThreadSafeQueue();
	private final boolean m_passExceptions;

	/**
	 * Creates a new <code>MessageQueue</code> instance.
	 * 
	 * @param passExceptions
	 *            <code>true</code> => allow exceptions to be inserted into the
	 *            queue and rethrown to callers of {@link #dequeue}.
	 */
	public MessageQueue(boolean passExceptions) {
		m_passExceptions = passExceptions;
	}

	/**
	 * Queue the given message.
	 * 
	 * @param message
	 *            A {@link Message}.
	 * @exception ThreadSafeQueue.ShutdownException
	 *                If the queue has been shutdown.
	 * @see #shutdown
	 */
	public void queue(Message message) throws ThreadSafeQueue.ShutdownException {

		m_queue.queue(message);
	}

	/**
	 * Queue the given exception.
	 * 
	 * @param exception
	 *            An exception.
	 * @exception AssertionError
	 *                If the queue does not allow exceptions to be propagated..
	 * @exception ThreadSafeQueue.ShutdownException
	 *                If the queue has been shutdown.
	 * @see #shutdown
	 */
	public void queue(Exception exception)
			throws ThreadSafeQueue.ShutdownException {

		if (!m_passExceptions) {
			throw new AssertionError(
					"This MessageQueue does not allow Exceptions to be queued");
		}

		m_queue.queue(exception);
	}

	/**
	 * Dequeue a message.
	 * 
	 * @param block
	 *            <code>true</code> => block until message is available,
	 *            <code>false</code => return <code>null</code> if no message is
	 *            available.
	 * @return
	 * @exception CommunicationException
	 *                If the queue allows exceptions to be propagated, queued
	 *                CommunicationExceptions are rethrown to callers of this
	 *                method.
	 * @exception ThreadSafeQueue.ShutdownException
	 *                If the queue has been shutdown.
	 * @see #shutdown
	 */
	public Message dequeue(boolean block) throws CommunicationException,
			ThreadSafeQueue.ShutdownException {

		final Object result = m_queue.dequeue(block);

		if (m_passExceptions && result instanceof Exception) {
			final Exception e = (Exception) result;
			throw new CommunicationException(e.getMessage(), e);
		}

		return (Message) result;
	}

	/**
	 * Shutdown the <code>MessageQueue</code>. Any {@link Message}s in the queue
	 * are discarded.
	 */
	public void shutdown() {
		m_queue.shutdown();
	}

	/**
	 * Throw an ShutdownException if we are shutdown.
	 * 
	 * @throws ThreadSafeQueue.ShutdownException
	 *             Thrown if the <code>ThreadSafeQueue</code> is shutdown.
	 */
	public void checkIfShutdown() throws ThreadSafeQueue.ShutdownException {
		m_queue.checkIfShutdown();
	}

	/**
	 * Synchronise on this object to make multiple <code>MessageQueue</code>
	 * operations thread safe.
	 * 
	 * @return The object.
	 */
	public Object getMonitor() {
		return m_queue.getCondition();
	}
}
