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

import org.bench4Q.common.communication.Message;
import org.bench4Q.common.util.FileContents;

/**
 * Message used to distribute a file from the console to the agent processes.
 */
public final class DistributeFileMessage implements Message {

	private static final long serialVersionUID = -4338519775293350257L;

	private final FileContents m_fileContents;

	/**
	 * Constructor.
	 * 
	 * @param fileContents
	 *            The file contents to distribute.
	 */
	public DistributeFileMessage(FileContents fileContents) {
		m_fileContents = fileContents;
	}

	/**
	 * Return the file contents.
	 * 
	 * @return The file contents.
	 */
	public FileContents getFileContents() {
		return m_fileContents;
	}
}
