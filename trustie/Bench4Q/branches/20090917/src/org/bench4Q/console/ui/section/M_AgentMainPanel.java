package org.bench4Q.console.ui.section;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.model.ConfigModel;
import org.bench4Q.console.ui.SwingDispatcherFactory;

public class M_AgentMainPanel extends JPanel {

	private final Resources m_resources;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;

	private ConfigModel m_configModel;

	private JLabel nameLabel;
	private JLabel stateLabel;
	private JTextField name;
	private JTextField state;

	private AgentIdentity m_agentIdentity;

	public M_AgentMainPanel(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, ConfigModel fileLoader,
			AgentIdentity agentIdentity) throws ConsoleException {
		m_resources = resources;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;
		this.m_configModel = fileLoader;
		this.m_agentIdentity = agentIdentity;

		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(690, 480));
		this.setMinimumSize(new Dimension(300, 200));

		nameLabel = new JLabel(m_resources.getString("TestNamePanel.nameLabel"),
				SwingConstants.RIGHT);
		stateLabel = new JLabel(m_resources.getString("TestNamePanel.commentLabel"),
				SwingConstants.RIGHT);

		DocumentListener doclistener = new DocListener();
		name = new JTextField();
		name.getDocument().addDocumentListener(doclistener);

		state = new JTextField();
		state.getDocument().addDocumentListener(doclistener);

		this.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1));
		this.add(name, new GridBagConstraints(1, 0, 1, 1, 100.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));

		this.add(stateLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1));
		this.add(state, new GridBagConstraints(1, 1, 1, 1, 100.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));
		
		m_configModel.addListener(new ConfigModel.AbstractListener() {
			public void isArgsChanged() {
				resetConfig();
			}
		});

	}
	
	protected void resetConfig() {
	}

	public void setConfigue() {

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
