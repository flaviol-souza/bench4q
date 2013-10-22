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
package scriptbq;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import scriptbq.tree.BqTreeObject;
import scriptbq.tree.BqTreeParent;
import scriptbq.tree.BqTreeView;
import scriptbq.wizard.WorkspaceWizard;

/**
 * The class configures a workbench window. 
 * The workbench window advisor object is created in response to a workbench window
 * being created (one per window), and is used to configure the window
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	/**
	 * constructor
	 * @param configurer
	 */
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    /**
     * Method to create actionbar
     */
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    /**
     * Method to make workSpace for Bench4Q_Script, it is used to save some files
     * @param rootPath
     */
    private void makeWorkSpace(String rootPath){
    	File workSpace = new File(rootPath);
    	if(!workSpace.exists()){
    		workSpace.mkdir();
    	}
    }
    
    /**
     * Method to save the workSpace
     * @param rootPath
     */
    private void saveWorkSpace(String rootPath) {
		Calendar c = Calendar.getInstance();
		String suffix = "("+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE)+
						"-"+c.get(Calendar.HOUR)+"-"+c.get(Calendar.MINUTE)+"-"+c.get(Calendar.SECOND)+")";
		File workSpace = new File(rootPath);
		if(workSpace.exists()){
			workSpace.renameTo(new File(rootPath+suffix));
		}
	}
  
    /**
     * Method to delete the workSpace
     * @param workSpace
     */
    private void deleteWorkSpace(File workSpace) {

    	   if(workSpace.exists()){                   
    		    if(workSpace.isFile()){                   
    		    	workSpace.delete();                     
    		    }else if(workSpace.isDirectory()){         
    		     File files[] = workSpace.listFiles();          
    		     for(int i=0;i<files.length;i++){          
    		      this.deleteWorkSpace(files[i]);         
    		     } 
    		    } 
    		    workSpace.delete(); 
    		   }else{ 
    		    System.out.println("not exist!"+'\n'); 
    		   } 
    } 

    
    /**
     * Method for preparations before the window is open
     */
    public void preWindowOpen() {
    	String rootpath="";
		try {
			rootpath = FileLocator.toFileURL(
					Platform.getBundle(Activator.PLUGIN_ID )
							.getEntry("")).getPath().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String tempPath=rootpath+"metadata.xml";
		File tmp = new File(tempPath);
		if(tmp.exists()){
			DialogSettings ConfigSetting = new DialogSettings(null);
			try {
				ConfigSetting.load(tempPath);
				BqConstant.BqRootPath=ConfigSetting.get("workspace");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			WorkspaceWizard wizard = new WorkspaceWizard();
			WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(),
	                wizard);
			dialog.setPageSize(200, 110);
			dialog.create();
			int i=dialog.open();
			if(i==Window.CANCEL){
				System.exit(-1);
			}
		}
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(960, 650));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setTitle("Bench4Q");
        makeWorkSpace(BqConstant.BqRootPath);   
        BqConstant.BqTreePath = BqConstant.BqRootPath+"/TenantGroup";
    }
    
    /**
     * Method before the window is closed
     */
    public boolean preWindowShellClose(){
    	String Info = "Do you want to save this test?";
    	boolean result;
    	BqTreeParent tenantgroup=null;
    	BqTreeView viewer = (BqTreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
    	        .findView(BqTreeView.TREEVIEW_ID);
    	BqTreeParent root = (BqTreeParent)viewer.getTreeViewer().getInput();
    	BqTreeObject[] children = root.getChildren();
    	for(BqTreeObject tmp:children){
    		if(tmp instanceof BqTreeParent && tmp.getName().equals("TenantGroup")){
    			tenantgroup = (BqTreeParent)tmp;
    			break;
    		}
    	}
    	if(tenantgroup==null||tenantgroup.getChildren()==null||tenantgroup.getChildren().length==0){
    		result=false;
    	}
    	else if(BqConstant.Save){
    		result=true;
    	}else{
    		result=MessageDialog.openQuestion(
    				null,
    				null,
    				Info);
    	}
    	if(result){
    		saveWorkSpace(BqConstant.BqRootPath+"/TenantGroup");
    	}
    	else{
    		File workSpace = new File(BqConstant.BqRootPath+"/TenantGroup");
    		deleteWorkSpace(workSpace);
    	}
    	return true;
    }
}
