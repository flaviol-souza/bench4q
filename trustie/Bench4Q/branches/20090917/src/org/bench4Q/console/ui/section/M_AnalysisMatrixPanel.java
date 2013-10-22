package org.bench4Q.console.ui.section;

import javax.swing.JPanel;

import org.bench4Q.console.common.Resources;
import org.bench4Q.console.model.ConfigModel;

public class M_AnalysisMatrixPanel extends JPanel {

	private final Resources m_resources;
	private ConfigModel m_fileLoader;

	public M_AnalysisMatrixPanel(Resources resources, ConfigModel fileLoader) {

		m_resources = resources;
		m_fileLoader = fileLoader;
	}

}
