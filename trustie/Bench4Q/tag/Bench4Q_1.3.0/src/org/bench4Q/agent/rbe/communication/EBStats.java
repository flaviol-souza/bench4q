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
package org.bench4Q.agent.rbe.communication;

import java.util.ArrayList;

/**
 * @author duanzhiquan
 * 
 */
public class EBStats implements Sendable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private volatile static EBStats uniqueInstance;

	// used to record test info
	private int[] loadStart;
	private long startTime;
	private long prepairTime;
	private long testTime;
	private long endTime;
	private int m_testduring;

	// used to record request info
	private int[][] trans = new int[15][15];
	private int[][] webInteractionThroughput;
	private ResultSet[] wirt = new ResultSet[15];
	private ResultSet[] tt = new ResultSet[15];
	private long WIPS = 0;
	private long WIRT = 0;

	// used to record session info
	private int[][] session;
	private ArrayList<Integer> sessionLen = new ArrayList<Integer>();

	private int OrderedSession;
	private int[] ErrorSession = new int[15];

	private double TotalProfit = 0;
	private int errorCnt = 0;
	private ErrorSet[] errors = new ErrorSet[15];

	private EBStats() {
		for (int i = 0; i < 15; i++) {
			wirt[i] = new ResultSet();
			tt[i] = new ResultSet();
			errors[i] = new ErrorSet();
		}
	}

	/**
	 * @param startTime
	 *            test start time.
	 * @param prepairTime
	 *            test prepair time.
	 * @param testInterval
	 *            test time.
	 * @param cooldown
	 *            test cool down time.
	 */
	public void init(long startTime, long prepairTime, long testInterval,
			long cooldown) {
		this.startTime = startTime;
		this.prepairTime = prepairTime * 1000 + startTime;
		this.testTime = this.prepairTime + testInterval * 1000;
		this.endTime = testTime + cooldown * 1000;

		m_testduring = (int) testInterval;

		webInteractionThroughput = new int[15][(int) (testInterval + 1)];
		loadStart = new int[(int) (testInterval + 1)];

		session = new int[2][(int) (testInterval + 1)];
	}

	/**
	 * @return the unique instance of EBStats
	 */
	public static EBStats getEBStats() {
		if (uniqueInstance == null) {
			synchronized (EBStats.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new EBStats();
				}
			}
		}
		return uniqueInstance;
	}

	/**
	 * used to clean recorded infos when another test starts.
	 */
	public static void cleaner() {
		uniqueInstance = null;
	}

	/**
	 * @param cur
	 *            transition from.
	 * @param next
	 *            transition to.
	 */
	public final synchronized void transition(int cur, int next) {
		if (System.currentTimeMillis() < prepairTime)
			return;
		trans[cur][next]++;
	}

	/**
	 * record request information.
	 * 
	 * @param state
	 * @param wirt_t1
	 * @param wirt_t2
	 * @param itt
	 */
	public synchronized void interaction(int state, long wirt_t1, long wirt_t2,
			long itt) {
		int b;
		if ((wirt_t2 >= prepairTime) && (wirt_t2 <= testTime)) {
			b = (int) ((wirt_t2 - prepairTime) / 1000L);
			webInteractionThroughput[state][b]++;
			b = (int) ((wirt_t1 - prepairTime) / 1000L);
			if (b >= 0) {
				loadStart[b]++;
			}
			Long tem = wirt_t2 - wirt_t1;
			wirt[state].addResult(new Double(tem));
			tt[state].addResult(new Double(itt));
		}

	}

	/**
	 * record session information. session[0][] records the session strated at
	 * that time. session[1][] records the session ended at that time.
	 * 
	 * @param start
	 *            session start time.
	 * @param end
	 *            session end time.
	 * @param len
	 *            session length.
	 * @param ordered
	 *            whether session ordered.
	 */
	public synchronized void sessionRecorder(long start, long end, int len,
			boolean ordered) {
		int b;

		b = (int) ((start - prepairTime) / 1000L);

		if ((b >= 0) && (b <= m_testduring)) {
			session[0][b]++;
		}

		b = (int) ((end - prepairTime) / 1000L);

		if ((b >= 0) && (b <= m_testduring)) {
			session[1][b]++;
			sessionLen.add(len);
			if (ordered) {
				OrderedSession++;
			}
		}

	}

	/**
	 * @param profit
	 */
	public synchronized void Profit(double profit) {
		TotalProfit += profit;
	}

	/**
	 * @param servlet
	 * @param message
	 * @param url
	 */
	public void error(int servlet, String message, String url) {
		EBError error = new EBError(message, url);
		errors[servlet].add(error);
		errorCnt++;
	}

	/**
	 * @param state
	 */
	public void addErrorSession(int state) {
		ErrorSession[state]++;
	}

	/**
	 * @return
	 */
	public long getWIPS() {
		return WIPS;
	}

	/**
	 * @return
	 */
	public long getWIRT() {
		return WIRT;
	}

	/**
	 * @return
	 */
	public int[] getErrorSession() {
		return ErrorSession;
	}

	/**
	 * @return
	 */
	public int getErrorCnt() {
		return errorCnt;
	}

	/**
	 * @return
	 */
	public ErrorSet[] getErrors() {
		return errors;
	}

	/**
	 * @return
	 */
	public int[][] getWebInteractionThroughput() {
		return webInteractionThroughput;
	}

	/**
	 * @return
	 */
	public ResultSet[] getWirt() {
		return wirt;
	}

	/**
	 * @return
	 */
	public int getTestduring() {
		return m_testduring;
	}

	/**
	 * @param testduring
	 */
	public void setTestduring(int testduring) {
		m_testduring = testduring;
	}

	/**
	 * @return
	 */
	public double getTotalProfit() {
		return TotalProfit;
	}

	/**
	 * @return
	 */
	public int[][] getTrans() {
		return trans;
	}

	/**
	 * @return
	 */
	public int[] getLoadStart() {
		return loadStart;
	}

	/**
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return
	 */
	public long getPrepairTime() {
		return prepairTime;
	}

	/**
	 * @param prepairTime
	 */
	public void setPrepairTime(long prepairTime) {
		this.prepairTime = prepairTime;
	}

	/**
	 * @return
	 */
	public long getTestTime() {
		return testTime;
	}

	/**
	 * @param testTime
	 */
	public void setTestTime(long testTime) {
		this.testTime = testTime;
	}

	/**
	 * @return
	 */
	public long getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 */
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return
	 */
	public int getM_testduring() {
		return m_testduring;
	}

	/**
	 * @param m_testduring
	 */
	public void setM_testduring(int m_testduring) {
		this.m_testduring = m_testduring;
	}

	/**
	 * @return
	 */
	public ResultSet[] getTt() {
		return tt;
	}

	/**
	 * @return
	 */
	public int[][] getSession() {
		return session;
	}

	/**
	 * @return
	 */
	public ArrayList<Integer> getSessionLen() {
		return sessionLen;
	}

	/**
	 * @return
	 */
	public int getOrderedSession() {
		return OrderedSession;
	}

	/**
	 * @param loadStart
	 */
	public void setLoadStart(int[] loadStart) {
		this.loadStart = loadStart;
	}

}
