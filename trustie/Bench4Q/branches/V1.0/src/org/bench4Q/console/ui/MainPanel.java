package org.bench4Q.console.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import org.bench4Q.console.common.Resources;

public class MainPanel extends JPanel {

	// BorderLayout borderLayout = new BorderLayout();
	JPanel panel = new JPanel();

	private final Resources m_resources;

	public MainPanel(Resources resources) {
		m_resources = resources;
	}

	private void jbInit() throws Exception {

		this.setLayout(new GridBagLayout());
		// this.add(panel, java.awt.BorderLayout.CENTER);
		this.add(panel, new GridBagConstraints(0, 0, 1, 1, 100.0, 100.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	}

	public void setShowForm(JPanel otherpanel) {
		this.remove(panel);
		panel = otherpanel;
		panel.setEnabled(true);

		this.add(panel, new GridBagConstraints(0, 0, 1, 1, 100.0, 100.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		this.updateUI();
	}

}
