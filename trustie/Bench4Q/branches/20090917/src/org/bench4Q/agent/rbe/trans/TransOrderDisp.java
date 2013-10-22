package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;
import org.bench4Q.agent.rbe.util.URLUtil;

public class TransOrderDisp extends Transition {
	public String request(EB eb, String html) {
		if (eb.cid == EB.ID_UNKNOWN) {
			eb.cid = URLUtil.NURand(eb.rand, URLUtil.cidA, 1, URLUtil.numCustomer);
		}

		return (eb.addIDs(EB.orderDispURL + "?" + URLUtil.unameAndPass(eb.cid)));
	}
}
