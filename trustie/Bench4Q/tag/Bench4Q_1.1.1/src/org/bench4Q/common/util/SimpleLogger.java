/**
 * =========================================================================
 * 					Bench4Q version 1.1.1
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

import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

import org.bench4Q.common.Logger;

/**
 * @author duanzhiquan
 *
 */
public final class SimpleLogger implements Logger {

	private final DateFormat m_dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,
			DateFormat.MEDIUM);

	private final String m_identifier;
	private final PrintWriter m_outputWriter;
	private final PrintWriter m_errorWriter;
	private final MultiLineFormatter m_formatter;

	/**
	 * Constructor.
	 * 
	 * @param identifier
	 *            Short description of process that owns the logger.
	 * @param outputWriter
	 *            Where to write normal output.
	 * @param errorWriter
	 *            Where to write error output.
	 * @param formatter
	 *            StringFormatter that will be used to format messages.
	 */
	public SimpleLogger(String identifier, PrintWriter outputWriter, PrintWriter errorWriter,
			MultiLineFormatter formatter) {
		m_identifier = identifier;
		m_outputWriter = outputWriter;
		m_errorWriter = errorWriter;
		m_formatter = formatter;
	}

	/**
	 * Constructor.
	 * 
	 * @param identifier
	 *            Short description of process that owns the logger.
	 * @param outputWriter
	 *            Where to write normal output.
	 * @param errorWriter
	 *            Where to write error output.
	 */
	public SimpleLogger(String identifier, PrintWriter outputWriter, PrintWriter errorWriter) {
		this(identifier, outputWriter, errorWriter, new NullMultiLineFormatter());
	}

	/**
	 * Log a message with context information.
	 * 
	 * @param message
	 *            The message
	 * @param where
	 *            Destination mask
	 */
	public void output(String message, int where) {
		writeMessage(m_outputWriter, message, where);
	}

	/**
	 * Log a message to the output log with context information.
	 * <p>
	 * Equivalent to <code>output(message, Logger.LOG)</code>.
	 * </p>
	 * 
	 * @param message
	 *            The message
	 */
	public void output(String message) {
		output(message, Logger.TERMINAL);
	}

	/**
	 * Log an error with context information.
	 * 
	 * @param message
	 *            The message
	 * @param where
	 *            Destination mask
	 */
	public void error(String message, int where) {
		writeMessage(m_errorWriter, message, where);
	}

	/**
	 * Log an error to the error log with context information.
	 * <p>
	 * Equivalent to <code>error(message, Logger.LOG)</code>.
	 * </p>
	 * 
	 * @param message
	 *            The message
	 */
	public void error(String message) {
		error(message, Logger.TERMINAL);
	}

	/**
	 * Get a <code>PrintWriter</code> that can be used to write to the output
	 * log file.
	 * 
	 * @return a <code>PrintWriter</code>
	 */
	public PrintWriter getOutputLogWriter() {
		return m_outputWriter;
	}

	/**
	 * Get a <code>PrintWriter</code> that can be used to write to the error
	 * log file.
	 * 
	 * @return a <code>PrintWriter</code>
	 */
	public PrintWriter getErrorLogWriter() {
		return m_errorWriter;
	}

	private void writeMessage(PrintWriter writer, String message, int where) {
		if (where != 0) {
			final StringBuffer formattedMessage = new StringBuffer();

			formattedMessage.append(m_dateFormat.format(new Date()));
			formattedMessage.append(" (");
			formattedMessage.append(m_identifier);
			formattedMessage.append("): ");
			formattedMessage.append(message);

			if ((where & Logger.TERMINAL) != 0) {
				writer.println(m_formatter.format(formattedMessage.toString()));
				writer.flush();
			}
		}
	}
}
