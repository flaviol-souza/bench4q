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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * <code>JDialog</code> that is more useful than that returned by
 * <code>JOptionPane.createDialog()</code>.
 */
public class JOptionPaneDialog extends JDialog {

	/**
	 * Constructor.
	 * 
	 * @param frame
	 *            Parent frame.
	 * @param title
	 *            The title.
	 * @param modal
	 *            <code>true</code> => dialog should be modal.
	 * @param optionPane
	 *            JOptionPane to wrap.
	 */
	public JOptionPaneDialog(JFrame frame, String title, boolean modal,
			JOptionPane optionPane) {

		super(frame, title, modal);
		setOptionPane(optionPane);
	}

	/**
	 * Constructor.
	 * 
	 * @param dialog
	 *            Parent dialog.
	 * @param title
	 *            The title.
	 * @param modal
	 *            <code>true</code> => dialog should be modal.
	 * @param optionPane
	 *            JOptionPane to wrap.
	 */
	public JOptionPaneDialog(JDialog dialog, String title, boolean modal,
			JOptionPane optionPane) {

		super(dialog, title, modal);
		setOptionPane(optionPane);
	}

	/**
	 * Common initialistion. We need separate constructors because JDialog does
	 * not allow us to treat parentComponent polymorphically.
	 * 
	 * @param optionPane
	 *            JOptionPane to wrap.
	 */
	private void setOptionPane(final JOptionPane optionPane) {

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		final Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(optionPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());

		addWindowListener(new WindowAdapter() {
			private boolean m_gotFocus = false;

			public void windowClosing(WindowEvent e) {
				optionPane.setValue(null);
			}

			public void windowActivated(WindowEvent e) {
				// Once window gets focus, set initial focus
				if (!m_gotFocus) {
					optionPane.selectInitialValue();
					m_gotFocus = true;
				}
			}
		});

		optionPane.addPropertyChangeListener(new PropertyChangeListener() {

			private boolean m_disable = false;

			public void propertyChange(PropertyChangeEvent e) {
				if (isVisible()
						&& e.getSource() == optionPane
						&& !m_disable
						&& (e.getPropertyName().equals(
								JOptionPane.VALUE_PROPERTY) || e
								.getPropertyName().equals(
										JOptionPane.INPUT_VALUE_PROPERTY))) {

					final Cursor oldCursor = getCursor();
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

					try {
						if (shouldClose()) {
							setVisible(false);
							dispose();
						}
					} finally {
						m_disable = true;
						optionPane.setValue(null);
						m_disable = false;
						setCursor(oldCursor);
					}
				}
			}
		});

		optionPane.setValue(null);
	}

	/**
	 * Whether dialog should be closed on a property change.
	 * 
	 * @return <code>true</code> => it should.
	 */
	protected boolean shouldClose() {
		return true;
	}
}
