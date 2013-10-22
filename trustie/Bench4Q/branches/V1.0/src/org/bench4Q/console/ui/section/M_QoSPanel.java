package org.bench4Q.console.ui.section;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.common.util.XMlFileLoader;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.bench4Q.console.ui.transfer.AgentsCollection;

public class M_QoSPanel extends JPanel {

	private final Resources m_resources;
	private XMlFileLoader m_fileLoader;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;
	private AgentsCollection m_agentsCollection;

	public M_QoSPanel(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, XMlFileLoader fileLoader,
			boolean getTotalOrNot, AgentIdentity agentIdentity, AgentsCollection agentsCollection) throws ConsoleException {
		m_resources = resources;
		m_fileLoader = fileLoader;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;
		m_agentsCollection = agentsCollection;

		final JTabbedPane qualitySectionTabbedPane = new JTabbedPane();
		this.setLayout(new BorderLayout());
		this.add(qualitySectionTabbedPane, BorderLayout.CENTER);

		JPanel sessionSection = new Q_SessionSection(m_resources, m_processControl,
				m_swingDispatcherFactory, getTotalOrNot, agentIdentity,
				m_agentsCollection);
		// testResultSessionSection
		// .setBorder(createTitledBorder("testResultPicSessionSection.tip"));
		qualitySectionTabbedPane.addTab(m_resources.getString("testResultPicSessionSection.title"),
				m_resources.getImageIcon("testResultPicSessionSection.image"), sessionSection,
				m_resources.getString("testResultPicSessionSection.tip"));

		JPanel testResultBenefitSection = new Q_BenefitSection(m_resources, getTotalOrNot, agentIdentity, m_agentsCollection);
		// testResultBenefitSection
		// .setBorder(createTitledBorder("testResultBenefitSection.tip"));
		qualitySectionTabbedPane.addTab(m_resources.getString("testResultBenefitSection.title"),
				m_resources.getImageIcon("testResultBenefitSection.image"),
				testResultBenefitSection, m_resources.getString("testResultBenefitSection.tip"));

	}

}
