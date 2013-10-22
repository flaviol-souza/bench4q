package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;
import org.bench4Q.agent.rbe.RBE;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.util.URLUtil;

/*
 * TPC-W initial transition. Decides whether new or returning user,
 *  and sets cid accordingly.  Sends HTTP request for home page.
 */
public class TransInit extends Transition {

	public String request(EB eb, String html) {
		if (eb.rand.nextInt(10) < 8) {
			eb.cid = URLUtil.NURand(eb.rand, URLUtil.cidA, 1, URLUtil.numCustomer);
			eb.sessionID = null;
			return (EB.homeURL + "?" + URLUtil.field_cid + "=" + eb.cid);
		} else {
			eb.cid = EB.ID_UNKNOWN;
			eb.sessionID = null;
			return (EB.homeURL);
		}
	}

	public void postProcess(EB eb, String html) {
		if (eb.sessionID == null) {
			eb.sessionID = URLUtil.findSessionID(html, URLUtil.yourSessionID, URLUtil.endSessionID);
			if (eb.sessionID == null) {
				EBStats.getEBStats().error(0, "Unable to find shopping (session) id"
						+ " tag in shopping cart page." + "sessionid = " + URLUtil.yourSessionID
						+ " endsessionID = " + URLUtil.endSessionID + "\nHtml\n" + html + "<???>",
						"<???>");
			}
		}
	};
}
