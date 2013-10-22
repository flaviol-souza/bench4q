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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.SwingUtilities;

import org.bench4Q.console.common.ErrorHandler;

final class SwingDispatcherFactoryImplementation implements
		SwingDispatcherFactory {

	private final ErrorHandler m_errorHandler;

	public SwingDispatcherFactoryImplementation(ErrorHandler errorHandler) {
		m_errorHandler = errorHandler;
	}

	public Object create(final Object delegate) {

		final InvocationHandler invocationHandler = new InvocationHandler() {
			public Object invoke(Object proxy, final Method method,
					final Object[] args) throws Throwable {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							method.invoke(delegate, args);
						} catch (InvocationTargetException e) {
							m_errorHandler.handleException(e.getCause());
						} catch (IllegalArgumentException e) {
							throw new AssertionError(e);
						} catch (IllegalAccessException e) {
							throw new AssertionError(e);
						}
					}
				});

				return null;
			}
		};

		final Class delegateClass = delegate.getClass();

		return Proxy.newProxyInstance(delegateClass.getClassLoader(),
				getAllInterfaces(delegateClass), invocationHandler);
	}

	private static Class[] getAllInterfaces(Class theClass) {
		final Set interfaces = new HashSet();

		Class c = theClass;

		do {
			final Iterator iterator = Arrays.asList(c.getInterfaces())
					.iterator();

			while (iterator.hasNext()) {
				final Class anInterface = (Class) iterator.next();

				if (Modifier.isPublic(anInterface.getModifiers())) {
					interfaces.add(anInterface);
				}
			}

			c = c.getSuperclass();
		} while (c != null);

		return (Class[]) interfaces.toArray(new Class[interfaces.size()]);
	}
}
