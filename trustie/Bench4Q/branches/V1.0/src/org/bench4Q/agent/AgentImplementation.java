package org.bench4Q.agent;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import org.bench4Q.agent.common.ConnectorFactory;
import org.bench4Q.agent.communication.ConsoleListener;
import org.bench4Q.agent.messages.StartJASptEMessage;
import org.bench4Q.agent.messages.TestResultMessage;
import org.bench4Q.agent.rbe.RBE;
import org.bench4Q.agent.rbe.communication.Args;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.common.Bench4QBuild;
import org.bench4Q.common.Bench4QException;
import org.bench4Q.common.Bench4QProperties;
import org.bench4Q.common.Logger;
import org.bench4Q.common.communication.ClientReceiver;
import org.bench4Q.common.communication.ClientSender;
import org.bench4Q.common.communication.CommunicationException;
import org.bench4Q.common.communication.ConnectionType;
import org.bench4Q.common.communication.Connector;
import org.bench4Q.common.communication.FanOutStreamSender;
import org.bench4Q.common.communication.IgnoreShutdownSender;
import org.bench4Q.common.communication.Message;
import org.bench4Q.common.communication.MessageDispatchSender;
import org.bench4Q.common.communication.MessagePump;
import org.bench4Q.common.communication.TeeSender;
import org.bench4Q.common.util.Directory;
import org.bench4Q.common.util.thread.Condition;
import org.bench4Q.console.messages.AgentAddress;
import org.bench4Q.console.messages.AgentProcessReportMessage;

public final class AgentImplementation implements AgentInterface {

	private Args m_args;

	private final Logger m_logger;
	private final File m_alternateFile;
	private final boolean m_proceedWithoutConsole;

	private AgentStateReport m_agentStateReport = null;

	private final Timer m_timer = new Timer(true);
	private final Condition m_eventSynchronisation = new Condition();
	private final AgentIdentityImplementation m_agentIdentity;
	private final ConsoleListener m_consoleListener;
	private final FanOutStreamSender m_fanOutStreamSender = new FanOutStreamSender(3);
	private final ConnectorFactory m_connectorFactory = new ConnectorFactory(ConnectionType.AGENT);
	
	
	private ConsoleCommunication consoleCommunication = null;

	/**
	 * We use an most one file store throughout an agent's life, but can't
	 * initialise it until we've read the properties and connected to the
	 * console.
	 */
	private volatile FileStore m_fileStore;

	public AgentImplementation(Logger logger, File alternateFile, boolean proceedWithoutConsole)
			throws Bench4QException {

		m_logger = logger;
		m_alternateFile = alternateFile;
		m_proceedWithoutConsole = proceedWithoutConsole;

		
		m_agentIdentity = new AgentIdentityImplementation(getHostName());
		m_consoleListener = new ConsoleListener(m_eventSynchronisation, m_logger, m_agentIdentity);
	}

	/**
	 * Run the Grinder agent process.
	 * 
	 * @throws Bench4QException
	 *             If an error occurs.
	 */
	public void run() throws Bench4QException {

		StartJASptEMessage startMessage = null;
		

		try {
			while (true) {
				m_logger.output(Bench4QBuild.getName());

				Bench4QProperties properties;
				boolean flag = true;

				do {
					properties = new Bench4QProperties(m_alternateFile != null ? m_alternateFile
							: Bench4QProperties.DEFAULT_PROPERTIES);

					if (startMessage != null) {
						properties.putAll(startMessage.getProperties());
					}

					m_agentIdentity
							.setName(properties.getProperty("grinder.hostID", getHostName()));

					m_agentIdentity.setIP(properties.getProperty("grinder.hostIP", getHostIP()));

					final Connector connector = properties.getBoolean("grinder.useConsole", true) ? m_connectorFactory
							.create(properties)
							: null;

					// We only reconnect if the connection details have changed.
					if (consoleCommunication != null
							&& !consoleCommunication.getConnector().equals(connector)) {

						consoleCommunication = null;
					}

					if (consoleCommunication == null && connector != null) {
						try {
							consoleCommunication = new ConsoleCommunication(connector);
							m_agentStateReport = new AgentStateReport(consoleCommunication);
							m_agentStateReport
									.sendStateMessage(AgentProcessReportMessage.STATE_STARTED);
							m_logger.output("connected to console at "
									+ connector.getEndpointAsString());
						} catch (CommunicationException e) {
							if (m_proceedWithoutConsole) {
								m_logger.error(e.getMessage()
										+ ", proceeding without the console; set "
										+ "grinder.useConsole=false to disable this warning.");
							} else {
								m_logger.error(e.getMessage());
								return;
							}
						}
					}

					if (consoleCommunication != null && startMessage == null) {
						m_logger.output("waiting for console signal");
						m_consoleListener.waitForMessage();

						if (m_consoleListener.received(ConsoleListener.START)) {
							startMessage = m_consoleListener.getLastStartJASptEMessage();
							continue; // Loop to handle new properties.
						} else {
							break; // Another message, check at end of outer
							// while loop.
						}
					}

					if (startMessage != null) {
						final Bench4QProperties messageProperties = startMessage.getProperties();
						m_args = startMessage.getArgs();
						final Directory fileStoreDirectory = m_fileStore.getDirectory();

						System.out.println("got start message");
						// System.out.println(startMessage.getAgentNumber());

						// Convert relative path to absolute path.
						messageProperties.setAssociatedFile(fileStoreDirectory
								.getFile(messageProperties.getAssociatedFile()));

						m_agentIdentity.setNumber(startMessage.getAgentNumber());

						flag = false;
					} else {
						m_agentIdentity.setNumber(-1);
					}

				} while (flag);

				if (startMessage != null) {
					
					m_consoleListener.setTestFinished(false);
					System.out.println("test started");
					m_agentStateReport.sendStateMessage(AgentProcessReportMessage.STATE_RUNNING);

//					 try {
//					 Thread.sleep(5000);
//					 } catch (InterruptedException e) {
//					 e.printStackTrace();
//					 }

					RBE rbe = new RBE(m_args);
					
					rbe.startWorkers();

					System.out.println("test finished");

					m_agentStateReport.sendStateMessage(AgentProcessReportMessage.STATE_FINISHED);
					
					m_consoleListener.setTestFinished(true);	
					
					m_consoleListener.waitForMessage();
					
					if (m_consoleListener.received(ConsoleListener.COLLECT)) {
						System.out.println("Received collection message.");
						System.out.println("sending TestResultMessage message....");
						Message result = new TestResultMessage(m_agentIdentity, EBStats
								.getEBStats(), m_fileStore.getCacheHighWaterMark());
						consoleCommunication.getSender().send(result);
					}
					

				}				

				if (consoleCommunication == null) {
					break;
				} else {
					// Ignore any pending start messages.
					m_consoleListener.discardMessages(ConsoleListener.START);

					if (!m_consoleListener.received(ConsoleListener.ANY)) {
						// We've got here naturally, without a console signal.
						m_logger.output("finished, waiting for console signal");
						m_consoleListener.waitForMessage();
					}

					if (m_consoleListener.received(ConsoleListener.START)) {
						startMessage = m_consoleListener.getLastStartJASptEMessage();
					} else if (m_consoleListener.received(ConsoleListener.STOP
							| ConsoleListener.SHUTDOWN)) {
						break;
					} else {
						// ConsoleListener.RESET or natural death.
						startMessage = null;
					}
				}
			}
		} finally {
			shutdownConsoleCommunication(consoleCommunication);
		}
	}

	private void shutdownConsoleCommunication(ConsoleCommunication consoleCommunication) {

		if (consoleCommunication != null) {
			consoleCommunication.shutdown();
		}

		m_consoleListener.discardMessages(ConsoleListener.ANY);
	}

	/**
	 * Clean up resources.
	 */
	public void shutdown() {
		m_timer.cancel();
		m_fanOutStreamSender.shutdown();
		m_consoleListener.shutdown();

		m_logger.output("finished");
	}

	private static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "UNNAMED HOST";
		}
	}

	private static String getHostIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			return "UNNAMED HOST";
		}
	}

	private final class ConsoleCommunication {
		private final ClientSender m_sender;
		private final Connector m_connector;
		private final MessagePump m_messagePump;

		public ConsoleCommunication(Connector connector) throws CommunicationException,
				FileStore.FileStoreException {

			final ClientReceiver receiver = ClientReceiver.connect(connector, new AgentAddress(
					m_agentIdentity));
			m_sender = ClientSender.connect(receiver);
			m_connector = connector;

			if (m_fileStore == null) {
				// Only create the file store if we connected.
				m_fileStore = new FileStore(new File("./" + m_agentIdentity.getName()
						+ "-file-store"), m_logger);
				m_consoleListener.setFileStore(m_fileStore);
			}

			m_sender.send(new AgentProcessReportMessage(m_agentIdentity,
					AgentProcessReportMessage.STATE_STARTED, m_fileStore.getCacheHighWaterMark()));

			final MessageDispatchSender fileStoreMessageDispatcher = new MessageDispatchSender();
			m_fileStore.registerMessageHandlers(fileStoreMessageDispatcher);

			final MessageDispatchSender messageDispatcher = new MessageDispatchSender();
			m_consoleListener.registerMessageHandlers(messageDispatcher);

			// Everything that the file store doesn't handle is tee'd to the
			// worker processes and our message handlers.
			fileStoreMessageDispatcher.addFallback(new TeeSender(messageDispatcher,
					new IgnoreShutdownSender(m_fanOutStreamSender)));

			m_messagePump = new MessagePump(receiver, fileStoreMessageDispatcher, 1);

		}

		public Connector getConnector() {
			return m_connector;
		}

		public ClientSender getSender() {
			return m_sender;
		}

		public void shutdown() {
			try {
				m_sender.send(new AgentProcessReportMessage(m_agentIdentity,
						AgentProcessReportMessage.STATE_FINISHED, m_fileStore
								.getCacheHighWaterMark()));
			} catch (CommunicationException e) {
				// Ignore - peer has probably shut down.
			} finally {
				m_messagePump.shutdown();
			}
		}
	}

	private final class AgentStateReport {

		private ConsoleCommunication m_consoleCommunication;
		private TimerTask m_reportRunningTask;

		public AgentStateReport(ConsoleCommunication consoleCommunication) {
			m_consoleCommunication = consoleCommunication;
		}

		public void sendStateMessage(short state) {
			final short m_state = state;
			if (m_reportRunningTask != null) {
				m_reportRunningTask.cancel();
			}
			m_reportRunningTask = new TimerTask() {
				public void run() {
					try {
						if (m_consoleCommunication != null) {
							m_consoleCommunication.getSender().send(
									new AgentProcessReportMessage(m_agentIdentity, m_state,
											m_fileStore.getCacheHighWaterMark()));
						}

					} catch (CommunicationException e) {
						cancel();
						e.printStackTrace();
					}
				}
			};
			m_timer.schedule(m_reportRunningTask, 1000, 1000);
		}

		public void shutdown() {
			m_reportRunningTask.cancel();
		}

	}
	
	
}
