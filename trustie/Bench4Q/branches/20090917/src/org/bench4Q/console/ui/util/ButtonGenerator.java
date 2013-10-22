package org.bench4Q.console.ui.util;

import java.awt.Dimension;

import javax.swing.JButton;

public class ButtonGenerator {

	public static JButton newButton(String label) {

		JButton button = new JButton(label);
		button.setMinimumSize(new Dimension(120, 30));
		button.repaint();
		return button;
	}

}
