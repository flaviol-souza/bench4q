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

import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.util.Logger;
import org.bench4Q.console.ConsoleFoundation;
import org.bench4Q.console.common.Resources;
import org.bench4Q.console.common.ResourcesImplementation;
import org.bench4Q.console.ui.ConsoleUI;

/**
 *The entry of console.
 * 
 * @author duanzhiquan
 * 
 */
public final class Console {

	private final ConsoleFoundation m_consoleFoundation;

	private Console(String[] args, Resources resources) throws Bench4QException {

		Class ui = ConsoleUI.class;

		m_consoleFoundation = new ConsoleFoundation(resources);
		m_consoleFoundation.createUI(ui);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final Resources resources = new ResourcesImplementation(
				"org.bench4Q.resources.Console");

		try {
			final Console console = new Console(args, resources);
			console.run();
		} catch (Bench4QException e) {
			Logger.getLogger().fatal("Cann't initiate console.", e);
			System.exit(1);
		}
		System.exit(0);
	}

	private void run() {
		m_consoleFoundation.run();
	}
}
