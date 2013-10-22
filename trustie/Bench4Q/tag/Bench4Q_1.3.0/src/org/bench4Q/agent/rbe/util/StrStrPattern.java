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
public class StrStrPattern extends StringPattern {
	protected String p; // The string to match.

	/**
	 * @param s
	 */
	public StrStrPattern(String s) {
		p = s;
	}

	/**
	 * @return
	 */
	public int length() {
		return (p.length());
	}

	// Search from index start to end, inclusive.
	public int find(String s, int start, int end) {
		int i = s.indexOf(p, start);
		if (i == -1) {
			return (-1);
		}
		// FIXME: This is slower than needed, when the
		// string is much longer than end.
		else if (i >= (end + p.length())) {
			return (-1);
		} else {
			this.start = i;
			this.end = this.start + p.length() - 1;
			return (i);
		}
	}

	// See if pattern matches exactly characters pos to end, inclusive.
	public boolean matchWithin(String s, int pos, int end) {
		if ((end - pos + 1) < p.length())
			return (false);

		if (s.startsWith(p, pos)) {
			this.start = pos;
			this.end = pos + p.length() - 1;
			return (true);
		} else {
			return (false);
		}
	}

	public String toString() {
		return p;
	}

	// Minimum and maximum lengths.
	protected int minLength() {
		return (p.length());
	}

	protected int maxLength() {
		return (p.length());
	}

}
