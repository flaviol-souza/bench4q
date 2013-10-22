package org.bench4Q.console.ui.section;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.bench4Q.console.common.Resources;
import org.bench4Q.console.model.ConfigModel;

public class M_UserSettingPanel extends JPanel {

	private final Resources m_resources;

	private ConfigModel m_configModel;

	private JLabel imageLabel;
	private JLabel imageExplain;
	private ButtonGroup group;
	private JRadioButton trueChoise;
	private JRadioButton falseChoise;

	private JLabel retryLabel;
	private JLabel retryExplain;
	private JTextField RetryField;

	private JLabel toleranceExplain;
	private JLabel ttExplain;
	private JLabel toleranceLabel;
	private JLabel ttLabel;
	private JTextField tolerance;
	private JTextField tt;

	public M_UserSettingPanel(Resources resources, ConfigModel fileLoader) {

		m_resources = resources;

		this.m_configModel = fileLoader;

		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(690, 480));
		this.setMinimumSize(new Dimension(300, 200));

		// this.setBorder(new
		// TitledBorder(m_resources.getString("MessSection.section_name")));
		group = new ButtonGroup();

		imageLabel = new JLabel(m_resources.getString("MessSection.imageLabel"),
				SwingConstants.RIGHT);
		trueChoise = addRadioButton(m_resources.getString("MessSection.trueChoise"), fileLoader
				.getArgs().isGetImage());
		falseChoise = addRadioButton(m_resources.getString("MessSection.falseChoise"), !fileLoader
				.getArgs().isGetImage());
		imageExplain = new JLabel(m_resources.getString("MessSection.imageExplain"),
				SwingConstants.LEFT);

		retryLabel = new JLabel(m_resources.getString("MessSection.retryLabel"),
				SwingConstants.RIGHT);
		RetryField = new JTextField(String.valueOf(fileLoader.getArgs().getRetry()));
		RetryField.getDocument().addDocumentListener(new ConfigueListener());
		retryExplain = new JLabel(m_resources.getString("MessSection.retryExplain"),
				SwingConstants.LEFT);

		toleranceLabel = new JLabel(m_resources.getString("GenelPanel.toleranceLabel"),
				SwingConstants.RIGHT);
		ttLabel = new JLabel(m_resources.getString("GenelPanel.ttLabel"), SwingConstants.RIGHT);
		toleranceExplain = new JLabel(m_resources.getString("GenelPanel.toleranceExplain"),
				SwingConstants.LEFT);
		ttExplain = new JLabel(m_resources.getString("GenelPanel.ttExplain"), SwingConstants.LEFT);
		tolerance = new JTextField(String.valueOf(fileLoader.getArgs().getTolerance()));
		tolerance.getDocument().addDocumentListener(new ConfigueListener());

		tt = new JTextField(String.valueOf(fileLoader.getArgs().getThinktime()));
		tt.getDocument().addDocumentListener(new ConfigueListener());

		this.add(imageLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));
		this.add(trueChoise, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));
		this.add(falseChoise, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));
		this.add(imageExplain, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));

		this.add(retryLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));
		this.add(RetryField, new GridBagConstraints(1, 2, 2, 1, 100.0, 0.0,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1,
				1));
		this.add(retryExplain, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));

		this.add(ttLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));
		this.add(tt, new GridBagConstraints(1, 4, 2, 1, 100.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));
		this.add(ttExplain, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));

		this.add(toleranceLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));
		this.add(tolerance, new GridBagConstraints(1, 6, 2, 1, 100.0, 0.0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1));
		this.add(toleranceExplain, new GridBagConstraints(0, 7, 3, 1, 0.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));

		JPanel ConfigURLConSection = new ConfigURLConSection(m_resources, this.m_configModel);
		this.add(ConfigURLConSection, new GridBagConstraints(0, 8, 3, 1, 100.0, 0.0,
				GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 1, 1));

		this.add(new JLabel(), new GridBagConstraints(0, 9, 3, 1, 100.0, 100.0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1));

		m_configModel.addListener(new ConfigModel.AbstractListener() {
			public void isArgsChanged() {
				resetConfig();
			}
		});

	}

	protected void resetConfig() {

		trueChoise.setSelected(m_configModel.getArgs().isGetImage());
		falseChoise.setSelected(!m_configModel.getArgs().isGetImage());
		RetryField.setText(String.valueOf(m_configModel.getArgs().getRetry()));
		tt.setText(String.valueOf(m_configModel.getArgs().getThinktime()));
		tolerance.setText(String.valueOf(m_configModel.getArgs().getTolerance()));

	}

	public JRadioButton addRadioButton(String name, final boolean b) {
		boolean selected = b;
		JRadioButton button = new JRadioButton(name, selected);
		group.add(button);

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				m_configModel.getArgs().setGetImage(b);

			}
		};
		button.addActionListener(listener);
		return button;
	}

	public void setConfigue() {
		int RetryValue = 0;
		double toleranceValue = 0;
		double ttValue = 0;
		try {
			if ((RetryField.getText().trim() != null) && (!RetryField.getText().trim().equals(""))) {
				RetryValue = Integer.parseInt(RetryField.getText().trim());
			}
			if ((tolerance.getText().trim() != null) && (!tolerance.getText().trim().equals(""))) {
				toleranceValue = Double.parseDouble(tolerance.getText().trim());
			}

			if ((tt.getText().trim() != null) && (!tt.getText().trim().equals(""))) {
				ttValue = Double.parseDouble(tt.getText().trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		m_configModel.getArgs().setRetry(RetryValue);
		m_configModel.getArgs().setTolerance(toleranceValue);
		m_configModel.getArgs().setThinktime(ttValue);

	}

	private class ConfigueListener implements DocumentListener {
		public void insertUpdate(DocumentEvent event) {
			setConfigue();
		}

		public void removeUpdate(DocumentEvent event) {
			setConfigue();
		}

		public void changedUpdate(DocumentEvent event) {
			setConfigue();
		}
	}

}
