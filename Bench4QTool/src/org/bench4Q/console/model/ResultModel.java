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
package org.bench4Q.console.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bench4Q.agent.rbe.communication.EBError;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.communication.ErrorSet;
import org.bench4Q.agent.rbe.communication.ResultSet;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.rm.ServerInfo;
import org.bench4Q.console.ui.transfer.AgentInfo;
import org.bench4Q.console.ui.transfer.AgentInfoObserver;
import org.bench4Q.console.ui.transfer.AgentsCollection;

/**
 * @author duanzhiquan
 * 
 */
public class ResultModel implements AgentInfoObserver {

	private File m_selectedFile;
	private AgentsCollection m_agentsCollection;
	private ArrayList<AgentInfo> m_result;
	private ArrayList<EBStats> m_stats;
	private final Resources m_resources;

	// used to record test info
	private int[] loadStart;
	private long startTime;
	private long prepairTime;
	private long testTime;
	private long endTime;
	private int m_testduring;

	// used to record request info
	private int[][] trans = new int[15][15];
	private int[][] webInteractionThroughput;
	private int[][] webInteractionThroughput_vip;
	private int[][] webInteractionThroughput_norm;

	private ResultSet[] wirt = new ResultSet[15];
	private ResultSet[] wirt_vip = new ResultSet[15];
	private ResultSet[] wirt_norm = new ResultSet[15];

	private ResultSet[] tt = new ResultSet[15];
	private ResultSet[] tt_vip = new ResultSet[15];
	private ResultSet[] tt_norm = new ResultSet[15];

	private double WIPS = 0.0D;
	private double WIPS_VIP = 0.0D;
	private double WIPS_NORM = 0.0D;
	private double WIRT_AVG = 0.0D;
	private double WIRT_VIP_AVG = 0.0D;
	private double WIRT_NORM_AVG = 0.0D;
	private double WIRT_95 = 0.0D;
	private double WIRT_VIP_95 = 0.0D;
	private double WIRT_NORM_95 = 0.0D;

	// used to record session info
	private int[][] session;
	private ArrayList<Integer> sessionLen = new ArrayList<Integer>();
	private ArrayList<Integer> sessionLen_vip = new ArrayList<Integer>();
	private ArrayList<Integer> sessionLen_norm = new ArrayList<Integer>();
	private int OrderedSession = 0;
	private int[] ErrorSession = new int[15];

	// others
	private double TotalProfit = 0.0D;
	private int errorCnt = 0;
	private int errorCnt_vip = 0;
	private int errorCnt_norm = 0;
	private ErrorSet[] errors = new ErrorSet[15];
	private ErrorSet[] errors_vip = new ErrorSet[15];
	private ErrorSet[] errors_norm = new ErrorSet[15];
	private Map<String, ServerInfo> m_MultiServers = new HashMap<String, ServerInfo>();
	private ArrayList<serverMon> m_serverMon = new ArrayList<serverMon>();
	private int VIPrate;
	private int time = -1;
	private DecimalFormat df = new DecimalFormat("0.0");

	static enum type {
		all, normal, vip;
	}

	private double[] trans_avg_res = new double[15];
	private double[] trans_avg_thp = new double[15];
	private double[] trans_avg_ratio = new double[15];

	/**
	 * @param resources
	 */
	public ResultModel(Resources resources) {
		this.m_resources = resources;
		this.m_result = new ArrayList<AgentInfo>();

		this.m_stats = new ArrayList<EBStats>();

		for (int i = 0; i < 15; i++) {
			this.wirt[i] = new ResultSet();
			this.tt[i] = new ResultSet();
			this.errors[i] = new ErrorSet();
		}
	}

	/**
	 * @param agentsCollection
	 * @param resources
	 */
	public ResultModel(AgentsCollection agentsCollection, Resources resources) {
		this(resources);
		this.m_agentsCollection = agentsCollection;
		this.m_agentsCollection.registerObserver(this);

	}

	/**
	 * save test result to the selected file.
	 */
	public void SaveToFile() {
		CalTotalResult();

		try {
			FileWriter outstream = new FileWriter(this.m_selectedFile);
			
			String nameS = m_selectedFile.getAbsolutePath();
			String lastname;
			if(nameS.endsWith(".bq")){
				nameS = nameS.substring(0, nameS.length()-3);
			}
			lastname = nameS.concat(".csv");
			File file = new File(lastname);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			FileWriter outstreamCSV = new FileWriter(file);


			outstream.write("bench4Q test result\n\n");

			outstream
					.write("****************************************************\n");

			outstream.write("The Total Statistics\n");

			outstream.write("WIPS:\t" + this.WIPS + "\n");

			outstream.write("WIRT average:\t" + this.WIRT_AVG + "\n");

			outstream.write("WIRT 95%:\t" + this.WIRT_95 + "\n");

			outstream.write("Complete Session:\t" + this.sessionLen.size()
					+ "\n");

			outstream.write("Error Session:\t" + this.errorCnt + "\n");
			if (!this.sessionLen_vip.isEmpty()) {

				outstream
						.write("****************************************************\n");

				outstream.write("The VIP customers Statistics\n");

				outstream.write("WIPS:\t" + this.WIPS_VIP + "\n");

				outstream.write("WIRT average:\t" + this.WIRT_VIP_AVG + "\n");

				outstream.write("WIRT 95%:\t" + this.WIRT_VIP_95 + "\n");

				outstream.write("Complete Session:\t"
						+ this.sessionLen_vip.size() + "\n");

				outstream.write("Error Session:\t" + this.errorCnt_vip + "\n");
			}
			if (!this.sessionLen_norm.isEmpty()) {
				outstream
						.write("****************************************************\n");

				outstream.write("The normal customers Statistics\n");

				outstream.write("WIPS:\t" + this.WIPS_NORM + "\n");

				outstream.write("WIRT average:\t" + this.WIRT_NORM_AVG + "\n");

				outstream.write("WIRT 95%:\t" + this.WIRT_NORM_95 + "\n");

				outstream.write("Complete Session:\t"
						+ this.sessionLen_norm.size() + "\n");

				outstream.write("Error Session:\t" + this.errorCnt_norm + "\n");
			}
			outstream
					.write("****************************************************\n");

			Iterator it = this.m_serverMon.iterator();
			while (it.hasNext()) {
				serverMon sMon = (serverMon) it.next();

				outstream.write(sMon.address + "\n");
				outstream.write("CPU Average Usage:\t" + sMon.CPU_avg + "\n");
				outstream.write("Memory average Usage:\t" + sMon.memory_avg
						+ "\n");
			}
			String[] name = { new String("INIT"), new String("ADMC"),
					new String("ADMR"), new String("BESS"), new String("BUYC"),
					new String("BUYR"), new String("CREG"), new String("HOME"),
					new String("NEWP"), new String("ORDD"), new String("ORDI"),
					new String("PROD"), new String("SREQ"), new String("SRES"),
					new String("SHOP") };
			
			outstreamCSV.write("Transaction name,Response Time,Throughput,Ratio\n");
			for (int i = 1; i < 15; i++) {
				String out = String.format("trans " + name[i] + "          " + "%-10.1f ms\t\t" + "%-10.1f\t\t" +"%-3.1f %%\n", trans_avg_res[i], trans_avg_thp[i], trans_avg_ratio[i]);
				outstreamCSV.write("trans " + name[i] + "," + trans_avg_res[i] + "," + trans_avg_thp[i] + "," + trans_avg_ratio[i] + "\n");
				outstream.write(out);
			}
			outstreamCSV.close();
			
			outstream
					.write("****************************************************\n");
			outstream.write("ERROR: \n");
			int totalerror = 0;
			for (int i = 0; i < 15; i++) {
				outstream.write("trans " + name[i] + " : ");
				int errornum = 0;
				for (EBError error : this.errors[i].getResult()) {
					if (error != null) {
						outstream.write(error.toString() + "\n");
						errornum++;
					}
				}
				outstream.write(errornum + "\n");
				totalerror += errornum;
			}
			outstream.write("Error Number: " + totalerror + "\n");
			outstream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			restartTest();
		}
	}

	private void CalTotalResult() {

		for (EBStats stat : m_stats) {
			// get total wips of all agents
			if (time == -1) {
				this.m_testduring = stat.getTestduring();
				this.webInteractionThroughput = new int[15][(int) (this.m_testduring + 1)];
				this.webInteractionThroughput_vip = new int[15][(int) (this.m_testduring + 1)];
				this.webInteractionThroughput_norm = new int[15][(int) (this.m_testduring + 1)];
				this.session = new int[2][(int) (this.m_testduring + 1)];
				this.time++;
				stat.getTestduring();
			}
			int[][] wips = stat.getWebInteractionThroughput();
			int[][] wips_vip = stat.getWebInteractionThroughput_vip();
			int[][] wips_norm = stat.getWebInteractionThroughput_norm();
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < (this.m_testduring + 1); j++) {
					this.webInteractionThroughput[i][j] += wips[i][j];
					this.webInteractionThroughput_vip[i][j] += wips_vip[i][j];
					this.webInteractionThroughput_norm[i][j] += wips_norm[i][j];
				}
			}

			// get total wirt of all agents
			ResultSet[] wirtOfAgent = stat.getWirt();
			ResultSet[] wirtofAgent_vip = stat.getWirt_vip();
			ResultSet[] wirtofAgent_norm = stat.getWirt_norm();
			for (int i = 0; i < 15; i++) {
				if (this.wirt[i] == null) {
					this.wirt[i] = new ResultSet();
				}
				if (this.wirt_vip[i] == null) {
					this.wirt_vip[i] = new ResultSet();
				}
				if (this.wirt_norm[i] == null) {
					this.wirt_norm[i] = new ResultSet();
				}
				this.wirt[i].getResult().addAll(wirtOfAgent[i].getResult());
				this.wirt_vip[i].getResult().addAll(wirtofAgent_vip[i].getResult());
				this.wirt_norm[i].getResult().addAll(wirtofAgent_norm[i].getResult());
			}

			// get total errors of all agents
			ErrorSet[] errorOfAgent = stat.getErrors();
			ErrorSet[] error_vip = stat.getErrors_vip();
			ErrorSet[] error_norm = stat.getErrors_norm();
			for (int i = 0; i < 15; i++) {
				if (this.errors[i] == null) {
					this.errors[i] = new ErrorSet();
				}
				if (this.errors_vip[i] == null) {
					this.errors_vip[i] = new ErrorSet();
				}
				if (this.errors_norm[i] == null) {
					this.errors_norm[i] = new ErrorSet();
				}
				this.errors[i].getResult().addAll(errorOfAgent[i].getResult());
				this.errors_vip[i].getResult().addAll(error_vip[i].getResult());
				this.errors_norm[i].getResult().addAll(
						error_norm[i].getResult());
			}

			// count errors
			for (int i = 0; i < this.errors.length; i++) {
				this.errorCnt += this.errors[i].getResult().size();
			}
			for (int i = 0; i < this.errors_vip.length; i++) {
				this.errorCnt_vip += this.errors_vip[i].getResult().size();
			}
			for (int i = 0; i < this.errors_norm.length; i++) {
				this.errorCnt_norm += this.errors_norm[i].getResult().size();
			}

			// get all trans matrix
			int[][] transOfAgent = stat.getTrans();
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					this.trans[i][j] += transOfAgent[i][j];
				}
			}

			// get total sps of all agents
			int[][] sessionTem = stat.getSession();
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < (this.m_testduring + 1); j++) {
					this.session[i][j] += sessionTem[i][j];
				}
			}

			// get total session Length.
			this.sessionLen.addAll(stat.getSessionLen());
			this.sessionLen_vip.addAll(stat.getSessionLen_vip());
			this.sessionLen_norm.addAll(stat.getSessionLen_norm());

			// get total session Length.
			this.OrderedSession += stat.getOrderedSession();

			// get total error session.
			int[] ErrorSessionTem = stat.getErrorSession();
			for (int i = 0; i < 15; i++) {
				ErrorSession[i] += ErrorSessionTem[i];
			}

		}

		TransAveResponseCal();
		TransAvgThroughputCal();

		WIRTCal(wirt, type.all);
		WIRTCal(wirt_vip, type.vip);
		WIRTCal(wirt_norm, type.normal);

		// calculate WIPS
		// int[] wips = new int[m_testduring + 1];

		this.WIPS = WIPScal(this.webInteractionThroughput);
		this.WIPS_VIP = WIPScal(this.webInteractionThroughput_vip);
		this.WIPS_NORM = WIPScal(this.webInteractionThroughput_norm);

		if (!this.m_MultiServers.isEmpty()) {
			ServerCal();
		}

	}

	private void TransAvgThroughputCal() {
		int length = webInteractionThroughput[0].length;
		int sum;
		int total = 0;
		for (int j = 0; j < 15; j++) {
			sum = 0;
			for (int i = 0; i < length; i++)
				sum += webInteractionThroughput[j][i];
			trans_avg_thp[j] = sum;
			total += sum;
		}
		for (int i = 0; i < 15; i++) {
			trans_avg_ratio[i] = Double.parseDouble(df.format(trans_avg_thp[i]
					/ total * 100));
		}

	}

	public void addAgent(AgentInfo agentInfo) throws ConsoleException {
	}

	public void getResult(AgentInfo agentInfo) {
		this.m_result.add(agentInfo);
		this.m_stats.add(agentInfo.getStats());
	}

	public void removeAgent(AgentInfo agentInfo) {
	}

	public void restartTest() {
		this.time = -1;
		this.m_MultiServers = new HashMap<String, ServerInfo>();

		if (!this.m_serverMon.isEmpty()) {
			this.m_serverMon.clear();
		}
		this.m_stats.clear();
		this.m_result.clear();

		this.wirt = new ResultSet[15];
		this.wirt_vip = new ResultSet[15];
		this.wirt_norm = new ResultSet[15];

		this.tt = new ResultSet[15];
		this.tt_vip = new ResultSet[15];
		this.tt_norm = new ResultSet[15];

		this.WIPS = 0.0D;
		this.WIPS_VIP = 0.0D;
		this.WIPS_NORM = 0.0D;
		this.WIRT_AVG = 0.0D;
		this.WIRT_VIP_AVG = 0.0D;
		this.WIRT_NORM_AVG = 0.0D;
		this.WIRT_95 = 0.0D;
		this.WIRT_VIP_95 = 0.0D;
		this.WIRT_NORM_95 = 0.0D;

		// used to record session info
		this.session = null;
		this.sessionLen = new ArrayList<Integer>();
		this.sessionLen_vip = new ArrayList<Integer>();
		this.sessionLen_norm = new ArrayList<Integer>();
		this.OrderedSession = 0;
		this.ErrorSession = new int[15];

		// others
		TotalProfit = 0;
		errorCnt = 0;
		errorCnt_vip = 0;
		errorCnt_norm = 0;
		errors = new ErrorSet[15];
		errors_vip = new ErrorSet[15];
		errors_norm = new ErrorSet[15];

	}

	/**
	 * @return
	 */
	public File getSelectedFile() {
		return this.m_selectedFile;
	}

	/**
	 * @param file
	 */
	public void setSelectedFile(File file) {
		this.m_selectedFile = file;
	}

	/**
	 * @param collection
	 */
	public void setAgentsCollection(AgentsCollection collection) {
		this.m_agentsCollection = collection;
		this.m_agentsCollection.registerObserver(this);
	}

	public void WIRTCal(ResultSet[] m_wirt, type m_type) {
		ArrayList<Double> wirtList = new ArrayList<Double>();
		for (int i = 0; i < 15; i++) {
			wirtList.addAll(m_wirt[i].getResult());
		}

		if (wirtList.size() <= 0) {
			return;
		}
		for (int i = 0; i < wirtList.size(); i++) {
			if (wirtList.get(i) != 0)
				break;
			return;
		}
		double total = 0.0D;

		Double[] wirtOrderd = new Double[wirtList.size()];
		wirtList.toArray(wirtOrderd);
		Arrays.sort(wirtOrderd);

		long number = Double.valueOf(wirtOrderd.length * 0.95D).longValue();
		double percent = 0.0D;

		for (int i = 0; i < wirtOrderd.length; i++) {
			total += wirtOrderd[i].doubleValue();
			if (i == number) {
				percent = wirtOrderd[i].doubleValue();
			}
		}
		double average = total / wirtOrderd.length;
		DecimalFormat df = new DecimalFormat("0.0");
		average = Double.parseDouble(df.format(average));
		percent = Double.parseDouble(df.format(percent));

		if (m_type == type.all) {
			this.WIRT_AVG = average;
			this.WIRT_95 = percent;
		} else if (m_type == type.vip) {
			this.WIRT_VIP_AVG = average;
			this.WIRT_VIP_95 = percent;
		} else {
			this.WIRT_NORM_AVG = average;
			this.WIRT_NORM_95 = percent;
		}
	}

	public double WIPScal(int[][] webInteraction) {
		double m_wips = 0.0D;
		for (int i = 1; i < 15; i++) {
			for (int j = 0; j < this.m_testduring + 1; j++) {
				webInteraction[0][j] += webInteraction[i][j];
			}
		}
		
		int i;
		for (i = this.m_testduring; i >= 0; i--) {
			if (webInteraction[0][i] > 0) {
				break;
			}
		}

		for (int j = 0; j < i; j++) {
			m_wips += webInteraction[0][j];
		}

		if ((i - 1) > 0) {
			DecimalFormat df = new DecimalFormat("0.0");
			m_wips /= (i - 1);
			m_wips = Double.parseDouble(df.format(m_wips));
		} else {
			m_wips = 0.0D;
		}
		return m_wips;
	}

	@Override
	public void saveTheChart(String prefix) {
		// TODO Auto-generated method stub

	}

	public void insertData(Map<String, ServerInfo> MultiServers) {
		this.m_MultiServers.putAll(MultiServers);
	}

	public class serverMon {
		public String address;
		public double CPU_avg;
		public double memory_avg;

		public serverMon() {
			address = null;
			CPU_avg = 0;
			memory_avg = 0;
		}
	}

	public void ServerCal() {
		Iterator servIterator = this.m_MultiServers.entrySet().iterator();
		DecimalFormat df = new DecimalFormat("0.0");

		while (servIterator.hasNext()) {
			Map.Entry<String, ServerInfo> serverEntry = (Map.Entry<String, ServerInfo>) servIterator.next();
			serverMon serMon = new serverMon();
			serMon.address = ((String) serverEntry.getKey());
			ServerInfo sInfo = (ServerInfo) serverEntry.getValue();
			double CPU_avg = 0.0D;
			for (int i = 0; i < sInfo.getCpu_ratio().size(); i++) {
				CPU_avg += ((Double) sInfo.getCpu_ratio().get(i)).doubleValue();
			}
			CPU_avg /= sInfo.getCpu_ratio().size();
			serMon.CPU_avg = Double.parseDouble(df.format(CPU_avg));

			double Mem_avg = 0.0D;
			for (int i = 0; i < sInfo.getMemory_usage().size(); i++) {
				Mem_avg += ((Double) sInfo.getMemory_usage().get(i))
						.doubleValue();
			}
			Mem_avg /= sInfo.getMemory_usage().size();
			serMon.memory_avg = Double.parseDouble(df.format(Mem_avg));

			this.m_serverMon.add(serMon);
		}
	}

	private void TransAveResponseCal() {
		double sum;
		int size;
		for (int i = 1; i < 15; i++) {
			sum = 0;
			size = 0;
			for (Iterator<Double> iterator = wirt[i].getResult().iterator(); iterator
					.hasNext();) {
				sum += iterator.next();
				size++;
			}
			trans_avg_res[i] = Double.parseDouble(df.format(sum / size));

		}
	}
}
