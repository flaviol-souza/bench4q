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
 * QueuedSender implementation.
 * 
 */
public final class QueuedSenderDecorator implements QueuedSender {

	private final Sender m_delegate;
	private final MessageQueue m_messageQueue = new MessageQueue(false);

	/**
	 * Constructor.
	 * 
	 * @param delegate
	 *            Sender to decorate.
	 */
	public QueuedSenderDecorator(Sender delegate) {
		m_delegate = delegate;
	}

	/**
	 * First flush any pending messages queued with {@link #queue} and then send
	 * the given message.
	 * 
	 * @param message
	 *            A {@link Message}.
	 * @exception CommunicationException
	 *                If an error occurs.
	 */
	public void send(Message message) throws CommunicationException {
		synchronized (m_messageQueue.getMonitor()) {
			queue(message);
			flush();
		}
	}

	/**
	 * Queue the given message for later sending.
	 * 
	 * @param message
	 *            A {@link Message}.
	 * @exception CommunicationException
	 *                If an error occurs.
	 * @see #flush
	 * @see #send
	 */
	public void queue(Message message) throws CommunicationException {

		try {
			m_messageQueue.queue(message);
		} catch (ThreadSafeQueue.ShutdownException e) {
			throw new CommunicationException("Shut down");
		}
	}

	/**
	 * Flush any pending messages queued with {@link #queue}.
	 * 
	 * @exception CommunicationException
	 *                if an error occurs
	 */
	public void flush() throws CommunicationException {

		try {
			synchronized (m_messageQueue.getMonitor()) {
				while (true) {
					final Message message = m_messageQueue.dequeue(false);

					if (message == null) {
						break;
					}

					m_delegate.send(message);
				}
			}
		} catch (ThreadSafeQueue.ShutdownException e) {
			throw new CommunicationException("Shut down");
		}
	}

	/**
	 * Cleanly shutdown the <code>Sender</code>.
	 * 
	 * <p>
	 * Any queued messages are discarded.
	 * </p>
	 */
	public void shutdown() {
		m_messageQueue.shutdown();
		m_delegate.shutdown();
	}
}
