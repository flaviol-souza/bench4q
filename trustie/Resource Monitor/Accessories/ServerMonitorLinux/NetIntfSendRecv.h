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
 * NetIntfSendRecv.h
 *
 *  Created on: Jul 18, 2010
 *      Author: xiaowei zhou
 */

#ifndef NETINTFSENDRECV_H_
#define NETINTFSENDRECV_H_

/**
 * get network received and sent bytes per second of all interfaces, ignoring loopback interface
 */
class NetIntfSendRecv {
private:

	/**
	 * previous total received bytes
	 */
	static long long last_total_recv_bytes;

	/**
	 * previous total sent bytes
	 */
	static long long last_total_sent_bytes;

	/**
	 * last system time in millis
	 */
	static long long last_time_millis;

	/**
	 * has the method for getting network received and sent data be invoked before
	 */
	static bool yetGetted;
public:
	/**
	 * get network received and sent bytes per second of all interfaces, ignoring loopback interface
	 * the first call to this method will return invalid value
	 */
	static void getNetSendRecvBytesPerSec(double *bytesRecvPerSec, double *bytesSentPerSec);
};

#endif /* NETINTFSENDRECV_H_ */
