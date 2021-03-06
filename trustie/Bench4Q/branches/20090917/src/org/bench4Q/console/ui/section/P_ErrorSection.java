package org.bench4Q.console.ui.section;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.bench4Q.agent.rbe.communication.ErrorSet;
import org.bench4Q.common.processidentity.AgentIdentity;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.ui.transfer.AgentInfo;
import org.bench4Q.console.ui.transfer.AgentInfoObserver;
import org.bench4Q.console.ui.transfer.AgentsCollection;
import org.bench4Q.console.ui.util.ButtonGenerator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class P_ErrorSection extends JPanel implements AgentInfoObserver {

	private final Resources m_resources;
	private final ProcessControl m_processControl;
	// public Buffer m_lookAndFeel;

	private Boolean[] SelectedResult = new Boolean[15];

	private AgentIdentity m_agentIdentity;
	private Boolean TotalOrNot;
	private AgentsCollection m_agentsCollection;

	private int TotalResultNumber;
	private int resultNumber;

	private ErrorSet[] errors;

	private int testduring;

	private PicPanel picPanel;

	// private final static String[] SERVLETS = { "home_servlet",
	// "shopping_cart_servlet",
	// "order_inquiry_servlet", "order_display_servlet",
	// "search_request_servlet",
	// "execute_search_servlet", "new_products_servlet", "best_sellers_servlet",
	// "product_detail_servlet", "customer_registration_servlet",
	// "buy_request_servlet",
	// "buy_confirm_servlet", "admin_request_servlet", "admin_response_servlet",
	// "Total", };

	private final static String[] SERVLETS = { "init", "admc", "admr", "bess", "buyc", "buyr",
			"creg", "home", "newp", "ordd", "ordi", "prod", "sreq", "sres", "shop" };

	public P_ErrorSection(Resources resources, ProcessControl processControl, Boolean TotalOrNot,
			AgentIdentity agentIdentity, AgentsCollection agentsCollection) {
		m_resources = resources;
		m_processControl = processControl;

		this.TotalOrNot = TotalOrNot;
		m_agentIdentity = agentIdentity;

		m_agentsCollection = agentsCollection;
		m_agentsCollection.registerObserver(this);

		errors = new ErrorSet[15];
		testduring = -1;

		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(550, 445));
		this.setMinimumSize(new Dimension(550, 445));

		JButton rangeButton = ButtonGenerator.newButton(m_resources
				.getString("TestResultWIPSSection.rangeButton"));
		rangeButton.addActionListener(new rangeSelectAction());
		JButton servletButton = ButtonGenerator.newButton(m_resources
				.getString("TestResultWIPSSection.servletButton"));
		servletButton.addActionListener(new servletSelectAction());
		JButton savePicButton = ButtonGenerator.newButton(m_resources
				.getString("TestResultWIPSSection.savePicButton"));
		savePicButton.addActionListener(new savePicAction());

		JButton HistoryButton = ButtonGenerator.newButton(m_resources
				.getString("TestResultSessionSection.HistoryButton"));
		savePicButton.addActionListener(new savePicAction());

		picPanel = new PicPanel();
		this.add(picPanel, new GridBagConstraints(0, 0, 1, 5, 99.0, 99.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1));
		this.add(rangeButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));
		this.add(servletButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1,
				1));
		this.add(savePicButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1,
				1));
		this.add(HistoryButton, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1,
				1));
		this.add(new JLabel(" "), new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1,
				1));

	}

	private JPanel getPanel() {
		return this;
	}

	private class rangeSelectAction implements ActionListener {

		public void actionPerformed(ActionEvent e) {

		}
	}

	// print out a WIPS picture.
	private JPanel printErrorPic() throws IOException {
		CategoryDataset dataset = getDataSet();
		JFreeChart chart = ChartFactory.createBarChart3D("Error", "Error type", "Error number",
				dataset, PlotOrientation.VERTICAL, true, // 是否显示图例(对于简单的柱状图必须是false)
				true, // 是否生成工具
				true // 是否生成URL链接
				);
		CategoryPlot plot = chart.getCategoryPlot();
		// 设置图表的纵轴和横轴org.jfree.chart.axis.CategoryAxis
		org.jfree.chart.axis.CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setLowerMargin(0.1);// 设置距离图片左端距离此时为10%
		domainAxis.setUpperMargin(0.1);// 设置距离图片右端距离此时为百分之10
		domainAxis.setCategoryLabelPositionOffset(10);// 图表横轴与标签的距离(10像素)
		domainAxis.setCategoryMargin(0.2);// 横轴标签之间的距离20%
		// domainAxis.setMaximumCategoryLabelLines(1);
		// domainAxis.setMaximumCategoryLabelWidthRatio(0);

		// 设定柱子的属性
		org.jfree.chart.axis.ValueAxis rangeAxis = plot.getRangeAxis();
		rangeAxis.setUpperMargin(0.1);// 设置最高的一个柱与图片顶端的距离(最高柱的10%)

		// 设置图表的颜色
		org.jfree.chart.renderer.category.BarRenderer3D renderer;
		renderer = new org.jfree.chart.renderer.category.BarRenderer3D();
		renderer.setBaseOutlinePaint(Color.red);
		renderer.setSeriesPaint(0, new Color(0, 255, 255));// 计划柱子的颜色为青色
		renderer.setSeriesOutlinePaint(0, Color.BLACK);// 边框为黑色
		renderer.setSeriesPaint(1, new Color(0, 255, 0));// 实报柱子的颜色为绿色
		renderer.setSeriesOutlinePaint(1, Color.red);// 边框为红色
		renderer.setItemMargin(0.1);// 组内柱子间隔为组宽的10%
		// 显示每个柱的数值，并修改该数值的字体属性
		renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setItemLabelFont(new Font("黑体", Font.BOLD, 12));// 12号黑体加粗
		renderer.setItemLabelPaint(Color.black);// 字体为黑色
		renderer.setItemLabelsVisible(true);
		plot.setRenderer(renderer);// 使用我们设计的效果

		// 设置纵横坐标的显示位置
		plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		return new ChartPanel(chart);
	}

	private CategoryDataset getDataSet() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < SERVLETS.length; i++) {
			dataset.addValue(((ErrorSet) errors[i]).getResult().size(), SERVLETS[i],
					"Errors of Servlet");
		}
		return dataset;
	}

	private class servletSelectAction implements ActionListener {
		private Checkbox[] m_option = new Checkbox[15];

		public void actionPerformed(ActionEvent e) {

			for (int i = 0; i < 15; i++) {
				m_option[i] = new Checkbox(SERVLETS[i]);
				if (SelectedResult[i] != null) {
					m_option[i].setState(SelectedResult[i]);
				}

			}

			String[] decide = { "OK", "Cancel" };

			int result = JOptionPane.showOptionDialog(getPanel(), m_option,
					"OptionPaneDemo.componenttitle", JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, decide, decide[0]);

			switch (result) {
			case 0:
				getResult();
				break;
			case 1:
				break;
			default:
				break;
			}
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
		if (TotalOrNot) {
			TotalResultNumber = m_agentsCollection.getAgentNumber();
		}

		ErrorSet[] result = agentInfo.getStats().getErrors();
		if (!TotalOrNot && agentInfo.getAgentIdentity().equals(m_agentIdentity)) {
			for (int i = 0; i < 15; i++) {
				this.errors[i] = result[i];
			}
		} else if (TotalOrNot) {
			for (int i = 0; i < 15; i++) {
				if (this.errors[i] == null) {
					this.errors[i] = new ErrorSet();
				}
				this.errors[i].getResult().addAll(result[i].getResult());

			}
			resultNumber++;

		} else {
			return;
		}

		if (AllResultReceived()) {
			try {
				picPanel.setShowForm(printErrorPic());
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.updateUI();
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
		errors = new ErrorSet[15];
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
			panel.add(noResultLabel, java.awt.BorderLayout.CENTER);
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
