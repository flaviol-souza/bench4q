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
import org.bench4Q.agent.rbe.util.URLUtil;

/**
 * TPC-W initial transition. Decides whether new or returning user, and sets cid
 * accordingly. Sends HTTP request for home page.
 * 
 * @author duanzhiquan
 * 
 */
public class TransInit extends Transition {

	public String request(EB eb, String html) {
		if (eb.rand.nextInt(10) < 8) {
			eb.cid = URLUtil.NURand(eb.rand, URLUtil.cidA, 1,
					URLUtil.numCustomer);
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
//			System.out.print(html);
			eb.sessionID = URLUtil.findSessionID(html, URLUtil.yourSessionID,
					URLUtil.endSessionID);
			if (eb.sessionID == null) {
				EBStats.getEBStats().error(0,
						"findSessionID: Unable to find sessionid",
						"<TransInit>", eb.isVIP);
			}
		}
	};
}
