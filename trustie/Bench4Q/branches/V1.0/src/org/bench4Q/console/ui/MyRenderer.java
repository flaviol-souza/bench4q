package org.bench4Q.console.ui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.bench4Q.console.ui.controlTree.TestElement;

public class MyRenderer extends DefaultTreeCellRenderer {

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
			boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		ImageIcon icon = null;
		if (node.getUserObject() instanceof TestElement) {
			TestElement data = (TestElement) node.getUserObject();
			icon = data.getImageIcon();
		}

		this.setIcon(icon);
		return this;
	}
}