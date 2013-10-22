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

package scriptbq.tree;

import java.io.File;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import scriptbq.BqConstant;
import scriptbq.action.AgentInfoAction;
import scriptbq.action.CommandLineAction;
import scriptbq.action.ConfigAction;
import scriptbq.action.DeployAction;
import scriptbq.action.NewAgentAction;
import scriptbq.action.NewTenantAction;
import scriptbq.action.ScriptAction;
import scriptbq.action.StartAllAction;
import scriptbq.action.StartTenantAction;

/**
 * The class BqTreeView is the extension of Eclipse's view. In the Bench4Q_Script,
 * it shows on the left of the frame. This class provides the operation of the
 * TreeViewer to perform the agents and tenant.
 */
public class BqTreeView extends ViewPart{
	
	/**
	 * The ID of the BqTreeView
	 */
	public static String TREEVIEW_ID = "scriptbq.Tree.BqTreeView";
	/**
	 * The TreeViewer to show the tenant and agent
	 */
	private TreeViewer viewer;
	/**
	 * The root of the TreeViewer, it is invisible
	 */
	private BqTreeParent invisibleRoot;
	/**
	 * Implements a simple web style navigation metaphor for a TreeViewer. 
	 * Home, back, and "drill into" functions are supported for the viewer, 
	 */
	private DrillDownAdapter drillDownAdapter;
	
	/**
	 * The class provides a sorter for the nodes on the TreeView
	 *
	 */
	class NameSorter extends ViewerSorter {
	}

	/**
	 * constructor
	 */
	public BqTreeView() {
		initialize();
	}
	
	/**
	 * Method to get the TreeViewer
	 * @return
	 */
	public TreeViewer getTreeViewer(){
		return viewer;
	}

	/**
	 * Method to initialize and set the root of the TreeView
	 */
	private void initialize() {
		BqTreeParent root = new BqTreeParent("TenantGroup");
		invisibleRoot = new BqTreeParent("");
		invisibleRoot.addChild(root);
		makeWorkSpace(BqConstant.BqTreePath);		
	}

	/**
	 * Method to create the control
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new BqTreeContentProvider());
		viewer.setLabelProvider(new BqTreeLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(invisibleRoot);
		CreateContextMenu();
		drillDownAdapter.addNavigationActions(getViewSite().getActionBars().getToolBarManager());
		
	}
	
	/**
	 * Method to make the workspace for the Bench4Q_Script testing in order 
	 * to save some configuration files.
	 * @param Path
	 */
	private void makeWorkSpace(String Path){
		File workSpace = new File(Path);
    	if(!workSpace.exists()){
    		workSpace.mkdir();
    	}
	}

	/**
	 * Method to get the selected object. This method is mainly used when select 
	 * the BqTreeObject when right click the mouse.
	 * @return
	 */
	private BqTreeObject GetSelectedObject(){
		StructuredSelection select = (StructuredSelection)viewer.getSelection();
		BqTreeObject element = (BqTreeObject)select.getFirstElement();
		return element;
	}
	
	/**
	 * Method to create the context menu when right click in the BqTreeView area.
	 */
	private void CreateContextMenu() {
		
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				BqTreeObject tmp = GetSelectedObject();
				if(tmp==null)
					return;
				if(tmp.getName().equals("TenantGroup")){
					BqTreeView.this.fillTenantGroupMenu(manager);
				}
				else if(tmp instanceof BqTreeParent){
					BqTreeView.this.fillTenantMenu(manager);
				}
				else{
					BqTreeView.this.fillAgentMenu(manager);
				}
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getTree());
		viewer.getTree().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	/**
	 * Method to create menu for the tenant group
	 * @param manager
	 */
	private void fillTenantGroupMenu(IMenuManager manager){
		manager.add(new StartAllAction());
		manager.add(new NewTenantAction((BqTreeParent)GetSelectedObject()));	
	}
	
	/**
	 * Method to create menu for the tenant
	 * @param manager
	 */
	private void fillTenantMenu(IMenuManager manager){
		manager.add(new ConfigAction((BqTreeParent)GetSelectedObject()));
		manager.add(new StartTenantAction((BqTreeParent)GetSelectedObject()));
		manager.add(new DeployAction((BqTreeParent)GetSelectedObject()));
		manager.add(new ScriptAction((BqTreeParent)GetSelectedObject()));
		manager.add(new NewAgentAction((BqTreeParent)GetSelectedObject()));	
		
	}
	
	/**
	 * Method to create menu for the agent
	 * @param manager
	 */
	private void fillAgentMenu(IMenuManager manager){
		manager.add(new AgentInfoAction((BqTreeObject)GetSelectedObject()));
		manager.add(new CommandLineAction((BqTreeObject)GetSelectedObject()));
	}

	/**
	 * Method to set the focus to this view
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}