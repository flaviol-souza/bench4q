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

import org.bench4Q.common.util.thread.InterruptibleRunnable;
import org.bench4Q.common.util.thread.ThreadPool;

/**
 * Active object that copies messages from a {@link Receiver} to a
 * {@link Sender}.
 */
public final class MessagePump {

	private final ThreadPool m_threadPool;
	private final Receiver m_receiver;
	private final Sender m_sender;
	private boolean m_shutdownTriggered = false;

	/**
	 * Constructor.
	 * 
	 * @param receiver
	 *            Receiver to read messages from.
	 * @param sender
	 *            Sender to send messages to.
	 * @param numberOfThreads
	 *            Number of worker threads to use. Order is not guaranteed if
	 *            more than one thread is used.
	 */
	public MessagePump(Receiver receiver, Sender sender, int numberOfThreads) {

		m_receiver = receiver;
		m_sender = sender;

		final ThreadPool.InterruptibleRunnableFactory runnableFactory = new ThreadPool.InterruptibleRunnableFactory() {
			public InterruptibleRunnable create() {
				return new MessagePumpRunnable();
			}
		};

		m_threadPool = new ThreadPool("Message pump", numberOfThreads,
				runnableFactory);

		m_threadPool.start();
	}

	/**
	 * Shut down the MessagePump.
	 * 
	 */
	public void shutdown() {

		if (!m_shutdownTriggered) {
			// Guard against repeat invocations due to a shutdown action
			// triggering a CommunicationException.
			m_shutdownTriggered = true;

			m_receiver.shutdown();
			m_sender.shutdown();

			// Now wait for the thread pool to finish.
			m_threadPool.stopAndWait();
		}
	}

	private class MessagePumpRunnable implements InterruptibleRunnable {
		public void interruptibleRun() {
			try {
				while (!m_threadPool.isStopped()) {
					final Message message = m_receiver.waitForMessage();

					if (message == null) {
						shutdown();
					} else {
						m_sender.send(message);
					}
				}
			} catch (CommunicationException e) {
				// Shutting down.
			} finally {
				shutdown();
			}
		}
	}
}
