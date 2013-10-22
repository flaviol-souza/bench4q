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
package scriptbq.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import scriptbq.Activator;
import scriptbq.resource.ImageRegistry;
import scriptbq.tree.BqTreeObject;
import scriptbq.tree.BqTreeParent;
import scriptbq.tree.BqTreeView;
import scriptbq.wizard.ConfigWizard;
import scriptbq.wizard.NewAgentWizard;
import scriptbq.wizard.NewTenantWizard;

/**
 * class used for creating new tenant
 */
public class NewTenantAction extends Action{
	
	private BqTreeParent selection;
	
	/**
	 * constructor, when click the toolbar
	 */
	public NewTenantAction(){
		super();
		setText("NewTenant");		
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		         ImageRegistry.getImagePath(ImageRegistry.NEWTENANT)));
		
	}
	
	/**
	 * constructor when click on the tree
	 * @param selection
	 */
	public NewTenantAction(BqTreeParent selection){
		super();
		this.selection = selection;
		setText("NewTenant");		
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		         ImageRegistry.getImagePath(ImageRegistry.NEWTENANT)));
	}
	
	public void run(){
		
		if(selection==null){
			BqTreeView viewer = (BqTreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
	        .findView(BqTreeView.TREEVIEW_ID);
			BqTreeParent root = (BqTreeParent)viewer.getTreeViewer().getInput();
			BqTreeObject[] children = root.getChildren();
			for(BqTreeObject tmp:children){
				if(tmp instanceof BqTreeParent && tmp.getName().equals("TenantGroup")){
					selection = (BqTreeParent)tmp;
					break;
				}
			}		
		}
		NewTenantWizard wizard = new NewTenantWizard(selection);
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(),
				                 wizard);
		dialog.create();
		int i=dialog.open();
		if(i==Window.OK){
			BqTreeParent tenant=wizard.getTenant();
			if(wizard.newAgent()){
				NewAgentWizard newAgentWizard = new NewAgentWizard(tenant);
				WizardDialog newAgentDialog = new WizardDialog(Display.getCurrent().getActiveShell(),
						newAgentWizard);
				newAgentDialog.create();
				newAgentDialog.open();
			}
			else if(wizard.Config()){
				ConfigWizard configWizard = new ConfigWizard(tenant);
				WizardDialog configDialog = new WizardDialog(Display.getCurrent().getActiveShell(),
						configWizard);
				configDialog.create();
				configDialog.open();
			}
		}
	}

}