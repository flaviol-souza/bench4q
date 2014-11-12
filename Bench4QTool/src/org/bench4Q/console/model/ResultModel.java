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

	private double WIPS = 0;
	private double WIPS_VIP = 0;
	private double WIPS_NORM = 0;
	private double WIRT_AVG = 0;
	private double WIRT_VIP_AVG = 0;
	private double WIRT_NORM_AVG = 0;
	private double WIRT_95 = 0;
	private double WIRT_VIP_95 = 0;
	private double WIRT_NORM_95 = 0;

	// used to record session info
	private int[][] session;
	private ArrayList<Integer> sessionLen = new ArrayList<Integer>();
	private ArrayList<Integer> sessionLen_vip = new ArrayList<Integer>();
	private ArrayList<Integer> sessionLen_norm = new ArrayList<Integer>();
	private int OrderedSession = 0;
	private int[] ErrorSession = new int[15];

	// others
	private double TotalProfit = 0;
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

	enum type {
		all, normal, vip
	};

	private double[] trans_avg_res = new double[15];
	private double[] trans_avg_thp = new double[15];
	private double[] trans_avg_ratio = new double[15];

	/**
	 * @param resources
	 */
	public ResultModel(Resources resources) {
		m_resources = resources;
		m_result = new ArrayList<AgentInfo>();

		m_stats = new ArrayList<EBStats>();

		for (int i = 0; i < 15; i++) {
			wirt[i] = new ResultSet();
			tt[i] = new ResultSet();
			errors[i] = new ErrorSet();
		}
	}

	/**
	 * @param agentsCollection
	 * @param resources
	 */
	public ResultModel(AgentsCollection agentsCollection, Resources resources) {
		this(resources);
		m_agentsCollection = agentsCollection;
		m_agentsCollection.registerObserver(this);

	}

	/**
	 * save test result to the selected file.
	 * 
	 * @throws IOException
	 */
	public void SaveToFile() {
		CalTotalResult();
		FileWriter outstream;
		FileWriter outstreamCSV;
		try {
			outstream = new FileWriter(m_selectedFile);
			String nameS = m_selectedFile.getAbsolutePath();
			String lastname;
			if (nameS.endsWith(".bq")) {
				nameS = nameS.substring(0, nameS.length() - 3);
			}
			lastname = nameS.concat(".csv");
			File file = new File(lastname);
			printAllResult(file);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			outstreamCSV = new FileWriter(file);

			outstream.write("bench4Q test result\n\n");

			outstream
					.write("****************************************************\n");

			outstream.write("The Total Statistics\n");

			outstream.write("WIPS:" + "\t" + WIPS + "\n");

			outstream.write("WIRT average:" + "\t" + WIRT_AVG + "\n");

			outstream.write("WIRT 95%:" + "\t" + WIRT_95 + "\n");

			outstream.write("Complete Session:" + "\t" + sessionLen.size()
					+ "\n");

			outstream.write("Error Session:" + "\t" + errorCnt + "\n");

			if (!sessionLen_vip.isEmpty()) {

				outstream
						.write("****************************************************\n");

				outstream.write("The VIP customers Statistics\n");

				outstream.write("WIPS:" + "\t" + WIPS_VIP + "\n");

				outstream.write("WIRT average:" + "\t" + WIRT_VIP_AVG + "\n");

				outstream.write("WIRT 95%:" + "\t" + WIRT_VIP_95 + "\n");

				outstream.write("Complete Session:" + "\t"
						+ sessionLen_vip.size() + "\n");

				outstream.write("Error Session:" + "\t" + errorCnt_vip + "\n");
			}
			if (!sessionLen_norm.isEmpty()) {
				outstream
						.write("****************************************************\n");

				outstream.write("The normal customers Statistics\n");

				outstream.write("WIPS:" + "\t" + WIPS_NORM + "\n");

				outstream.write("WIRT average:" + "\t" + WIRT_NORM_AVG + "\n");

				outstream.write("WIRT 95%:" + "\t" + WIRT_NORM_95 + "\n");

				outstream.write("Complete Session:" + "\t"
						+ sessionLen_norm.size() + "\n");

				outstream.write("Error Session:" + "\t" + errorCnt_norm + "\n");
			}
			outstream
					.write("****************************************************\n");

			Iterator it = m_serverMon.iterator();
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
			// outstream.write("trans table: " + "\n");
			// for (int i = 0; i < name.length; i++) {
			// outstream.write(name[i] + "	");
			// }
			// outstream.write("\n");
			// for (int i = 0; i < 15; i++) {
			// outstream.write(name[i] + " ");
			// for (int j = 0; j < 15; j++) {
			// outstream.write(trans[i][j] + "	");
			// }
			// outstream.write("\n");
			// }

			// outstream
			// .write("****************************************************\n");
			// outstream.write("Web Interaction Throughput:\n");
			// for (int i = 0; i < 15; i++) {
			// outstream.write("wips " + i + " : [\n");
			// for (int j = 0; j < webInteractionThroughput[i].length; j++) {
			// outstream.write(webInteractionThroughput[i][j] + "\n");
			// }
			// outstream.write("]\n");
			//
			// }

			// outstream
			// .write("****************************************************\n");
			// outstream.write("trans response time:\n");
			// for (int i = 0; i < 15; i++) {
			// outstream.write("trans " + name[i] + " : ");
			// double num=0.0;
			// for (Double result : wirt[i].getResult()) {
			// // outstream.write(result + "\n");
			// num += result;
			// }
			// if (wirt[i].getResult().size() != 0) {
			// num = num / wirt[i].getResult().size();
			// DecimalFormat df = new DecimalFormat("0.0");
			// num = Double.parseDouble(df.format(num));
			// outstream.write(num + "\n");
			// }
			// else {
			// outstream.write("0" + "\n");
			// }
			//
			// }
			// outstream.write("]\n");
			// outstream
			// .write("****************************************************\n");
			// outstream.write("session per second: [\n");
			// for (int j = 0; j < session[1].length; j++) {
			// outstream.write(session[1][j] + "\n");
			// }
			// outstream.write("]\n");

			// outstream
			// .write("****************************************************\n");
			// outstream.write("session length:\n");
			// for (Integer result : sessionLen) {
			// outstream.write(result + "\n");
			// }
			// outstream.write("]\n");

			// outstream
			// .write("****************************************************\n");
			// outstream.write("Ordered Session:" + OrderedSession + "]\n");
			//
			outstream
					.write("****************************************************\n");
			outstream
					.write("Transaction name    Response Time\t\tThroughput\t\tRatio\n");
			outstreamCSV
					.write("Transaction name,Response Time,Throughput,Ratio\n");
			for (int i = 1; i < 15; i++) {
				String out = String.format("trans " + name[i] + "          "
						+ "%-10.1f ms\t\t" + "%-10.1f\t\t" + "%-3.1f %%\n",
						trans_avg_res[i], trans_avg_thp[i], trans_avg_ratio[i]);
				outstreamCSV.write("trans " + name[i] + "," + trans_avg_res[i]
						+ "," + trans_avg_thp[i] + "," + trans_avg_ratio[i]
						+ "\n");
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
				for (EBError error : errors[i].getResult()) {
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

	private void printPageWithArray(FileWriter outstream, ResultSet[] array)
			throws IOException {

		String[] name = { new String("INIT"), new String("ADMC"),
				new String("ADMR"), new String("BESS"), new String("BUYC"),
				new String("BUYR"), new String("CREG"), new String("HOME"),
				new String("NEWP"), new String("ORDD"), new String("ORDI"),
				new String("PROD"), new String("SREQ"), new String("SRES"),
				new String("SHOP") };

		// outstream.write("trans table: " + "\n");
		for (int i = 0; i < name.length; i++) {
			outstream.write(name[i] + "\t" + array[i].getResult() + "\n");
		}
	}

	private void printPageWithArray(FileWriter outstream, ErrorSet[] array)
			throws IOException {

		String[] name = { new String("INIT"), new String("ADMC"),
				new String("ADMR"), new String("BESS"), new String("BUYC"),
				new String("BUYR"), new String("CREG"), new String("HOME"),
				new String("NEWP"), new String("ORDD"), new String("ORDI"),
				new String("PROD"), new String("SREQ"), new String("SRES"),
				new String("SHOP") };

		outstream.write("\n");
		for (int i = 0; i < name.length; i++) {
			outstream.write(name[i] + "\t" + array[i].getResult() + "\n");
		}
	}

	private void printAllResult(File file) throws IOException {
		String path = file.getParentFile().getPath();
		File newFile = new File(path + File.separator + "total-result.cvs");
		FileWriter outstream = new FileWriter(newFile);

		try {
			newFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		outstream.write("\n\n");
		outstream.write("*********************************  Web Interaction Response Time (WIRT) *********************************\n");
		printPageWithArray(outstream, wirt);
		outstream.write("*** WIRT-Normal ***************************************************************\n");
		printPageWithArray(outstream, wirt_norm);
		outstream.write("*** WIRT-Vip ***************************************************************\n");
		printPageWithArray(outstream, wirt_vip);

		outstream.write("\n\n");
		outstream.write("*********************************  Think Time (TT) *********************************\n");
		printPageWithArray(outstream, tt);
		outstream.write("*** TT-Normal ***************************************************************\n");
		printPageWithArray(outstream, tt_norm);
		outstream.write("*** TT-Vip ***************************************************************\n");
		printPageWithArray(outstream, tt_vip);
		
		outstream.write("\n\n");
		outstream.write("*********************************  Errors  *********************************\n");
		printPageWithArray(outstream, errors);
		outstream.write("*** Error-Normal ***************************************************************\n");
		printPageWithArray(outstream, errors_norm);
		outstream.write("*** Error-Vip ***************************************************************\n");
		printPageWithArray(outstream, errors_vip);

		outstream.write("\n");
		outstream.write("All result ****************************************************\n");
		outstream.write("The Total Statistics\n");
		outstream.write("WIPS:" + "\t" + WIPS + "\n");
		outstream.write("WIRT average:" + "\t" + WIRT_AVG + "\n");
		outstream.write("WIRT 95%:" + "\t" + WIRT_95 + "\n");
		outstream.write("Complete Session:" + "\t" + sessionLen.size() + "\n");
		outstream.write("Error Session:" + "\t" + errorCnt + "\n");
		outstream.write("****************************************************\n");

		outstream.close();
	}

	private void CalTotalResult() {

		for (EBStats stat : m_stats) {
			// get total wips of all agents

			if (time == -1) {
				m_testduring = stat.getTestduring();
				webInteractionThroughput = new int[15][(int) (m_testduring + 1)];
				webInteractionThroughput_vip = new int[15][(int) (m_testduring + 1)];
				webInteractionThroughput_norm = new int[15][(int) (m_testduring + 1)];
				session = new int[2][(int) (m_testduring + 1)];
				time++;
				stat.getTestduring();
			}
			int[][] wips = stat.getWebInteractionThroughput();
			int[][] wips_vip = stat.getWebInteractionThroughput_vip();
			int[][] wips_norm = stat.getWebInteractionThroughput_norm();
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < (m_testduring + 1); j++) {
					webInteractionThroughput[i][j] += wips[i][j];
					webInteractionThroughput_vip[i][j] += wips_vip[i][j];
					webInteractionThroughput_norm[i][j] += wips_norm[i][j];
				}
			}
			// System.out.println("Resultmodel");
			// for (int i = 0; i < 15; i++) {
			// for (int j = 0; j < (m_testduring + 1); j++) {
			// System.out.print(webInteractionThroughput[i][j] + " ");
			// }
			// System.out.println();
			// }

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
				this.wirt_vip[i].getResult().addAll(
						wirtofAgent_vip[i].getResult());
				this.wirt_norm[i].getResult().addAll(
						wirtofAgent_norm[i].getResult());
			}

			// get total wirt of all agents
			ResultSet[] ttOfAgent = stat.getTt();
			ResultSet[] ttOfAgent_vip = stat.getTt_vip();
			ResultSet[] ttOfAgent_norm = stat.getTt_norm();
			for (int i = 0; i < 15; i++) {
				if (this.tt[i] == null) {
					this.tt[i] = new ResultSet();
				}
				if (this.tt_vip[i] == null) {
					this.tt_vip[i] = new ResultSet();
				}
				if (this.tt_norm[i] == null) {
					this.tt_norm[i] = new ResultSet();
				}
				this.tt[i].getResult().addAll(ttOfAgent[i].getResult());
				this.tt_vip[i].getResult().addAll(ttOfAgent_vip[i].getResult());
				this.tt_norm[i].getResult().addAll(ttOfAgent_norm[i].getResult());

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
			for (int i = 0; i < errors.length; i++) {
				errorCnt += errors[i].getResult().size();
			}
			for (int i = 0; i < errors_vip.length; i++) {
				errorCnt_vip += errors_vip[i].getResult().size();
			}
			for (int i = 0; i < errors_norm.length; i++) {
				errorCnt_norm += errors_norm[i].getResult().size();
			}

			// get all trans matrix
			int[][] transOfAgent = stat.getTrans();
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					trans[i][j] += transOfAgent[i][j];
				}
			}

			// get total sps of all agents
			int[][] sessionTem = stat.getSession();
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < (m_testduring + 1); j++) {
					session[i][j] += sessionTem[i][j];
				}
			}

			// get total session Length.
			sessionLen.addAll(stat.getSessionLen());
			sessionLen_vip.addAll(stat.getSessionLen_vip());
			sessionLen_norm.addAll(stat.getSessionLen_norm());

			// get total session Length.
			OrderedSession += stat.getOrderedSession();

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

		WIPS = WIPScal(webInteractionThroughput);
		WIPS_VIP = WIPScal(webInteractionThroughput_vip);
		WIPS_NORM = WIPScal(webInteractionThroughput_norm);

		if (!m_MultiServers.isEmpty())
			ServerCal();

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
			// trans_avg_ratio[i] =
			// Double.parseDouble(df.format(trans_avg_thp[i] / total * 100));
			trans_avg_ratio[i] = trans_avg_thp[i] / total * 100;
		}

	}

	public void addAgent(AgentInfo agentInfo) throws ConsoleException {
	}

	public void getResult(AgentInfo agentInfo) {
		m_result.add(agentInfo);
		m_stats.add(agentInfo.getStats());
	}

	public void removeAgent(AgentInfo agentInfo) {
	}

	public void restartTest() {
		// webInteractionThroughput = null;
		// webInteractionThroughput_norm = null;
		// webInteractionThroughput_vip = null;
		time = -1;
		m_MultiServers = new HashMap<String, ServerInfo>();
		;
		if (!m_serverMon.isEmpty())
			m_serverMon.clear();
		m_stats.clear();
		m_result.clear();

		wirt = new ResultSet[15];
		wirt_vip = new ResultSet[15];
		wirt_norm = new ResultSet[15];

		tt = new ResultSet[15];
		tt_vip = new ResultSet[15];
		tt_norm = new ResultSet[15];

		WIPS = 0;
		WIPS_VIP = 0;
		WIPS_NORM = 0;
		WIRT_AVG = 0;
		WIRT_VIP_AVG = 0;
		WIRT_NORM_AVG = 0;
		WIRT_95 = 0;
		WIRT_VIP_95 = 0;
		WIRT_NORM_95 = 0;

		// used to record session info
		session = null;
		sessionLen = new ArrayList<Integer>();
		sessionLen_vip = new ArrayList<Integer>();
		sessionLen_norm = new ArrayList<Integer>();
		OrderedSession = 0;
		ErrorSession = new int[15];

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
		return m_selectedFile;
	}

	/**
	 * @param file
	 */
	public void setSelectedFile(File file) {
		m_selectedFile = file;
	}

	/**
	 * @param collection
	 */
	public void setAgentsCollection(AgentsCollection collection) {
		m_agentsCollection = collection;
		m_agentsCollection.registerObserver(this);
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
		double total = 0;

		Double[] wirtOrderd = new Double[wirtList.size()];
		wirtList.toArray(wirtOrderd);
		Arrays.sort(wirtOrderd);

		long number = (long) ((long) wirtOrderd.length * 0.95);
		double percent = 0;

		for (int i = 0; i < wirtOrderd.length; i++) {
			total += wirtOrderd[i];
			if (i == number)
				percent = wirtOrderd[i];
		}
		double average = total / wirtOrderd.length;
		DecimalFormat df = new DecimalFormat("0.0");
		// average = Double.parseDouble(df.format(average));
		// percent = Double.parseDouble(df.format(percent));

		if (m_type == type.all) {
			WIRT_AVG = average;
			WIRT_95 = percent;
		} else if (m_type == type.vip) {
			WIRT_VIP_AVG = average;
			WIRT_VIP_95 = percent;
		} else {
			WIRT_NORM_AVG = average;
			WIRT_NORM_95 = percent;
		}
	}

	public double WIPScal(int[][] webInteraction) {
		double m_wips = 0;
		for (int i = 1; i < 15; i++) {
			for (int j = 0; j < (m_testduring + 1); j++) {
				webInteraction[0][j] += webInteraction[i][j];
			}
		}

		int i;
		for (i = m_testduring; i >= 0; i--) {
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
			// m_wips = Double.parseDouble(df.format(m_wips));
			m_wips = m_wips;
		} else {
			m_wips = 0;
		}
		return m_wips;
	}

	@Override
	public void saveTheChart(String prefix) {
		// TODO Auto-generated method stub

	}

	public void insertData(Map<String, ServerInfo> MultiServers) {
		m_MultiServers.putAll(MultiServers);
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
		Iterator servIterator = m_MultiServers.entrySet().iterator();
		DecimalFormat df = new DecimalFormat("0.0");

		while (servIterator.hasNext()) {
			Map.Entry<String, ServerInfo> serverEntry = (Map.Entry<String, ServerInfo>) servIterator
					.next();
			serverMon serMon = new serverMon();
			serMon.address = serverEntry.getKey();
			ServerInfo sInfo = serverEntry.getValue();
			double CPU_avg = 0;
			for (int i = 0; i < sInfo.getCpu_ratio().size(); i++) {
				CPU_avg += sInfo.getCpu_ratio().get(i);
			}
			CPU_avg = CPU_avg / sInfo.getCpu_ratio().size();
			serMon.CPU_avg = Double.parseDouble(df.format(CPU_avg));

			double Mem_avg = 0;
			for (int i = 0; i < sInfo.getMemory_usage().size(); i++) {
				Mem_avg += sInfo.getMemory_usage().get(i);
			}
			Mem_avg = Mem_avg / sInfo.getMemory_usage().size();
			serMon.memory_avg = Double.parseDouble(df.format(Mem_avg));

			m_serverMon.add(serMon);
		}
	}

	private void TransAveResponseCal() {
		double sum;
		int size;
		for (int i = 1; i < 15; i++) {
			sum = 0;
			size = 0;
			if (wirt[i] != null) {
				for (Iterator<Double> iterator = wirt[i].getResult().iterator(); iterator
						.hasNext();) {
					sum += iterator.next();
					size++;
				}
			}
			if (size == 0)
				size = 1;
			// trans_avg_res[i] = Double.parseDouble(df.format(sum / size));
			trans_avg_res[i] = sum / size;

		}
	}
}
