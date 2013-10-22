/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there.
 * If you have any problem, you can  
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
 *  * Developer(s): Wang Sa.
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
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.rm.ServerCollection;
import org.bench4Q.console.rm.ServerInfo;
import org.bench4Q.console.rm.ServerInfoObserver;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;

/**
 * @author wang sa
 *
 */
public class P_DISKREADSection extends JPanel implements ServerInfoObserver{
	
	private static final long serialVersionUID = -4664913085497482123L;
	
	private Resources m_resources;
	private ProcessControl m_processControl;
	private SwingDispatcherFactory m_swingDispatcherFactory;

	private int testduring;
	
	private Boolean TotalOrNot;

	private ServerCollection m_serverCollection;

	private PicPanel picPanel;

	double[][] result;
	
	private double Max = Double.MIN_VALUE; 
	private double Min = Double.MAX_VALUE;
	private double Mean = 0;
	private double Var = 0;
	
	private int num;
	private int deleteNum=5;
	
	/**
	 * @param resources
	 * @param processControl
	 * @param dispatcherFactory
	 * @param serverCollection
	 * @throws ConsoleException
	 */
	public P_DISKREADSection(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory,
		    ServerCollection serverCollection)
			throws ConsoleException 
			{
		m_resources = resources;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;


		

		m_serverCollection = serverCollection;
		m_serverCollection.registerObserver(this);

		testduring = -1;


		this.setLayout(new GridBagLayout());

		this.setPreferredSize(new Dimension(683, 475));
		this.setMinimumSize(new Dimension(683, 475));

		picPanel = new PicPanel();
		this.add(picPanel, new GridBagConstraints(0, 0, 1, 4, 99.0, 99.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 1, 1));
		
			}
	/**
	 * @return
	 */
	public JPanel printDISKREADPic() {
		double[][] value = wipsSmooth();
		for (int i = 0; i < value[0].length; ++i) {
			value[0][i] = i;

		}
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		
		String series1 = "Basic";
		String series2 = "test (" + "Max = " + Max + " Min = " + Min
				+ " Average = " + Mean + " StDev = " + Var + " )";

		for (int i = 0; i < value[0].length; ++i) {
			defaultcategorydataset.addValue(value[1][i], series1, new Integer(
					(int) value[0][i]));
			
			defaultcategorydataset.addValue(result[1][i + num], series2,
					new Integer((int) value[0][i]));
		}
		JFreeChart chart = ChartFactory.createLineChart("DISK Read Bytes/sec",
				"time (s)", "byte/s", defaultcategorydataset,
				PlotOrientation.VERTICAL, true, true, false);
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
		lineandshaperenderer.setSeriesFillPaint(0, Color.BLACK);
		lineandshaperenderer.setSeriesStroke(1, new BasicStroke(2.0F, 1, 0,
				2.0F, new float[] { 1F, 10000F }, 0.0F));
		lineandshaperenderer.setSeriesFillPaint(0, Color.darkGray);
//		DefaultXYDataset ds = new DefaultXYDataset();
//		ds.addSeries("test (" + "Max = " + Max + " Min = " + Min + " Average = " + Mean + " StDev = "+ Var + " )", result);
//		JFreeChart chart = ChartFactory.createXYLineChart("DISK Read Bytes/sec",
//				"time (s)", "byte/s", ds, PlotOrientation.VERTICAL, true,
//				false, false);
//		final XYPlot plot = chart.getXYPlot();
//		plot.setDomainCrosshairVisible(true);
//		plot.setRangeCrosshairVisible(true);
//		plot.setRangeCrosshairLockedOnData(true);
//		plot.setBackgroundPaint(Color.WHITE);
//
//		plot.setRangeCrosshairVisible(true);
//		
//		XYItemRenderer render = chart.getXYPlot().getRenderer();
//		render.setSeriesPaint(0, Color.BLUE);

		final ChartPanel chartPanel = new ChartPanel(chart);
		return chartPanel;
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
	public void getResult(ServerInfo serverInfo) {
		
		ArrayList<Double> filter = new ArrayList<Double>();
		ArrayList<Integer> position = new ArrayList<Integer>();
		double mid, sum;
		sum = 0.0;
		for(int i = 0; i < serverInfo.getDiskRead().size(); i++){
			
			mid = (double) serverInfo.getDiskRead().get(i);
			if(mid == -1.0){
				filter.add(1.0);
				position.add(i + 1);
			}
			else if(mid == -2.0)
				continue;
			else{
				filter.add(mid);
				position.add(i + 1);
			}
					
		}
		result = new double[2][filter.size()];
		int size = filter.size();
		for(int j = 0; j < size; j++){
			mid = filter.get(j);
			result[1][j] = mid;
			if(mid > Max)
				Max = mid;
			else if (mid < Min)
				Min = mid;
			sum = mid + sum;
			result[0][j] = position.get(j);
		}
		Mean = sum / size;
		for(int i = 0; i < size; i++)
			Var = Var + (result[1][i]-Mean) * (result[1][i]-Mean);
		Var = Math.sqrt(Var / (size-1));
		
		DecimalFormat df = new DecimalFormat("0.0");
		
		Max = Double.parseDouble(df.format(Max));
		Min = Double.parseDouble(df.format(Min));
		Mean = Double.parseDouble(df.format(Mean));
		Var = Double.parseDouble(df.format(Var));
		
		picPanel.setShowForm(printDISKREADPic());
		this.updateUI();
		restartTest();
  
		
	}
	@Override
	public void restartTest() {
		result = null;
		
	}
private double[][] wipsSmooth() {
		
		int i = result[1].length - 1;
		double[][] smooth;
		if (i > deleteNum * 2) {

			smooth = new double[2][i - deleteNum + 2];
			num = deleteNum / 2 ;
			if (i != -1) {
				smooth[1][0] = 0;
				for (int j = 0; j < deleteNum; j++) {
					smooth[1][0] += result[1][j];
				}
				for (int j = deleteNum; j <= i; j++) {
					smooth[1][j - deleteNum + 1] = smooth[1][j - deleteNum]
							+ result[1][j]
							- result[1][j - deleteNum];
				}
				for (int j = 0; j <= i - deleteNum + 1; j++) {
					smooth[1][j] = smooth[1][j] / deleteNum;
				}
			}
		} else if (i > 0) {
			num = 0;
			smooth = new double[2][i];
			for (int j = 0; j < i; j++) {
				smooth[1][j] = result[1][j];
			}

		} else {
			num = 0;
			smooth = new double[2][1];
			smooth[0][0] = 0;
			smooth[1][0] = 0;
		}

		return smooth;
	}

}
