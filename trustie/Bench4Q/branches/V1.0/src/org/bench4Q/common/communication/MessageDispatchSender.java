package org.bench4Q.common.communication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bench4Q.common.util.ListenerSupport;

/**
 * Passive {@link Sender}class that dispatches incoming messages to the
 * appropriate handler.
 * 
 * @author Philip Aston
 * @version $Revision: 3762 $
 */
public final class MessageDispatchSender implements Sender, MessageDispatchRegistry {

	/* Guarded by m_handlers. */
	private final Map m_handlers = Collections.synchronizedMap(new HashMap());

	/* Guarded by m_responders. */
	private final Map m_responders = Collections.synchronizedMap(new HashMap());

	private final ListenerSupport m_fallbackHandlers = new ListenerSupport();

	/**
	 * Register a message handler.
	 * 
	 * @param messageType
	 *            Messages of this type will be routed to the handler.
	 * @param messageHandler
	 *            The message handler.
	 * 
	 * @return The previous message handler registered for
	 *         <code>messageType</code> or <code>null</code>.
	 */
	public Sender set(Class messageType, Sender messageHandler) {
		return (Sender) m_handlers.put(messageType, messageHandler);
	}

	/**
	 * Register a message responder.
	 * 
	 * @param messageType
	 *            Messages of this type will be routed to the handler.
	 * @param messageResponder
	 *            The message responder.
	 * 
	 * @return The previous message handler registered for
	 *         <code>messageType</code> or <code>null</code>.
	 */
	public BlockingSender set(Class messageType, BlockingSender messageResponder) {
		return (BlockingSender) m_responders.put(messageType, messageResponder);
	}

	/**
	 * Register a message handler that is called if no other handler or
	 * responder is registered for the message type.
	 * 
	 * @param messageHandler
	 *            The sender.
	 */
	public void addFallback(Sender messageHandler) {
		m_fallbackHandlers.add(messageHandler);
	}

	/**
	 * Sends a message to each handler until one claims to have handled the
	 * message.
	 * 
	 * @param message
	 *            The message.
	 * @throws CommunicationException
	 *             If one of the handlers failed.
	 */
	public void send(final Message message) throws CommunicationException {

		if (message instanceof MessageRequiringResponse) {
			final MessageRequiringResponse messageRequringResponse = (MessageRequiringResponse) message;

			final Message requestMessage = messageRequringResponse.getMessage();

			final BlockingSender responder = (BlockingSender) m_responders.get(requestMessage
					.getClass());

			if (responder != null) {
				messageRequringResponse.sendResponse(responder.blockingSend(requestMessage));
				return;
			}
		} else {
			final Sender handler = (Sender) m_handlers.get(message.getClass());

			if (handler != null) {
				handler.send(message);
				return;
			}
		}

		final CommunicationException[] exception = new CommunicationException[1];

		m_fallbackHandlers.apply(new ListenerSupport.Informer() {
			public void inform(Object listener) {
				try {
					((Sender) listener).send(message);
				} catch (CommunicationException e) {
					exception[0] = e;
				}
			}
		});

		if (message instanceof MessageRequiringResponse) {
			final MessageRequiringResponse messageRequringResponse = (MessageRequiringResponse) message;

			if (!messageRequringResponse.isResponseSent()) {
				// No one responded.
				messageRequringResponse.sendResponse(new NoResponseMessage());
			}
		}

		if (exception[0] != null) {
			throw exception[0];
		}
	}

	/**
	 * Shutdown all our handlers.
	 */
	public void shutdown() {
		final Sender[] handlers;

		synchronized (m_handlers) {
			handlers = (Sender[]) m_handlers.values().toArray(new Sender[m_handlers.size()]);
		}

		for (int i = 0; i < handlers.length; ++i) {
			handlers[i].shutdown();
		}

		final BlockingSender[] responders;

		synchronized (m_responders) {
			responders = (BlockingSender[]) m_responders.values().toArray(
					new BlockingSender[m_responders.size()]);
		}

		for (int i = 0; i < responders.length; ++i) {
			responders[i].shutdown();
		}

		m_fallbackHandlers.apply(new ListenerSupport.Informer() {
			public void inform(Object listener) {
				((Sender) listener).shutdown();
			}
		});
	}
}
