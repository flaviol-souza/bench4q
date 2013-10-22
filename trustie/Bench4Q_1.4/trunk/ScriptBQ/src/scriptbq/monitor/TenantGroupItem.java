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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import scriptbq.tree.BqTreeObject;

/**
 * This class stands for the Item of the TenantGroupTab folder, each item is corresponding 
 * to a tenant.
 */
public class TenantGroupItem extends Composite{
	
	/**
	 * Tenantfolder is the collection of items represent the agents under the tenant
	 */
	private CTabFolder Tenantfolder;
	//private BqTreeObject agent;

	/**
	 * constructor
	 */
	public TenantGroupItem(Composite parent, int style) {
		super(parent, style);
		FillLayout layout = new FillLayout();
        this.setLayout(layout);
		this.Tenantfolder = new CTabFolder(this,SWT.BORDER);
		Tenantfolder.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	/**
	 * Method to fill the Tenantfolder
	 * @param agent  The agent needs monitoring
	 */
	public void fillCTabItem(BqTreeObject agent){
		CTabItem tabItemExecution = new CTabItem(Tenantfolder, SWT.NONE);
		tabItemExecution.setText(agent.getName());
		BqGraph bg = new BqGraph(Tenantfolder,SWT.NONE,agent);
		tabItemExecution.setControl(bg);
	    Tenantfolder.setSelection(Tenantfolder.getItems().length - 1);
	}

}
