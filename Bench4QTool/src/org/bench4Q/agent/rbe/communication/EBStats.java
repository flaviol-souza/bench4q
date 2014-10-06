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

import org.bench4Q.common.util.Logger;

/**
 * @author duanzhiquan
 * 
 */
public class EBStats implements Sendable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static volatile EBStats uniqueInstance;

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
	private long WIPS = 0L;
	private long WIRT = 0L;

	// used to record session info
	private int[][] session;
	private ArrayList<Integer> sessionLen = new ArrayList<Integer>();

	private int OrderedSession;
	private int[] ErrorSession = new int[15];

	private double TotalProfit = 0.0D;
	private int errorCnt = 0;
	private ErrorSet[] errors = new ErrorSet[15];

	private String title;

	private boolean selected = false;

	// QoS record info
	// author: wangsa
	private int[][] webInteractionThroughput_vip;
	private int[][] webInteractionThroughput_norm;
	private ResultSet[] wirt_vip = new ResultSet[15];
	private ResultSet[] wirt_norm = new ResultSet[15];
	private ResultSet[] tt_vip = new ResultSet[15];
	private ResultSet[] tt_norm = new ResultSet[15];
	private int[][] session_vip;
	private int[][] session_norm;
	private ArrayList<Integer> sessionLen_vip = new ArrayList<Integer>();
	private ArrayList<Integer> sessionLen_norm = new ArrayList<Integer>();

	private int OrderedSession_vip;
	private int OrderedSession_norm;

	private int[] ErrorSession_vip = new int[15];
	private int[] ErrorSession_norm = new int[15];

	private ErrorSet[] errors_vip = new ErrorSet[15];
	private ErrorSet[] errors_norm = new ErrorSet[15];

	private int errorCnt_vip = 0;
	private int errorCnt_norm = 0;
	private double VIPrate;

	private EBStats() {
		for (int i = 0; i < 15; i++) {
			this.wirt[i] = new ResultSet();
			this.wirt_norm[i] = new ResultSet();
			this.wirt_vip[i] = new ResultSet();

			this.tt[i] = new ResultSet();
			this.tt_norm[i] = new ResultSet();
			this.tt_vip[i] = new ResultSet();

			this.errors[i] = new ErrorSet();
			this.errors_norm[i] = new ErrorSet();
			this.errors_vip[i] = new ErrorSet();
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
		this.prepairTime = (prepairTime * 1000L + startTime);
		this.testTime = (this.prepairTime + testInterval * 1000L);
		this.endTime = (this.testTime + cooldown * 1000L);

		this.m_testduring = ((int) testInterval);

		this.webInteractionThroughput = new int[15][(int) (testInterval + 1L)];

		this.webInteractionThroughput_vip = new int[15][(int) (testInterval + 1L)];
		this.webInteractionThroughput_norm = new int[15][(int) (testInterval + 1L)];

		this.loadStart = new int[(int) (testInterval + 1L)];
		this.session = new int[2][(int) (testInterval + 1L)];

		this.session_vip = new int[2][(int) (testInterval + 1L)];
		this.session_norm = new int[2][(int) (testInterval + 1L)];
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
		if (System.currentTimeMillis() < this.prepairTime) {
			return;
		}
		this.trans[cur][next] += 1;
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
			long itt, boolean isVIP) {
		int b;
		if ((wirt_t2 >= this.prepairTime) && (wirt_t2 <= this.testTime)) {
			b = (int) ((wirt_t2 - this.prepairTime) / 1000L);
			this.webInteractionThroughput[state][b]++;

			if (isVIP) {
				this.webInteractionThroughput_vip[state][b]++;
			} else {
				this.webInteractionThroughput_norm[state][b]++;
			}

			b = (int) ((wirt_t1 - this.prepairTime) / 1000L);
			if (b >= 0) {
				this.loadStart[b]++;
			}
			Long tem = Long.valueOf(wirt_t2 - wirt_t1);
			this.wirt[state].addResult(new Double(tem));
			this.tt[state].addResult(new Double(itt));
			if (isVIP) {
				this.wirt_vip[state].addResult(new Double(tem));
				this.tt_vip[state].addResult(new Double(itt));
			} else {
				this.wirt_norm[state].addResult(new Double(tem));
				this.tt_norm[state].addResult(new Double(itt));
			}

		}

	}

	/**
	 * record session information. session[0][] records the session started at
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
			boolean ordered, boolean isVIP) {
		int b = (int) ((start - this.prepairTime) / 1000L);

		if ((b >= 0) && (b <= this.m_testduring)) {
			this.session[0][b]++;
			if (isVIP){
				this.session_vip[0][b]++;
			} else {
				this.session_norm[0][b]++;
			}
		}

		b = (int) ((end - this.prepairTime) / 1000L);

		if ((b >= 0) && (b <= this.m_testduring)) {
			this.session[1][b] ++;
			this.sessionLen.add(Integer.valueOf(len));
			if (isVIP) {
				this.session_vip[1][b] ++;
				this.sessionLen_vip.add(Integer.valueOf(len));
			} else {
				this.session_norm[1][b] ++;
				this.sessionLen_norm.add(Integer.valueOf(len));
			}
			if (ordered) {
				this.OrderedSession ++;
				if (isVIP) {
					this.OrderedSession_vip ++;
				} else {
					this.OrderedSession_norm ++;
				}
			}
		}

	}

	/**
	 * @param profit
	 */
	public synchronized void Profit(double profit) {
		this.TotalProfit += profit;
	}

	/**
	 * @param servlet
	 * @param message
	 * @param url
	 */
	public void error(int servlet, String message, String url, boolean isVIP) {
		EBError error = new EBError(message, url);
		this.errors[servlet].add(error);
		this.errorCnt ++;
		if (isVIP) {
			this.errors_vip[servlet].add(error);
			this.errorCnt_vip ++;
		} else {
			this.errors_norm[servlet].add(error);
			this.errorCnt_norm ++;
		}
	}

	/**
	 * @param state
	 */
	public void addErrorSession(int state, boolean isVIP) {
		this.ErrorSession[state] ++;
		if (isVIP) {
			this.ErrorSession_vip[state] ++;
		} else {
			this.ErrorSession_norm[state] ++;
		}
	}

	/**
	 * @return
	 */
	public long getWIPS() {
		return this.WIPS;
	}

	/**
	 * @return
	 */
	public long getWIRT() {
		return this.WIRT;
	}

	/**
	 * @return
	 */
	public int[] getErrorSession() {
		return this.ErrorSession;
	}

	/**
	 * @return
	 */
	public int getErrorCnt() {
		return this.errorCnt;
	}

	/**
	 * @return
	 */
	public ErrorSet[] getErrors() {
		return this.errors;
	}

	/**
	 * @return
	 */
	public int[][] getWebInteractionThroughput() {
		return this.webInteractionThroughput;
	}

	/**
	 * @return
	 */
	public ResultSet[] getWirt() {
		return this.wirt;
	}

	/**
	 * @return
	 */
	public int getTestduring() {
		return this.m_testduring;
	}

	/**
	 * @param testduring
	 */
	public void setTestduring(int testduring) {
		this.m_testduring = testduring;
	}

	/**
	 * @return
	 */
	public double getTotalProfit() {
		return this.TotalProfit;
	}

	/**
	 * @return
	 */
	public int[][] getTrans() {
		return this.trans;
	}

	/**
	 * @return
	 */
	public int[] getLoadStart() {
		return this.loadStart;
	}

	/**
	 * @return
	 */
	public long getStartTime() {
		return this.startTime;
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
		return this.prepairTime;
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
		return this.testTime;
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
		return this.endTime;
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
		return this.m_testduring;
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
		return this.tt;
	}

	/**
	 * @return
	 */
	public int[][] getSession() {
		return this.session;
	}

	/**
	 * @return
	 */
	public ArrayList<Integer> getSessionLen() {
		return this.sessionLen;
	}

	/**
	 * @return
	 */
	public int getOrderedSession() {
		return this.OrderedSession;
	}

	/**
	 * @param loadStart
	 */
	public void setLoadStart(int[] loadStart) {
		this.loadStart = loadStart;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int[][] getWebInteractionThroughput_vip() {
		return this.webInteractionThroughput_vip;
	}

	public void setWebInteractionThroughput_vip(
			int[][] webInteractionThroughputVip) {
		this.webInteractionThroughput_vip = webInteractionThroughputVip;
	}

	public int[][] getWebInteractionThroughput_norm() {
		return this.webInteractionThroughput_norm;
	}

	public void setWebInteractionThroughput_norm(
			int[][] webInteractionThroughputNorm) {
		this.webInteractionThroughput_norm = webInteractionThroughputNorm;
	}

	public ResultSet[] getWirt_vip() {
		return this.wirt_vip;
	}

	public void setWirt_vip(ResultSet[] wirtVip) {
		this.wirt_vip = wirtVip;
	}

	public ResultSet[] getWirt_norm() {
		return this.wirt_norm;
	}

	public void setWirt_norm(ResultSet[] wirtNorm) {
		this.wirt_norm = wirtNorm;
	}

	public ResultSet[] getTt_vip() {
		return this.tt_vip;
	}

	public void setTt_vip(ResultSet[] ttVip) {
		this.tt_vip = ttVip;
	}

	public ResultSet[] getTt_norm() {
		return this.tt_norm;
	}

	public void setTt_norm(ResultSet[] ttNorm) {
		this.tt_norm = ttNorm;
	}

	public int[][] getSession_vip() {
		return this.session_vip;
	}

	public void setSession_vip(int[][] sessionVip) {
		this.session_vip = sessionVip;
	}

	public int[][] getSession_norm() {
		return this.session_norm;
	}

	public void setSession_norm(int[][] sessionNorm) {
		this.session_norm = sessionNorm;
	}

	public ArrayList<Integer> getSessionLen_vip() {
		return this.sessionLen_vip;
	}

	public void setSessionLen_vip(ArrayList<Integer> sessionLenVip) {
		this.sessionLen_vip = sessionLenVip;
	}

	public ArrayList<Integer> getSessionLen_norm() {
		return this.sessionLen_norm;
	}

	public void setSessionLen_norm(ArrayList<Integer> sessionLenNorm) {
		this.sessionLen_norm = sessionLenNorm;
	}

	public int getOrderedSession_vip() {
		return this.OrderedSession_vip;
	}

	public void setOrderedSession_vip(int orderedSessionVip) {
		this.OrderedSession_vip = orderedSessionVip;
	}

	public int getOrderedSession_norm() {
		return this.OrderedSession_norm;
	}

	public void setOrderedSession_norm(int orderedSessionNorm) {
		this.OrderedSession_norm = orderedSessionNorm;
	}

	public int[] getErrorSession_vip() {
		return this.ErrorSession_vip;
	}

	public void setErrorSession_vip(int[] errorSessionVip) {
		this.ErrorSession_vip = errorSessionVip;
	}

	public int[] getErrorSession_norm() {
		return this.ErrorSession_norm;
	}

	public void setErrorSession_norm(int[] errorSessionNorm) {
		this.ErrorSession_norm = errorSessionNorm;
	}

	public ErrorSet[] getErrors_vip() {
		return this.errors_vip;
	}

	public void setErrors_vip(ErrorSet[] errorsVip) {
		this.errors_vip = errorsVip;
	}

	public ErrorSet[] getErrors_norm() {
		return this.errors_norm;
	}

	public void setErrors_norm(ErrorSet[] errorsNorm) {
		this.errors_norm = errorsNorm;
	}

	public int getErrorCnt_vip() {
		return this.errorCnt_vip;
	}

	public void setErrorCnt_vip(int errorCntVip) {
		this.errorCnt_vip = errorCntVip;
	}

	public int getErrorCnt_norm() {
		return this.errorCnt_norm;
	}

	public void setErrorCnt_norm(int errorCntNorm) {
		this.errorCnt_norm = errorCntNorm;
	}

	public double getVIPrate() {
		return this.VIPrate;
	}

	public void setVIPrate(double vIPrate) {
		this.VIPrate = vIPrate;
	}

}
