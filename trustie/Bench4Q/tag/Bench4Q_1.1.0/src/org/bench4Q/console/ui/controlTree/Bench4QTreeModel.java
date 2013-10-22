/**
 * =========================================================================
 * 					Bench4Q version 1.0.0
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
package org.bench4Q.console.ui.controlTree;

import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.model.ConfigModel;
import org.bench4Q.console.model.ResultModel;
import org.bench4Q.console.ui.HashTree;
import org.bench4Q.console.ui.ListedHashTree;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.bench4Q.console.ui.section.M_AgentMainPanel;
import org.bench4Q.console.ui.section.M_AnalysisMatrixPanel;
import org.bench4Q.console.ui.section.M_GlobalSettingPanel;
import org.bench4Q.console.ui.section.M_LoadSimulatorPanel;
import org.bench4Q.console.ui.section.M_LoadWorkerPanel;
import org.bench4Q.console.ui.section.M_PerformancePanel;
import org.bench4Q.console.ui.section.M_QoSPanel;
import org.bench4Q.console.ui.section.M_UserSettingPanel;
import org.bench4Q.console.ui.transfer.AgentInfo;
import org.bench4Q.console.ui.transfer.AgentInfoObserver;
import org.bench4Q.console.ui.transfer.AgentsCollection;
import org.bench4Q.console.ui.transfer.AgentsCollectionSubject;

public class Bench4QTreeModel extends DefaultTreeModel implements AgentInfoObserver {

	/**
	 * 2009-7-24 author: duanzhiquan Technology Center for Software Engineering
	 * Institute of Software, Chinese Academy of Sciences Beijing 100190, China
	 * Email:duanzhiquan07@otcaix.iscas.ac.cn
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1005728548365959491L;

	JTree m_tree;
	private final Resources m_resources;
	private final ProcessControl m_processControl;
	private final AgentsCollectionSubject m_subject;
	private final SwingDispatcherFactory m_swingDispatcherFactory;
	private ConfigModel m_configModel;
	private ResultModel m_resultModel;

	public Bench4QTreeModel(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory swingDispatcherFactory, ConfigModel configModel,
			ResultModel resultModel, AgentsCollectionSubject subject) throws ConsoleException {
		super(
				new DefaultMutableTreeNode(resources
						.getString("ControlTree.ROOT_ELEMENT_NAME.title")));
		m_resources = resources;
		m_processControl = processControl;
		m_swingDispatcherFactory = swingDispatcherFactory;
		m_configModel = configModel;
		m_resultModel = resultModel;
		m_subject = subject;
		m_subject.registerObserver(this);
		initTree();
	}

	public JTree getTree() {
		return m_tree;
	}

	public void setTree(JTree tree) {
		this.m_tree = tree;
	}

	public HashTree getCurrentSubTree(Bench4QTreeNode node) {
		ListedHashTree hashTree = new ListedHashTree(node);
		Enumeration enumNode = node.children();
		while (enumNode.hasMoreElements()) {
			Bench4QTreeNode child = (Bench4QTreeNode) enumNode.nextElement();
			hashTree.add(node, getCurrentSubTree(child));
		}
		return hashTree;
	}

	public HashTree getTestPlan() {
		return getCurrentSubTree((Bench4QTreeNode) ((Bench4QTreeNode) this.getRoot()).getChildAt(0));
	}

	private void initTree() throws ConsoleException {
		// Insert the test plan node
		JPanel globalSettingPanel = new M_GlobalSettingPanel(m_resources, m_processControl,
				m_swingDispatcherFactory, m_configModel);
		Bench4QTreeNode RootNode = new Bench4QTreeNode(new RootNode(m_resources), this, true, null,
				globalSettingPanel);

		JPanel loadSimulatorPanel = new M_LoadSimulatorPanel(m_resources, m_configModel);
		Bench4QTreeNode LoadSimulatorNode = new Bench4QTreeNode(new LoadSimulatorNode(m_resources),
				this, true, null, loadSimulatorPanel);

		JPanel loadWorkerPanel = new M_LoadWorkerPanel(m_resources, m_processControl,
				m_swingDispatcherFactory, m_configModel, (AgentsCollection) m_subject);
		Bench4QTreeNode LoadWorkerNode = new Bench4QTreeNode(new LoadWorkerNode(m_resources), this,
				true, null, loadWorkerPanel);

		JPanel userSettingPanel = new M_UserSettingPanel(m_resources, m_configModel);
		Bench4QTreeNode UserNode = new Bench4QTreeNode(new UserNode(m_resources), this, true, null,
				userSettingPanel);

		JPanel analysisMatrixPanel = new M_AnalysisMatrixPanel(m_resources, m_configModel);
		Bench4QTreeNode AnalysisMatrixNode = new Bench4QTreeNode(
				new AnalysisMatrixNode(m_resources), this, true, null, analysisMatrixPanel);

		JPanel performancePanel = new M_PerformancePanel(m_resources, m_processControl,
				m_swingDispatcherFactory, m_configModel, true, null, (AgentsCollection) m_subject);
		Bench4QTreeNode PerformanceNode = new Bench4QTreeNode(new PerformanceNode(m_resources),
				this, true, null, performancePanel);

		JPanel QoSPanel = new M_QoSPanel(m_resources, m_processControl, m_swingDispatcherFactory,
				m_configModel, true, null, (AgentsCollection) m_subject);
		Bench4QTreeNode QoSNode = new Bench4QTreeNode(new QoSNode(m_resources), this, true, null,
				QoSPanel);

		insertNodeInto(RootNode, (MutableTreeNode) getRoot(), 0);

		insertNodeInto(LoadSimulatorNode, (Bench4QTreeNode) RootNode, 0);
		insertNodeInto(AnalysisMatrixNode, (Bench4QTreeNode) RootNode, 1);

		insertNodeInto(LoadWorkerNode, (Bench4QTreeNode) LoadSimulatorNode, 0);
		insertNodeInto(UserNode, (Bench4QTreeNode) LoadSimulatorNode, 1);

		insertNodeInto(PerformanceNode, (Bench4QTreeNode) AnalysisMatrixNode, 0);
		insertNodeInto(QoSNode, (Bench4QTreeNode) AnalysisMatrixNode, 1);

		// Let others know that the tree content has changed.
		// This should not be necessary, but without it, nodes are not shown
		// when the user
		// uses the Close menu item
		nodeStructureChanged((MutableTreeNode) getRoot());
		// expandAll(m_tree, new TreePath(getRoot()), true);
	}

	private void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		} // Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

	public void addAgent(AgentInfo agentInfo) throws ConsoleException {

		JPanel agentMainPanel = new M_AgentMainPanel(m_resources, m_processControl,
				m_swingDispatcherFactory, m_configModel, agentInfo.getAgentIdentity());
		Bench4QTreeNode AgentNode = new Bench4QTreeNode(new AgentNode(agentInfo, m_resources),
				this, false, agentInfo.getAgentIdentity(), agentMainPanel);

		JPanel loadSimulatorPanel = new M_LoadSimulatorPanel(m_resources, m_configModel);
		Bench4QTreeNode LoadSimulatorNode = new Bench4QTreeNode(new LoadSimulatorNode(m_resources),
				this, false, agentInfo.getAgentIdentity(), loadSimulatorPanel);

		JPanel loadWorkerPanel = new M_LoadWorkerPanel(m_resources, m_processControl,
				m_swingDispatcherFactory, m_configModel, (AgentsCollection) m_subject);
		Bench4QTreeNode LoadWorkerNode = new Bench4QTreeNode(new LoadWorkerNode(m_resources), this,
				false, agentInfo.getAgentIdentity(), loadWorkerPanel);

		JPanel userSettingPanel = new M_UserSettingPanel(m_resources, m_configModel);
		Bench4QTreeNode UserNode = new Bench4QTreeNode(new UserNode(m_resources), this, false,
				agentInfo.getAgentIdentity(), userSettingPanel);

		JPanel analysisMatrixPanel = new M_AnalysisMatrixPanel(m_resources, m_configModel);
		Bench4QTreeNode AnalysisMatrixNode = new Bench4QTreeNode(
				new AnalysisMatrixNode(m_resources), this, false, agentInfo.getAgentIdentity(),
				analysisMatrixPanel);

		JPanel performancePanel = new M_PerformancePanel(m_resources, m_processControl,
				m_swingDispatcherFactory, m_configModel, false, agentInfo.getAgentIdentity(),
				(AgentsCollection) m_subject);
		Bench4QTreeNode PerformanceNode = new Bench4QTreeNode(new PerformanceNode(m_resources),
				this, false, agentInfo.getAgentIdentity(), performancePanel);

		JPanel QoSPanel = new M_QoSPanel(m_resources, m_processControl, m_swingDispatcherFactory,
				m_configModel, false, agentInfo.getAgentIdentity(), (AgentsCollection) m_subject);
		Bench4QTreeNode QoSNode = new Bench4QTreeNode(new QoSNode(m_resources), this, false,
				agentInfo.getAgentIdentity(), QoSPanel);

		nodeStructureChanged((MutableTreeNode) getRoot());
		expandAll(m_tree, new TreePath(getRoot()), true);
	}

	public void removeAgent(AgentInfo agentInfo) {
		Enumeration enumNode = ((DefaultMutableTreeNode) getRoot()).children();
		while (enumNode.hasMoreElements()) {
			Bench4QTreeNode child = (Bench4QTreeNode) enumNode.nextElement();
			if (child.getAgentIdentity().equals(agentInfo.getAgentIdentity())) {
				removeNodeFromParent((MutableTreeNode) enumNode);
			}
		}
		nodeStructureChanged((MutableTreeNode) getRoot());

	}

	public void getResult(AgentInfo created) {
		created.getAgentIdentity();

	}

	public void restartTest() {
	}

}
