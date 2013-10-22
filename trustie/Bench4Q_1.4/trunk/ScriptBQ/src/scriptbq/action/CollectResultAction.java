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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

import scriptbq.BqConstant;
import scriptbq.tree.BqTreeObject;
import scriptbq.tree.BqTreeParent;

/**
 * class used for collect result action
 */
public class CollectResultAction extends Action {
	int tenantnum;
	BqTreeParent [] tenantList;
	/**
	 * constructor  for multiple tenants
	 * @param tenantList
	 */
	public CollectResultAction(BqTreeObject [] tenantList){
		tenantnum=tenantList.length;
		this.tenantList=new BqTreeParent[tenantList.length];
		int num=0;
		for(BqTreeObject tmp:tenantList){
			this.tenantList[num]=(BqTreeParent)tmp;
			num++;
		}
	}
	
	/**
	 * constructor
	 * @param tenant
	 */
	public CollectResultAction(BqTreeParent tenant){
		tenantnum=1;
		this.tenantList=new BqTreeParent[1];
		this.tenantList[0]=tenant;
	}
	public void run(){
		try{
			ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
			IRunnableWithProgress runnable = new IRunnableWithProgress(){
				
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Collecting......", tenantnum);
					for(BqTreeObject temp:tenantList){
						BqTreeParent tenant=(BqTreeParent)temp;
						for(BqTreeObject tmp:tenant.getChildren()){
							monitor.subTask("Collecting Agent Result:"+tmp.getName());
							try {
								tmp.getSsh().Command("pkill java");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							tmp.getSsh().GetFile("~/throughput_"+tmp.getIpAddress()+".csv", BqConstant.BqTreePath+"/"+tenant.getName());
							tmp.getSsh().GetFile("~/responseTime_"+tmp.getIpAddress()+".csv", BqConstant.BqTreePath+"/"+tenant.getName());
							//tmp.getSsh().GetFile("~/responseTimeSmallSize_"+tmp.getIpAddress()+".csv", BqConstant.BqTreePath+"/"+tenant.getName());
							try {
								tmp.getSsh().Command("rm throughput_"+tmp.getIpAddress()+".csv");
								tmp.getSsh().Command("rm responseTime_"+tmp.getIpAddress()+".csv");
								tmp.getSsh().Command("rm responseTimeSmallSize_"+tmp.getIpAddress()+".csv");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							monitor.worked(10);
						}				
					}
					monitor.done();
					if(monitor.isCanceled()){
						throw new InterruptedException("Collect canceled!");
					}				
				}			
			};
			progressDialog.run(true, true, runnable);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
}
