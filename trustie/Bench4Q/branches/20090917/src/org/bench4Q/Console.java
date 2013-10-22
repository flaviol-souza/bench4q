// Copyright (C) 2000 Paco Gomez
// Copyright (C) 2000 - 2008 Philip Aston
// All rights reserved.
//
// This file is part of The Grinder software distribution. Refer to
// the file LICENSE which is part of The Grinder distribution for
// licensing details. The Grinder distribution is available on the
// Internet at http://grinder.sourceforge.net/
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
// FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
// COPYRIGHT HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
// INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
// HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
// STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
// OF THE POSSIBILITY OF SUCH DAMAGE.

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
 * This is the entry point of The Grinder console.
 * 
 * @author Philip Aston
 * @version $Revision: 3762 $
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
	 * Entry point.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		final Resources resources = new ResourcesImplementation(
				"org.bench4Q.resources.Console");

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
