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

import org.eclipse.jface.wizard.Wizard;

import scriptbq.BqConstant;
import scriptbq.tree.BqTreeParent;
/**
 * This class is the wizard of recording a script 
 *
 */
public class ScriptWizard extends Wizard{
	
	private ScriptWizardPage scriptWizardPage;
	/**
	 * the tenant which the script belong to
	 */
	private BqTreeParent selection;
	
	public ScriptWizard(BqTreeParent selection){
		this.setWindowTitle("Script");
		this.selection = selection;
	}
	
	public void addPages(){
		scriptWizardPage = new ScriptWizardPage();
		addPage(scriptWizardPage);		
	}

	public boolean performFinish() {
		if(scriptWizardPage.getExisted()){
			String existPath=scriptWizardPage.getExistedFile();
			File exist=new File(existPath);
			String tempPath=BqConstant.BqTreePath+"/"+selection.getName()+"/Script.xml";
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
			String tempPath=BqConstant.BqTreePath+"/"+selection.getName()+"/Script.xml";
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
		return true;
	}

}
