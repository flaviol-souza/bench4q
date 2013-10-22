package org.bench4Q.console.ui.section;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.bench4Q.common.util.XMlFileLoader;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.ui.SwingDispatcherFactory;

public class M_LoadWorkerPanel extends JPanel {

	private final Resources m_resources;
	private XMlFileLoader m_fileLoader;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;

	public M_LoadWorkerPanel(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, XMlFileLoader fileLoader)
			throws ConsoleException {

		m_resources = resources;
		m_fileLoader = fileLoader;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;

		final JTabbedPane LoadWorkerPane = new JTabbedPane();
		this.setLayout(new GridBagLayout());

		this.add(LoadWorkerPane, new GridBagConstraints(0, 0, 1, 1, 100.0, 100.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		JPanel configLoadSection = new LW_ConfigLoadSection(m_resources, this.m_fileLoader);
		// configLoadSection.setBorder(createTitledBorder("configLoadSection.tip"));
		LoadWorkerPane.addTab(m_resources.getString("configLoadSection.title"), m_resources
				.getImageIcon("configLoadSection.image"), configLoadSection, m_resources
				.getString("configLoadSection.tip"));

		JPanel configLoadShowSection = new LW_ConfigLoadShowSection(m_resources, m_processControl,
				m_swingDispatcherFactory);
		// configLoadShowSection.setBorder(createTitledBorder("configLoadShowSection.tip"));
		LoadWorkerPane.addTab(m_resources.getString("configLoadShowSection.title"), m_resources
				.getImageIcon("configLoadShowSection.image"), configLoadShowSection, m_resources
				.getString("configLoadShowSection.tip"));

	}

}
