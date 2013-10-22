/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there.
 * If you have any problem, you can  
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
 *  * Developer(s): Wang Sa.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.console.ui.controlTree;

import javax.swing.ImageIcon;

import org.bench4Q.console.common.Resources;

/**
 * @author wang sa
 *
 */
public class DatabasePCNode implements TestElement{

	private final Resources m_resources;
	private String m_ip;

	/**
	 * @param resources
	 */
	public DatabasePCNode(Resources resources, String ip) {
		m_resources = resources;
		m_ip = ip;
	}

	public String getTip() {
		return m_ip;
	}

	public String getName() {
		return m_ip;
	}

	public ImageIcon getImageIcon() {
		final ImageIcon imageIcon = m_resources
				.getImageIcon("ControlTree.ServerPCNode.image");
		if (imageIcon != null) {
			return imageIcon;
		}
		return null;
	}

	public String toString() {
		return m_ip;
	}

}

