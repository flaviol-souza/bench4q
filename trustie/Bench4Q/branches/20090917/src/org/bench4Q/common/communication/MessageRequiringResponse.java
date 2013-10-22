package org.bench4Q.common.communication;

/**
 * Pair a {@link Message} with a way to send a response.
 * 
 * <p>
 * Package scope
 * </p>.
 * 
 * @see ClientSender#blockingSend(Message)
 * @see MessageDispatchRegistry#set(Class, BlockingSender)
 * @author Philip Aston
 * @version $Revision: 3642 $
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
