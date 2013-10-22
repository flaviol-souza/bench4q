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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.bench4Q.common.Closer;
import org.bench4Q.common.util.ListenerSupport;

/**
 * Wrapper for a {@link Socket} that is {ResourcePool.ResourcePool} and
 * understands our connection close protocol.
 * 
 * <p>
 * Client classes that access the sockets streams through
 * {@link #getInputStream} or {@link #getOutputStream}, and that do not
 * otherwise know they have exclusive access, should synchronise on the
 * particular stream object while they use it.
 * </p>
 * 
 */
final class SocketWrapper implements ResourcePool.Resource {

	private final Socket m_socket;
	private final ConnectionIdentity m_connectionIdentity;
	private final InputStream m_inputStream;
	private final OutputStream m_outputStream;

	private final ListenerSupport m_closedListeners = new ListenerSupport();

	private final ListenerSupport.Informer m_closedInformer = new ListenerSupport.Informer() {
		public void inform(Object listener) {
			((ClosedListener) listener).socketClosed();
		}
	};

	private Address m_address;

	/**
	 * Constructor.
	 * 
	 * @param socket
	 *            Socket to wrap. If the caller maintains any references to the
	 *            socket, if should synchronise access to the socket streams as
	 *            described in {@link SocketWrapper}.
	 * @throws CommunicationException
	 *             If an error occurred.
	 */
	public SocketWrapper(Socket socket) throws CommunicationException {
		m_socket = socket;

		try {
			m_inputStream = m_socket.getInputStream();
			m_outputStream = m_socket.getOutputStream();

			m_connectionIdentity = new ConnectionIdentity(m_socket
					.getInetAddress(), m_socket.getPort(), System
					.currentTimeMillis());
		} catch (IOException e) {
			Closer.close(m_socket);

			throw new CommunicationException(
					"Could not establish communication", e);
		}
	}

	/**
	 * Close the SocketWrapper and its underlying resources.
	 * 
	 * <p>
	 * No need to synchronise access to the close, isClosed - they should be
	 * thread safe. Also, we're careful not to hold locks around the listener
	 * notification.
	 * </p>
	 */
	public void close() {
		if (!m_socket.isClosed()) {
			// Java provides no way for socket code to enquire whether the
			// peer has closed the connection. We make an effort to tell the
			// peer.
			synchronized (m_outputStream) {
				new StreamSender(m_outputStream).shutdown();
			}

			Closer.close(m_socket);

			// Close before informing listeners to prevent recursion.
			m_closedListeners.apply(m_closedInformer);
		}
	}

	public ConnectionIdentity getConnectionIdentity() {
		return m_connectionIdentity;
	}

	/**
	 * See note in {@link SocketWrapper} class documentation about the need to
	 * synchronise around any usage of the returned <code>InputStream</code>.
	 * 
	 * @return The input stream.
	 */
	public InputStream getInputStream() {
		return m_inputStream;
	}

	/**
	 * See note in {@link SocketWrapper} class documentation about the need to
	 * synchronise around any usage of the returned <code>OutputStream</code>.
	 * 
	 * @return The output stream.
	 */
	public OutputStream getOutputStream() {
		return m_outputStream;
	}

	/**
	 * Socket event notification interface.
	 */
	public interface ClosedListener {
		void socketClosed();
	}

	public void addClosedListener(ClosedListener listener) {
		m_closedListeners.add(listener);
	}

	/**
	 * Set an external object that identifies this socket. This can be used in
	 * conjunction with {@link FanOutServerSender#send} to address a particular
	 * target.
	 * 
	 * @param address
	 *            The address. We only care about its equality semantics.
	 */
	public void setAddress(Address address) {
		m_address = address;
	}

	/**
	 * Return the address for this socket.
	 * 
	 * @return The address, or <code>null</code> if no address has been set.
	 */
	public Address getAddress() {
		return m_address;
	}
}
