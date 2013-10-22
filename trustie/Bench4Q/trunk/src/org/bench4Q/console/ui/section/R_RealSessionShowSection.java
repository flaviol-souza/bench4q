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

import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
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
public class R_RealSessionShowSection extends JPanel implements
		AgentInfoObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1675439180146322483L;
	private final Resources m_resources;
	private final ProcessControl m_processControl;

	private int[] loadStart;
	private int testduring;

	private PicPanel picPanel;

	private int TotalResultNumber;
	private int resultNumber;
	private AgentsCollection m_agentsCollection;

	/**
	 * @param resources
	 * @param processControl
	 * @param agentsCollection
	 * @throws ConsoleException
	 */
	public R_RealSessionShowSection(Resources resources,
			ProcessControl processControl, AgentsCollection agentsCollection)
			throws ConsoleException {

		m_resources = resources;
		m_processControl = processControl;
		m_agentsCollection = agentsCollection;
		m_agentsCollection.registerObserver(this);

		this.setLayout(new GridBagLayout());

		this.setPreferredSize(new Dimension(683, 550));
		this.setMinimumSize(new Dimension(683, 550));

		testduring = -1;
		resultNumber = 0;

		picPanel = new PicPanel();
		this.add(picPanel, new GridBagConstraints(0, 0, 1, 4, 99.0, 99.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 1, 1));

	}

	private JPanel printPic() throws IOException {
		double[][] value = new double[2][testduring];
		for (int i = 0; i < value[0].length; ++i) {
			value[0][i] = i;
			value[1][i] = loadStart[i];
		}

		// calculate the avrg load start every second.
		int avrgLoad = 0;
		for (int i = 0; i < loadStart.length; i++) {
			avrgLoad += loadStart[i];
		}
		avrgLoad /= testduring;

		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		String series1 = "real";

		for (int i = 0; i < value[0].length; ++i) {
			defaultcategorydataset.addValue(value[1][i], series1, new Integer(
					(int) value[0][i]));
		}

		JFreeChart chart = ChartFactory.createLineChart(
				"REAL LOAD:" + avrgLoad, "time", "load",
				defaultcategorydataset, PlotOrientation.VERTICAL, true, true,
				false);
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
		return new ChartPanel(chart);
	}

	public void addAgent(AgentInfo agentInfo) {

	}

	public void getResult(AgentInfo agentInfo) {
		if (testduring == -1) {
			testduring = agentInfo.getStats().getTestduring();
			loadStart = new int[(int) (testduring + 1)];
		}
		TotalResultNumber = m_agentsCollection.getAgentNumber();

		int[][] result = agentInfo.getStats().getSession();

		for (int j = 0; j < (testduring + 1); j++) {
			loadStart[j] += result[0][j];
		}

		resultNumber++;

		if (AllResultReceived()) {
			try {
				picPanel.setShowForm(printPic());
			} catch (IOException e) {
				e.printStackTrace();
			}
			restartTest();
		}

	}

	private boolean AllResultReceived() {
		if (resultNumber == TotalResultNumber) {
			return true;
		} else {
			return false;
		}
	}

	public void removeAgent(AgentInfo agentInfo) {

	}

	public void restartTest() {
		testduring = -1;
		loadStart = null;
		resultNumber = 0;
		TotalResultNumber = 0;

	}

	private class PicPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 4347821333940195499L;
		JPanel panel;

		public PicPanel() {
			this.setLayout(new GridBagLayout());
			panel = new JPanel();
			JLabel noResultLabel = new JLabel(m_resources
					.getString("RealSessionShowSection.testNotFinished"));
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
	@Override
	public void saveTheChart(String prefix) {
		// TODO Auto-generated method stub
		
	}

}
