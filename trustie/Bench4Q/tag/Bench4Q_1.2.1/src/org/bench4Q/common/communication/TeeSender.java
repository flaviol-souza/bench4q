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
 * Passive {@link Sender} class that delegates to two other {@link Sender}s.
 * 
 */
public final class TeeSender implements Sender {

	private final Sender m_delegate1;
	private final Sender m_delegate2;

	/**
	 * Constructor.
	 * 
	 * @param delegate1
	 *            The first <code>Sender</code>.
	 * @param delegate2
	 *            The seconds <code>Sender</code>.
	 */
	public TeeSender(Sender delegate1, Sender delegate2) {
		m_delegate1 = delegate1;
		m_delegate2 = delegate2;
	}

	/**
	 * Send the given message.
	 * 
	 * @param message
	 *            A {@link Message}.
	 * @exception CommunicationException
	 *                If an error occurs.
	 */
	public void send(Message message) throws CommunicationException {
		m_delegate1.send(message);
		m_delegate2.send(message);
	}

	/**
	 * Cleanly shutdown the <code>Sender</code>.
	 */
	public void shutdown() {
		m_delegate1.shutdown();
		m_delegate2.shutdown();
	}
}
