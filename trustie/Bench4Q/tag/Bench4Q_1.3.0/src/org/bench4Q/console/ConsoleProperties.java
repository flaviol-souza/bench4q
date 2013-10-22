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

package org.bench4Q.console;

import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.bench4Q.common.Bench4QProperties;
import org.bench4Q.common.communication.CommunicationDefaults;
import org.bench4Q.console.common.ConsoleException;
import org.bench4Q.console.common.DisplayMessageConsoleException;
import org.bench4Q.console.common.Resources;

/**
 * Class encapsulating the console options.
 */
public final class ConsoleProperties {

	/** Property name. */
	public static final String CONSOLE_HOST_PROPERTY = "bench4Q.console.consoleHost";

	/** Property name. */
	public static final String CONSOLE_PORT_PROPERTY = "bench4Q.console.consolePort";

	/** Property name. */
	public static final String PROPERTIES_FILE_PROPERTY = "bench4Q.console.propertiesFile";

	/** config file name. */
	public static final String DEFAULT_CONFIG_FILE_PROPERTY = "bench4Q.console.defaultConfigFile";

	/** Property name. */
	public static final String LOOK_AND_FEEL_PROPERTY = "bench4Q.console.lookAndFeel";

	private final PropertyChangeSupport m_changeSupport = new PropertyChangeSupport(
			this);

	private final List m_propertyList = new ArrayList();

	private final FileProperty m_propertiesFile = new FileProperty(
			PROPERTIES_FILE_PROPERTY);

	private final FileProperty m_configFile = new FileProperty(
			DEFAULT_CONFIG_FILE_PROPERTY);

	private final StringProperty m_consoleHost = new StringProperty(
			CONSOLE_HOST_PROPERTY, CommunicationDefaults.CONSOLE_HOST);

	private final IntProperty m_consolePort = new IntProperty(
			CONSOLE_PORT_PROPERTY, CommunicationDefaults.CONSOLE_PORT);

	private final Resources m_resources;

	/**
	 * Use to save and load properties, and to keep track of the associated
	 * file.
	 */
	private final Bench4QProperties m_properties;

	/**
	 * Construct a ConsoleProperties backed by the given file.
	 * 
	 * @param resources
	 *            Console resources.
	 * @param file
	 *            The properties file.
	 * @throws ConsoleException
	 *             If the properties file cannot be read or the properties file
	 *             contains invalid data.
	 * 
	 */
	public ConsoleProperties(Resources resources, File file)
			throws ConsoleException {

		m_resources = resources;

		try {
			m_properties = new Bench4QProperties(file);
		} catch (Bench4QProperties.PersistenceException e) {
			throw new DisplayMessageConsoleException(m_resources,
					"couldNotLoadOptionsError.text", e);
		}

		final Iterator propertyIterator = m_propertyList.iterator();

		while (propertyIterator.hasNext()) {
			((Property) propertyIterator.next()).setFromProperties();
		}
	}

	/**
	 * Copy constructor. Does not copy property change listeners.
	 * 
	 * @param properties
	 *            The properties to copy.
	 */
	public ConsoleProperties(ConsoleProperties properties) {
		m_resources = properties.m_resources;
		m_properties = properties.m_properties;
		set(properties);
	}

	/**
	 * Assignment. Does not copy property change listeners, nor the associated
	 * file.
	 * 
	 * @param properties
	 *            The properties to copy.
	 */
	public void set(ConsoleProperties properties) {

		m_consoleHost.set(properties.getConsoleHost());
		m_consolePort.set(properties.getConsolePort());
		m_propertiesFile.set(properties.getPropertiesFile());
		m_configFile.set(properties.getConfigFile());
	}

	/**
	 * Add a <code>PropertyChangeListener</code>.
	 * 
	 * @param listener
	 *            The listener.
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		m_changeSupport.addPropertyChangeListener(listener);
	}

	/**
	 * Add a <code>PropertyChangeListener</code> which listens to a particular
	 * property.
	 * 
	 * @param property
	 *            The property.
	 * @param listener
	 *            The listener.
	 */
	public void addPropertyChangeListener(String property,
			PropertyChangeListener listener) {
		m_changeSupport.addPropertyChangeListener(property, listener);
	}

	/**
	 * Save to the associated file.
	 * 
	 * @throws ConsoleException
	 *             If an error occurs.
	 */
	public void save() throws ConsoleException {
		final Iterator propertyIterator = m_propertyList.iterator();

		while (propertyIterator.hasNext()) {
			((Property) propertyIterator.next()).setToProperties();
		}

		try {
			m_properties.save();
		} catch (Bench4QProperties.PersistenceException e) {
			throw new DisplayMessageConsoleException(m_resources,
					"couldNotSaveOptionsError.text", e);
		}
	}

	/**
	 * Get the console host as a string.
	 * 
	 * @return The address.
	 */
	public String getConsoleHost() {
		return m_consoleHost.get();
	}

	/**
	 * Set the console host.
	 * 
	 * @param s
	 *            Either a machine name or the IP address.
	 * @throws ConsoleException
	 *             If the address is not valid.
	 */
	public void setConsoleHost(String s) throws ConsoleException {
		// We treat any address that we can look up as valid. I guess we
		// could also try binding to it to discover whether it is local,
		// but that could take an indeterminate amount of time.

		if (s.length() > 0) { // Empty string => all local hosts.
			final InetAddress newAddress;

			try {
				newAddress = InetAddress.getByName(s);
			} catch (UnknownHostException e) {
				throw new DisplayMessageConsoleException(m_resources,
						"unknownHostError.text");
			}

			if (newAddress.isMulticastAddress()) {
				throw new DisplayMessageConsoleException(m_resources,
						"invalidConsoleHostError.text");
			}
		}

		m_consoleHost.set(s);
	}

	/**
	 * Get the console port.
	 * 
	 * @return The port.
	 */
	public int getConsolePort() {
		return m_consolePort.get();
	}

	/**
	 * Set the console port.
	 * 
	 * @param i
	 *            The port number.
	 * @throws ConsoleException
	 *             If the port number is not sensible.
	 */
	public void setConsolePort(int i) throws ConsoleException {
		if (i < CommunicationDefaults.MIN_PORT
				|| i > CommunicationDefaults.MAX_PORT) {
			throw new DisplayMessageConsoleException(m_resources,
					"invalidPortNumberError.text", new Object[] {
							new Integer(CommunicationDefaults.MIN_PORT),
							new Integer(CommunicationDefaults.MAX_PORT), });
		}

		m_consolePort.set(i);
	}

	/**
	 * Get the config file.
	 * 
	 * @return The config file. <code>null</code> => No file set.
	 */
	public File getConfigFile() {
		return m_configFile.get();
	}

	/**
	 * Set and save the config file.
	 * 
	 * @param configFile
	 *            The config file. <code>null</code> => No file set.
	 * @throws ConsoleException
	 * @throws ConsoleException
	 *             If the property could not be saved.
	 */
	public void setAndSaveConfigFile(File configFile) throws ConsoleException {
		m_configFile.set(configFile);
		m_configFile.save();
	}

	/**
	 * Get the properties file.
	 * 
	 * @return The properties file. <code>null</code> => No file set.
	 */
	public File getPropertiesFile() {
		return m_propertiesFile.get();
	}

	/**
	 * Set and save the properties file.
	 * 
	 * @param propertiesFile
	 *            The properties file. <code>null</code> => No file set.
	 * @throws ConsoleException
	 * @throws ConsoleException
	 *             If the property could not be saved.
	 */
	public void setAndSavePropertiesFile(File propertiesFile)
			throws ConsoleException {
		m_propertiesFile.set(propertiesFile);
		m_propertiesFile.save();
	}

	private abstract class Property {
		private final String m_propertyName;
		private final Object m_defaultValue;
		private Object m_value;

		Property(String propertyName, Object defaultValue) {
			m_propertyName = propertyName;
			m_defaultValue = defaultValue;
			m_value = defaultValue;
			m_propertyList.add(this);
		}

		abstract void setFromProperties() throws ConsoleException;

		abstract void setToProperties();

		public final void save() throws ConsoleException {
			setToProperties();

			try {
				m_properties.saveSingleProperty(m_propertyName);
			} catch (Bench4QProperties.PersistenceException e) {
				throw new DisplayMessageConsoleException(m_resources,
						"couldNotSaveOptionsError.text", e);
			}
		}

		protected final String getPropertyName() {
			return m_propertyName;
		}

		protected final Object getDefaultValue() {
			return m_defaultValue;
		}

		protected final Object getValue() {
			return m_value;
		}

		protected final void setValue(Object value) {
			final Object old = m_value;
			m_value = value;
			m_changeSupport.firePropertyChange(getPropertyName(), old, m_value);
		}
	}

	private final class StringProperty extends Property {
		public StringProperty(String propertyName, String defaultValue) {
			super(propertyName, defaultValue);
		}

		public void setFromProperties() {
			set(m_properties.getProperty(getPropertyName(),
					(String) getDefaultValue()));
		}

		public void setToProperties() {
			if (get() != getDefaultValue()) {
				m_properties.setProperty(getPropertyName(), get());
			} else {
				m_properties.remove(getPropertyName());
			}
		}

		public String get() {
			return (String) getValue();
		}

		public void set(String s) {
			setValue(s);
		}
	}

	private final class PatternProperty extends Property {
		public PatternProperty(String propertyName, String defaultExpression) {
			super(propertyName, Pattern.compile(defaultExpression));
		}

		public void setFromProperties() throws ConsoleException {
			set(m_properties.getProperty(getPropertyName(), null));
		}

		public void setToProperties() {
			if (get() != getDefaultValue()) {
				m_properties.setProperty(getPropertyName(), get().pattern());
			} else {
				m_properties.remove(getPropertyName());
			}
		}

		public Pattern get() {
			return (Pattern) getValue();
		}

		public void set(String expression) throws ConsoleException {
			if (expression == null) {
				set((Pattern) getDefaultValue());
			} else {
				try {
					set(Pattern.compile(expression));
				} catch (PatternSyntaxException e) {
					throw new DisplayMessageConsoleException(m_resources,
							"regularExpressionError.text",
							new Object[] { getPropertyName(), }, e);
				}
			}
		}

		public void set(Pattern pattern) {
			setValue(pattern);
		}
	}

	private final class IntProperty extends Property {
		public IntProperty(String propertyName, int defaultValue) {
			super(propertyName, new Integer(defaultValue));
		}

		public void setFromProperties() {
			set(m_properties.getInt(getPropertyName(),
					((Integer) getDefaultValue()).intValue()));
		}

		public void setToProperties() {
			m_properties.setInt(getPropertyName(), get());
		}

		public int get() {
			return ((Integer) getValue()).intValue();
		}

		public void set(int i) {
			setValue(new Integer(i));
		}
	}

	private final class FileProperty extends Property {
		public FileProperty(String propertyName) {
			super(propertyName, null);
		}

		public void setFromProperties() {
			set(m_properties.getFile(getPropertyName(), null));
		}

		public void setToProperties() {
			if (get() != getDefaultValue()) {
				m_properties.setFile(getPropertyName(), get());
			} else {
				m_properties.remove(getPropertyName());
			}
		}

		public File get() {
			return (File) getValue();
		}

		public void set(File file) {
			setValue(file);
		}
	}

	private final class BooleanProperty extends Property {
		public BooleanProperty(String propertyName, boolean defaultValue) {
			super(propertyName, Boolean.valueOf(defaultValue));
		}

		public void setFromProperties() {
			set(m_properties.getBoolean(getPropertyName(),
					((Boolean) getDefaultValue()).booleanValue()));
		}

		public void setToProperties() {
			m_properties.setBoolean(getPropertyName(), get());
		}

		public boolean get() {
			return ((Boolean) getValue()).booleanValue();
		}

		public void set(boolean b) {
			setValue(Boolean.valueOf(b));
		}
	}

	private final class RectangleProperty extends Property {
		public RectangleProperty(String propertyName) {
			super(propertyName, null);
		}

		public void setFromProperties() {
			final String property = m_properties.getProperty(getPropertyName(),
					null);

			if (property == null) {
				set(null);
			} else {
				final StringTokenizer tokenizer = new StringTokenizer(property,
						",");

				try {
					set(new Rectangle(Integer.parseInt(tokenizer.nextToken()),
							Integer.parseInt(tokenizer.nextToken()), Integer
									.parseInt(tokenizer.nextToken()), Integer
									.parseInt(tokenizer.nextToken())));
				} catch (NoSuchElementException e) {
					set(null);
				} catch (NumberFormatException e) {
					set(null);
				}
			}
		}

		public void setToProperties() {
			final Rectangle value = get();

			if (value != getDefaultValue()) {
				m_properties.setProperty(getPropertyName(), value.x + ","
						+ value.y + "," + value.width + "," + value.height);
			} else {
				m_properties.remove(getPropertyName());
			}
		}

		public Rectangle get() {
			return (Rectangle) getValue();
		}

		public void set(Rectangle rectangle) {
			setValue(rectangle);
		}
	}
}
