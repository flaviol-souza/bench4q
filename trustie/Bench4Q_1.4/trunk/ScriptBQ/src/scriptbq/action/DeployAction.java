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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import scriptbq.Activator;
import scriptbq.BqConstant;
import scriptbq.resource.ImageRegistry;
import scriptbq.tree.BqTreeObject;
import scriptbq.tree.BqTreeParent;

/**
 * class for deployment
 *
 */
public class DeployAction extends Action{
	private BqTreeParent selection;
	/**
	 * whether to record script
	 */
	private boolean isScript;
	
	/**
	 * constructor
	 * @param selection
	 */
	public DeployAction(BqTreeParent selection){
		super();
		this.selection = selection;
		setText("Deploy");		
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		         ImageRegistry.getImagePath(ImageRegistry.DEPLOY)));
	}
	public void run(){
	//	MessageDialog.openInformation(null,"AgentInfo","deploy");
		String tempPath=BqConstant.BqTreePath+"/"+selection.getName()+"/config.xml";
		File tmp = new File(tempPath);
		if(tmp.exists()){
			DialogSettings ConfigSetting = new DialogSettings(null);
			try {
				ConfigSetting.load(tempPath);
				isScript=ConfigSetting.get("isScript").equals("true");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try{
			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
			IRunnableWithProgress runnable = new IRunnableWithProgress(){

				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					BqTreeObject[] Agents = selection.getChildren();
					monitor.beginTask("Deploying......", Agents.length);
					for(BqTreeObject tmp:Agents){
						monitor.subTask("Deploying Agent:"+tmp.getName());
						try {
							tmp.getSsh().Command("pkill java");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						String rootpath="";
						try {
							rootpath = FileLocator.toFileURL(
									Platform.getBundle(Activator.PLUGIN_ID )
											.getEntry("")).getPath().toString();
							//System.out.println(rootpath);
						} catch (IOException e) {
							e.printStackTrace();
						}
						tmp.getSsh().ScpFile(rootpath+"/agent/agent.jar", "~/");
						tmp.getSsh().ScpFile(BqConstant.BqTreePath+"/"+selection.getName()+"/config.xml", "~/");
						if(isScript){
							tmp.getSsh().ScpFile(BqConstant.BqTreePath+"/"+selection.getName()+"/Script.xml", "~/");
						}
						//tmp.getSsh().ScpFile("D:/agent.jar","~/");
						//tmp.getSsh().ScpFile("D:/config.xml","~/");
						//tmp.getSsh().ScpFile("D:/Script.xml","~/");
						
						/*try {
							tmp.getSsh().Command(". /etc/profile;java -jar agent.jar");
							//tmp.getSsh().Command("java -D<java.rmi.server.hostname>=<"+tmp.getIpAddress()+">");
							//System.out.println("java -D\"java.rmi.server.hostname\"=\""+tmp.getIpAddress()+"\"");
						} catch (IOException e) {
							e.printStackTrace();
						}*/
						/*String rmiSite = BqConstant.RMIPrefix+tmp.getIpAddress()+BqConstant.RMISuffix;
						//String rmiSite = BqConstant.RMIPrefix+"ie"+BqConstant.RMISuffix;
						RMIInterface rmi = null;
						try {
							rmi = (RMIInterface)Naming.lookup(rmiSite);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (NotBoundException e) {
							e.printStackTrace();
						}
						if(rmi!=null){
							tmp.setRMI(rmi);
						}*/
						monitor.worked(10);
					}				
					monitor.done();
					if(monitor.isCanceled()){
						throw new InterruptedException("Deploy canceled!");
					}				
				}			
			};
			progressDialog.run(true, true, runnable);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}


}
