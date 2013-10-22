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
package org.bench4Q.common;

import java.io.PrintWriter;

/**
 * Interface to The Grinder logging system.
 * 
 * <p>
 * Output can be sent to either the terminal used to launch the worker process,
 * or to the process log files. The destination is specified by a mask of
 * constant values.
 * </p>
 */
public interface Logger {
	/** Destination constant that represents the log files. * */
	int LOG = 1 << 0;

	/** Destination constant that represents the terminal. * */
	int TERMINAL = 1 << 1;

	/**
	 * Log a message to the output log with context information.
	 * <p>
	 * Equivalent to <code>output(message, Logger.LOG)</code>.
	 * </p>
	 * 
	 * @param message
	 *            The message
	 */
	void output(String message);

	/**
	 * Log a message with context information.
	 * 
	 * @param message
	 *            The message
	 * @param where
	 *            Destination mask
	 */
	void output(String message, int where);

	/**
	 * Log an error to the error log with context information.
	 * <p>
	 * Equivalent to <code>error(message, Logger.LOG)</code>.
	 * </p>
	 * 
	 * @param message
	 *            The message
	 */
	void error(String message);

	/**
	 * Log an error with context information.
	 * 
	 * @param message
	 *            The message
	 * @param where
	 *            Destination mask
	 */
	void error(String message, int where);

	/**
	 * Get a <code>PrintWriter</code> that can be used to write to the output
	 * log file.
	 * 
	 * @return a <code>PrintWriter</code>
	 */
	PrintWriter getOutputLogWriter();

	/**
	 * Get a <code>PrintWriter</code> that can be used to write to the error
	 * log file.
	 * 
	 * @return a <code>PrintWriter</code>
	 */
	PrintWriter getErrorLogWriter();
}
