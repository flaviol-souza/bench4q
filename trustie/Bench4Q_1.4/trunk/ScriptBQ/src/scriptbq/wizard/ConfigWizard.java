/**
 * =========================================================================
 * 					Bench4Q_Script version 1.3.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at  
 * http://www.trustie.net/projects/project/show/Bench4Q
 * You can find latest version there. 
 * Bench4Q_Script adds a script module for Internet application to Bench4Q
 * http://www.trustie.com/projects/project/show/Bench4Q_Script
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
 *  * Initial developer(s): Wangsa , Tianfei , WUYulong , Zhufeng
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */


package scriptbq.wizard;

import java.io.IOException;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.wizard.Wizard;

import scriptbq.BqConstant;
import scriptbq.tree.BqTreeParent;

/**
 * This class is wizard to change tenant's configuration.
 *
 */
public class ConfigWizard extends Wizard{

	/**
	 * The wizard page of this wizard.
	 */
	private ConfigWizardPage configWizardPage;
	/**
	 * The file path of configuration file.
	 */
	private String tempPath;
	/**
	 * The DialogSettings used to restore configuration information.
	 */
	private DialogSettings ConfigSetting;
	/**
	 * The tenant which was chosen.
	 */
	private BqTreeParent selection;
	
	/**
	 * Constructor
	 * @param selection  The tenant which was chosen.
	 */
	public ConfigWizard(BqTreeParent selection){
		this.selection=selection;
		this.setWindowTitle("Configuration");
		tempPath = BqConstant.BqTreePath+"/"+selection.getName()+"/config.xml";
		ConfigSetting = new DialogSettings("Config");
	}
	/**
	 * addPages
	 */
	public void addPages() {
		configWizardPage = new ConfigWizardPage(tempPath);		
    	addPage(configWizardPage); 
    }
	/**
	 * The perform that will be done after the wizard finished.
	 */
	public boolean performFinish() {
		selection.setStdyTime(Long.valueOf(configWizardPage.getStdyTime()).longValue());
		ConfigSetting.put("baseLoad", configWizardPage.getBaseLoad());
		ConfigSetting.put("stdyTime", configWizardPage.getStdyTime());
		ConfigSetting.put("baseURL", configWizardPage.getBaseURL());
		ConfigSetting.put("prepairTime", configWizardPage.getprepairTime());
		ConfigSetting.put("cooldown", configWizardPage.getcooldown());
		ConfigSetting.put("isScript", configWizardPage.getscript());
		ConfigSetting.put("mix", configWizardPage.getmix());
		ConfigSetting.put("thinkTime", configWizardPage.getthinktime());
		ConfigSetting.put("STOperiod", 60000);
		ConfigSetting.put("STOdelay", 30000);
		ConfigSetting.put("tenant", selection.getName());
			try {
				ConfigSetting.save(tempPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return true;
	}

}
