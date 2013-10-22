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

// ServerMonitorMFC.h : main header file for the SERVERMONITORMFC DLL
//

#if !defined(AFX_SERVERMONITORMFC_H__C0B78ADF_5C7E_4D4F_9650_FE22CFE26D0A__INCLUDED_)
#define AFX_SERVERMONITORMFC_H__C0B78ADF_5C7E_4D4F_9650_FE22CFE26D0A__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

#include <pdh.h>

#include "ServerMonData.h"

#include "org_bench4Q_servermonitor_ServerDataManager.h"

/////////////////////////////////////////////////////////////////////////////
// CServerMonitorMFCApp
// See ServerMonitorMFC.cpp for the implementation of this class
//

class CServerMonitorMFCApp : public CWinApp
{
public:
	ServerMonData QueryServerData();
	CServerMonitorMFCApp();
	~CServerMonitorMFCApp();

	HQUERY getMemberHQuery() {return hQuery;}



// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CServerMonitorMFCApp)
	public:
	virtual BOOL InitInstance();
	virtual int ExitInstance();
	//}}AFX_VIRTUAL

	//{{AFX_MSG(CServerMonitorMFCApp)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()

private:

	HQUERY hQuery;

	HCOUNTER hCounterCPU;
	HCOUNTER hCounterMem;
	HCOUNTER hCounterDiskRead;
	HCOUNTER hCounterDiskWrite;
	HCOUNTER *hCounterNetRecvArray;
	HCOUNTER *hCounterNetSentArray;

	// the array size of both network related hcounters
	int netHCounterArraySize;

	bool inited;

    static PDH_COUNTER_PATH_ELEMENTS cpeCPU;
	static PDH_COUNTER_PATH_ELEMENTS cpeMem;
	static PDH_COUNTER_PATH_ELEMENTS cpeDiskRead;
	static PDH_COUNTER_PATH_ELEMENTS cpeDiskWrite;
	static PDH_COUNTER_PATH_ELEMENTS cpeNetRecv;
	static PDH_COUNTER_PATH_ELEMENTS cpeNetSent;

};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_SERVERMONITORMFC_H__C0B78ADF_5C7E_4D4F_9650_FE22CFE26D0A__INCLUDED_)
