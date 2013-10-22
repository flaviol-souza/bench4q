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

import java.net.InetAddress;

/**
 * Value object that represents the identity of an accepted connection.
 */
public final class ConnectionIdentity {

	private final InetAddress m_inetAddress;
	private final int m_port;
	private final long m_connectionTime;

	/**
	 * Constructor.
	 * 
	 * @param inetAddress
	 *            TCP address of connection.
	 * @param port
	 *            TCP port of connection.
	 * @param connectionTime
	 *            Connection time - milliseconds since the Epoch.
	 */
	ConnectionIdentity(InetAddress inetAddress, int port, long connectionTime) {
		m_inetAddress = inetAddress;
		m_port = port;
		m_connectionTime = connectionTime;
	}

	/**
	 * Hash code.
	 * 
	 * @return The hash code.
	 */
	public int hashCode() {
		return (int) m_connectionTime ^ m_port;
	}

	/**
	 * Equality.
	 * 
	 * @param o
	 *            Object to compare.
	 * @return <code>true</code> => its equal to this ConnectionIdentity.
	 */
	public boolean equals(Object o) {

		if (o == this) {
			return true;
		}

		if (o == null || o.getClass() != ConnectionIdentity.class) {
			return false;
		}

		final ConnectionIdentity other = (ConnectionIdentity) o;

		return m_connectionTime == other.m_connectionTime
				&& m_port == other.m_port
				&& m_inetAddress.equals(other.m_inetAddress);
	}
}
