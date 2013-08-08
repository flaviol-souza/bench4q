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

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.model.ConfigModel;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.bench4Q.console.ui.controlTree.Type;
import org.bench4Q.console.ui.transfer.AgentsCollection;

/**
 * @author duanzhiquan
 * 
 */
public class M_ErrorPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5448849142776995211L;
	private final Resources m_resources;
	private ConfigModel m_fileLoader;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;
	private AgentsCollection m_agentsCollection;

	/**
	 * @param resources
	 * @param processControl
	 * @param dispatcherFactory
	 * @param fileLoader
	 * @param getTotalOrNot
	 * @param agentIdentity
	 * @param agentsCollection
	 * @throws ConsoleException
	 */
	public M_ErrorPanel(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, ConfigModel fileLoader,
			boolean getTotalOrNot, AgentIdentity agentIdentity,
			AgentsCollection agentsCollection, Type type) throws ConsoleException {
		m_resources = resources;
		m_fileLoader = fileLoader;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;
		m_agentsCollection = agentsCollection;

		final JTabbedPane errorSectionTabbedPane = new JTabbedPane();
		this.setLayout(new BorderLayout());
		this.add(errorSectionTabbedPane, BorderLayout.CENTER);

		JPanel testResultPicErrorSection = new E_ErrorSection(m_resources,
				m_processControl, getTotalOrNot, agentIdentity,
				m_agentsCollection, type);
		// testResultPicErrorSection
		// .setBorder(createTitledBorder("testResultPicErrorSection.tip"));
		errorSectionTabbedPane.addTab(m_resources
				.getString("testResultPicErrorSection.title"), m_resources
				.getImageIcon("testResultPicErrorSection.image"),
				testResultPicErrorSection, m_resources
						.getString("testResultPicErrorSection.tip"));

	}

}
