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

	/**
	 * 
	 */
	public RBE() {
		m_workers = new ArrayList<Workers>();
		TestInterval = 0;
	}

	/**
	 * @param arg
	 */
	public RBE(Args arg) {
		this();
		m_args = arg;
	}

	/**
	 * 
	 */
	public void startWorkers() {
		// long startTime = System.currentTimeMillis();
		//
		// long prepairTime = m_args.getPrepair();
		// long cooldown = m_args.getCooldown();
		// long testInterval = 0;
		// int testPhaseEndTime;
		// for (TestPhase testPhase : m_args.getEbs()) {
		// testPhaseEndTime = testPhase.getTriggerTime()
		// + testPhase.getStdyTime();
		// if (testPhaseEndTime > testInterval) {
		// testInterval = testPhaseEndTime;
		// }
		// }
		//
		// EBStats.getEBStats().init(startTime, prepairTime, testInterval,
		// cooldown);
		// HttpClientFactory.setRetryCount(m_args.getRetry());
		// Date date = new Date();
		// EBStats.getEBStats().setTitle(format(date));
		// EBStats.getEBStats().setVIPrate(m_args.getRate());
		// int identity=0;
		//
		// if (m_args.getRbetype().equalsIgnoreCase("closed")) {
		// for (TestPhase testPhase : m_args.getEbs()) {
		// identity++;
		// m_workers.add(new WorkersClosed(startTime, testPhase
		// .getTriggerTime(), testPhase.getStdyTime(), testPhase
		// .getBaseLoad(), testPhase.getRandomLoad(), testPhase
		// .getRate(), m_args, identity));
		// }
		// } else if (m_args.getRbetype().equalsIgnoreCase("open")) {
		// for (TestPhase testPhase : m_args.getEbs()) {
		// identity++;
		// m_workers.add(new WorkersOpen(startTime, testPhase
		// .getTriggerTime(), testPhase.getStdyTime(), testPhase
		// .getBaseLoad(), testPhase.getRandomLoad(), testPhase
		// .getRate(), m_args, identity));
		// }
		// } else {
		// System.out.println("Error parameter.");
		// System.out.println("Start closed as default.");
		// for (TestPhase testPhase : m_args.getEbs()) {
		// identity++;
		// m_workers.add(new WorkersClosed(startTime, testPhase
		// .getTriggerTime(), testPhase.getStdyTime(), testPhase
		// .getBaseLoad(), testPhase.getRandomLoad(), testPhase
		// .getRate(), m_args, identity));
		// }
		// }
		//
		// for (Workers worker : m_workers) {
		// worker.setDaemon(true);
		// worker.start();
		// }
		//
		// TestInterval = calculateTestInterval();
		// long endTime = startTime + TestInterval * 1000L;
		//
		// try {
		// Thread.sleep(TestInterval * 1000L);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		//
		// boolean flag = true;
		//
		// while (((System.currentTimeMillis() - endTime) < 0) && flag) {
		// flag = false;
		// for (Workers worker : m_workers) {
		// if (worker.isAlive()) {
		// flag = true;
		// worker.stop();
		// }
		// }
		// try {
		// Thread.sleep(1000L);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		//
		// clear();
	}

	private void clear() {
		m_workers = null;
	}

	private long calculateTestInterval() {
		int max = 0;
		int workerEndTime;

		for (TestPhase testPhase : m_args.getEbs()) {
			workerEndTime = testPhase.getStdyTime()
					+ testPhase.getTriggerTime();
			if (workerEndTime > max) {
				max = workerEndTime;
			}
		}
		return max;
	}

	/**
	 * @return args
	 */
	public Args getArgs() {
		return m_args;
	}

	private String format(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dataString = formatter.format(date);
		return dataString;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();

		long prepairTime = m_args.getPrepair();
		long cooldown = m_args.getCooldown();
		long testInterval = 0;
		int testPhaseEndTime;
		int baseLoadEbs = 0;
		for (TestPhase testPhase : m_args.getEbs()) {
			testPhaseEndTime = testPhase.getTriggerTime()
					+ testPhase.getStdyTime();
			if (testPhaseEndTime > testInterval) {
				testInterval = testPhaseEndTime;
			}
			baseLoadEbs += testPhase.getBaseLoad();
		}

		EBStats.getEBStats().init(startTime, prepairTime, testInterval,
				cooldown);
		HttpClientFactory.setRetryCount(m_args.getRetry());
		Date date = new Date();
		EBStats.getEBStats().setTitle(format(date));
		EBStats.getEBStats().setVIPrate(m_args.getRate());
		int identity = 0;

		FrequencySettings.setQntWorkers(baseLoadEbs);
		if (m_args.getRbetype().equalsIgnoreCase("closed")) {
			for (TestPhase testPhase : m_args.getEbs()) {
				identity++;
				m_workers.add(new WorkersClosed(startTime, testPhase
						.getTriggerTime(), testPhase.getStdyTime(), testPhase
						.getBaseLoad(), testPhase.getRandomLoad(), testPhase
						.getRate(), testPhase, m_args, identity));
			}
		} else if (m_args.getRbetype().equalsIgnoreCase("open")) {
			for (TestPhase testPhase : m_args.getEbs()) {
				identity++;
				m_workers.add(new WorkersOpen(startTime, testPhase
						.getTriggerTime(), testPhase.getStdyTime(), testPhase
						.getBaseLoad(), testPhase.getRandomLoad(), testPhase
						.getRate(), testPhase, m_args, identity));
			}
		} else {
			System.out.println("Error parameter.");
			System.out.println("Start closed as default.");
			for (TestPhase testPhase : m_args.getEbs()) {
				identity++;
				m_workers.add(new WorkersClosed(startTime, testPhase
						.getTriggerTime(), testPhase.getStdyTime(), testPhase
						.getBaseLoad(), testPhase.getRandomLoad(), testPhase
						.getRate(), testPhase, m_args, identity));
			}
		}

		for (Workers worker : m_workers) {
			worker.setDaemon(true);
			worker.start();
		}

		TestInterval = calculateTestInterval();
		long endTime = startTime + TestInterval * 1000L;
		try {
			Thread.sleep(TestInterval * 1000L);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {

			for (Workers worker : m_workers) {
				worker.setStop(true);
			}
			Logger.getLogger().info("Test finished.");

			clear();
		}

	}

}
