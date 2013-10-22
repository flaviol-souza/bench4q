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

import java.util.ArrayList;
import java.util.Date;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.communication.TestPhase;

/**
 * @author duanzhiquan
 * 
 */
public class Workers_Closed extends Workers {
	

	private ArrayList<EB> ebs;
	
	public Workers_Closed(long startTime, long triggerTime, long stdyTime, int baseLoad,
			int randomLoad, int rate, Args args) {
		super(startTime, triggerTime, stdyTime, baseLoad, randomLoad, rate, args);
		
		ebs = new ArrayList<EB>();		
		long maxEBNum = 0;
		if (m_rate > 0) {
			maxEBNum = m_baseLoad + m_randomLoad + m_rate * m_stdyTime;
		} else {
			maxEBNum = m_baseLoad + m_randomLoad ;
		}		
		
		// initialize the pool
		for (int j = 0; j < maxEBNum+5; j++) {
			EB eb = new EB_Closed(m_args);
			eb.setDaemon(true);
			eb.start();
			ebs.add(eb);
		}		
	}

	@Override
	void StartEB() {
		
		long beginTime = System.currentTimeMillis();
		long endTime = beginTime + m_stdyTime * 1000L;
		
		int currentLoad = 0;
		int baseLoad = m_baseLoad;
		
		while (((System.currentTimeMillis() - endTime) < 0) && (baseLoad > 0)) {
			int realLoad = baseLoad + m_randomLoad;
			if (realLoad > currentLoad) {
				for (int j = currentLoad; j <= realLoad; j++) {
					((EB_Closed) ebs.get(j)).setTest(true);
				}
			} else if (realLoad < currentLoad) {
				for (int j = currentLoad; j >= realLoad; j--) {
					((EB_Closed) ebs.get(j - 1)).setTest(false);
				}
			}			
			baseLoad += m_rate;
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for(EB eb : ebs){
			((EB_Closed)eb).stop();
		}
		ebs = null;
			
	}
}
