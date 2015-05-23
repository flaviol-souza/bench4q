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
import java.util.List;

/**
 * @author duanzhiquan
 * 
 */
public class Args implements Sendable {

	private static final long serialVersionUID = 5555852443194024065L;
	private String testName;
	private String testDescription;
	private String rbetype;
	private double interval;
	private String lbHost;
	private int lbPort;
	private int nvms;
	private double downStep;
	private double upStep;
	private int prepair;
	private int cooldown;
	private String out;
	private String mix;
	private double slow;
	private boolean getImage;
	private String baseURL;
	private String DBURL;
	private int WebPort;
	private int DBPort;
	private boolean UseEJB;
	private double p_s_to_l;
	private double p_l_to_s;
	private double lambda_short;
	private double lambda_long;
	private double rate;

	private boolean MoniWeb;
	private boolean MoniDB;

	private double tolerance;
	private int retry;
	private double thinktime;
	private boolean ttMMPP;
	private boolean replay;
	private boolean record;
	private String time;

	private ArrayList<TestPhase> testPhase;

	private String typeFrenquency;
	private boolean tfOption;
	private boolean disturbanceOption;
	
	private int addLoad;
	private int addLoadOpt;
	private String hyperHost;
	private int hyperPort;
	private String JSONCommand;
	private double timeToSendCommand;

	private List<Double> intervalMulti;
	/**
	 * constructor
	 */
	public Args() {
		this.testPhase = new ArrayList<TestPhase>();
		// initiate the argument
		this.rbetype = "closed";
		this.interval = 1.0D;
		this.nvms = 0;
		this.downStep = 0.5D;
		this.upStep = 0.75D;
		this.prepair = 600;
		this.cooldown = 300;
		this.out = "out";
		this.mix = "shopping";
		this.slow = 1.0D;
		this.getImage = true;
		this.tolerance = 1.0D;
		this.thinktime = 1.0D;
		this.tfOption = false;
		this.disturbanceOption = false;
		this.timeToSendCommand = 0.4D;
		this.hyperHost = "lserver";
		this.hyperPort = 5000;
		this.JSONCommand = "{\"control\": {\"kparams\": {\"ki\":\"-0.01\",\"kp\": \"0.0\", \"kd\": \"0.0\"}, \"enable\": \"1\", \"ref\": \"45\"}, \"res\": \"yes\", \"req\": \"update\"}";
		this.ttMMPP = true; // with stagger
		this.baseURL = "http://localhost:8080/jaspte";
	}
	
		
	public int getAddLoad() {
		return addLoad;
	}


	public void setAddLoad(int addLoad) {
		this.addLoad = addLoad;
	}


	public int getAddLoadOpt() {
		return addLoadOpt;
	}


	public void setAddLoadOpt(int addLoadOpt) {
		this.addLoadOpt = addLoadOpt;
	}


	/**
	 * @return rbe type
	 */
	public String getRbetype() {
		return this.rbetype;
	}

	/**
	 * @param rbetype
	 */
	public void setRbetype(String rbetype) {
		this.rbetype = rbetype;
	}

	/**
	 * @return prepair
	 */
	public int getPrepair() {
		return this.prepair;
	}

	/**
	 * @param prepair
	 */
	public void setPrepair(int prepair) {
		this.prepair = prepair;
	}

	/**
	 * @return cooldown
	 */
	public int getCooldown() {
		return this.cooldown;
	}

	/**
	 * @param cooldown
	 */
	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	/**
	 * @return out name
	 */
	public String getOut() {
		return this.out;
	}

	/**
	 * @param out
	 */
	public void setOut(String out) {
		this.out = out;
	}

	/**
	 * @return mix
	 */
	public String getMix() {
		return this.mix;
	}

	/**
	 * @param mix
	 */
	public void setMix(String mix) {
		this.mix = mix;
	}

	/**int
	 * @return slow rate
	 */
	public double getSlow() {
		return this.slow;
	}

	/**
	 * @param slow
	 */
	public void setSlow(double slow) {
		this.slow = slow;
	}

	/**
	 * @return base URL
	 */
	public String getBaseURL() {
		return this.baseURL;
	}

	/**
	 * @param baseURL
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * @return tolerance time
	 */
	public double getTolerance() {
		return this.tolerance;
	}

	/**
	 * @param tolerance
	 */
	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * @return think time
	 */
	public double getThinktime() {
		return this.thinktime;
	}

	/**
	 * @param thinktime
	 */
	public void setThinktime(double thinktime) {
		this.thinktime = thinktime;
	}

	public boolean isTtMMPP() {
		return this.ttMMPP;
	}

	public void setTtMMPP(boolean ttMMPP) {
		this.ttMMPP = ttMMPP;
	}

	/**
	 * @return whether get Image
	 */
	public boolean isGetImage() {
		return this.getImage;
	}

	/**
	 * @param getImage
	 */
	public void setGetImage(boolean getImage) {
		this.getImage = getImage;
	}

	/**
	 * @return retry time
	 */
	public int getRetry() {
		return this.retry;
	}

	/**
	 * @param retry
	 */
	public void setRetry(int retry) {
		this.retry = retry;
	}

	/**
	 * @return testPhase
	 */
	public ArrayList<TestPhase> getEbs() {
		return this.testPhase;
	}

	/**
	 * @param testPhase
	 */
	public void setEbs(ArrayList<TestPhase> testPhase) {
		this.testPhase = testPhase;
	}

	/**
	 * @param testPhase
	 */
	public void addEB(TestPhase testPhase) {
		this.testPhase.add(testPhase);
	}

	/**
	 * @param index
	 */
	public void deleteEB(int index) {
		this.testPhase.remove(index);
	}

	/**
	 * @return interval
	 */
	public double getInterval() {
		return this.interval;
	}

	/**
	 * @param interval
	 */
	public void setInterval(double interval) {
		this.interval = interval;
	}

	
	public double getDownStep() {
		return downStep;
	}


	public void setDownStep(double downStep) {
		this.downStep = downStep;
	}

	public double getUpStep() {
		return upStep;
	}


	public void setUpStep(double upStep) {
		this.upStep = upStep;
	}


	/**
	 * @return number of machines
	 */
	public int getNvms() {
		return this.nvms;
	}

	/**
	 * @param number of machines
	 */
	public void setNvms(int nvms) {
		this.nvms = nvms;
	}

	/**
	 * @return number of machines
	 */
	public String getLbHost() {
		return this.lbHost;
	}

	/**
	 * @param number of machines
	 */
	public void setLbHost(String host) {
		this.lbHost = host;
	}
	
	/**
	 * @return number of machines
	 */
	public int getLbPort() {
		return this.lbPort;
	}

	/**
	 * @param number of machines
	 */
	public void setLbPort(int port) {
		this.lbPort = port;
	}
	/**
	 * @return test name
	 */
	public String getTestName() {
		return this.testName;
	}

	/**
	 * @param testName
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}

	/**
	 * @return test description
	 */
	public String getTestDescription() {
		return this.testDescription;
	}

	/**
	 * @param testDescription
	 */
	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

	/**
	 * @return
	 */
	public boolean isReplay() {
		return this.replay;
	}

	public String getDBURL() {
		return this.DBURL;
	}

	/**
	 * @param replay
	 */
	public void setReplay(boolean replay) {
		this.replay = replay;
	}

	public void setDBURL(String dBURL) {
		this.DBURL = dBURL;
	}

	/**
	 * @return
	 */
	public int getWebPort() {
		return this.WebPort;
	}

	/**
	 * @param webPort
	 */
	public void setWebPort(int webPort) {
		this.WebPort = webPort;
	}

	/**
	 * @return
	 */
	public int getDBPort() {
		return this.DBPort;
	}

	/**
	 * @param dBPort
	 */
	public void setDBPort(int dBPort) {
		this.DBPort = dBPort;
	}

	/**
	 * @return
	 */
	public boolean isUseEJB() {
		return this.UseEJB;
	}

	/**
	 * @param useEJB
	 */
	public void setUseEJB(boolean useEJB) {
		this.UseEJB = useEJB;
	}

	/**
	 * @return
	 */
	public boolean isMoniWeb() {
		return this.MoniWeb;
	}

	/**
	 * @param moniWeb
	 */
	public void setMoniWeb(boolean moniWeb) {
		this.MoniWeb = moniWeb;
	}

	/**
	 * @return
	 */
	public boolean isMoniDB() {
		return this.MoniDB;
	}

	/**
	 * @param moniDB
	 */
	public void setMoniDB(boolean moniDB) {
		this.MoniDB = moniDB;
	}

	/**
	 * @return
	 */
	public boolean isRecord() {
		return this.record;
	}

	/**
	 * @param record
	 */
	public void setRecord(boolean record) {
		this.record = record;
	}

	/**
	 * @return
	 */
	public String getTime() {
		return this.time;
	}

	/**
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	public double getP_s_to_l() {
		return this.p_s_to_l;
	}

	public void setP_s_to_l(double pSToL) {
		this.p_s_to_l = pSToL;
	}

	public double getP_l_to_s() {
		return this.p_l_to_s;
	}

	public void setP_l_to_s(double pLToS) {
		this.p_l_to_s = pLToS;
	}

	public double getLambda_short() {
		return this.lambda_short;
	}

	public void setLambda_short(double lambdaShort) {
		this.lambda_short = lambdaShort;
	}

	public double getLambda_long() {
		return this.lambda_long;
	}

	public void setLambda_long(double lambdaLong) {
		this.lambda_long = lambdaLong;
	}

	public double getRate() {
		return this.rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getTypeFrenquency() {
		return this.typeFrenquency;
	}

	public void setTypeFrenquency(String typeFrenquency) {
		this.typeFrenquency = typeFrenquency;
	}

	public ArrayList<TestPhase> getTestPhase() {
		return testPhase;
	}

	public void setTestPhase(ArrayList<TestPhase> testPhase) {
		this.testPhase = testPhase;
	}

	/**
	 * @return the intervalMulti
	 */
	public List<Double> getIntervalMulti() {
		return intervalMulti;
	}

	/**
	 * @param intervalMulti
	 *            the intervalMulti to set
	 */
	public void setIntervalMulti(List<Double> intervalMulti) {
		this.intervalMulti = intervalMulti;
	}

	public boolean getTfOption() {
		return tfOption;
	}

	public void setTfOption(boolean tfOption) {
		this.tfOption = tfOption;
	}


	public boolean getDisturbanceOption() {
		return this.disturbanceOption;
	}
	
	public void setDisturbanceOption(boolean option){
		this.disturbanceOption = option;
	}

	public String getHyperHost() {
		return this.hyperHost;
	}

	public int getHyperPort() {
		return this.hyperPort;
	}

	public String getJSONCommand() {
		return this.JSONCommand;
	}
	
	public void setJSONCommand(String jSONCommand) {
		JSONCommand = jSONCommand;
	}

	public void setHyperHost(String hyperHost) {
		this.hyperHost = hyperHost;
	}


	public void setHyperPort(int hyperPort) {
		this.hyperPort = hyperPort;
	}

	public double getTimeToSendCommand() {
		return this.timeToSendCommand;
	}


	public void setTimeToSendCommand(double timeToSendCommand) {
		this.timeToSendCommand = timeToSendCommand;
	}
	

}
