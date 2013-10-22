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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.communication.CommunicationDefaults;
import org.bench4Q.console.ConsoleProperties;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;

abstract class OptionsDialogHandler {
	private final JFrame m_parentFrame;
	private final Resources m_resources;

	/** A working copy of console properties. */
	private final ConsoleProperties m_properties;

	private final JTextField m_consoleHost = new JTextField();
	private final IntegerField m_consolePort = new IntegerField(
			CommunicationDefaults.MIN_PORT, CommunicationDefaults.MAX_PORT);
	private final JSlider m_sfSlider = new JSlider(1, 6, 1);
	private final JCheckBox m_resetConsoleWithProcessesCheckBox;
	private final JOptionPaneDialog m_dialog;
	private final JTextField m_externalEditorCommand = new JTextField(20);
	private final JTextField m_externalEditorArguments = new JTextField(20);

	public OptionsDialogHandler(JFrame parentFrame,
			ConsoleProperties properties, final Resources resources) {

		m_parentFrame = parentFrame;
		;
		m_resources = resources;
		m_properties = new ConsoleProperties(properties);

		final JPanel addressLabelPanel = new JPanel(new GridLayout(0, 1, 0, 1));
		addressLabelPanel.add(new JLabel(m_resources
				.getString("consoleHost.label")));
		addressLabelPanel.add(new JLabel(m_resources
				.getString("consolePort.label")));

		final JPanel addressFieldPanel = new JPanel(new GridLayout(0, 1, 0, 1));
		addressFieldPanel.add(m_consoleHost);
		addressFieldPanel.add(m_consolePort);

		final JPanel addressPanel = new JPanel();
		addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.X_AXIS));
		addressPanel.add(addressLabelPanel);
		addressPanel.add(Box.createHorizontalStrut(5));
		addressPanel.add(addressFieldPanel);

		// Use BorderLayout so the address panel uses its preferred
		// height, and full available width. Sadly I couldn't find a more
		// straightforward way.
		final JPanel communicationTab = new JPanel(new BorderLayout());
		communicationTab.add(addressPanel, BorderLayout.NORTH);
		communicationTab.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		m_resetConsoleWithProcessesCheckBox = new JCheckBox(m_resources
				.getString("resetConsoleWithProcesses.label"));
		final JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.add(m_resetConsoleWithProcessesCheckBox);

		final JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.addTab(m_resources
				.getString("options.communicationTab.title"), null,
				communicationTab, m_resources
						.getString("options.communicationTab.tip"));

		final Object[] options = { m_resources.getString("options.ok.label"),
				m_resources.getString("options.cancel.label"),
				m_resources.getString("options.save.label"), };

		final JOptionPane optionPane = new JOptionPane(communicationTab,
				JOptionPane.PLAIN_MESSAGE, 0, null, options);

		m_dialog = new JOptionPaneDialog(m_parentFrame, m_resources
				.getString("options.label"), true, optionPane) {

			protected boolean shouldClose() {
				final Object value = optionPane.getValue();

				if (value == options[1]) {
					return true;
				} else {
					try {
						setProperties(m_properties);
					} catch (ConsoleException e) {
						new ErrorDialogHandler(m_dialog, m_resources)
								.handleException(e);
						return false;
					}

					if (value == options[2]) {
						try {
							m_properties.save();
						} catch (Bench4QException e) {
							final Throwable cause = e.getCause();

							final String messsage = (cause != null ? cause : e)
									.getMessage();

							new ErrorDialogHandler(m_dialog, m_resources)
									.handleErrorMessage(messsage, m_resources
											.getString("fileError.title"));
							return false;
						}
					}

					// Success.
					setNewOptions(m_properties);
					return true;
				}
			}
		};

		m_dialog.pack();
	}

	private void setProperties(ConsoleProperties properties)
			throws ConsoleException {

		properties.setConsoleHost(m_consoleHost.getText());
		properties.setConsolePort(m_consolePort.getValue());
	}

	/**
	 * Show the dialog.
	 * 
	 * @param initialProperties
	 *            A set of properties to initialise the options with.
	 */
	public void showDialog(ConsoleProperties initialProperties) {
		m_properties.set(initialProperties);

		// Initialise input values.
		m_consoleHost.setText(m_properties.getConsoleHost());
		m_consolePort.setValue(m_properties.getConsolePort());

		m_dialog.setLocationRelativeTo(m_parentFrame);
		SwingUtilities.updateComponentTreeUI(m_dialog);
		m_dialog.setVisible(true);
	}

	/**
	 * User should override this to handle new options set by the dialog.
	 * 
	 * @param newOptions
	 */
	protected abstract void setNewOptions(ConsoleProperties newOptions);

	private final class ChooseCommandAction extends CustomAction {
		private final JFileChooser m_fileChooser = new JFileChooser(".");

		ChooseCommandAction() {
			super(m_resources, null, true);

			putValue(Action.NAME, "...");

			m_fileChooser.setDialogTitle(m_resources
					.getString("choose-external-editor.label"));
		}

		public void actionPerformed(ActionEvent event) {
			final String buttonText = m_resources
					.getString("choose-external-editor.label");

			if (m_fileChooser.showDialog(m_parentFrame, buttonText) == JFileChooser.APPROVE_OPTION) {

				m_externalEditorCommand.setText(m_fileChooser.getSelectedFile()
						.getAbsolutePath());
			}
		}
	}
}
