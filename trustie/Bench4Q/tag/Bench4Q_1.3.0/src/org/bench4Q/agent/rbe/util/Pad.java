/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there.
 * If you have any problem, you can  
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
public class Pad {

	/**
	 * Pad on the right with spaces.
	 * 
	 * @param size
	 * @param s
	 * @return
	 */
	public static String r(int size, String s) {
		return r(size, " ", s);
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String r(int size, double v) {
		return r(size, String.valueOf(v));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String r(int size, int v) {
		return r(size, String.valueOf(v));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String r(int size, char v) {
		return r(size, String.valueOf(v));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String r(int size, Object v) {
		return r(size, v.toString());
	}

	/* Pad on the right with zeros. */
	/**
	 * @param size
	 * @param s
	 * @return
	 */
	public static String rz(int size, String s) {
		return r(size, "0", s);
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String rz(int size, double v) {
		return rz(size, String.valueOf(v));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String rz(int size, int v) {
		return (rz(size, String.valueOf(v)));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String rz(int size, char v) {
		return rz(size, String.valueOf(v));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String rz(int size, Object v) {
		return rz(size, v.toString());
	}

	/**
	 * Pad on the right with arbitrary characters.
	 * 
	 * @param size
	 * @param pad
	 * @param s
	 * @return
	 */
	public static String r(int size, String pad, String s) {
		if (s.length() >= size) {
			return s;
		}
		return (s + expandRight(size - s.length(), pad));
	}

	/**
	 * @param size
	 * @param pad
	 * @param v
	 * @return
	 */
	public static String r(int size, String pad, double v) {
		String s = String.valueOf(v);
		return r(size, pad, s);
	}

	/**
	 * @param size
	 * @param pad
	 * @param v
	 * @return
	 */
	public static String r(int size, String pad, int v) {
		String s = String.valueOf(v);
		return r(size, pad, s);
	}

	/**
	 * @param size
	 * @param pad
	 * @param v
	 * @return
	 */
	public static String r(int size, String pad, char v) {
		String s = String.valueOf(v);
		return r(size, pad, s);
	}

	/**
	 * @param size
	 * @param pad
	 * @param v
	 * @return
	 */
	public static String r(int size, String pad, Object v) {
		String s = v.toString();
		return (r(size, pad, s));
	}

	/**
	 * Pad on the left with zeros.
	 * 
	 * @param size
	 * @param s
	 * @return
	 */
	public static String lz(int size, String s) {
		return (l(size, "0", s));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String lz(int size, double v) {
		return (lz(size, String.valueOf(v)));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String lz(int size, int v) {
		return (lz(size, String.valueOf(v)));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String lz(int size, char v) {
		return (lz(size, String.valueOf(v)));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String lz(int size, Object v) {
		return (lz(size, v.toString()));
	}

	/**
	 * Pad on the left with spaces.
	 * 
	 * @param size
	 * @param s
	 * @return
	 */
	public static String l(int size, String s) {
		return (l(size, " ", s));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String l(int size, double v) {
		return (l(size, String.valueOf(v)));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String l(int size, int v) {
		return (l(size, String.valueOf(v)));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String l(int size, char v) {
		return (l(size, String.valueOf(v)));
	}

	/**
	 * @param size
	 * @param v
	 * @return
	 */
	public static String l(int size, Object v) {
		return (l(size, v.toString()));
	}

	/**
	 * Pad on the left with arbitrary characters.
	 * 
	 * @param size
	 * @param pad
	 * @param s
	 * @return
	 */
	public static String l(int size, String pad, String s) {
		if (s.length() >= size) {
			return (s);
		}

		return (expandLeft(size - s.length(), pad) + s);
	}

	/**
	 * @param size
	 * @param pad
	 * @param v
	 * @return
	 */
	public static String l(int size, String pad, double v) {
		String s = String.valueOf(v);
		return (l(size, pad, s));
	}

	/**
	 * @param size
	 * @param pad
	 * @param v
	 * @return
	 */
	public static String l(int size, String pad, int v) {
		String s = String.valueOf(v);
		return (l(size, pad, s));
	}

	/**
	 * @param size
	 * @param pad
	 * @param v
	 * @return
	 */
	public static String l(int size, String pad, char v) {
		String s = String.valueOf(v);
		return (l(size, pad, s));
	}

	/**
	 * @param size
	 * @param pad
	 * @param v
	 * @return
	 */
	public static String l(int size, String pad, Object v) {
		String s = v.toString();
		return (l(size, pad, s));
	}

	/**
	 * @param size
	 * @param s
	 * @return
	 */
	public static String expandRight(int size, String s) {
		while (s.length() < size)
			s = s + s;

		return (s.substring(0, size));
	}

	/**
	 * @param size
	 * @param s
	 * @return
	 */
	public static String expandLeft(int size, String s) {
		while (s.length() < size)
			s = s + s;

		return (s.substring(s.length() - size));
	}
}
