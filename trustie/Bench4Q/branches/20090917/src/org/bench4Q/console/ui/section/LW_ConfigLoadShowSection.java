package org.bench4Q.console.ui.section;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.model.ConfigModel;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class LW_ConfigLoadShowSection extends JPanel {

	private final Resources m_resources;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;
	private ConfigModel m_configModel;
	private Args m_args;

	private PicPanel picPanel;
	private Boolean[] SelectedResult = new Boolean[15];

	public LW_ConfigLoadShowSection(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory, ConfigModel configModel, LW_ConfigLoadSection configLoadSection)
			throws ConsoleException {

		m_resources = resources;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;
		m_configModel = configModel;
		m_args = m_configModel.getArgs();

		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(683, 475));
		this.setMinimumSize(new Dimension(683, 475));

		picPanel = new PicPanel(m_args);
		// JPanel picPanel = new JPanel();
		this.add(picPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		m_configModel.addListener(new ConfigModel.AbstractListener() {
			public void isArgsChanged() {
				resetConfig();
			}
		});
		configLoadSection.addListener(new LW_ConfigLoadSection.AbstractListener() {
			public void isArgsChanged() {
				resetConfig();
			}
		});

	}

	protected void resetConfig() {
		m_args = m_configModel.getArgs();
		try {
			picPanel.setShowForm(drawPreviewPic(m_args));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public JPanel drawPreviewPic(Args args) throws IOException {
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		XYSeries xySeries = new XYSeries("Basic");

		ArrayList<TestPhase> ebs = args.getEbs();
		int prepairTime = args.getPrepair();
		int cooldownTime = args.getCooldown();

		ArrayList<XYSeries> series = new ArrayList<XYSeries>();
		int i = 1;
		for (TestPhase phase : ebs) {
			XYSeries seriesBasic = new XYSeries("Basic " + i);
			XYSeries seriesRandom = new XYSeries("Random " + i);
			series.add(seriesBasic);
			series.add(seriesRandom);
			int startTime = prepairTime + phase.getTriggerTime();
			int endTime = startTime + phase.getStdyTime();
			int startLoad = phase.getBaseLoad();
			int endLoad = startLoad + phase.getStdyTime() * phase.getRate();
			int startRadomLoad = startLoad + phase.getRandomLoad();
			int endRadomLoad = endLoad + phase.getRandomLoad();

			seriesBasic.add(new Double(startTime), new Integer(0));
			seriesBasic.add(new Double(startTime), new Integer(startLoad));
			seriesBasic.add(new Double(endTime), new Integer(endLoad));
			seriesBasic.add(new Double(endTime), new Integer(0));
			dataset.addSeries(seriesBasic);

			seriesRandom.add(new Double(startTime), new Integer(endLoad));
			seriesRandom.add(new Double(startTime), new Integer(startRadomLoad));
			seriesRandom.add(new Double(endTime), new Integer(endRadomLoad));
			seriesRandom.add(new Double(endTime), new Integer(0));
			dataset.addSeries(seriesRandom);

			i++;

		}

		final JFreeChart chart = ChartFactory.createXYStepAreaChart("Stacked Area Chart", // chart
				// title
				"time", // domain axis label
				"load", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, false);

		chart.setBackgroundPaint(Color.white);

		// color
        final XYPlot plot = chart.getXYPlot();
        
        int j = 0;
        for(XYSeries s : series){
        	
        	if (j%2 == 0) {
        		plot.getRenderer().setSeriesPaint(j, Color.white);
			} else {
				plot.getRenderer().setSeriesPaint(j, Color.gray);
			}
        	j++;
        }

        // fill shapes      
        final XYStepAreaRenderer rend = (XYStepAreaRenderer) plot.getRenderer();
        rend.setShapesFilled(true);

		return new ChartPanel(chart);
	}

	private class PicPanel extends JPanel {
		// BorderLayout borderLayout = new BorderLayout();
		JPanel panel;

		public PicPanel(Args args) {
			this.setLayout(new GridBagLayout());
			try {
				panel = drawPreviewPic(args);
			} catch (IOException e) {
				e.printStackTrace();
			}

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
