package org.bench4Q.console.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.bench4Q.common.util.Logger;

public class ListModelInterval extends JPanel {

	private JList<Double> list;

	private DefaultListModel model;

	public ListModelInterval() {
		setLayout(new BorderLayout());
		this.model = new DefaultListModel();
		this.list = new JList<Double>(model);
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane pane = new JScrollPane(list);

		JButton exportButton = new JButton("Export intervals list");
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (model.getSize() > 0) {
					File file = prepareFile(false);
					if (file != null) {
						try {
							FileWriter outstream = new FileWriter(file);
							for (Double inteval : getListInterval()) {
								outstream.write(inteval.toString() + "\n");
							}
							outstream.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}else{
					Logger.getLogger().info("Nothing to export ...");
				}
			}
		});

		JButton importButton = new JButton("Import intervals list");
		importButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = prepareFile(true);
				if (file != null) {
					try {
						Scanner scanner = new Scanner(file);
						// scanner.useDelimiter(",");
						while (scanner.hasNext()) {
							model.addElement(Double.valueOf(scanner.next()));
						}
						scanner.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				} else {
					Logger.getLogger().info("Null file ...");
				}
			}
		});

		JButton addButton = new JButton("Add Interval");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String value = JOptionPane.showInputDialog(ListModelInterval.this, "New interval");
				model.addElement(Double.valueOf(value));
			}
		});

		JButton editButton = new JButton("Edit Selected");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (model.getSize() > 0) {
					String value = JOptionPane.showInputDialog(ListModelInterval.this, "Edit interval",
							list.getSelectedValue());
					model.setElementAt(value, list.getSelectedIndex());
				}
			}
		});

		JButton removeButton = new JButton("Remove Selected");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (model.getSize() > 0)
					model.removeElement(list.getSelectedValue());
			}
		});

		add(pane);

		JPanel borderPanel = new JPanel();
		borderPanel.setPreferredSize(new Dimension(this.getWidth(), 90));
		borderPanel.setBorder(BorderFactory.createTitledBorder("Options"));
		borderPanel.setOpaque(true);
		borderPanel.setBackground(Color.WHITE);

		borderPanel.add(importButton, BorderLayout.NORTH);
		borderPanel.add(exportButton, BorderLayout.LINE_START);
		borderPanel.add(addButton, BorderLayout.CENTER);
		borderPanel.add(editButton, BorderLayout.LINE_END);
		borderPanel.add(removeButton, BorderLayout.SOUTH);

		add(borderPanel, BorderLayout.SOUTH);
	}

	private File prepareFile(boolean toOpen) {
		JFileChooser saveFile = new JFileChooser();
		saveFile.setFileSelectionMode(JFileChooser.FILES_ONLY);

		FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("CVS Documents (*.csv)", "csv");
		saveFile.addChoosableFileFilter(fileNameExtensionFilter);
		saveFile.setFileFilter(fileNameExtensionFilter);

		int result = -1;
		if(toOpen)
			result = saveFile.showOpenDialog(this);
		else
			result = saveFile.showSaveDialog(this);
		
		if (result == JFileChooser.CANCEL_OPTION)
			return null;

		File file = saveFile.getSelectedFile();
		String filePath = file.getAbsolutePath();
		if (!filePath.endsWith("." + fileNameExtensionFilter.getExtensions()[0])) {
			file = new File(filePath + "." + fileNameExtensionFilter.getExtensions()[0]);
		}

		return file;
	}

	public List<Double> getListInterval() {
		List<Double> listInterval = new ArrayList<Double>();
		for (int i = 0; i < list.getModel().getSize(); i++) {
			listInterval.add(list.getModel().getElementAt(i));
		}

		return listInterval;
	}

}
