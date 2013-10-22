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

public class TestPhase implements Sendable {
	private int baseLoad;
	private int randomLoad;

	private int rate;
	private int stdyTime;
	private int triggerTime;

	public int getBaseLoad() {
		return baseLoad;
	}

	public void setBaseLoad(int base) {
		this.baseLoad = base;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public int getStdyTime() {
		return stdyTime;
	}

	public void setStdyTime(int time) {
		this.stdyTime = time;
	}

	public int getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(int triggerTime) {
		this.triggerTime = triggerTime;
	}

	public int getRandomLoad() {
		return randomLoad;
	}

	public void setRandomLoad(int randomLoad) {
		this.randomLoad = randomLoad;
	}

}
