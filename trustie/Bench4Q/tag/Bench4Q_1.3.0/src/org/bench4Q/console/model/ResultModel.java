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
import java.util.ArrayList;

import org.bench4Q.agent.rbe.communication.EBError;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.communication.ErrorSet;
import org.bench4Q.agent.rbe.communication.ResultSet;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
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
	private ResultSet[] wirt = new ResultSet[15];
	private ResultSet[] tt = new ResultSet[15];
	private long WIPS = 0;
	private long WIRT = 0;

	// used to record session info
	private int[][] session;
	private ArrayList<Integer> sessionLen = new ArrayList<Integer>();
	private int OrderedSession = 0;
	private int[] ErrorSession = new int[15];

	// others
	private double TotalProfit = 0;
	private int errorCnt = 0;
	private ErrorSet[] errors = new ErrorSet[15];

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
	 */
	public void SaveToFile() {
		CalTotalResult();
		FileWriter outstream;
		try {
			outstream = new FileWriter(m_selectedFile);

			outstream.write("bench4Q test result\n\n");

			outstream.write("WIPS:" + WIPS + "\n");

			outstream
					.write("****************************************************\n");
			String[] name = { new String("INIT"), new String("ADMC"),
					new String("ADMR"), new String("BESS"), new String("BUYC"),
					new String("BUYR"), new String("CREG"), new String("HOME"),
					new String("NEWP"), new String("ORDD"), new String("ORDI"),
					new String("PROD"), new String("SREQ"), new String("SRES"),
					new String("SHOP") };
			outstream.write("trans table: " + "\n");
			for (int i = 0; i < name.length; i++) {
				outstream.write(name[i] + "	");
			}
			outstream.write("\n");
			for (int i = 0; i < 15; i++) {
				outstream.write(name[i] + " ");
				for (int j = 0; j < 15; j++) {
					outstream.write(trans[i][j] + "	");
				}
				outstream.write("\n");
			}

			outstream
					.write("****************************************************\n");
			outstream.write("Web Interaction Throughput:\n");
			for (int i = 0; i < 15; i++) {
				outstream.write("wips " + i + " : [\n");
				for (int j = 0; j < webInteractionThroughput[i].length; j++) {
					outstream.write(webInteractionThroughput[i][j] + "\n");
				}
				outstream.write("]\n");

			}

			outstream
					.write("****************************************************\n");
			outstream.write("trans response time:\n");
			for (int i = 0; i < 15; i++) {
				outstream.write("trans " + i + " : [\n");
				for (Double result : wirt[i].getResult()) {
					outstream.write(result + "\n");
				}
			}
			outstream.write("]\n");
			outstream
					.write("****************************************************\n");
			outstream.write("session per second: [\n");
			for (int j = 0; j < session[1].length; j++) {
				outstream.write(session[1][j] + "\n");
			}
			outstream.write("]\n");

			outstream
					.write("****************************************************\n");
			outstream.write("session length:\n");
			for (Integer result : sessionLen) {
				outstream.write(result + "\n");
			}
			outstream.write("]\n");

			outstream
					.write("****************************************************\n");
			outstream.write("Ordered Session:" + OrderedSession + "]\n");

			outstream
					.write("****************************************************\n");
			outstream.write("ERROR: \n");
			for (int i = 0; i < 15; i++) {
				for (EBError error : errors[i].getResult()) {
					if (error != null) {
						outstream.write(error.toString() + "\n");
					}

				}
			}

			outstream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void CalTotalResult() {
		m_testduring = m_stats.get(0).getTestduring();
		webInteractionThroughput = new int[15][(int) (m_testduring + 1)];
		session = new int[2][(int) (m_testduring + 1)];

		for (EBStats stat : m_stats) {
			// get total wips of all agents
			int[][] wips = stat.getWebInteractionThroughput();
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < (m_testduring + 1); j++) {
					webInteractionThroughput[i][j] += wips[i][j];
				}
			}

			// get total wirt of all agents
			ResultSet[] wirtOfAgent = stat.getWirt();
			for (int i = 0; i < 15; i++) {
				if (this.wirt[i] == null) {
					this.wirt[i] = new ResultSet();
				}
				this.wirt[i].getResult().addAll(wirtOfAgent[i].getResult());
			}

			// get total errors of all agents
			ErrorSet[] errorOfAgent = stat.getErrors();
			for (int i = 0; i < 15; i++) {
				if (this.errors[i] == null) {
					this.errors[i] = new ErrorSet();
				}
				this.errors[i].getResult().addAll(errorOfAgent[i].getResult());

			}

			// count errors
			for (int i = 0; i < errors.length; i++) {
				errorCnt += errors[i].getResult().size();
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

			// get total session Length.
			OrderedSession += stat.getOrderedSession();

			// get total error session.
			int[] ErrorSessionTem = stat.getErrorSession();
			for (int i = 0; i < 15; i++) {
				ErrorSession[i] += ErrorSessionTem[i];
			}

		}

		// calculate WIPS
		int[] wips = new int[m_testduring + 1];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < (m_testduring + 1); j++) {
				wips[j] += webInteractionThroughput[i][j];
			}
		}

		int i;
		for (i = m_testduring; i >= 0; i--) {
			if (webInteractionThroughput[0][i] > 0) {
				break;
			}
		}

		for (int j = 0; j < i; j++) {
			WIPS += webInteractionThroughput[0][j];
		}

		if ((i - 1) > 0) {
			WIPS /= (i - 1);
		} else {
			WIPS = 0;
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
}
