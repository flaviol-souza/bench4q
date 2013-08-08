package org.bench4Q.console.ui.section;

import javax.annotation.Resource;
import javax.swing.JPanel;

import org.bench4Q.console.common.Resources;
import org.bench4Q.console.model.ConfigModel;

/**
 * @author wangsa
 *
 */
public class M_ServerPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3260189767038013579L;
	private final Resources m_resources;
	private ConfigModel m_fileLoader;
	
	/**
	 * @param resources
	 * @param fileLoader
	 */
	public M_ServerPanel(Resources resources, ConfigModel fileLoader) {
		m_resources = resources;
		m_fileLoader = fileLoader;
	}

}
