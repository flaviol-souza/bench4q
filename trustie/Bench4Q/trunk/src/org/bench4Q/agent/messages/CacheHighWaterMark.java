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

package org.bench4Q.agent.messages;

import java.io.Serializable;

/**
 * A timed checkpoint of the agent cache state.
 */
public interface CacheHighWaterMark extends Serializable {

	/**
	 * Return whether this <code>CacheHighWaterMark</code> was generated for the
	 * same cache as the given <code>CacheHighWaterMark</code>.
	 * 
	 * 
	 * @param other
	 *            The <code>CacheHighWaterMark</code> to compare.
	 * @return <code>true</code> if and only if this cache state is for the same
	 *         cache.
	 */
	boolean isForSameCache(CacheHighWaterMark other);

	/**
	 * Get the checkpoint time.
	 * 
	 * @return The time.
	 */
	long getTime();
}
