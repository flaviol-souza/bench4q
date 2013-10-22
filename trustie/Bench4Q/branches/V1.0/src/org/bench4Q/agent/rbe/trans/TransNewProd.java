package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;
import org.bench4Q.agent.rbe.util.URLUtil;

public class TransNewProd extends Transition {

	public String request(EB eb, String html) {
		int srchType = eb.rand.nextInt(3);
		String url = baseURL();

		url = url + "?" + URLUtil.field_subject + "=" + URLUtil.unifHomeSubject(eb.rand);

		return (eb.addIDs(url));
	}

	protected String baseURL() {
		return EB.newProdURL;
	}
}
