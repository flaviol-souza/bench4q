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

import java.util.Vector;

import org.bench4Q.agent.rbe.EB;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.util.CharSetStrPattern;
import org.bench4Q.agent.rbe.util.StrStrPattern;
import org.bench4Q.agent.rbe.util.URLUtil;

/**
 * @author duanzhiquan
 * 
 */
public class TransProdDet extends Transition {

	/**
	 * 
	 */
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
			EBStats.getEBStats().error(11,
					"Unable to find any items for product detail.", "???", eb.isVIP);
			return ("");
		}

		i = eb.rand.nextInt(iid.size());
		i = ((Integer) iid.elementAt(i)).intValue();

		String url = EB.prodDetURL + "?" + URLUtil.field_iid + "=" + i;
		return (eb.addIDs(url));
	}

}
