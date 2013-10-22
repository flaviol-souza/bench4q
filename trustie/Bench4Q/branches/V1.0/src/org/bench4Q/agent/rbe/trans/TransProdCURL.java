package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;

public class TransProdCURL extends TransProdDet {

	public String request(EB eb, String html) {
		return (super.request(eb, eb.prevHTML));
	}
}
