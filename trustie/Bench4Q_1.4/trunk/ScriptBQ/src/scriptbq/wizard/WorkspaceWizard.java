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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.wizard.Wizard;

import scriptbq.Activator;
import scriptbq.BqConstant;
/**
 * This class is the wizard of choosing workspace  before the window opens
 *
 */
public class WorkspaceWizard extends Wizard {

	/**
	 * wizard page
	 */
	private WorkspaceWizardPage workspaceWizardPage;
	/**
	 * file path of the metadata file which stores workspace information
	 */
	private String tempPath;
	/**
	 * DialogSettigns to restore workspace information
	 */
	private DialogSettings ConfigSetting;
	
	public WorkspaceWizard(){
		this.setWindowTitle("Workspace Launcher");
		ConfigSetting = new DialogSettings("Workspace");
	}
	@Override
	public void addPages(){
		workspaceWizardPage = new WorkspaceWizardPage("Workspace");
		addPage(workspaceWizardPage);		
	}
	public boolean performFinish() {
		// TODO Auto-generated method stub
		BqConstant.BqRootPath=workspaceWizardPage.getWorkspace();
		if(workspaceWizardPage.getDefault()){
			String rootpath="";
			try {
				rootpath = FileLocator.toFileURL(
						Platform.getBundle(Activator.PLUGIN_ID )
								.getEntry("")).getPath().toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
			tempPath=rootpath+"metadata.xml";
			ConfigSetting.put("workspace",BqConstant.BqRootPath);
				try {
					ConfigSetting.save(tempPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return true;
	}

}
