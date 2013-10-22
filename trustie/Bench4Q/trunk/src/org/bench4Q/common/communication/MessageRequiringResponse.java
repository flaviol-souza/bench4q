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
 * Pair a {@link Message} with a way to send a response.
 * 
 * <p>
 * Package scope
 * </p>
 * .
 * 
 * @see ClientSender#blockingSend(Message)
 * @see MessageDispatchRegistry#set(Class, BlockingSender)
 */
final class MessageRequiringResponse implements Message {

	private static final long serialVersionUID = 1L;

	private final Message m_message;

	private transient Sender m_responder;
	private transient boolean m_responseSent;

	MessageRequiringResponse(Message message) {
		m_message = message;
	}

	/**
	 * Provide access to the original message.
	 * 
	 * @return The message.
	 */
	public Message getMessage() {
		return m_message;
	}

	void setResponder(Sender sender) {
		m_responder = sender;
	}

	/**
	 * Send the response.
	 * 
	 * @param message
	 *            A {@link Message}.
	 * @throws CommunicationException
	 *             If an error occurs.
	 */
	public void sendResponse(Message message) throws CommunicationException {

		if (m_responder == null) {
			throw new CommunicationException("Response sender not set");
		}

		if (m_responseSent) {
			throw new CommunicationException("One response message only");
		}

		m_responseSent = true;
		m_responder.send(message);
	}

	/**
	 * Query whether the response has been sent.
	 * 
	 * @return <code>true</code> if and only if the response has been sent.
	 */
	public boolean isResponseSent() {
		return m_responseSent;
	}
}
