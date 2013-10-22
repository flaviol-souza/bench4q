package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;
import org.bench4Q.agent.rbe.RBE;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.util.CharSetStrPattern;
import org.bench4Q.agent.rbe.util.StrStrPattern;
import org.bench4Q.agent.rbe.util.URLUtil;

public class TransShopCartAdd extends TransShopCart {
	/* protected String url; inherited. */

	private static final StrStrPattern iid = new StrStrPattern("I_ID=");

	public String request(EB eb, String html) {
		int i, e, id;

		/* Find the I_ID to add. */
		i = iid.find(html);
		if (i == -1) {
			EBStats.getEBStats().error(14, "Unable to find I_ID in product detail page.", "???");
			return ("");
		}
		i = i + iid.length();

		e = CharSetStrPattern.notDigit.find(html.substring(i));
		if (e == -1) {
			EBStats.getEBStats().error(14, "Unable to find I_ID in product detail page.", "???");
			return ("");
		}
		e = e + i;
		id = Integer.parseInt(html.substring(i, e));

		url = EB.shopCartURL + "?" + URLUtil.field_addflag + "=Y&" + URLUtil.field_iid + "=" + id;

		return (eb.addIDs(url));
	}

	/* Find C_ID and SHOPPING_ID, if not already known. */
	/*
	 * public void postProcess(EB eb, String html) inherited from
	 * EBWShopCartTrans
	 */
}
