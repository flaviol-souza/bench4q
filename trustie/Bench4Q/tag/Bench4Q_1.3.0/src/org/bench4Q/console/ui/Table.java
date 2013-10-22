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
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * A read-only JTable that works in conjunction with an extended TableModel
 * specifies some cell rendering.
 */
public final class Table extends JTable {

	private final MyCellRenderer m_cellRenderer = new MyCellRenderer();
	private final TableCellRenderer m_headerRenderer = new MyHeaderRenderer();
	private final Font m_boldFont;
	private final Font m_plainFont;
	private final Color m_defaultForeground;
	private final Color m_defaultBackground;

	/**
	 * Interface for our extended TableModel.
	 */
	public interface TableModel extends javax.swing.table.TableModel {
		/**
		 * @param row
		 * @param column
		 * @return
		 */
		boolean isBold(int row, int column);

		/**
		 * @param row
		 * @param column
		 * @return <code>null</code> => default colour.
		 */
		Color getForeground(int row, int column);

		/**
		 * @param row
		 * @param column
		 * @return <code>null</code> => default colour.
		 */
		Color getBackground(int row, int column);
	}

	/**
	 * @param tableModel
	 */
	public Table(TableModel tableModel) {
		super(tableModel);

		setRowSelectionAllowed(true);
		setColumnSelectionAllowed(true);
		setDragEnabled(true);

		m_defaultForeground = m_cellRenderer.getForeground();
		m_defaultBackground = m_cellRenderer.getBackground();

		// Believe it or not, I found the font returned from getFont() is bold
		// whereas the renderer normally renders in a plain font. Thus we
		// can't rely on getFont() for the default - go figure!
		m_plainFont = m_cellRenderer.getFont().deriveFont(Font.PLAIN);
		m_cellRenderer.setFont(m_plainFont);
		m_boldFont = m_cellRenderer.getFont().deriveFont(Font.BOLD);

		createDefaultColumnsFromModel();
	}

	/**
	 * Set the header renderer for every column to work around Swing's dumb
	 * optimisation where it assumes the default header renderer is always the
	 * same height. This nearly gives reasonably resize behaviour with J2SE 1.3
	 * (but only on the second resize event), and doesn't work at all for J2SE
	 * 1.4; I'm still investigating.
	 */
	public void addColumn(TableColumn column) {
		column.setHeaderRenderer(m_headerRenderer);
		super.addColumn(column);
	}

	public TableCellRenderer getCellRenderer(int row, int column) {
		final TableModel model = (TableModel) getModel();

		final int modelColumn = getColumnModel().getColumn(column)
				.getModelIndex();

		final Color foreground = model.getForeground(row, modelColumn);
		final Color background = model.getBackground(row, modelColumn);

		final boolean bold = model.isBold(row, modelColumn);

		if (foreground == null && background == null && !bold) {
			return super.getCellRenderer(row, column);
		} else {
			m_cellRenderer.setForeground(foreground != null ? foreground
					: m_defaultForeground);
			m_cellRenderer.setBackground(background != null ? background
					: m_defaultBackground);
			m_cellRenderer.setTheFont(bold ? m_boldFont : m_plainFont);

			return m_cellRenderer;
		}
	}

	private static final class MyCellRenderer extends DefaultTableCellRenderer {
		private Font m_font;

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			final DefaultTableCellRenderer defaultRenderer = (DefaultTableCellRenderer) super
					.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);

			// DefaultTableCellRenderer strangely only supports a
			// single font per Table. We override to set font on a per
			// cell basis.
			defaultRenderer.setFont(m_font);

			return defaultRenderer;
		}

		public void setTheFont(Font f) {
			m_font = f;
		}
	}

	private static final class MyHeaderRenderer implements TableCellRenderer {

		private final JTextArea m_textArea = new JTextArea();

		private MyHeaderRenderer() {
			m_textArea.setLineWrap(true);
			m_textArea.setWrapStyleWord(true);
			m_textArea.setOpaque(false);
			m_textArea.setEditable(false);
		}

		/**
		 * If working out what to do from the Swing source, be aware that
		 * JTableHeader.createDefaultRenderer() uses an anonymous inner class
		 * that overrides
		 * DefaultTableCellRenderer.getTableCellRendererComponent().
		 */
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			if (table != null) {
				final JTableHeader header = table.getTableHeader();

				if (header != null) {
					m_textArea.setForeground(header.getForeground());
					m_textArea.setBackground(header.getBackground());
					m_textArea.setFont(header.getFont());
				}

				// See Java Bug 4760433.
				m_textArea.setSize(table.getColumnModel().getColumn(column)
						.getWidth(), Integer.MAX_VALUE);
			}

			m_textArea.setText((value == null) ? "" : value.toString());
			m_textArea.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

			return m_textArea;
		}
	}
}
