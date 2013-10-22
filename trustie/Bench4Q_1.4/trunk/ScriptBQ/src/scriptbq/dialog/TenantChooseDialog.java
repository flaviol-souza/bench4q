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

package scriptbq.dialog;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import scriptbq.tree.BqTreeObject;
import scriptbq.tree.BqTreeParent;
import scriptbq.tree.BqTreeView;
import scriptbq.wizard.NewAgentWizard;

/**
 * This class is used to choose tenant when users want to create a new agent by clicking
 * the toolbar. In this case, tenant is not been selected. 
 */
public class TenantChooseDialog extends TitleAreaDialog{

	/**
	 * The comboBox for all the tenants
	 */
	private Combo tenantCombo;
	/**
	 * The data of all the tenants
	 */
	private ArrayList<BqTreeParent> dataList = null;
	/**
	 * Names of all the tenants
	 */
	private String[] tenantNames = null;
	
	/**
	 * constructor
	 * @param parentShell
	 */
	public TenantChooseDialog(Shell parentShell) {
		super(parentShell);
	}
	
	/**
	 * Method used to create contents on composite, this method is neccessary
	 * for the dialog.
	 */
	protected Control createContents(Composite parent){
		super.createContents(parent);
		this.getShell().setText("TenantChooseDialog");
		this.setTitle("Tenant Choose");
		this.setMessage("Please choose the tenant!");
		return parent;	
	}
	
	/**
	 * Method used to get the selected tenant from the root of tenants
	 * @param root  The root of the tenants
	 */
	private void getTenant(BqTreeParent root){
		if(!root.getName().equals("")&&!root.getName().equals("TenantGroup")){
			dataList.add(root);
		}
		BqTreeObject[] children = root.getChildren();
		for(BqTreeObject tmp:children){
			if(tmp instanceof BqTreeParent ){
				getTenant((BqTreeParent)tmp);
			}
		}	
	}
	
	/**
	 * Method to initialize the data
	 */
	private void initData(){
		
		dataList = new ArrayList<BqTreeParent>();
		
		BqTreeView viewer = (BqTreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().
		                                getActivePage().findView(BqTreeView.TREEVIEW_ID);
		BqTreeParent root = (BqTreeParent)viewer.getTreeViewer().getInput();
		getTenant(root);
		tenantNames = new String[dataList.size()];
		for(int i = 0; i < tenantNames.length; i++){
			tenantNames[i] = dataList.get(i).getName();
		}
	}
	
	/**
	 * Method to show the area of dialog, it is necessary for the dialog
	 */
	protected Control createDialogArea(Composite parent){
		super.createDialogArea(parent);
		initData();
		Composite composite = new Composite(parent,SWT.NONE);
		composite.setLayout(new GridLayout(2,false));
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gridData);	
		
		new Label(composite,SWT.NONE).setText("specify the tenant:");
		tenantCombo = new Combo(composite,SWT.DROP_DOWN);
		tenantCombo.setItems(tenantNames);
		tenantCombo.setLayoutData(gridData);
		return parent;		
	}

	/**
	 * Method to get the selected tenant from the comboBox
	 * @return   The selected BqTreeParent
	 */
	public BqTreeParent getSelected(){
		int index = tenantCombo.getSelectionIndex();
		if(dataList.size()==0 || index<0)
			return null;
		return dataList.get(index);
	}

	/**
	 * This method overrides the super class, it is triggered when click the OK button
	 */
	protected void okPressed(){
		BqTreeParent selection = getSelected();
		if(selection!=null){
			NewAgentWizard wizard = new NewAgentWizard(selection);
			WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(),
					                 wizard);
			this.getShell().dispose();
			dialog.create();
			dialog.open();		
		}
		else{
			this.setErrorMessage("Please specify the tenant!");
		}
	}
}
