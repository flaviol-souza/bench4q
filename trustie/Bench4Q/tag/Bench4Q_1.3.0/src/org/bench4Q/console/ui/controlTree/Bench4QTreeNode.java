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
package org.bench4Q.console.ui.controlTree;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.console.ui.transfer.AgentInfo;

/**
 * @author duanzhiquan
 * 
 */
public class Bench4QTreeNode extends DefaultMutableTreeNode implements
		NamedTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8151364295522063549L;
	private Bench4QTreeModel m_treeModel;
	private AgentNode agentNode;

	private Boolean TotalOrNot;
	private AgentIdentity m_agentIdentity;

	private JPanel m_panel;

	/**
	 * 
	 */
	public Bench4QTreeNode() {
		this(null, null, null, null, null);
	}

	/**
	 * @param userObj
	 * @param treeModel
	 * @param TotalOrNot
	 * @param agentIdentity
	 * @param panel
	 */
	public Bench4QTreeNode(TestElement userObj, Bench4QTreeModel treeModel,
			Boolean TotalOrNot, AgentIdentity agentIdentity, JPanel panel) {
		super(userObj);
		if (userObj instanceof AgentNode) {
			agentNode = (AgentNode) userObj;
		}
		this.m_treeModel = treeModel;
		this.TotalOrNot = TotalOrNot;
		this.m_agentIdentity = agentIdentity;
		m_panel = panel;
	}

	/**
	 * @return
	 */
	public ImageIcon getIcon() {
		return getIcon(true);
	}

	/**
	 * @param enabled
	 * @return
	 */
	public ImageIcon getIcon(boolean enabled) {
		TestElement testElement = getTestElement();
		return testElement.getImageIcon();

	}

	/**
	 * @return
	 */
	public TestElement getTestElement() {
		return (TestElement) getUserObject();
	}

	public String getName() {
		return ((TestElement) getUserObject()).getName();
	}

	public void nameChanged() {
		m_treeModel.nodeChanged(this);
	}

	public void setName(String name) {
	}

	/**
	 * @return
	 */
	public AgentInfo getAgentInfo() {
		return agentNode.getAgentInfo();
	}

	/**
	 * @return
	 */
	public Boolean getTotalOrNot() {
		return TotalOrNot;
	}

	/**
	 * @return
	 */
	public AgentIdentity getAgentIdentity() {
		return m_agentIdentity;
	}

	/**
	 * @return
	 */
	public JPanel getPanel() {
		return m_panel;
	}

	/**
	 * @param panel
	 */
	public void setPanel(JPanel panel) {
		this.m_panel = panel;
	}

}
