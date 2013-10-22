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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.bench4Q.common.Bench4QException;
import org.bench4Q.console.ConsoleFoundation;
import org.bench4Q.console.ConsoleProperties;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.ErrorHandler;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.model.ConfigModel;
import org.bench4Q.console.model.ResultModel;
import org.bench4Q.console.ui.controlTree.Bench4QTreeModel;
import org.bench4Q.console.ui.controlTree.Bench4QTreeNode;
import org.bench4Q.console.ui.transfer.AgentsCollection;

/**
 * @author duanzhiquan
 * 
 */
public final class ConsoleUI implements ConsoleFoundation.UI {

	private ConfigModel m_configModel;
	private ResultModel m_resultModel;

	private final ActionTable m_actionTable = new ActionTable();
	private final CollectingResultAction m_collectingResultAction;
	private final StartAction m_startAction;
	private final ExitAction m_exitAction;
	private final StopAction m_stopAction;

	private final SavePlanAction m_savePlanAction;
	private final SavePlanAsAction m_savePlanAsAction;

	private final Resources m_resources;
	private final ConsoleProperties m_properties;
	private final ProcessControl m_processControl;

	private JFrame m_frame;
	private static final int windowHeight = 610;
	private static final int leftWidth = 170;
	private static final int rightWidth = 730;
	private static final int windowWidth = leftWidth + rightWidth;

	Bench4QTreeModel m_treeModel;
	JTree m_tree;

	JScrollPane m_treeView = null;
	JScrollPane m_mainView = null;
	JSplitPane m_splitPane = null;
	MainPanel m_mainPanel;

	private final ErrorHandler m_errorHandler;
	private final OptionalConfirmDialog m_optionalConfirmDialog;
	private final Font m_titleLabelFont;
	final SwingDispatcherFactory m_swingDispatcherFactory;

	private AgentsCollection m_agentsCollection;
	private int totalAgentNumber;

	/**
	 * @param resources
	 * @param consoleProperties
	 * @param processControl
	 * @throws ConsoleException
	 */
	public ConsoleUI(Resources resources, ConsoleProperties consoleProperties,
			ProcessControl processControl) throws ConsoleException {

		m_resources = resources;
		m_properties = consoleProperties;
		m_processControl = processControl;

		m_configModel = new ConfigModel();
		m_resultModel = new ResultModel(m_resources);

		// Create the frame to contain the a menu and the top level pane.
		// Do before actions are constructed as we use the frame to create
		// dialogs.
		m_frame = new JFrame(m_resources.getString("title"));

		final ErrorDialogHandler errorDialogHandler = new ErrorDialogHandler(
				m_frame, m_resources);

		m_errorHandler = (ErrorHandler) new SwingDispatcherFactoryImplementation(
				null).create(errorDialogHandler);

		m_swingDispatcherFactory = new SwingDispatcherFactoryImplementation(
				m_errorHandler);

		m_optionalConfirmDialog = new OptionalConfirmDialog(m_frame,
				m_resources, m_properties);

		m_collectingResultAction = new CollectingResultAction();

		m_exitAction = new ExitAction();
		m_startAction = new StartAction();
		m_stopAction = new StopAction();
		m_savePlanAction = new SavePlanAction();
		m_savePlanAsAction = new SavePlanAsAction();

		m_actionTable.add(m_collectingResultAction);

		m_actionTable.add(m_exitAction);
		m_actionTable.add(m_startAction);
		m_actionTable.add(m_stopAction);

		m_actionTable.add(new NewPlanAction());
		m_actionTable.add(new OpenPlanAction());
		m_actionTable.add(m_savePlanAsAction);
		m_actionTable.add(new SavePlanAction());
		m_actionTable.add(new SaveResultsAction());

		m_actionTable.add(new AboutAction(m_resources
				.getImageIcon("logo.image")));
		m_actionTable.add(new StartProcessesAction());

		m_actionTable.add(new OptionsAction());
		m_actionTable.add(new ResetProcessesAction());

		m_actionTable.add(new StopProcessesAction());

		// Create the tabbed test display.
		final JTabbedPane tabbedPane = new JTabbedPane();

		final JSplitPane splitPane;

		m_agentsCollection = new AgentsCollection(m_resources,
				m_processControl, m_swingDispatcherFactory);
		m_resultModel.setAgentsCollection(m_agentsCollection);
		m_treeModel = new Bench4QTreeModel(m_resources, m_processControl,
				m_swingDispatcherFactory, m_configModel, m_resultModel,
				m_agentsCollection);
		m_tree = new JTree(m_treeModel);
		m_tree.setRootVisible(false);
		m_tree.setShowsRootHandles(true);
		m_treeModel.setTree(m_tree);
		m_tree.setCellRenderer(new MyRenderer());

		m_treeView = new JScrollPane(m_tree);

		m_tree
				.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent e) {
						try {
							controlTreeValueChanged(e);
						} catch (ConsoleException e1) {
							e1.printStackTrace();
						}
					}
				});
		expandAll(m_tree, new TreePath(m_treeModel.getRoot()), true);

		m_mainPanel = new MainPanel(m_resources);

		m_mainView = new JScrollPane(m_mainPanel);
		m_mainView.setPreferredSize(new Dimension(rightWidth, windowHeight));

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_treeView,
				m_mainView);
		splitPane.setContinuousLayout(true);
		splitPane.setDividerLocation(leftWidth);

		m_titleLabelFont = new JLabel().getFont().deriveFont(
				Font.PLAIN | Font.ITALIC);

		final JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.add(tabbedPane, BorderLayout.CENTER);

		// Create a panel to hold the tool bar and the test pane.
		final JPanel toolBarPanel = new JPanel(new BorderLayout());
		final JToolBar mainToolBar = new JToolBar();
		new ToolBarAssembler(mainToolBar, false).populate("main.toolbar");
		toolBarPanel.add(mainToolBar, BorderLayout.NORTH);
		toolBarPanel.add(splitPane, BorderLayout.CENTER);

		// set the preferred size of the demo
		m_frame.setPreferredSize(new Dimension(windowWidth + 10,
				windowHeight + 10));
		m_frame.setMinimumSize(new Dimension(windowWidth + 10,
				windowHeight + 10));
		m_frame.setBackground(Color.lightGray);

		// Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = m_frame.getSize();

		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		m_frame.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);

		m_frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		m_frame.addWindowListener(new WindowCloseAdapter());

		final Container topLevelPane = m_frame.getContentPane();
		final JMenuBar menuBar = new JMenuBar();
		new MenuBarAssembler(menuBar).populate("menubar");
		topLevelPane.add(menuBar, BorderLayout.NORTH);
		topLevelPane.add(toolBarPanel, BorderLayout.CENTER);

		final ImageIcon logoIcon = m_resources.getImageIcon("logo.image");

		if (logoIcon != null) {
			final Image logoImage = logoIcon.getImage();

			if (logoImage != null) {
				m_frame.setIconImage(logoImage);
			}
		}

		m_frame.setVisible(true);
	}

	private void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path, expand);
			}
		} // Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

	void controlTreeValueChanged(TreeSelectionEvent e) throws ConsoleException {

		Bench4QTreeNode node = (Bench4QTreeNode) m_tree
				.getLastSelectedPathComponent();
		TreePath pathnode = e.getNewLeadSelectionPath();

		if (m_tree.isVisible(pathnode)) {
			m_mainPanel.setShowForm(node.getPanel());
		}

	}

	private void CreateActionTable() {

	}

	private abstract class ListTokeniserTemplate {
		private final JComponent m_component;

		protected ListTokeniserTemplate(JComponent component) {
			m_component = component;
		}

		public void populate(String key) {
			final String tokens = m_resources.getString(key);
			final Iterator iterator = Collections.list(
					new StringTokenizer(tokens)).iterator();

			while (iterator.hasNext()) {
				final String itemKey = (String) iterator.next();

				if ("-".equals(itemKey)) {
					dash();
				} else if (">".equals(itemKey)) {
					greaterThan();
				} else {
					token(itemKey);
				}
			}
		}

		protected final JComponent getComponent() {
			return m_component;
		}

		protected void dash() {
		}

		protected void greaterThan() {
		}

		protected abstract void token(String key);
	}

	private abstract class AbstractMenuAssembler extends ListTokeniserTemplate {

		protected AbstractMenuAssembler(JComponent component) {
			super(component);
			new MnemonicHeuristics(component);
		}

		protected void token(String menuItemKey) {
			final JMenuItem menuItem = new JMenuItem() {
				private static final long serialVersionUID = 7064759693327950575L;

				public Dimension getPreferredSize() {
					final Dimension d = super.getPreferredSize();
					d.height = (int) (d.height * 0.9);
					return d;
				}
			};

			m_actionTable.setAction(menuItem, menuItemKey);

			final Icon icon = menuItem.getIcon();

			final Icon rolloverIcon = (Icon) menuItem.getAction().getValue(
					CustomAction.ROLLOVER_ICON);

			menuItem.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					menuItem.setIcon(menuItem.isArmed() ? rolloverIcon : icon);
				}
			});

			getComponent().add(menuItem);
		}
	}

	private final class MenuAssembler extends AbstractMenuAssembler {

		protected MenuAssembler(JMenu component) {
			super(component);
		}

		protected void dash() {
			((JMenu) getComponent()).addSeparator();
		}
	}

	private final class PopupMenuAssembler extends AbstractMenuAssembler {

		protected PopupMenuAssembler(JPopupMenu component) {
			super(component);

			component.addContainerListener(new ContainerAdapter() {
				public void componentAdded(ContainerEvent e) {
					if (e.getChild() instanceof JMenuItem) {
						final JMenuItem menuItem = (JMenuItem) e.getChild();

						menuItem.setVisible(((CustomAction) menuItem
								.getAction()).isRelevantToSelection());

						menuItem.getAction().addPropertyChangeListener(
								new PropertyChangeListener() {
									public void propertyChange(
											PropertyChangeEvent evt) {
										if (evt
												.getPropertyName()
												.equals(
														CustomAction.RELEVANT_TO_SELECTION)) {
											menuItem
													.setVisible(((CustomAction) menuItem
															.getAction())
															.isRelevantToSelection());
										}
									}
								});
					}
				}
			});
		}

		protected void dash() {
			((JPopupMenu) getComponent()).addSeparator();
		}
	}

	private final class MenuBarAssembler extends ListTokeniserTemplate {

		protected MenuBarAssembler(JComponent component) {
			super(component);
			new MnemonicHeuristics(component);
		}

		protected void greaterThan() {
			getComponent().add(Box.createHorizontalGlue());
		}

		protected void token(String key) {
			final JMenu menu = new JMenu(m_resources.getString(key
					+ ".menu.label"));

			new MenuAssembler(menu).populate(key + ".menu");

			getComponent().add(menu);
		}
	}

	private final class ToolBarAssembler extends ListTokeniserTemplate {

		private final boolean m_small;

		protected ToolBarAssembler(JComponent component, boolean small) {
			super(component);
			m_small = small;
		}

		protected void dash() {
			((JToolBar) getComponent()).addSeparator();
		}

		protected void token(String key) {
			final JButton button = new CustomJButton();

			if (m_small) {
				button.setBorder(BorderFactory.createRaisedBevelBorder());
			}

			getComponent().add(button);

			// Must set the action _after_ adding to the tool bar or the
			// rollover image isn't set correctly.
			m_actionTable.setAction(button, key);
		}
	}

	private static class ActionTable {
		private final Map m_map = new HashMap();

		public void add(CustomAction action) {
			m_map.put(action.getKey(), action);
		}

		public void setAction(AbstractButton button, String actionKey) {
			final CustomAction action = (CustomAction) m_map.get(actionKey);

			if (action != null) {
				button.setAction(action);
				action.registerButton(button);
			} else {
				System.err.println("Action '" + actionKey + "' not found");
				button.setEnabled(false);
			}
		}
	}

	private final class WindowCloseAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			m_exitAction.exit();
		}
	}

	private final class SaveResultsAction extends CustomAction {
		private final JFileChooser m_fileChooser = new JFileChooser(".");

		public SaveResultsAction() {
			super(m_resources, "save-results", true);
			m_processControl
					.addProcessStatusListener(new EnableIfAgentsFinished(this));
			m_fileChooser.setDialogTitle(MnemonicHeuristics
					.removeMnemonicMarkers(m_resources
							.getString("save-results.label")));

			final String Text = m_resources.getString("bench4Q.result.label");

			m_fileChooser.addChoosableFileFilter(new FileFilter() {
				public boolean accept(File file) {
					return isBQFile(file) || file.isDirectory();
				}

				public String getDescription() {
					return Text;
				}
			});
		}

		public void actionPerformed(ActionEvent event) {

			File currentFile = m_configModel.getSelectedFile();
			if (currentFile != null) {
				m_fileChooser.setSelectedFile(currentFile);
			}

			if (m_fileChooser.showSaveDialog(m_frame) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			File file = m_fileChooser.getSelectedFile();
			String fname = m_fileChooser.getName(file);

			if (fname != null && fname.trim().length() > 0) {
				if (fname.equalsIgnoreCase("bq"))
					;
				else {
					fname = fname.concat(".bq");
				}
			}
			if (file.isFile())
				fname = file.getName();
			file = m_fileChooser.getCurrentDirectory();

			file = new File(file.getPath().concat(File.separator).concat(fname));
			if (file.exists()) {
				if (JOptionPane.showConfirmDialog(m_frame, m_resources
						.getString("saveResultCoverConfirm.text"),
						(String) getValue(NAME), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
					return;

				}

			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

			m_resultModel.setSelectedFile(file);
			m_resultModel.SaveToFile();
		}
	}

	private final class OptionsAction extends CustomAction {
		private final OptionsDialogHandler m_optionsDialogHandler;

		OptionsAction() {
			super(m_resources, "options", true);

			m_optionsDialogHandler = new OptionsDialogHandler(m_frame,
					m_properties, m_resources) {
				protected void setNewOptions(ConsoleProperties newOptions) {
					m_properties.set(newOptions);
					// m_samplingControlPanel.refresh();
				}
			};
		}

		public void actionPerformed(ActionEvent event) {
			m_optionsDialogHandler.showDialog(m_properties);
		}
	}

	private final class AboutAction extends CustomAction {

		private final ImageIcon m_logoIcon;

		AboutAction(ImageIcon logoIcon) {
			super(m_resources, "about", true);
			m_logoIcon = logoIcon;
		}

		public void actionPerformed(ActionEvent event) {

			final Resources resources = m_resources;

			final String title = MnemonicHeuristics
					.removeMnemonicMarkers(resources.getString("about.label"));
			final String aboutText = resources.getStringFromFile("about.text",
					true);

			final JEditorPane htmlPane = new JEditorPane("text/html", aboutText);
			htmlPane.setEditable(false);
			htmlPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			htmlPane.setBackground(new JLabel().getBackground());

			final JScrollPane contents = new JScrollPane(htmlPane,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {
				public Dimension getPreferredSize() {
					final Dimension d = super.getPreferredSize();
					d.width = 500;
					d.height = 400;
					return d;
				}
			};

			htmlPane.setCaretPosition(0);

			JOptionPane.showMessageDialog(m_frame, contents, title,
					JOptionPane.PLAIN_MESSAGE, m_logoIcon);
		}
	}

	private final class ExitAction extends CustomAction {

		ExitAction() {
			super(m_resources, "exit");
		}

		public void actionPerformed(ActionEvent e) {
			exit();
		}

		void exit() {

			System.exit(0);
		}
	}

	private final class StartAction extends CustomAction {
		StartAction() {
			super(m_resources, "start");
		}

		public void actionPerformed(ActionEvent e) {
			// m_model.start();

			// putValue() won't work here as the event won't fire if
			// the value doesn't change.
			firePropertyChange(SET_ACTION_PROPERTY, null, m_stopAction);
		}
	}

	private final class StopAction extends CustomAction {
		StopAction() {
			super(m_resources, "stop");
		}

		public void actionPerformed(ActionEvent e) {
			// m_model.stop();
			stopped();
		}

		public void stopped() {
			// putValue() won't work here as the event won't fire if
			// the value doesn't change.
			firePropertyChange(SET_ACTION_PROPERTY, null, m_startAction);
		}
	}

	private final class CollectingResultAction extends CustomAction {
		CollectingResultAction() {
			super(m_resources, "collecting-result");
			m_processControl
					.addProcessStatusListener(new EnableIfAgentsFinished(this));
		}

		public void actionPerformed(ActionEvent e) {
			m_processControl.collectResultProcesses();

		}

	}

	private final class NewPlanAction extends CustomAction {
		private final JFileChooser m_fileChooser = new JFileChooser(".");

		public NewPlanAction() {
			super(m_resources, "new-plan");
			m_fileChooser.setDialogTitle(MnemonicHeuristics
					.removeMnemonicMarkers(m_resources
							.getString("new-plan.label")));

			final String XMLFilesText = m_resources
					.getString("bench4Q.configXML.label");

			m_fileChooser.addChoosableFileFilter(new FileFilter() {
				public boolean accept(File file) {
					return isXMLFile(file) || file.isDirectory();
				}

				public String getDescription() {
					return XMLFilesText;
				}
			});
		}

		public void actionPerformed(ActionEvent event) {
			File currentFile = m_configModel.getSelectedFile();
			if (currentFile != null) {
				m_fileChooser.setSelectedFile(currentFile);
			} else {

			}
			if (m_fileChooser.showSaveDialog(m_frame) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			final File file = m_fileChooser.getSelectedFile();
			if (JOptionPane.showConfirmDialog(m_frame, m_resources
					.getString("NewConfirmation.text"),
					(String) getValue(NAME), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
				return;
			}
			m_configModel.setSelectedFile(file);
			m_configModel.newEmptyFile(file);
		}
	}

	private final class OpenPlanAction extends CustomAction {
		private final JFileChooser m_fileChooser = new JFileChooser(".");

		public OpenPlanAction() {
			super(m_resources, "open-plan");
			m_fileChooser.setDialogTitle(MnemonicHeuristics
					.removeMnemonicMarkers(m_resources
							.getString("open-plan.label")));

			final String XMLFilesText = m_resources
					.getString("bench4Q.configXML.label");

			m_fileChooser.addChoosableFileFilter(new FileFilter() {
				public boolean accept(File file) {
					return isXMLFile(file) || file.isDirectory();
				}

				public String getDescription() {
					return XMLFilesText;
				}
			});
		}

		public void actionPerformed(ActionEvent event) {
			File currentFile = m_configModel.getSelectedFile();
			if (currentFile != null) {
				m_fileChooser.setSelectedFile(currentFile);
			} else {

			}
			if (m_fileChooser.showSaveDialog(m_frame) != JFileChooser.APPROVE_OPTION) {
				return;
			}
			final File file = m_fileChooser.getSelectedFile();
			if (JOptionPane.showConfirmDialog(m_frame, m_resources
					.getString("openConfirmation.text"),
					(String) getValue(NAME), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
				return;
			}
			m_configModel.setSelectedFile(file);
			if (!m_configModel.initRBEWithXMLFile()) {
				JOptionPane
						.showMessageDialog(
								m_frame,
								m_resources
										.getString("OpenPlanERRORFile.warningtext"),
								m_resources
										.getString("OpenPlanERRORFile.warningtitle"),
								JOptionPane.WARNING_MESSAGE);
				m_configModel.setSelectedFile(null);
			}

		}
	}

	private final class SavePlanAction extends CustomAction {
		public SavePlanAction() {
			super(m_resources, "save-plan");
			m_configModel.addListener(new ConfigModel.AbstractListener() {
				public void isSelectFileSet() {
					final File selectedFile = m_configModel.getSelectedFile();
					setEnabled(selectedFile != null);
				}
			});
		}

		public void actionPerformed(ActionEvent event) {
			if (m_configModel.getSelectedFile() == null) {
				JOptionPane
						.showMessageDialog(
								m_frame,
								m_resources
										.getString("SavePlanNoRelatedFile.warningtext"),
								m_resources
										.getString("SavePlanNoRelatedFile.warningtitle"),
								JOptionPane.WARNING_MESSAGE);
				return;
			}
			m_configModel.SaveToFile();
		}
	}

	private final class SavePlanAsAction extends CustomAction {

		private final JFileChooser m_fileChooser = new JFileChooser(".");

		public SavePlanAsAction() {
			super(m_resources, "save-plan-as", true);

			m_fileChooser.setDialogTitle(MnemonicHeuristics
					.removeMnemonicMarkers(m_resources
							.getString("save-plan-as.label")));

			final String XMLFilesText = m_resources
					.getString("bench4Q.configXML.label");

			m_fileChooser.addChoosableFileFilter(new FileFilter() {
				public boolean accept(File file) {
					return isXMLFile(file) || file.isDirectory();
				}

				public String getDescription() {
					return XMLFilesText;
				}
			});
		}

		public void actionPerformed(ActionEvent event) {

			File currentFile = m_configModel.getSelectedFile();
			if (currentFile != null) {
				m_fileChooser.setSelectedFile(currentFile);
			} else {

			}

			if (m_fileChooser.showSaveDialog(m_frame) != JFileChooser.APPROVE_OPTION) {
				return;
			}

			final File file = m_fileChooser.getSelectedFile();
			if (JOptionPane.showConfirmDialog(m_frame, m_resources
					.getString("saveConfirmation.text"),
					(String) getValue(NAME), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
				return;
			}
			m_configModel.SaveToFile(file);

		}
	}

	private class EnableIfAgentsConnected implements ProcessControl.Listener {

		private final Action m_action;

		EnableIfAgentsConnected(Action action) {
			m_action = action;
			enableOrDisable();
		}

		public final void update(ProcessControl.ProcessReports[] processStatuses) {
			enableOrDisable();
		}

		protected final void enableOrDisable() {
			m_action.setEnabled(shouldEnable());
		}

		protected boolean shouldEnable() {
			return m_processControl.getNumberOfLiveAgents() > 0;
		}
	}

	private class EnableIfAgentsFinished implements ProcessControl.Listener {

		private final Action m_action;

		EnableIfAgentsFinished(Action action) {
			m_action = action;
			enableOrDisable();
		}

		public final void update(ProcessControl.ProcessReports[] processStatuses) {
			enableOrDisable();
		}

		protected final void enableOrDisable() {
			m_action.setEnabled(shouldEnable());
		}

		protected boolean shouldEnable() {
			return m_processControl.getNumberOfLiveAgents() > 0;
		}
	}

	private class StartProcessesAction extends CustomAction {

		StartProcessesAction() {
			super(m_resources, "start-processes");
			m_processControl
					.addProcessStatusListener(new EnableIfAgentsConnected(this));
		}

		public void actionPerformed(final ActionEvent event) {
			m_processControl
					.startWorkerProcesses(null, m_configModel.getArgs());
		}
	}

	private final class ResetProcessesAction extends CustomAction {
		ResetProcessesAction() {
			super(m_resources, "reset-processes");
			m_processControl
					.addProcessStatusListener(new EnableIfAgentsConnected(this));
		}

		public void actionPerformed(ActionEvent event) {

		}
	}

	private final class StopProcessesAction extends CustomAction {
		StopProcessesAction() {
			super(m_resources, "stop-processes");
			m_processControl
					.addProcessStatusListener(new EnableIfAgentsConnected(this));
		}

		public void actionPerformed(ActionEvent event) {

			try {
				final int chosen = m_optionalConfirmDialog.show(m_resources
						.getString("stopProcessesConfirmation.text"),
						(String) getValue(NAME), JOptionPane.OK_CANCEL_OPTION,
						"stopProcessesAsk");

				if (chosen != JOptionPane.OK_OPTION
						&& chosen != OptionalConfirmDialog.DONT_ASK_OPTION) {
					return;
				}
			} catch (Bench4QException e) {
				getErrorHandler().handleException(e);
				return;
			}

			m_processControl.stopAgentAndWorkerProcesses();
		}
	}

	/**
	 * Return an error handler that other classes can use to report problems
	 * through the UI.
	 * 
	 * @return The exception handler.
	 */
	public ErrorHandler getErrorHandler() {
		return m_errorHandler;
	}

	private boolean isXMLFile(File f) {
		return f != null && (!f.exists() || f.isFile())
				&& f.getName().endsWith(".xml");
	}

	private boolean isBQFile(File f) {
		return f != null && (!f.exists() || f.isFile())
				&& f.getName().endsWith(".bq");
	}
}
