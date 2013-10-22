package org.bench4Q.console.ui.controlTree;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.console.ui.transfer.AgentInfo;

public class Bench4QTreeNode extends DefaultMutableTreeNode implements NamedTreeNode {

	private Bench4QTreeModel m_treeModel;
	private AgentNode agentNode;

	private Boolean TotalOrNot;
	private AgentIdentity m_agentIdentity;
	
	private JPanel m_panel;

	public Bench4QTreeNode() {
		this(null, null, null, null, null);
	}

	public Bench4QTreeNode(TestElement userObj, Bench4QTreeModel treeModel, Boolean TotalOrNot,
			AgentIdentity agentIdentity, JPanel panel) {
		super(userObj);
		if (userObj instanceof AgentNode) {
			agentNode = (AgentNode) userObj;
		}
		this.m_treeModel = treeModel;
		this.TotalOrNot = TotalOrNot;
		this.m_agentIdentity = agentIdentity;
		m_panel = panel;
	}

	public ImageIcon getIcon() {
		return getIcon(true);
	}

	public ImageIcon getIcon(boolean enabled) {
		TestElement testElement = getTestElement();
		return testElement.getImageIcon();

	}

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

	public AgentInfo getAgentInfo() {
		return agentNode.getAgentInfo();
	}

	public Boolean getTotalOrNot() {
		return TotalOrNot;
	}

	public AgentIdentity getAgentIdentity() {
		return m_agentIdentity;
	}

	public JPanel getPanel() {
		return m_panel;
	}

	public void setPanel(JPanel panel) {
		this.m_panel = panel;
	}

}
