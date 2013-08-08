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

package org.bench4Q.common;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

/**
 * Make {@link InterruptedException}s and {@link InterruptedIOException}s easier
 * to propagate.
 * 
 * <p>
 * Our policy on interrupt handling:
 * 
 * <ul>
 * <li>{@link Thread#interrupt()} and {@link ThreadGroup#interrupt()} are used
 * in shut down code. We can't simply swallow {@link InterruptedException}s.</li>
 * 
 * <li>Whenever core code catches an {@link InterruptedException} which it
 * doesn't know how to handle, it rethrows it in an
 * {@link UncheckedInterruptedException}.</li>
 * 
 * <li>
 * {@link org.bench4Q.common.util.thread.InterruptibleRunnable#interruptibleRun()}
 * implementations are carefully reviewed to ensure that they do not ignore the
 * interrupt condition and will exit whenever {@link InterruptedException} and
 * {@link InterruptedIOException}s are received. We only interrupt code that
 * implements
 * {@link org.bench4Q.common.util.thread.InterruptibleRunnable#interruptibleRun()}
 * .</li>
 * 
 * <li>The InterruptibleRunnable's are invoked by wrapping then in an
 * InterruptibleRunnableAdapter. This exits cleanly, silently handling
 * {@link UncheckedInterruptedException}s.</li>
 * 
 * <li>Whenever core code outside an
 * {@link org.bench4Q.common.util.thread.InterruptibleRunnable} catches an
 * {@link IOException}, it calls {@link #ioException(IOException)}, which will
 * throw an {@link UncheckedInterruptedException} if necessary.</li>
 * 
 * <li>Other code may exit cleanly or may ignore the interrupt condition due to
 * third-party libraries swallowing {@link InterruptedException}s. This doesn't
 * matter as we should never interrupt this code.</li>
 * </ul>
 * </p>
 */
public class UncheckedInterruptedException extends UncheckedBench4QException {

	/**
	 * Constructor.
	 * 
	 * @param e
	 *            The original InterruptedException.
	 */
	public UncheckedInterruptedException(InterruptedException e) {
		super("Thread interrupted", e);
	}

	private UncheckedInterruptedException(InterruptedIOException e) {
		super("Thread interrupted", e);
	}

	/**
	 * {@link InterruptedIOException}s are a pain to handle as they extend
	 * {@link IOException}. {@link IOException} handlers should call this,
	 * unless they are part of an
	 * {@link org.bench4Q.common.util.thread.InterruptibleRunnable} and know
	 * what they're doing.
	 * 
	 * @param e
	 *            An {@link IOException}.
	 */
	public static void ioException(IOException e) {
		// SocketTimeoutException extends InterruptedIOException. One gets the
		// impression that JavaSoft was never serious about applications doing
		// anything other than ignoring interrupts.
		if (e instanceof InterruptedIOException
				&& !(e instanceof SocketTimeoutException)) {
			throw new UncheckedInterruptedException((InterruptedIOException) e);
		}
	}
}
