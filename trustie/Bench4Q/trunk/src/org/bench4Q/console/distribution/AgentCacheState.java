/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
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

package org.bench4Q.console.distribution;

import java.beans.PropertyChangeListener;

/**
 * Simplistic model of remote file caches.
 * 
 * <p>
 * This tracks the state of all the caches, so {@link #getOutOfDate()} will
 * return <code>true</code> if any one of the caches is out of date. For per
 * cache information, see {@link AgentSet#getAddressOfOutOfDateAgents}.
 * </p>
 */
public interface AgentCacheState {

	/**
	 * Enquire whether one or more agent caches is out of date.
	 * 
	 * @return <code>true</code> => at least one agent cache is out of date.
	 */
	boolean getOutOfDate();

	/**
	 * Inform that agent caches are out of date due to a change to a file.
	 * Called whenever a new or modified file is found.
	 * 
	 * @param time
	 *            Caches need to be refreshed with files newer than this time
	 *            (milliseconds since Epoch).
	 */
	void setNewFileTime(long time);

	/**
	 * Allow other parties to register their interest in changes to our state.
	 * 
	 * @param listener
	 *            Listener to notify on a state change.
	 */
	void addListener(PropertyChangeListener listener);
}
