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

/**
 * <p>
 * <strong>Persistent Module</strong>
 * persist Agent data with CSV format
 * </p>
 */
package src.storage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
/**
 * <strong>The Main class of Persistent Module</strong>
 * <p>
 * Used to initialize file stream and defined the CSV store format.
 * Make StorageThread object restored in file.
 * </p>
 * @version 1.1 build in 20110823
 */
public class StoFile {
	String curDir;
	static DataStorage ds;

	/**
	 * the file stream to write file of throughput
	 */
	DataOutputStream fileThroughput;
	/**
	 * the file stream to write file of response time
	 */
	DataOutputStream fileResponseTime;
	/**
	 * the file stream to write file of response time of main page which exclude .img .css and so on
	 */
	DataOutputStream fileResponseTime_SS;
	/**
	 * singleton variable, the instance of this class, shared by EB threads
	 */
	private volatile static StoFile uniqueInstance;
	
	//added on 20110906, for a real time offset in response time file
	int respOffset=0;
	int respOffset_SS=0;
	public static StoFile getInstance() {
		if (uniqueInstance == null) {
			synchronized (StoFile.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new StoFile();
				}
			}
		}
		return uniqueInstance;
	}
	/**
	 * persistent process
	 * @param ds the structure going to be persisted
	 */
	public void sto(final DataStorage ds) {
		StoFile.ds = ds;
		StoThread st = new StoThread(this);
		st.start();
	}
	public void sto4Thread() {
		stoThroughput();
		stoResponseTime();
	}
	/**
	 * <strong>Construction Method</strong>: initial file variable
	 */
	private StoFile() {
		String separator;
		InetAddress addr;
		File f_1;
		File f_2;
		File f_3;
		FileOutputStream s_fileThroughput;
		FileOutputStream s_fileResponseTime;
		FileOutputStream s_fileResponseTiem_SS;
		curDir = System.getProperty("user.dir");
		separator = System.getProperty("file.separator");
		//added at 20110901, added IP address in file name.
		try{ 
			addr = InetAddress.getLocalHost(); 
			String sAddr = addr.toString();
			System.out.println(sAddr);
			sAddr = sAddr.substring(sAddr.indexOf('/')+1);
			System.out.println(sAddr);
			String throughputFileDir = curDir + separator + "throughput_"+sAddr+".csv";
			String responseTimeDir = curDir + separator + "responseTime_"+sAddr+".csv";
			String responseTimeDirIM = curDir + separator + "responseTimeSmallSize_"+sAddr+".csv";

//			String throughputFileDir = curDir + separator + "throughput.csv";
//			String responseTimeDir = curDir + separator + "responseTime.csv";
//			String responseTimeDirIM = curDir + separator + "responseTimeSmallSize.csv";

			f_1 = new File(throughputFileDir);
			f_2 = new File(responseTimeDir);
			f_3 = new File(responseTimeDirIM);
			try {
				//modified by wuyulong, change file output mode to append 
				s_fileThroughput = new FileOutputStream(f_1,true);
				s_fileResponseTime = new FileOutputStream(f_2,true);
				s_fileResponseTiem_SS = new FileOutputStream(f_3,true);
				fileThroughput = new DataOutputStream(s_fileThroughput);
				fileResponseTime = new DataOutputStream(s_fileResponseTime);
				fileResponseTime_SS = new DataOutputStream(s_fileResponseTiem_SS);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(Exception   e){
			e.printStackTrace();
		} 
	}
	/**
	 * the method that store throughput to corresponding file in .csv format  
	 */
	private void stoThroughput() {
		try {
			for(int i=0;i<ds.webIThroughput.length; i++) {
				for(int j=0; j<ds.webIThroughput[i].length; j++) {
					String s = ""+ds.webIThroughput[i][j];
					fileThroughput.writeChars(s+",");
				}
				fileThroughput.writeChars("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * the method that store response time to corresponding file in .csv format
	 */
	private void stoResponseTime() {
		try {
			for(int i=0; i<ds.webIResponseTime.length; i++) {
				for(int j=0; j<ds.webIResponseTime[i].length; j++) {
					for(Iterator<Double> k=ds.webIResponseTime[i][j].iterator(); k.hasNext();) 
					{
						fileResponseTime.writeChars((respOffset+i) + "," + j + "," + k.next() + "\n");
					}
				}
			}
			for(int i=0; i<ds.webIResponseTime_SS.length; i++) {
				for(int j=0; j<ds.webIResponseTime_SS[i].length; j++) {
					for(Iterator<Double> k=ds.webIResponseTime_SS[i][j].iterator(); k.hasNext();) 
					{
						fileResponseTime_SS.writeChars((respOffset_SS+i) + "," + j + "," + k.next() + "\n");
					}
				}
			}
			respOffset+=ds.webIResponseTime.length;
			respOffset_SS+=ds.webIResponseTime_SS.length;
			System.out.println("respOffset:"+respOffset+" respOffset_SS:"+respOffset_SS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public void close() {
		try {
			fileThroughput.close();
			fileResponseTime.close();
			fileResponseTime_SS.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
/**
 * take store process in a independent thread to improve efficiency 
 * @author WU Yulong
 */
class StoThread extends Thread {
	private StoFile sd;
	public StoThread(StoFile sd) {
		this.sd = sd;
	}
	public void run() {
		sd.sto4Thread();
	}
}
