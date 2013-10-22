/**
 * =========================================================================
 * 			Bench4Q Server Monitor for Linux
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
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *  * Developer(s): Xiaowei Zhou.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 */

/*
 * CPUUsages.h
 *
 *  Created on: Jul 16, 2010
 *      Author: xiaowei zhou
 */

#ifndef CPUUSAGES_H_
#define CPUUSAGES_H_

/**
 * get cpu usages
 */
class CPUUsages {
private:

	const static int NCPUSTATES = 5;

	/**
	 * Current CPU time
	 */
	static long cp_time[NCPUSTATES];

	/**
	 * Previous CPU time
	 */
	static long cp_old[NCPUSTATES];

	/**
	 * difference between current and previous CPU time
	 */
	static long cp_diff[NCPUSTATES];

	static char buffer[4096 + 1];

	static inline char * skip_token(const char *p);

	/**
	 * Compute the difference between current and previous CPU time
	 */
	static long computeDiff(int cnt, int *out, register long *newvar, register long *old, long *diffs);

public:

	/**
	 * Get the CPU usage percentage, the first call to this method will return invalid value.
	 */
	static double getCpuUsages();


};

#endif /* CPUUSAGES_H_ */
