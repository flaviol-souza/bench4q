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

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.IFolderLayout;

import scriptbq.console.BqConsole;

/**
 * This class determines the perspective of Bench4Q_Script, it generates the initial 
 * page layout and visible action set for a page.
 *
 */
public class Perspective implements IPerspectiveFactory {
	/**
	 * The ID of TreeView
	 */
	private String TreeViewID = "scriptbq.Tree.BqTreeView";
	/**
	 * The ID of ConsoleView
	 */
	private String ConsoleViewID = IConsoleConstants.ID_CONSOLE_VIEW;
	/**
	 * The ID of MonitorView
	 */
	private String MonitorViewID = "scriptbq.monitor.BqMonitorView";

	/**
	 * Method to generates the initial page layout and visible action set for a page.
	 */
	public void createInitialLayout(IPageLayout layout) {
		        
		layout.addView(TreeViewID, IPageLayout.LEFT, 0.25f, IPageLayout.ID_EDITOR_AREA);
		layout.addView(ConsoleViewID,IPageLayout.BOTTOM,0.70f,IPageLayout.ID_EDITOR_AREA); 
		IFolderLayout folder=layout.createFolder("message", IPageLayout.LEFT, 0.70f, IPageLayout.ID_EDITOR_AREA);
		folder.addView(MonitorViewID);
		//folder.addPlaceholder(MonitorViewID);
		layout.setEditorAreaVisible(false);
		BqConsole console = new BqConsole();
		console.openConsole();
		BqConsole.showMessage("Welcome to Bench4Q!\n");
		layout.getViewLayout(TreeViewID).setCloseable(false);
	}
}
