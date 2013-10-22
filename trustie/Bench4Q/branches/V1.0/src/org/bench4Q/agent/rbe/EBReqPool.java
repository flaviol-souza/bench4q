package org.bench4Q.agent.rbe;

import java.util.ArrayList;

import org.bench4Q.agent.rbe.communication.Args;

public class EBReqPool {

	private ArrayList<EB_ReqOpen> pool;
	private Args m_args;
	private int ebnum;

	public EBReqPool(Args args) {
		pool = new ArrayList<EB_ReqOpen>();
		ebnum = 0;
		m_args = args;
	}

	public void initialize(int ebNum) {
		for (int i = 0; i < ebNum; i++) {
			ebnum++;
			EB_ReqOpen eb = new EB_ReqOpen(this, m_args, ebnum);
			eb.setDaemon(true);
			eb.start();
			pool.add(eb);
		}

	}

	public void addEB(int ebNum) {
		for (int i = 0; i < ebNum; i++) {
			ebnum++;
			EB_ReqOpen eb = new EB_ReqOpen(this, m_args, ebnum);
			eb.setDaemon(true);
			eb.start();
			pool.add(eb);
		}
	}

	public EB_ReqOpen getEB() {
		if (pool.isEmpty()) {
			addEB(10);
		}
		EB_ReqOpen eb = null;
		while (eb == null) {
			if (!pool.isEmpty()) {
				eb = pool.get(0);
				pool.remove(0);
			} else {
				addEB(10);
			}

		}
		return eb;
	}

	public void comeback(EB_ReqOpen eb) {
		pool.add(eb);
	}
	
	public void clean() {
		for(EB_ReqOpen eb : pool){
			eb.stop();
		}
		pool = null;
	}

}
