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

import java.text.MessageFormat;

/**
 * Exception that can be displayed through the user interface. Supports
 * internationalised text through {@link Resources}.
 */
public class DisplayMessageConsoleException extends ConsoleException {

	/**
	 * Constructor.
	 * 
	 * @param resources
	 *            Resources to use.
	 * @param resourceKey
	 *            Resource key that specifies message.
	 */
	public DisplayMessageConsoleException(Resources resources,
			String resourceKey) {
		super(getMessage(resources, resourceKey));
	}

	/**
	 * Constructor.
	 * 
	 * @param resources
	 *            Resources to use.
	 * @param resourceKey
	 *            Resource key that specifies message.
	 * @param e
	 *            Nested exception.
	 */
	public DisplayMessageConsoleException(Resources resources,
			String resourceKey, Exception e) {
		super(getMessage(resources, resourceKey), e);
	}

	/**
	 * Constructor.
	 * 
	 * @param resources
	 *            Resources to use.
	 * @param resourceKey
	 *            Resource key that specifies message.
	 * @param arguments
	 *            Message arguments.
	 */
	public DisplayMessageConsoleException(Resources resources,
			String resourceKey, Object[] arguments) {
		super(MessageFormat.format(getMessage(resources, resourceKey),
				arguments));
	}

	/**
	 * Constructor.
	 * 
	 * @param resources
	 *            Resources to use.
	 * @param resourceKey
	 *            Resource key that specifies message.
	 * @param arguments
	 *            Message arguments.
	 * @param e
	 *            Nested exception.
	 */
	public DisplayMessageConsoleException(Resources resources,
			String resourceKey, Object[] arguments, Exception e) {
		super(MessageFormat.format(getMessage(resources, resourceKey),
				arguments), e);
	}

	private static String getMessage(Resources resources, String resourceKey) {

		final String resourceValue = resources.getString(resourceKey, false);

		if (resourceValue != null) {
			return resourceValue;
		} else {
			return "No message found for key \"" + resourceKey + "\"";
		}
	}
}
