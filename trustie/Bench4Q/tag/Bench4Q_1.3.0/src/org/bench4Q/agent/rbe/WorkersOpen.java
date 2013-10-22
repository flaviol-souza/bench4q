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
package org.bench4Q.agent.rbe;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bench4Q.agent.rbe.communication.Args;

/**
 * @author duanzhiquan
 * 
 */
public class WorkersOpen extends Workers {

	/**
	 * @param startTime
	 * @param triggerTime
	 * @param stdyTime
	 * @param baseLoad
	 * @param randomLoad
	 * @param rate
	 * @param args
	 */
	public WorkersOpen(long startTime, long triggerTime, long stdyTime,
			int baseLoad, int randomLoad, int rate, Args args) {
		super(startTime, triggerTime, stdyTime, baseLoad, randomLoad, rate,
				args);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bench4Q.agent.rbe.Workers#StartEB()
	 * 
	 * using thread pool.
	 */
	@Override
	void StartEB() {

		int corePoolSize = m_baseLoad * 10;
		int maximumPoolSize = 50000;
		int keepAliveTime = 60;
		int workQueueLength = (int) (m_baseLoad * 0.8);

		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize,
				maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(workQueueLength),
				new ThreadPoolExecutor.CallerRunsPolicy());

		long beginTime = System.currentTimeMillis();
		long endTime = beginTime + m_stdyTime * 1000L;
		int baseLoad = m_baseLoad;
		while ((System.currentTimeMillis() - endTime) < 0) {
			long stime = System.currentTimeMillis();
			int realLoad = baseLoad + m_randomLoad;

			for (int j2 = 0; j2 < realLoad; j2++) {
				EB eb = new EBOpen(m_args);
				eb.setDaemon(true);
				threadPool.execute(eb);
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

	}
}
