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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.bench4Q.common.UncheckedInterruptedException;

/**
 * Connection factory.
 */
public final class Connector {

	private final String m_hostString;
	private final int m_port;
	private final ConnectionType m_connectionType;

	/**
	 * Constructor.
	 * 
	 * @param hostString
	 *            TCP address to connect to.
	 * @param port
	 *            TCP port to connect to.
	 * @param connectionType
	 *            Connection type.
	 */
	public Connector(String hostString, int port, ConnectionType connectionType) {
		m_hostString = hostString;
		m_port = port;
		m_connectionType = connectionType;
	}

	/**
	 * Factory method that makes a TCP connection and returns a corresponding
	 * socket.
	 * 
	 * @return A socket wired to the connection.
	 * @throws CommunicationException
	 *             If connection could not be establish.
	 */
	Socket connect() throws CommunicationException {
		return connect(null);
	}

	Socket connect(Address address) throws CommunicationException {
		final InetAddress inetAddress;

		try {
			inetAddress = InetAddress.getByName(m_hostString);
		} catch (UnknownHostException e) {
			throw new CommunicationException("Could not resolve host '"
					+ m_hostString + '\'', e);
		}

		try {
			// Bind to any local port.
			final Socket socket = new Socket(inetAddress, m_port);

			final OutputStream outputStream = socket.getOutputStream();
			m_connectionType.write(outputStream);

			final ObjectOutputStream objectStream = new ObjectOutputStream(
					outputStream);
			objectStream.writeObject(address);
			objectStream.flush();
			return socket;
		} catch (IOException e) {
			UncheckedInterruptedException.ioException(e);
			throw new CommunicationException("Failed to connect to '"
					+ inetAddress + ':' + m_port + '\'', e);
		}
	}

	/**
	 * Equality.
	 * 
	 * @return Hash code.
	 */
	public int hashCode() {
		return m_hostString.hashCode() ^ m_port ^ m_connectionType.hashCode();
	}

	/**
	 * Equality.
	 * 
	 * @param o
	 *            Object to compare.
	 * @return <code>true</code> => its equal to this <code>Connector</code>.
	 */
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o == null || o.getClass() != Connector.class) {
			return false;
		}

		final Connector other = (Connector) o;

		return m_port == other.m_port
				&& m_connectionType.equals(other.m_connectionType)
				&& m_hostString.equals(other.m_hostString);
	}

	/**
	 * Return a description of the connection address.
	 * 
	 * @return The description.
	 */
	public String getEndpointAsString() {
		String host;

		try {
			host = InetAddress.getByName(m_hostString).toString();
		} catch (UnknownHostException e) {
			host = m_hostString;
		}

		return host + ":" + m_port;
	}

	/**
	 * Connection details read from a stream.
	 * 
	 * @see Connector#read
	 */
	static final class ConnectDetails {
		private final ConnectionType m_connectionType;
		private final Address m_address;

		private ConnectDetails(ConnectionType connectionType, Address address) {
			m_connectionType = connectionType;
			m_address = address;
		}

		public ConnectionType getConnectionType() {
			return m_connectionType;
		}

		public Address getAddress() {
			return m_address;
		}
	}

	/**
	 * Read connection details from a stream.
	 * 
	 * @param in
	 *            The stream.
	 * @return The details.
	 * @throws CommunicationException
	 *             If the details could not be read.
	 */
	static ConnectDetails read(InputStream in) throws CommunicationException {
		final ConnectionType type = ConnectionType.read(in);

		try {
			final Address address = (Address) new ObjectInputStream(in)
					.readObject();
			return new ConnectDetails(type, address);
		} catch (IOException e) {
			throw new CommunicationException("Could not read address details",
					e);
		} catch (ClassNotFoundException e) {
			throw new CommunicationException("Could not read address details",
					e);
		}
	}
}
