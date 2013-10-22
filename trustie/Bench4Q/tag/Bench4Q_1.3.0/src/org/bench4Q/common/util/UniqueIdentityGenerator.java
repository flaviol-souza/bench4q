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

import java.util.Random;

/**
 * Simple generator of unique Strings.
 */
public final class UniqueIdentityGenerator {

	private final String m_unique;
	private int m_nextNumber = 0;
	private static final Random s_random = new Random();

	/**
	 * Constructor.
	 */
	public UniqueIdentityGenerator() {
		m_unique = hashCode() + "|" + System.currentTimeMillis() + "|"
				+ s_random.nextInt();
	}

	/**
	 * Create a unique string.
	 * 
	 * @param prefix
	 *            Prefix for generated strings.
	 * @return The unique string.
	 */
	public synchronized String createUniqueString(String prefix) {
		return prefix + ":" + m_unique + ":" + m_nextNumber++;
	}
}
