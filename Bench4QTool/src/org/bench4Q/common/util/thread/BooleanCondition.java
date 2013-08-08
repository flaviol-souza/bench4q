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

/**
 * Lock object that has two states. A caller can wait for the state to change to
 * a particular value, but can also be woken by another thread.
 */
public final class BooleanCondition {
	private final Condition m_condition = new Condition();
	private boolean m_state = false;
	private int m_waiters = 0;
	private boolean m_wakeUp;

	/**
	 * Wait for our state to match the passed value.
	 * 
	 * @param state
	 *            State to wait for.
	 * @return The new state value. Can differ from the parameter if we have
	 *         been woken by another thread calling {@link #wakeUpAllWaiters()}.
	 */
	public boolean await(boolean state) {
		synchronized (m_condition) {
			++m_waiters;

			try {
				while (m_state != state && !m_wakeUp) {
					m_condition.waitNoInterrruptException();
				}
			} finally {
				--m_waiters;
				m_condition.notifyAll();
			}

			return m_state;
		}
	}

	/**
	 * Set the state to the passed value.
	 * 
	 * @param state
	 *            The new state.
	 */
	public void set(boolean state) {
		synchronized (m_condition) {
			m_state = state;
			m_condition.notifyAll();
		}
	}

	/**
	 * Query the state without blocking.
	 * 
	 * @return The state.
	 */
	public boolean get() {
		synchronized (m_condition) {
			return m_state;
		}
	}

	/**
	 * Wake up other threads that are waiting in {@link #await(boolean)}.
	 */
	public void wakeUpAllWaiters() {
		synchronized (m_condition) {
			if (m_waiters == 0) {
				return;
			}

			m_wakeUp = true;
			m_condition.notifyAll();

			try {
				while (m_waiters > 0) {
					m_condition.waitNoInterrruptException();
				}
			} finally {
				m_wakeUp = false;
				m_condition.notifyAll();
			}
		}
	}
}
