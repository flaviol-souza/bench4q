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

package org.bench4Q.common.processidentity;

import org.bench4Q.agent.rbe.communication.EBStats;

/**
 * Common interface for enquiring about a process.
 * 
 * @author Philip Aston
 * @version $Revision: 3878 $
 */
public interface ResultReport {

	/**
	 * Constant representing the "connected" state.
	 */
	short STATE_CONNECTED = 0;

	/**
	 * Constant representing the "started" state.
	 */
	short STATE_STARTED = 1;

	/**
	 * Constant representing the "running" state.
	 */
	short STATE_RUNNING = 2;

	/**
	 * Constant representing the "finished" state.
	 */
	short STATE_FINISHED = 3;

	/**
	 * Constant representing the "unknown" state.
	 */
	short STATE_UNKNOWN = 4;

	/**
	 * Return the unique process identity.
	 * 
	 * @return The process identity.
	 */
	ProcessIdentity getIdentity();

	EBStats getEBStats();

	// /**
	// * Comparator that compares ProcessReports by state, then by name.
	// */
	// final class StateThenNameThenNumberComparator implements Comparator {
	// public int compare(Object o1, Object o2) {
	// final ResultReport processReport1 = (ResultReport) o1;
	// final ResultReport processReport2 = (ResultReport) o2;
	//
	// final int stateComparison = processReport1.getState() -
	// processReport2.getState();
	//
	// if (stateComparison == 0) {
	// final int nameComparison =
	// processReport1.getIdentity().getName().compareTo(
	// processReport2.getIdentity().getName());
	//
	// if (nameComparison == 0) {
	// return processReport1.getIdentity().getNumber()
	// - processReport2.getIdentity().getNumber();
	// } else {
	// return nameComparison;
	// }
	// } else {
	// return stateComparison;
	// }
	// }
	// }
}
