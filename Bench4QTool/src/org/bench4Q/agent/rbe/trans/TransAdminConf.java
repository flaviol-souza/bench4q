/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there. 
 * 
 * Distributed according to the GNU Lesser General Public Licence. 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by   
 * the Free Software Foundation; either version 2.1 of the License, or any
 * later version.
 * 
 * SEE Copyright.txt FOR FULL COPYRIGHT INFORMATION.
 * 
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * This version is a based on the implementation of TPC-W from University of Wisconsin. 
 * This version used some source code of The Grinder.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *  * Initial developer(s): Zhiquan Duan.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.util.CharSetStrPattern;
import org.bench4Q.agent.rbe.util.Pad;
import org.bench4Q.agent.rbe.util.StrStrPattern;
import org.bench4Q.agent.rbe.util.URLUtil;

/**
 * TPC-W Administrative Request transition from the product detail page to the
 * admin request page. Supplies the I_ID of the book being changed.
 * 
 * @author duanzhiquan
 * 
 */
public class TransAdminConf extends Transition {

	private static final StrStrPattern iid = new StrStrPattern(
			"I_ID\" TYPE=\"hidden\" VALUE=\"");

	public String request(EB eb, String html) {
		String url;
		int i, e, id;

		/* Find the I_ID to add. */
		i = iid.find(html);
		if (i == -1) {
			EBStats.getEBStats().error(1,
					"Unable to find I_ID in admin confirm page.", "???", eb.isVIP);
			return ("");
		}
		i = i + iid.length();

		e = CharSetStrPattern.notDigit.find(html.substring(i));
		if (e == -1) {
			EBStats.getEBStats().error(1,
					"Unable to find end of I_ID in admin confirm page.", "???", eb.isVIP);
			return ("");
		}
		e = e + i;
		id = Integer.parseInt(html.substring(i, e));
		url = EB.adminConfURL + "?" + URLUtil.field_newimage + "="
				+ URLUtil.unifImage(eb.rand) + "&" + URLUtil.field_newthumb
				+ "=" + URLUtil.unifThumbnail(eb.rand) + "&"
				+ URLUtil.field_newcost + "=" + URLUtil.unifDollars(eb.rand)
				+ "." + Pad.lz(2, URLUtil.unifCents(eb.rand)) + "&"
				+ URLUtil.field_iid + "=" + id;

		return (eb.addIDs(url));
	}
}
