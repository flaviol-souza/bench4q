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
package org.bench4Q.console.distribution;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.bench4Q.agent.messages.CacheHighWaterMark;
import org.bench4Q.common.util.Directory;

/**
 * Implementation of {@link CacheParameters}.
 */
final class CacheParametersImplementation implements CacheParameters,
		Serializable {

	private static final long serialVersionUID = 1L;

	private final Directory m_directory;
	private final Pattern m_fileFilterPattern;

	public CacheParametersImplementation(Directory directory,
			Pattern fileFilterPattern) {
		m_directory = directory;
		m_fileFilterPattern = fileFilterPattern;
	}

	public Directory getDirectory() {
		return m_directory;
	}

	public Pattern getFileFilterPattern() {
		return m_fileFilterPattern;
	}

	public CacheHighWaterMark createHighWaterMark(long time) {
		return new CacheHighWaterMarkImplementation(this, time);
	}

	public int hashCode() {
		return m_directory.hashCode()
				^ m_fileFilterPattern.pattern().hashCode();
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final CacheParametersImplementation other = (CacheParametersImplementation) o;

		return m_directory.equals(other.m_directory)
				&& m_fileFilterPattern.pattern().equals(
						other.m_fileFilterPattern.pattern());
	}

	private static final class CacheHighWaterMarkImplementation implements
			CacheHighWaterMark {

		private static final long serialVersionUID = 1L;

		private final CacheParameters m_cacheParameters;
		private final long m_time;

		public CacheHighWaterMarkImplementation(
				CacheParameters cacheParameters, long time) {
			m_cacheParameters = cacheParameters;
			m_time = time;
		}

		public boolean isForSameCache(CacheHighWaterMark other) {
			if (!(other instanceof CacheHighWaterMarkImplementation)) {
				return false;
			}

			final CacheHighWaterMarkImplementation otherHighWater = (CacheHighWaterMarkImplementation) other;

			return m_cacheParameters.equals(otherHighWater.m_cacheParameters);
		}

		public long getTime() {
			return m_time;
		}
	}
}
