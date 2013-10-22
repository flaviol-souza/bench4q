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
package scriptbq.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import scriptbq.Activator;
import scriptbq.resource.ImageRegistry;
import scriptbq.tree.BqTreeParent;
import scriptbq.wizard.ScriptWizard;

/**
 * class for the action to record script
 */
public class ScriptAction extends Action{
	
	private BqTreeParent selection;
	
	/**
	 * constructor
	 * @param selection
	 */
	public ScriptAction(BqTreeParent selection){
		super();
		this.selection = selection;
		setText("Script");		
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		         ImageRegistry.getImagePath(ImageRegistry.SCRIPT)));
	}
	
	public void run(){
		ScriptWizard wizard = new ScriptWizard(selection);
		WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(),
                wizard);
		dialog.create();
		dialog.open();
	}

}
