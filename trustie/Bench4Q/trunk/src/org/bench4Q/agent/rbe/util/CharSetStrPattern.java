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
public class CharSetStrPattern extends AbCharStrPattern {
	/**
	 * 
	 */
	public static final CharRangeStrPattern digit = new CharRangeStrPattern(
			'0', '9');

	/**
	 * 
	 */
	public static final CharRangeStrPattern lower = new CharRangeStrPattern(
			'a', 'z');

	/**
	 * 
	 */
	public static final CharRangeStrPattern upper = new CharRangeStrPattern(
			'A', 'Z');

	/**
	 * 
	 */
	public static final CharSetStrPattern notDigit;

	static {
		notDigit = new CharSetStrPattern();
		notDigit.set((char) 0, (char) 255);
		notDigit.clear('0', '9');
	}

	protected byte[] mask = new byte[32];

	/**
	 * @param c
	 */
	public void set(char c) {
		int i = c >> 3;
		int bit = 1 << (c & 7);

		mask[i] |= bit;
	}

	/**
	 * @param s
	 * @param e
	 */
	public void set(char s, char e) {
		int si = s >> 3;
		int ei = e >> 3;

		int b;

		if (si == ei) {
			mask[si] |= ((1 << ((e & 7) - (s & 7) + 1)) - 1) << (s & 7);
		} else {
			for (b = si + 1; b < ei; b++) {
				mask[b] = (byte) 0xff;
			}

			mask[si] |= 0xff << (s & 7);
			mask[ei] |= (1 << ((e & 7) + 1)) - 1;
		}
	}

	/**
	 * @param c
	 */
	public void set(String c) {
		int i;
		for (i = 0; i < c.length(); i++) {
			set(c.charAt(i));
		}
	}

	/**
	 * @param c
	 */
	public void clear(char c) {
		int i = c >> 3;
		int bit = 1 << (c & 7);

		mask[i] &= (0xff ^ bit);
	}

	/**
	 * @param s
	 * @param e
	 */
	public void clear(char s, char e) {
		int si = s >> 3;
		int ei = e >> 3;

		int b;

		if (si == ei) {
			mask[si] &= 0xff ^ ((1 << ((e & 7) - (s & 7) + 1)) - 1) << (s & 7);
		} else {
			for (b = si + 1; b < ei; b++) {
				mask[b] = (byte) 0;
			}

			mask[si] &= 0xff >> (8 - (s & 7));
			mask[ei] &= 0xfe << (e & 7);
		}
	}

	/**
	 * @param c
	 */
	public void clear(String c) {
		int i;
		for (i = 0; i < c.length(); i++) {
			clear(c.charAt(i));
		}
	}

	/**
	 * @param c
	 * @return
	 */
	public boolean get(char c) {
		int i = c >> 3;
		int bit = 1 << (c & 7);

		return ((mask[i] & bit) != 0);
	}

	// Does this character match.
	protected boolean charMatch(char c) {
		int i = c >> 3;
		int bit = 1 << (c & 7);

		return ((mask[i] & bit) != 0);
	};

}
