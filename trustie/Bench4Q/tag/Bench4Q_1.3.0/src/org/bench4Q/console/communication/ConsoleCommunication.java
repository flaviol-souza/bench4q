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

package org.bench4Q.console.communication;

import org.bench4Q.common.communication.Address;
import org.bench4Q.common.communication.Message;
import org.bench4Q.common.communication.MessageDispatchRegistry;

/**
 * Handles communication for the console.
 */
public interface ConsoleCommunication {

	/**
	 * Returns the message dispatch registry which callers can use to register
	 * new message handlers.
	 * 
	 * @return The registry.
	 */
	MessageDispatchRegistry getMessageDispatchRegistry();

	/**
	 * Shut down communication.
	 */
	void shutdown();

	/**
	 * Wait to receive a message, then process it.
	 * 
	 * @return <code>true</code> if we processed a message successfully;
	 *         <code>false</code> if we've been shut down.
	 * @see #shutdown()
	 */
	boolean processOneMessage();

	/**
	 * Send the given message to the agent processes (which may pass it on to
	 * their workers).
	 * 
	 * @param message
	 *            The message to send.
	 */
	void sendToAgents(Message message);

	/**
	 * Send the given message to the given agent processes (which may pass it on
	 * to its workers).
	 * 
	 * @param address
	 *            The address to which the message should be sent.
	 * @param message
	 *            The message to send.
	 */
	void sendToAddressedAgents(Address address, Message message);

	/**
	 * The number of connections that have been accepted. Used by the unit
	 * tests.
	 * 
	 * @return The number of accepted connections.
	 */
	int getNumberOfConnections();
}
