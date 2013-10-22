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
 * CPUUsages.cpp
 *
 *  Created on: Jul 16, 2010
 *      Author: xiaowei zhou
 */

#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>

#include "CPUUsages.h"

long CPUUsages::cp_time[NCPUSTATES];
long CPUUsages::cp_old[NCPUSTATES];
char CPUUsages::buffer[4096 + 1];
long CPUUsages::cp_diff[NCPUSTATES] = { 0, 0, 0, 0, 0 };



inline char *CPUUsages::skip_token(const char *p) {
	while (isspace(*p))
		p++;
	while (*p && !isspace(*p))
		p++;
	return (char *) p;
}

/**
 * For comments, see CPUUsages.h
 */
double CPUUsages::getCpuUsages() {
	int fd = open("/proc/stat", O_RDONLY);
	int len = read(fd, buffer, sizeof(buffer) - 1);
	close( fd);
	buffer[len] = '\0';

	// cpu used, nice, sys, idle, iowait usage percentage multiplied by 10.0
	int cpu_states[NCPUSTATES];

	char *p = skip_token(buffer); // skip the word "cpu" at the beginning of the first line
	cp_time[0] = strtoul(p, &p, 0); // get cpu used time
	cp_time[1] = strtoul(p, &p, 0); // get cpu nice time
	cp_time[2] = strtoul(p, &p, 0); // get cpu sys time
	cp_time[3] = strtoul(p, &p, 0); // get cpu idle time
	cp_time[4] = strtoul(p, &p, 0); // get cpu iowait time

	computeDiff(NCPUSTATES, cpu_states, cp_time, cp_old, cp_diff);

//	printf("cpu used:%4.1f nice:%4.1f sys:%4.1f idle:%4.1f iowait:%4.1f\n",
//			cpu_states[0] / 10.0, cpu_states[1] / 10.0, cpu_states[2] / 10.0,
//			cpu_states[3] / 10.0, cpu_states[4] / 10.0);

	// cpu usage = user(cpu_states[0]) + nice(cpu_states[1]) + sys(cpu_states[2])
	return (cpu_states[0] +cpu_states[1]+cpu_states[2])/10.0;
}

/**
 * For comments, see CPUUsages.h
 */
long CPUUsages::computeDiff(int cnt, int *out, register long *newvar,
		register long *old, long *diffs) {
	register int i;
	register long change;
	register long total_change;
	register long *dp;

	/* initialization */
	total_change = 0;
	dp = diffs;

	/* calculate changes for each state and the overall change */
	for (i = 0; i < cnt; i++) {
		if ((change = *newvar - *old) < 0) {
			/* this only happens when the counter wraps */
			change = (int) ((unsigned long) *newvar - (unsigned long) *old);
		}
		total_change += (*dp++ = change);
		*old++ = *newvar++;
	}

	/* avoid divide by zero potential */
	if (total_change == 0) {
		total_change = 1;
	}

	/* calculate percentages based on overall change, rounding up */
	for (i = 0; i < cnt; i++) {
		//printf("dd %ld %ld\n",(*diffs* 1000 + half_total),total_change);
		*out++ = (int) ((*diffs++ * 1000) / total_change);
	}

	/* return the total in case the caller wants to use it */
	return (total_change);
}



