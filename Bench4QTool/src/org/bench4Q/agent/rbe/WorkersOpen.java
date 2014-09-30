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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.agent.rbe.communication.TypeFrequency;

/**
 * @author duanzhiquan
 * 
 */
public class WorkersOpen extends Workers {

	public WorkersOpen(long startTime, long triggerTime, long stdyTime,
			int baseLoad, int randomLoad, int rate, TestPhase testPhase,
			Args args, int identity) {

		super(startTime, triggerTime, stdyTime, baseLoad, randomLoad, rate,
				testPhase, args, identity);

		this.trace = new ArrayList<ArrayList<Integer>>();
		if (this.m_args.isReplay()) {
			try {
				FileInputStream fi = new FileInputStream(this.m_args.getTime()
						+ "-" + identity);
				ObjectInputStream ois = new ObjectInputStream(fi);
				this.trace = ((ArrayList<ArrayList<Integer>>) ois.readObject());
				fi.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	void StartEB() {

		int corePoolSize = this.m_baseLoad * 2;
		int maximumPoolSize = this.m_baseLoad * 5;
		int keepAliveTime = 60;
		int workQueueLength = (int) (this.m_baseLoad * 0.8D);

		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(corePoolSize,
				maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(workQueueLength),
				new ThreadPoolExecutor.AbortPolicy());
		long beginTime = System.currentTimeMillis();
		long endTime = beginTime + this.m_stdyTime * 1000L;
		int baseLoad = this.m_baseLoad;

		int realLoad = baseLoad + m_randomLoad;
		long timeInt = System.currentTimeMillis();
		Map<Integer, PropertiesEB> mapProperties = new HashMap<Integer, PropertiesEB>();
		for (int indexEB = 0; indexEB < realLoad; indexEB++) {
			TypeFrequency type = TypeFrequency.getType(m_args
					.getTypeFrenquency());
			PropertiesEB propertiesEB = FrequencySettings.createProperties(
					indexEB, m_testPhase, type, timeInt);
			mapProperties.put(indexEB, propertiesEB);
		}

		while ((!isStop()) && (System.currentTimeMillis() - endTime < 0L)) {
			long stime = System.currentTimeMillis();
			realLoad = baseLoad + this.m_randomLoad;

			for (int j2 = 0; j2 < realLoad; j2++) {
				ArrayList<Integer> tra = new ArrayList<Integer>();
				if (this.m_args.isReplay()) {
					tra = (ArrayList<Integer>) this.trace.get(j2);
				} else {
					this.trace.add(tra);
				}
				boolean isVIP = Math.random() < this.m_args.getRate() / 100.0D;
				EB eb = new EBOpen(this.m_args, tra, isVIP);
				eb.setPropertiesEB(mapProperties.get(j2));
				eb.setDaemon(true);
				try {
					threadPool.execute(eb);
				} catch (RejectedExecutionException e) {
					EBStats.getEBStats().addErrorSession(0, eb.isVIP);
					EBStats.getEBStats().error(0, "Request is rejected!",
							this.m_args.getBaseURL(), eb.isVIP);
				}
			}
			baseLoad += this.m_rate;
			long etime = System.currentTimeMillis();
			long interval = (long) (1000.0D * this.m_args.getInterval());
			if (etime - stime < interval) {
				try {
					Thread.sleep(interval - (etime - stime));
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();

				}
			}
		}
	}
}
