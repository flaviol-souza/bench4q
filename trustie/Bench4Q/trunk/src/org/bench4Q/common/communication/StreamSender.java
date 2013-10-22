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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.bench4Q.common.Closer;

/**
 * Class that manages the sending of messages to a server.
 * 
 */
public class StreamSender extends AbstractSender {

	private final OutputStream m_outputStream;
	private final Object m_streamLock;

	/**
	 * Constructor.
	 * 
	 * @param outputStream
	 *            The output stream to write to.
	 */
	public StreamSender(OutputStream outputStream) {
		this(outputStream, outputStream);
	}

	/**
	 * Constructor.
	 * 
	 * @param outputStream
	 *            The output stream to write to.
	 * @param streamLock
	 *            Lock on this object around all stream operations.
	 */
	private StreamSender(OutputStream outputStream, Object streamLock) {
		m_outputStream = new BufferedOutputStream(outputStream);
		m_streamLock = streamLock;
	}

	/**
	 * Send a message.
	 * 
	 * @param message
	 *            The message.
	 * @throws IOException
	 *             If an error occurs.
	 */
	protected final void writeMessage(Message message) throws IOException {
		synchronized (m_streamLock) {
			writeMessageToStream(message, m_outputStream);
		}
	}

	/**
	 * Cleanly shutdown the <code>Sender</code>. Ignore most errors, connection
	 * has probably been reset by peer.
	 */
	public void shutdown() {

		super.shutdown();

		Closer.close(m_outputStream);
	}
}
