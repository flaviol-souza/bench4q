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

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;


/**
 * This class is the wizard page of ConfigWizard.
 */
public class ConfigWizardPage extends WizardPage{
	
	/**
	 * The DialogSettings used to restore configuration information.
	 */
	private DialogSettings ConfigSetting;
	/**
	 * The file path of configuration file.
	 */
	private String tempPath;
	/**
	 * Text control of base load.
	 */
	private Text baseLoad;
	/**
	 * Text control of testing time
	 */
	private Text stdyTime;
	/**
	 * Text control of base URL of book store application
	 */
	private Text baseURL;
	/**
	 * Text control of prepare time
	 */
	private Text prepairTime;
	/**
	 * Text control of cool down time
	 */
	private Text cooldown;
	/**
	 * Text control of think time
	 */
	private Text thinkTime;
	/**
	 * default value of base load
	 */
	private String default_baseLoad = "20";
	/**
	 * default value of testing time
	 */
	private String default_stdyTime = "120";
	/**
	 * default value of base URL
	 */
	private String default_baseURL = "http://133.133.133.95:8080/bench4Q";
	/**
	 * default value of prepare time 
	 */
	private String default_prepairTime = "0";
	/**
	 * default value of cool down time
	 */
	private String default_cooldown = "0";
	/**
	 * default value of think time
	 */
	private String default_thinktime="0.01";
	/**
	 * radio button. true for book store type
	 */
	private Button bookstore;
	/**
	 * radio button. true for script type
	 */
	private Button script;
	/**
	 * combo box for mix type of bookstore
	 */
	private Combo mix;
	
	/**
	 * Constructor
	 * @param Path the file path of configuration file 
	 */
	protected ConfigWizardPage(String Path) {
		super("wizardPage");
		setTitle("Config the Tenant");
		setDescription("This wizard allows the user to make configuration of the Tenant.");
		tempPath = Path;
	}
	/**
	 * createControl
	 * @param parent parent composite
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		composite.setLayout(new GridLayout(1,false));
		//GridData gridData = new GridData();
		//gridData.horizontalAlignment = GridData.FILL;
		//gridData.verticalAlignment = GridData.FILL;
		//gridData.grabExcessHorizontalSpace = true;
		//gridData.grabExcessVerticalSpace = true;
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Group group2=new Group(composite, SWT.NONE);
		group2.setText("Please choose tenant's form:");
		group2.setLayout(new GridLayout(2,true));
		group2.setLayoutData(new GridData(GridData.FILL_BOTH));
		bookstore = new Button(group2, SWT.RADIO);
		bookstore.setText("bookstore's mix");
		//bookstore.setSelection(true);
		bookstore.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				thinkTime.setEnabled(true);
				baseURL.setEnabled(true);
			}
		});
		mix=new Combo(group2,SWT.DROP_DOWN);
		mix.setLayoutData(new GridData(GridData.FILL_BOTH));
		mix.add("shopping", 0);
		mix.add("browsing",1);
		mix.add("ordering",2);
		mix.setText("shopping");
		script = new Button(group2, SWT.RADIO);
		script.setText("use script");
		script.setLayoutData(new GridData(GridData.FILL_BOTH));
		script.setSelection(true);
		script.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				thinkTime.setEnabled(false);
				baseURL.setEnabled(false);
			}
		});
		Group group1=new Group(composite, SWT.NONE);
		group1.setLayout(new GridLayout(2,true));
		//group1.set
		group1.setLayoutData(new GridData(GridData.FILL_BOTH));
		new Label(group1, SWT.NONE).setText("BaseLoad:");
		baseLoad = new Text(group1, SWT.BORDER);
		baseLoad.setLayoutData(new GridData(GridData.FILL_BOTH));
		baseLoad.setText(default_baseLoad);
		
		new Label(group1, SWT.NONE).setText("StdyTime:");
		stdyTime = new Text(group1, SWT.BORDER);
		stdyTime.setLayoutData(new GridData(GridData.FILL_BOTH));
		stdyTime.setText(default_stdyTime);
		
		new Label(group1, SWT.NONE).setText("PrepairTime:");
		prepairTime = new Text(group1, SWT.BORDER);
		prepairTime.setLayoutData(new GridData(GridData.FILL_BOTH));
		prepairTime.setText(default_prepairTime);
		
		new Label(group1, SWT.NONE).setText("cooldown:");
		cooldown = new Text(group1, SWT.BORDER);
		cooldown.setLayoutData(new GridData(GridData.FILL_BOTH));
		cooldown.setText(default_cooldown);

		new Label(group1, SWT.NONE).setText("Thinktime:");
		thinkTime = new Text(group1, SWT.BORDER);
		thinkTime.setLayoutData(new GridData(GridData.FILL_BOTH));
		thinkTime.setText(default_thinktime);
		thinkTime.setEnabled(false);
		
		new Label(group1, SWT.NONE).setText("BaseURL:");
		baseURL = new Text(group1, SWT.BORDER);
		baseURL.setLayoutData(new GridData(GridData.FILL_BOTH));
		baseURL.setText(default_baseURL);
		baseURL.setEnabled(false);
		
		File tmp = new File(tempPath);
		if(tmp.exists()){
			ConfigSetting = new DialogSettings(null);
			try {
				ConfigSetting.load(tempPath);
				baseLoad.setText(ConfigSetting.get("baseLoad"));
				stdyTime.setText(ConfigSetting.get("stdyTime"));
				baseURL.setText(ConfigSetting.get("baseURL"));
				prepairTime.setText(ConfigSetting.get("prepairTime"));
				cooldown.setText(ConfigSetting.get("cooldown"));
				script.setSelection(ConfigSetting.get("isScript").equals("true"));
				bookstore.setSelection(!script.getSelection());
				mix.setText(ConfigSetting.get("mix"));
				thinkTime.setText(ConfigSetting.get("thinkTime"));
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}			
		setControl(composite);
	}
	/**
	 * getNextPage according to the choose of users
	 * @return the next page of this page in the NewTenant wizard
	 */
	public IWizardPage getNextPage(){
		if(getscript())
			return super.getNextPage();
		else
			return getWizard().getPage("Complete");
	}
	/**
	 * get base load value in text control
	 * @return base load value
	 */
	public String getBaseLoad(){
		return baseLoad.getText();
	}
	/**
	 * set base load value in text control
	 * @param s base load value
	 */
	public void setBaseLoad(String s){
		baseLoad.setText(s);
	}
	/**
	 * get testing time value in text control
	 * @return testing time value
	 */
	public String getStdyTime(){
		return stdyTime.getText();
	}
	/**
	 * set testing time value in text control
	 * @param s testing time value
	 */
	public void setStdyTime(String s){
		stdyTime.setText(s);
	}
	/**
	 * get base URL value in text control
	 * @return base URL value
	 */
	public String getBaseURL(){
		return baseURL.getText();
	}
	/**
	 * set base URL value in text control
	 * @param s base URL value
	 */
	public void setBaseURL(String s){
		baseURL.setText(s);
	}
	/**
	 * get prepare time value in text control
	 * @return prepare time value
	 */
	public String getprepairTime(){
		return prepairTime.getText();
	}
	/**
	 * set prepare time value in text control
	 * @param s prepare time value
	 */
	public void setprepairTime(String s){
		prepairTime.setText(s);
	}
	/**
	 * get cool down value in text control
	 * @return cool down value
	 */
	public String getcooldown(){
		return cooldown.getText();
	}
	/**
	 * set cool down value in text control
	 * @param s cool down value
	 */
	public void setcooldown(String s){
		cooldown.setText(s);
	}
	/**
	 * get selection of bookstore button
	 * @return selection
	 */
	public boolean getbookstore()
	{
		return bookstore.getSelection();
	}
	/**
	 * set selection of bookstore button
	 * @param s selection
	 */
	public void setbookstore(String s)
	{
		bookstore.setSelection(s.equals("true"));
	}
	/**
	 * get selection of script button
	 * @return selection
	 */
	public boolean getscript()
	{
		return script.getSelection();
	}
	/**
	 * set selection of script button
	 * @param s selection
	 */
	public void setscript(String s)
	{
		script.setSelection(s.equals("true"));
	}
	/**
	 * get mix value in text control
	 * @return mix value
	 */
	public String getmix()
	{
		return mix.getText();
	}
	/**
	 * set mix value in text control
	 * @param s mix value
	 */
	public void setmix(String m)
	{
		mix.setText(m);
	}
	/**
	 * get think time value in text control
	 * @return think time  value
	 */
	public String getthinktime(){
		return thinkTime.getText();
	}
}
