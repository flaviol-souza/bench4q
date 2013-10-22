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

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.SwingUtilities;

/**
 * Automatically set mnemonics for all {@link AbstractButton}s in a
 * {@link ContainerListener}. Uses <a href=
 * "http://weblogs.java.net/blog/enicholas/archive/2006/06/mnemonic_magic.html">
 * heuristics suggested by Ethan Nichols</a>.
 * 
 * <p>
 * A mnemonic can be explicitly indicated for a button by prefixing the
 * character in the button's text with an underscore.
 * </p>
 */
final class MnemonicHeuristics {

	private final Heuristic[] m_heuristics = { new FirstCharacterHeuristic(),
			new UpperCaseHeuristic(), new ConsonantHeuristic(),
			new LetterOrDigitHeuristic(), };

	private final MnemonicMap m_existingMnemonics = new MnemonicMap();

	private final MnemonicChangedListener m_mnemonicChangedListener = new MnemonicChangedListener();
	private final TextChangedListener m_textChangedListener = new TextChangedListener();

	/**
	 * Each <code>MnemonicHeuristics</code> is built for a particular container.
	 * It sets mnemonics for any {@link AbstractButton}s that already exist in
	 * the container, and registers a listener to set mnemonics for new buttons
	 * added to the container.
	 * 
	 * <p>
	 * Additionally, it watches the text of each button and recalculates
	 * mnemonics on changes.
	 * </p>
	 * 
	 * @param theContainer
	 *            The container.
	 */
	public MnemonicHeuristics(Container theContainer) {

		// Mutter, mutter. JMenu extends Container and overrides the various
		// flavours of <code>add</code>, but does not delegate to Container
		// implementation, nor override {@link Container#getComponents()}.
		// Hence the need for this hack:
		final Container container;

		if (theContainer instanceof JMenu) {
			container = ((JMenu) theContainer).getPopupMenu();
		} else {
			container = theContainer;
		}

		final Component[] existingComponents = container.getComponents();

		for (int i = 0; i < existingComponents.length; ++i) {
			if (existingComponents[i] instanceof AbstractButton) {
				final AbstractButton button = (AbstractButton) existingComponents[i];
				m_mnemonicChangedListener.add(button);
				m_textChangedListener.add(button);
				setMnemonic(button);
			}
		}

		container.addContainerListener(new ContainerListener() {
			public void componentAdded(ContainerEvent e) {
				if (e.getChild() instanceof AbstractButton) {
					final AbstractButton button = (AbstractButton) e.getChild();
					m_mnemonicChangedListener.add(button);
					m_textChangedListener.add(button);
					setMnemonic(button);
				}
			}

			public void componentRemoved(ContainerEvent e) {
				final Component button = e.getChild();
				m_mnemonicChangedListener.remove(button);
				m_textChangedListener.remove(e.getChild());
			}
		});
	}

	private void setMnemonic(final AbstractButton button) {
		final int existingMnemonic = button.getMnemonic();

		if (existingMnemonic != 0) {
			m_existingMnemonics.add(existingMnemonic, button);
			return;
		}

		final String text = button.getText();

		if (text == null) {
			return;
		}

		// Remove our text changed listener whilst changing text to prevent
		// recursion.
		m_textChangedListener.remove(button);
		button.setText(removeMnemonicMarkers(text));
		m_textChangedListener.add(button);

		// Look for explicit mnemonic indicated by an underscore in the button's
		// text. We remove the underscores.
		int underscore = text.indexOf('_');
		int numberOfUnderscores = 0;

		while (underscore >= 0 && underscore < text.length() - 1) {

			final int explicitMnemonic = toKey(text.charAt(underscore + 1),
					false);

			final AbstractButton existingExplicit = m_existingMnemonics
					.getExplicit(explicitMnemonic);

			// If there is an existing button with the same explicit mnemonic,
			// it
			// takes precedence and we fall back to other heuristics.
			if (explicitMnemonic != 0 && existingExplicit == null
					|| existingExplicit == button) {

				final AbstractButton oldButton = m_existingMnemonics
						.remove(explicitMnemonic);

				button.setMnemonic(explicitMnemonic);

				// Calling setDisplayedIndex() directly here doesn't work for
				// text
				// change events since it is overwritten by
				// AbstractButton.setText(),
				// based on the original text. I've submitted a bug to Sun.
				//
				// Instead, we dispatch the change in the AWT event dispatching
				// thread,
				// which works for the common case that setText() is called from
				// that
				// thread.
				final int index = underscore - numberOfUnderscores;

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						button.setDisplayedMnemonicIndex(index);
					}
				});

				m_existingMnemonics.addExplicit(explicitMnemonic, button);

				// If there is a different existing button with an implicit
				// mnemonic,
				// we take precedence and we calculate a new mnemonic for it.
				if (oldButton != null && oldButton != button) {
					oldButton.setMnemonic(0);
					setMnemonic(oldButton);
				}

				return;
			}

			// Treat subsequent underscores as indications of alternative
			// mnemonics.
			underscore = text.indexOf('_', underscore + 1);
			++numberOfUnderscores;
		}

		// No explicit mnemonic, use heuristics.
		for (int i = 0; i < m_heuristics.length; ++i) {
			final int result = m_heuristics[i].apply(button.getText());

			if (result != 0) {
				button.setMnemonic(result);
				m_existingMnemonics.add(result, button);
				return;
			}
		}
	}

	private abstract class AbstractPropertyListener implements
			PropertyChangeListener {

		private final String m_property;

		protected AbstractPropertyListener(String property) {
			m_property = property;
		}

		public void add(Component component) {
			component.addPropertyChangeListener(m_property, this);
		}

		public void remove(Component component) {
			component.removePropertyChangeListener(m_property, this);
		}
	}

	private final class MnemonicChangedListener extends
			AbstractPropertyListener {
		public MnemonicChangedListener() {
			super(AbstractButton.MNEMONIC_CHANGED_PROPERTY);
		}

		public void propertyChange(PropertyChangeEvent evt) {
			m_existingMnemonics.remove(
					((Integer) evt.getOldValue()).intValue(),
					(AbstractButton) evt.getSource());
		}
	}

	private final class TextChangedListener extends AbstractPropertyListener {
		public TextChangedListener() {
			super(AbstractButton.TEXT_CHANGED_PROPERTY);
		}

		public void propertyChange(PropertyChangeEvent evt) {
			final AbstractButton button = (AbstractButton) evt.getSource();
			m_existingMnemonics.remove(button.getMnemonic(), button);
			button.setMnemonic(0);
			setMnemonic(button);
		}
	}

	private int toKey(char c, boolean filterExisting) {
		// We convert candidate characters to key by converting to uppercase...
		final char upper = Character.toUpperCase(c);

		// .. filtering out existing mnemonics...
		if (!filterExisting || !m_existingMnemonics.contains(upper)) {

			// .. and throwing away anything that doesn't map to a key.
			if (KeyEvent.getKeyText(upper).equals(String.valueOf(upper))) {
				return upper;
			}
		}

		return 0;
	}

	private int toKey(char c) {
		return toKey(c, true);
	}

	private interface Heuristic {
		int apply(String text);
	}

	private class FirstCharacterHeuristic implements Heuristic {
		public int apply(String text) {
			return text.length() > 0 ? toKey(text.charAt(0)) : 0;
		}
	}

	private abstract class AbstactEarliestMatchHeuristic implements Heuristic {
		public int apply(String text) {
			final char[] characters = text.toCharArray();

			for (int i = 0; i < characters.length; ++i) {
				if (matches(characters[i])) {
					final int result = toKey(characters[i]);

					if (result != 0) {
						return result;
					}
				}
			}

			return 0;
		}

		protected abstract boolean matches(char c);
	}

	private class UpperCaseHeuristic extends AbstactEarliestMatchHeuristic {
		protected boolean matches(char c) {
			return Character.isUpperCase(c);
		}
	}

	private class LetterOrDigitHeuristic extends AbstactEarliestMatchHeuristic {
		protected boolean matches(char c) {
			return Character.isLetterOrDigit(c);
		}
	}

	private class ConsonantHeuristic extends LetterOrDigitHeuristic {
		protected boolean matches(char c) {
			// Prioritising consonants is English-centric and rough and ready.
			// I'm
			// not sweating about whether this ought to consider other Unicode
			// characters.
			return super.matches(c) && c != 'a' && c != 'e' && c != 'i'
					&& c != 'o' && c != 'u';
		}
	}

	private static class MnemonicMap {
		private final Map m_map = new HashMap();

		public void add(int mnemonic, AbstractButton button) {
			m_map.put(new Integer(mnemonic), new ButtonWrapper(button, false));
		}

		public void addExplicit(int mnemonic, AbstractButton button) {
			m_map.put(new Integer(mnemonic), new ButtonWrapper(button, true));
		}

		public AbstractButton getExplicit(int mnemonic) {
			final ButtonWrapper wrapper = (ButtonWrapper) m_map
					.get(new Integer(mnemonic));

			if (wrapper != null && wrapper.isExplicit()) {
				return wrapper.getButton();
			}

			return null;
		}

		public AbstractButton remove(int mnemonic) {
			final ButtonWrapper wrapper = (ButtonWrapper) m_map
					.remove(new Integer(mnemonic));

			return wrapper != null ? wrapper.getButton() : null;
		}

		/**
		 * Version of <code>remove</code> that only removes an entry if it was
		 * for a particular button.
		 * 
		 * @param mnemonic
		 * @param button
		 * @return
		 */
		public AbstractButton remove(int mnemonic, AbstractButton button) {
			final ButtonWrapper wrapper = (ButtonWrapper) m_map
					.get(new Integer(mnemonic));

			if (wrapper != null && wrapper.getButton() == button) {
				return remove(mnemonic);
			}

			return null;
		}

		public boolean contains(int mnemonic) {
			return m_map.containsKey(new Integer(mnemonic));
		}

		private static class ButtonWrapper {
			private final AbstractButton m_button;
			private final boolean m_isExplicit;

			public ButtonWrapper(AbstractButton button, boolean explicitMnemonic) {
				m_button = button;
				m_isExplicit = explicitMnemonic;
			}

			public AbstractButton getButton() {
				return m_button;
			}

			public boolean isExplicit() {
				return m_isExplicit;
			}
		}
	}

	/**
	 * Utility method that removes explicit mnemonic indicators from a string.
	 * 
	 * @param s
	 *            The string to clean.
	 * @return <code>s</code>, without its mnemonic indicators.
	 */
	public static String removeMnemonicMarkers(String s) {
		return s.replaceAll("_", "");
	}
}
