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

public class Args implements Sendable {
	/**
	 * 2009-7-17 author: duanzhiquan Technology Center for Software Engineering
	 * Institute of Software, Chinese Academy of Sciences Beijing 100190, China
	 * Email:duanzhiquan07@otcaix.iscas.ac.cn
	 * 
	 * 
	 */
	private static final long serialVersionUID = 5555852443194024065L;
	private String testName;
	private String testDescription;
	private String rbetype;
	private double interval;
	private int prepair;
	private int cooldown;
	private String out;
	private String mix;
	private double slow;
	private boolean getImage;
	private String baseURL;
	private double tolerance;
	private int retry;
	private double thinktime;
	private int urlConnectionTimeOut;
	private int urlReadTimeOut;
	private ArrayList<TestPhase> testPhase;

	public Args() {
		testPhase = new ArrayList<TestPhase>();
		// initiate the argument
		rbetype = "closed";
		interval = 1;
		prepair = 600;
		cooldown = 300;
		out = "out";
		mix = "shopping";
		slow = 1.0;
		getImage = true;
		tolerance = 1.0;
		thinktime = 1.0;
		baseURL = "http://localhost:8080/jaspte";
		urlConnectionTimeOut = 0;
		urlReadTimeOut = 0;
	}

	public String getRbetype() {
		return rbetype;
	}

	public void setRbetype(String rbetype) {
		this.rbetype = rbetype;
	}

	public int getPrepair() {
		return prepair;
	}

	public void setPrepair(int prepair) {
		this.prepair = prepair;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public String getOut() {
		return out;
	}

	public void setOut(String out) {
		this.out = out;
	}

	public String getMix() {
		return mix;
	}

	public void setMix(String mix) {
		this.mix = mix;
	}

	public double getSlow() {
		return slow;
	}

	public void setSlow(double slow) {
		this.slow = slow;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	public double getThinktime() {
		return thinktime;
	}

	public void setThinktime(double thinktime) {
		this.thinktime = thinktime;
	}

	public boolean isGetImage() {
		return getImage;
	}

	public void setGetImage(boolean getImage) {
		this.getImage = getImage;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public ArrayList<TestPhase> getEbs() {
		return testPhase;
	}

	public void setEbs(ArrayList<TestPhase> testPhase) {
		this.testPhase = testPhase;
	}

	public void addEB(TestPhase testPhase) {
		this.testPhase.add(testPhase);
	}

	public void deleteEB(int index) {
		this.testPhase.remove(index);
	}

	public int getUrlConnectionTimeOut() {
		return urlConnectionTimeOut;
	}

	public void setUrlConnectionTimeOut(int urlConnectionTimeOut) {
		this.urlConnectionTimeOut = urlConnectionTimeOut;
	}

	public int getUrlReadTimeOut() {
		return urlReadTimeOut;
	}

	public void setUrlReadTimeOut(int urlReadTimeOut) {
		this.urlReadTimeOut = urlReadTimeOut;
	}

	public double getInterval() {
		return interval;
	}

	public void setInterval(double interval) {
		this.interval = interval;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestDescription() {
		return testDescription;
	}

	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

}
