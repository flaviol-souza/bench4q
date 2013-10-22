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

/**
 * Class that manages the sending of messages to a server.
 */
public final class ClientSender extends StreamSender implements BlockingSender {

	/**
	 * Factory method that makes a TCP connection and returns a corresponding
	 * <code>Sender</code>.
	 * 
	 * @param connector
	 *            Connector to use to make the connection to the server.
	 * @return The ClientSender.
	 * @throws CommunicationException
	 *             If failed to connect.
	 */
	public static ClientSender connect(Connector connector)
			throws CommunicationException {

		return new ClientSender(new SocketWrapper(connector.connect()));
	}

	/**
	 * Factory method that makes <code>Sender</code> around the existing TCP
	 * connection owned by the supplied <code>ClientReceiver</code>.
	 * 
	 * @param clientReceiver
	 *            We create a paired <code>Sender</code> for this
	 *            <code>Receiver</code>.
	 * @return The ClientSender.
	 * @throws CommunicationException
	 *             If failed to connect.
	 */
	public static ClientSender connect(ClientReceiver clientReceiver)
			throws CommunicationException {

		return new ClientSender(clientReceiver.getSocketWrapper());
	}

	private final SocketWrapper m_socketWrapper;

	private ClientSender(SocketWrapper socketWrapper)
			throws CommunicationException {

		super(socketWrapper.getOutputStream());
		m_socketWrapper = socketWrapper;
	}

	/**
	 * Cleanly shutdown the <code>Sender</code>.
	 */
	public void shutdown() {
		// Close the socket wrapper first as that needs to use the socket.
		m_socketWrapper.close();

		super.shutdown();
	}

	/**
	 * Send the given message and await a response.
	 * 
	 * <p>
	 * The input stream is that of our socket. This method should only be used
	 * where the sender can guarantee that the input stream will be free for
	 * exclusive use - we don't lock out external processes from interrupting
	 * the stream.
	 * </p>
	 * 
	 * @param message
	 *            A {@link Message}.
	 * @return The response message.
	 * @throws CommunicationException
	 *             If an error occurs.
	 */
	public Message blockingSend(Message message) throws CommunicationException {
		final MessageRequiringResponse messageRequiringResponse = new MessageRequiringResponse(
				message);

		final Message result;

		synchronized (m_socketWrapper) {
			send(messageRequiringResponse);

			final Receiver receiver = new StreamReceiver(m_socketWrapper
					.getInputStream());

			result = receiver.waitForMessage();
		}

		if (result == null) {
			throw new CommunicationException("Shut down");
		} else if (result instanceof NoResponseMessage) {
			throw new NoResponseException("Server did not respond");
		}

		return result;
	}
}
