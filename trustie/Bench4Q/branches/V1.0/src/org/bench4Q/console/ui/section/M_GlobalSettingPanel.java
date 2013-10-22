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

import org.bench4Q.common.util.XMlFileLoader;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.ui.ProcessStatusTableModel;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.bench4Q.console.ui.Table;

public class M_GlobalSettingPanel extends JPanel {

	private final Resources m_resources;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;

	private XMlFileLoader fileLoader;

	private JLabel nameLabel;
	private JLabel commentLabel;
	private JTextField name;
	private JTextField comment;

	public M_GlobalSettingPanel(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, XMlFileLoader fileLoader)
			throws ConsoleException {
		m_resources = resources;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;
		this.fileLoader = fileLoader;

		this.setLayout(new BorderLayout());

		nameLabel = new JLabel(m_resources.getString("TestNamePanel.nameLabel"),
				SwingConstants.RIGHT);
		commentLabel = new JLabel(m_resources.getString("TestNamePanel.commentLabel"),
				SwingConstants.RIGHT);

		DocumentListener doclistener = new DocListener();
		name = new JTextField();
		name.getDocument().addDocumentListener(doclistener);

		comment = new JTextField();
		comment.getDocument().addDocumentListener(doclistener);

		final JTabbedPane tabbedPane = new JTabbedPane();

		JPanel contextPane = new JPanel();
		contextPane.setLayout(new GridBagLayout());
		contextPane.setPreferredSize(new Dimension(690, 480));
		contextPane.setMinimumSize(new Dimension(300, 200));

		contextPane.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1));
		contextPane.add(name, new GridBagConstraints(1, 0, 1, 1, 100.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1,
				1));

		contextPane.add(commentLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1));
		contextPane.add(comment, new GridBagConstraints(1, 1, 1, 1, 100.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1,
				1));

		// contextPane.add(URLLabel, new GridBagConstraints(0, 2, 1, 1, 0.0,
		// 0.0,
		// GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5,
		// 5), 1, 1));
		// contextPane.add(URL, new GridBagConstraints(1, 2, 1, 1, 100.0, 0.0,
		// GridBagConstraints.WEST,
		// GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));

		contextPane.add(new JLabel(), new GridBagConstraints(0, 2, 2, 1, 100.0, 100.0,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));

		final ProcessStatusTableModel processStatusModel = new ProcessStatusTableModel(m_resources,
				m_processControl, m_swingDispatcherFactory);
		final JScrollPane processStatusPane = new JScrollPane(new Table(processStatusModel));
		processStatusPane.setMinimumSize(new Dimension(60, 30));
		processStatusPane.setPreferredSize(new Dimension(60, 40));

		tabbedPane.addTab(m_resources.getString("GlobalSettingPanel.contextPane.title"),
				m_resources.getImageIcon("GlobalSettingPanel.contextPane.image"), contextPane,
				m_resources.getString("GlobalSettingPanel.contextPane.tip"));
		tabbedPane.addTab(m_resources.getString("GlobalSettingPanel.processStatusPane.title"),
				m_resources.getImageIcon("GlobalSettingPanel.processStatusPane.image"),
				processStatusPane, m_resources
						.getString("GlobalSettingPanel.processStatusPane.tip"));

		this.add(tabbedPane, BorderLayout.CENTER);

	}

	public void setConfigue() {
		String m_name = null;
		String m_desc = null;
		
		m_name = name.getText().trim();
		m_desc = comment.getText().trim();

		fileLoader.getArgs().setTestName(m_name);
		fileLoader.getArgs().setTestDescription(m_desc);		

	}

	private class DocListener implements DocumentListener {
		public void insertUpdate(DocumentEvent event) {
			setConfigue();
		}

		public void removeUpdate(DocumentEvent event) {
			setConfigue();
		}

		public void changedUpdate(DocumentEvent event) {
			setConfigue();
		}
	}

}
