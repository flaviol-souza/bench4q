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

import java.io.OutputStream;

import org.bench4Q.common.communication.ResourcePool.Resource;
import org.bench4Q.common.util.thread.Executor;

/**
 * Manages the sending of messages to many TCP clients.
 * 
 */
public final class FanOutServerSender extends AbstractFanOutSender {

	/**
	 * Constructor.
	 * 
	 * @param acceptor
	 *            Acceptor.
	 * @param connectionType
	 *            Connection type.
	 * @param numberOfThreads
	 *            Number of sender threads to use.
	 * @throws Acceptor.ShutdownException
	 *             If the acceptor has been shutdown.
	 */
	public FanOutServerSender(Acceptor acceptor, ConnectionType connectionType,
			int numberOfThreads) throws Acceptor.ShutdownException {

		this(acceptor.getSocketSet(connectionType), new Executor(
				numberOfThreads));
	}

	/**
	 * Constructor.
	 * 
	 * @param acceptedSockets
	 *            Socket set.
	 * @param executor
	 *            A kernel to use.
	 * @throws CommunicationException
	 *             If server socket could not be bound.
	 */
	private FanOutServerSender(ResourcePool acceptedSockets, Executor executor) {

		super(executor, acceptedSockets);
	}

	/**
	 * Send a message to a particular address.
	 * 
	 * @param address
	 *            Address to send message to.
	 * @param message
	 *            The message.
	 * @exception CommunicationException
	 *                If an error occurs.
	 */
	public void send(Address address, Message message)
			throws CommunicationException {

		if (isShutdown()) {
			throw new CommunicationException("Shut down");
		}

		writeAddressedMessage(address, message);
	}

	/**
	 * Return an output stream from a socket resource.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The output stream.
	 * @throws CommunicationException
	 *             If the output stream could not be obtained from the socket.
	 */
	protected OutputStream resourceToOutputStream(ResourcePool.Resource resource)
			throws CommunicationException {

		// We don't need to synchronise access to the SocketWrapper;
		// access is protected through the socket set and only we hold
		// the reservation.
		return ((SocketWrapper) resource).getOutputStream();
	}

	/**
	 * Return the address of a socket.
	 * 
	 * @param resource
	 *            The resource.
	 * @return The address, or <code>null</code> if the socket has no address.
	 */
	protected Address getAddress(Resource resource) {

		// We don't need to synchronise access to the SocketWrapper;
		// access is protected through the socket set and only we hold
		// the reservation.
		return ((SocketWrapper) resource).getAddress();
	}
}
