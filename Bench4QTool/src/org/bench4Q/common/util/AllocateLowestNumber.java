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
package org.bench4Q.common.util;

/**
 * A set that associates an number with each of member. Objects added to the set
 * are allocated the lowest available number, starting at 0.
 */
public interface AllocateLowestNumber {

	/**
	 * Add a new object. If the object already belongs to the set, the existing
	 * associated number is returned.
	 * 
	 * @param o
	 *            The object.
	 * @return The associated number.
	 */
	int add(Object o);

	/**
	 * Remove an object from the set. The number previously associated with the
	 * object (if any) is freed for re-use.
	 * 
	 * @param o
	 *            The object.
	 */
	void remove(Object o);

	/**
	 * Call <code>iteratorCallback</code> for each member of the set.
	 * 
	 * @param iteratorCallback
	 *            Called for each member of the set.
	 */
	void forEach(IteratorCallback iteratorCallback);

	/**
	 * Iteration callback, see {@link AllocateLowestNumber#forEach}.
	 */
	interface IteratorCallback {
		/**
		 * Called for a member of the set.
		 * 
		 * @param object
		 *            The object.
		 * @param number
		 *            The associated number.
		 */
		void objectAndNumber(Object object, int number);
	}
}
