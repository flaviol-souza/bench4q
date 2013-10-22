package org.bench4Q.console.ui.section;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.Random;

import javax.swing.JPanel;

import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.ui.SwingDispatcherFactory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;

public class LW_ConfigLoadShowSection extends JPanel {

	private final Resources m_resources;
	private final ProcessControl m_processControl;
	private final SwingDispatcherFactory m_swingDispatcherFactory;

	private Boolean[] SelectedResult = new Boolean[15];

	public LW_ConfigLoadShowSection(Resources resources, ProcessControl processControl,
			SwingDispatcherFactory dispatcherFactory) throws ConsoleException {

		m_resources = resources;
		m_processControl = processControl;
		m_swingDispatcherFactory = dispatcherFactory;

		this.setLayout(new GridBagLayout());

		JPanel picPanel = null;
		try {
			picPanel = drawPreviewPic();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// JPanel picPanel = new JPanel();
		this.add(picPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	}

	// print out a WIPS picture.
	public JPanel drawPreviewPic() throws IOException {
		double[][] value = new double[2][70];
		Random radom = new Random();
		for (int i = 0; i < value[0].length; ++i) {
			value[0][i] = i;
			if ((i >= 0) && (i < 10)) {
				value[1][i] = 50 + radom.nextInt(10);
			} else if ((i >= 10) && (i < 20)) {
				value[1][i] = 100 + radom.nextInt(30);
			} else if ((i >= 20) && (i < 30)) {
				value[1][i] = 50 + radom.nextInt(40);
			} else if ((i >= 30) && (i < 40)) {
				value[1][i] = 100 + radom.nextInt(200);
			} else if ((i >= 40) && (i < 50)) {
				value[1][i] = 400 + radom.nextInt(100);
			} else if ((i >= 50) && (i < 60)) {
				value[1][i] = 100 + radom.nextInt(25);
			} else if ((i >= 60) && (i < 70)) {
				value[1][i] = 50 + radom.nextInt(20);
			}
			// value[1][i] = webInteractionThroughput[i];
		}
		DefaultXYDataset ds = new DefaultXYDataset();
		ds.addSeries("test", value);

		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
		String series1 = "Basic";
		// String series2 = "High";

		for (int i = 0; i < value[0].length; ++i) {
			defaultcategorydataset.addValue(value[1][i], series1, new Integer((int) value[0][i]));
		}
		ds.addSeries("test", value);

		JFreeChart chart = ChartFactory.createLineChart("Load Fluctuation", "Test Duration",
				"Load", defaultcategorydataset, PlotOrientation.VERTICAL, true, true, false);
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
		return new ChartPanel(chart);
	}

}
