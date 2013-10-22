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
package src;

import java.util.ArrayList;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import src.communication.Args;
import src.statistic.StatisticJump;
import src.storage.StorageThread;

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
	
	private StatisticJump statJump;

	/**
	 * @brief Initialize by the bookstore application
	 * @param args
	 */
	public EBClosed(Args args, ArrayList<Integer> trace){
		//System.out.println("create");
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


		m_trace = trace;
		it = m_trace.iterator();

		//Question1: Are the 3 variables below duplicated?
		tt_scale = this.m_args.getThinktime();
		tolerance_scale = this.m_args.getTolerance();

		//Question2: 既然static变量,为何还要在具体的构造函数中赋值呢?这样岂不失去了static的意义?
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
			System.out.println("shopping");
			trans = mix.getTrans("shopping");
		} else if (mixType.equals("ordering")) {
			transProb = mix.getTransProb("ordering");
			trans = mix.getTrans("ordering");
		} else {
			System.out.println("EB initiate mix ERROR");
		}
		
		//初始化httpclient
				m_Client = HttpClientFactory.getInstance();
				m_Client.getParams().setCookiePolicy(CookiePolicy.RFC_2965);
				Cookie cookie=new Cookie();
				cookie.setDomain("AgentCookie4Server");
				cookie.setPath("/");
				cookie.setName(m_args.getTenant());
				HttpState initialState = new HttpState();
				initialState.addCookie(cookie);
				m_Client.setState(initialState);
	}

	/**
	 * 根据脚本初始化
	 * @param args
	 * @param script 脚本路径
	 */
	public EBClosed(Args args, String script, int num){
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
		threadNum = num;

		tt_scale = this.m_args.getThinktime();
		tolerance_scale = this.m_args.getTolerance();

		params=new String[5];
		
		//初始化httpclient
		m_Client = HttpClientFactory.getInstance();
		m_Client.getParams().setCookiePolicy(CookiePolicy.RFC_2965);
		Cookie cookie=new Cookie();
		cookie.setDomain("AgentCookie4Server");
		cookie.setPath("/");
		cookie.setName(m_args.getTenant());
		HttpState initialState = new HttpState();
		initialState.addCookie(cookie);
		m_Client.setState(initialState);

	}
	public void run() {
		while (!this.terminate) {
			if (test) {
				isVIP = rand.nextDouble() < rate ? true : false;
				test();
				if(!m_args.isScript())
				{
					m_Client.getState().clearCookies();
				}
			} else {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
				}
			}
		}
		StorageThread.getUniq().updateCancelCount();
	}

	/**
	 * 
	 */
	public void test() {

		long startGet;
		long endGet;
		long tt = 0L; // Think Time.
		boolean sign = true;
		startGet = System.currentTimeMillis();
		// session start time.
		sessionStart = startGet;

		while ((maxTrans == -1) || (maxTrans > 0)) {

			if(System.currentTimeMillis()>=StorageThread.getM_endTime()){
				return;
			}
			if (this.terminate || !this.test) {
				sessionEnd = System.currentTimeMillis();
				//EBStats.getEBStats().sessionRecorder(sessionStart, sessionEnd,
				//		sessionLen, Ordered, isVIP);
				return;
			}
			if (nextReq != null) {
				// Check if user session is finished.
				if (toHome) {
					// User session is complete. Start new user session.
					sessionEnd = System.currentTimeMillis();
					//EBStats.getEBStats().sessionRecorder(sessionStart,
					//		sessionEnd, sessionLen, Ordered, isVIP);
					initialize();
					return;
				}
				if (nextReq.equals("")) {
					//EBStats.getEBStats().addErrorSession(curState, isVIP);
					initialize();
					continue;
				}
				// Receive HTML response page.
				if(m_args.isScript()){
					start= System.currentTimeMillis();
					while(m_args.getscriptType().get(scriptStep%(m_args.getscriptURL().size())).equals("others")){
						//added by wuyulong at 20110901, for thinkTime
						String sleepTime = m_args.getscriptSleep().get(scriptStep%(m_args.getscriptURL().size()));
						try {
							sleep(Integer.parseInt(sleepTime));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						startGet = System.currentTimeMillis();
						//if time is up,quit without delay
						if(startGet>=StorageThread.getM_endTime()){
							return;
						}
						sign=getHTML(curState,params);
						endGet = System.currentTimeMillis();
						if (sign == false) {
							//EBStats.getEBStats().addErrorSession(curState, isVIP);
							initialize();
							continue;
						}
						// Compute and store Web Interaction Response Time (WIRT)
						StorageThread.getUniq().responsetimeRecorder(curState, startGet, endGet, false);
						StorageThread.getUniq().throughtputRecorder(curState, endGet);
						//EBStats.getEBStats()
						//.interaction(curState, startGet, endGet, tt, isVIP);
						sessionLen++;
						if(!nextState())
							return;
					}
					//added by wuyulong at 20110901, for thinkTime
					/*String sleepTime = m_args.getscriptSleep().get(scriptStep%(m_args.getscriptURL().size()));
					try {
						sleep(Integer.parseInt(sleepTime));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}*/
					startGet = System.currentTimeMillis();
					sign=getHTML(curState,params);
					endGet = System.currentTimeMillis();
					end= System.currentTimeMillis();
					if (sign == false) {
						//EBStats.getEBStats().addErrorSession(curState, isVIP);
						initialize();
						continue;
					}
					// Compute and store Web Interaction Response Time (WIRT)
					StorageThread.getUniq().responsetimeRecorder(curState, startGet, endGet, false);
					StorageThread.getUniq().throughtputRecorder(curState, endGet);
//					EBStats.getEBStats().interaction(curState, startGet, endGet, tt, isVIP);
					sessionLen++;
					//EBStats.getEBStats().responsetimeRecorder(m_args.getscriptState().get((scriptStep-1)%(m_args.getscriptURL().size())),startTime,endTime,true);
//					StorageThread.getUniq().responsetimeRecorder(m_args.getscriptState().get((scriptStep-1)%(m_args.getscriptURL().size())), startTime, endTime);
					StorageThread.getUniq().responsetimeRecorder(curState, start, end, true);
				}
				else{
						//not script
					startGet = System.currentTimeMillis();
					sign = getHTML(curState, nextReq);
					endGet = System.currentTimeMillis();
					if (sign == false) {
						//EBStats.getEBStats().addErrorSession(curState, isVIP);
						initialize();
						continue;
					}
					// Compute and store Web Interaction Response Time (WIRT)
					StorageThread.getUniq().responsetimeRecorder(curState, startGet, endGet, false);
					StorageThread.getUniq().throughtputRecorder(curState, endGet);
					//EBStats.getEBStats()
					//.interaction(curState, startGet, endGet, tt, isVIP);
					sessionLen++;
					if (curState == 4) {
						Ordered = true;
					}
					curTrans.postProcess(this, html);
				}
			} //if (nextReq != null) end
			else {
				html = null;
				endGet = startGet;
			}
			if(!nextState())
				return;
			if (nextReq != null) {
				// Pick think time (TT), and compute absolute request time
				if(!m_args.isScript()){
					tt = thinkTime();
				}
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
			} //end if (nextReq != null)
			else {
				//EBStats.getEBStats().addErrorSession(curState, isVIP);
				initialize();
				continue;
			}
		}
	}
	/**
	 * @brief reset the session. 
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
