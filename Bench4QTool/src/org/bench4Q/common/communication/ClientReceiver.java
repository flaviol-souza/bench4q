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
 * Manages receipt of messages from a server over a TCP connection.
 */
public final class ClientReceiver extends StreamReceiver {

	/**
	 * Factory method that makes a TCP connection and returns a corresponding
	 * <code>Receiver</code>.
	 * 
	 * @param connector
	 *            Connector to use to make the connection to the server.
	 * @param address
	 *            The address of this ClientReceiver - can be used with
	 *            {@link FanOutServerSender#send(Address, Message)}.
	 * @return The ClientReceiver.
	 * @throws CommunicationException
	 *             If failed to connect.
	 */
	public static ClientReceiver connect(Connector connector, Address address)
			throws CommunicationException {

		return new ClientReceiver(new SocketWrapper(connector.connect(address)));
	}

	private final SocketWrapper m_socketWrapper;

	private ClientReceiver(SocketWrapper socketWrapper) {
		super(socketWrapper.getInputStream());
		m_socketWrapper = socketWrapper;
	}

	/**
	 * Cleanly shut down the <code>Receiver</code>.
	 */
	public void shutdown() {
		// Close the socket wrapper first as that needs to use the socket.
		m_socketWrapper.close();
		super.shutdown();
	}

	SocketWrapper getSocketWrapper() {
		return m_socketWrapper;
	}
}
