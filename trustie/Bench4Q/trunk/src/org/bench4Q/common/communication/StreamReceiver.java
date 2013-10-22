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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.bench4Q.common.Closer;
import org.bench4Q.common.UncheckedInterruptedException;

/**
 * Manages receipt of messages from a server over a stream.
 * 
 */
public class StreamReceiver implements Receiver {

	private final InputStream m_inputStream;
	private final Object m_streamLock;
	private boolean m_shutdown = false;

	/**
	 * Constructor.
	 * 
	 * @param inputStream
	 *            The input stream to read from.
	 */
	public StreamReceiver(InputStream inputStream) {
		this(inputStream, inputStream);
	}

	/**
	 * Constructor.
	 * 
	 * @param inputStream
	 *            The input stream to read from.
	 * @param streamLock
	 *            Lock on this object around all stream operations.
	 */
	private StreamReceiver(InputStream inputStream, Object streamLock) {
		m_inputStream = inputStream;
		m_streamLock = streamLock;
	}

	/**
	 * Block until a message is available. Typically called from a message
	 * dispatch loop.
	 * 
	 * @return The message or <code>null</code> if shut down.
	 * @throws CommunicationException
	 *             If an error occurred receiving a message.
	 */
	public final Message waitForMessage() throws CommunicationException {

		if (m_shutdown) {
			return null;
		}

		try {
			final Message message;

			// This blocks holding the lock, by design.
			synchronized (m_streamLock) {
				final ObjectInputStream objectStream = new ObjectInputStream(
						m_inputStream);

				message = (Message) objectStream.readObject();
			}

			if (message instanceof CloseCommunicationMessage) {
				shutdown();
				return null;
			}

			return message;
		} catch (IOException e) {
			UncheckedInterruptedException.ioException(e);
			throw new CommunicationException("Failed to read message", e);
		} catch (ClassNotFoundException e) {
			throw new CommunicationException("Failed to read message", e);
		}
	}

	/**
	 * Cleanly shut down the <code>Receiver</code>. Ignore errors, connection
	 * has probably been reset by peer.
	 */
	public void shutdown() {

		m_shutdown = true;

		Closer.close(m_inputStream);
	}
}
