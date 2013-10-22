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

package org.bench4Q.agent.communication;

import org.bench4Q.agent.AgentIdentityImplementation;
import org.bench4Q.agent.FileStore;
import org.bench4Q.agent.messages.CollectResultMessage;
import org.bench4Q.agent.messages.StartJASptEMessage;
import org.bench4Q.agent.messages.StopJASptEMessage;
import org.bench4Q.common.communication.CommunicationException;
import org.bench4Q.common.communication.Message;
import org.bench4Q.common.communication.MessageDispatchRegistry;
import org.bench4Q.common.communication.Sender;
import org.bench4Q.common.util.thread.Condition;

/**
 * Process console messages and allows them to be asynchronously queried.
 * 
 */
public final class ConsoleListener {

	/**
	 * Constant that represents start message.
	 * 
	 * @see #received
	 */
	public static final int START = 1 << 0;                 //  START = 1

	/**
	 * Constant that represents a a reset message.
	 * 
	 * @see #received
	 */
	public static final int STOP = 1 << 1;                  //  STOP = 10

	/**
	 * Constant that represents a stop message.
	 * 
	 * @see #received
	 */
	public static final int COLLECT = 1 << 2;               //   COLLECT = 100

	/**
	 * Constant that represents a communication shutdown.
	 * 
	 * @see #received
	 */
	public static final int SHUTDOWN = 1 << 3;              //   SHUTDOWN = 1000

	/**
	 * Constant that represent any message.
	 * 
	 * @see #received
	 */
	public static final int ANY = START | COLLECT | STOP | SHUTDOWN;      //  ANY = 1111

	private final Condition m_notifyOnMessage;
	private int m_messagesReceived = 0;
	private int m_lastMessagesReceived = 0;
	private StartJASptEMessage m_startJASptEMessage;
	private StopJASptEMessage m_stopJASptEMessage;
	private CollectResultMessage m_collectResultMessage;
	private final AgentIdentityImplementation m_agentIdentity;
	private volatile FileStore m_fileStore;

	private boolean TestFinished = false;

	/**
	 * @param TestFinished
	 */
	public void setTestFinished(boolean TestFinished) {
		this.TestFinished = TestFinished;
	}

	/**
	 * @param fileStore
	 */
	public void setFileStore(FileStore fileStore) {
		m_fileStore = fileStore;
	}

	/**
	 * ConstfileStoreructor.
	 * 
	 * @param notifyOnMessage
	 *            An <code>Object</code> to notify when a message arrives.
	 * @param agentIdentity
	 */
	public ConsoleListener(Condition notifyOnMessage,
			AgentIdentityImplementation agentIdentity) {
		m_notifyOnMessage = notifyOnMessage;

		m_agentIdentity = agentIdentity;
	}

	/**
	 * Shut down.
	 */
	public void shutdown() {
		setReceived(SHUTDOWN);
	}

	/**
	 * Wait until any message is received.
	 * 
	 * <p>
	 * After calling this method, the actual messages can be determined using
	 * {@link #received}.
	 * </p>
	 * 
	 */
	public void waitForMessage() {
		while (!checkForMessage(ConsoleListener.ANY)) {
			synchronized (m_notifyOnMessage) {
				m_notifyOnMessage.waitNoInterrruptException();
			}
		}
	}

	/**
	 * Check for messages matching the given mask.
	 * 
	 * <p>
	 * After calling this method, the actual messages can be determined using
	 * {@link #received}.
	 * </p>
	 * 
	 * @param mask
	 *            The messages to check for.
	 * @return <code>true</code> if at least one message matches the
	 *         <code>mask</code> parameter has been received since the last time
	 *         the message was checked for, or if communications have been
	 *         shutdown. <code>false</code> otherwise.
	 */
	public boolean checkForMessage(int mask) {
		synchronized (this) {
			final int intersection = m_messagesReceived & mask;

			try {
				m_lastMessagesReceived = intersection;
			} finally {
				m_messagesReceived ^= intersection;
			}
		}

		return received(mask | SHUTDOWN);
	}

	/**
	 * Discard pending messages that match the given mask.
	 * 
	 * @param mask
	 *            The messages to discard.
	 */
	public void discardMessages(int mask) {
		synchronized (this) {
			m_lastMessagesReceived &= ~mask;
			m_messagesReceived &= ~mask;
		}
	}

	/**
	 * Query the messages set up by the last {@link #checkForMessage} or
	 * {@link #waitForMessage} call.
	 * 
	 * @param mask
	 *            The messages to check for.
	 * @return <code>true</code> if one or more of the received messages matches
	 *         <code>mask</code>.
	 */
	public synchronized boolean received(int mask) {
		return (m_lastMessagesReceived & mask) != 0;
	}

	private void setReceived(int message) {
		synchronized (this) {
			m_messagesReceived |= message;
		}

		synchronized (m_notifyOnMessage) {
			m_notifyOnMessage.notifyAll();
		}
	}

	/**
	 * Registers message handlers with a dispatcher.
	 * 
	 * @param messageDispatcher
	 *            The dispatcher.
	 */

	public void registerMessageHandlers(
			MessageDispatchRegistry messageDispatcher) {

		messageDispatcher.set(StartJASptEMessage.class,
				new AbstractMessageHandler() {
					public void send(Message message)
							throws CommunicationException {
						// m_logger.output("received a start message");
						m_startJASptEMessage = (StartJASptEMessage) message;
						setReceived(START);
					}
				});

		messageDispatcher.set(StopJASptEMessage.class,
				new AbstractMessageHandler() {
					public void send(Message message)
							throws CommunicationException {
						// m_logger.output("received a stop message");
						m_stopJASptEMessage = (StopJASptEMessage) message;
						setReceived(STOP);
					}
				});

		messageDispatcher.set(CollectResultMessage.class,
				new AbstractMessageHandler() {
					public void send(Message message)
							throws CommunicationException {
						// m_logger.output("received a collecting result message");
						m_collectResultMessage = (CollectResultMessage) message;
						setReceived(COLLECT);

					}
				});
	}

	/**
	 * Return the last {@link StartJASptEMessage} received.
	 * 
	 * @return The message.
	 */
	public StartJASptEMessage getLastStartJASptEMessage() {
		return m_startJASptEMessage;
	}

	/**
	 * @return the last {@link StopJASptEMessage} received.
	 */
	public StopJASptEMessage getLastStopJASptEMessage() {
		return m_stopJASptEMessage;
	}

	/**
	 * @return the last {@link CollectResultMessage} received.
	 */
	public CollectResultMessage getLastCollectResultMessage() {
		return m_collectResultMessage;
	}

	private abstract class AbstractMessageHandler implements Sender {
		public void shutdown() {
			final boolean shutdown;

			synchronized (ConsoleListener.this) {
				shutdown = (m_messagesReceived & SHUTDOWN) == 0;
			}

			if (shutdown) {
				// m_logger.output("communication shut down", Logger.LOG);
				setReceived(SHUTDOWN);
			}
		}
	}
}
