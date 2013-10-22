/*------------------------------------------------------------------------
 * rbe.EBWCustRegTrans.java
 * Timothy Heil
 * 10/13/99
 *
 * ECE902 Fall '99
 *
 * TPC-W customer registeration transition to the customer registration 
 *  page from the shopping cart page.
 *------------------------------------------------------------------------*/

package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;

public class TransCustReg extends Transition {

	public String request(EB eb, String html) {
		return (eb.addIDs(EB.custRegURL));
	}
}
