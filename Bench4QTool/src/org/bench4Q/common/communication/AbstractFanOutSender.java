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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.bench4Q.common.communication.ResourcePool.Resource;
import org.bench4Q.common.util.thread.Executor;
import org.bench4Q.common.util.thread.InterruptibleRunnable;

/**
 * Manages the sending of messages to many Receivers.
 */
abstract class AbstractFanOutSender extends AbstractSender {

	private final Executor m_executor;
	private final ResourcePool m_resourcePool;

	/**
	 * Constructor.
	 * 
	 * @param executor
	 *            Executor to use.
	 * @param resourcePool
	 *            Pool of resources from which the output streams can be
	 *            reserved.
	 */
	protected AbstractFanOutSender(Executor executor, ResourcePool resourcePool) {
		m_executor = executor;
		m_resourcePool = resourcePool;
	}

	/**
	 * Send a message.
	 * 
	 * @param message
	 *            The message.
	 * @exception IOException
	 *                If an error occurs.
	 */
	protected final void writeMessage(final Message message)
			throws CommunicationException {
		writeAddressedMessage(new SendToEveryoneAddress(), message);
	}

	/**
	 * Send a message.
	 * 
	 * @param address
	 * @param message
	 *            The message.
	 * @throws CommunicationException
	 * @exception IOException
	 *                If an error occurs.
	 */
	protected final void writeAddressedMessage(Address address, Message message)
			throws CommunicationException {

		try {
			// We reserve all the resources here and hand off the
			// reservations to WriteMessageToStream instances. This
			// guarantees order of messages to a given resource for this
			// AbstractFanOutSender.
			final Iterator iterator = m_resourcePool.reserveAll().iterator();

			while (iterator.hasNext()) {
				final ResourcePool.Reservation reservation = (ResourcePool.Reservation) iterator
						.next();

				final Resource resource = reservation.getResource();

				if (!address.includes(getAddress(resource))) {
					reservation.free();
					continue;
				}

				// We don't need to synchronise access to the stream; access is
				// protected through the socket set and only we hold the
				// reservation.
				m_executor.execute(new WriteMessageToStream(message,
						resourceToOutputStream(resource), reservation));
			}
		} catch (Executor.ShutdownException e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * Subclasses must implement this to return an output stream from a
	 * resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The output stream.
	 * @throws CommunicationException
	 *             If the output stream could not be obtained from the resource.
	 */
	protected abstract OutputStream resourceToOutputStream(
			ResourcePool.Resource resource) throws CommunicationException;

	/**
	 * Subclasses must implement this to return the address associated with a
	 * resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The address, or <code>null</code> if the resource has no address.
	 */
	protected abstract Address getAddress(Resource resource);

	/**
	 * Allow subclasses to access the resource pool.
	 * 
	 * @return The resource pool.
	 */
	protected final ResourcePool getResourcePool() {
		return m_resourcePool;
	}

	/**
	 * Shut down this sender.
	 */
	public void shutdown() {
		super.shutdown();

		m_executor.gracefulShutdown();
	}

	private static final class SendToEveryoneAddress implements Address {
		public boolean includes(Address address) {
			return true;
		}
	}

	private static final class WriteMessageToStream implements
			InterruptibleRunnable {

		private final Message m_message;
		private final OutputStream m_outputStream;
		private final ResourcePool.Reservation m_reservation;

		public WriteMessageToStream(Message message, OutputStream outputStream,
				ResourcePool.Reservation reservation) {
			m_message = message;
			m_outputStream = outputStream;
			m_reservation = reservation;
		}

		public void interruptibleRun() {
			try {
				writeMessageToStream(m_message, m_outputStream);
			} catch (IOException e) {
				// InterruptedIOExceptions take this path.
				m_reservation.close();
			} finally {
				m_reservation.free();
			}
		}
	}
}
