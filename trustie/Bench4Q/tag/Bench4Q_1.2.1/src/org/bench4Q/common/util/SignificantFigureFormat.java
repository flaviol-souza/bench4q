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

import java.text.DecimalFormat;
import java.text.FieldPosition;

/**
 * Java doesn't provide a NumberFormatter which understands significant figures,
 * this is a cheap and cheerful one. Not extensively tested.
 */
public class SignificantFigureFormat extends DecimalFormat {

	private static final long serialVersionUID = 1;

	private static final int s_decimalPlaces = 40;

	private final int m_significantFigures;

	/**
	 * Constructor.
	 * 
	 * @param significantFigures
	 *            Number of significant figures.
	 */
	public SignificantFigureFormat(int significantFigures) {

		// 40 DP, should match value of s_decimalPlaces.
		super("0.0000000000000000000000000000000000000000");

		m_significantFigures = significantFigures;
	}

	private static int boundingPowerOfTen(double number) {

		if (number == 0d || Double.isInfinite(number) || Double.isNaN(number)) {
			return 1;
		}

		final double abs = Math.abs(number);

		int i = 0;
		double x = 1;

		if (abs < 1) {
			while (x > abs) {
				x /= 10;
				--i;
			}

			return i + 1;
		} else {
			while (!(x > abs)) {
				x *= 10;
				++i;
			}

			return i;
		}
	}

	/**
	 * Almost certainly doesn't set position correctly.
	 * 
	 * @param number
	 *            Number to format.
	 * @param buffer
	 *            Buffer to append result to.
	 * @param position
	 *            Field position.
	 * @return a <code>StringBuffer</code> value
	 */
	public StringBuffer format(double number, StringBuffer buffer, FieldPosition position) {

		if (Double.isInfinite(number) || Double.isNaN(number)) {
			return super.format(number, buffer, position);
		}

		final int shift = boundingPowerOfTen(number) - m_significantFigures;
		final double factor = Math.pow(10, shift);

		super.format(factor * Math.round(number / factor), buffer, position);

		final int truncate = shift < 0 ? s_decimalPlaces + shift : s_decimalPlaces + 1;

		buffer.setLength(buffer.length() - truncate);

		return buffer;
	}

	/**
	 * Almost certainly doesn't set position correctly.
	 * 
	 * @param number
	 *            Number to format.
	 * @param buffer
	 *            Buffer to append result to.
	 * @param position
	 *            Field position.
	 * @return a <code>StringBuffer</code> value
	 */
	public StringBuffer format(long number, StringBuffer buffer, FieldPosition position) {

		return format((double) number, buffer, position);
	}
}
