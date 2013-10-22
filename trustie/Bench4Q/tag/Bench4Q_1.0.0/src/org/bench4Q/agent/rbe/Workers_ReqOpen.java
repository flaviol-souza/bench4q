/**
 * =========================================================================
 * 					Bench4Q version 1.0.0
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
package org.bench4Q.agent.rbe;

import org.bench4Q.agent.rbe.communication.Args;

public class Workers_ReqOpen extends Workers {

	private EBReqPool m_reqpool;

	public Workers_ReqOpen(long startTime, long triggerTime, long stdyTime, int baseLoad,
			int randomLoad, int rate, Args args) {
		super(startTime, triggerTime, stdyTime, baseLoad, randomLoad, rate, args);

		m_reqpool = new EBReqPool(m_args);

		long maxEBNum = 0;
		if (m_rate > 0) {
			maxEBNum = m_baseLoad + m_randomLoad + m_rate * m_stdyTime;
		} else {
			maxEBNum = m_baseLoad + m_randomLoad;
		}

		m_reqpool.initialize((int) (maxEBNum * 1.5));

	}

	@Override
	void StartEB() {
		long beginTime = System.currentTimeMillis();
		long endTime = beginTime + m_stdyTime * 1000L;
		int baseLoad = m_baseLoad;
		while ((System.currentTimeMillis() - endTime) < 0) {
			long stime = System.currentTimeMillis();
			int realLoad = baseLoad + m_randomLoad;

			for (int j2 = 0; j2 < realLoad; j2++) {
				EB_ReqOpen eb = m_reqpool.getEB();
				eb.go();
			}
			baseLoad += m_rate;
			long etime = System.currentTimeMillis();
			long interval = (long) ((long) 1000 * m_args.getInterval());
			if ((etime - stime) < interval) {
				try {
					Thread.sleep((interval - (etime - stime)));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				// interval is not long enough to finish the work. This kind
				// of situation is not handled.Harry up to do next work.
			}
		}

		m_reqpool.clean();
	}

}
