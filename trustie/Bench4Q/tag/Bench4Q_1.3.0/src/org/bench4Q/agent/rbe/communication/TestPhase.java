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
	private int stdyTime;
	private int triggerTime;

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
	public int getStdyTime() {
		return stdyTime;
	}

	/**
	 * @param time
	 */
	public void setStdyTime(int time) {
		this.stdyTime = time;
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
