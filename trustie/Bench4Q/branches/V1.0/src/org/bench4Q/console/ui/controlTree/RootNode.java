package org.bench4Q.console.ui.controlTree;

import javax.swing.ImageIcon;

import org.bench4Q.console.common.Resources;

public class RootNode implements TestElement {

	private final Resources m_resources;

	public RootNode(Resources resources) {
		m_resources = resources;
	}

	public String getTip() {
		return m_resources.getString("ControlTree.globalsetting.tip");
	}

	public String getName() {
		return m_resources.getString("ControlTree.globalsetting.title");
	}

	public ImageIcon getImageIcon() {
		final ImageIcon imageIcon = m_resources.getImageIcon("ControlTree.globalsetting.image");
		if (imageIcon != null) {
			return imageIcon;
		}
		return null;
	}

	public String toString() {
		return m_resources.getString("ControlTree.globalsetting.title");
	}

}