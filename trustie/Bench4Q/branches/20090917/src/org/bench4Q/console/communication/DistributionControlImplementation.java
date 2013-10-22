// Copyright (C) 2007 - 2008 Philip Aston
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

package org.bench4Q.console.communication;

import org.bench4Q.agent.messages.CacheHighWaterMark;
import org.bench4Q.agent.messages.ClearCacheMessage;
import org.bench4Q.agent.messages.DistributeFileMessage;
import org.bench4Q.agent.messages.DistributionCacheCheckpointMessage;
import org.bench4Q.common.communication.Address;
import org.bench4Q.common.util.FileContents;

/**
 * Implementation of {@link DistributionControl}.
 * 
 * @author Philip Aston
 * @version $Revision:$
 */
public class DistributionControlImplementation implements DistributionControl {

	private final ConsoleCommunication m_consoleCommunication;

	/**
	 * Constructor.
	 * 
	 * @param consoleCommunication
	 *            The console communication handler.
	 */
	public DistributionControlImplementation(ConsoleCommunication consoleCommunication) {
		m_consoleCommunication = consoleCommunication;
	}

	/**
	 * Signal agents matching the given address to clear their file caches.
	 * 
	 * @param address
	 *            The address of the agents.
	 */
	public void clearFileCaches(Address address) {
		m_consoleCommunication.sendToAddressedAgents(address, new ClearCacheMessage());
	}

	/**
	 * Send a file to the agents matching the given address.
	 * 
	 * @param address
	 *            The address of the agents.
	 * @param fileContents
	 *            The file contents.
	 */
	public void sendFile(Address address, FileContents fileContents) {
		m_consoleCommunication.sendToAddressedAgents(address, new DistributeFileMessage(
				fileContents));
	}

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
	public void setHighWaterMark(Address address, CacheHighWaterMark highWaterMark) {
		m_consoleCommunication.sendToAddressedAgents(address,
				new DistributionCacheCheckpointMessage(highWaterMark));
	}
}
