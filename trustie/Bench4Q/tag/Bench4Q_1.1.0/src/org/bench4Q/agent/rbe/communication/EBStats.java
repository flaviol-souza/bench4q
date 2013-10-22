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
package org.bench4Q.agent.rbe.communication;

import java.util.ArrayList;

public class EBStats implements Sendable {
	private volatile static EBStats uniqueInstance;

	private int[][] webInteractionThroughput;
	private int[] loadStart;

	private ResultSet[] wirt;
	private ResultSet[] tt;
	private ArrayList<Long> session;

	private long startTime; // When to start test
	private long prepairTime;
	private long testTime;
	private long endTime;

	private int CompleteSession;
	private int[] ErrorSession;
	private int errorCnt;
	private long WIPS;
	private long WIRT;
	private int[][] trans;
	private double TotalProfit;

	private int m_testduring;

	private ErrorSet[] errors;

	private EBStats() {
		trans = new int[15][15];

		wirt = new ResultSet[15];
		tt = new ResultSet[15];
		errors = new ErrorSet[15];
		for (int i = 0; i < 15; i++) {
			wirt[i] = new ResultSet();
			tt[i] = new ResultSet();
			errors[i] = new ErrorSet();
		}
		session = new ArrayList<Long>(60);
		CompleteSession = 0;
		ErrorSession = new int[15];
		errorCnt = 0;
		WIPS = 0;
		WIRT = 0;
		TotalProfit = 0;
	}

	public void init(long startTime, long prepairTime, long testInterval, long cooldown) {
		this.startTime = startTime;
		this.prepairTime = prepairTime * 1000 + startTime;
		this.testTime = this.prepairTime + testInterval * 1000;
		this.endTime = testTime + cooldown * 1000;

		m_testduring = (int) testInterval;

		webInteractionThroughput = new int[15][(int) (testInterval + 1)];
		loadStart = new int[(int) (testInterval + 1)];
	}

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

	public final synchronized void transition(int cur, int next) {
		if (System.currentTimeMillis() < prepairTime)
			return;
		trans[cur][next]++;
	}

	public synchronized void interaction(int state, long wirt_t1, long wirt_t2, long itt) {
		int b;
		if ((wirt_t2 >= prepairTime) && (wirt_t2 <= testTime)) {
			b = (int) ((wirt_t2 - prepairTime) / 1000L);
			webInteractionThroughput[state][b]++;
		}
		if ((wirt_t1 >= prepairTime) && (wirt_t2 <= testTime)) {
			b = (int) ((wirt_t1 - prepairTime) / 1000L);
			loadStart[b]++;
		}
		Long tem = wirt_t2 - itt - wirt_t1;
		if (tem > 0) {
			wirt[state].addResult(new Double(tem));
		}
		tt[state].addResult(new Double(itt));
	}

	public synchronized void Profit(double profit) {

		TotalProfit += profit;
	}

	public void error(int servlet, String message, String url) {
		EBError error = new EBError(message, url);
		errors[servlet].add(error);
		errorCnt++;
	}

	public void addCompleteSession() {
		CompleteSession++;
	}

	public void addErrorSession(int state) {
		ErrorSession[state]++;
	}

	public long getWIPS() {
		return WIPS;
	}

	public long getWIRT() {
		return WIRT;
	}

	public int getCompleteSession() {
		return CompleteSession;
	}

	public int[] getErrorSession() {
		return ErrorSession;
	}

	public int getErrorCnt() {
		return errorCnt;
	}

	public ErrorSet[] getErrors() {
		return errors;
	}

	public int[][] getWebInteractionThroughput() {
		return webInteractionThroughput;
	}

	public void setWebInteractionThroughput(int[][] webInteractionThroughput) {
		this.webInteractionThroughput = webInteractionThroughput;
	}

	public ResultSet[] getWirt() {
		return wirt;
	}

	public void setWirt(ResultSet[] wirt) {
		this.wirt = wirt;
	}

	public int getTestduring() {
		return m_testduring;
	}

	public void setTestduring(int testduring) {
		m_testduring = testduring;
	}

	public double getTotalProfit() {
		return TotalProfit;
	}

	public void setTotalProfit(double totalProfit) {
		TotalProfit = totalProfit;
	}

	public int[][] getTrans() {
		return trans;
	}

	public int[] getLoadStart() {
		return loadStart;
	}

}
