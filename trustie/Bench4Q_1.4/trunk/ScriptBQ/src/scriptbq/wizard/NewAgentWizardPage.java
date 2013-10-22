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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
/**
 * This class is the wizard page of new agent wizard
 *
 */
public class NewAgentWizardPage extends WizardPage{
	
	/**
	 * text control of host name
	 */
	private Text hostname;
	/**
	 * text control of user name
	 */
	private Text username;
	/**
	 * text control of password
	 */
	private Text password;
	/**
	 * Constructor
	 */
	protected NewAgentWizardPage() {
		super("NewAgent");
		setTitle("Create a new agent");
		setDescription("This wizard allows the user to create a new agent for test.");
	}

	/**
	 * create control
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(2,false));
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		composite.setLayoutData(gridData);		
		
		new Label(composite, SWT.NONE).setText("hostname:");
		hostname = new Text(composite, SWT.BORDER);
		hostname.setLayoutData(gridData);
		//hostname.setText("133.133.241.244");
		//hostname.setText("133.133.134.34");
		
		new Label(composite, SWT.NONE).setText("username:");
		username = new Text(composite, SWT.BORDER);
		username.setLayoutData(gridData);
		//username.setText("root");
		//username.setText("bjyjtdj");
		
		new Label(composite, SWT.NONE).setText("password:");
		password = new Text(composite, SWT.BORDER);
		password.setEchoChar('*');
		password.setLayoutData(gridData);
		//password.setText("onceas");
		//password.setText("znjhmyy");
				
		setControl(composite);
	}
	public String getHostName(){
		return hostname.getText();
	}
	public String getUserName(){
		return username.getText();
	}
	public String getPassWord(){
		return password.getText();
	}
	

}
