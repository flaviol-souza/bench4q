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

package org.bench4Q.console.communication;

import org.bench4Q.agent.messages.CacheHighWaterMark;
import org.bench4Q.common.communication.Address;
import org.bench4Q.common.util.FileContents;

/**
 * Interface for sending files to the agent process caches.
 * 
 */
public interface DistributionControl {

	/**
	 * Signal agents matching the given address to clear their file caches.
	 * 
	 * @param address
	 *            The address of the agents.
	 */
	void clearFileCaches(Address address);

	/**
	 * Send a file to the agents matching the given address.
	 * 
	 * @param address
	 *            The address of the agents.
	 * @param fileContents
	 *            The file contents.
	 */
	void sendFile(Address address, FileContents fileContents);

	/**
	 * Inform agent processes of a checkpoint of the cache state. Each agent
	 * should maintain this (perhaps persistently), and report it in status
	 * reports.
	 * 
	 * @param address
	 *            The address of the agents.
	 * @param highWaterMark
	 *            A checkpoint of the cache state.
	 */
	void setHighWaterMark(Address address, CacheHighWaterMark highWaterMark);
}
