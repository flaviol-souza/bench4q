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
import org.bench4Q.agent.rbe.util.CharSetStrPattern;
import org.bench4Q.agent.rbe.util.StrStrPattern;
import org.bench4Q.agent.rbe.util.URLUtil;

/**
 * @author duanzhiquan
 * 
 */
public class TransShopCartRef extends TransShopCart {
	/* protected String url; inherited. */

	private static final StrStrPattern qtyPat = new StrStrPattern("NAME=\"QTY");
	private static final StrStrPattern valuePat = new StrStrPattern("VALUE=\"");

	public String request(EB eb, String html) {
		int i, c;

		/* Find out how many items are on the page. */
		for (c = 0, i = qtyPat.find(html, 0); i != -1; i = qtyPat.find(html,
				i + 1), c++)
			;

		if (c == 0) {
			EBStats.getEBStats().error(14,
					"Unable to find QTY in shopping cart page.", "???", eb.isVIP);
			return ("");
		}

		/* Get the current quantities. */
		int[] qty = new int[c];
		for (c = 0, i = qtyPat.find(html, 0); i != -1; i = qtyPat.find(html,
				i + 1), c++) {
			int j = valuePat.find(html, i + qtyPat.length())
					+ valuePat.length();
			int e = CharSetStrPattern.notDigit.find(html, j);
			qty[c] = Integer.parseInt(html.substring(j, e));
		}

		int[] qtyNew = new int[c];
		if (c == 1) {
			// See TPC-W Clause 2.4.5.1
			qtyNew[0] = eb.rand.nextInt(10) + 1;
		} else {
			// See TPC-W Clause 2.4.5.1
			int r = eb.rand.nextInt(c) + 1;
			int[] idx = new int[c];
			for (i = 0; i < c; idx[i] = i, i++)
				;
			for (i = 0; i < (c - 1); i++) {
				int d = eb.rand.nextInt(c - i) + i;
				int a = idx[d];
				idx[d] = idx[i];
				idx[i] = a;
			}

			for (i = 0; i < r; i++) {
				qtyNew[idx[i]] = eb.rand.nextInt(9);
				if (qtyNew[idx[i]] >= qty[idx[i]]) {
					qtyNew[idx[i]]++;
				}
			}
		}

		url = EB.shopCartURL + "?" + URLUtil.field_addflag + "=N";

		for (i = 0; i < c; i++) {
			url = url + "&" + URLUtil.field_qty + "=" + qtyNew[i];
		}

		return (eb.addIDs(url));
	}
}
