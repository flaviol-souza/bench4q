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
 * MemAvail.cpp
 *
 *  Created on: Jul 17, 2010
 *      Author: xiaowei zhou
 */

#include "MemAvail.h"
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>

char MemAvail::buffer[4096 + 1];

char *MemAvail::skip_token(const char *p) {
	while (isspace(*p))
		p++;
	while (*p && !isspace(*p))
		p++;
	return (char *) p;
}

char *MemAvail::skip_line(const char *p)
{
	while (*p && *p != '\n')
		p++;
	if(*p == '\n')
		p++;
	return (char *) p;
}

/**
 * For comments, see MemAvail.h
 */
double MemAvail::getAvailablePhysicalMemoryMB()
{
	int fd = open("/proc/meminfo", O_RDONLY);
	int len = read(fd, buffer, sizeof(buffer) - 1);
	close( fd);
	buffer[len] = '\0';

	unsigned long totalMemFreeKB = 0l;

	// get free memory
	char *p = skip_line(buffer);
	p = skip_token(p);
	totalMemFreeKB += strtoul(p, &p, 0);

	// add buffered memory
	p = skip_line(p);
	p = skip_token(p);
	totalMemFreeKB += strtoul(p, &p, 0);

	// add cached memory
	p = skip_line(p);
	p = skip_token(p);
	totalMemFreeKB += strtoul(p, &p, 0);

	return (double)totalMemFreeKB / 1024.0; // convert to MBytes
}



