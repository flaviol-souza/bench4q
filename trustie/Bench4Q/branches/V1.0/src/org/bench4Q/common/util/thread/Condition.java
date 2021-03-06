// Copyright (C) 2005, 2006, 2007 Philip Aston
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

package org.bench4Q.common.util.thread;

import org.bench4Q.common.UncheckedInterruptedException;

/**
 * Object used for synchronisation.
 * 
 * @author Philip Aston
 * @version $Revision: 3762 $
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
	public void waitNoInterrruptException() throws UncheckedInterruptedException {
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
	public void waitNoInterrruptException(long timeout) throws UncheckedInterruptedException {
		try {
			super.wait(timeout);
		} catch (InterruptedException e) {
			throw new UncheckedInterruptedException(e);
		}
	}
}
