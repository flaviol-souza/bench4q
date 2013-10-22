/**
 * =========================================================================
 * 					Bench4Q_Script version 1.3.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://www.trustie.net/projects/project/show/Bench4Q
 * You can find latest version there. 
 * 
 * Bench4Q_Script adds a script module for Internet application to Bench4Q
 * you can access it at http://www.trustie.com/projects/project/show/Bench4Q_Script
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
 *  * Initial developer(s): WuYulong, Wangsa , Tianfei , Zhufeng
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package src.statistic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * a temporary implement, an adapter to fit the transform process in EBClosed
 */
public class StaticJumpAdapter extends StatisticJump{
	/**
	 * the suffix of it correspond to the No. of request in script, the value correspond to the No. of the main page in scriptPageTrace
	 * 下标对应于每个页面（主页和非主页）的在script中的序号，值对应于该页面所属的主页在scriptPageTrace中的序号
	 */
	private ArrayList<Integer> scriptState4Trace;
	public StaticJumpAdapter() {
		super();
		scriptState4Trace = new ArrayList<Integer>(128);
	}
	@Override
	public synchronized int getNextJump(int index) {
		int index1 = scriptState4Trace.get(index);
		int index2 = super.getNextJump(index1);
		//获得对应的序号为index2的脚本的第一个请求在scriptState中的下标
		//get the suffix in scriptState of the first request whose No. equals to index2
		int count=0;
		for(ListIterator<Integer> k=scriptState4Trace.listIterator();k.hasNext();count++) {
			if(k.next()==index2) {
				return count;
			}
		}
		try {
			throw new Exception("Data or logical Error.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	@Override
	public void init(ArrayList<String> scriptPage,
			ArrayList<Integer> scriptState) {
		super.initTableStruc(scriptPage.size());
		//change to use a new initTrace method
		initTrace(scriptState);
		super.initStaticTable();
	}
	/**
	 * override the same name method in StatisticJump, added the initialization of scriptState4Trace
	 * 重写了StatisticJump中的同名方法，加入了scriptState4Trace的初始化
	 * construct the execute trace by scirptState
	 * 根据scriptState构造脚本中的主页执行路径
	 * @param scriptState
	 */
	private void initTrace(ArrayList<Integer> scriptState) {
		int prev=-1, index=-1;
		Iterator<Integer> k = scriptState.iterator();
		index = prev = k.next();
		super.scriptPageTrace.add(index);
		scriptState4Trace.add(super.scriptPageTrace.size()-1);
		while(k.hasNext()) {
			index = k.next();
			if(index != prev) {
				super.scriptPageTrace.add(index);
				prev = index;
			}
			scriptState4Trace.add(super.scriptPageTrace.size()-1);
		}
	}
	
}
