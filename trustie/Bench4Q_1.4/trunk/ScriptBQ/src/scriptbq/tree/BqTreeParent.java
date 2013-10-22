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

import java.util.ArrayList;

/**
 * This class is the subclass of the BqTreeObject. The class BqTreeParent
 * represents the parent of BqTreeObject. In Bench4Q_Script, it represents
 * the Tenant.
 */
public class BqTreeParent extends BqTreeObject{
	
	/**
	 * The list of all the children
	 */
	private ArrayList<BqTreeObject> children;
	
	/**
	 * The parameter stands for the length of tenant testing
	 */
	private long stdyTime;
	
	/**
	 * constructer
	 * @param name
	 */
	public BqTreeParent(String name) {
		super(name);
		children = new ArrayList<BqTreeObject>();
	}
	/*
	 * Method to add child of the parent
	 */
	public void addChild(BqTreeObject child) {
		children.add(child);
		child.setParent(this);
	}
	
	/**
	 * Method to remove the child from the parent
	 * @param child
	 */
	public void removeChild(BqTreeObject child) {
		children.remove(child);
		child.setParent(null);
	}
	
	/**
	 * Method to get all the children under the parent
	 * @return
	 */
	public BqTreeObject [] getChildren() {
		return (BqTreeObject [])children.toArray(new BqTreeObject[children.size()]);
	}
	
	/**
	 * Check whether the BqTreeParent has child
	 * @return
	 */
	public boolean hasChildren() {
		return children.size()>0;
	}
	
	/**
	 * Method to set the stdyTime for the tenant
	 * @param stdyTime
	 */
	public void setStdyTime(long stdyTime){
		this.stdyTime=stdyTime;
	}
	
	/**
	 * Method to get the stdyTime of the tenant
	 * @return
	 */
	public long getStdyTime(){
		return stdyTime;
	}
}
