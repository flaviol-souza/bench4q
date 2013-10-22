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

public class M_PerformancePanel extends JPanel {

	private final Resources m_resources;
	private XMlFileLoader m_fileLoader;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;
	private AgentsCollection m_agentsCollection;

	public M_PerformancePanel(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, XMlFileLoader fileLoader,
			boolean getTotalOrNot, AgentIdentity agentIdentity, AgentsCollection agentsCollection) throws ConsoleException {

		m_resources = resources;
		m_fileLoader = fileLoader;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;
		m_agentsCollection = agentsCollection;

		JTabbedPane testResultPicTablePane = new JTabbedPane();

		JPanel testResultPicWIPSSection = new P_WIPSSection(m_resources, m_processControl,
				m_swingDispatcherFactory, getTotalOrNot, agentIdentity,
				m_agentsCollection);
		// testResultPicWIPSSection
		// .setBorder(createTitledBorder("testResultPicWIPSSection.tip"));
		testResultPicTablePane.addTab(m_resources.getString("testResultPicWIPSSection.title"),
				m_resources.getImageIcon("testResultPicWIPSSection.image"),
				testResultPicWIPSSection, m_resources.getString("testResultPicWIPSSection.tip"));

		JPanel testResultPicWIRTSection = new P_WIRTSection(m_resources, m_processControl,
				m_swingDispatcherFactory, getTotalOrNot, agentIdentity,
				m_agentsCollection);
		// testResultPicWIRTSection
		// .setBorder(createTitledBorder("testResultPicWIRTSection.tip"));
		testResultPicTablePane.addTab(m_resources.getString("testResultPicWIRTSection.title"),
				m_resources.getImageIcon("testResultPicWIRTSection.image"),
				testResultPicWIRTSection, m_resources.getString("testResultPicWIRTSection.tip"));

		JPanel testResultPicErrorSection = new P_ErrorSection(m_resources, m_processControl, getTotalOrNot, agentIdentity, m_agentsCollection);
		// testResultPicErrorSection
		// .setBorder(createTitledBorder("testResultPicErrorSection.tip"));
		testResultPicTablePane.addTab(m_resources.getString("testResultPicErrorSection.title"),
				m_resources.getImageIcon("testResultPicErrorSection.image"),
				testResultPicErrorSection, m_resources.getString("testResultPicErrorSection.tip"));

		this.setLayout(new BorderLayout());
		this.add(testResultPicTablePane, BorderLayout.CENTER);
	}

}
