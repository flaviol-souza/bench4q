package org.bench4Q.agent.rbe.trans;

import java.util.Vector;

import org.bench4Q.agent.rbe.EB;
import org.bench4Q.agent.rbe.RBE;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.util.CharSetStrPattern;
import org.bench4Q.agent.rbe.util.StrStrPattern;
import org.bench4Q.agent.rbe.util.URLUtil;

public class TransProdDet extends Transition {

	public static final StrStrPattern itemPat = new StrStrPattern("I_ID=");

	public String request(EB eb, String html) {
		Vector<Integer> iid = new Vector<Integer>(0);
		int i;

		// Save HTML for the <CURL> transistion.
		// See TPC-W Spec. Clause 2.14.5.4 and chart in Clause 1.1
		eb.prevHTML = html;

		// Scan html for items.
		for (i = itemPat.find(html); i != -1; i = itemPat.find(html, i)) {
			i = i + itemPat.length();
			int s = CharSetStrPattern.digit.find(html, i);
			int e = CharSetStrPattern.notDigit.find(html, s + 1);
			iid.addElement(new Integer(Integer.parseInt(html.substring(s, e))));
		}

		if (iid.size() == 0) {
			EBStats.getEBStats().error(11, "Unable to find any items for product detail.", "???");
			return ("");
		}

		i = eb.rand.nextInt(iid.size());
		i = ((Integer) iid.elementAt(i)).intValue();

		String url = EB.prodDetURL + "?" + URLUtil.field_iid + "=" + i;
		return (eb.addIDs(url));
	}

}
