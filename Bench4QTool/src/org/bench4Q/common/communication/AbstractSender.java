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
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.bench4Q.common.UncheckedInterruptedException;

/**
 * Abstract class that manages the sending of messages.
 */
abstract class AbstractSender implements Sender {

	private volatile boolean m_shutdown = false;

	/**
	 * Send the given message.
	 * 
	 * @param message
	 *            A {@link Message}.
	 * @exception CommunicationException
	 *                If an error occurs.
	 */
	public final void send(Message message) throws CommunicationException {

		if (m_shutdown) {
			throw new CommunicationException("Shut down");
		}

		try {
			writeMessage(message);
		} catch (IOException e) {
			UncheckedInterruptedException.ioException(e);
			throw new CommunicationException(
					"Exception whilst sending message", e);
		}
	}

	/**
	 * Template method for subclasses to implement the sending of a message.
	 * 
	 * @param message
	 * @throws CommunicationException
	 * @throws IOException
	 */
	protected abstract void writeMessage(Message message)
			throws CommunicationException, IOException;

	protected static final void writeMessageToStream(Message message,
			OutputStream stream) throws IOException {

		// I tried the model of using a single ObjectOutputStream for the
		// lifetime of the Sender and a single ObjectInputStream for each
		// Reader. However, the corresponding ObjectInputStream would get
		// occasional EOF's during readObject. Seems like voodoo to me,
		// but creating a new ObjectOutputStream for every message fixes
		// this.

		// Dr Heinz M. Kabutz's Java Specialists 2004-05-19 newsletter
		// (http://www.javaspecialists.co.za) may hold the answer.
		// ObjectOutputStream's cache based on object identity. The EOF
		// might be due to this, or at least ObjectOutputStream.reset()
		// may help. I can't get excited enough about the cost of creating
		// a new ObjectOutputStream() to try this as the bulk of what we
		// send are long[]'s so aren't cacheable, and it would break sends
		// that reuse Messages.

		final ObjectOutputStream objectStream = new ObjectOutputStream(stream);
		objectStream.writeObject(message);
		objectStream.flush();
	}

	/**
	 * Cleanly shutdown the <code>Sender</code>.
	 */
	public void shutdown() {
		try {
			send(new CloseCommunicationMessage());
		} catch (CommunicationException e) {
			// Ignore.
		}

		// Keep track of whether we've been closed. Can't rely on delegate
		// as some implementations don't do anything with close(), e.g.
		// ByteArrayOutputStream.
		m_shutdown = true;
	}

	/**
	 * Return whether we are shutdown.
	 * 
	 * @return <code>true</code> if and only if we are shut down.
	 */
	public boolean isShutdown() {
		return m_shutdown;
	}
}
