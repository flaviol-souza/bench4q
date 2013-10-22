// Copyright (C) 2005 - 2008 Philip Aston
// All rights reserved.
//
// This file is part of The Grinder software distribution. Refer to
// the file LICENSE which is part of The Grinder distribution for
// licensing details. The Grinder distribution is available on the
// Internet at http://grinder.sourceforge.net/
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

package org.bench4Q.common.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Generic support for listeners.
 * 
 * @author Philip Aston
 * @version $Revision: 3940 $
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
	 * Adapter interface for use with {@link
	 * ListenerSupport#apply(net.grinder.util.ListenerSupport.Informer)}.
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
	 * Adapter interface for use with {@link
	 * ListenerSupport#apply(net.grinder.util.ListenerSupport.HandlingInformer)}.
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
