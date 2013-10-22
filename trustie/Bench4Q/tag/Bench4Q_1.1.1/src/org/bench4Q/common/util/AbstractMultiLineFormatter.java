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

/**
 * Abstract implementation of MultiLineFormatter.
 */
public abstract class AbstractMultiLineFormatter implements MultiLineFormatter {

	/**
	 * Convenience method.
	 * 
	 * @param input
	 *            Input text.
	 * @return Formatted result.
	 */
	public String format(String input) {
		final StringBuffer result = new StringBuffer();
		StringBuffer buffer = new StringBuffer(input);

		while (buffer.length() > 0) {
			final StringBuffer remainder = new StringBuffer();
			transform(buffer, remainder);

			if (result.length() > 0) {
				result.append("\n");
			}

			result.append(buffer.toString());

			buffer = remainder;
		}

		return result.toString();
	}
}
