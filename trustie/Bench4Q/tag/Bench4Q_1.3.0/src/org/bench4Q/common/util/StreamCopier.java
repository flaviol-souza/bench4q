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

package org.bench4Q.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bench4Q.common.Closer;
import org.bench4Q.common.UncheckedInterruptedException;
import org.bench4Q.common.util.thread.InterruptibleRunnable;
import org.bench4Q.common.util.thread.InterruptibleRunnableAdapter;

/**
 * Class that copies from <code>InputStream</code>s to <code>OutputStream</code>
 * s. Can be used in conjunction with an active thread - see
 * {@link #getInterruptibleRunnable}.
 */
public class StreamCopier {

	private final byte[] m_buffer;
	private final boolean m_closeStreams;

	/**
	 * Constructor.
	 * 
	 * @param bufferSize
	 *            The buffer size.
	 * @param closeStreams
	 *            <code>true</code> => ensure the streams are closed after
	 *            copying.
	 */
	public StreamCopier(int bufferSize, boolean closeStreams) {
		m_buffer = new byte[bufferSize];
		m_closeStreams = closeStreams;
	}

	/**
	 * Copies from the input stream to the output stream until the input stream
	 * is empty or one of the streams reports an error.
	 * 
	 * <p>
	 * Not thread safe - use multiple <code>StreamCopier</code> instances
	 * instead.
	 * 
	 * @param in
	 *            Input stream.
	 * @param out
	 *            Output stream.
	 * @throws IOException
	 *             If an IO problem occurred during the copy.
	 */
	public void copy(InputStream in, OutputStream out) throws IOException {

		try {
			while (true) {
				final int bytesRead = in.read(m_buffer, 0, m_buffer.length);

				if (bytesRead == -1) {
					break;
				}

				out.write(m_buffer, 0, bytesRead);
				out.flush();
			}
		} finally {
			if (m_closeStreams) {
				Closer.close(out);
				Closer.close(in);
			}
		}
	}

	/**
	 * Creates a <code>InterruptibleRunnable</code> that can be used to copy a
	 * stream with an active Thread.
	 * 
	 * <p>
	 * Any exceptions that occur during processing are simply discarded.
	 * 
	 * @param in
	 *            Input stream.
	 * @param out
	 *            Output stream.
	 * @return The <code>InterruptibleRunnable</code>.
	 */
	public InterruptibleRunnable getInterruptibleRunnable(final InputStream in,
			final OutputStream out) {

		return new InterruptibleRunnable() {
			public void interruptibleRun() {
				try {
					copy(in, out);
				} catch (IOException e) {
					// Be silent about IOExceptions.
					UncheckedInterruptedException.ioException(e);
				}
			}
		};
	}

	/**
	 * Convenience version of
	 * {@link #getInterruptibleRunnable(InputStream, OutputStream)} that returns
	 * a {@link Runnable}.
	 * 
	 * @param in
	 *            Input stream.
	 * @param out
	 *            Output stream.
	 * @return The <code>InterruptibleRunnable</code>.
	 */
	public Runnable getRunnable(InputStream in, OutputStream out) {
		return new InterruptibleRunnableAdapter(getInterruptibleRunnable(in,
				out));
	}
}
