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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;

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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author duanzhiquan
 * 
 */
public class S_SPSSection extends JPanel implements AgentInfoObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7663512504865352763L;
	private final Resources m_resources;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;

	private int[][] session;
	private long SPS;
	private int testduring;

	private PicPanel picPanel;

	private AgentIdentity m_agentIdentity;
	private Boolean TotalOrNot;
	private int TotalResultNumber;
	private int resultNumber;

	private AgentsCollection m_agentsCollection;

	/**
	 * @param resources
	 * @param processControl
	 * @param dispatcherFactory
	 * @param TotalOrNot
	 * @param agentIdentity
	 * @param agentsCollection
	 * @throws ConsoleException
	 */
	public S_SPSSection(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, Boolean TotalOrNot,
			AgentIdentity agentIdentity, AgentsCollection agentsCollection)
			throws ConsoleException {

		m_resources = resources;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;
		m_agentIdentity = agentIdentity;
		m_agentsCollection = agentsCollection;
		m_agentsCollection.registerObserver(this);

		this.TotalOrNot = TotalOrNot;

		this.setLayout(new GridBagLayout());

		this.setPreferredSize(new Dimension(683, 475));
		this.setMinimumSize(new Dimension(683, 475));

		testduring = -1;
		resultNumber = 0;

		picPanel = new PicPanel();
		this.add(picPanel, new GridBagConstraints(0, 0, 1, 4, 99.0, 99.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 1, 1));

	}

	private JPanel getPanel() {
		return this;
	}

	// print out a WIPS picture.
	private JPanel printWIPSPic() throws IOException {
		double[][] value = sessionSmooth();
		for (int i = 0; i < value[0].length; ++i) {
			value[0][i] = i;
			// value[1][i] = webInteractionThroughput[i];.
		}

		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		String series1 = "Basic";
		String series2 = "real";

		for (int i = 0; i < value[0].length; ++i) {
			defaultcategorydataset.addValue(value[1][i], series1, new Integer(
					(int) value[0][i]));
			defaultcategorydataset.addValue(session[1][i], series2,
					new Integer((int) value[0][i]));
		}

		JFreeChart chart = ChartFactory.createLineChart("SPS = " + SPS, "time",
				"SPS", defaultcategorydataset, PlotOrientation.VERTICAL, true,
				true, false);
		chart.setBackgroundPaint(Color.white);
		CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
		categoryplot.setBackgroundPaint(Color.WHITE);
		categoryplot.setRangeGridlinePaint(Color.white);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberaxis.setAutoRangeIncludesZero(true);
		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
				.getRenderer();
		lineandshaperenderer.setShapesVisible(false);
		lineandshaperenderer.setSeriesStroke(0, new BasicStroke(2.0F, 1, 1,
				1.0F, new float[] { 1F, 1F }, 0.0F));
		lineandshaperenderer.setSeriesStroke(1, new BasicStroke(2.0F, 1, 0,
				2.0F, new float[] { 1F, 10000F }, 0.0F));
		return new ChartPanel(chart);
	}

	private double[][] sessionSmooth() {

		int i;
		for (i = testduring; i >= 0; i--) {
			if (session[1][i] > 0) {
				break;
			}
		}

		for (int j = 0; j < i; j++) {
			SPS += session[1][j];
		}

		if ((i - 1) > 0) {
			SPS /= (i - 1);
		} else {
			SPS = 0;
		}

		double[][] smooth;
		if (i > 50) {

			smooth = new double[2][i - 28];
			if (i != -1) {
				smooth[1][0] = 0;
				for (int j = 0; j < 30; j++) {
					smooth[1][0] += session[1][j];
				}
				for (int j = 30; j <= i; j++) {
					smooth[1][j - 29] = smooth[1][j - 30] + session[1][j]
							- session[1][j - 30];
				}
				for (int j = 0; j <= i - 29; j++) {
					smooth[1][j] = smooth[1][j] / 30;
				}
			}
		} else if (i > 0) {
			smooth = new double[2][i];
			for (int j = 0; j < i; j++) {
				smooth[1][j] = session[1][j];
			}

		} else {
			smooth = new double[2][1];
			smooth[0][0] = 0;
			smooth[1][0] = 0;
		}

		return smooth;
	}

	public void addAgent(AgentInfo agentInfo) {

	}

	public void getResult(AgentInfo agentInfo) {
		if (testduring == -1) {
			testduring = agentInfo.getStats().getTestduring();
			session = new int[2][(int) (testduring + 1)];
		}

		if (TotalOrNot) {
			TotalResultNumber = m_agentsCollection.getAgentNumber();
		}

		int[][] result = agentInfo.getStats().getSession();
		if (!TotalOrNot && agentInfo.getAgentIdentity().equals(m_agentIdentity)) {
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < (testduring + 1); j++) {
					session[i][j] = result[i][j];
				}
			}
		} else if (TotalOrNot) {
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < (testduring + 1); j++) {
					session[i][j] += result[i][j];
				}
			}
			resultNumber++;

		} else {
			return;
		}
		if (AllResultReceived()) {
			try {
				picPanel.setShowForm(printWIPSPic());

			} catch (IOException e) {
				e.printStackTrace();
			}
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
		session = null;
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

}
