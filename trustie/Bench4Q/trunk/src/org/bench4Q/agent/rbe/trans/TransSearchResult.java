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
import org.bench4Q.agent.rbe.util.RBEUtil;
import org.bench4Q.agent.rbe.util.URLUtil;

/**
 * @author duanzhiquan
 * 
 */
public class TransSearchResult extends Transition {

	public String request(EB eb, String html) {
		int srchType = eb.rand.nextInt(3);
		String url = EB.searchResultURL;

		switch (srchType) {
		case 0:
			url = url
					+ "?"
					+ URLUtil.field_srchType
					+ "="
					+ URLUtil.authorType
					+ "&"
					+ URLUtil.field_srchStr
					+ "="
					+ URLUtil.digSyl(URLUtil.NURand(eb.rand, URLUtil.numItemA,
							0, RBEUtil.numItem / 10), 7);
			break;
		case 1:
			url = url
					+ "?"
					+ URLUtil.field_srchType
					+ "="
					+ URLUtil.titleType
					+ "&"
					+ URLUtil.field_srchStr
					+ "="
					+ URLUtil.digSyl(URLUtil.NURand(eb.rand, URLUtil.numItemA,
							0, RBEUtil.numItem / 5), 7);
			break;
		case 2:
			url = url + "?" + URLUtil.field_srchType + "="
					+ URLUtil.subjectType + "&" + URLUtil.field_srchStr + "="
					+ URLUtil.unifSubject(eb.rand);
			break;
		}

		return (eb.addIDs(url));
	}
}
