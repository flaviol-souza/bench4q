/**
 * =========================================================================
 * 					Bench4Q_Script version 1.3.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at  
 * http://www.trustie.net/projects/project/show/Bench4Q
 * You can find latest version there. 
 * Bench4Q_Script adds a script module for Internet application to Bench4Q
 * http://www.trustie.com/projects/project/show/Bench4Q_Script
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
 *  * Initial developer(s): Wangsa , Tianfei , WUYulong , Zhufeng
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package scriptbq.monitor;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.swt.SWTException;

import scriptbq.BqConstant;
import scriptbq.tree.BqTreeObject;

/**
 * The class BqActionReport is used to redraw all the graph. It is the engine of
 * the monitoring. 
 */
public class BqActionReport extends Thread{
	
	/**
	 * The parent container
	 */
	private BqGraph graph;
	/**
	 * The delay time for thread
	 */
	private int delayTime;
	/**
	 * The begin time
	 */
	private long beginTime = 0;
	/**
	 * whether the thread is stopped
	 */
	private boolean isStopped;
	/**
	 * The corresponding agent
	 */
	private BqTreeObject agent;
	private int stdyTime;
	int throughput_all=0;
	int responsetime_all=0;
	int throughput_average;
	int responsetime_average;
	
	/**
	 * constructor
	 * @param graph
	 * @param agent
	 */
	public BqActionReport(BqGraph graph,BqTreeObject agent){
		this.graph = graph;
		this.isStopped = false;
		this.agent=agent;
		String tempPath = BqConstant.BqTreePath+"/"+this.agent.getParent().getName()+"/config.xml";
		File tmp = new File(tempPath);
		if(tmp.exists()){
			DialogSettings ConfigSetting = new DialogSettings(null);
			try {
				ConfigSetting.load(tempPath);
				stdyTime= Integer.valueOf(ConfigSetting.get("stdyTime")).intValue();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(stdyTime<=300){
			graph.getCanvasLeft().setTimeFrame(stdyTime*1000);
			graph.getCanvasRight().setTimeFrame(stdyTime*1000);
		}
	}
	
	/**
	 * Method to set the delay time of the thread.
	 * @param delayTime
	 */
	public void setDelay(int delayTime){
		this.delayTime = delayTime;
	}
	
	/**
	 * Method to stop the thread
	 */
	public synchronized void stopThread(){
	    isStopped=true;
	    notify();
	}
	
	/**
	 * The run method of the thread
	 */
	public void run(){
		//agent.reset();
		beginTime = System.currentTimeMillis();
		try {
			agent.getRMI().getStarted();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		while(!graph.isDisposed()&&!isStopped){
			int throughput=0;
			int responseTime=0;
			int totalTime = (int) (((System.currentTimeMillis() - beginTime)/1000)*1000);
			
			if(totalTime>stdyTime*1000){
				 try {
          			agent.getSsh().Command("pkill java");
          		} catch (IOException e) {
          			e.printStackTrace();
          		}
				 throughput_average=throughput_all/stdyTime;
				 responsetime_average=responsetime_all/stdyTime;
				 graph.getCanvasLeft().setResult("throughput", throughput_average);
				 graph.getCanvasRight().setResult("responsetime", responsetime_average);
				 graph.addPointRight(totalTime, responseTime);
					try{
							graph.getDisplay().syncExec(new Runnable(){
								public void run() {
									graph.getCanvasLeft().redraw();	
									graph.getCanvasRight().redraw();
								}		
							});
					}catch(SWTException e) {
		                    return;
		            }
				break;
			}
			try {
				throughput=agent.getRMI().getThroughput();
				//System.out.println(throughput);
				responseTime=(int)agent.getRMI().getResponsetime();
				throughput_all+=throughput;
				responsetime_all+=responseTime;
			} catch (RemoteException e1) {
				//e1.printStackTrace();
				agent.reset();
				try {
					agent.getRMI().getStarted();
					throughput=agent.getRMI().getThroughput();
					responseTime=(int)agent.getRMI().getResponsetime();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			graph.addPointLeft(totalTime, throughput);
			graph.addPointRight(totalTime, responseTime);
			try{
					graph.getDisplay().syncExec(new Runnable(){
						public void run() {
							graph.getCanvasLeft().redraw();	
							graph.getCanvasRight().redraw();
						}		
					});
			}catch(SWTException e) {
                    return;
            }
			try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		}
		synchronized (this) {
            try {
                if(!isStopped) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}
}
