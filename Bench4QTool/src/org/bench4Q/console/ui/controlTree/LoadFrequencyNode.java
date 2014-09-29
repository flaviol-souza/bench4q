package org.bench4Q.console.ui.controlTree;

import javax.swing.ImageIcon;

import org.bench4Q.console.common.Resources;

public class LoadFrequencyNode implements TestElement {

	private final Resources m_resources;

	/**
	 * @param resources
	 */
	public LoadFrequencyNode(Resources resources) {
		m_resources = resources;
	}

	@Override
	public String getTip() {
		return m_resources.getString("ControlTree.LoadFrequency.tip");
	}

	@Override
	public String getName() {
		return m_resources.getString("ControlTree.LoadFrequency.title");
	}

	@Override
	public ImageIcon getImageIcon() {
		final ImageIcon imageIcon = m_resources
				.getImageIcon("ControlTree.LoadFrequency.image");
		if (imageIcon != null) {
			return imageIcon;
		}
		return null;
	}

	public String toString() {
		return m_resources.getString("ControlTree.LoadFrequency.title");
	}
}
