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
 * Interface for classes that manage the sending of messages.
 */
public interface BlockingSender {

	/**
	 * Send the given message and await a response.
	 * 
	 * <p>
	 * The input stream is implementation dependent. This should only be used
	 * where the sender can guarantee that the input stream will be free for
	 * exclusive use.
	 * </p>
	 * 
	 * @param message
	 *            A {@link Message}.
	 * @return The response message.
	 * @throws CommunicationException
	 *             If an error occurs.
	 */
	Message blockingSend(Message message) throws CommunicationException;

	/**
	 * Cleanly shut down the <code>Sender</code>.
	 */
	void shutdown();

	/**
	 * Exception indicating that the server chose to send no response back to a
	 * {@link BlockingSender#blockingSend(Message)}.
	 * 
	 * @author Philip Aston
	 * @version $Revision: 3642 $
	 */
	class NoResponseException extends CommunicationException {

		NoResponseException(String s) {
			super(s);
		}
	}
}
