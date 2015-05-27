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
/**
 * 2009-9-7
 * author: duanzhiquan
 * Technology Center for Software Engineering
 * Institute of Software, Chinese Academy of Sciences
 * Beijing 100190, China 
 * Email:duanzhiquan07@otcaix.iscas.ac.cn
 * 
 * 
 */
package org.bench4Q.agent.rbe;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.TestPhase;

/**
 * @author duanzhiquan
 * 
 */
public abstract class Workers extends Thread {

	public long m_startTime;
	public long m_triggerTime;
	public long m_experimetTime;
	public int m_baseLoad;
	public int m_randomLoad;
	public int m_rate;
	public TestPhase m_testPhase;
	public Args m_args;
	public ArrayList<ArrayList<Integer>> trace;
	public int m_identity;
	private boolean stop = false;
	public ArrayList<EB> m_ebs = new ArrayList<EB>();

	public Workers(long startTime, long triggerTime, long experimentTime,
			int baseLoad, int randomLoad, int rate, TestPhase testPhase,
			Args args, int identity) {
		this.m_startTime = startTime;
		this.m_triggerTime = triggerTime;
		this.m_experimetTime = experimentTime;
		this.m_baseLoad = baseLoad;
		this.m_randomLoad = randomLoad;
		this.m_rate = rate;
		this.m_args = args;
		this.m_identity = identity;

		m_testPhase = testPhase;
	}

	public void run() {
		long w = 0L;
		for (;;) {
			w = this.m_startTime + this.m_triggerTime * 1000L - System.currentTimeMillis();
			if (w < 0L) {
				break;
			}
			try {
				Thread.sleep(w);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		StartEB();

		if ((!this.m_args.isReplay()) && (this.m_args.isRecord())) {
			try {
				FileOutputStream fs = new FileOutputStream(
						this.m_args.getTime() + "-" + this.m_identity);
				ObjectOutputStream op = new ObjectOutputStream(fs);
				op.writeObject(this.trace);
				op.close();
				fs.flush();
				fs.close();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		clean();
	}

	private void clean() {
		this.m_ebs = null;
	}

	abstract void StartEB();

	public boolean isStop() {
		return this.stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

}
