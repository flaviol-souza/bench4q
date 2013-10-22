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
 * NetIntfSendRecv.cpp
 *
 *  Created on: Jul 18, 2010
 *      Author: xiaowei zhou
 */

#include "NetIntfSendRecv.h"

#include <stdio.h>
#include <string.h>
#include <sys/timeb.h>

#define MAX_NAME_LEN	72

long long NetIntfSendRecv::last_total_recv_bytes;
long long NetIntfSendRecv::last_total_sent_bytes;
long long NetIntfSendRecv::last_time_millis;
bool NetIntfSendRecv::yetGetted = false;

/**
 * For comments, see NetIntfSendRecv.h
 */
void NetIntfSendRecv::getNetSendRecvBytesPerSec(double *bytesRecvPerSec, double *bytesSentPerSec)
{
	char line[256];
	FILE *fp;
	char dev_name[MAX_NAME_LEN]; 	// device name

	// accumulated send and receive bytes of one network interface
	long long recv_bytes, sent_bytes;

	// accumulated send and receive bytes of all network interfaces
	long long total_recv_bytes, total_sent_bytes;

	struct timeb time_now;
	long long time_now_millis;
	int i;

	if ((fp = fopen("/proc/net/dev", "r")) == NULL) {
		// cannot open net stat file
		*bytesRecvPerSec = -1.0;
		*bytesSentPerSec = -1.0;
		return;
	}

	// get current time millis
	ftime(&time_now);
	time_now_millis = (long long) time_now.time * 1000LL
			+ (long long) time_now.millitm;

	total_recv_bytes = 0LL;
	total_sent_bytes = 0LL;
	while (fgets(line, 256, fp) != NULL) {
		// every line of the net stat file is the data of one network interface

		i = sscanf(line,
				"%*[ ]%[0-9a-zA-Z]: %llu %*u %*u %*u %*u %*u %*u %*u %llu %*u %*u %*u %*u %*u %*u %*u",
				dev_name, &recv_bytes, &sent_bytes);

		if (i != 3 || strcmp(dev_name, "lo") == 0) {
			// ignoring the invalid device and the loop back interface
			continue;
		}

		total_recv_bytes += recv_bytes;
		total_sent_bytes += sent_bytes;
	} // while (fgets(line, 256, fp) != NULL) {

	fclose(fp);

	if(yetGetted) {
		// calculate the network send and receive bytes per second
		if (time_now_millis != last_time_millis) {
			*bytesRecvPerSec = (double) (total_recv_bytes - last_total_recv_bytes)
					/ (double) (time_now_millis - last_time_millis) * 1000.0;
			*bytesSentPerSec = (double) (total_sent_bytes - last_total_sent_bytes)
					/ (double) (time_now_millis - last_time_millis) * 1000.0;
		} else {
			*bytesRecvPerSec = 0.0;
			*bytesSentPerSec = 0.0;
		}

		last_time_millis = time_now_millis;
		last_total_recv_bytes = total_recv_bytes;
		last_total_sent_bytes = total_sent_bytes;
	} else {
		// the first time of getting the values, always return zero
		last_time_millis = time_now_millis;
		last_total_recv_bytes = total_recv_bytes;
		last_total_sent_bytes = total_sent_bytes;
		*bytesRecvPerSec = 0.0;
		*bytesSentPerSec = 0.0;
		yetGetted = true;
	}
}



