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

import org.bench4Q.common.UncheckedInterruptedException;

/**
 * Constants that are used to discriminate between different types of
 * connections.
 * 
 */
public final class ConnectionType {

	/** Connection type constant. */
	public static final ConnectionType AGENT = new ConnectionType(0,
			"AGENT connection type");

	/** Connection type constant. */
	public static final ConnectionType WORKER = new ConnectionType(1,
			"WORKER connection type");

	/** Connection type constant. */
	public static final ConnectionType CONSOLE_CLIENT = new ConnectionType(2,
			"CONSOLE_CLIENT connection type");

	/**
	 * Serialisation method that reads a ConnectionType from a stream. Package
	 * scope.
	 * 
	 * @param in
	 *            The stream.
	 * @return The ConnectionType.
	 * @throws CommunicationException
	 */
	static ConnectionType read(InputStream in) throws CommunicationException {

		final int i;

		try {
			i = in.read();
		} catch (IOException e) {
			UncheckedInterruptedException.ioException(e);
			throw new CommunicationException("Failed to read connection type",
					e);
		}

		switch (i) {
		case 0:
			return ConnectionType.AGENT;

		case 1:
			return ConnectionType.WORKER;

		case 2:
			return ConnectionType.CONSOLE_CLIENT;

		default:
			throw new CommunicationException("Unknown connection type (" + i
					+ ")");
		}
	}

	private final int m_identity;
	private final String m_description;

	private ConnectionType(int identity, String description) {
		m_identity = identity;
		m_description = description;
	}

	/**
	 * Serialisation method that writes a ConnectionType to a stream. Package
	 * scope.
	 * 
	 * @param out
	 *            The stream.
	 * @throws CommunicationException
	 *             If write failed.
	 */
	void write(OutputStream out) throws CommunicationException {
		try {
			out.write(m_identity);
			out.flush();
		} catch (IOException e) {
			UncheckedInterruptedException.ioException(e);
			throw new CommunicationException("Write failed", e);
		}
	}

	/**
	 * Implement {@link Object#hashCode}.
	 * 
	 * @return The hash code.
	 */
	public int hashCode() {
		return m_identity;
	}

	/**
	 * Equality.
	 * 
	 * @param other
	 *            An <code>Object</code> to compare.
	 * @return <code>true</code> => <code>other</code> is equal to this object.
	 */
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}

		if (other == null || other.getClass() != ConnectionType.class) {
			return false;
		}

		final ConnectionType otherConnectionType = (ConnectionType) other;
		return m_identity == otherConnectionType.m_identity;
	}

	/**
	 * Describe ourself.
	 * 
	 * @return The description.
	 */
	public String toString() {
		return m_description;
	}
}
