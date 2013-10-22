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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import scriptbq.ssh.BqSsh;
import scriptbq.tree.BqTreeObject;
import scriptbq.tree.BqTreeParent;
import scriptbq.tree.BqTreeView;
/**
 * This class is the wizard to create a new agent
 *
 */
public class NewAgentWizard extends Wizard {

	/**
	 * The wizard page of this wizard
	 */
	private NewAgentWizardPage mainpage;
	/**
	 * The tenant which the new agent belong to
	 */
	private BqTreeParent selection;
	/**
	 * Constructor
	 * @param selection The tenant which the new agent belong to
	 */
	public NewAgentWizard(BqTreeParent selection){
		String title = "New Agent for Tenant: "+selection.getName();
		this.setWindowTitle(title);
		this.selection = selection;
	}
	/**
	 * addPages
	 */
	public void addPages() {
    	mainpage = new NewAgentWizardPage();
    	addPage(mainpage);
    }
	/**
	 * add the new agent into the BqTree
	 * @param agent the agent that will be added
	 */
	public void TreeModify(BqTreeObject agent){
		
		selection.addChild(agent);
		BqTreeView viewer = (BqTreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView(BqTreeView.TREEVIEW_ID);	
		viewer.getTreeViewer().refresh();
		viewer.getTreeViewer().setExpandedState(selection,true);			
	}
	/**
	 * perform after the wizard finished
	 */
	public boolean performFinish() {
			
		String hostName = mainpage.getHostName();
		String userName = mainpage.getUserName();
		String passWord = mainpage.getPassWord();
		String AgentName = "Agent"+"("+mainpage.getHostName().trim()+")";
		BqTreeObject Agent = new BqTreeObject(AgentName);
		IRunnableWithProgress st = new SshThread(hostName,userName,passWord,Agent,this);
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
		try {
			progressDialog.run(true, true, st);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Internal class use SSH to connect agent client
	 *
	 */
	class SshThread implements IRunnableWithProgress{

		private String hostname,username,password;
		/**
		 * the agent of this SSH connection
		 */
		private BqTreeObject selection;
		private NewAgentWizard wizard;
		/**
		 * Constructor
		 * @param hostName name of host
		 * @param userName name of user
		 * @param passWord password of user
		 * @param selection the agent of this SSH connection
		 * @param wizard the wizard of new agent
		 */
		public SshThread(String hostName,String userName,String passWord,
				         BqTreeObject selection,NewAgentWizard wizard){
			hostname = hostName;
			username = userName;
			password = passWord;
			this.selection = selection;
			this.wizard = wizard;
		}		
		public void run() {
			
		}

		/**
		 * run
		 */
		public void run(IProgressMonitor monitor)
				throws InvocationTargetException, InterruptedException {
			final BqSsh agentSsh = new BqSsh(hostname,username,password);		
			monitor.beginTask("Connecting......", IProgressMonitor.UNKNOWN);
			final boolean isCon = agentSsh.Connect();
			monitor.done();
			if(monitor.isCanceled()){
				throw new InterruptedException("agent canceled!");
			}	
			
			Display.getDefault().asyncExec(new Runnable() { 
	            public void run() { 
	            	if(isCon){
	            		selection.setSsh(agentSsh);
	        			selection.SetInfo(hostname, username, password);
	        			wizard.TreeModify(selection);
	        			wizard.getShell().dispose();
	            	}
	            	else{
	            		wizard.mainpage.setErrorMessage("Failed to Connect the Agent!");
	            	}
	            } 
			});
			
		}
	}

}
