package org.bench4Q.console.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ListModelInterval extends JPanel {

	JList<Double> list;

	DefaultListModel model;

	public ListModelInterval() {
		setLayout(new BorderLayout());
		model = new DefaultListModel();
		list = new JList<Double>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane pane = new JScrollPane(list);
		
		JButton addButton = new JButton("Add Interval");
		JButton editButton = new JButton("Edit Selected");
		JButton removeButton = new JButton("Remove Selected");


		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                String value = JOptionPane.showInputDialog(ListModelInterval.this, "Input value interval" );
				model.addElement(Double.valueOf(value));
			}
		});

		editButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				if (model.getSize() > 0){
					String value = JOptionPane.showInputDialog(ListModelInterval.this, "Edit value interval", list.getSelectedValue());
					model.setElementAt(value, list.getSelectedIndex());
				}
			}
		});

		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (model.getSize() > 0)
					model.removeElement(list.getSelectedValue());
			}
		});

		add(pane, BorderLayout.NORTH);
		add(addButton, BorderLayout.LINE_START);
		add(editButton, BorderLayout.CENTER);
		add(removeButton, BorderLayout.LINE_END);
	}
	
	public List<Double> getListInterval(){
		List<Double> listInterval = new ArrayList<Double>();
		for (int i = 0; i < list.getModel().getSize(); i++) {
			listInterval.add((Double)list.getModel().getElementAt(i));
        }
		
		return listInterval;
	}

}
