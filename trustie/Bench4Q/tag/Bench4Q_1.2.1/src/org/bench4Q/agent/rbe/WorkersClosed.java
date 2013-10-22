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
/**
 * 2009-9-7
 * author: duanzhiquan
 * Technology Center for Software Engineering
 * Institute of Software, Chinese Academy of Sciences
 * Beijing 100190, China 
 * Email:duanzhiquan07@otcaix.iscas.ac.cn
 * 
 * 
 */
package org.bench4Q.agent.rbe;

import java.util.ArrayList;

import org.bench4Q.agent.rbe.communication.Args;

/**
 * @author duanzhiquan
 *
 */
public class WorkersClosed extends Workers {

	private ArrayList<EB> ebs;

	/**
	 * @param startTime
	 * @param triggerTime
	 * @param stdyTime
	 * @param baseLoad
	 * @param randomLoad
	 * @param rate
	 * @param args
	 */
	public WorkersClosed(long startTime, long triggerTime, long stdyTime, int baseLoad,
			int randomLoad, int rate, Args args) {
		super(startTime, triggerTime, stdyTime, baseLoad, randomLoad, rate, args);

		ebs = new ArrayList<EB>();
		long maxEBNum = 0;
		if (m_rate > 0) {
			maxEBNum = m_baseLoad + m_randomLoad + m_rate * m_stdyTime;
		} else {
			maxEBNum = m_baseLoad + m_randomLoad;
		}

		// initialize the pool
		for (int j = 0; j < maxEBNum + 5; j++) {
			EB eb = new EBClosed(m_args);
			eb.setDaemon(true);
			eb.start();
			ebs.add(eb);
		}
	}

	void StartEB() {

		long beginTime = System.currentTimeMillis();
		long endTime = beginTime + m_stdyTime * 1000L;

		int currentLoad = 0;
		int baseLoad = m_baseLoad;

		while (((System.currentTimeMillis() - endTime) < 0) && (baseLoad > 0)) {
			int realLoad = baseLoad + m_randomLoad;
			if (realLoad > currentLoad) {
				for (int j = currentLoad; j <= realLoad; j++) {
					((EBClosed) ebs.get(j)).setTest(true);
				}
			} else if (realLoad < currentLoad) {
				for (int j = currentLoad; j >= realLoad; j--) {
					((EBClosed) ebs.get(j - 1)).setTest(false);
				}
			}
			baseLoad += m_rate;
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (EB eb : ebs) {
			((EBClosed) eb).stop();
		}
		ebs = null;

	}
}
