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
#include "stdafx.h"
#include "org_bench4Q_servermonitor_ServerDataManager.h"
#include "ServerMonitorMFC.h"

extern CServerMonitorMFCApp theApp;

ServerDataJni serverDataJni;

// initialize JNI related variables
void ServerDataJni::init( JNIEnv * env )
{
	jclass classLocalRef = env->FindClass("org/bench4Q/servermonitor/ServerData");
	clazz = (jclass)env->NewGlobalRef(classLocalRef);
	clazzConstructor = env->GetMethodID(clazz, "<init>","()V");
	
	setCpuPercent = env->GetMethodID(clazz,"setCpuPercent","(D)V");
	setMemAvailMB = env->GetMethodID(clazz,"setMemAvailMB","(D)V");
	setDiskReadBytesPerSecond = env->GetMethodID(clazz,"setDiskReadBytesPerSecond","(D)V");
	setDiskWriteBytesPerSecond= env->GetMethodID(clazz,"setDiskWriteBytesPerSecond","(D)V");
	setNetworkRecvBytesPerSecond= env->GetMethodID(clazz,"setNetworkRecvBytesPerSecond","(D)V");
	setNetworkSentBytesPerSecond= env->GetMethodID(clazz,"setNetworkSentBytesPerSecond","(D)V");
	setCurTimeMillis = env->GetMethodID(clazz,"setCurTimeMillis","(J)V");

	inited=true;
}

// get server monitoring data from another object and generate java object
jobject ServerDataJni::getServerDataObject(JNIEnv* env)
{
	if(!inited) {
		init(env);
	}

	jobject serverData=env->NewObject(clazz, clazzConstructor);

	ServerMonData s = theApp.QueryServerData();
	
	env->CallVoidMethod(serverData, setCpuPercent, s.cpuPercent);
	env->CallVoidMethod(serverData, setCurTimeMillis, s.tickCountMillis);
	env->CallVoidMethod(serverData, setMemAvailMB, s.memAvailMB);
	env->CallVoidMethod(serverData, setDiskReadBytesPerSecond, s.diskReadBytesPerSecond);
	env->CallVoidMethod(serverData, setDiskWriteBytesPerSecond, s.diskWriteBytesPerSecond);
	env->CallVoidMethod(serverData, setNetworkRecvBytesPerSecond, s.networkRecvBytesPerSecond);
	env->CallVoidMethod(serverData, setNetworkSentBytesPerSecond, s.networkSentBytesPerSecond);

	return serverData;
}

ServerDataJni::ServerDataJni()
{
	inited = false;
}

// this function is exported to JNI
JNIEXPORT jobject JNICALL Java_org_bench4Q_servermonitor_ServerDataManager_getServerData
(JNIEnv *env, jclass)
{
	return serverDataJni.getServerDataObject(env);
}
