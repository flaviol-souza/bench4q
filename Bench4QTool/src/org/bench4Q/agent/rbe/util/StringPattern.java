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
public abstract class StringPattern {
	int start, end; // Last match.

	/**
	 * Find a match in the given string. Return index of first character of
	 * pattern, if the pattern is found. Returns -1 if no pattern is found.
	 * Search whole string.
	 * 
	 * @param s
	 * @return
	 */
	public int find(String s) {
		return (find(s, 0, s.length() - 1));
	}

	/**
	 * Search starting at index start.
	 * 
	 * @param s
	 * @param start
	 * @return
	 */
	public int find(String s, int start) {
		return (find(s, start, s.length() - 1));
	}

	/**
	 * Search from index start to end, inclusive. Note that the ENTIRE pattern
	 * must fit between start and end, not just begin matching before end.
	 * 
	 * @param s
	 * @param start
	 * @param end
	 * @return
	 */
	public abstract int find(String s, int start, int end);

	/**
	 * See if pattern matches first part of string.
	 * 
	 * @param s
	 * @return
	 */

	public boolean match(String s) {
		return (match(s, 0, s.length() - 1));
	}

	/**
	 * See if pattern matches at index pos.
	 * 
	 * @param s
	 * @param pos
	 * @return
	 */
	public boolean match(String s, int pos) {
		return (matchWithin(s, pos, s.length() - 1));
	}

	/**
	 * See if pattern matches exactly characters pos to end, inclusive.
	 * 
	 * @param s
	 * @param pos
	 * @param end
	 * @return
	 */
	public boolean match(String s, int pos, int end) {
		int saveStart = start;
		int saveEnd = this.end;

		if (matchWithin(s, pos, end)) {
			if (this.end == end) {
				return (true);
			}
			this.start = saveStart;
			this.end = saveEnd;
		}

		return (false);
	}

	/**
	 * Find a complete match starting at pos and stopping before or at end.
	 * 
	 * @param s
	 * @param pos
	 * @param end
	 * @return
	 */
	public abstract boolean matchWithin(String s, int pos, int end);

	/**
	 * Returns the index of the last charcter that matched the pattern.
	 * 
	 * @return
	 */
	public int end() {
		return (end);
	}

	/**
	 * // Returns the index of the first character that last matched the
	 * pattern.
	 * 
	 * @return
	 */
	public int start() {
		return (start);
	}

	// Minimum and maximum lengths.
	protected abstract int minLength();

	protected abstract int maxLength();
}
