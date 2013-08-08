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
package org.bench4Q.common.util;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bench4Q.common.Bench4QException;

/**
 * Introspects a boolean property of a Java Bean and provides setter and getter
 * methods.
 */
public final class BooleanProperty {
	private final Object m_bean;
	private final Class m_beanClass;
	private final PropertyDescriptor m_propertyDescriptor;

	/**
	 * Constructor.
	 * 
	 * @param bean
	 *            Bean to introspect.
	 * @param propertyName
	 *            The property.
	 * @throws PropertyException
	 *             If a boolean property of the given name could not be found.
	 */
	public BooleanProperty(Object bean, String propertyName)
			throws PropertyException {

		m_bean = bean;
		m_beanClass = bean.getClass();

		try {
			m_propertyDescriptor = new PropertyDescriptor(propertyName,
					m_beanClass);
		} catch (IntrospectionException e) {
			throw new PropertyException("Could not find property '"
					+ propertyName + "' in class '" + m_beanClass + "'", e);
		}

		final Class propertyType = m_propertyDescriptor.getPropertyType();

		if (!propertyType.equals(Boolean.TYPE)
				&& !propertyType.equals(Boolean.class)) {
			throw new PropertyException(toString()
					+ ": property is not boolean");
		}
	}

	/**
	 * Getter method.
	 * 
	 * @return The current value of the property.
	 * @throws PropertyException
	 *             If the value could not be read.
	 */
	public boolean get() throws PropertyException {

		// Despite what the JavaDoc for PropertyDescriptor.getReadMethod()
		// says, this is guaranteed to be non-null if the property name is
		// non-null.
		final Method readMethod = m_propertyDescriptor.getReadMethod();

		try {
			final Boolean result = (Boolean) readMethod.invoke(m_bean,
					new Object[0]);
			return result.booleanValue();
		} catch (IllegalAccessException e) {
			throw new PropertyException(toString() + ": could not read", e);
		} catch (InvocationTargetException e) {
			throw new PropertyException(toString() + ": could not read", e
					.getTargetException());
		}
	}

	/**
	 * Setter method.
	 * 
	 * @param value
	 *            The new value of the property.
	 * @throws PropertyException
	 *             If the value could not be written.
	 */
	public void set(boolean value) throws PropertyException {
		// Despite what the JavaDoc for
		// PropertyDescriptor.getWriteMethod() says, this is guaranteed to
		// be non-null if the property name is non-null.
		final Method writeMethod = m_propertyDescriptor.getWriteMethod();

		try {
			writeMethod.invoke(m_bean, new Object[] { value ? Boolean.TRUE
					: Boolean.FALSE });
		} catch (IllegalAccessException e) {
			throw new PropertyException(toString() + ": could not write", e);
		} catch (InvocationTargetException e) {
			throw new PropertyException(toString() + ": could not write", e
					.getTargetException());
		}
	}

	/**
	 * Describe the property.
	 * 
	 * @return A description of the property.
	 */
	public String toString() {
		return m_beanClass.getName() + "." + m_propertyDescriptor.getName();
	}

	/**
	 * Indicates a problem with accessing the property.
	 */
	public static final class PropertyException extends Bench4QException {

		private PropertyException(String message) {
			super(message);
		}

		private PropertyException(String message, Throwable t) {
			super(message, t);
		}
	}
}
