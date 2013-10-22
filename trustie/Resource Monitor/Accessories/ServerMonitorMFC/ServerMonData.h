/**
 * =========================================================================
 * 			Bench4Q Server Monitor for Windows
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

// ServerMonData.h: interface for the ServerMonData class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_SERVERMONDATA_H__C0616973_05A1_4415_BBE5_514C8D8493F3__INCLUDED_)
#define AFX_SERVERMONDATA_H__C0616973_05A1_4415_BBE5_514C8D8493F3__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

// the data structure of server monitoring data
class _declspec(dllexport) ServerMonData
{
public:
	ServerMonData();
	virtual ~ServerMonData();

	double cpuPercent;
	double memAvailMB;
	double diskReadBytesPerSecond;
	double diskWriteBytesPerSecond;
	double networkRecvBytesPerSecond;
	double networkSentBytesPerSecond;
	DWORD tickCountMillis;


};

#endif // !defined(AFX_SERVERMONDATA_H__C0616973_05A1_4415_BBE5_514C8D8493F3__INCLUDED_)
