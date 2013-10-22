package org.bench4Q.common.communication;

/**
 * Register of message handlers, keyed by message type.
 * 
 * <p>
 * The current implementation of this interface ({@link MessageDispatchSender})
 * does not interpret the message type polymorphically. That is, a message is
 * only passed to a handler if the handler was registered for the message's
 * class; messages registered for super classes are not invoked.
 * </p>
 * 
 * @author Philip Aston
 * @version $Revision:$
 */
public interface MessageDispatchRegistry {

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
	Sender set(Class messageType, Sender messageHandler);

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
	BlockingSender set(Class messageType, BlockingSender messageResponder);

	/**
	 * Register a message handler that is called if no other handler or
	 * responder is registered for the message type. There can be multiple such
	 * handlers.
	 * 
	 * @param messageHandler
	 *            The sender.
	 */
	void addFallback(Sender messageHandler);

	/**
	 * Most handlers ignore the shutdown event, so provide this as a convenient
	 * base for anonymous classes.
	 */
	public abstract static class AbstractHandler implements Sender {
		/**
		 * Ignore shutdown events.
		 */
		public void shutdown() {
		}
	}

	/**
	 * Most handlers ignore the shutdown event, so provide this as a convenient
	 * base for anonymous classes.
	 */
	public abstract static class AbstractBlockingHandler implements BlockingSender {

		/**
		 * Ignore shutdown events.
		 */
		public void shutdown() {
		}
	}
}
