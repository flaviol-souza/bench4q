package org.bench4Q.console.ui.section;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;

import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.agent.rbe.communication.TypeFrequency;
import org.bench4Q.common.util.Logger;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.model.ConfigModel;

public class M_LoadFrequencyPanel extends JPanel implements ActionListener {

	private JPanel mainPanel;
	private ArrayList<TestPhase> dataSet;
	private ConfigModel m_configModel;
	private JPanel painelButtons;
	private JPanel functionPanel;

	private JLabel lb_startTime;
	private JLabel lb_endTime;
	private JLabel lb_pauseTime;
	private JLabel lb_polarity;
	private JLabel lb_quantity;

	private JTextField tf_startTime;
	private JTextField tf_endTime;
	private JTextField tf_pauseTime;
	private JTextField tf_polarity;
	private JTextField tf_quantity;

	public M_LoadFrequencyPanel(Resources resources, ConfigModel configModel) {
		this.m_configModel = configModel;
		this.dataSet = m_configModel.getArgs().getTestPhase();

		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BorderLayout());

		this.add(this.mainPanel);

		List<String> listFunctions = new ArrayList<String>();
		listFunctions.add("Select");
		for (TypeFrequency type : TypeFrequency.values()) {
			listFunctions.add(type.getName());
		}

		this.painelButtons = new JPanel(new GridLayout());
		JComboBox functionCombo = new JComboBox(listFunctions.toArray());
		painelButtons.add(functionCombo);

		this.mainPanel.add(painelButtons, BorderLayout.NORTH);

		functionPanel = new JPanel();
		functionPanel.setLayout(new GridBagLayout());

		functionPanel.setPreferredSize(new Dimension(400, 200));
		functionPanel.setMinimumSize(new Dimension(300, 200));

		functionCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				TypeFrequency type = TypeFrequency.getType((String) e.getItem());
				if (type != null && e.getStateChange() == ItemEvent.SELECTED) {
					Logger.getLogger().debug(type.getName());
					createPanelFunction(type);
				}
			}
		});

		this.mainPanel.add(functionPanel);

		functionCombo.setSelectedIndex(1);
	}

	private void createPanelFunction(final TypeFrequency type) {

		this.functionPanel.removeAll();
		this.m_configModel.getArgs().setTypeFrenquency(type.getName());
		int row = 0;

		lb_startTime = new JLabel("Start Time");
		tf_startTime = new JTextField(String.valueOf(dataSet.get(0).getFrequency().getStartTime()));
		tf_startTime.getDocument().addDocumentListener(new StartTimeListener());
		functionPanel.add(lb_startTime, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1));
		functionPanel.add(tf_startTime, new GridBagConstraints(1, row++, 1, 1, 100.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));

		lb_endTime = new JLabel("Duration Step");
		tf_endTime = new JTextField(String.valueOf(dataSet.get(0).getFrequency().getEndTime()));
		tf_endTime.getDocument().addDocumentListener(new EndTimeListener());
		functionPanel.add(lb_endTime, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1));
		functionPanel.add(tf_endTime, new GridBagConstraints(1, row++, 1, 1, 100.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));

		if (type.getName().compareTo("Pulse") == 0) {
			lb_pauseTime = new JLabel("Pause");
			tf_pauseTime = new JTextField(String.valueOf(dataSet.get(0).getFrequency().getPauseTime()));
			tf_pauseTime.getDocument().addDocumentListener(new PauseTimeListener());
			functionPanel.add(lb_pauseTime, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1));
			functionPanel.add(tf_pauseTime, new GridBagConstraints(1, row++, 1, 1, 100.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));
		}

		if (type.getName().compareTo("Step") == 0) {
			lb_polarity = new JLabel("Polarity");
			tf_polarity = new JTextField(String.valueOf(dataSet.get(0).getFrequency().getPolarity()));
			tf_polarity.getDocument().addDocumentListener(new PolarityListener());
			functionPanel.add(lb_polarity, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
					GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1));
			functionPanel.add(tf_polarity, new GridBagConstraints(1, row++, 1, 1, 100.0, 0.0, GridBagConstraints.WEST,
					GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));
		}

		lb_quantity = new JLabel("Quantity");
		tf_quantity = new JTextField(String.valueOf(dataSet.get(0).getFrequency().getQuantity()));
		tf_quantity.getDocument().addDocumentListener(new QuantityListener());
		functionPanel.add(lb_quantity, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1));
		functionPanel.add(tf_quantity, new GridBagConstraints(1, row++, 1, 1, 100.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));

		this.functionPanel.updateUI();
		this.functionPanel.repaint();

	}

	private class StartTimeListener implements DocumentListener {
		public void insertUpdate(DocumentEvent event) {
			changeText();
		}

		public void removeUpdate(DocumentEvent event) {
			changeText();
		}

		public void changedUpdate(DocumentEvent event) {
			changeText();
		}

		private void changeText() {
			int value = 0;
			String text = tf_startTime.getText().trim();
			try {
				if ((text != null) && (!text.equals(""))) {
					value = Integer.parseInt(text);
				}
			} catch (NumberFormatException e) {
				value = 0;
				System.err.println(e.getMessage());
			} finally {
				// System.out.println("addLoadListener: "+addLoadValue);
				dataSet.get(0).getFrequency().setStartTime(value);
			}
		}
	}

	private class EndTimeListener implements DocumentListener {
		public void insertUpdate(DocumentEvent event) {
			changeText();
		}

		public void removeUpdate(DocumentEvent event) {
			changeText();
		}

		public void changedUpdate(DocumentEvent event) {
			changeText();
		}

		private void changeText() {
			int value = 0;
			String text = tf_endTime.getText().trim();
			try {
				if ((text != null) && (!text.equals(""))) {
					value = Integer.parseInt(text);
				}
			} catch (NumberFormatException e) {
				value = 0;
				System.err.println(e.getMessage());
			} finally {
				// System.out.println("addLoadListener: "+addLoadValue);
				dataSet.get(0).getFrequency().setEndTime(value);
			}
		}
	}

	private class PauseTimeListener implements DocumentListener {
		public void insertUpdate(DocumentEvent event) {
			changeText();
		}

		public void removeUpdate(DocumentEvent event) {
			changeText();
		}

		public void changedUpdate(DocumentEvent event) {
			changeText();
		}

		private void changeText() {
			int value = 0;
			String text = tf_pauseTime.getText().trim();
			try {
				if ((text != null) && (!text.equals(""))) {
					value = Integer.parseInt(text);
				}
			} catch (NumberFormatException e) {
				value = 0;
				System.err.println(e.getMessage());
			} finally {
				// System.out.println("addLoadListener: "+addLoadValue);
				dataSet.get(0).getFrequency().setPauseTime(value);
			}
		}
	}

	private class PolarityListener implements DocumentListener {
		public void insertUpdate(DocumentEvent event) {
			changeText();
		}

		public void removeUpdate(DocumentEvent event) {
			changeText();
		}

		public void changedUpdate(DocumentEvent event) {
			changeText();
		}

		private void changeText() {
			boolean value = false;
			String text = tf_polarity.getText().trim();
			try {
				if ((text != null) && (!text.equals(""))) {
					value = Boolean.getBoolean(text);
				}
			} catch (NumberFormatException e) {
				value = false;
				System.err.println(e.getMessage());
			} finally {
				// System.out.println("addLoadListener: "+addLoadValue);
				dataSet.get(0).getFrequency().setPolarity(value);
			}
		}
	}

	private class QuantityListener implements DocumentListener {
		public void insertUpdate(DocumentEvent event) {
			changeText();
		}

		public void removeUpdate(DocumentEvent event) {
			changeText();
		}

		public void changedUpdate(DocumentEvent event) {
			changeText();
		}

		private void changeText() {
			int value = 0;
			String text = tf_quantity.getText().trim();
			try {
				if ((text != null) && (!text.equals(""))) {
					value = Integer.parseInt(text);
				}
			} catch (NumberFormatException e) {
				value = 0;
				System.err.println(e.getMessage());
			} finally {
				// System.out.println("addLoadListener: "+addLoadValue);
				dataSet.get(0).getFrequency().setQuantity(value);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
