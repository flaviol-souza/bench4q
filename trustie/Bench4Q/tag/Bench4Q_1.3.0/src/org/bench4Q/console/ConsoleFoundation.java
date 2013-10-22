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

import java.io.File;
import java.util.Timer;

import org.bench4Q.common.Bench4QException;
import org.bench4Q.console.common.ErrorHandler;
import org.bench4Q.console.common.ErrorQueue;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.communication.ConsoleCommunication;
import org.bench4Q.console.communication.ConsoleCommunicationImplementation;
import org.bench4Q.console.communication.DistributionControlImplementation;
import org.bench4Q.console.communication.ProcessControlImplementation;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * This is the entry point of The Bench4Q console.
 */
public final class ConsoleFoundation {

	private final MutablePicoContainer m_container;
	private final Timer m_timer;

	/**
	 * Constructor. Locates the console properties in the user's home directory.
	 * 
	 * @param resources
	 *            Console resources
	 * 
	 * @exception Bench4QException
	 *                If an error occurs.
	 */
	public ConsoleFoundation(Resources resources) throws Bench4QException {

		this(resources, new Timer(true), new ConsoleProperties(resources,
		// Some platforms do not have user home directories, fall back
				// to java.home.
				new File(System.getProperty("user.home", System
						.getProperty("java.home")), ".bench4Q_console")));
	}

	/**
	 * Constructor. Allows properties to be specified.
	 * 
	 * @param resources
	 *            Console resources
	 * @param timer
	 *            A timer.
	 * @param properties
	 *            The properties.
	 * 
	 * @exception Bench4QException
	 *                If an error occurs.
	 */
	public ConsoleFoundation(Resources resources, Timer timer,
			ConsoleProperties properties) throws Bench4QException {

		m_timer = timer;

		m_container = new DefaultPicoContainer();

		m_container.registerComponentInstance(resources);
		m_container.registerComponentInstance(properties);
		m_container.registerComponentInstance(timer);

		m_container
				.registerComponentImplementation(ConsoleCommunicationImplementation.class);
		m_container
				.registerComponentImplementation(DistributionControlImplementation.class);
		m_container
				.registerComponentImplementation(ProcessControlImplementation.class);
		m_container.registerComponentImplementation(ErrorQueue.class);
	}

	/**
	 * Factory method to create a console user interface implementation.
	 * PicoContainer is used to satisfy the requirements of the implementation's
	 * constructor.
	 * 
	 * @param uiClass
	 *            The implementation class - must implement
	 *            {@link ConsoleFoundation.UI}.
	 * @return An instance of the user interface class.
	 */
	public UI createUI(Class uiClass) {
		m_container.registerComponentImplementation(uiClass);

		final UI ui = (UI) m_container.getComponentInstanceOfType(uiClass);

		final ErrorQueue errorQueue = (ErrorQueue) m_container
				.getComponentInstanceOfType(ErrorQueue.class);

		errorQueue.setErrorHandler(ui.getErrorHandler());

		return ui;
	}

	/**
	 * Shut down the console.
	 * 
	 */
	public void shutdown() {
		final ConsoleCommunication communication = (ConsoleCommunication) m_container
				.getComponentInstanceOfType(ConsoleCommunication.class);

		communication.shutdown();

		m_timer.cancel();
	}

	/**
	 * Console message event loop. Dispatches communication messages
	 * appropriately. Blocks until we are {@link #shutdown()}.
	 */
	public void run() {
		m_container.start();

		final ConsoleCommunication communication = (ConsoleCommunication) m_container
				.getComponentInstanceOfType(ConsoleCommunication.class);

		while (communication.processOneMessage()) {
			// Process until communication is shut down.
		}
	}

	/**
	 * Contract for user interfaces.
	 */
	public interface UI {
		/**
		 * Return an error handler to which errors should be reported.
		 * 
		 * @return The error handler.
		 */
		ErrorHandler getErrorHandler();
	}

}
