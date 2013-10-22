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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * This class is the last wizard page for the wizard of NewTenant.
 * 
 */
public class CompleteWizardPage extends WizardPage {

	/**
	 * Radio button.Select it means creating a new agent for tenant
	 *  after the NewTenant wizard closed.
	 */
	private Button newAgent;
	/**
	 * Radio button.Select it means changing configuration of tenant
	 * after the NewTenant wizard closed.
	 */
	private Button Config;
	/**
	 * constructor
	 */
	protected CompleteWizardPage(){
		super("Complete");
		setTitle("Finish creating the tenant");
		setDescription("You have finished creating a new tenant.Now you can:");
	}
	@Override
	/**
	 * createControl
	 * @param parent parent composite
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(1,false));
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gridData);
		new Label(composite, SWT.NONE).setText("Now you can:");
		newAgent = new Button(composite, SWT.RADIO);
		newAgent.setText("Create a new agent for this tenant.");
		newAgent.setSelection(true);
		GridData data1 = new GridData();
		data1.horizontalAlignment = GridData.BEGINNING;
		data1.verticalAlignment=GridData.END;
		data1.grabExcessHorizontalSpace = true;
		data1.horizontalIndent = 20;
		data1.verticalIndent=30;
		newAgent.setLayoutData(data1);
		Config = new Button(composite, SWT.RADIO);
		Config.setText("change the configuration of this tanant.");
		GridData data2 = new GridData();
		data2.horizontalAlignment = GridData.BEGINNING;
		data2.verticalAlignment=GridData.END;
		data2.grabExcessHorizontalSpace = true;
		data2.horizontalIndent = 20;
		data2.verticalIndent=10;
		Config.setLayoutData(data2);
		Button DoNone = new Button(composite, SWT.RADIO);
		DoNone.setText("I'd like to create agents by myself later.");
		DoNone.setLayoutData(data2);
		setControl(composite);
	}
	/**
	 * get NewAgent button's state
	 * @return true indicate button agent was selected.
	 */
	public boolean getNewAgent(){
		return newAgent.getSelection();
	}
	/**
	 * get Configuration button's state
	 * @return true indicate button config was selected.
	 */
	public boolean getConfig(){
		return Config.getSelection();
	}
}
