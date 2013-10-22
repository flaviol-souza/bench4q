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


final class SwingDispatcherFactoryImplementation implements SwingDispatcherFactory {

	private final ErrorHandler m_errorHandler;

	public SwingDispatcherFactoryImplementation(ErrorHandler errorHandler) {
		m_errorHandler = errorHandler;
	}

	public Object create(final Object delegate) {

		final InvocationHandler invocationHandler = new InvocationHandler() {
			public Object invoke(Object proxy, final Method method, final Object[] args)
					throws Throwable {

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
			final Iterator iterator = Arrays.asList(c.getInterfaces()).iterator();

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
