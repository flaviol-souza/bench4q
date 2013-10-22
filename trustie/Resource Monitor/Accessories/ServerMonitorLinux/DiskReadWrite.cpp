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
 * DIskReadWrite.cpp
 *
 *  Created on: Jul 17, 2010
 *      Author: xiaowei zhou
 */

#include "DiskReadWrite.h"
#include <stdio.h>
#include <string.h>
#include <limits.h>
#include <unistd.h>
#include <sys/timeb.h>

#define MAX_NAME_LEN	72

long long DiskReadWrite::last_total_rd_sec;
long long DiskReadWrite::last_total_wr_sec;
long long DiskReadWrite::last_time_millis;
bool DiskReadWrite::yetGetted = false;

/**
 * For comments, see DiskReadWrite.h
 */
int DiskReadWrite::is_device(char *name) {
	char syspath[PATH_MAX];
	char *slash;

	/* Some devices may have a slash in their name (eg. cciss/c0d0...) */
	while ((slash = strchr(name, '/'))) {
		*slash = '!';
	}
	snprintf(syspath, sizeof(syspath), "%s/%s", "/sys/block", name);

	return !(access(syspath, F_OK));
}

/**
 * For comments, see DiskReadWrite.h
 */
void DiskReadWrite::getDiskReadWriteBytesPerSec(double *bytesReadPerSec, double *bytesWritePerSec) {

	char line[256];
	FILE *fp;
	char dev_name[MAX_NAME_LEN]; 	// device name

	// accumulated read and write sectors(512bytes) of one disk drive
	unsigned long long rd_sec, wr_sec;

	// accumulated read and write sectors(512bytes) of all drives
	unsigned long long total_rd_sec, total_wr_sec;

	struct timeb time_now;
	long long time_now_millis;
	int i;

	if ((fp = fopen("/proc/diskstats", "r")) == NULL) {
		// cannot open diskstats file
		*bytesReadPerSec = -1.0;
		*bytesWritePerSec = -1.0;
		return;
	}

	// get current time millis
	ftime(&time_now);
	time_now_millis = (long long) time_now.time * 1000LL
			+ (long long) time_now.millitm;

	total_rd_sec = 0LL;
	total_wr_sec = 0LL;
	while (fgets(line, 256, fp) != NULL) {
		// every line of the diskstats file is the data of one drive (or partition)

		/* major minor name rio rmerge rsect ruse wio wmerge wsect wuse running use aveq */
//		i = sscanf(line,
//				"%u %u %s %lu %lu %llu %lu %lu %lu %llu %lu %lu %lu %lu",
//				&major, &minor, dev_name, &rd_ios, &rd_merges,
//				&rd_sec, &rd_ticks, &wr_ios, &wr_merges,
//				&wr_sec, &wr_ticks, &ios_pgr, &tot_ticks, &rq_ticks);
		i = sscanf(line,
				"%*u %*u %s %*u %*u %llu %*u %*u %*u %llu %*u %*u %*u %*u",
				dev_name, &rd_sec, &wr_sec);

		if (i != 3 || !is_device(dev_name)) {
			continue;
		}

		// decide whether to add the value to the reads and writes
		// only add disk drives, ignoring partitions and other
		if(strlen(dev_name) >= 3) {
			if ((dev_name[0] == 's' || dev_name[0] == 'h') && dev_name[1]
					== 'd' && dev_name[2] >= 97 && dev_name[2] <= 122
					&& dev_name[3] == '\0') {
				// the device name is like sda, sdb, hda, hdb etc.
				total_rd_sec += rd_sec;
				total_wr_sec += wr_sec;
			}
		}
	} // while (fgets(line, 256, fp) != NULL)

	fclose(fp);

	if(yetGetted) {
		// calculate the disk read and write bytes per second
		if (time_now_millis != last_time_millis) {
			*bytesReadPerSec = (double) (total_rd_sec - last_total_rd_sec)
					/ (double) (time_now_millis - last_time_millis) * 1000.0
					* 512.0;
			*bytesWritePerSec = (double) (total_wr_sec - last_total_wr_sec)
					/ (double) (time_now_millis - last_time_millis) * 1000.0
					* 512.0;
		} else {
			*bytesReadPerSec = 0.0;
			*bytesWritePerSec = 0.0;
		}

		last_time_millis = time_now_millis;
		last_total_rd_sec = total_rd_sec;
		last_total_wr_sec = total_wr_sec;
	} else {
		// the first time of getting the values, always return zero
		last_time_millis = time_now_millis;
		last_total_rd_sec = total_rd_sec;
		last_total_wr_sec = total_wr_sec;
		*bytesReadPerSec = 0.0;
		*bytesWritePerSec = 0.0;
		yetGetted = true;
	}


}
