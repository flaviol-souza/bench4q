package org.bench4Q;

import java.io.File;
import java.io.PrintWriter;
import org.bench4Q.agent.AgentImplementation;
import org.bench4Q.agent.AgentInterface;
import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.Logger;
import org.bench4Q.common.util.SimpleLogger;

public final class Agent extends AbstractMainClass {

	/**
	 * The Grinder agent process entry point.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {

		final Logger logger = new SimpleLogger("agent", new PrintWriter(System.out),
				new PrintWriter(System.err));

		try {
			final Agent agent = new Agent(args, logger);
			agent.run();
		} catch (LoggedInitialisationException e) {
			System.exit(1);
		} catch (Throwable e) {
			final PrintWriter errorWriter = logger.getErrorLogWriter();
			e.printStackTrace(errorWriter);
			errorWriter.flush();
			System.exit(2);
		}

		System.exit(0);
	}

	private final AgentInterface m_agent;

	private Agent(String[] args, Logger logger) throws Bench4QException {
		super(logger);

		File propertiesFile = null;

		for (int i = 0; i < args.length; ++i) {
			if (i == args.length - 1 && !args[i].startsWith("-")) {
				propertiesFile = new File(args[i]);
			} else {
				throw barfUsage();
			}
		}

		m_agent = new AgentImplementation(logger, propertiesFile, true);

	}

	private void run() throws Bench4QException {
		m_agent.run();
		m_agent.shutdown();
	}
}
