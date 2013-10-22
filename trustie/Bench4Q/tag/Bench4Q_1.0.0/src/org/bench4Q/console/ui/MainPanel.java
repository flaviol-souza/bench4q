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
