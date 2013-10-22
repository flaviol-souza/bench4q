/**
 * =========================================================================
 * 					Bench4Q version 1.1.1
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.model.ConfigModel;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.bench4Q.console.ui.transfer.AgentsCollection;

/**
 * @author duanzhiquan
 *
 */
public class M_LoadWorkerPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7257661569069751992L;
	private final Resources m_resources;
	private ConfigModel m_configModel;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;

	/**
	 * @param resources
	 * @param processControl
	 * @param dispatcherFactory
	 * @param configModel
	 * @param agentsCollection
	 * @throws ConsoleException
	 */
	public M_LoadWorkerPanel(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, ConfigModel configModel,
			AgentsCollection agentsCollection) throws ConsoleException {

		m_resources = resources;
		m_configModel = configModel;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;

		final JTabbedPane LoadWorkerPane = new JTabbedPane();
		this.setLayout(new GridBagLayout());

		this.add(LoadWorkerPane, new GridBagConstraints(0, 0, 1, 1, 100.0, 100.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		JPanel configLoadSection = new LW_ConfigLoadSection(m_resources, m_configModel);
		// configLoadSection.setBorder(createTitledBorder("configLoadSection.tip"));
		LoadWorkerPane.addTab(m_resources.getString("configLoadSection.title"), m_resources
				.getImageIcon("configLoadSection.image"), configLoadSection, m_resources
				.getString("configLoadSection.tip"));

		JPanel configLoadShowSection = new LW_ConfigLoadShowSection(m_resources, m_processControl,
				m_swingDispatcherFactory, m_configModel, (LW_ConfigLoadSection) configLoadSection);
		// configLoadShowSection.setBorder(createTitledBorder("configLoadShowSection.tip"));
		LoadWorkerPane.addTab(m_resources.getString("configLoadShowSection.title"), m_resources
				.getImageIcon("configLoadShowSection.image"), configLoadShowSection, m_resources
				.getString("configLoadShowSection.tip"));

		JPanel RealLoadShowSection = new LW_RealLoadShowSection(m_resources, m_processControl,
				agentsCollection);
		LoadWorkerPane.addTab(m_resources.getString("RealLoadShowSection.title"), m_resources
				.getImageIcon("RealLoadShowSection.image"), RealLoadShowSection, m_resources
				.getString("RealLoadShowSection.tip"));
	}

}
