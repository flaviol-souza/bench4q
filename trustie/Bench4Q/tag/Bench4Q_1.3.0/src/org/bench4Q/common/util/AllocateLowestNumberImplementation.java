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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Implementation of
 * 
 * @{link {@link AllocateLowestNumber}.
 */
public final class AllocateLowestNumberImplementation implements
		AllocateLowestNumber {

	private static final long serialVersionUID = 1L;

	/** Guarded by self. */
	private Map m_map = new HashMap();

	/** Guarded by {@link #m_map}. */
	private int m_nextN = 0;

	/**
	 * Add a new object. If the object already belongs to the set, the existing
	 * associated number is returned.
	 * 
	 * @param o
	 *            The object.
	 * @return The associated number.
	 */
	public int add(Object o) {
		synchronized (m_map) {
			final Integer n = (Integer) m_map.get(o);

			if (n != null) {
				return n.intValue();
			}

			final int nextN = m_nextN;

			m_map.put(o, new Integer(nextN));

			++m_nextN;

			final int mapSize = m_map.size();

			while (m_nextN < mapSize
					&& m_map.containsValue(new Integer(m_nextN))) {
				++m_nextN;
			}

			return nextN;
		}
	}

	/**
	 * Remove an object from the set. The number previously associated with the
	 * object (if any) is freed for re-use.
	 * 
	 * @param o
	 *            The object.
	 */
	public void remove(Object o) {
		synchronized (m_map) {
			final Integer n = (Integer) m_map.remove(o);

			if (n != null) {
				if (n.intValue() <= m_nextN) {
					m_nextN = n.intValue();
				}
			}
		}
	}

	/**
	 * Call <code>iteratorCallback</code> for each member of the set.
	 * 
	 * @param iteratorCallback
	 *            Called for each member of the set.
	 */
	public void forEach(IteratorCallback iteratorCallback) {
		final Map clonedMap;
		synchronized (m_map) {
			clonedMap = new HashMap(m_map);
		}

		final Iterator iterator = clonedMap.entrySet().iterator();

		while (iterator.hasNext()) {
			final Entry entry = (Entry) iterator.next();
			final Integer n = (Integer) entry.getValue();

			iteratorCallback.objectAndNumber(entry.getKey(), n.intValue());
		}
	}
}
