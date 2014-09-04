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

import javax.rmi.CORBA.Tie;

import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;

/**
 * @author duanzhiquan
 * 
 */
public class EBClosed extends EB {
	/**
	 * 
	 */
	public boolean test;
	/**
	 * 
	 */
	public boolean terminate;

	private static final boolean DEBUG = false;

	/**
	 * @param args
	 */
	public EBClosed(Args args, ArrayList<Integer> trace) {
		m_args = args;
		terminate = false;
		test = false;
		maxTrans = 1000000;
		curState = 0;
		nextReq = null;
		html = null;
		prevHTML = null;
		cid = ID_UNKNOWN;
		sessionID = null;
		shopID = ID_UNKNOWN;
		fname = null;
		lname = null;
		toHome = false;
		rate = args.getRate() / 100.0;

		p_s_to_l = args.getP_s_to_l();
		p_l_to_s = args.getP_l_to_s();
		lambda_short = args.getLambda_short();
		lambda_long = args.getLambda_long();

		m_trace = trace;
		it = m_trace.iterator();

		tt_scale = this.m_args.getThinktime();
		tt_stagger = this.m_args.isTtMMPP();
		tolerance_scale = this.m_args.getTolerance();
		retry = this.m_args.getRetry();

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

		String mixType = m_args.getMix();
		Mix mix = new Mix();
		Mix.initialize();
		if (mixType.equals("browsing")) {
			transProb = mix.getTransProb("browsing");
			trans = mix.getTrans("browsing");
		} else if (mixType.equals("shopping")) {
			transProb = mix.getTransProb("shopping");
			trans = mix.getTrans("shopping");
		} else if (mixType.equals("ordering")) {
			transProb = mix.getTransProb("ordering");
			trans = mix.getTrans("ordering");
		} else {
			System.out.println("EB initiate mix ERROR");
		}
	}

	public void run() {
		while (!this.terminate) {
			if (this.propertiesEB.isFrenquency()) {
				isVIP = rand.nextDouble() < rate ? true : false;
				first = true;
				test2();
				m_Client.getState().clearCookies();
			} else if (test) {
				isVIP = rand.nextDouble() < rate ? true : false;
				// System.out.println(isVIP);
				first = true;
				test();
				m_Client.getState().clearCookies();
			} else {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					System.out.println("Unable to sleep!");
				}
			}
		}
	}

	/**
	 * 
	 */
	public void test2() {

		long startGet;
		long endGet;
		long tt = 0L; // Think Time.
		boolean sign = true;
		startGet = System.currentTimeMillis();
		// session start.
		sessionStart = startGet;

		while ((maxTrans == -1) || (maxTrans > 0)) {

			long currentTimeMillis = System.currentTimeMillis();
			//permite terminar as requisicoes
			if (currentTimeMillis > this.propertiesEB.getTimeEnd()){
				maxTrans = 0;
			}
			
			if (currentTimeMillis >= this.propertiesEB.getTimeStart() && currentTimeMillis <= this.propertiesEB.getTimeEnd() ) {
				System.out.println("entrou close");
				if (this.terminate || !this.test) {
					sessionEnd = System.currentTimeMillis();
					EBStats.getEBStats().sessionRecorder(sessionStart,
							sessionEnd, sessionLen, Ordered, isVIP);
					return;
				}
				if (nextReq != null) {
					// Check if user session is finished.
					if (toHome) {
						// User session is complete. Start new user session.
						sessionEnd = System.currentTimeMillis();
						EBStats.getEBStats().sessionRecorder(sessionStart,
								sessionEnd, sessionLen, Ordered, isVIP);
						initialize();
						return;
					}
					if (nextReq.equals("")) {
						EBStats.getEBStats().addErrorSession(curState, isVIP);
						// sessionEnd = System.currentTimeMillis();
						// EBStats.getEBStats().sessionRecorder(sessionStart,
						// sessionEnd, sessionLen, Ordered);
						initialize();
						continue;
					}
					// Receive HTML response page.

					if (rate > 0) {
						if (isVIP) {
							if (nextReq.contains("?"))
								nextReq = nextReq
										+ "&bench4q_session_priority=10";
							else {
								nextReq = nextReq
										+ "?bench4q_session_priority=10";
							}
						} else {
							if (nextReq.contains("?"))
								nextReq = nextReq
										+ "&bench4q_session_priority=1";
							else {
								nextReq = nextReq
										+ "?bench4q_session_priority=1";
							}
						}

					}
					if (first) {
						m_Client = HttpClientFactory.getInstance();
						m_Client.getParams().setCookiePolicy(
								CookiePolicy.RFC_2965);

					}

					startGet = System.currentTimeMillis();
					sign = getHTML(curState, nextReq);
					// System.out.println("send");
					endGet = System.currentTimeMillis();

					if (sign == false) {
						EBStats.getEBStats().addErrorSession(curState, isVIP);
						// sessionEnd = System.currentTimeMillis();
						// EBStats.getEBStats().sessionRecorder(sessionStart,
						// sessionEnd, sessionLen, Ordered);
						initialize();
						continue;
					}

					first = false;
					// Compute and store Web Interaction Response Time (WIRT)
					EBStats.getEBStats().interaction(curState, startGet,
							endGet, tt, isVIP);
					sessionLen++;
					if (curState == 4) {
						Ordered = true;
					}
					curTrans.postProcess(this, html);
				} else {
					html = null;
					endGet = startGet;
				}
				if (!nextState())
					return;
				if (nextReq != null) {
					// Pick think time (TT), and compute absolute request time
					tt = MAP();
					startGet = endGet + tt;
					if (terminate || !this.test)
						return;
					try {
						sleep(tt);
					} catch (InterruptedException inte) {
						Thread.currentThread().interrupt();
						return;
					}
					if (maxTrans > 0)
						maxTrans--;
				} else {
					EBStats.getEBStats().addErrorSession(curState, isVIP);
					// sessionEnd = System.currentTimeMillis();
					// EBStats.getEBStats().sessionRecorder(sessionStart,
					// sessionEnd,
					// sessionLen, Ordered);
					initialize();
					continue;
				}
			}

		}
	}

	public void test() {

		long startGet;
		long endGet;
		long tt = 0L; // Think Time.
		boolean sign = true;
		startGet = System.currentTimeMillis();
		// session start.
		sessionStart = startGet;

		while ((maxTrans == -1) || (maxTrans > 0)) {

			if (this.terminate || !this.test) {
				sessionEnd = System.currentTimeMillis();
				EBStats.getEBStats().sessionRecorder(sessionStart, sessionEnd,
						sessionLen, Ordered, isVIP);
				return;
			}
			if (nextReq != null) {
				// Check if user session is finished.
				if (toHome) {
					// User session is complete. Start new user session.
					sessionEnd = System.currentTimeMillis();
					EBStats.getEBStats().sessionRecorder(sessionStart,
							sessionEnd, sessionLen, Ordered, isVIP);
					initialize();
					return;
				}
				if (nextReq.equals("")) {
					EBStats.getEBStats().addErrorSession(curState, isVIP);
					// sessionEnd = System.currentTimeMillis();
					// EBStats.getEBStats().sessionRecorder(sessionStart,
					// sessionEnd, sessionLen, Ordered);
					initialize();
					continue;
				}
				// Receive HTML response page.

				if (rate > 0) {
					if (isVIP) {
						if (nextReq.contains("?"))
							nextReq = nextReq + "&bench4q_session_priority=10";
						else {
							nextReq = nextReq + "?bench4q_session_priority=10";
						}
					} else {
						if (nextReq.contains("?"))
							nextReq = nextReq + "&bench4q_session_priority=1";
						else {
							nextReq = nextReq + "?bench4q_session_priority=1";
						}
					}

				}
				if (first) {
					m_Client = HttpClientFactory.getInstance();
					m_Client.getParams().setCookiePolicy(CookiePolicy.RFC_2965);

				}

				startGet = System.currentTimeMillis();
				sign = getHTML(curState, nextReq);
				// System.out.println("send");
				endGet = System.currentTimeMillis();

				if (sign == false) {
					EBStats.getEBStats().addErrorSession(curState, isVIP);
					// sessionEnd = System.currentTimeMillis();
					// EBStats.getEBStats().sessionRecorder(sessionStart,
					// sessionEnd, sessionLen, Ordered);
					initialize();
					continue;
				}

				first = false;
				// Compute and store Web Interaction Response Time (WIRT)
				EBStats.getEBStats().interaction(curState, startGet, endGet,
						tt, isVIP);
				sessionLen++;
				if (curState == 4) {
					Ordered = true;
				}
				curTrans.postProcess(this, html);
			} else {
				html = null;
				endGet = startGet;
			}
			if (!nextState())
				return;
			if (nextReq != null) {
				// Pick think time (TT), and compute absolute request time
				tt = MAP();
				startGet = endGet + tt;
				if (terminate || !this.test)
					return;
				try {
					sleep(tt);
				} catch (InterruptedException inte) {
					Thread.currentThread().interrupt();
					return;
				}
				if (maxTrans > 0)
					maxTrans--;
			} else {
				EBStats.getEBStats().addErrorSession(curState, isVIP);
				// sessionEnd = System.currentTimeMillis();
				// EBStats.getEBStats().sessionRecorder(sessionStart,
				// sessionEnd,
				// sessionLen, Ordered);
				initialize();
				continue;
			}
		}
	}

	/**
	 * initialize session info.
	 */
	public void initialize() {
		// initialize session info.
		curState = 0;
		nextReq = null;
		html = null;
		prevHTML = null;
		cid = ID_UNKNOWN;
		sessionID = null;
		shopID = ID_UNKNOWN;
		fname = null;
		lname = null;

		// initialize session record info.
		sessionStart = 0;
		sessionEnd = 0;
		sessionLen = 1;
		Ordered = false;
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
