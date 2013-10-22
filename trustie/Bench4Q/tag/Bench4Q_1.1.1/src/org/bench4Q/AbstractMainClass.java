
package org.bench4Q;

import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.Logger;

/**
 * Basic functionality for a main class. Crudely extracted for now.
 * 
 */

public abstract class AbstractMainClass {

	private final Logger m_logger;

	/**
	 * Constructor.
	 * 
	 * @param logger
	 *            Logger to which output should be directed.
	 * @param usage
	 *            Usage message.
	 * @throws Bench4QException
	 *             If a problem occurred.
	 */
	protected AbstractMainClass(Logger logger) throws Bench4QException {

		m_logger = logger;
	}

	/**
	 * Return our logger.
	 * 
	 * @return The logger.
	 */
	protected final Logger getLogger() {
		return m_logger;
	}

	/**
	 * Log an error and return a {@link LoggedInitialisationException} that can
	 * be thrown.
	 * 
	 * @param message
	 *            The message to throw.
	 * @return An exception for the caller to throw.
	 */
	protected final LoggedInitialisationException barfError(String message) {
		m_logger.error("Error: " + message);
		return new LoggedInitialisationException(message);
	}

	/**
	 * Log a usage message and return a {@link LoggedInitialisationException}
	 * that can be thrown.
	 * 
	 * @return An exception for the caller to throw.
	 */
	protected final LoggedInitialisationException barfUsage() {
		return barfError("unrecognised or invalid option." + "\n\n");
	}

	/**
	 * Exception indicating that an error message has already been logged.
	 */
	protected static class LoggedInitialisationException extends Bench4QException {

		/**
		 * Constructor.
		 * 
		 * @param message
		 *            The error message.
		 */
		public LoggedInitialisationException(String message) {
			super(message);
		}
	}
}
