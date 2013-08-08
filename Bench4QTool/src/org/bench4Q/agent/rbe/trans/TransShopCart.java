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
/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there.
 * If you have any problem, you can  
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
import org.bench4Q.agent.rbe.util.StrStrPattern;
import org.bench4Q.agent.rbe.util.URLUtil;

/**
 * @author duanzhiquan
 * 
 */
public class TransShopCart extends Transition {
	protected String url;

	public String request(EB eb, String html) {

		url = EB.shopCartURL + "?" + URLUtil.field_addflag + "=N";

		return (eb.addIDs(url));
	}

	/* Find C_ID and SHOPPING_ID, if not already known. */
	public void postProcess(EB eb, String html) {
		if (eb.cid == EB.ID_UNKNOWN) {
			eb.cid = eb.findID(html, URLUtil.yourCID);
			// System.out.println("Found CID = " + eb.cid);
		}

		if (eb.sessionID == null) {
			eb.sessionID = findSessionID(eb, html, URLUtil.yourSessionID,
					URLUtil.endSessionID, "SESSIONID");
//			System.out.println(html);
			// System.out.println("Found SESSIONID = " + eb.sessionID);
		}

		if (eb.shopID == EB.ID_UNKNOWN) {
			eb.shopID = findID(eb, html, URLUtil.yourShopID, "SHOPPING_ID");
		}
	}

	private int findID(EB eb, String html, StrStrPattern tag, String name) {
		int id = eb.findID(html, tag);

		if (id == EB.ID_UNKNOWN) {
			EBStats.getEBStats().error(
					14,
					"findId: Unable to find " + name
							+ " in shopping cart page.", url, eb.isVIP);
		}

		return (id);
	}

	private String findSessionID(EB eb, String html, StrStrPattern tag,
			StrStrPattern etag, String name) {
		String id = URLUtil.findSessionID(html, tag, etag);

		if (id == null) {
			EBStats.getEBStats().error(
					14,
					"findSessionID: Unable to find " + name
							+ " tag in shopping cart page.", url, eb.isVIP);
		}

		return (id);
	}
}
