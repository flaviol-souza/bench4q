/**
 * =========================================================================
 * 					Bench4Q_Script version 1.3.1
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at  
 * http://www.trustie.net/projects/project/show/Bench4Q
 * You can find latest version there. 
 * Bench4Q_Script adds a script module for Internet application to Bench4Q
 * http://www.trustie.com/projects/project/show/Bench4Q_Script
 * 
 * Distributed according to the GNU Lesser General Public Licence. 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by   
 * the Free Software Foundation; either version 2.1 of the License, or any
 * later version.
 * 
 * SEE Copyright.txt FOR FULL COPYRIGHT INFORMATION.
 * 
 * This source code is distributed "as is" in the hope that it will be
 * useful.  It comes with no warranty, and no author or distributor
 * accepts any responsibility for the consequences of its use.
 *
 *
 * This version is a based on the implementation of TPC-W from University of Wisconsin. 
 * This version used some source code of The Grinder.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 *  * Initial developer(s): Wangsa , Tianfei , WUYulong , Zhufeng
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package scriptbq;

/**
 * Class stands for some global parameters of the Bench4Q_Script
 */
public class BqConstant {
	/**
	 * The root path of the workspace 
	 */
	public static String BqRootPath = "C:/Bench4Q_Script";
	/**
	 * The path of tenant group
	 */
	public static String BqTreePath = BqRootPath+"/TenantGroup";
	public static String DEFAULTBASELOAD = "20";
	public static String DEFAULTSTDYTIME = "10";
	public static String DEFAULTBASEURL = "http://localhost:8080";
	public static String MAXQGENERATOR = "maxq.generator.IsacCodeGenerator";
	public static String RMISuffix = ":1099/RMIServer";
	public static String RMIPrefix = "rmi://";
	public static int MAXQPORT = 8090;
	public static boolean Save=false;
	
}
