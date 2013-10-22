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

package org.bench4Q;

import java.io.PrintWriter;

import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.Logger;
import org.bench4Q.common.util.SimpleLogger;
import org.bench4Q.console.ConsoleFoundation;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.common.ResourcesImplementation;
import org.bench4Q.console.ui.ConsoleUI;

/**
 * @author duanzhiquan
 *
 */
public final class Console extends AbstractMainClass {

	private final ConsoleFoundation m_consoleFoundation;

	private Console(String[] args, Resources resources, Logger logger) throws Bench4QException {

		super(logger);

		Class ui = ConsoleUI.class;

		m_consoleFoundation = new ConsoleFoundation(resources, logger);
		m_consoleFoundation.createUI(ui);
	}

	private void run() {
		m_consoleFoundation.run();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Resources resources = new ResourcesImplementation("org.bench4Q.resources.Console");

		final Logger logger = new SimpleLogger(resources.getString("shortTitle"), new PrintWriter(
				System.out), new PrintWriter(System.err));

		try {
			final Console console = new Console(args, resources, logger);
			console.run();
		} catch (LoggedInitialisationException e) {
			System.exit(1);
		} catch (Bench4QException e) {
			logger.error("Could not initialise:");
			final PrintWriter errorWriter = logger.getErrorLogWriter();
			e.printStackTrace(errorWriter);
			errorWriter.flush();
			System.exit(2);
		}

		System.exit(0);
	}
}
