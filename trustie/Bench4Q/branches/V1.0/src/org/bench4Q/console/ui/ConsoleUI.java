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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.Logger;
import org.bench4Q.common.util.XMlFileLoader;
import org.bench4Q.console.ConsoleFoundation;
import org.bench4Q.console.ConsoleProperties;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.ErrorHandler;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ProcessControl;
import org.bench4Q.console.ui.controlTree.Bench4QTreeModel;
import org.bench4Q.console.ui.controlTree.Bench4QTreeNode;
import org.bench4Q.console.ui.transfer.AgentsCollection;

public final class ConsoleUI implements ConsoleFoundation.UI {

	private Args m_args;

	private XMlFileLoader m_fileLoader;

	// private AgentInfo m_agentInfo;

	private final ActionTable m_actionTable = new ActionTable();
	private final CollectingResultAction m_collectingResultAction;
	private final CloseFileAction m_closeFileAction;
	private final StartAction m_startAction;
	private final ExitAction m_exitAction;
	private final StopAction m_stopAction;
	private final SaveFileAction m_saveFileAction;
	private final SaveFileAsAction m_saveFileAsAction;

	private final Resources m_resources;
	private final ConsoleProperties m_properties;
	private final ProcessControl m_processControl;

	private final JFrame m_frame;
	static final int windowHeight = 610;
	static final int leftWidth = 170;
	static final int rightWidth = 730;
	static final int windowWidth = leftWidth + rightWidth;

	boolean isModified = false;

	DefaultMutableTreeNode m_top;
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

	public ConsoleUI(Resources resources, ConsoleProperties consoleProperties,
			ProcessControl processControl, Logger logger) throws ConsoleException {

		m_resources = resources;
		m_properties = consoleProperties;
		m_processControl = processControl;

		// Create the frame to contain the a menu and the top level pane.
		// Do before actions are constructed as we use the frame to create
		// dialogs.
		m_frame = new JFrame(m_resources.getString("title"));

		final ErrorDialogHandler errorDialogHandler = new ErrorDialogHandler(m_frame, m_resources,
				logger);

		m_errorHandler = (ErrorHandler) new SwingDispatcherFactoryImplementation(null)
				.create(errorDialogHandler);

		m_swingDispatcherFactory = new SwingDispatcherFactoryImplementation(m_errorHandler);

		m_optionalConfirmDialog = new OptionalConfirmDialog(m_frame, m_resources, m_properties);

		m_collectingResultAction = new CollectingResultAction();
		m_closeFileAction = new CloseFileAction();
		m_exitAction = new ExitAction();
		m_startAction = new StartAction();
		m_stopAction = new StopAction();
		m_saveFileAction = new SaveFileAction();
		m_saveFileAsAction = new SaveFileAsAction();

		m_actionTable.add(m_collectingResultAction);
		m_actionTable.add(m_closeFileAction);
		m_actionTable.add(m_exitAction);
		m_actionTable.add(m_startAction);
		m_actionTable.add(m_stopAction);
		m_actionTable.add(m_saveFileAsAction);
		// m_actionTable.add(m_distributeFilesAction);
		m_actionTable.add(new AboutAction(m_resources.getImageIcon("logo.image")));
		m_actionTable.add(new ChooseDirectoryAction());
		m_actionTable.add(new StartProcessesAction());
		m_actionTable.add(new NewFileAction());
		m_actionTable.add(new OptionsAction());
		m_actionTable.add(new ResetProcessesAction());
		m_actionTable.add(new SaveFileAction());
		m_actionTable.add(new SaveResultsAction());
		m_actionTable.add(new StopProcessesAction());

		m_fileLoader = new XMlFileLoader();

		// Create the tabbed test display.
		final JTabbedPane tabbedPane = new JTabbedPane();

		final JSplitPane splitPane;

		m_agentsCollection = new AgentsCollection(m_resources, m_processControl,
				m_swingDispatcherFactory);

		m_treeModel = new Bench4QTreeModel(m_resources, m_processControl, m_swingDispatcherFactory,
				m_fileLoader, m_agentsCollection);
		m_tree = new JTree(m_treeModel);
		m_tree.setRootVisible(false);
		m_tree.setShowsRootHandles(true);
		m_treeModel.setTree(m_tree);
		m_tree.setCellRenderer(new MyRenderer());

		m_treeView = new JScrollPane(m_tree);

		m_tree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
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

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_treeView, m_mainView);
		splitPane.setContinuousLayout(true);
		splitPane.setDividerLocation(leftWidth);

		m_titleLabelFont = new JLabel().getFont().deriveFont(Font.PLAIN | Font.ITALIC);

		final JPanel contentPanel = new JPanel(new BorderLayout());
		// contentPanel.add(controlAndTotalPanel, BorderLayout.WEST);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);

		// Create a panel to hold the tool bar and the test pane.
		final JPanel toolBarPanel = new JPanel(new BorderLayout());
		final JToolBar mainToolBar = new JToolBar();
		new ToolBarAssembler(mainToolBar, false).populate("main.toolbar");
		toolBarPanel.add(mainToolBar, BorderLayout.NORTH);
		toolBarPanel.add(splitPane, BorderLayout.CENTER);

		// set the preferred size of the demo
		m_frame.setPreferredSize(new Dimension(windowWidth + 10, windowHeight + 10));
		m_frame.setMinimumSize(new Dimension(windowWidth + 10, windowHeight + 10));
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

		Bench4QTreeNode node = (Bench4QTreeNode) m_tree.getLastSelectedPathComponent();
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
			final Iterator iterator = Collections.list(new StringTokenizer(tokens)).iterator();

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

	/** Work around polymorphic interface that's missing from Swing. */
	private abstract class AbstractMenuAssembler extends ListTokeniserTemplate {

		protected AbstractMenuAssembler(JComponent component) {
			super(component);
			new MnemonicHeuristics(component);
		}

		protected void token(String menuItemKey) {
			final JMenuItem menuItem = new JMenuItem() {

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

						menuItem.setVisible(((CustomAction) menuItem.getAction())
								.isRelevantToSelection());

						menuItem.getAction().addPropertyChangeListener(
								new PropertyChangeListener() {
									public void propertyChange(PropertyChangeEvent evt) {
										if (evt.getPropertyName().equals(
												CustomAction.RELEVANT_TO_SELECTION)) {
											menuItem.setVisible(((CustomAction) menuItem
													.getAction()).isRelevantToSelection());
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
			final JMenu menu = new JMenu(m_resources.getString(key + ".menu.label"));

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
				button.setBorder(BorderFactory.createEmptyBorder());
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

		SaveResultsAction() {
			super(m_resources, "save-results", true);

			// m_fileChooser.setDialogTitle(MnemonicHeuristics.removeMnemonicMarkers(m_resources
			// .getString("save-results.label")));
			//
			// m_fileChooser.setSelectedFile(new
			// File(m_resources.getString("default.filename")));
			//
			// m_lookAndFeel.addListener(new
			// LookAndFeel.ComponentListener(m_fileChooser));
		}

		public void actionPerformed(ActionEvent event) {
		}
	}

	private final class OptionsAction extends CustomAction {
		private final OptionsDialogHandler m_optionsDialogHandler;

		OptionsAction() {
			super(m_resources, "options", true);

			m_optionsDialogHandler = new OptionsDialogHandler(m_frame, m_properties, m_resources) {
				protected void setNewOptions(ConsoleProperties newOptions) {
					m_properties.set(newOptions);
					//m_samplingControlPanel.refresh();
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

			final String title = MnemonicHeuristics.removeMnemonicMarkers(resources
					.getString("about.label"));
			final String aboutText = resources.getStringFromFile("about.text", true);

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

			JOptionPane.showMessageDialog(m_frame, contents, title, JOptionPane.PLAIN_MESSAGE,
					m_logoIcon);
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
			// try {
			// m_frameBounds.store();
			// } catch (ConsoleException e) {
			// getErrorHandler().handleException(e);
			// }
			// dzq
			// final Buffer[] buffers = m_editorModel.getBuffers();
			//
			// for (int i = 0; i < buffers.length; ++i) {
			// if (!m_closeFileAction.closeBuffer(buffers[i])) {
			// return;
			// }
			// }

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
			m_processControl.addProcessStatusListener(new EnableIfAgentsFinished(this));

			// totalAgentNumber =
		}

		public void actionPerformed(ActionEvent e) {

			int chosen;
			// try {
			// chosen = m_optionalConfirmDialog.show(m_resources
			// .getString("CollectingResultConfirmation.text"), (String)
			// getValue(NAME),
			// JOptionPane.OK_CANCEL_OPTION, "CollectingResult");
			// if (chosen != JOptionPane.OK_OPTION
			// && chosen != OptionalConfirmDialog.DONT_ASK_OPTION) {
			// return;
			// }
			// } catch (DisplayMessageConsoleException e1) {
			// e1.printStackTrace();
			// } catch (PropertyException e1) {
			// e1.printStackTrace();
			// }

			m_processControl.collectResultProcesses();

		}

	}

	private final class NewFileAction extends CustomAction {
		public NewFileAction() {
			super(m_resources, "new-file");
		}

		public void actionPerformed(ActionEvent event) {
			// dzq
			// m_editorModel.selectNewBuffer();
		}
	}

	private final class SaveFileAction extends CustomAction {
		public SaveFileAction() {
			super(m_resources, "save-file");
			// dzq
			// m_editorModel.addListener(new EditorModel.AbstractListener() {
			// public void bufferStateChanged(Buffer ignored) {
			// final Buffer buffer = m_editorModel.getSelectedBuffer();
			//
			// setEnabled(buffer != null && buffer.isDirty());
			// }
			// });
		}

		public void actionPerformed(ActionEvent event) {
			// try {
			// final Buffer buffer = m_editorModel.getSelectedBuffer();
			//
			// if (buffer.getFile() != null) {
			// if (!buffer.isUpToDate()
			// && JOptionPane.showConfirmDialog(m_frame, m_resources
			// .getString("outOfDateOverwriteConfirmation.text"), buffer
			// .getFile().toString(), JOptionPane.YES_NO_OPTION) ==
			// JOptionPane.NO_OPTION) {
			// return;
			// }
			//
			// buffer.save();
			// } else {
			// m_saveFileAsAction.saveBufferAs(buffer);
			// }
			// } catch (ConsoleException e) {
			// getErrorHandler().handleException(e);
			// }
		}
	}

	private final class SaveFileAsAction extends CustomAction {

		// private final JFileChooser m_fileChooser = new JFileChooser(".");

		public SaveFileAsAction() {
			super(m_resources, "save-file-as", true);

			// m_editorModel.addListener(new EditorModel.AbstractListener() {
			// public void bufferStateChanged(Buffer ignored) {
			// setEnabled(m_editorModel.getSelectedBuffer() != null);
			// }
			// });
			//
			// m_fileChooser.setDialogTitle(MnemonicHeuristics.removeMnemonicMarkers(m_resources
			// .getString("save-file-as.label")));
			//
			// final String pythonFilesText =
			// m_resources.getString("pythonScripts.label");
			//
			// m_fileChooser.addChoosableFileFilter(new FileFilter() {
			// public boolean accept(File file) {
			// return m_editorModel.isPythonFile(file) || file.isDirectory();
			// }
			//
			// public String getDescription() {
			// return pythonFilesText;
			// }
			// });
			//
			// m_lookAndFeel.addListener(new
			// LookAndFeel.ComponentListener(m_fileChooser));
		}

		public void actionPerformed(ActionEvent event) {
			// try {
			// saveBufferAs(m_editorModel.getSelectedBuffer());
			// } catch (ConsoleException e) {
			// getErrorHandler().handleException(e);
			// }
		}

		// void saveBufferAs(Buffer buffer) throws ConsoleException {
		// final File currentFile = buffer.getFile();
		// final Directory distributionDirectory =
		// m_properties.getDistributionDirectory();
		//
		// if (currentFile != null) {
		// m_fileChooser.setSelectedFile(currentFile);
		// } else {
		// m_fileChooser.setCurrentDirectory(distributionDirectory.getFile());
		// }
		//
		// if (m_fileChooser.showSaveDialog(m_frame) !=
		// JFileChooser.APPROVE_OPTION) {
		// return;
		// }
		//
		// final File file = m_fileChooser.getSelectedFile();
		//
		// if (!distributionDirectory.isParentOf(file)
		// && JOptionPane.showConfirmDialog(m_frame, m_resources
		// .getString("saveOutsideOfDistributionConfirmation.text"),
		// (String) getValue(NAME), JOptionPane.YES_NO_OPTION) ==
		// JOptionPane.NO_OPTION) {
		// return;
		// }
		//
		// if (!file.equals(currentFile)) {
		// // Save as.
		// final Buffer oldBuffer = m_editorModel.getBufferForFile(file);
		//
		// if (oldBuffer != null) {
		// final ArrayList messages = new ArrayList();
		// messages.add(m_resources.getString("ignoreExistingBufferConfirmation.text"));
		//
		// if (oldBuffer.isDirty()) {
		// messages.add(m_resources.getString("existingBufferHasUnsavedChanges.text"));
		// }
		//
		// if (!oldBuffer.isUpToDate()) {
		// messages.add(m_resources.getString("existingBufferOutOfDate.text"));
		// }
		//
		// messages.add(m_resources.getString("ignoreExistingBufferConfirmation2.text"));
		//
		// if (JOptionPane.showConfirmDialog(m_frame, messages.toArray(),
		// file.toString(),
		// JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
		// return;
		// }
		//
		// m_editorModel.closeBuffer(oldBuffer);
		// } else {
		// if (file.exists()
		// && JOptionPane.showConfirmDialog(m_frame, m_resources
		// .getString("overwriteConfirmation.text"), file.toString(),
		// JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
		// return;
		// }
		// }
		// } else {
		// // We only need to check whether the current buffer is up to
		// // date
		// // for Save, not Save As.
		// if (!buffer.isUpToDate()
		// && JOptionPane.showConfirmDialog(m_frame, m_resources
		// .getString("outOfDateOverwriteConfirmation.text"), buffer.getFile()
		// .toString(), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
		// return;
		// }
		// }
		//
		// buffer.save(file);
		// }
	}

	private final class CloseFileAction extends CustomAction {
		public CloseFileAction() {
			super(m_resources, "close-file");

			// m_editorModel.addListener(new EditorModel.AbstractListener() {
			// public void bufferStateChanged(Buffer ignored) {
			// setEnabled(m_editorModel.getSelectedBuffer() != null);
			// }
			// });
		}

		public void actionPerformed(ActionEvent event) {
			// closeBuffer(m_editorModel.getSelectedBuffer());
		}

		// boolean closeBuffer(Buffer buffer) {
		// if (buffer != null) {
		// while (buffer.isDirty()) {
		// // Loop until we've saved the buffer successfully or
		// // canceled.
		//
		// final String confirmationMessage = MessageFormat.format(m_resources
		// .getString("saveModifiedBufferConfirmation.text"),
		// new Object[] { buffer.getDisplayName() });
		//
		// final int chosen = JOptionPane.showConfirmDialog(m_frame,
		// confirmationMessage,
		// (String) getValue(NAME), JOptionPane.YES_NO_CANCEL_OPTION);
		//
		// if (chosen == JOptionPane.YES_OPTION) {
		// try {
		// if (buffer.getFile() != null) {
		// buffer.save();
		// } else {
		// m_saveFileAsAction.saveBufferAs(buffer);
		// }
		// } catch (JASptEException e) {
		// getErrorHandler().handleException(e);
		// return false;
		// }
		// } else if (chosen == JOptionPane.NO_OPTION) {
		// break;
		// } else {
		// return false;
		// }
		// }
		//
		// m_editorModel.closeBuffer(buffer);
		// }
		//
		// return true;
		// }
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
			m_processControl.addProcessStatusListener(new EnableIfAgentsConnected(this));
		}

		public void actionPerformed(final ActionEvent event) {
			// try {
			// final File propertiesFile =
			// m_editorModel.getSelectedPropertiesFile();
			//
			// if (propertiesFile == null) {
			// final int chosen = m_optionalConfirmDialog.show(m_resources
			// .getString("propertiesNotSetConfirmation.text"),
			// (String) getValue(NAME), JOptionPane.OK_CANCEL_OPTION,
			// "propertiesNotSetAsk");
			//
			// if (chosen != JOptionPane.OK_OPTION
			// && chosen != OptionalConfirmDialog.DONT_ASK_OPTION) {
			// return;
			// }
			//
			// if (m_fileLoader.getArgs() != null) {
			// m_processControl.startWorkerProcesses(null,
			// m_fileLoader.getArgs());
			// }
			// // m_processControl.startWorkerProcesses(null, m_args);
			// } else {
			// if (m_editorModel.isABufferDirty()) {
			// final int chosen = m_optionalConfirmDialog.show(m_resources
			// .getString("startWithUnsavedBuffersConfirmation.text"),
			// (String) getValue(NAME), JOptionPane.OK_CANCEL_OPTION,
			// "startWithUnsavedBuffersAsk");
			//
			// if (chosen != JOptionPane.OK_OPTION
			// && chosen != OptionalConfirmDialog.DONT_ASK_OPTION) {
			// return;
			// }
			// }
			//
			// if (m_fileDistribution.getAgentCacheState().getOutOfDate()) {
			// final int chosen = m_optionalConfirmDialog.show(m_resources
			// .getString("cachesOutOfDateConfirmation.text"),
			// (String) getValue(NAME), JOptionPane.OK_CANCEL_OPTION,
			// "distributeOnStartAsk");
			//
			// if (chosen != JOptionPane.OK_OPTION
			// && chosen != OptionalConfirmDialog.DONT_ASK_OPTION) {
			// return;
			// }
			//
			// // The distribution is done in a background thread. When
			// // it
			// // completes, it dispatches our callback in the Swing
			// // thread.
			// m_distributeFilesAction.distribute(new Runnable() {
			// public void run() {
			// StartProcessesAction.this.actionPerformed(event);
			// }
			// });
			//
			// return;
			// }
			//
			// final JASptEProperties properties = new
			// JASptEProperties(propertiesFile);
			//
			// final File scriptFile =
			// properties.resolveRelativeFile(properties.getFile(
			// JASptEProperties.SCRIPT, JASptEProperties.DEFAULT_SCRIPT));
			//
			// final Directory directory =
			// m_properties.getDistributionDirectory();
			//
			// final File relativePath = directory.getRelativePath(scriptFile);
			//
			// // relativePath == null <=> absolute path that is outside of
			// // the
			// // directory. We allow this, since it is fairly obvious to
			// // the user
			// // what is going on.
			// if (relativePath != null &&
			// !directory.getFile(relativePath).isFile()) {
			// getErrorHandler().handleErrorMessage(
			// m_resources.getString("scriptNotInDirectoryError.text"),
			// (String) getValue(NAME));
			//
			// return;
			// }
			//
			// // Ensure the properties passed to the agent has a relative
			// // associated path.
			// properties.setAssociatedFile(directory.getRelativePath(propertiesFile));
			//
			// if (m_fileLoader.getArgs() != null) {
			// m_processControl.startWorkerProcesses(properties,
			// m_fileLoader.getArgs());
			// }
			//
			// }
			// } catch (JASptEException e) {
			// getErrorHandler().handleException(e);
			// }

			m_processControl.startWorkerProcesses(null, m_fileLoader.getArgs());
		}
	}

	private final class ResetProcessesAction extends CustomAction {
		ResetProcessesAction() {
			super(m_resources, "reset-processes");
			m_processControl.addProcessStatusListener(new EnableIfAgentsConnected(this));
		}

		public void actionPerformed(ActionEvent event) {

			// final ConsoleProperties properties = m_properties;
			//
			// try {
			// final int chosen = m_optionalConfirmDialog.show(m_resources
			// .getString("resetConsoleWithProcessesConfirmation.text"),
			// (String) getValue(NAME), JOptionPane.YES_NO_CANCEL_OPTION,
			// "resetConsoleWithProcessesAsk");
			//
			// switch (chosen) {
			// case JOptionPane.YES_OPTION:
			// properties.setResetConsoleWithProcesses(true);
			// break;
			//
			// case JOptionPane.NO_OPTION:
			// properties.setResetConsoleWithProcesses(false);
			// break;
			//
			// case OptionalConfirmDialog.DONT_ASK_OPTION:
			// break;
			//
			// default:
			// return;
			// }
			// } catch (JASptEException e) {
			// getErrorHandler().handleException(e);
			// return;
			// }
			//
			// if (properties.getResetConsoleWithProcesses()) {
			// m_model.reset();
			// m_sampleModelViews.resetStatisticsViews();
			// }
			//
			// m_processControl.resetWorkerProcesses();
		}
	}

	private final class StopProcessesAction extends CustomAction {
		StopProcessesAction() {
			super(m_resources, "stop-processes");
			m_processControl.addProcessStatusListener(new EnableIfAgentsConnected(this));
		}

		public void actionPerformed(ActionEvent event) {

			try {
				final int chosen = m_optionalConfirmDialog.show(m_resources
						.getString("stopProcessesConfirmation.text"), (String) getValue(NAME),
						JOptionPane.OK_CANCEL_OPTION, "stopProcessesAsk");

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

	private final class ChooseDirectoryAction extends CustomAction {
		private final JFileChooser m_fileChooser = new JFileChooser(".");

		ChooseDirectoryAction() {
			super(m_resources, "choose-directory", true);

			// m_fileChooser.setDialogTitle(m_resources.getString("choose-directory.tip"));
			//
			// m_fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			//
			// m_fileChooser.setSelectedFile(m_properties.getDistributionDirectory().getFile());
			//
			// m_lookAndFeel.addListener(new
			// LookAndFeel.ComponentListener(m_fileChooser));
		}

		public void actionPerformed(ActionEvent event) {
			// try {
			// final String title =
			// MnemonicHeuristics.removeMnemonicMarkers(m_resources
			// .getString("choose-directory.label"));
			//
			// if (m_fileChooser.showDialog(m_frame, title) ==
			// JFileChooser.APPROVE_OPTION) {
			//
			// final Directory directory = new
			// Directory(m_fileChooser.getSelectedFile());
			// final File file = directory.getFile();
			//
			// if (!file.exists()) {
			// if (JOptionPane.showConfirmDialog(m_frame, m_resources
			// .getString("createDirectory.text"), file.toString(),
			// JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
			// return;
			// }
			//
			// directory.create();
			// }
			//
			// final ConsoleProperties properties = m_properties;
			// properties.setAndSaveDistributionDirectory(directory);
			// }
			// } catch (IOException e) {
			// UncheckedInterruptedException.ioException(e);
			// getErrorHandler().handleException(e);
			// } catch (JASptEException e) {
			// getErrorHandler().handleException(e);
			// }
		}
	}

	// private final class DistributeFilesAction extends CustomAction {
	//
	// private final Condition m_cacheStateCondition = new Condition();
	//
	// DistributeFilesAction() {
	// super(m_resources, "distribute-files");
	//
	// final AgentCacheState agentCacheState =
	// m_fileDistribution.getAgentCacheState();
	//
	// agentCacheState.addListener(new PropertyChangeListener() {
	// public void propertyChange(PropertyChangeEvent ignored) {
	// setEnabled(shouldEnable());
	// synchronized (m_cacheStateCondition) {
	// m_cacheStateCondition.notifyAll();
	// }
	// }
	// });
	//
	// setEnabled(shouldEnable());
	// }
	//
	// private boolean shouldEnable() {
	// return m_fileDistribution.getAgentCacheState().getOutOfDate();
	// }
	//
	// public void actionPerformed(ActionEvent event) {
	// distribute(null);
	// }
	//
	// public void distribute(final Runnable onCompletionCallback) {
	// final FileDistributionHandler distributionHandler =
	// m_fileDistribution.getHandler();
	//
	// final ProgressMonitor progressMonitor = new ProgressMonitor(m_frame,
	// getValue(NAME),
	// "", 0, 100);
	// progressMonitor.setMillisToDecideToPopup(0);
	// progressMonitor.setMillisToPopup(0);
	//
	// final Runnable distributionRunnable = new Runnable() {
	// public void run() {
	// while (!progressMonitor.isCanceled()) {
	// try {
	// final FileDistributionHandler.Result result = distributionHandler
	// .sendNextFile();
	//
	// if (result == null) {
	// break;
	// }
	//
	// progressMonitor.setProgress(result.getProgressInCents());
	// progressMonitor.setNote(result.getFileName());
	// } catch (FileContents.FileContentsException e) {
	// // We don't want to put a dialog in the user's face
	// // for every problem. Lets just log the the terminal
	// // until we have a proper console log.
	// e.printStackTrace();
	// }
	// }
	//
	// progressMonitor.close();
	//
	// if (onCompletionCallback != null) {
	// // The cache status is updated asynchronously by agent
	// // reports.
	// // If we have a listener, we wait for up to five seconds
	// // for all
	// // agents to indicate that they are up to date.
	// synchronized (m_cacheStateCondition) {
	// for (int i = 0; i < 5 && shouldEnable(); ++i) {
	// m_cacheStateCondition.waitNoInterrruptException(1000);
	// }
	// }
	//
	// SwingUtilities.invokeLater(onCompletionCallback);
	// }
	// }
	// };
	//
	// new Thread(distributionRunnable).start();
	// }
	// }

	/**
	 * Return an error handler that other classes can use to report problems
	 * through the UI.
	 * 
	 * @return The exception handler.
	 */
	public ErrorHandler getErrorHandler() {
		return m_errorHandler;
	}
}
