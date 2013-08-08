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

package org.bench4Q.console.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.bench4Q.common.processidentity.AgentProcessReport;
import org.bench4Q.common.processidentity.ProcessReport;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.ProcessReportDescriptionFactory;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.common.ProcessReportDescriptionFactory.ProcessDescription;
import org.bench4Q.console.communication.ProcessControl;

/**
 * TableModel for the process status table. No need to synchronise, all calls
 * after initialisation are dispatched to us in the SwingThread.
 */
public class ProcessStatusTableModel extends AbstractTableModel implements
		Table.TableModel {

	private static final int NAME_COLUMN_INDEX = 0;
	private static final int TYPE_COLUMN_INDEX = 1;
	private static final int STATE_COLUMN_INDEX = 2;

	private final Comparator m_processReportComparator = new ProcessReport.StateThenNameThenNumberComparator();

	private final Comparator m_processReportsComparator = new ProcessControl.ProcessReportsComparator();

	private final ProcessReportDescriptionFactory m_descriptionFactory;

	private final String[] m_columnHeadings;
	private final String m_workerProcessesString;
	private final String m_threadsString;

	private Row[] m_data = new Row[0];

	/**
	 * @param resources
	 * @param processControl
	 * @param swingDispatcherFactory
	 * @throws ConsoleException
	 */
	public ProcessStatusTableModel(Resources resources,
			ProcessControl processControl,
			SwingDispatcherFactory swingDispatcherFactory)
			throws ConsoleException {

		m_descriptionFactory = new ProcessReportDescriptionFactory(resources);

		m_columnHeadings = new String[3];
		m_columnHeadings[NAME_COLUMN_INDEX] = resources
				.getString("processTable.nameColumn.label");
		m_columnHeadings[TYPE_COLUMN_INDEX] = resources
				.getString("processTable.processTypeColumn.label");
		m_columnHeadings[STATE_COLUMN_INDEX] = resources
				.getString("processTable.stateColumn.label");

		m_workerProcessesString = resources
				.getString("processTable.processes.label");
		m_threadsString = resources.getString("processTable.threads.label");

		processControl
				.addProcessStatusListener((ProcessControl.Listener) swingDispatcherFactory
						.create(new ProcessControl.Listener() {
							public void update(
									ProcessControl.ProcessReports[] processReports) {
								final List rows = new ArrayList();
								int runningThreads = 0;
								int totalThreads = 0;
								int workerProcesses = 0;

								Arrays.sort(processReports,
										m_processReportsComparator);

								for (int i = 0; i < processReports.length; ++i) {
									final AgentProcessReport agentProcessStatus = processReports[i]
											.getAgentProcessReport();
									rows
											.add(new ProcessDescriptionRow(
													m_descriptionFactory
															.create(agentProcessStatus)));
									;

									Arrays.sort(processReports,
											m_processReportsComparator);
								}

								rows.add(new TotalRow(runningThreads,
										totalThreads, processReports.length));

								m_data = (Row[]) rows.toArray(new Row[rows
										.size()]);

								fireTableDataChanged();
							}
						}));
	}

	public int getColumnCount() {
		return m_columnHeadings.length;
	}

	public String getColumnName(int column) {
		return m_columnHeadings[column];
	}

	public int getRowCount() {
		return m_data.length;
	}

	public Object getValueAt(int row, int column) {

		if (row < m_data.length) {
			return m_data[row].getValueForColumn(column);
		} else {
			return "";
		}
	}

	public boolean isBold(int row, int column) {
		return row == m_data.length - 1;
	}

	public Color getForeground(int row, int column) {
		return null;
	}

	public Color getBackground(int row, int column) {
		return null;
	}

	private abstract class Row {
		abstract String getName();

		abstract String getIP();

		abstract String getState();

		public String getValueForColumn(int column) {
			switch (column) {
			case NAME_COLUMN_INDEX:
				return getName();

			case TYPE_COLUMN_INDEX:
				return getIP();

			case STATE_COLUMN_INDEX:
				return getState();

			default:
				return "?";
			}
		}
	}

	private class ProcessDescriptionRow extends Row {
		private final ProcessDescription m_description;

		public ProcessDescriptionRow(ProcessDescription description) {
			m_description = description;
		}

		public String getName() {
			return m_description.getName();
		}

		public String getIP() {
			return m_description.getIP();
		}

		public String getState() {
			return m_description.getState();
		}
	}

	private class IndentedNameRow extends Row {
		private final String m_indent;
		private final Row m_delegate;

		public IndentedNameRow(String indent, Row row) {
			m_indent = indent;
			m_delegate = row;
		}

		public String getName() {
			return m_indent + m_delegate.getName();
		}

		public String getIP() {
			return m_delegate.getIP();
		}

		public String getState() {
			return m_delegate.getState();
		}
	}

	private final class TotalRow extends Row {
		private final int m_runningThreads;
		private final int m_totalThreads;
		private final int m_workerProcesses;

		public TotalRow(int runningThreads, int totalThreads,
				int workerProcesses) {
			m_runningThreads = runningThreads;
			m_totalThreads = totalThreads;
			m_workerProcesses = workerProcesses;
		}

		public String getName() {
			return "";
		}

		public String getIP() {
			return "";
		}

		public String getState() {
			return "" + m_workerProcesses + " " + m_workerProcessesString;
		}
	}
}
