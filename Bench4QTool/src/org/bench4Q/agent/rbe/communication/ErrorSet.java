/**
 * =========================================================================
 * 					Bench4Q version 1.2.1
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
 *  * Initial developer(s): Zhiquan Duan.
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * 
 */
package org.bench4Q.agent.rbe.communication;

import java.util.ArrayList;

/**
 * @author duanzhiquan
 * 
 */
public class ErrorSet implements Sendable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6403614138394936079L;
	private ArrayList<EBError> error;

	/**
	 * 
	 */
	public ErrorSet() {
		super();
		error = new ArrayList<EBError>(60);
	}

	/**
	 * @param initLenth
	 */
	public ErrorSet(int initLenth) {
		super();
		error = new ArrayList<EBError>(initLenth);
	}

	/**
	 * @param value
	 */
	public void add(EBError value) {
		error.add(value);
	}

	/**
	 * @return
	 */
	public ArrayList<EBError> getResult() {
		return error;
	}

	/**
	 * @param error
	 */
	public void setResult(ArrayList<EBError> error) {
		this.error = error;
	}

}
