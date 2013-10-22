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

import java.util.LinkedList;
import java.util.List;

/**
 * Generic support for listeners.
 */
public final class ListenerSupport {

	private final List m_listeners = new LinkedList();

	/**
	 * Add a listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	public void add(Object listener) {
		synchronized (m_listeners) {
			m_listeners.add(listener);
		}
	}

	/**
	 * Adapter interface
	 */
	public interface Informer {

		/**
		 * Should notify the listener appropriately.
		 * 
		 * @param listener
		 *            The listener.
		 */
		void inform(Object listener);
	}

	/**
	 * Adapter interface
	 */
	public interface HandlingInformer {

		/**
		 * Should notify the listener appropriately.
		 * 
		 * @param listener
		 *            The listener.
		 * @return <code>true</code> => event handled, do not delegate to
		 *         further Handlers.
		 */
		boolean inform(Object listener);
	}

	/**
	 * Notify the listeners of an event.
	 * 
	 * @param informer
	 *            An adapter to be applied to each listener.
	 */
	public void apply(Informer informer) {
		final Object[] cloneList;

		synchronized (m_listeners) {
			cloneList = m_listeners.toArray(new Object[m_listeners.size()]);
		}

		for (int i = 0; i < cloneList.length; ++i) {
			informer.inform(cloneList[i]);
		}
	}

	/**
	 * Notify the listeners of an event.
	 * 
	 * @param handler
	 *            An adapter to be applied to each listener.
	 * @return <code>true</code> => a listener handled the event.
	 */
	public boolean apply(HandlingInformer handler) {
		final Object[] cloneList;

		synchronized (m_listeners) {
			cloneList = m_listeners.toArray(new Object[m_listeners.size()]);
		}

		for (int i = 0; i < cloneList.length; ++i) {
			if (handler.inform(cloneList[i])) {
				return true;
			}
		}

		return false;
	}
}
