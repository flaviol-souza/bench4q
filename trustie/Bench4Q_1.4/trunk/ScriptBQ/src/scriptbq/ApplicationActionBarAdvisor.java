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
package scriptbq;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import scriptbq.action.AboutAction;
import scriptbq.action.NewAgentAction;
import scriptbq.action.NewTenantAction;
import scriptbq.action.SaveAction;
import scriptbq.action.StartAllAction;
import scriptbq.action.SwitchWorkspaceAction;
import scriptbq.resource.ImageRegistry;

/**
 * class used for configuring the action bars of a workbench window
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	
	private IContributionItem showViewList = null;
	private IWorkbenchAction exitAction = null;
	private IWorkbenchAction helpContentsAction;
	
	/**
	 * constructor
	 * @param configurer
	 */
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    /**
     * Method to makeActions
     */
    protected void makeActions(IWorkbenchWindow window) {
    	showViewList = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
    	exitAction = ActionFactory.QUIT.create(window);
    	exitAction.setHoverImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
		         ImageRegistry.getImagePath(ImageRegistry.EXIT)));
    	helpContentsAction = ActionFactory.HELP_CONTENTS.create(window); register(helpContentsAction);
    }

    /**
     * Method to fill the menu bar
     */
    protected void fillMenuBar(IMenuManager menuBar) {
    	
    	MenuManager fileMenu = new MenuManager("File");
    	MenuManager editMenu = new MenuManager("Edit");
    	MenuManager windowMenu = new MenuManager("Window", IWorkbenchActionConstants.M_WINDOW);
    	MenuManager helpMenu = new MenuManager("&Help",IWorkbenchActionConstants.M_HELP);
    	menuBar.add(fileMenu);
    	menuBar.add(editMenu);
    	menuBar.add(windowMenu);
    	menuBar.add(helpMenu);		
    	MenuManager newMenu = new MenuManager("New");
    	fileMenu.add(newMenu);
    	newMenu.add(new NewTenantAction());
    	newMenu.add(new NewAgentAction());
    	fileMenu.add(new SaveAction());
    	MenuManager switchMenu = new MenuManager("Switch Workspace");
    	fileMenu.add(switchMenu);
    	switchMenu.add(new SwitchWorkspaceAction());
    	fileMenu.add(exitAction);
    	editMenu.add(new StartAllAction());
        MenuManager showViewMenu = new MenuManager("Views", IWorkbenchActionConstants.SHOW_EXT);
        windowMenu.add(showViewMenu);      
        showViewMenu.add(showViewList);
        helpMenu.add(helpContentsAction);
        helpMenu.add(new AboutAction());
    	
    }
    
    /**
     * Method to fill the tool bar
     */
    protected void fillCoolBar(ICoolBarManager cbManager){
    	
    	IToolBarManager toolBar=new ToolBarManager(SWT.FLAT|SWT.SHADOW_OUT);
    	cbManager.add(toolBar);    
        toolBar.add(new NewTenantAction());
        toolBar.add(new NewAgentAction());
        toolBar.add(new Separator());
        toolBar.add(new StartAllAction());
        toolBar.add(new Separator());

    }
    
}
