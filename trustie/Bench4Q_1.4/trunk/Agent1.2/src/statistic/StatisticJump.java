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
import java.util.Random;
/**
 * 根据脚本构造一个二维跳转矩阵，实现依概率跳转
 * @author WU Yulong
 *
 */
public class StatisticJump {
	private double[][] m_statTable;
	/**
	 * 按script中主页的出现顺序存储其在
	 */
	protected ArrayList<Integer> scriptPageTrace;
	public StatisticJump() {
		scriptPageTrace = new ArrayList<Integer>(128);
	}
	/**
	 * 完成相关数据的初始化工作
	 * @param scriptPage 脚本读取后唯一性存储的“主页”URL
	 * @param scriptState 以脚本中出现顺序存储当前交互所属主页在scriptPage中的下标
	 */
	public void init(ArrayList<String> scriptPage,
			ArrayList<Integer> scriptState) {
		initTableStruc(scriptPage.size());
		initTrace(scriptState);
		initStaticTable();
	}
	/**
	 * 根据当前所在主页的下标，依脚本概率获取下一跳的index
	 * @param index 当前所在主页的下标
	 * @return 下一跳的index
	 */
	public int getNextJump(int index) {
		//生成一个0-1之间的伪随机数
		Random rnd = new Random(System.currentTimeMillis());
		double rand = rnd.nextDouble();
		
		if(rand < m_statTable[index][0]) {
			return 0;
		}
		double prev = m_statTable[index][0];
		for(int j=1; j<m_statTable[index].length; j++) {
			if(m_statTable[index][j]==0.0) 
				continue;
			if(prev <= rand && rand < prev + m_statTable[index][j]) {
				return j;
			}
			else {
				prev += m_statTable[index][j];
			}
		}
		try {
			throw new Exception("Data or logical Error.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	/**
	 * 为m_statTable开辟空间
	 * @param size 二维空间size
	 */
	protected void initTableStruc(int size) {
		m_statTable = new double[size][size];
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				m_statTable[i][j] = 0.0; 
			}
		}
	}
	/**
	 * 根据scriptState构造脚本中的主页执行路径
	 * @param scriptState
	 */
	private void initTrace(ArrayList<Integer> scriptState) {
		int prev=-1, index=-1;
		Iterator<Integer> k = scriptState.iterator();
		index = prev = k.next();
		scriptPageTrace.add(index);
		while(k.hasNext()) {
			index = k.next();
			if(index != prev) {
				scriptPageTrace.add(index);
				prev = index;
			}
		}
	}
	/**
	 * 初始化跳转概率存储结构m_statTable
	 */
	protected void initStaticTable() {
		int prev = -1, index = -1;
		Iterator<Integer> k = scriptPageTrace.iterator();
		prev = k.next();
		while(k.hasNext()) {
			index = k.next();
			m_statTable[prev][index]++;
			prev = index;
		}
		//为了形成一个闭合路径，当最后一个主页没有跳转到其他主页的记录时，会人工加入一个跳转到第一个主页的记录
		breakFor: {
			for(int i=0; i<m_statTable[m_statTable.length-1].length; i++) {
				if(m_statTable[m_statTable.length-1][i]!=0.0)
					break breakFor;
			}
			m_statTable[m_statTable.length-1][0] = 1.0;
		}
		convertTable();
	}
	/**
	 * 将m_statTable中的计数转化为概率
	 */
	private void convertTable() {
		for(int i=0; i<m_statTable.length; i++) {
			double sum=0;
			for(int j=0; j<m_statTable[i].length; j++) {
				sum += m_statTable[i][j];
			}
			for(int j=0; j<m_statTable[i].length; j++) {
				if(m_statTable[i][j]!=0)
					m_statTable[i][j] /= sum;
			}
		}
	}
}
