package org.bench4Q.agent.rbe;

import java.util.ArrayList;
import java.util.Date;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.communication.TestPhase;

public class Workers_ReqOpen extends Workers {

	private EBReqPool m_reqpool;

	public Workers_ReqOpen(long startTime, long triggerTime, long stdyTime, int baseLoad,
			int randomLoad, int rate, Args args) {
		super(startTime, triggerTime, stdyTime, baseLoad, randomLoad, rate, args);

		m_reqpool = new EBReqPool(m_args);

		long maxEBNum = 0;
		if (m_rate > 0) {
			maxEBNum = m_baseLoad + m_randomLoad + m_rate * m_stdyTime;
		} else {
			maxEBNum = m_baseLoad + m_randomLoad;
		}

		m_reqpool.initialize((int) (maxEBNum * 1.5));

	}

	@Override
	void StartEB() {
		long beginTime = System.currentTimeMillis();
		long endTime = beginTime + m_stdyTime * 1000L;
		int baseLoad = m_baseLoad;
		while ((System.currentTimeMillis() - endTime) < 0) {
			long stime = System.currentTimeMillis();
			int realLoad = baseLoad + m_randomLoad;

			for (int j2 = 0; j2 < realLoad; j2++) {
				EB_ReqOpen eb = m_reqpool.getEB();
				eb.go();
			}
			baseLoad += m_rate;
			long etime = System.currentTimeMillis();
			long interval = (long) ((long) 1000 * m_args.getInterval());
			if ((etime - stime) < interval) {
				try {
					Thread.sleep((interval - (etime - stime)));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				// interval is not long enough to finish the work. This kind
				// of situation is not handled.Harry up to do next work.
			}
		}
		
		m_reqpool.clean();
	}

}
