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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.agent.rbe.communication.TypeFrequency;
import org.bench4Q.common.util.Logger;

/**
 * @author duanzhiquan
 * 
 */
public class WorkersClosed extends Workers {

	private ArrayList<EB> ebs;

	public WorkersClosed(long startTime, long triggerTime, long stdyTime,
			int baseLoad, int randomLoad, int rate, TestPhase testPhase,
			Args args, int identity) {
		super(startTime, triggerTime, stdyTime, baseLoad, randomLoad, rate,
				testPhase, args, identity);
		this.trace = new ArrayList<ArrayList<Integer>>();
		if (this.m_args.isReplay()) {
			try {
				FileInputStream fi = new FileInputStream(this.m_args.getTime()+ "-" + identity);
				ObjectInputStream ois = new ObjectInputStream(fi);
				this.trace = ((ArrayList<ArrayList<Integer>>) ois.readObject());
				ois.close();
				fi.close();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (IOException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}

		}
		this.ebs = new ArrayList<EB>();

		// initialize the pool
		for (int j = 0; j < baseLoad; j++) {
			ArrayList<Integer> tra = new ArrayList<Integer>();
			if (this.m_args.isReplay()) {
				tra = (ArrayList<Integer>)this.trace.get(j);
				Logger.getLogger().debug(j+"\t"+tra.toArray().toString());
				EB eb = new EBClosed(this.m_args, tra);
				eb.setDaemon(true);
				// if(j > baseLoad / 2)
				// eb.joke = true;
				eb.start();
				this.ebs.add(eb);
				
			} else {
				EB eb = new EBClosed(this.m_args, tra);
				eb.setPropertiesEB(new PropertiesEB());
				eb.setDaemon(true);
				eb.start();
				Logger.getLogger().debug(j);
				this.ebs.add(eb);
				this.trace.add(tra);
			}

		}
	}

	void StartEB() {
		int n = 0;
		long beginTime = System.currentTimeMillis();
		long endTime = beginTime + this.m_stdyTime * 1000L;

		int baseLoad = this.m_baseLoad;

		TypeFrequency type = TypeFrequency.getType(m_args.getTypeFrenquency());
		

		int index = 0;
		for(EB eb : ebs){
			PropertiesEB propertiesEB = 
					FrequencySettings.createProperties(index++, m_testPhase, type, beginTime);
			eb.setPropertiesEB(propertiesEB);
			((EBClosed)eb).setTest(true);
		}

		while ((!isStop()) && (System.currentTimeMillis() - endTime < 0L)) {
			try {
				Thread.sleep(500L);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		for (EB eb : this.ebs) {
			//((EBClosed) eb).setTerminate(true);
			((EBClosed) eb).setTest(false);
		}
		this.ebs = null;

	}

}
