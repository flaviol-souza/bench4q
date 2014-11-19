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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
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
import org.bench4Q.common.util.Logger;

/**
 * @author duanzhiquan
 * 
 */
public abstract class EB extends Thread {

	protected PropertiesEB propertiesEB;
	protected long sessionStart = 0;
	protected long sessionEnd = 0;
	protected int sessionLen = 1;
	protected boolean Ordered = false;

	public boolean isVIP;

	protected Args m_args;

	/**
	 * CUSTOMER_ID. See TPC-W Spec.
	 */
	public int cid;
	/**
	 * ESSION_ID. See TPC-W Spec.
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
	 * Next HTTP request.
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
	 * MMPP used to generate think times
	 * */
	public static MMPP mmpp_tt = new MMPP();

	/**
	 * Think time-scaling.
	 */
	public double tt_scale;

	public boolean tt_stagger;

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
	public ArrayList<Integer> m_trace;

	public int state = 1;

	public double p_s_to_l;
	public double p_l_to_s;
	public double lambda_short;
	public double lambda_long;

	public boolean first;
	public double rate;
	Cookie[] cookies;
	public long start;
	public long end;
	public HttpClient m_Client;
	public boolean joke = false;

	/**
	 * 
	 */
	public Iterator it;

	/**
	 * 
	 */
	public final StrStrPattern imgPat = new StrStrPattern("<IMG");
	/**
	 * 
	 */
	public final StrStrPattern inputPat = new StrStrPattern("<INPUT TYPE=\"IMAGE\"");
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
	 * @return boolean
	 */
	public boolean getHTML(int state, String url) {

		double tolerance = tolerance(this.curState);
		html = "";

		GetMethod httpget = new GetMethod(url);
		try {

			if (tolerance != 0.0D) {
				httpget.getParams().setSoTimeout((int) (tolerance * 1000.0D));
			}
			this.start = System.currentTimeMillis();
			int statusCode = this.m_Client.executeMethod(httpget);
			this.end = System.currentTimeMillis();
			Cookie[] cc = this.m_Client.getState().getCookies();
			for (int i = 0; i < cc.length; i++) {
				if (cc[i].getName().equalsIgnoreCase("jsessionid")) {
					this.sessionID = cc[i].getValue();
				}
			}

			if (sessionID == null) {
				String cString = null;
				Header[] header = httpget.getRequestHeaders();
				for (int i = 0; i < header.length; i++) {
					if (header[i].getName().equalsIgnoreCase("cookie")) {
						cString = header[i].getValue();
					}
				}
				int indexs = 0;
				if (cString != null) {
					indexs = cString.indexOf("JSESSIONID=");
				}
				indexs += "JSESSIONID=".length();
				String nextString = cString.substring(indexs);
				int indexe = 0;
				indexe = nextString.indexOf(";");
				this.sessionID = nextString.substring(0, indexe);
			}

			if (statusCode != HttpStatus.SC_OK) {
				EBStats.getEBStats().error(state, "HTTP response ERROR: " + statusCode, url, isVIP);
				return false;
			}

			BufferedReader bin = new BufferedReader(new InputStreamReader(httpget.getResponseBodyAsStream()));
			StringBuilder result = new StringBuilder();
			String s;
			while ((s = bin.readLine()) != null) {
				result.append(s);
			}
			this.html = new String(result);
		} catch (Exception e) {
			EBStats.getEBStats().error(state, "get methed ERROR.", url, isVIP);
			Logger.getLogger().debug("get methed ERROR. " + e.toString());
			return false;
		} finally {
			httpget.releaseConnection();
		}

		int statusCode;
		httpget.releaseConnection();
		if (!this.m_args.isGetImage()) {
			return true;
		}
		Vector<ImageReader> imageRd = new Vector<ImageReader>(0);
		URL u;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			EBStats.getEBStats().error(state, "get image ERROR.", url, this.isVIP);
			Logger.getLogger().debug("get image ERROR. "+e.toString());
			return false;
		}

		findImg(this.html, u, this.imgPat, this.srcPat, this.quotePat, imageRd);
		findImg(this.html, u, this.inputPat, this.srcPat, this.quotePat, imageRd);
		while (imageRd.size() > 0) {
			int max = imageRd.size();
			int min = Math.max(max - RBEUtil.maxImageRd, 0);
			try {
				for (int i = min; i < max; i++) {
					ImageReader rd = (ImageReader) imageRd.elementAt(i);
					if (!rd.readImage(state, this.isVIP)) {
						imageRd.removeElementAt(i);
						i--;
						max--;
					}
				}
			} catch (InterruptedException inte) {
				EBStats.getEBStats().error(state, "get image ERROR.", url, this.isVIP);
				return true;
			}
		}

		return true;
	}

	/**
	 * @return
	 * 
	 */
	public boolean nextState() {
		int i;
		if (!(m_args.isReplay())) {
			i = rand.nextInt(MAX_PROB - MIN_PROB + 1) + MIN_PROB;
			if (m_args.isRecord()) {
				m_trace.add(i);
			}
		} else {

			if (it.hasNext())
				i = (Integer) it.next();
			else
				return false;

		}
		for (int j = 0; j < transProb[curState].length; j++) {
			if (transProb[curState][j] >= i) {
				// record the trans.
				EBStats.getEBStats().transition(curState, j);
				// System.out.println("randnumber = " + i + " nextstate = " +
				// j);
				curTrans = trans[curState][j];
				nextReq = curTrans.request(this, html);
				toHome = trans[curState][j].toHome();
				curState = j;
				return true;
			}
		}
		return false;

	}

	/**
	 * think time obey an Negative exponential distribution. TPC-W spec for
	 * Think Time (Clause 5.3.2.1)
	 * 
	 * @return think time.
	 */

	public long thinkTime(double time) {

		if (tt_stagger) {
			double r = rand.nextDouble();
			long result;
			if (r < (4.54e-5)) {

				result = 70000L;
			} else {
				result = ((long) (-7000 * Math.log(r)));
			}
			// System.out.println("ExpNeg:\t"+(long) (result *
			// tt_scale)+"\tscale:\t"+tt_scale);
			return ((long) (result * tt_scale));
		} else {
			//XXX nao é o lugar para este tipo de think time
			/****************************************************
			 * generate the user think times from a 2-state MAP e.g., the MAP
			 * has mean = 7second, so r=1000*mean(ms)
			 ***************************************************/
			// long r = rbe.negExp(rand, 7000L, 0.36788, 70000L, 4.54e-5,
			// 7000.0);
			double r = 1000 * mmpp_tt.gen_interval();

			r = (long) (tt_scale * r); // tt_scale = 1 if think=7second

			// System.out.println("MMPP: "+(long) r+"\tscale:\t"+tt_scale);

			return ((long) r);
		}

	}

	public long MAP() {
		double r = rand.nextDouble();

		if (state == 0 && r <= p_l_to_s)
			state = 1;
		else if (state == 1 && r <= p_s_to_l)
			state = 0;

		if (state == 0)
			return thinkTime(lambda_long);
		else if (state == 1)
			return thinkTime(lambda_short);
		else
			return -1;

	}

	/**
	 * @return tolerance time.
	 */
	public double tolerance(int cur) {

		int LONG = 80;
		int SHORT = 8;

		int[] tolerance = { SHORT, LONG, LONG, SHORT, LONG, LONG, LONG, SHORT, SHORT, LONG, LONG, SHORT, SHORT, SHORT,
				LONG };

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
	public void findImg(String html, URL url, StringPattern imgPat, StringPattern srcPat, StringPattern quotePat,
			Vector<ImageReader> imageRd) {
		int cur = 0;
		while ((cur = imgPat.find(html, cur)) > -1) {
			cur = srcPat.find(html, imgPat.end() + 1);
			quotePat.find(html, srcPat.end() + 1);
			String imageURLString = html.substring(srcPat.end() + 1, quotePat.start());
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

	public PropertiesEB getPropertiesEB() {
		return propertiesEB;
	}

	public void setPropertiesEB(PropertiesEB propertiesEB) {
		this.propertiesEB = propertiesEB;
	}

}
