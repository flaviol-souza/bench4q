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

package org.bench4Q.console.client;

import org.bench4Q.common.communication.ClientSender;
import org.bench4Q.common.communication.CommunicationException;
import org.bench4Q.common.communication.ConnectionType;
import org.bench4Q.common.communication.Connector;

/**
 * Something that can create {@link ConsoleConnection} instances.
 */
public class ConsoleConnectionFactory {

	/**
	 * Create a {@link ConsoleConnection}.
	 * 
	 * @param host
	 *            Console host.
	 * @param port
	 *            Console port.
	 * @return The {@link ConsoleConnection}.
	 * @throws ConsoleConnectionException
	 *             Failed to establish a connection.
	 */
	public ConsoleConnection connect(String host, int port)
			throws ConsoleConnectionException {

		try {
			return new ConsoleConnectionImplementation(ClientSender
					.connect(new Connector(host, port,
							ConnectionType.CONSOLE_CLIENT)));
		} catch (CommunicationException e) {
			throw new ConsoleConnectionException("Failed to connect", e);
		}
	}
}
