package org.bench4Q.console.ui.controlTree;

import javax.swing.ImageIcon;

import org.bench4Q.console.common.Resources;

public class LoadWorkerNode implements TestElement {

	private final Resources m_resources;

	public LoadWorkerNode(Resources resources) {
		m_resources = resources;
	}

	public String getTip() {
		return m_resources.getString("ControlTree.LoadWorkerNode.tip");
	}

	public String getName() {
		return m_resources.getString("ControlTree.LoadWorkerNode.title");
	}

	public ImageIcon getImageIcon() {
		final ImageIcon imageIcon = m_resources.getImageIcon("ControlTree.LoadWorkerNode.image");
		if (imageIcon != null) {
			return imageIcon;
		}
		return null;
	}

	public String toString() {
		return m_resources.getString("ControlTree.LoadWorkerNode.title");
	}
}