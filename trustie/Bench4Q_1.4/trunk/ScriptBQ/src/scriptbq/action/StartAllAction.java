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


import java.rmi.RemoteException;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import scriptbq.Activator;
import scriptbq.monitor.BqMonitorView;
import scriptbq.resource.ImageRegistry;
import scriptbq.tree.BqTreeObject;
import scriptbq.tree.BqTreeParent;
import scriptbq.tree.BqTreeView;

/**
 * class for the action to start all
 */
public class StartAllAction extends Action{
	private BqTreeParent invisibleRoot;
	
	/**
	 * constructor
	 */
	public StartAllAction(){
		super();
		setText("StartAll");		
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		         ImageRegistry.getImagePath(ImageRegistry.STARTALL)));
		//BqTreeView viewer = (BqTreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().
        //        getActivePage().findView(BqTreeView.TREEVIEW_ID);
		//invisibleRoot = (BqTreeParent)viewer.getTreeViewer().getInput();
	}
	public void run(){
		BqTreeView viewer = (BqTreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().
                getActivePage().findView(BqTreeView.TREEVIEW_ID);
		invisibleRoot = (BqTreeParent)viewer.getTreeViewer().getInput();
		BqTreeParent tenantgroup=(BqTreeParent)invisibleRoot.getChildren()[0];
		BqTreeObject[] tenant = tenantgroup.getChildren();
		for(BqTreeObject temp:tenant ){
			BqTreeParent Tenant=(BqTreeParent)temp;
			BqTreeObject[] Agents = Tenant.getChildren();
			for(BqTreeObject tmp:Agents){
				try {
					tmp.reset();
					tmp.getRMI().getStarted();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().
			getActivePage().showView(BqMonitorView.MONITORVIEW_ID);
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BqMonitorView monitorView=(BqMonitorView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().
				getActivePage().findView(BqMonitorView.MONITORVIEW_ID);
		monitorView.addAllTabs(tenantgroup);
		/*try {
			rmi = (RMIInterface)Naming.lookup("rmi://133.133.134.34:1099/RMIServer");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			e1.printStackTrace();
		}
		try {
			rmi.getStarted();
		} catch (RemoteException e) {
			e.printStackTrace();
		}*/
//		MessageDialog.openInformation(
//				null,
//				"message",
//				"startall");
	}

}
