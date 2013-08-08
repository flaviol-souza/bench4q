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

package org.bench4Q.console.ui;

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * JTextField that only accepts integers within a particular range.
 */
class IntegerField extends JTextField {

	private static final Toolkit s_toolkit = Toolkit.getDefaultToolkit();
	private static final double s_log10 = Math.log(10);
	private final int m_minimumValue;
	private final int m_maximumValue;

	private static int log10(long x) {
		// The 1e-10 is a cosmological constant to account for FP
		// rounding errors that occur, e.g. for x=10.
		return new Double((Math.log(x) / s_log10) + 1e-10).intValue();
	}

	private static int maxFieldWidth(int minimumValue, int maximumValue) {
		final long min = minimumValue < 0 ? 10 * -minimumValue : minimumValue;
		final long max = maximumValue < 0 ? 10 * -maximumValue : maximumValue;

		return log10(Math.max(Math.abs(min), Math.abs(max))) + 1;
	}

	public IntegerField(int minimumValue, int maximumValue) {
		super(maxFieldWidth(minimumValue, maximumValue));

		if (minimumValue > maximumValue) {
			throw new IllegalArgumentException(
					"Minimum value exceeds maximum value");
		}

		m_minimumValue = minimumValue;
		m_maximumValue = maximumValue;

		setValue(m_minimumValue);
		setDocument(new FormattedDocument());
	}

	public int getValue() {

		try {
			return Integer.parseInt(getText());
		} catch (NumberFormatException e) {
			// Occurs if field is blank or "-".
			return m_minimumValue;
		}
	}

	public void setValue(int value) {

		if (value < m_minimumValue || value > m_maximumValue) {
			throw new IllegalArgumentException("Value out of bounds");
		}

		setText(Integer.toString(value));
	}

	/**
	 * Extend <code>PlainDocument</code> to perform our checks..
	 */
	public class FormattedDocument extends PlainDocument {

		public void insertString(int offset, String string,
				AttributeSet attributeSet) throws BadLocationException {

			final String currentText = super.getText(0, getLength());

			final String result = currentText.substring(0, offset) + string
					+ currentText.substring(offset);

			if (m_minimumValue >= 0 || !result.equals("-")) {
				try {
					final int x = Integer.parseInt(result);

					if (x < m_minimumValue || x > m_maximumValue) {
						s_toolkit.beep();
						return;
					}
				} catch (NumberFormatException e) {
					s_toolkit.beep();
					return;
				}
			}

			super.insertString(offset, string, attributeSet);
		}
	}

	public void addChangeListener(final ChangeListener listener) {

		getDocument().addDocumentListener(new DocumentListener() {

			private void notifyChangeListener() {
				listener.stateChanged(new ChangeEvent(this));
			}

			public void changedUpdate(DocumentEvent e) {
				notifyChangeListener();
			}

			public void insertUpdate(DocumentEvent e) {
				notifyChangeListener();
			}

			public void removeUpdate(DocumentEvent e) {
				notifyChangeListener();
			}
		});
	}
}
