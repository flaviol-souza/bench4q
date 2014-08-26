package org.bench4Q.console.ui.section;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.agent.rbe.communication.TypeFrequency;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.model.ConfigModel;

public class M_LoadFrequencyPanel extends JPanel implements ActionListener {

	private JTable table;
	private JPanel mainPanel;
	private ArrayList dataSet;
	private JPanel functionPanel;
	private ConfigModel m_configModel;

	private int stdyTime;

	public M_LoadFrequencyPanel(Resources resources, ConfigModel configModel) {
		this.m_configModel = configModel;

		this.getConfigModel();

		this.createPanel();
		this.createButtons();
	}

	private void getConfigModel() {
		for (TestPhase phase : this.m_configModel.getArgs().getEbs()) {
			if (phase.getStdyTime() > stdyTime)
				stdyTime = phase.getStdyTime();
		}
	}

	private void createButtons() {
		final List<String> listFunctions = new ArrayList<String>();

		listFunctions.add("Select");
		for (TypeFrequency type : TypeFrequency.values()) {
			listFunctions.add(type.getName());
		}

		final JPanel painelButtons = new JPanel(new GridLayout());
		mainPanel.add(painelButtons, BorderLayout.NORTH);

		String[] array = new String[listFunctions.size()];
		array = listFunctions.toArray(array);

		final JComboBox<String> functionCombo = new JComboBox<String>(array);
		functionCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				TypeFrequency type = TypeFrequency.getType((String) e.getItem());
				if (type != null) {
					createPanelFunction(type);
				}
			}
		});
		painelButtons.add(functionCombo);
	}

	private void createPanelFunction(final TypeFrequency type) {
		functionPanel = new JPanel(new GridLayout());
		functionPanel.setPreferredSize(new Dimension(690, 480));
		functionPanel.setMinimumSize(new Dimension(300, 200));

		m_configModel.getArgs().setTypeFrenquency(type.getName());
		
		// Object[][] dados = { { stdyTime / 2, stdyTime / 2, "+" } };
		this.dataSet = m_configModel.getArgs().getEbs();

		table = new JTable();
		table.setModel(new MyTableModel(type, this.dataSet));
		// table.setRowHeight(1, 10);
		// table.setSize(100, 30);

		JScrollPane scrollTable = new JScrollPane();
		scrollTable.setViewportView(table);

		functionPanel.add(scrollTable);

		mainPanel.add(functionPanel);
		mainPanel.revalidate();
		mainPanel.repaint();

		m_configModel.addListener(new ConfigModel.AbstractListener() {
			public void isArgsChanged() {
				resetConfig(type);
			}
		});

	}

	protected void resetConfig(TypeFrequency type) {
		dataSet = m_configModel.getArgs().getEbs();
		MyTableModel myTableModel = new MyTableModel( type, dataSet);
		this.table.setModel(myTableModel);
		this.table.revalidate();
	}

	private void createPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		this.add(mainPanel);
	}

	private class MyTableModel extends AbstractTableModel {

		private String[] columnNames = new String[0];
		private ArrayList data = new ArrayList();
		private TypeFrequency type;

		public MyTableModel(TypeFrequency type, ArrayList dataSet) {
			this.columnNames = type.getAttributes();
			this.data = dataSet;
			this.type = type;
		}

		public int getColumnCount() {
			return columnNames.length;
		}

		public int getRowCount() {
			return data.size();
		}

		public String getColumnName(int column) {
			return columnNames[column];
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			return this.type.getValueAt(data, rowIndex, columnIndex);
		}

		public void setValueAt(Object value, int row, int col) {
			this.type.setValueAt(data, value, row, col);
			// resetShowPanel();

		}

		public boolean isCellEditable(int row, int col) {
			return true;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
