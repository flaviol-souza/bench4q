/**
 * =========================================================================
 * 					Bench4Q version 1.0.0
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

import org.bench4Q.agent.rbe.communication.Args;

public class EBReqPool {

	private ArrayList<EB_ReqOpen> pool;
	private Args m_args;
	private int ebnum;

	public EBReqPool(Args args) {
		pool = new ArrayList<EB_ReqOpen>();
		ebnum = 0;
		m_args = args;
	}

	public void initialize(int ebNum) {
		for (int i = 0; i < ebNum; i++) {
			ebnum++;
			EB_ReqOpen eb = new EB_ReqOpen(this, m_args, ebnum);
			eb.setDaemon(true);
			eb.start();
			pool.add(eb);
		}

	}

	public void addEB(int ebNum) {
		for (int i = 0; i < ebNum; i++) {
			ebnum++;
			EB_ReqOpen eb = new EB_ReqOpen(this, m_args, ebnum);
			eb.setDaemon(true);
			eb.start();
			pool.add(eb);
		}
	}

	public EB_ReqOpen getEB() {
		if (pool.isEmpty()) {
			addEB(10);
		}
		EB_ReqOpen eb = null;
		while (eb == null) {
			if (!pool.isEmpty()) {
				eb = pool.get(0);
				pool.remove(0);
			} else {
				addEB(10);
			}

		}
		return eb;
	}

	public void comeback(EB_ReqOpen eb) {
		pool.add(eb);
	}

	public void clean() {
		for (EB_ReqOpen eb : pool) {
			eb.stop();
		}
		pool = null;
	}

}
