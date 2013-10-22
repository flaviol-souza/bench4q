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

import java.util.ArrayList;

import org.bench4Q.agent.rbe.communication.Args;

public class Workers_EBOpen extends Workers {

	public Workers_EBOpen(long startTime, long triggerTime, long stdyTime, int baseLoad,
			int randomLoad, int rate, Args args) {
		super(startTime, triggerTime, stdyTime, baseLoad, randomLoad, rate, args);

	}

	@Override
	void StartEB() {
		ArrayList<EB_EBOpen> ebopens = new ArrayList<EB_EBOpen>();

		long beginTime = System.currentTimeMillis();
		long endTime = beginTime + m_stdyTime * 1000L;
		int baseLoad = m_baseLoad;
		while ((System.currentTimeMillis() - endTime) < 0) {
			long stime = System.currentTimeMillis();
			int realLoad = baseLoad + m_randomLoad;

			for (int j2 = 0; j2 < realLoad; j2++) {
				EB eb = new EB_EBOpen(m_args);
				eb.setDaemon(true);
				eb.start();
				ebopens.add((EB_EBOpen) eb);
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
		for (EB_EBOpen eb : ebopens) {
			eb.stop();
		}

	}
}
