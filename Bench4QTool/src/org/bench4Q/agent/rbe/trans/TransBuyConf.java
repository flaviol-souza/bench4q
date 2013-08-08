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
import org.bench4Q.agent.rbe.util.CharStrPattern;
import org.bench4Q.agent.rbe.util.StrStrPattern;
import org.bench4Q.agent.rbe.util.URLUtil;

/**
 * @author duanzhiquan
 * 
 */
public class TransBuyConf extends Transition {

	private static final StrStrPattern fnamePat = new StrStrPattern(
			"Firstname:</TD><TD>");
	private static final StrStrPattern lnamePat = new StrStrPattern(
			"Lastname: </TD><TD>");
	private static final CharStrPattern ltPat = new CharStrPattern('<');

	public String request(EB eb, String html) {

		String url = EB.buyConfURL;
		String n;

		int temp = html.indexOf("Total");
		int begin = html.indexOf("$", temp);
		if (begin > 0) {
			int end = html.indexOf("<", begin);
			String totalString = html.substring(begin + 1, end);
			double total = Double.parseDouble(totalString.trim());
			EBStats.getEBStats().Profit(total);

		}

		n = findName(fnamePat, html, eb.fname, "First name", eb.isVIP);
		if (eb.fname == null) {
			eb.fname = n;
		}

		n = findName(lnamePat, html, eb.lname, "Last name", eb.isVIP);
		if (eb.lname == null) {
			eb.lname = n;
		}

		if (eb.cid == EB.ID_UNKNOWN) {
			EBStats.getEBStats().error(4,
					"CID not known transitioning to buy confirm page.", "???", eb.isVIP);
		}
		url = url + "?" + URLUtil.field_cctype + "="
				+ URLUtil.unifCCType(eb.rand);
		url = url + "&" + URLUtil.field_ccnumber + "="
				+ URLUtil.nstring(eb.rand, 16, 16);

		url = url + "&" + URLUtil.field_ccname + "=" + eb.fname + "+"
				+ eb.lname;
		url = url + "&" + URLUtil.field_ccexp + "="
				+ URLUtil.unifExpDate(eb.rand);

		if (eb.rand.nextInt(100) < 5) {
			url = url + "&" + URLUtil.field_street1 + "="
					+ URLUtil.astring(eb.rand, 15, 40);
			url = url + "&" + URLUtil.field_street2 + "="
					+ URLUtil.astring(eb.rand, 15, 40);

			// Odd difference between this and BuyReq cit (astring(eb.rand,
			// 10,30))
			url = url + "&" + URLUtil.field_city + "="
					+ URLUtil.astring(eb.rand, 4, 30);
			url = url + "&" + URLUtil.field_state + "="
					+ URLUtil.astring(eb.rand, 2, 20);
			url = url + "&" + URLUtil.field_zip + "="
					+ URLUtil.astring(eb.rand, 5, 10);
			url = url + "&" + URLUtil.field_country + "="
					+ URLUtil.unifCountry(eb.rand);
		} else {
			url = url + "&" + URLUtil.field_street1 + "=";
			url = url + "&" + URLUtil.field_street2 + "=";

			// Odd difference between this and BuyReq cit (astring(eb.rand,
			// 10,30))
			url = url + "&" + URLUtil.field_city + "=";
			url = url + "&" + URLUtil.field_state + "=";
			url = url + "&" + URLUtil.field_zip + "=";
			url = url + "&" + URLUtil.field_country + "=";
		}

		url = url + "&" + URLUtil.field_shipping + "="
				+ URLUtil.unifShipping(eb.rand);
		return (eb.addIDs(url));
	}

	private String findName(StrStrPattern namePat, String html, String prev,
			String nameType, boolean isVIP) {
		String name;

		int i = namePat.find(html);
		if (i == -1) {
			EBStats.getEBStats().error(
					4,
					"Unable to find " + nameType
							+ " in HTML for buy request page.", "???", isVIP);
			return ("");
		}

		i = i + namePat.length();
		int j = ltPat.find(html, i);
		if (j == -1) {
			EBStats.getEBStats().error(
					4,
					"Unable to find " + nameType
							+ " in HTML for buy request page.", "???", isVIP);
			return ("");
		}

		name = html.substring(i, j);
		if (name.length() > 15) {
			System.out.println("WARNING: Name found is too long ("
					+ name.length() + ") (" + name + ").");
		}

		name = URLUtil.mungeURL(name);

		if ((prev != null) && (!prev.equals(name))) {
			EBStats.getEBStats().error(
					4,
					"Known " + nameType + " (" + prev
							+ ") does not equal name found in HTML (" + name
							+ ") in buy request page.", "????", isVIP);
		}

		return (name);
	}
}
