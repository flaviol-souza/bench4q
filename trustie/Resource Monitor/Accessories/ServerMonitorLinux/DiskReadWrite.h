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
 * DIskReadWrite.h
 *
 *  Created on: Jul 17, 2010
 *      Author: xiaowei zhou
 */

#ifndef DISKREADWRITE_H_
#define DISKREADWRITE_H_

/**
 * get disk read and written bytes per second
 */
class DiskReadWrite {
private:

	/**
	 * previous total read sectors, one sector is 512bytes
	 */
	static long long last_total_rd_sec;

	/**
	 * previous total written sectors, one sector is 512bytes
	 */
	static long long last_total_wr_sec;

	/**
	 * last system time in millis
	 */
	static long long last_time_millis;

	/**
	 * has the method for getting disk read and write data be invoked before
	 */
	static bool yetGetted;

	/**
	 * decide whether the given name is a name for a device
	 */
	static int is_device(char *name);

public:

	/**
	 * Get the disk read and written bytes per second, the first call to this method will return invalid value.
	 */
	static void getDiskReadWriteBytesPerSec(double *bytesReadPerSec, double *bytesWritePerSec);
};

#endif /* DISKREADWRITE_H_ */
