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

// ServerMonitorMFC.cpp : Defines the initialization routines for the DLL.
//

#include "stdafx.h"
#include "ServerMonitorMFC.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

#pragma comment ( lib , "Pdh.lib" )

//
//	Note!
//
//		If this DLL is dynamically linked against the MFC
//		DLLs, any functions exported from this DLL which
//		call into MFC must have the AFX_MANAGE_STATE macro
//		added at the very beginning of the function.
//
//		For example:
//
//		extern "C" BOOL PASCAL EXPORT ExportedFunction()
//		{
//			AFX_MANAGE_STATE(AfxGetStaticModuleState());
//			// normal function body here
//		}
//
//		It is very important that this macro appear in each
//		function, prior to any calls into MFC.  This means that
//		it must appear as the first statement within the 
//		function, even before any object variable declarations
//		as their constructors may generate calls into the MFC
//		DLL.
//
//		Please see MFC Technical Notes 33 and 58 for additional
//		details.
//

/////////////////////////////////////////////////////////////////////////////
// CServerMonitorMFCApp

BEGIN_MESSAGE_MAP(CServerMonitorMFCApp, CWinApp)
	//{{AFX_MSG_MAP(CServerMonitorMFCApp)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CServerMonitorMFCApp construction

// initialize the pdh counter path elements
PDH_COUNTER_PATH_ELEMENTS CServerMonitorMFCApp::cpeCPU=
	{ _TEXT("."), _TEXT("Processor"), _TEXT("_Total"), NULL, -1, _TEXT("% Processor Time") };

PDH_COUNTER_PATH_ELEMENTS CServerMonitorMFCApp::cpeMem=
	{ _TEXT("."), _TEXT("Memory"), NULL, NULL, -1, _TEXT("Available MBytes") };

PDH_COUNTER_PATH_ELEMENTS CServerMonitorMFCApp::cpeDiskRead=
	{ _TEXT("."), _TEXT("LogicalDisk"), _TEXT("_Total"), NULL, -1, _TEXT("Disk Read Bytes/sec") };

PDH_COUNTER_PATH_ELEMENTS CServerMonitorMFCApp::cpeDiskWrite=
	{ _TEXT("."), _TEXT("LogicalDisk"), _TEXT("_Total"), NULL, -1, _TEXT("Disk Write Bytes/sec") };

PDH_COUNTER_PATH_ELEMENTS CServerMonitorMFCApp::cpeNetRecv=
	{ _TEXT("."), _TEXT("Network Interface"), NULL, NULL, -1, _TEXT("Bytes Received/sec") };

PDH_COUNTER_PATH_ELEMENTS CServerMonitorMFCApp::cpeNetSent=
	{ _TEXT("."), _TEXT("Network Interface"), NULL, NULL, -1, _TEXT("Bytes Sent/sec") };


CServerMonitorMFCApp::CServerMonitorMFCApp()
{

	hQuery = NULL;

	hCounterCPU = NULL;
	hCounterMem = NULL;
	hCounterDiskRead = NULL;
	hCounterDiskWrite = NULL;
	hCounterNetRecvArray = NULL;
	hCounterNetSentArray = NULL;

	netHCounterArraySize = 0;

	inited = false;

}

CServerMonitorMFCApp::~CServerMonitorMFCApp()
{

}

/////////////////////////////////////////////////////////////////////////////
// The one and only CServerMonitorMFCApp object
CServerMonitorMFCApp theApp;

// init the dll instance
// add some pdh counters
BOOL CServerMonitorMFCApp::InitInstance() 
{
	char szFullPath[MAX_PATH];
	DWORD cbPathSize;
	PDH_STATUS pdhStatus;

	// open pdh query
	if ((pdhStatus=PdhOpenQuery(NULL, 0, &hQuery)) == ERROR_SUCCESS)
    {
		// add the CPU counter
		cbPathSize = MAX_PATH;
		if ((pdhStatus=PdhMakeCounterPath(&cpeCPU, szFullPath, &cbPathSize, 0)) == ERROR_SUCCESS)
		{
			PdhAddCounter(hQuery, szFullPath, 0, &hCounterCPU);
		}

		// add the memory counter
		cbPathSize = MAX_PATH;
		if ((pdhStatus=PdhMakeCounterPath(&cpeMem, szFullPath, &cbPathSize, 0)) == ERROR_SUCCESS)
		{
			PdhAddCounter(hQuery, szFullPath, 0, &hCounterMem);
		}

		// add the disk read counter
		cbPathSize = MAX_PATH;
		if ((pdhStatus=PdhMakeCounterPath(&cpeDiskRead, szFullPath, &cbPathSize, 0)) == ERROR_SUCCESS)
		{
			PdhAddCounter(hQuery, szFullPath, 0, &hCounterDiskRead);
		}

		// add the disk write counter
		cbPathSize = MAX_PATH;
		if ((pdhStatus=PdhMakeCounterPath(&cpeDiskWrite, szFullPath, &cbPathSize, 0)) == ERROR_SUCCESS)
		{
			PdhAddCounter(hQuery, szFullPath, 0, &hCounterDiskWrite);
		}
    }
	


	// enumerate and add network counters
	
	DWORD dwNetIntfCounterListSize = 0;
	DWORD dwNetIntfInstanceListSize = 0;

	// obtain the length of the buffers for the enumeration
	pdhStatus = PdhEnumObjectItems(
		NULL,
		_TEXT("."),
		_TEXT("Network Interface"),
		NULL,
		&dwNetIntfCounterListSize,
		NULL,
		&dwNetIntfInstanceListSize,
		PERF_DETAIL_WIZARD,
		0);
	
	LPTSTR szNetIntfCounterListBuf = new TCHAR[dwNetIntfCounterListSize];
	LPTSTR szNetIntfInstanceListBuf = new TCHAR[dwNetIntfInstanceListSize];

	// enumerate the instances of network instances
	pdhStatus = PdhEnumObjectItems(
		NULL,
		_TEXT("."),
		_TEXT("Network Interface"),
		szNetIntfCounterListBuf,
		&dwNetIntfCounterListSize,
		szNetIntfInstanceListBuf,
		&dwNetIntfInstanceListSize,
		PERF_DETAIL_WIZARD,
		0);

	delete[]szNetIntfCounterListBuf;

	if(pdhStatus == ERROR_SUCCESS)
	{
		// determine the length of the network interface instance array
		int arrayLen = 0;
		DWORD i;
		if(dwNetIntfInstanceListSize != (DWORD)2)
		{
			for(i = dwNetIntfInstanceListSize - 2;i>=0;i--)
			{
				if(szNetIntfInstanceListBuf[i] == (TCHAR)'\0')
				{
					arrayLen++;
				}
				if(i==0)
				{
					break;
				}
			}
		} // if(dwInstanceListSize != (DWORD)2)

		// construct the network interface instance array
		LPTSTR *instanceArray = new LPTSTR[arrayLen];
		int arrayInd = 0;
		bool toInsert = true;
		for(i=0;i<dwNetIntfInstanceListSize;i++)
		{
			if(szNetIntfInstanceListBuf[i] != (TCHAR)'\0')
			{
				if(toInsert==true)
				{
					toInsert=false;
					instanceArray[arrayInd++]=&szNetIntfInstanceListBuf[i];
				}
			}
			else
			{
				toInsert=true;
			}
		} // for(i=0;i<dwInstanceListSize;i++)

		netHCounterArraySize = 0;
		hCounterNetRecvArray = new HCOUNTER[arrayLen];
		hCounterNetSentArray = new HCOUNTER[arrayLen];

		// add network counters
		for(i=0;i<arrayLen;i++) {
			if(_tcscmp(instanceArray[i], _TEXT("MS TCP Loopback interface")) != 0) { // exclude the loopback interface

				cpeNetRecv.szInstanceName=instanceArray[i];
				cpeNetSent.szInstanceName=instanceArray[i];

				cbPathSize = MAX_PATH;
				if ((pdhStatus=PdhMakeCounterPath(&cpeNetRecv, szFullPath, &cbPathSize, 0)) == ERROR_SUCCESS)
				{
					PdhAddCounter(hQuery, szFullPath, 0, &hCounterNetRecvArray[netHCounterArraySize]);
				}

				cbPathSize = MAX_PATH;
				if ((pdhStatus=PdhMakeCounterPath(&cpeNetSent, szFullPath, &cbPathSize, 0)) == ERROR_SUCCESS)
				{
					PdhAddCounter(hQuery, szFullPath, 0, &hCounterNetSentArray[netHCounterArraySize]);
				}

				netHCounterArraySize++;
			}
		}

		delete[]instanceArray;

	} // if(pdhStatus == ERROR_SUCCESS)

	delete[]szNetIntfInstanceListBuf;

	inited = true;
	
	return CWinApp::InitInstance();
}

// exit the dll instance, release some resources
int CServerMonitorMFCApp::ExitInstance() 
{
	int i;

	inited = false;

	if(hCounterCPU != NULL) {
		PdhRemoveCounter(hCounterCPU);
		hCounterCPU = NULL;
	}
	if(hCounterMem != NULL) {
		PdhRemoveCounter(hCounterMem);
		hCounterMem = NULL;
	}
	if(hCounterDiskRead != NULL) {
		PdhRemoveCounter(hCounterDiskRead);
		hCounterDiskRead = NULL;
	}
	if(hCounterDiskWrite != NULL) {
		PdhRemoveCounter(hCounterDiskWrite);
		hCounterDiskWrite = NULL;
	}

	if(hCounterNetRecvArray != NULL)
	{
		for(i=0;i<netHCounterArraySize;i++)
		{
			PdhRemoveCounter(hCounterNetRecvArray[i]);
		}

		delete[]hCounterNetRecvArray;
		hCounterNetRecvArray = NULL;
	}
	if(hCounterNetSentArray != NULL)
	{
		for(i=0;i<netHCounterArraySize;i++)
		{
			PdhRemoveCounter(hCounterNetSentArray[i]);
		}

		delete[]hCounterNetSentArray;
		hCounterNetSentArray = NULL;
	}
	netHCounterArraySize = 0;

	if(hQuery != NULL)
	{
		PdhCloseQuery(hQuery);
	}
	hQuery = NULL;
	
	return CWinApp::ExitInstance();
}

// query the monitoring data
ServerMonData CServerMonitorMFCApp::QueryServerData()
{
	PDH_STATUS pdhStatus;

	ServerMonData monData;
	
	monData.tickCountMillis = GetTickCount();

	// PdhCollectQueryData querys the monitoring data from system
	if (!inited || (pdhStatus = PdhCollectQueryData(hQuery)) != ERROR_SUCCESS)
	{
		// return abnormal data
		monData.cpuPercent = -1.0;
		monData.diskReadBytesPerSecond = -1.0;
		monData.diskWriteBytesPerSecond = -1.0;
		monData.memAvailMB = -1.0;
		monData.networkRecvBytesPerSecond = -1.0;
		monData.networkSentBytesPerSecond = -1.0;
	}
	else
	{
		PDH_FMT_COUNTERVALUE counterValue; // formatted counter value

		// get the formatted cpu count value
		if ((pdhStatus = PdhGetFormattedCounterValue(hCounterCPU, PDH_FMT_DOUBLE,
			NULL, &counterValue)) != ERROR_SUCCESS) {
			monData.cpuPercent = -1.0;
		} else {
			monData.cpuPercent = counterValue.doubleValue;
		}

		// get the formatted memory count value
		if ((pdhStatus = PdhGetFormattedCounterValue(hCounterMem, PDH_FMT_DOUBLE,
			NULL, &counterValue)) != ERROR_SUCCESS) {
			monData.memAvailMB = -1.0;
		} else {
			monData.memAvailMB = counterValue.doubleValue;
		}

		// get the formatted disk read count value
		if ((pdhStatus = PdhGetFormattedCounterValue(hCounterDiskRead, PDH_FMT_DOUBLE,
			NULL, &counterValue)) != ERROR_SUCCESS) {
			monData.diskReadBytesPerSecond = -1.0;
		} else {
			monData.diskReadBytesPerSecond = counterValue.doubleValue;
		}

		// get the formatted disk write count value
		if ((pdhStatus = PdhGetFormattedCounterValue(hCounterDiskWrite, PDH_FMT_DOUBLE,
			NULL, &counterValue)) != ERROR_SUCCESS) {
			monData.diskWriteBytesPerSecond = -1.0;
		} else {
			monData.diskWriteBytesPerSecond = counterValue.doubleValue;
		}

		int i;
		// get the formatted network received count value
		monData.networkRecvBytesPerSecond = -1.0;
		if(netHCounterArraySize > 0) {
			monData.networkRecvBytesPerSecond = 0.0;
			for(i=0;i<netHCounterArraySize;i++) {
				if ((pdhStatus = PdhGetFormattedCounterValue(hCounterNetRecvArray[i], PDH_FMT_DOUBLE,
					NULL, &counterValue)) != ERROR_SUCCESS) {
					monData.networkRecvBytesPerSecond = -1.0;
					break;
				} else {
					monData.networkRecvBytesPerSecond += counterValue.doubleValue;
				}
			}
		}

		// get the formatted network sent count value
		monData.networkSentBytesPerSecond = -1.0;
		if(netHCounterArraySize > 0) {
			monData.networkSentBytesPerSecond = 0.0;
			for(i=0;i<netHCounterArraySize;i++) {
				if ((pdhStatus = PdhGetFormattedCounterValue(hCounterNetSentArray[i], PDH_FMT_DOUBLE,
					NULL, &counterValue)) != ERROR_SUCCESS) {
					monData.networkSentBytesPerSecond = -1.0;
					break;
				} else {
					monData.networkSentBytesPerSecond += counterValue.doubleValue;
				}
			}
		}

	} // if ((pdhStatus = PdhCollectQueryData(hQuery)) != ERROR_SUCCESS)
	
	return monData;
}
