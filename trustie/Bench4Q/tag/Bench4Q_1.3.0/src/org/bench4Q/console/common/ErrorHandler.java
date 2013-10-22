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

/**
 * Interface for things that can deal with reporting errors and exceptions.
 */
public interface ErrorHandler {

	/**
	 * Method that handles error messages.
	 * 
	 * @param errorMessage
	 *            The error message.
	 * @see #handleErrorMessage(String, String)
	 * @see #handleInformationMessage(String)
	 */
	void handleErrorMessage(String errorMessage);

	/**
	 * Method that handles error messages.
	 * 
	 * @param errorMessage
	 *            The error message.
	 * @param title
	 *            A title to use.
	 * @see #handleErrorMessage(String)
	 */
	void handleErrorMessage(String errorMessage, String title);

	/**
	 * Method that handles exceptions.
	 * 
	 * @param throwable
	 *            The exception.
	 */
	void handleException(Throwable throwable);

	/**
	 * Method that handles exceptions.
	 * 
	 * @param throwable
	 *            The exception.
	 * @param title
	 *            A title to use.
	 */
	void handleException(Throwable throwable, String title);

	/**
	 * Method that handles information messages. Information messages are lower
	 * priority than error messages. In general, error messages should interrupt
	 * the user (e.g. use a modal dialog), but information messages should just
	 * be logged.
	 * 
	 * @param informationMessage
	 *            The information message.
	 * @see #handleErrorMessage(String)
	 */
	void handleInformationMessage(String informationMessage);
}
