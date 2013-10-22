package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;

public class TransOrderInq extends Transition {

	public String request(EB eb, String html) {
		return (eb.addIDs(EB.orderInqURL));
	}
}
