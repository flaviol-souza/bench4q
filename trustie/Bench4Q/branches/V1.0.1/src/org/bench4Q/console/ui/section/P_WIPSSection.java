/**
 * =========================================================================
 * 					Bench4Q version 1.0.0
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
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import org.bench4Q.console.ui.util.ButtonGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class P_WIPSSection extends JPanel implements AgentInfoObserver {

	private final Resources m_resources;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;

	private int[][] webInteractionThroughput;
	private long WIPS;
	private int testduring;

	private PicPanel picPanel;

	private AgentIdentity m_agentIdentity;
	private Boolean TotalOrNot;
	private int TotalResultNumber;
	private int resultNumber;

	private AgentsCollection m_agentsCollection;

	// public Buffer m_lookAndFeel;

	private Boolean[] SelectedResult = new Boolean[15];

	private final static String[] SERVLETS = { "home_servlet", "shopping_cart_servlet",
			"order_inquiry_servlet", "order_display_servlet", "search_request_servlet",
			"execute_search_servlet", "new_products_servlet", "best_sellers_servlet",
			"product_detail_servlet", "customer_registration_servlet", "buy_request_servlet",
			"buy_confirm_servlet", "admin_request_servlet", "admin_response_servlet", "Total", };

	public P_WIPSSection(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, Boolean TotalOrNot,
			AgentIdentity agentIdentity, AgentsCollection agentsCollection) throws ConsoleException {

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

		JButton rangeButton = ButtonGenerator.newButton(m_resources
				.getString("TestResultWIPSSection.rangeButton"));
		rangeButton.addActionListener(new rangeSelectAction());
		JButton servletButton = ButtonGenerator.newButton(m_resources
				.getString("TestResultWIPSSection.servletButton"));
		servletButton.addActionListener(new servletSelectAction());
		JButton savePicButton = ButtonGenerator.newButton(m_resources
				.getString("TestResultWIPSSection.savePicButton"));
		savePicButton.addActionListener(new savePicAction());

		picPanel = new PicPanel();
		this.add(picPanel, new GridBagConstraints(0, 0, 1, 4, 99.0, 99.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1));
		// this.add(rangeButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
		// GridBagConstraints.WEST,
		// GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));
		// this.add(servletButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
		// GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,
		// 5, 5, 5), 1,
		// 1));
		// this.add(savePicButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
		// GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,
		// 5, 5, 5), 1,
		// 1));
		// this.add(new JLabel(" "), new GridBagConstraints(1, 3, 1, 1, 1.0,
		// 1.0,
		// GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5,
		// 5, 5, 5), 1,
		// 1));

	}

	private JPanel getPanel() {
		return this;
	}

	private class rangeSelectAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {

		}
	}

	// print out a WIPS picture.
	private JPanel printWIPSPic() throws IOException {
		double[][] value = wipsSmooth();
		for (int i = 0; i < value[0].length; ++i) {
			value[0][i] = i;
			// value[1][i] = webInteractionThroughput[i];.
		}

		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		String series1 = "Basic";
		String series2 = "real";
		// String series2 = "High";

		for (int i = 0; i < value[0].length; ++i) {
			defaultcategorydataset.addValue(value[1][i], series1, new Integer((int) value[0][i]));
			defaultcategorydataset.addValue(webInteractionThroughput[0][i], series2, new Integer(
					(int) value[0][i]));
		}

		JFreeChart chart = ChartFactory.createLineChart("WIPS = " + WIPS, "time", "WIPS",
				defaultcategorydataset, PlotOrientation.VERTICAL, true, true, false);
		chart.setBackgroundPaint(Color.white);
		CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
		categoryplot.setBackgroundPaint(Color.lightGray);
		categoryplot.setRangeGridlinePaint(Color.white);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberaxis.setAutoRangeIncludesZero(true);
		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
				.getRenderer();
		lineandshaperenderer.setShapesVisible(false);
		lineandshaperenderer.setSeriesStroke(0, new BasicStroke(2.0F, 1, 1, 1.0F, new float[] { 1F,
				1F }, 0.0F));
		lineandshaperenderer.setSeriesStroke(1, new BasicStroke(2.0F, 1, 0, 2.0F, new float[] { 1F,
				10000F }, 0.0F));
		return new ChartPanel(chart);
	}

	private double[][] wipsSmooth() {

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < (testduring + 1); j++) {
				webInteractionThroughput[0][j] += webInteractionThroughput[i][j];
			}
		}

		int i;
		for (i = testduring; i >= 0; i--) {
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

		double[][] smooth;
		if (i > 50) {

			smooth = new double[2][i - 28];
			if (i != -1) {
				smooth[1][0] = 0;
				for (int j = 0; j < 30; j++) {
					smooth[1][0] += webInteractionThroughput[0][j];
				}
				for (int j = 30; j <= i; j++) {
					smooth[1][j - 29] = smooth[1][j - 30] + webInteractionThroughput[0][j]
							- webInteractionThroughput[0][j - 30];
				}
				for (int j = 0; j <= i - 29; j++) {
					smooth[1][j] = smooth[1][j] / 30;
				}
			}
		} else if (i > 0) {
			smooth = new double[2][i];
			for (int j = 0; j < i; j++) {
				smooth[1][j] = webInteractionThroughput[0][j];
			}

		} else {
			smooth = new double[2][1];
			smooth[0][0] = 0;
			smooth[1][0] = 0;
		}

		return smooth;
	}

	private class servletSelectAction implements ActionListener {
		private Checkbox[] m_option = new Checkbox[15];

		public void actionPerformed(ActionEvent e) {

			// for (int i = 0; i < 15; i++) {
			// m_option[i] = new Checkbox(SERVLETS[i]);
			// if (SelectedResult[i] != null) {
			// m_option[i].setState(SelectedResult[i]);
			// }
			//
			// }
			//
			// String[] decide = { "OK", "Cancel" };
			//
			// int result = JOptionPane.showOptionDialog(getPanel(), m_option,
			// "OptionPaneDemo.componenttitle", JOptionPane.DEFAULT_OPTION,
			// JOptionPane.INFORMATION_MESSAGE, null, decide, decide[0]);
			//
			// switch (result) {
			// case 0:
			// getResult();
			// break;
			// case 1:
			// break;
			// default:
			// break;
			// }
		}

		private void getResult() {
			for (int i = 0; i < 15; i++) {
				SelectedResult[i] = m_option[i].getState();
			}

		}
	}

	private class savePicAction implements ActionListener {

		private final JFileChooser m_fileChooser = new JFileChooser(".");

		public savePicAction() {

		}

		public void actionPerformed(ActionEvent event) {

		}
	}

	public void addAgent(AgentInfo agentInfo) {

	}

	public void getResult(AgentInfo agentInfo) {
		if (testduring == -1) {
			testduring = agentInfo.getStats().getTestduring();
			webInteractionThroughput = new int[15][(int) (testduring + 1)];
		}

		if (TotalOrNot) {
			TotalResultNumber = m_agentsCollection.getAgentNumber();
		}

		int[][] result = agentInfo.getStats().getWebInteractionThroughput();
		if (!TotalOrNot && agentInfo.getAgentIdentity().equals(m_agentIdentity)) {
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < (testduring + 1); j++) {
					webInteractionThroughput[i][j] = result[i][j];
				}
			}
		} else if (TotalOrNot) {
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < (testduring + 1); j++) {
					webInteractionThroughput[i][j] += result[i][j];
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
		if (TotalOrNot || (TotalOrNot && resultNumber == TotalResultNumber)) {
			return true;
		} else {
			return false;
		}
	}

	public void removeAgent(AgentInfo agentInfo) {

	}

	public void restartTest() {
		testduring = -1;
		webInteractionThroughput = null;
		resultNumber = 0;
		TotalResultNumber = 0;

	}

	private class PicPanel extends JPanel {
		// BorderLayout borderLayout = new BorderLayout();
		JPanel panel;

		public PicPanel() {
			this.setLayout(new GridBagLayout());
			panel = new JPanel();
			JLabel noResultLabel = new JLabel(m_resources.getString("Picture.noResultReceived"));
			panel.add(noResultLabel);

			// this.add(panel, java.awt.BorderLayout.CENTER);
			this.add(panel, new GridBagConstraints(0, 0, 1, 1, 100.0, 100.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
					0));
		}

		public void setShowForm(JPanel otherpanel) {
			this.remove(panel);
			panel = otherpanel;
			panel.setEnabled(true);

			this.add(panel, new GridBagConstraints(0, 0, 1, 1, 100.0, 100.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
					0));
			this.updateUI();
		}

	}

}
