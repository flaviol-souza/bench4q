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
package scriptbq.monitor;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import scriptbq.action.CollectResultAction;
import scriptbq.tree.BqTreeObject;
import scriptbq.tree.BqTreeParent;

/**
 * This class is also depends on the extension point of Eclipse. This view provides an implemention
 * of monitoring the real time testing, it gets the real time result from the agent and displays on
 * the composite to show the performance.
 */
public class BqMonitorView extends ViewPart{

	/**
	 * The MonitorView's ID
	 */
	public static String MONITORVIEW_ID = "scriptbq.monitor.BqMonitorView";
	/**
	 * TenantGroupfolder for all of the tenants
	 */
    private CTabFolder TenantGroupfolder; 
    /**
     * tenantMap is used to store the data for the tenant.
     */
    Map<String, Integer> tenantMap;
    
	/**
	 * Method to create control, it is necessary to show the monitor view.
	 */
	public void createPartControl(Composite parent) {
		tenantMap=new HashMap<String, Integer>();
        TenantGroupfolder = new CTabFolder(parent, SWT.NONE);
        TenantGroupfolder.setLayout(new GridLayout());
        TenantGroupfolder.setSimple(false);
        TenantGroupfolder.setSelectionForeground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        TenantGroupfolder.setSelectionBackground(new Color(parent.getDisplay(),20,130,200));
        //addTabs((BqTreeParent)invisibleRoot.getChildren()[0]);
	}

	/**
	 * Method to add all the tabs corresponding to the tenants for monitoring
	 * @param TenantGroup
	 */
	public void addAllTabs(BqTreeParent TenantGroup){
		BqTreeObject[] TenantList = TenantGroup.getChildren();
		long stdyTime=0;
		for(BqTreeObject temp: TenantList){
			BqTreeParent Tenant=(BqTreeParent)temp;
			if(Tenant.getStdyTime()>stdyTime){
				stdyTime=Tenant.getStdyTime();
			}
			CTabItem tabItemExecution = new CTabItem(TenantGroupfolder, SWT.NONE);
			tabItemExecution.setText(getTenantTab(Tenant.getName()));
		    TenantGroupItem tgi = new TenantGroupItem(TenantGroupfolder,SWT.NONE);
		    BqTreeObject[] AgentList = Tenant.getChildren();
			for(BqTreeObject tmp:AgentList){
				tgi.fillCTabItem(tmp);
			}
		    tabItemExecution.setControl(tgi);
		}  
	    TenantGroupfolder.setSelection(TenantGroupfolder.getItems().length - 1);
	    Timer finishTimer = new Timer("finishTimer");
	    finishTimer.schedule(new FinishTask(TenantList), (stdyTime+2)*1000);
	}
	
	/**
	 * Method to add a monitoring tab for the tenant
	 * @param Tenant
	 */
	public void addTabs(BqTreeParent Tenant){
		BqTreeObject[] AgentList = Tenant.getChildren();
		CTabItem tabItemExecution = new CTabItem(TenantGroupfolder, SWT.NONE);
		tabItemExecution.setText(getTenantTab(Tenant.getName()));
		TenantGroupItem tgi = new TenantGroupItem(TenantGroupfolder,SWT.NONE);
		//System.out.println(AgentList.length);
		for(BqTreeObject tmp: AgentList){
			//System.out.println(tmp.getName());
		    tgi.fillCTabItem(tmp);
		} 
		tabItemExecution.setControl(tgi);
	    TenantGroupfolder.setSelection(TenantGroupfolder.getItems().length - 1);
	    Timer finishTimer = new Timer("finishTimer");
	    finishTimer.schedule(new FinishTask(Tenant), (Tenant.getStdyTime()+2)*1000);
	}
	
	/**
	 * Method to set the focus of the view
	 */
	public void setFocus() {	
	}
	
	/**
	 * Method to get the tab corresponding to the specific tenant
	 * @param tenant
	 * @return
	 */
	public String getTenantTab(String tenant){
		if(tenantMap.containsKey(tenant)){
			int i=tenantMap.get(tenant);
			i++;
			tenantMap.put(tenant, i);
			return tenant+"("+i+")";
		}
		else{
			tenantMap.put(tenant, 1);
			return tenant;
		}
	}
}

/**
 * class used to show the finish of the task
 */
class FinishTask extends TimerTask {
	private int type;
	private static int tenant=0;
	private static int tenantgroup=1;
	private BqTreeParent Tenant;
	private BqTreeObject[] TenantList;
	public FinishTask(BqTreeParent Tenant) {
		type=tenant;
		this.Tenant=Tenant;
	}
	public FinishTask(BqTreeObject[] Tenant) {
		type=tenantgroup;
		this.TenantList=Tenant;
	}
	public void run() {
		Display display=Display.getDefault();
		if(display==null){
			System.out.println("null");
		}
		else{
			display.asyncExec(new Runnable(){
			public void run(){
				String Info = "Finished!Do you want to collect details of results form agent?";
				//System.out.println(Info);
				boolean result=MessageDialog.openQuestion(
							null,
							null,
							Info);
				if(result){
					CollectResultAction collect;
					if(type==tenant){
						collect=new CollectResultAction(Tenant);
					}else{
						collect=new CollectResultAction(TenantList);
					}
					collect.run();
				}
			}
		});}
	}
}
