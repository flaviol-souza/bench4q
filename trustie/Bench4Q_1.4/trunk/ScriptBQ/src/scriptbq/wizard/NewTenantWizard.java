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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.PlatformUI;

import scriptbq.BqConstant;
import scriptbq.tree.BqTreeObject;
import scriptbq.tree.BqTreeParent;
import scriptbq.tree.BqTreeView;
/**
 * This class is the wizard of create new tenant
 *
 */
public class NewTenantWizard extends Wizard{

	private NewTenantWizardPage newTenantWizardPage;
	private ConfigWizardPage configWizardPage;
	private ScriptWizardPage scriptWizardPage;
	private CompleteWizardPage completeWizardPage;
	private BqTreeParent selection;
	
	/**
	 * the file path of configuration file of the tenant
	 */
	private String tempPath;
	/**
	 * DialogSettings used to restore configuration of the tenant
	 */
	private DialogSettings ConfigSetting;
	/**
	 * tenant that is created
	 */
	private BqTreeParent tenant;
	/**
	 * true for creating a new agent for this tenant after the wizard closed
	 */
	private boolean newAgent;
	/**
	 * true for changing configuration after the wizard closed
	 */
	private boolean config;
	
	public NewTenantWizard(BqTreeParent selection){
		this.setWindowTitle("New Tenant");
		this.selection = selection;
		ConfigSetting = new DialogSettings("Config");
	}
	
	public void addPages() {
		newTenantWizardPage = new NewTenantWizardPage();
    	addPage(newTenantWizardPage);
    	configWizardPage= new ConfigWizardPage("");
    	addPage(configWizardPage);
    	scriptWizardPage = new ScriptWizardPage();
    	addPage(scriptWizardPage);
    	completeWizardPage = new CompleteWizardPage();
    	addPage(completeWizardPage);
    }

	/**
	 * Override the parent class method
	 * control the finish button's status
	 */
	public boolean canFinish()
	{ 
		IWizardPage page = getContainer().getCurrentPage();
		if( page instanceof CompleteWizardPage)
			return true;
		else return false;
	}

	@Override
	public boolean performFinish() {
		boolean result = true;
		String TenantName = newTenantWizardPage.getTenantName();
		File TenantFile = new File(BqConstant.BqTreePath+"/"+TenantName);
		if(TenantFile.exists()){
			newTenantWizardPage.setErrorMessage("This Tenant is existed!");
			result = false;		
		}
		else{
			selection.addChild(new BqTreeParent(TenantName));
			BqTreeView viewer = (BqTreeView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().
			                                getActivePage().findView(BqTreeView.TREEVIEW_ID);
			viewer.getTreeViewer().refresh();
			viewer.getTreeViewer().setExpandedState(selection,true);
			TenantFile.mkdir();
			tempPath=BqConstant.BqTreePath+"/"+TenantName+"/config.xml";
			ConfigSetting.put("tenant", TenantName);
			ConfigSetting.put("baseLoad", configWizardPage.getBaseLoad());
			ConfigSetting.put("stdyTime", configWizardPage.getStdyTime());
			ConfigSetting.put("baseURL", configWizardPage.getBaseURL());
			ConfigSetting.put("prepairTime", configWizardPage.getprepairTime());
			ConfigSetting.put("cooldown", configWizardPage.getcooldown());
			ConfigSetting.put("isScript", configWizardPage.getscript());
			ConfigSetting.put("mix", configWizardPage.getmix());
			ConfigSetting.put("thinkTime", configWizardPage.getthinktime());
			ConfigSetting.put("STOperiod", 60000);
			ConfigSetting.put("STOdelay", 30000);
				try {
					ConfigSetting.save(tempPath);
				} catch (IOException e) {
					result=false;
					e.printStackTrace();
				}
		}
		BqTreeObject [] tenantgroup=selection.getChildren();
		for(BqTreeObject tmp:tenantgroup){
			if(tmp.getName().equals(TenantName)){
				tenant=(BqTreeParent)tmp;
				break;
			}
		}
		tenant.setStdyTime(Long.valueOf(configWizardPage.getStdyTime()).longValue());
		if(configWizardPage.getscript()){
			if(scriptWizardPage.getExisted()){
				String existPath=scriptWizardPage.getExistedFile();
				File exist=new File(existPath);
				String tempPath=BqConstant.BqTreePath+"/"+tenant.getName()+"/Script.xml";
				File tmp = new File(tempPath);
				try {
					FileChannel fcin = new FileInputStream(exist).getChannel();
					FileChannel fcout = new FileOutputStream(tmp).getChannel();
					fcin.transferTo(0,fcin.size(),fcout);
					fcin.close();
					fcout.close();
				} catch (FileNotFoundException e) {    
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				File exist=new File(BqConstant.BqTreePath+"/Script.xml");
				String tempPath=BqConstant.BqTreePath+"/"+tenant.getName()+"/Script.xml";
				File tmp = new File(tempPath);
				try {
					FileChannel fcin = new FileInputStream(exist).getChannel();
					FileChannel fcout = new FileOutputStream(tmp).getChannel();
					fcin.transferTo(0,fcin.size(),fcout);
					fcin.close();
					fcout.close();
				} catch (FileNotFoundException e) {    
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				exist.delete();
			}
		}
		newAgent=completeWizardPage.getNewAgent();
		config=completeWizardPage.getConfig();
		return result;
	}
	
	public BqTreeParent getTenant(){
		return tenant;
	}
	
	public boolean newAgent(){
		return newAgent;
	}

	public boolean Config(){
		return config;
	}
}
