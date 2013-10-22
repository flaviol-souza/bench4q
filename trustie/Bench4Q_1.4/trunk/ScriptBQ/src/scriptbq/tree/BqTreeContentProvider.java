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

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * This class is used to provide content for BqTreeView. The model of TreeViewer in JFace is MVC,
 * BqTreeContentProvider provides the content of it. This class is necessary for the BqTreeViewer
 * to show out.
 */
public class BqTreeContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	/**
	 * Method for input changed
	 */
	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}
	
	/**
	 * Method to dispose the TreeViewer
	 */
	public void dispose() {
	}
	
	/**
	 * Method to get the contents of TreeViewer
	 */
	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}
	
	/**
	 * Method to get the parent of element on the TreeViewer
	 * @param child   The specified child on the Tree
	 */
	public Object getParent(Object child) {
		if (child instanceof BqTreeObject) {
			return ((BqTreeObject)child).getParent();
		}
		return null;
	}
	
	/**
	 * Method to get all Children of the specified parent
	 * @param parent  The specified parent on the Tree
	 */
	public Object [] getChildren(Object parent) {
		if (parent instanceof BqTreeParent) {
			return ((BqTreeParent)parent).getChildren();
		}
		return new Object[0];
	}
	
	/**
	 * Method to chenck if the element on the Tree has child
	 * @param parent   The specified elment on the Tree
	 * @return   The bool value implies whether the element has child on the Tree
	 */
	public boolean hasChildren(Object parent) {
		if (parent instanceof BqTreeParent)
			return ((BqTreeParent)parent).hasChildren();
		return false;
	}

}
