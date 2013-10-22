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

// TestServerMonitor.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "TestServerMonitor.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// The one and only application object

#include "ServerMonData.h"

#pragma comment(lib,"ServerMonitorMFC.lib")

ServerMonData __declspec(dllimport) QueryServerMonData();

CWinApp theApp;

using namespace std;

int _tmain(int argc, TCHAR* argv[], TCHAR* envp[])
{
	int nRetCode = 0;

	// initialize MFC and print and error on failure
	if (!AfxWinInit(::GetModuleHandle(NULL), NULL, ::GetCommandLine(), 0))
	{
		cerr << _T("Fatal Error: MFC initialization failed") << endl;
		nRetCode = 1;
	}
	else
	{
		while(true) {
			ServerMonData monDat = QueryServerMonData();
			cout<<"CPU Usage: "<<monDat.cpuPercent<<endl;
			cout<<"Available Memory in MBytes: "<<monDat.memAvailMB<<endl;
			cout<<"Disk read per second in bytes: "<<monDat.diskReadBytesPerSecond<<endl;
			cout<<"Disk write per second in bytes: "<<monDat.diskWriteBytesPerSecond<<endl;
			cout<<"Network receive per second in bytes: "<<monDat.networkRecvBytesPerSecond<<endl;
			cout<<"Network send per second in bytes: "<<monDat.networkSentBytesPerSecond<<endl;
			cout<<"Tick count millis: "<<monDat.tickCountMillis<<endl;
			cout<<endl;
			Sleep(1000);
		}
	}

	return nRetCode;
}


