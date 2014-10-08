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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.Iterator;
import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.common.util.Logger;

/**
 * @author duanzhiquan
 * 
 */
public class RBE implements Runnable {

	private Args m_args;
	private ArrayList<Workers> m_workers;
	private long TestInterval;

	public RBE() {
		this.m_workers = new ArrayList<Workers>();
		this.TestInterval = 0L;
	}

	public RBE(Args arg) {
		this();
		this.m_args = arg;
	}

	public void startWorkers() {
	}

	private void clear() {
		m_workers = null;
	}

	private long calculateTestInterval() {
		int max = 0;
		int workerEndTime;

		for (TestPhase testPhase : this.m_args.getEbs()) {
			workerEndTime = testPhase.getStdyTime() + testPhase.getTriggerTime();
			if (workerEndTime > max) {
				max = workerEndTime;
			}
		}
		return max;
	}

	public Args getArgs() {
		return this.m_args;
	}

	private String format(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dataString = formatter.format(date);
		return dataString;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();

		long prepairTime = this.m_args.getPrepair();
		long cooldown = this.m_args.getCooldown();
		long testInterval = 0L;
		int testPhaseEndTime;
		int baseLoadEbs = 0;
		for (TestPhase testPhase : this.m_args.getEbs()) {
			testPhaseEndTime = testPhase.getTriggerTime() + testPhase.getStdyTime();
			if (testPhaseEndTime > testInterval) {
				testInterval = testPhaseEndTime;
				Logger.getLogger().debug("testInterval: "+testInterval);
			}
			baseLoadEbs += testPhase.getBaseLoad();
		}

		EBStats.getEBStats().init(startTime, prepairTime, testInterval, cooldown);
		HttpClientFactory.setRetryCount(this.m_args.getRetry());
		Date date = new Date();
		EBStats.getEBStats().setTitle(format(date));
		EBStats.getEBStats().setVIPrate(this.m_args.getRate());
		int identity = 0;

		FrequencySettings.setQntWorkers(baseLoadEbs);
		if (this.m_args.getRbetype().equalsIgnoreCase("closed")) {
			Logger.getLogger().debug("# EBS Close: " + this.m_args.getEbs().size());
			for (TestPhase testPhase : this.m_args.getEbs()) {
				identity++;
				this.m_workers.add(new WorkersClosed(startTime, testPhase.getTriggerTime(), testPhase.getStdyTime(),
						testPhase.getBaseLoad(), testPhase.getRandomLoad(), testPhase.getRate(), testPhase,
						this.m_args, identity));
			}
		} else if (this.m_args.getRbetype().equalsIgnoreCase("open")) {
			Logger.getLogger().debug("# EBS Open: " + this.m_args.getEbs().size());
			for (TestPhase testPhase : this.m_args.getEbs()) {
				identity++;
				this.m_workers.add(new WorkersOpen(startTime, testPhase.getTriggerTime(), testPhase.getStdyTime(),
						testPhase.getBaseLoad(), testPhase.getRandomLoad(), testPhase.getRate(), testPhase,
						this.m_args, identity));
			}
		} else {
			System.out.println("Error parameter.");
			System.out.println("Start closed as default.");
			for (TestPhase testPhase : this.m_args.getEbs()) {
				identity++;
				this.m_workers.add(new WorkersClosed(startTime, testPhase.getTriggerTime(), testPhase.getStdyTime(),
						testPhase.getBaseLoad(), testPhase.getRandomLoad(), testPhase.getRate(), testPhase,
						this.m_args, identity));
				Logger.getLogger().debug("Error: " + testPhase.getRate());
			}
		}
		Logger.getLogger().debug("m_workers: " + this.m_workers.size());
		for (Workers worker : this.m_workers) {
			worker.setDaemon(true);
			worker.start();
		}

		this.TestInterval = calculateTestInterval();
		long endTime = startTime + this.TestInterval * 1000L;
		try {
			Thread.sleep(this.TestInterval * 1000L);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {

			for (Workers worker : this.m_workers) {
				worker.setStop(true);
			}
			Logger.getLogger().info("Test finished.");

			clear();
		}

	}

}
