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
package org.bench4Q.agent.rbe.util;

/**
 * @author duanzhiquan
 * 
 */
public abstract class AbCharStrPattern extends StringPattern {

	// Search from index start to end, inclusive.
	public int find(String s, int start, int end) {
		int i;

		for (i = start; i <= end; i++) {
			if (charMatch(s.charAt(i))) {
				this.start = i;
				this.end = i;
				return (i);
			}
		}

		return (-1);
	}

	// See if pattern matches exactly characters pos to end, inclusive.
	public boolean matchWithin(String s, int pos, int end) {
		if (charMatch(s.charAt(pos))) {
			this.start = this.end = pos;
			return (true);
		} else {
			return (false);
		}
	}

	// Minimum and maximum lengths.
	protected int minLength() {
		return 1;
	}

	protected int maxLength() {
		return 1;
	}

	// Does this character match.
	protected abstract boolean charMatch(char c);
}
