package org.bench4Q.console.ui.section;

import javax.swing.JPanel;

import org.bench4Q.common.util.XMlFileLoader;
import org.bench4Q.console.common.Resources;

public class M_AnalysisMatrixPanel extends JPanel {

	private final Resources m_resources;
	private XMlFileLoader m_fileLoader;

	public M_AnalysisMatrixPanel(Resources resources, XMlFileLoader fileLoader) {

		m_resources = resources;
		m_fileLoader = fileLoader;
	}

}
