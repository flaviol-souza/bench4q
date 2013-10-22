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
 * MemAvail.h
 *
 *  Created on: Jul 17, 2010
 *      Author: xiaowei zhou
 */

#ifndef MEMAVAIL_H_
#define MEMAVAIL_H_

/**
 * get available physical memory in MBytes
 */
class MemAvail {
private:
	static char buffer[4096 + 1];
	static char * skip_token(const char *p);
	static char * skip_line(const char *p);

public:
	/**
	 * get available physical memory in MBytes
	 */
	static double getAvailablePhysicalMemoryMB();

};

#endif /* MEMAVAIL_H_ */
