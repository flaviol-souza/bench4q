/**
 * =========================================================================
 * 					Bench4Q version 1.0.0
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there.  
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
 *  * Initial developer(s): Zhiquan Duan.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */

package org.bench4Q.console.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Composite <code>Action</code> which fans out events to two delegates.
 */
final class TeeAction extends AbstractAction {

	private final Action m_action1;
	private final Action m_action2;

	public TeeAction(Action action1, Action action2) {
		m_action1 = action1;
		m_action2 = action2;
	}

	public void actionPerformed(ActionEvent e) {

		if (m_action1.isEnabled()) {
			m_action1.actionPerformed(e);
		}

		if (m_action2.isEnabled()) {
			m_action2.actionPerformed(e);
		}
	}

	public boolean isEnabled() {
		return m_action1.isEnabled() || m_action2.isEnabled();
	}
}
