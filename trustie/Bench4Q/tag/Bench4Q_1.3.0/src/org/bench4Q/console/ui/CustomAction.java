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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ImageIcon;

import org.bench4Q.console.common.Resources;

/**
 * Customised Action.
 * 
 */
abstract class CustomAction extends AbstractAction {

	/** Property key used to indicate a set action event. */
	protected static final String SET_ACTION_PROPERTY = "setAction";

	/** Property key for rollover icon value. */
	public static final String ROLLOVER_ICON = "RolloverIcon";

	/**
	 * Property key for Boolean value indicating whether action is relevant to
	 * the selected context.
	 */
	public static final String RELEVANT_TO_SELECTION = "RelevantToSelection";

	private final String m_key;
	private final Set m_propertyChangeListenersByButton = new HashSet();

	protected CustomAction(Resources resources, String key) {
		this(resources, key, false);
	}

	protected CustomAction(Resources resources, String key,
			boolean isDialogAction) {
		super();

		m_key = key;

		final String label = resources.getString(m_key + ".label", false);

		if (label != null) {
			if (isDialogAction) {
				putValue(Action.NAME, label + "...");
			} else {
				putValue(Action.NAME, label);
			}
		}

		final String tip = resources.getString(m_key + ".tip", false);

		if (tip != null) {
			putValue(Action.SHORT_DESCRIPTION, tip);
		}

		final ImageIcon imageIcon = resources.getImageIcon(m_key + ".image");

		if (imageIcon != null) {
			putValue(Action.SMALL_ICON, imageIcon);
		}

		final ImageIcon rolloverImageIcon = resources.getImageIcon(m_key
				+ ".rollover-image");

		if (rolloverImageIcon != null) {
			putValue(CustomAction.ROLLOVER_ICON, rolloverImageIcon);
		}
	}

	public final String getKey() {
		return m_key;
	}

	public final void registerButton(final AbstractButton button) {
		if (!m_propertyChangeListenersByButton.contains(button)) {
			addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					if (e.getPropertyName().equals(SET_ACTION_PROPERTY)) {

						final CustomAction newAction = (CustomAction) e
								.getNewValue();

						button.setAction(newAction);
						newAction.registerButton(button);
					}
				}
			});

			m_propertyChangeListenersByButton.add(button);
		}
	}

	public void setRelevantToSelection(boolean b) {
		putValue(RELEVANT_TO_SELECTION, Boolean.valueOf(b));
	}

	public boolean isRelevantToSelection() {
		final Boolean b = (Boolean) getValue(RELEVANT_TO_SELECTION);
		return b != null && b.booleanValue();
	}
}
