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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.bench4Q.console.ui.controlTree.Type;
import org.bench4Q.console.ui.transfer.AgentInfo;
import org.bench4Q.console.ui.transfer.AgentInfoObserver;
import org.bench4Q.console.ui.transfer.AgentsCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;


/**
 * @author duanzhiquan
 * 
 */
public class S_LengthSection extends JPanel implements AgentInfoObserver {

	/**
	 * 
	 */
	private static final long serialVersionUID = -795294685621746576L;
	private final Resources m_resources;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;

	private AgentIdentity m_agentIdentity;
	private Boolean TotalOrNot;
	private AgentsCollection m_agentsCollection;

	private int TotalResultNumber;
	private int resultNumber;

	private ArrayList<Integer> sessionLen = new ArrayList<Integer>();

	private PicPanel picPanel;
	
	private Type m_type;

	/**
	 * @param resources
	 * @param processControl
	 * @param dispatcherFactory
	 * @param TotalOrNot
	 * @param agentIdentity
	 * @param agentsCollection
	 * @throws ConsoleException
	 */
	public S_LengthSection(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, Boolean TotalOrNot,
			AgentIdentity agentIdentity, AgentsCollection agentsCollection, Type type)
			throws ConsoleException {

		m_resources = resources;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;

		this.TotalOrNot = TotalOrNot;
		m_agentIdentity = agentIdentity;
		m_agentsCollection = agentsCollection;
		m_agentsCollection.registerObserver(this);
		m_type = type;

		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(683, 550));
		this.setMinimumSize(new Dimension(683, 550));

		picPanel = new PicPanel();
		this.add(picPanel, new GridBagConstraints(0, 0, 1, 5, 99.0, 99.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 1, 1));

	}

	/**
	 * @return
	 * @throws IOException
	 */
	public JPanel drawSessionPic() throws IOException {

		CategoryDataset dataset = getDataSet();
		JFreeChart chart = ChartFactory.createBarChart3D("Session Length Distribution",
				"Session type", "Session number", dataset,
				PlotOrientation.VERTICAL, true, true, true);
		CategoryPlot plot = chart.getCategoryPlot();
		org.jfree.chart.axis.CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLowerMargin(0.1);
		domainAxis.setUpperMargin(0.1);
		domainAxis.setCategoryLabelPositionOffset(10);
		domainAxis.setCategoryMargin(0.2);

		org.jfree.chart.axis.ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperMargin(0.1);

		org.jfree.chart.renderer.category.BarRenderer3D renderer;
		renderer = new org.jfree.chart.renderer.category.BarRenderer3D();
		renderer.setBaseOutlinePaint(Color.red);
		renderer.setSeriesPaint(0, new Color(0, 255, 255));
		renderer.setSeriesOutlinePaint(0, Color.BLACK);
		renderer.setSeriesPaint(1, new Color(0, 255, 0));
		renderer.setSeriesOutlinePaint(1, Color.red);
		renderer.setItemMargin(0.1);
		renderer
				.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
//		renderer.setItemLabelFont(new Font("ºÚÌå", Font.BOLD, 12));
//		renderer.setItemLabelPaint(Color.black);
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));
		renderer.setBaseItemLabelsVisible(true);
		renderer.setItemLabelAnchorOffset(10D);
		plot.setRenderer(renderer);

		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		plot.setBackgroundPaint(Color.WHITE);

		return new ChartPanel(chart);
	}

	private CategoryDataset getDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		int[] res = calculate();

		for (int i = 0; i < res.length; i++) {
			if (res[i] != 0) {
				dataset.addValue(res[i], new Integer(i), "Session length");
			}

		}
		return dataset;
	}

	private int[] calculate() {
		int[] res = new int[40];
		for (int tem : sessionLen) {
			if (tem < 20) {
				res[tem]++;
			} else {
				res[39]++;
			}
		}
		return res;
	}

	private JPanel getPanel() {
		return this;
	}

	private class rangeSelectAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {

		}
	}

	public void addAgent(AgentInfo agentInfo) {
	}

	public void getResult(AgentInfo agentInfo) {
		if (TotalOrNot) {
			TotalResultNumber = m_agentsCollection.getAgentNumber();
		}

		ArrayList<Integer> sessionlength;
		
		if(m_type == Type.all)
			sessionlength = agentInfo.getStats().getSessionLen();
		else if(m_type == Type.VIP)
			sessionlength = agentInfo.getStats().getSessionLen_vip();
		else {
			sessionlength = agentInfo.getStats().getSessionLen_norm();
		}
		
		if (!TotalOrNot && agentInfo.getAgentIdentity().equals(m_agentIdentity)) {
			sessionLen.addAll(sessionlength);

		} else if (TotalOrNot) {
			sessionLen.addAll(sessionlength);

			resultNumber++;

		} else {
			return;
		}

		if (AllResultReceived()) {
			try {
				picPanel.setShowForm(drawSessionPic());
				restartTest();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private boolean AllResultReceived() {
		return (TotalOrNot || (!TotalOrNot && resultNumber == TotalResultNumber)) ? true
				: false;
	}

	public void removeAgent(AgentInfo agentInfo) {
	}

	public void restartTest() {
		sessionLen = new ArrayList<Integer>();
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


	@Override
	public void saveTheChart(String prefix) {
		// TODO Auto-generated method stub
		
	}

}
