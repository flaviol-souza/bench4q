package org.bench4Q.console.ui.section;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import org.bench4Q.agent.rbe.communication.TestPhase;
import org.bench4Q.common.util.ListenerSupport;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.model.ConfigModel;
import org.bench4Q.console.model.ConfigModel.Listener;

public class LW_ConfigLoadSection extends JPanel implements ActionListener {

	private final ListenerSupport m_listeners = new ListenerSupport();
	
	private final Resources m_resources;
	JTable table = null;
	JPanel jPanel1 = new JPanel();
	JScrollPane scrollPane = null;
	BorderLayout borderLayout1 = new BorderLayout();
	JButton add = null;
	JButton delete = null;
	JButton deleteAll = null;

	ArrayList dataSet = null;
	private ConfigModel m_configModel;
	private AbstractTableModel MyDatamodel;

	private String colNames[] = new String[5];

	public LW_ConfigLoadSection(Resources resources, ConfigModel configModel) {
		m_resources = resources;
		this.m_configModel = configModel;
		
		colNames[0] = m_resources.getString("LW_ConfigLoadSection.tableColNameBaseLoad");
		colNames[1] = m_resources.getString("LW_ConfigLoadSection.tableColNameRandomLoad");
		colNames[2] = m_resources.getString("LW_ConfigLoadSection.tableColNameRate");
		colNames[3] = m_resources.getString("LW_ConfigLoadSection.tableColNameTriggerTime");
		colNames[4] = m_resources.getString("LW_ConfigLoadSection.tableColNameStdyTime");
		
		this.dataSet = m_configModel.getArgs().getEbs();
		MyDatamodel = new MyTableModel(this.colNames, this.dataSet);
		table = new JTable(MyDatamodel);
		table.setPreferredScrollableViewportSize(new Dimension(100, 120));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		
		
		// Create the scroll pane and add the table to it.
		scrollPane = new JScrollPane(table);

		add = new JButton(m_resources.getString("LW_ConfigLoadSection.increase"));
		delete = new JButton(m_resources.getString("LW_ConfigLoadSection.delete"));
		deleteAll = new JButton(m_resources.getString("LW_ConfigLoadSection.deleteAll"));
		add.addActionListener(this);
		delete.addActionListener(this);
		deleteAll.addActionListener(this);
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		m_configModel.addListener(new ConfigModel.AbstractListener() {
			public void isArgsChanged() {
				resetConfig();
			}
		});

	}
	
	protected void resetConfig() {
		dataSet = m_configModel.getArgs().getEbs();
		MyDatamodel = new MyTableModel(colNames, dataSet);
		this.table.setModel(MyDatamodel);
		this.table.revalidate();
	}

	public JTable getTable() {
		return table;
	}

	private void jbInit() throws Exception {
		this.setLayout(borderLayout1);
		this.add(scrollPane, java.awt.BorderLayout.CENTER);
		jPanel1.add(add);
		jPanel1.add(delete);
		jPanel1.add(deleteAll);
		this.add(jPanel1, java.awt.BorderLayout.NORTH);
	}
	
	public void addListener(Listener listener) {
		m_listeners.add(listener);
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(m_resources.getString("LW_ConfigLoadSection.increase"))) {
			((MyTableModel) (this.table.getModel())).addRow(new TestPhase());
			this.table.revalidate();
		}else if (e.getActionCommand().equals(m_resources.getString("LW_ConfigLoadSection.delete"))) {
			if (table.getSelectedRow() != -1) {
				int nResult;
				nResult = JOptionPane.showConfirmDialog(null, m_resources
						.getString("LW_ConfigLoadSection.delete.confirm"), m_resources
						.getString("LW_ConfigLoadSection.hint"), JOptionPane.YES_NO_OPTION);
				if (nResult == 0) {
					((MyTableModel) (this.table.getModel())).removeRow(table.getSelectedRow());
					this.table.revalidate();
				}
			}
		}else if (e.getActionCommand().equals(m_resources.getString("LW_ConfigLoadSection.deleteAll"))) {
			if (table.getSelectedRow() != -1) {
				int nResult;
				nResult = JOptionPane.showConfirmDialog(null, m_resources
						.getString("LW_ConfigLoadSection.deleteAll.confirm"), m_resources
						.getString("LW_ConfigLoadSection.hint"), JOptionPane.YES_NO_OPTION);
				if (nResult == 0) {
					for (int i = 0; i < ((MyTableModel) (this.table.getModel())).getRowCount(); i++) {
						((MyTableModel) (this.table.getModel())).removeRow(i);
					}
					this.table.revalidate();
				}
			}
		}
		resetShowPanel();
	}
	
	private void resetShowPanel(){
		m_listeners.apply(
			      new ListenerSupport.Informer() {
			        public void inform(Object listener) {
			          ((Listener)listener).isArgsChanged();
			        }
			      });
	}

	private class MyTableModel extends AbstractTableModel {

		private String[] columnNames = new String[0];
		private ArrayList data = new ArrayList();

		public MyTableModel(String[] colNames, ArrayList dataSet) {
			this.columnNames = colNames;
			this.data = dataSet;
		}

		public void removeRow(int selectedRow) {
			data.remove(selectedRow);
			this.fireTableRowsDeleted(0, 0);
		}

		public void addRow(TestPhase testPhase) {
			data.add(testPhase);
			this.fireTableRowsInserted(0, 0);
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
			if (columnIndex == 0) {
				return ((TestPhase) data.get(rowIndex)).getBaseLoad();
			} else if (columnIndex == 1) {
				return ((TestPhase) data.get(rowIndex)).getRandomLoad();
			} else if (columnIndex == 2) {
				return ((TestPhase) data.get(rowIndex)).getRate();
			} else if (columnIndex == 3) {
				return ((TestPhase) data.get(rowIndex)).getTriggerTime();
			} else if (columnIndex == 4) {
				return ((TestPhase) data.get(rowIndex)).getStdyTime();
			} else {

				return null;
			}
		}

		public void setValueAt(Object value, int row, int col) {
			if (col == 0) {
				((TestPhase) data.get(row)).setBaseLoad(Integer.valueOf((String) value));
			} else if (col == 1) {
				((TestPhase) data.get(row)).setRandomLoad(Integer.valueOf((String) value));
			} else if (col == 2) {
				((TestPhase) data.get(row)).setRate(Integer.valueOf((String) value));
			} else if (col == 3) {
				((TestPhase) data.get(row)).setTriggerTime(Integer.valueOf((String) value));
			} else if (col == 4) {
				((TestPhase) data.get(row)).setStdyTime(Integer.valueOf((String) value));
			} else {
			}
			
			resetShowPanel();
			
		}

		public boolean isCellEditable(int row, int col) {
			return true;
		}
	}
	
	public interface Listener extends EventListener {

		void isArgsChanged();

	}

	public abstract static class AbstractListener implements Listener {

		public void isArgsChanged() {
		}
	}

	public JButton getAdd() {
		return add;
	}

	public JButton getDelete() {
		return delete;
	}
	
	public JButton getDeleteAll() {
		return deleteAll;
	}

	public void SaveConfig() {
		m_configModel.getArgs().setEbs(dataSet);
	}

}
