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

#if !defined(___GetMonData_h____)
#define ___GetMonData_h____

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include <Pdh.h>

#include "ServerMonData.h"

// get the server monitoring data
// this method is dll exported for testing, not for JNI
ServerMonData _declspec(dllexport) QueryServerMonData();









#endif
