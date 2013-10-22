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

package org.bench4Q.agent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.bench4Q.agent.common.EngineException;
import org.bench4Q.agent.messages.CacheHighWaterMark;
import org.bench4Q.agent.messages.ClearCacheMessage;
import org.bench4Q.agent.messages.DistributeFileMessage;
import org.bench4Q.agent.messages.DistributionCacheCheckpointMessage;
import org.bench4Q.common.UncheckedInterruptedException;
import org.bench4Q.common.communication.CommunicationException;
import org.bench4Q.common.communication.Message;
import org.bench4Q.common.communication.MessageDispatchRegistry;
import org.bench4Q.common.communication.MessageDispatchRegistry.AbstractHandler;
import org.bench4Q.common.util.Directory;
import org.bench4Q.common.util.FileContents;
import org.bench4Q.common.util.StreamCopier;

/**
 * Process {@link ClearCacheMessage}s and {@link DistributeFileMessage}s
 * received from the console.
 * 
 */
public final class FileStore {

	private final File m_readmeFile;

	// Access guarded by self.
	private final Directory m_incomingDirectory;

	private final Directory m_currentDirectory;

	// Guarded by m_incomingDirectory
	private boolean m_incremental;

	private volatile CacheHighWaterMark m_cacheHighWaterMark = new OutOfDateCacheHighWaterMark();

	/**
	 * @param directory
	 * @throws FileStoreException
	 */
	public FileStore(File directory) throws FileStoreException {

		final File rootDirectory = directory.getAbsoluteFile();

		if (rootDirectory.exists()) {
			if (!rootDirectory.isDirectory()) {
				throw new FileStoreException("Could not write to directory '"
						+ rootDirectory
						+ "' as file with that name already exists");
			}

			if (!rootDirectory.canWrite()) {
				throw new FileStoreException("Could not write to directory '"
						+ rootDirectory + "'");
			}
		}

		m_readmeFile = new File(rootDirectory, "README.txt");

		try {
			m_incomingDirectory = new Directory(new File(rootDirectory,
					"incoming"));
			m_currentDirectory = new Directory(new File(rootDirectory,
					"current"));
		} catch (Directory.DirectoryException e) {
			throw new FileStoreException(e.getMessage(), e);
		}

		m_incremental = false;
	}

	/**
	 * @return CacheHighWaterMark
	 * @throws FileStoreException
	 */
	public Directory getDirectory() throws FileStoreException {
		try {
			synchronized (m_incomingDirectory) {
				if (m_incomingDirectory.getFile().exists()) {
					m_incomingDirectory.copyTo(m_currentDirectory,
							m_incremental);
				}

				m_incremental = true;
			}

			return m_currentDirectory;
		} catch (IOException e) {
			UncheckedInterruptedException.ioException(e);
			throw new FileStoreException(
					"Could not create file store directory", e);
		}
	}

	/**
	 * @return CacheHighWaterMark
	 */
	public CacheHighWaterMark getCacheHighWaterMark() {
		return m_cacheHighWaterMark;
	}

	/**
	 * Registers message handlers with a dispatcher.
	 * 
	 * @param messageDispatcher
	 *            The dispatcher.
	 */

	public void registerMessageHandlers(
			MessageDispatchRegistry messageDispatcher) {

		messageDispatcher.set(ClearCacheMessage.class, new AbstractHandler() {
			public void send(Message message) throws CommunicationException {
				// m_logger.output("Clearing file store");

				try {
					synchronized (m_incomingDirectory) {
						m_incomingDirectory.deleteContents();
						m_incremental = false;
					}
				} catch (Directory.DirectoryException e) {
					// m_logger.error(e.getMessage());
					throw new CommunicationException(e.getMessage(), e);
				}
			}
		});

		messageDispatcher.set(DistributeFileMessage.class,
				new AbstractHandler() {
					public void send(Message message)
							throws CommunicationException {
						try {
							synchronized (m_incomingDirectory) {
								m_incomingDirectory.create();

								createReadmeFile();

								final FileContents fileContents = ((DistributeFileMessage) message)
										.getFileContents();

								// m_logger.output("Updating file store: " +
								// fileContents);
								fileContents.create(m_incomingDirectory);
							}
						} catch (FileContents.FileContentsException e) {
							// m_logger.error(e.getMessage());
							throw new CommunicationException(e.getMessage(), e);
						} catch (Directory.DirectoryException e) {
							// m_logger.error(e.getMessage());
							throw new CommunicationException(e.getMessage(), e);
						}
					}
				});

		messageDispatcher.set(DistributionCacheCheckpointMessage.class,
				new AbstractHandler() {
					public void send(Message message)
							throws CommunicationException {
						m_cacheHighWaterMark = ((DistributionCacheCheckpointMessage) message)
								.getCacheHighWaterMark();
					}
				});
	}

	private void createReadmeFile() throws CommunicationException {
		if (!m_readmeFile.exists()) {
			try {
				new StreamCopier(4096, true).copy(getClass()
						.getResourceAsStream("resources/FileStoreReadme.txt"),
						new FileOutputStream(m_readmeFile));
			} catch (IOException e) {
				UncheckedInterruptedException.ioException(e);
				// m_logger.error(e.getMessage());
				throw new CommunicationException(e.getMessage(), e);
			}
		}
	}

	/**
	 * Exception that indicates a <code>FileStore</code> related problem.
	 */
	public static final class FileStoreException extends EngineException {
		FileStoreException(String message) {
			super(message);
		}

		FileStoreException(String message, Throwable e) {
			super(message, e);
		}
	}

	private static final class OutOfDateCacheHighWaterMark implements
			CacheHighWaterMark {

		public long getTime() {
			return -1;
		}

		public boolean isForSameCache(CacheHighWaterMark other) {
			return false;
		}
	}
}
