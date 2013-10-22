package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;

public class TransSearchReq extends Transition {

	public String request(EB eb, String html) {
		return (eb.addIDs(EB.searchReqURL));
	}
}
