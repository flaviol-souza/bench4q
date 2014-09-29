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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;

/**
 * @author duanzhiquan
 * 
 */
public class EBOpen extends EB {

	/**
	 * @param args
	 */
	public EBOpen(Args args, ArrayList<Integer> trace, boolean _isVIP) {
		this.m_args = args;
		this.maxTrans = 1000;
		this.curState = 0;
		this.nextReq = null;
		this.html = null;
		this.prevHTML = null;
		this.cid = ID_UNKNOWN;
		this.sessionID = null;
		this.shopID = ID_UNKNOWN;
		this.fname = null;
		this.lname = null;
		this.toHome = false;
		this.m_trace = trace;
		this.it = this.m_trace.iterator();
		this.isVIP = _isVIP;

		this.p_s_to_l = args.getP_s_to_l();
		this.p_l_to_s = args.getP_l_to_s();
		this.lambda_short = args.getLambda_short();
		this.lambda_long = args.getLambda_long();

		this.tt_scale = this.m_args.getThinktime();
		tt_stagger = this.m_args.isTtMMPP();
		this.tolerance_scale = this.m_args.getTolerance();
		this.retry = this.m_args.getRetry();

		www = this.m_args.getBaseURL();
		if (www.endsWith("/")) {
			homeURL = www + "home";
			shopCartURL = www + "shopping_cart";
			orderInqURL = www + "order_inquiry";
			orderDispURL = www + "order_display";
			searchReqURL = www + "search_request";
			searchResultURL = www + "execute_search";
			newProdURL = www + "new_products";
			bestSellURL = www + "best_sellers";
			prodDetURL = www + "product_detail";
			custRegURL = www + "customer_registrationt";
			buyReqURL = www + "buy_request";
			buyConfURL = www + "buy_confirm";
			adminReqURL = www + "admin_request";
			adminConfURL = www + "admin_response";
		} else {
			homeURL = www + "/home";
			shopCartURL = www + "/shopping_cart";
			orderInqURL = www + "/order_inquiry";
			orderDispURL = www + "/order_display";
			searchReqURL = www + "/search_request";
			searchResultURL = www + "/execute_search";
			newProdURL = www + "/new_products";
			bestSellURL = www + "/best_sellers";
			prodDetURL = www + "/product_detail";
			custRegURL = www + "/customer_registration";
			buyReqURL = www + "/buy_request";
			buyConfURL = www + "/buy_confirm";
			adminReqURL = www + "/admin_request";
			adminConfURL = www + "/admin_response";
		}

		String mixType = this.m_args.getMix();
		Mix mix = new Mix();
		Mix.initialize();
		if (mixType.equals("browsing")) {
			this.transProb = mix.getTransProb("browsing");
			this.trans = mix.getTrans("browsing");
		} else if (mixType.equals("shopping")) {
			this.transProb = mix.getTransProb("shopping");
			this.trans = mix.getTrans("shopping");
		} else if (mixType.equals("ordering")) {
			this.transProb = mix.getTransProb("ordering");
			this.trans = mix.getTrans("ordering");
		} else {
			System.out.println("EB initiate mix ERROR");
		}
	}

	public void run() {
		long tt = 0L; // Think Time.
		boolean sign = true;
		long wirt_t1 = System.currentTimeMillis();
		long wirt_t2;
		long currentTimeMillis = System.currentTimeMillis();
		// session start.
		this.sessionStart = wirt_t1;
		this.first = true;
		while ((this.maxTrans == -1) || (this.maxTrans > 0)) {
			currentTimeMillis = System.currentTimeMillis();
			// se o tempo do degrao foi alcancado 
			if (currentTimeMillis > this.propertiesEB.getTimeEnd()){
				maxTrans = 0;
			}

			if (this.nextReq != null) {
				// Check if user session is finished.
				if (this.toHome) {
					this.sessionEnd = System.currentTimeMillis();
					EBStats.getEBStats().sessionRecorder(this.sessionStart,
							this.sessionEnd, this.sessionLen, this.Ordered, this.isVIP);
					return;
				}
				if (this.nextReq.equals("")) {
					EBStats.getEBStats().addErrorSession(this.curState, this.isVIP);
					return;
				}
				// Send HTTP request.
				URL httpReq;
				try {
					httpReq = new URL(nextReq);
				} catch (MalformedURLException e) {
					EBStats.getEBStats().addErrorSession(this.curState, this.isVIP);
					return;
				}

				if (this.first) {
					this.m_Client = HttpClientFactory.getInstance();
					this.m_Client.getParams().setCookiePolicy(CookiePolicy.RFC_2965);
				}

				wirt_t1 = System.currentTimeMillis();
				sign = getHTML(this.curState, this.nextReq);
				if (!sign) {
					EBStats.getEBStats().addErrorSession(this.curState, this.isVIP);
					return;
				}

				wirt_t2 = System.currentTimeMillis();

				EBStats.getEBStats().interaction(this.curState, wirt_t1, wirt_t2, tt, this.isVIP);
				this.sessionLen++;

				if (this.curState == 4) {
					this.Ordered = true;
				}

				this.curTrans.postProcess(this, this.html);
			} else {
				this.html = null;
				wirt_t2 = wirt_t1;
			}
			if (!nextState()){
				return;
			}
			if (this.nextReq != null) {
				// Pick think time (TT), and compute absolute request time
				tt = MAP();
				wirt_t1 = wirt_t2 + tt;
				try {
					sleep(tt);
				} catch (InterruptedException inte) {
					EBStats.getEBStats().addErrorSession(this.curState, this.isVIP);
					System.out.println(" Caught an interrupted exception!");
					return;
				}
				if (this.maxTrans > 0){
					this.maxTrans--;
				}
			} else {
				EBStats.getEBStats().addErrorSession(this.curState, this.isVIP);
			}
		}
	}
}
