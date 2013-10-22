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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.wizard.Wizard;

import scriptbq.Activator;
import scriptbq.BqConstant;
/**
 * This class is the wizard of switching workspace
 *
 */
public class SwitchWorkspaceWizard extends Wizard {

	/**
	 * wizard page of switch workspace
	 */
	private SwitchWorkspaceWizardPage switchWizardPage;
	/**
	 * file path of the metadata file which stores workspace information
	 */
	private String tempPath;
	/**
	 * DialogSettigns to restore workspace information
	 */
	private DialogSettings ConfigSetting;
	
	
	public SwitchWorkspaceWizard(){
		this.setWindowTitle("Workspace Launcher");
		ConfigSetting = new DialogSettings("Workspace");
	}
	@Override
	public void addPages(){
		switchWizardPage = new SwitchWorkspaceWizardPage("Workspace");
		addPage(switchWizardPage);		
	}
	public boolean performFinish() {
		// TODO Auto-generated method stub
		boolean result=true;
		String oldPath=BqConstant.BqRootPath;
		BqConstant.BqRootPath=switchWizardPage.getWorkspace()+"ScriptBq";
		String rootpath="";
		try {
			rootpath = FileLocator.toFileURL(
					Platform.getBundle(Activator.PLUGIN_ID )
							.getEntry("")).getPath().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		tempPath=rootpath+"metadata.xml";
		File metaData=new File(tempPath);
		if(metaData.exists()){
			result=metaData.delete();
		}
		if(switchWizardPage.getDefault()){
			ConfigSetting.put("workspace",BqConstant.BqRootPath);
				try {
					ConfigSetting.save(tempPath);
				} catch (IOException e) {
					e.printStackTrace();
					result=false;
				}
		}
		if(switchWizardPage.getCopy()){
			result=copyFile(oldPath,BqConstant.BqRootPath);
		}
		return result;
	}
	/**
	 * copy file
	 * @param filename1 the source file
	 * @param filename2 the destination file 
	 * @return
	 */
	public boolean copyFile(String filename1,String filename2){
		File in=new File(filename1);
		File out=new File(filename2);
		if(!in.exists()){
			return false;
		}
		if(!out.exists()) 
			out.mkdirs();
		File[] file=in.listFiles();
		FileInputStream fin=null;
		FileOutputStream fout=null;
		for(int i=0;i<file.length;i++){
		if(file[i].isFile()){
			try {
				fin=new FileInputStream(file[i]);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("in.name="+file[i].getName());
			try {
				fout=new FileOutputStream(new File(filename2+"/"+file[i].getName()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int c;
			byte[] b=new byte[1024*5];
			try {
				while((c=fin.read(b))!=-1){
					fout.write(b, 0, c);
				}
				fin.close();
				fout.flush();
				fout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else copyFile(filename1+"/"+file[i].getName(),filename2+"/"+file[i].getName());
		}
		return true;

	}

}
