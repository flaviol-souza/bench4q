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
package org.bench4Q.console.ui.section;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.model.ConfigModel;
import org.bench4Q.console.ui.ProcessStatusTableModel;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.bench4Q.console.ui.Table;

/**
 * @author duanzhiquan
 * 
 */
public class M_GlobalSettingPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7701640692297360752L;
	private final Resources m_resources;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;

	private ConfigModel m_configModel;

	private JLabel nameLabel;
	private JLabel commentLabel;
	private JTextField name;
	private JTextField comment;

	/**
	 * @param resources
	 * @param processControl
	 * @param dispatcherFactory
	 * @param configModel
	 * @throws ConsoleException
	 */
	public M_GlobalSettingPanel(Resources resources,
			ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, ConfigModel configModel)
			throws ConsoleException {
		m_resources = resources;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;
		m_configModel = configModel;

		this.setLayout(new BorderLayout());

		nameLabel = new JLabel(
				m_resources.getString("TestNamePanel.nameLabel"),
				SwingConstants.RIGHT);
		commentLabel = new JLabel(m_resources
				.getString("TestNamePanel.commentLabel"), SwingConstants.RIGHT);

		name = new JTextField(m_configModel.getArgs().getTestName());
		name.getDocument().addDocumentListener(new NameListener());

		comment = new JTextField(m_configModel.getArgs().getTestDescription());
		comment.getDocument().addDocumentListener(new Commentlistener());

		final JTabbedPane tabbedPane = new JTabbedPane();

		JPanel contextPane = new JPanel();
		contextPane.setLayout(new GridBagLayout());
		contextPane.setPreferredSize(new Dimension(690, 480));
		contextPane.setMinimumSize(new Dimension(300, 200));

		contextPane.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 1, 1));
		contextPane.add(name, new GridBagConstraints(1, 0, 1, 1, 100.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 1, 1));

		contextPane.add(commentLabel, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
				new Insets(5, 5, 5, 5), 1, 1));
		contextPane.add(comment, new GridBagConstraints(1, 1, 1, 1, 100.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 1, 1));

		contextPane.add(new JLabel(), new GridBagConstraints(0, 2, 2, 1, 100.0,
				100.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(5, 5, 5, 5), 1, 1));

		final ProcessStatusTableModel processStatusModel = new ProcessStatusTableModel(
				m_resources, m_processControl, m_swingDispatcherFactory);
		final JScrollPane processStatusPane = new JScrollPane(new Table(
				processStatusModel));
		processStatusPane.setMinimumSize(new Dimension(60, 30));
		processStatusPane.setPreferredSize(new Dimension(60, 40));

		tabbedPane.addTab(m_resources
				.getString("GlobalSettingPanel.contextPane.title"), m_resources
				.getImageIcon("GlobalSettingPanel.contextPane.image"),
				contextPane, m_resources
						.getString("GlobalSettingPanel.contextPane.tip"));
		tabbedPane
				.addTab(
						m_resources
								.getString("GlobalSettingPanel.processStatusPane.title"),
						m_resources
								.getImageIcon("GlobalSettingPanel.processStatusPane.image"),
						processStatusPane,
						m_resources
								.getString("GlobalSettingPanel.processStatusPane.tip"));

		this.add(tabbedPane, BorderLayout.CENTER);

		m_configModel.addListener(new ConfigModel.AbstractListener() {
			public void isArgsChanged() {
				resetConfig();
			}
		});
	}

	protected void resetConfig() {
		name.setText(String.valueOf(m_configModel.getArgs().getTestName()));
		comment.setText(String.valueOf(m_configModel.getArgs()
				.getTestDescription()));
	}

	/**
	 * 
	 */
	public void setConfigue() {
		String m_name = null;
		String m_desc = null;

		m_name = name.getText().trim();
		m_desc = comment.getText().trim();

		m_configModel.getArgs().setTestName(m_name);
		m_configModel.getArgs().setTestDescription(m_desc);

	}

	private class NameListener implements DocumentListener {
		public void insertUpdate(DocumentEvent event) {
			String m_name = name.getText().trim();
			m_configModel.getArgs().setTestName(m_name);
		}

		public void removeUpdate(DocumentEvent event) {
			String m_name = name.getText().trim();
			m_configModel.getArgs().setTestName(m_name);
		}

		public void changedUpdate(DocumentEvent event) {
			String m_name = name.getText().trim();
			m_configModel.getArgs().setTestName(m_name);
		}
	}

	private class Commentlistener implements DocumentListener {
		public void insertUpdate(DocumentEvent event) {
			String m_desc = comment.getText().trim();
			m_configModel.getArgs().setTestDescription(m_desc);
		}

		public void removeUpdate(DocumentEvent event) {
			String m_desc = comment.getText().trim();
			m_configModel.getArgs().setTestDescription(m_desc);
		}

		public void changedUpdate(DocumentEvent event) {
			String m_desc = comment.getText().trim();
			m_configModel.getArgs().setTestDescription(m_desc);
		}
	}

}
