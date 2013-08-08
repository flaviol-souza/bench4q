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

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.bench4Q.common.util.BooleanProperty;
import org.bench4Q.console.ConsoleProperties;
import org.bench4Q.console.common.DisplayMessageConsoleException;
import org.bench4Q.console.common.Resources;

/**
 * Optional confirmation dialogs.
 */
final class OptionalConfirmDialog {

	private final JFrame m_frame;
	private final Resources m_resources;
	private final ConsoleProperties m_properties;

	/**
	 * Option value returned if the user has selected not to be asked again.
	 */
	public static final int DONT_ASK_OPTION = 999;

	/**
	 * Constructor.
	 * 
	 * @param frame
	 *            Parent frame.
	 * @param resources
	 *            Resources object to use for strings and things.
	 * @param properties
	 *            Console properties.
	 */
	public OptionalConfirmDialog(JFrame frame, Resources resources,
			ConsoleProperties properties) {
		m_frame = frame;
		m_resources = resources;
		m_properties = properties;
	}

	/**
	 * Show a confirmation dialog.
	 * 
	 * @param message
	 *            The question to ask.
	 * @param title
	 *            The dialog title.
	 * @param optionType
	 *            The option types to present. See {@link JOptionPane}.
	 * @param askPropertyName
	 *            The name of a boolean Console property that is set if the user
	 *            has chosen not to display the confirmation.
	 * @return The chosen option. This is either a value returned from
	 *         {@link JOptionPane#showConfirmDialog(Component, Object)} or
	 *         {@link #DONT_ASK_OPTION}.
	 * @throws BooleanProperty.PropertyException
	 *             If the property could not be read or written.
	 * @throws DisplayMessageConsoleException
	 *             If a problem occurred persisting the property.
	 */
	public int show(String message, String title, int optionType,
			String askPropertyName) throws BooleanProperty.PropertyException,
			DisplayMessageConsoleException {

		final BooleanProperty askProperty = new BooleanProperty(m_properties,
				askPropertyName);

		if (!askProperty.get()) {
			return DONT_ASK_OPTION;
		}

		final JCheckBox dontAskMeAgainCheckBox = new JCheckBox(m_resources
				.getString("dontAskMeAgain.text"));
		dontAskMeAgainCheckBox.setAlignmentX(Component.RIGHT_ALIGNMENT);

		final Object[] messageArray = { message, new JLabel(), // Pad.
				dontAskMeAgainCheckBox, };

		final int chosen = JOptionPane.showConfirmDialog(m_frame, messageArray,
				title, optionType);

		if (chosen != JOptionPane.CANCEL_OPTION
				&& chosen != JOptionPane.CLOSED_OPTION) {
			try {
				askProperty.set(!dontAskMeAgainCheckBox.isSelected());
			} catch (BooleanProperty.PropertyException e) {
				final Throwable cause = e.getCause();

				if (cause instanceof DisplayMessageConsoleException) {
					throw (DisplayMessageConsoleException) cause;
				}

				throw e;
			}
		}

		return chosen;
	}
}
