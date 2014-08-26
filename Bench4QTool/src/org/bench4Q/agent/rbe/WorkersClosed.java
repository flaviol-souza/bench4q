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

/**
 * @author duanzhiquan
 * 
 */
public class WorkersClosed extends Workers {

	private ArrayList<EB> ebs;
	

	

	/**
	 * @param startTime
	 * @param triggerTime
	 * @param stdyTime
	 * @param baseLoad
	 * @param randomLoad
	 * @param rate
	 * @param args
	 */
	public WorkersClosed(long startTime, long triggerTime, long stdyTime,
			int baseLoad, int randomLoad, int rate, TestPhase testPhase,
			Args args, int identity) {
		super(startTime, triggerTime, stdyTime, baseLoad, randomLoad, rate,
				testPhase, args, identity);
		trace = new ArrayList<ArrayList<Integer>>();
		if (m_args.isReplay()) {
			FileInputStream fi;
			try {
				fi = new FileInputStream(m_args.getTime() + "-" + identity);
				ObjectInputStream ois = new ObjectInputStream(fi);
				trace = (ArrayList<ArrayList<Integer>>) ois.readObject();
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
		ebs = new ArrayList<EB>();

		// initialize the pool
		for (int j = 0; j < baseLoad; j++) {
			ArrayList<Integer> tra = new ArrayList<Integer>();
			if (m_args.isReplay()) {
				tra = trace.get(j);
				EB eb = new EBClosed(m_args, tra);
				eb.setDaemon(true);
				// if(j > baseLoad / 2)
				// eb.joke = true;
				eb.start();
				ebs.add(eb);
			} else {
				EB eb = new EBClosed(m_args, tra);
				eb.setPropertiesEB(new PropertiesEB());
				eb.setDaemon(true);
				eb.start();
				ebs.add(eb);
				trace.add(tra);
			}

		}
	}

	void StartEB() {
		int n = 0;
		long beginTime = System.currentTimeMillis();
		long endTime = beginTime + m_stdyTime * 1000L;

		int baseLoad = m_baseLoad;

		TypeFrequency type = TypeFrequency.getType(m_args.getTypeFrenquency());

		Iterator iterator = ebs.iterator();


		int index = 0;
		while (iterator.hasNext()) {
			EBClosed eb = (EBClosed) iterator.next();
			//
			PropertiesEB propertiesEB = FrequencySettings.createProperties(index++, m_testPhase, type, beginTime);
			eb.setPropertiesEB(propertiesEB);
			eb.setTest(true);	
		}

		while (!isStop() && (System.currentTimeMillis() - endTime) < 0) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

		for (EB eb : ebs) {
			((EBClosed) eb).setTerminate(true);
			// ((EBClosed) eb).stop();
		}
		ebs = null;

	}
	
}
