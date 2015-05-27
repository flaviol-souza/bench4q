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

import org.bench4Q.common.util.Logger;

/**
 * @author duanzhiquan
 * 
 */
public class TestPhase implements Sendable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2233988134129070190L;
	private int baseLoad;
	private int randomLoad;

	private int rate;
	private int experimentTime;
	private int triggerTime;

	private TestFrequency frequency;

	public TestPhase() {
		this.frequency = new TestFrequency();
	}
	 
	
	/**
	 * @return the frequency
	 */
	public TestFrequency getFrequency() {
		return frequency;
	}

	/**
	 * @param frequency
	 *            the frequency to set
	 */
	public void setFrequency(TestFrequency frequency) {
		this.frequency = frequency;
		Logger.getLogger().debug(
				"TestPhase:\nduration: "+this.frequency.getDurationTime()+
				"\tduration: "+this.frequency.getStartTime()+
				"\tduration: "+this.frequency.getEndTime()+
				"\tduration: "+this.frequency.getPauseTime()+
				"\tduration: "+this.frequency.getType().getName());
		
	}
	
	/**
	 * @return
	 */
	public int getBaseLoad() {
		return baseLoad;
	}

	/**
	 * @param base
	 */
	public void setBaseLoad(int base) {
		this.baseLoad = base;
	}

	/**
	 * @return
	 */
	public int getRate() {
		return rate;
	}

	/**
	 * @param rate
	 */
	public void setRate(int rate) {
		this.rate = rate;
	}

	/**
	 * @return
	 */
	public int getExperimentTime() {
		return experimentTime;
	}

	/**
	 * @param time
	 */
	public void setExperimentTime(int time) {
		this.experimentTime = time;
	}

	/**
	 * @return
	 */
	public int getTriggerTime() {
		return triggerTime;
	}

	/**
	 * @param triggerTime
	 */
	public void setTriggerTime(int triggerTime) {
		this.triggerTime = triggerTime;
	}

	/**
	 * @return
	 */
	public int getRandomLoad() {
		return randomLoad;
	}

	/**
	 * @param randomLoad
	 */
	public void setRandomLoad(int randomLoad) {
		this.randomLoad = randomLoad;
	}

}
