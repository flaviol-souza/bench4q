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

import org.bench4Q.agent.rbe.communication.Args;

/**
 * @author duanzhiquan
 * 
 */
public abstract class Workers extends Thread {

	public long m_startTime;
	public long m_triggerTime;
	public long m_stdyTime;
	public int m_baseLoad;
	public int m_randomLoad;
	public int m_rate;
	public Args m_args;

	public ArrayList<EB> m_ebs = new ArrayList<EB>();

	public Workers(long startTime, long triggerTime, long stdyTime, int baseLoad, int randomLoad,
			int rate, Args args) {
		super();
		m_startTime = startTime;
		m_triggerTime = triggerTime;
		m_stdyTime = stdyTime;
		m_baseLoad = baseLoad;
		m_randomLoad = randomLoad;
		m_rate = rate;
		m_args = args;
	}

	public void run() {
		long w = 0;
		while (true) {
			w = m_startTime + m_triggerTime * 1000L - System.currentTimeMillis();
			if (w < 0)
				break;
			try {
				Thread.sleep(w);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		StartEB();

		try {
			Thread.sleep(m_stdyTime * 1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		boolean flag = true;

		while ( flag ) {
			flag = false;
			for (EB eb : m_ebs) {
				if (eb.isAlive()) {
					flag = true;
					eb.stop();
				}
			}
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		clean();
	}

	private void clean() {
		m_ebs = null;
	}

	abstract void StartEB();

}
