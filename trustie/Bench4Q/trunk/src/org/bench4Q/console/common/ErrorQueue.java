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
package org.bench4Q.console.common;

import java.util.LinkedList;

/**
 * {@link ErrorHandler} that queues up its errors when a delegate
 * <code>ErrorHandler</code> is not available, and passes the errors on when a
 * delegate is available.
 */
public final class ErrorQueue implements ErrorHandler {

	private ErrorHandler m_delegate = null;
	private final LinkedList m_queue = new LinkedList();

	/**
	 * Set the delegate error handler. Any queued up errors will be reported to
	 * the delegate immediately.
	 * 
	 * @param errorHandler
	 *            Where to report errors.
	 */
	public void setErrorHandler(ErrorHandler errorHandler) {
		synchronized (this) {
			m_delegate = errorHandler;

			if (m_delegate != null) {
				synchronized (m_queue) {
					while (m_queue.size() > 0) {
						final DelayedError delayedError = (DelayedError) m_queue
								.removeFirst();

						delayedError.apply(m_delegate);
					}
				}
			}
		}
	}

	private static interface DelayedError {
		void apply(ErrorHandler errorHandler);
	}

	private void queue(DelayedError delayedError) {
		synchronized (this) {
			if (m_delegate != null) {
				delayedError.apply(m_delegate);
			} else {
				synchronized (m_queue) {
					m_queue.add(delayedError);
				}
			}
		}
	}

	/**
	 * Method that handles error messages.
	 * 
	 * @param errorMessage
	 *            The error message.
	 */
	public void handleErrorMessage(final String errorMessage) {
		queue(new DelayedError() {
			public void apply(ErrorHandler errorHandler) {
				errorHandler.handleErrorMessage(errorMessage);
			}
		});
	}

	/**
	 * Method that handles error messages.
	 * 
	 * @param errorMessage
	 *            The error message.
	 * @param title
	 *            A title to use.
	 */
	public void handleErrorMessage(final String errorMessage, final String title) {
		queue(new DelayedError() {
			public void apply(ErrorHandler errorHandler) {
				errorHandler.handleErrorMessage(errorMessage, title);
			}
		});
	}

	/**
	 * Method that handles exceptions.
	 * 
	 * @param throwable
	 *            The exception.
	 */
	public void handleException(final Throwable throwable) {
		queue(new DelayedError() {
			public void apply(ErrorHandler errorHandler) {
				errorHandler.handleException(throwable);
			}
		});
	}

	/**
	 * Method that handles exceptions.
	 * 
	 * @param throwable
	 *            The exception.
	 * @param title
	 *            A title to use.
	 */
	public void handleException(final Throwable throwable, final String title) {
		queue(new DelayedError() {
			public void apply(ErrorHandler errorHandler) {
				errorHandler.handleException(throwable, title);
			}
		});
	}

	/**
	 * Method that handles information messages.
	 * 
	 * @param informationMessage
	 *            The information message.
	 */
	public void handleInformationMessage(final String informationMessage) {
		queue(new DelayedError() {
			public void apply(ErrorHandler errorHandler) {
				errorHandler.handleInformationMessage(informationMessage);
			}
		});
	}
}
