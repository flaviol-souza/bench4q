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
package org.bench4Q.console.ui.section;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bench4Q.agent.rbe.communication.ResultSet;
import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.bench4Q.console.ui.transfer.AgentInfo;
import org.bench4Q.console.ui.transfer.AgentInfoObserver;
import org.bench4Q.console.ui.transfer.AgentsCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;

/**
 * @author duanzhiquan
 * 
 */
public class P_WIRTSection extends JPanel implements AgentInfoObserver {

	private static final long serialVersionUID = -2190152980187105247L;
	private Resources m_resources;
	private ProcessControl m_processControl;
	private SwingDispatcherFactory m_swingDispatcherFactory;

	private ResultSet[] wirt;

	private ArrayList<Double> thiswirt;
	private long WIRT;
	private int testduring;

	private AgentsCollection m_agentsCollection;

	private AgentIdentity m_agentIdentity;
	private Boolean TotalOrNot;
	private int TotalResultNumber;
	private int resultNumber;

	private PicPanel picPanel;

	double[][] result;

	/**
	 * 
	 */
	public P_WIRTSection() {
		thiswirt = new ArrayList();
	}

	/**
	 * @param resources
	 * @param processControl
	 * @param dispatcherFactory
	 * @param TotalOrNot
	 * @param agentIdentity
	 * @param agentsCollection
	 * @throws ConsoleException
	 */
	public P_WIRTSection(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, Boolean TotalOrNot,
			AgentIdentity agentIdentity, AgentsCollection agentsCollection)
			throws ConsoleException {

		m_resources = resources;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;

		this.TotalOrNot = TotalOrNot;
		m_agentIdentity = agentIdentity;

		m_agentsCollection = agentsCollection;
		m_agentsCollection.registerObserver(this);

		testduring = -1;
		resultNumber = 0;

		wirt = new ResultSet[15];
		thiswirt = new ArrayList();

		this.setLayout(new GridBagLayout());

		this.setPreferredSize(new Dimension(550, 445));
		this.setMinimumSize(new Dimension(550, 445));

		picPanel = new PicPanel();
		this.add(picPanel, new GridBagConstraints(0, 0, 1, 4, 99.0, 99.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 1, 1));

	}

	private class rangeSelectAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}
	}

	private class servletSelectAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}
	}

	private class savePicAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {

		}
	}

	/**
	 * print out a WIRT picture.
	 * 
	 * @return
	 */
	public JPanel printWIRTPic() {
		wirtCDFPrepare();
		DefaultXYDataset ds = new DefaultXYDataset();
		ds.addSeries("test", result);
		JFreeChart chart = ChartFactory.createXYLineChart("WIRT CDF",
				"time (ms)", "percent", ds, PlotOrientation.VERTICAL, false,
				false, false);
		final XYPlot plot = chart.getXYPlot();
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);
		plot.setRangeCrosshairLockedOnData(true);
		plot.setBackgroundPaint(Color.WHITE);

		plot.setRangeCrosshairVisible(true);

		final ChartPanel chartPanel = new ChartPanel(chart);
		return chartPanel;
	}

	private void wirtCDFPrepare() {

		if (thiswirt.size() <= 0) {
			return;
		}

		Double[] wirtOrderd = new Double[thiswirt.size()];
		thiswirt.toArray(wirtOrderd);
		Arrays.sort(wirtOrderd);

		double[][] temp = new double[2][wirtOrderd.length];
		int tempIndex = 1;
		int num = 1;
		Double current = wirtOrderd[0];
		temp[0][0] = 0;
		temp[1][0] = 0;
		for (int i = 1; i < wirtOrderd.length; i++) {
			// if ( (wirtOrderd[i]-current<0.00001) ||
			// (current-wirtOrderd[i]<0.00001) ) {
			if (Math.floor(wirtOrderd[i]) == Math.floor(current)) {
				num++;
			} else {
				temp[0][tempIndex] = current;
				temp[1][tempIndex] = num;

				current = wirtOrderd[i];
				num = 1;
				tempIndex++;
			}
		}
		temp[0][tempIndex] = current;
		temp[1][tempIndex] = num;
		tempIndex++;
		result = new double[2][tempIndex];

		result[0][0] = temp[0][0];
		result[1][0] = temp[1][0];

		for (int i = 1; i < tempIndex; i++) {
			result[0][i] = temp[0][i];
			result[1][i] = temp[1][i];
		}
		for (int i = 1; i < tempIndex; i++) {
			result[1][i] = result[1][i - 1] + result[1][i];
		}
		for (int i = 0; i < tempIndex; i++) {
			result[1][i] = result[1][i] * 100l / wirtOrderd.length;
		}
	}

	public void addAgent(AgentInfo agentInfo) {
	}

	public void getResult(AgentInfo agentInfo) {

		if (TotalOrNot) {
			TotalResultNumber = m_agentsCollection.getAgentNumber();
		}

		ResultSet[] result = agentInfo.getStats().getWirt();
		if (!TotalOrNot && agentInfo.getAgentIdentity().equals(m_agentIdentity)) {
			for (int i = 0; i < 15; i++) {
				this.wirt[i] = result[i];
			}
		} else if (TotalOrNot) {
			for (int i = 0; i < 15; i++) {
				if (this.wirt[i] == null) {
					this.wirt[i] = new ResultSet();
				}
				this.wirt[i].getResult().addAll(result[i].getResult());

			}
			resultNumber++;

		} else {
			return;
		}
		for (int i = 0; i < 15; i++) {
			thiswirt.addAll(wirt[i].getResult());
		}

		if (AllResultReceived()) {
			picPanel.setShowForm(printWIRTPic());
			this.updateUI();
			restartTest();
		}
	}

	private boolean AllResultReceived() {
		return (TotalOrNot || (!TotalOrNot && resultNumber == TotalResultNumber)) ? true
				: false;
	}

	public void removeAgent(AgentInfo agentInfo) {
	}

	public void restartTest() {
		testduring = -1;
		wirt = new ResultSet[15];
		thiswirt = new ArrayList();
		resultNumber = 0;
		TotalResultNumber = 0;
	}

	private class PicPanel extends JPanel {
		// BorderLayout borderLayout = new BorderLayout();
		JPanel panel;

		public PicPanel() {
			this.setLayout(new GridBagLayout());

			panel = new JPanel();
			JLabel noResultLabel = new JLabel(m_resources
					.getString("Picture.noResultReceived"));
			panel.add(noResultLabel);
			// this.add(panel, java.awt.BorderLayout.CENTER);
			this.add(panel, new GridBagConstraints(0, 0, 1, 1, 100.0, 100.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
		}

		public void setShowForm(JPanel otherpanel) {
			this.remove(panel);
			panel = otherpanel;
			panel.setEnabled(true);

			this.add(panel, new GridBagConstraints(0, 0, 1, 1, 100.0, 100.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
			this.updateUI();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		P_WIRTSection test = null;
		test = new P_WIRTSection();
		for (Double i = 10d; i < 15; i++) {
			test.thiswirt.add(i);
			test.thiswirt.add(i - 1);
		}

		test.wirtCDFPrepare();

		System.out.println();

	}

}
