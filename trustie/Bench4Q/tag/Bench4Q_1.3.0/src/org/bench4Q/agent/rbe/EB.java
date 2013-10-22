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
package org.bench4Q.agent.rbe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.trans.Transition;
import org.bench4Q.agent.rbe.util.CharSetStrPattern;
import org.bench4Q.agent.rbe.util.CharStrPattern;
import org.bench4Q.agent.rbe.util.RBEUtil;
import org.bench4Q.agent.rbe.util.StrStrPattern;
import org.bench4Q.agent.rbe.util.StringPattern;
import org.bench4Q.agent.rbe.util.URLUtil;

/**
 * @author duanzhiquan
 * 
 */
public abstract class EB extends Thread {

	protected long sessionStart = 0;
	protected long sessionEnd = 0;
	protected int sessionLen = 1;
	protected boolean Ordered = false;

	protected Args m_args;

	/**
	 * CUSTOMER_ID. See TPC-W Spec.
	 */
	public int cid;
	/**
	 *ESSION_ID. See TPC-W Spec.
	 */
	public String sessionID;
	/**
	 * Shopping ID.
	 */
	public int shopID;
	/**
	 * Customer first name.
	 */
	public String fname = null;
	/**
	 * Customer last name.
	 */
	public String lname = null;

	/**
	 * Transition probabilities.
	 */
	public int[][] transProb;
	/**
	 * EB transitions.
	 */
	public Transition[][] trans;
	/**
	 * 
	 */
	public Transition curTrans;
	/**
	 * Current state.
	 */
	public int curState;
	/**
	 *Next HTTP request.
	 */
	public String nextReq;
	/**
	 * Received HTML
	 */
	public String html;
	/**
	 * HTML from a previous page.
	 */
	public String prevHTML;
	/**
	 * Number of transitions. -1 implies continuous
	 */
	public int maxTrans;
	/**
	 * 
	 */
	public byte[] buffer = new byte[4096];

	/**
	 * 
	 */
	public boolean toHome;

	/**
	 * 
	 */
	public Random rand = new Random();

	/**
	 * Think time-scaling.
	 */
	public double tt_scale;

	/**
	 * Tolerance_scale.
	 */
	public double tolerance_scale;
	/**
	 * 
	 */
	public int retry;

	/**
	 * 
	 */
	public static final int NO_TRANS = 0;
	/**
	 * 
	 */
	public static final int MIN_PROB = 1;
	/**
	 * 
	 */
	public static final int MAX_PROB = 9999;
	/**
	 * 
	 */
	public static final int ID_UNKNOWN = -1;

	/**
	 * 
	 */
	public static String www;
	/**
	 * 
	 */
	public static String homeURL;
	/**
	 * 
	 */
	public static String shopCartURL;
	/**
	 * 
	 */
	public static String orderInqURL;
	/**
	 * 
	 */
	public static String orderDispURL;
	/**
	 * 
	 */
	public static String searchReqURL;
	/**
	 * 
	 */
	public static String searchResultURL;
	/**
	 * 
	 */
	public static String newProdURL;
	/**
	 * 
	 */
	public static String bestSellURL;
	/**
	 * 
	 */
	public static String prodDetURL;
	/**
	 * 
	 */
	public static String custRegURL;
	/**
	 * 
	 */
	public static String buyReqURL;
	/**
	 * 
	 */
	public static String buyConfURL;
	/**
	 * 
	 */
	public static String adminReqURL;
	/**
	 * 
	 */
	public static String adminConfURL;

	/**
	 * 
	 */
	public final StrStrPattern imgPat = new StrStrPattern("<IMG");
	/**
	 * 
	 */
	public final StrStrPattern inputPat = new StrStrPattern(
			"<INPUT TYPE=\"IMAGE\"");
	/**
	 * 
	 */
	public final StrStrPattern srcPat = new StrStrPattern("SRC=\"");
	/**
	 * 
	 */
	public final CharStrPattern quotePat = new CharStrPattern('\"');

	/**
	 * @param state
	 * @param url
	 * @return bolean
	 */
	public boolean getHTML(int state, String url) {
		double tolerance = tolerance(curState);
		html = "";
		int statusCode;
		GetMethod httpget = new GetMethod(url);
		httpget.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		if (tolerance != 0) {
			httpget.getParams().setSoTimeout((int) (tolerance * 1000));
		}

		try {
			statusCode = HttpClientFactory.getInstance().executeMethod(httpget);

			if (statusCode != HttpStatus.SC_OK) {
				EBStats.getEBStats().error(state,
						"HTTP response ERROR: " + statusCode, url);
				return false;
			}
			BufferedReader bin = new BufferedReader(new InputStreamReader(
					httpget.getResponseBodyAsStream()));
			StringBuilder result = new StringBuilder();
			String s;
			while ((s = bin.readLine()) != null) {
				result.append(s);
			}
			html = new String(result);
		} catch (Exception e) {
			EBStats.getEBStats().error(state, "get methed ERROR.", url);
			return false;
		} finally {
			// always release the connection after we're done
			httpget.releaseConnection();
		}

		if (!m_args.isGetImage()) {
			return true;
		}
		Vector<ImageReader> imageRd = new Vector<ImageReader>(0);
		URL u;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			EBStats.getEBStats().error(state, "get image ERROR.", url);
			return false;
		}
		findImg(html, u, imgPat, srcPat, quotePat, imageRd);
		findImg(html, u, inputPat, srcPat, quotePat, imageRd);
		while (imageRd.size() > 0) {
			int max = imageRd.size();
			int min = Math.max(max - RBEUtil.maxImageRd, 0);
			int i;
			try {
				for (i = min; i < max; i++) {
					ImageReader rd = (ImageReader) imageRd.elementAt(i);
					if (!rd.readImage(state)) {
						imageRd.removeElementAt(i);
						i--;
						max--;
					}
				}
			} catch (InterruptedException inte) {
				EBStats.getEBStats().error(state, "get image ERROR.", url);
				return true;
			}
		}

		return true;
	}

	/**
	 * 
	 */
	public void nextState() {
		int i = rand.nextInt(MAX_PROB - MIN_PROB + 1) + MIN_PROB;
		for (int j = 0; j < transProb[curState].length; j++) {

			if (transProb[curState][j] >= i) {
				// record the trans.
				EBStats.getEBStats().transition(curState, j);
				curTrans = trans[curState][j];
				nextReq = curTrans.request(this, html);
				toHome = trans[curState][j].toHome();
				curState = j;
				return;
			}
		}
	}

	/**
	 * think time obey an Negative exponential distribution. TPC-W spec for
	 * Think Time (Clause 5.3.2.1)
	 * 
	 * @return think time.
	 */

	public long thinkTime() {
		double r = rand.nextDouble();
		long result;
		if (r < (4.54e-5)) {
			result = 70000L;
		} else {
			result = ((long) (-7000.0 * Math.log(r)));
		}
		return ((long) (result * tt_scale));

	}

	/**
	 * @return tolerance time.
	 */
	public double tolerance(int cur) {

		int LONG = 80;
		int SHORT = 8;

		int[] tolerance = { SHORT, LONG, LONG, SHORT, LONG, LONG, LONG, SHORT,
				SHORT, LONG, LONG, SHORT, SHORT, SHORT, LONG };

		return tolerance_scale * tolerance[cur];

	}

	/**
	 * @param html
	 * @param url
	 * @param imgPat
	 * @param srcPat
	 * @param quotePat
	 * @param imageRd
	 */
	public void findImg(String html, URL url, StringPattern imgPat,
			StringPattern srcPat, StringPattern quotePat,
			Vector<ImageReader> imageRd) {
		int cur = 0;
		while ((cur = imgPat.find(html, cur)) > -1) {
			cur = srcPat.find(html, imgPat.end() + 1);
			quotePat.find(html, srcPat.end() + 1);
			String imageURLString = html.substring(srcPat.end() + 1, quotePat
					.start());
			imageRd.addElement(new ImageReader(url, imageURLString, buffer));
			cur = quotePat.start() + 1;
		}
	}

	/**
	 * Adds CUSTOMER_ID and SHOPPING_ID fields to HTTP request, if they are
	 * known.
	 * 
	 * @param i
	 * @return addID URL
	 */
	public String addIDs(String i) {
		if (sessionID != null) {
			i = URLUtil.addSession(i, URLUtil.field_sessionID, "" + sessionID);
		}
		if (cid != ID_UNKNOWN) {
			i = URLUtil.addField(i, URLUtil.field_cid, "" + cid);
		}
		if (shopID != ID_UNKNOWN) {
			i = URLUtil.addField(i, URLUtil.field_shopID, "" + shopID);
		}
		return i;
	}

	/**
	 * @param html
	 * @param tag
	 * @return id
	 */
	public int findID(String html, StrStrPattern tag) {
		int id;
		// Find the tag string.
		int i = tag.find(html);
		if (i == -1) {
			return (EB.ID_UNKNOWN);
		}
		i = i + tag.length();

		// Find the digits following the tag string.
		int j = CharSetStrPattern.digit.find(html.substring(i));
		if (j == -1) {
			return (EB.ID_UNKNOWN);
		}

		// Find the end of the digits.
		j = j + i;
		int k = CharSetStrPattern.notDigit.find(html.substring(j));
		if (k == -1) {
			k = html.length();
		} else {
			k = k + j;
		}

		id = Integer.parseInt(html.substring(j, k));

		return id;
	}

}
