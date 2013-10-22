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

package org.bench4Q.common.util.thread;

import org.bench4Q.common.UncheckedInterruptedException;

/**
 * Object used for synchronisation.
 * 
 * @see UncheckedInterruptedException
 */
public class Condition {

	/**
	 * Wait until we are notified, or receive an {@link InterruptedException}.
	 * 
	 * @see Object#wait()
	 * @throws UncheckedInterruptedException
	 *             If we receive an {@link InterruptedException}.
	 */
	public void waitNoInterrruptException()
			throws UncheckedInterruptedException {
		try {
			super.wait();
		} catch (InterruptedException e) {
			throw new UncheckedInterruptedException(e);
		}
	}

	/**
	 * Wait until we are notified, time out, or receive an
	 * {@link InterruptedException}.
	 * 
	 * @param timeout
	 *            the maximum time to wait in milliseconds.
	 * @see Object#wait(long)
	 * @throws UncheckedInterruptedException
	 *             If we receive an {@link InterruptedException}.
	 */
	public void waitNoInterrruptException(long timeout)
			throws UncheckedInterruptedException {
		try {
			super.wait(timeout);
		} catch (InterruptedException e) {
			throw new UncheckedInterruptedException(e);
		}
	}
}
