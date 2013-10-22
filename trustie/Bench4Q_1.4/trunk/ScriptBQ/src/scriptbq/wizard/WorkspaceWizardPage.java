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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;

import scriptbq.BqConstant;

/**
 * This class is the wizard page of WorkspaceWizard
 *
 */
public class WorkspaceWizardPage extends WizardPage {

	/**
	 * combo box of workspace
	 */
	private Combo workspace;
	/**
	 * button of opening directory dialog
	 */
	private Button selectWorkspace;
	/**
	 * the workspace path
	 */
	private String workspacePath;
	/**
	 * checkbox button  use this workspace as default and do not ask again
	 */
	private Button defaultselection;
	/**
	 * Constructor
	 * @param pageName
	 */
	protected WorkspaceWizardPage(String pageName) {
		super(pageName);
		setTitle("Select a workspace");
		setDescription("Bench4Q stores your projects in a folder called a workspace.\nChoose a workspace folder to use for this session.");		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createControl(Composite parent) {
		// TODO Auto-generated method stub
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(3,false));
		new Label(composite, SWT.NONE).setText("Workspace:");
		workspace=new Combo(composite,SWT.DROP_DOWN);
		workspace.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//workspace.setText(BqConstant.BqRootPath);
		workspace.add(BqConstant.BqRootPath);
		selectWorkspace=new Button(composite,SWT.NULL);
		selectWorkspace.setText("Browse...");
		selectWorkspace.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dlg = new DirectoryDialog(Display.getCurrent().getActiveShell());
				   dlg.setFilterPath(workspace.getText());
				   dlg.setText("SWT's DirectoryDialog");
				   dlg.setMessage("Select a directory");
				   workspacePath = dlg.open();
				   if (workspacePath != null) {
					   workspace.add(workspacePath);
					   workspace.setText(workspacePath);
				   }
			}
		});
		defaultselection=new Button(composite,SWT.CHECK|SWT.LEFT);
		defaultselection.setText("use this as the default and do not ask again.");
		GridData data = new GridData();
		data.horizontalAlignment = GridData.BEGINNING;
		data.verticalAlignment=GridData.END;
		data.grabExcessHorizontalSpace = true;
		data.horizontalSpan = 2;
		data.verticalIndent=45;
		defaultselection.setLayoutData(data);

		setControl(composite);
	}
	public String getWorkspace(){
		return workspace.getText();
	}
	public boolean getDefault(){
		return defaultselection.getSelection();
	}

}
