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

import java.util.ArrayList;

import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.logging.Log;
import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.common.util.Logger;

/**
 * @author duanzhiquan
 * 
 */
public class EBClosed extends EB {
	public boolean test;
	public boolean terminate;

	public EBClosed(Args args, ArrayList<Integer> trace) {
		this.m_args = args;
		this.terminate = false;
		this.test = false;
		this.maxTrans = 1000000;
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
		this.rate = (args.getRate() / 100.0D);

		this.p_s_to_l = args.getP_s_to_l();
		this.p_l_to_s = args.getP_l_to_s();
		this.lambda_short = args.getLambda_short();
		this.lambda_long = args.getLambda_long();

		this.m_trace = trace;
		this.it = this.m_trace.iterator();

		this.tt_scale = this.m_args.getThinktime();
		this.tt_stagger = this.m_args.isTtMMPP();
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
			custRegURL = www + "customer_registration";
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
		this.startExp = System.currentTimeMillis();
		while (!this.terminate) {
			if (this.test) {
				this.isVIP = this.rand.nextDouble() < this.rate ? true : false;
				// System.out.println(isVIP);
				this.first = true;
				test();
				this.m_Client.getState().clearCookies();
			} else {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					Logger.getLogger().error("Unable to sleep!");
				}
			}
		}
	}

	public void test() {
		long tt = 0L; // Think Time.
		boolean sign = true;
		long startGet = System.currentTimeMillis();
		long currentTimeMillis = System.currentTimeMillis();
		this.sessionStart = startGet;
		
		
		while ((this.maxTrans == -1) || (this.maxTrans > 0)) {
			currentTimeMillis = System.currentTimeMillis();
			// permite terminar as requisicoes antes do fim do experimento
			if (currentTimeMillis > this.propertiesEB.getTimeEnd() && this.propertiesEB.isFrenquency()) {
				Logger.getLogger().debug(
						this.getName() + " is ENDING ... " + (currentTimeMillis - startExp)/1000);
				this.test = false;
			}
			
			

			if (currentTimeMillis >= this.propertiesEB.getTimeStart()) {
				if (this.terminate || !this.test) {
					this.sessionEnd = System.currentTimeMillis();
					EBStats.getEBStats().sessionRecorder(this.sessionStart, this.sessionEnd, this.sessionLen,
							this.Ordered, this.isVIP);
					return;
				}
				
				
				
				long endGet;
				if (this.nextReq != null) {
					// Check if user session is finished.
					if (this.toHome) {
						// User session is complete. Start new user session.
						this.sessionEnd = System.currentTimeMillis();
						EBStats.getEBStats().sessionRecorder(this.sessionStart, this.sessionEnd, this.sessionLen,
								this.Ordered, this.isVIP);
						initialize();
						return;
					}
					if (this.nextReq.equals("")) {
						EBStats.getEBStats().addErrorSession(this.curState, this.isVIP);
						initialize();
						continue;
					}
					// Receive HTML response page.
					if (this.rate > 0) {
						if (isVIP) {
							if (this.nextReq.contains("?")) {
								this.nextReq += "&bench4q_session_priority=10";
							} else {
								this.nextReq += "?bench4q_session_priority=10";
							}
						} else if (this.nextReq.contains("?")) {
							this.nextReq += "&bench4q_session_priority=1";
						} else {
							this.nextReq += "?bench4q_session_priority=1";
						}
					}
					if (this.first) {
						this.m_Client = HttpClientFactory.getInstance();
						this.m_Client.getParams().setCookiePolicy(CookiePolicy.RFC_2965);
					}

					startGet = System.currentTimeMillis();
					sign = getHTML(this.curState, this.nextReq, (currentTimeMillis - startExp)/1000);	
					
					endGet = System.currentTimeMillis();

					if (!sign) {
						EBStats.getEBStats().addErrorSession(this.curState, this.isVIP);
						initialize();
						
						continue;
					}
					this.first = false;

					// Compute and store Web Interaction Response Time (WIRT)
					EBStats.getEBStats().interaction(this.curState, startGet, endGet, tt, this.isVIP);
					this.sessionLen++;
					if (this.curState == 4) {
						this.Ordered = true;
					}
					this.curTrans.postProcess(this, this.html);
				} else {
					this.html = null;
					endGet = startGet;
				}

				if (!nextState()) {
					return;
				}
				if (this.nextReq != null) {
					// Pick think time (TT), and compute absolute request time
					tt = MAP();
					startGet = endGet + tt;
					if ((this.terminate) || (!this.test)) {
						return;
					}
					try {
						sleep(tt);
					} catch (InterruptedException inte) {
						Thread.currentThread().interrupt();
						return;
					}
					if (this.maxTrans > 0) {
						this.maxTrans--;
					}
				} else {
					EBStats.getEBStats().addErrorSession(this.curState, this.isVIP);
					initialize();
				}
			} else {
				try {
					// libera de sobrecarga
					Thread.sleep(500L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void initialize() {
		// initialize session info.
		this.curState = 0;
		this.nextReq = null;
		this.html = null;
		this.prevHTML = null;
		this.cid = ID_UNKNOWN;
		this.sessionID = null;
		this.shopID = ID_UNKNOWN;
		this.fname = null;
		this.lname = null;

		// initialize session record info.
		this.sessionStart = 0L;
		this.sessionEnd = 0L;
		this.sessionLen = 1;
		this.Ordered = false;
	}

	/**
	 * @param test
	 */
	public void setTest(boolean test) {
		this.test = test;
	}

	/**
	 * @param terminate
	 */
	public void setTerminate(boolean terminate) {
		this.terminate = terminate;
	}
}
