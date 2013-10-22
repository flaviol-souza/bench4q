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
package org.bench4Q.agent.rbe;

import org.bench4Q.agent.rbe.trans.TransAdminConf;
import org.bench4Q.agent.rbe.trans.TransAdminReq;
import org.bench4Q.agent.rbe.trans.TransBestSell;
import org.bench4Q.agent.rbe.trans.TransBuyConf;
import org.bench4Q.agent.rbe.trans.TransBuyReq;
import org.bench4Q.agent.rbe.trans.TransCustReg;
import org.bench4Q.agent.rbe.trans.TransHome;
import org.bench4Q.agent.rbe.trans.TransInit;
import org.bench4Q.agent.rbe.trans.TransNewProd;
import org.bench4Q.agent.rbe.trans.TransOrderDisp;
import org.bench4Q.agent.rbe.trans.TransOrderInq;
import org.bench4Q.agent.rbe.trans.TransProdCURL;
import org.bench4Q.agent.rbe.trans.TransProdDet;
import org.bench4Q.agent.rbe.trans.TransSearchReq;
import org.bench4Q.agent.rbe.trans.TransSearchResult;
import org.bench4Q.agent.rbe.trans.TransShopCart;
import org.bench4Q.agent.rbe.trans.TransShopCartAdd;
import org.bench4Q.agent.rbe.trans.TransShopCartRef;
import org.bench4Q.agent.rbe.trans.Transition;

/**
 * @author duanzhiquan
 * 
 */
public class Mix {
	private static Transition[][] cTransArray;
	private static final Transition init = new TransInit();
	private static final Transition admc = new TransAdminConf();
	private static final Transition admr = new TransAdminReq();
	private static final Transition bess = new TransBestSell();
	private static final Transition buyc = new TransBuyConf();
	private static final Transition buyr = new TransBuyReq();
	private static final Transition creg = new TransCustReg();
	private static final Transition home = new TransHome();
	private static final Transition newp = new TransNewProd();
	private static final Transition ordd = new TransOrderDisp();
	private static final Transition ordi = new TransOrderInq();
	private static final Transition prod = new TransProdDet();
	private static final Transition proc = new TransProdCURL();
	private static final Transition sreq = new TransSearchReq();
	private static final Transition sres = new TransSearchResult();
	private static final Transition shop = new TransShopCart();
	private static final Transition shoa = new TransShopCartAdd();
	private static final Transition shor = new TransShopCartRef();

	private static final Transition[] trans = { init, admc, admr, bess, buyc,
			buyr, creg, home, newp, ordd, ordi, prod, sreq, sres, shop };

	private static final int[][] BrowsingTransProb = {
			// INIT ADMC ADMR BESS BUYC BUYR CREG HOME NEWP ORDD ORDI PROD SREQ
			// SRES
			// SHOP
			/* INIT */{ 0, 0, 0, 0, 0, 0, 0, 9999, 0, 0, 0, 0, 0, 0, 0 },
			/* ADMC */{ 0, 0, 0, 0, 0, 0, 0, 9877, 0, 0, 0, 0, 9999, 0, 0 },
			/* ADMR */{ 0, 8999, 0, 0, 0, 0, 0, 9999, 0, 0, 0, 0, 0, 0, 0 },
			/* BESS */{ 0, 0, 0, 0, 0, 0, 0, 4607, 0, 0, 0, 5259, 9942, 0, 9999 },
			/* BUYC */{ 0, 0, 0, 0, 0, 0, 0, 9999, 0, 0, 0, 0, 0, 0, 0 },
			/* BUYR */{ 0, 0, 0, 0, 9199, 0, 0, 9595, 0, 0, 0, 0, 0, 0, 9999 },
			/* CREG */{ 0, 0, 0, 0, 0, 9145, 0, 9619, 0, 0, 0, 0, 9999, 0, 0 },
			/* HOME */{ 0, 0, 0, 3792, 0, 0, 0, 0, 7585, 0, 7688, 0, 9559, 0,
					9999 },
			/* NEWP */{ 0, 0, 0, 0, 0, 0, 0, 299, 0, 0, 0, 9867, 9941, 0, 9999 },
			/* ORDD */{ 0, 0, 0, 0, 0, 0, 0, 802, 0, 0, 0, 0, 9999, 0, 0 },
			/* ORDI */{ 0, 0, 0, 0, 0, 0, 0, 523, 0, 8856, 0, 0, 9999, 0, 0 },
			/* PROD */{ 0, 0, 47, 0, 0, 0, 0, 8346, 0, 0, 0, 9749, 9890, 0,
					9999 },
			/* SREQ */{ 0, 0, 0, 0, 0, 0, 0, 788, 0, 0, 0, 0, 0, 9955, 9999 },
			/* SRES */{ 0, 0, 0, 0, 0, 0, 0, 3674, 0, 0, 0, 9868, 9942, 0, 9999 },
			/* SHOP */{ 0, 0, 0, 0, 0, 0, 4099, 8883, 0, 0, 0, 0, 0, 0, 9999 } };

	private static final int[][] ShoppingTransProb = {
			// INIT ADMC ADMR BESS BUYC BUYR CREG HOME NEWP ORDD ORDI PROD SREQ
			// SRES
			// SHOP
			/* INIT */{ 0, 0, 0, 0, 0, 0, 0, 9999, 0, 0, 0, 0, 0, 0, 0 },
			/* ADMC */{ 0, 0, 0, 0, 0, 0, 0, 9952, 0, 0, 0, 0, 9999, 0, 0 },
			/* ADMR */{ 0, 8999, 0, 0, 0, 0, 0, 9999, 0, 0, 0, 0, 0, 0, 0 },
			/* BESS */{ 0, 0, 0, 0, 0, 0, 0, 167, 0, 0, 0, 472, 9927, 0, 9999 },
			/* BUYC */{ 0, 0, 0, 0, 0, 0, 0, 84, 0, 0, 0, 0, 9999, 0, 0 },
			/* BUYR */{ 0, 0, 0, 0, 4614, 0, 0, 6546, 0, 0, 0, 0, 0, 0, 9999 },
			/* CREG */{ 0, 0, 0, 0, 0, 8666, 0, 8760, 0, 0, 0, 0, 9999, 0, 0 },
			/* HOME */{ 0, 0, 0, 3124, 0, 0, 0, 0, 6249, 0, 6718, 0, 7026, 0,
					9999 },
			/* NEWP */{ 0, 0, 0, 0, 0, 0, 0, 156, 0, 0, 0, 9735, 9784, 0, 9999 },
			/* ORDD */{ 0, 0, 0, 0, 0, 0, 0, 69, 0, 0, 0, 0, 9999, 0, 0 },
			/* ORDI */{ 0, 0, 0, 0, 0, 0, 0, 72, 0, 8872, 0, 0, 9999, 0, 0 },
			/* PROD */{ 0, 0, 58, 0, 0, 0, 0, 832, 0, 0, 0, 1288, 8603, 0, 9999 },
			/* SREQ */{ 0, 0, 0, 0, 0, 0, 0, 635, 0, 0, 0, 0, 0, 9135, 9999 },
			/* SRES */{ 0, 0, 0, 0, 0, 0, 0, 2657, 0, 0, 0, 9294, 9304, 0, 9999 },
			/* SHOP */{ 0, 0, 0, 0, 0, 0, 2585, 9552, 0, 0, 0, 0, 0, 0, 9999 } };

	private static final int[][] OrderingTransProb = {
			// INIT ADMC ADMR BESS BUYC BUYR CREG HOME NEWP ORDD ORDI PROD SREQ
			// SRES
			// SHOP
			/* INIT */{ 0, 0, 0, 0, 0, 0, 0, 9999, 0, 0, 0, 0, 0, 0, 0 },
			/* ADMC */{ 0, 0, 0, 0, 0, 0, 0, 8348, 0, 0, 0, 0, 9999, 0, 0 },
			/* ADMR */{ 0, 8999, 0, 0, 0, 0, 0, 9999, 0, 0, 0, 0, 0, 0, 0 },
			/* BESS */{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 333, 9998, 0, 9999 },
			/* BUYC */{ 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 9999, 0, 0 },
			/* BUYR */{ 0, 0, 0, 0, 7999, 0, 0, 9453, 0, 0, 0, 0, 0, 0, 9999 },
			/* CREG */{ 0, 0, 0, 0, 0, 9899, 0, 9901, 0, 0, 0, 0, 9999, 0, 0 },
			/* HOME */{ 0, 0, 0, 499, 0, 0, 0, 0, 999, 0, 1269, 0, 1295, 0,
					9999 },
			/* NEWP */{ 0, 0, 0, 0, 0, 0, 0, 504, 0, 0, 0, 9942, 9976, 0, 9999 },
			/* ORDD */{ 0, 0, 0, 0, 0, 0, 0, 9939, 0, 0, 0, 0, 9999, 0, 0 },
			/* ORDI */{ 0, 0, 0, 0, 0, 0, 0, 1168, 0, 9968, 0, 0, 9999, 0, 0 },
			/* PROD */{ 0, 0, 99, 0, 0, 0, 0, 3750, 0, 0, 0, 5621, 6341, 0,
					9999 },
			/* SREQ */{ 0, 0, 0, 0, 0, 0, 0, 815, 0, 0, 0, 0, 0, 9815, 9999 },
			/* SRES */{ 0, 0, 0, 0, 0, 0, 0, 486, 0, 0, 0, 7817, 9998, 0, 9999 },
			/* SHOP */{ 0, 0, 0, 0, 0, 0, 9499, 9918, 0, 0, 0, 0, 0, 0, 9999 } };

	private static final Transition[][] TransArray = {
			// INIT ADMC ADMR BESS BUYC BUYR CREG HOME NEWP ORDD ORDI PROD SREQ
			// SRES SHOP
			/* INIT */{ null, null, null, null, null, null, null, init, null,
					null, null, null, null, null, null },
			/* ADMC */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* ADMR */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* BESS */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* BUYC */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* BUYR */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* CREG */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* HOME */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* NEWP */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* ORDD */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* ORDI */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* PROD */{ null, null, null, null, null, null, null, null, null,
					null, null, proc, null, null, shoa },
			/* SREQ */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* SRES */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, null },
			/* SHOP */{ null, null, null, null, null, null, null, null, null,
					null, null, null, null, null, shor } };

	/**
	 * 
	 */
	public static void initialize() {
		cTransArray = new Transition[15][15];
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				cTransArray[i][j] = null;
			}
		}
	}

	/**
	 * @param string
	 * @return Transition metrix
	 */
	public Transition[][] getTrans(String string) {
		if (string.equalsIgnoreCase("browsing")) {
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					cTransArray[i][j] = TransArray[i][j];
					if ((BrowsingTransProb[i][j] > 0)
							&& (cTransArray[i][j] == null)) {
						cTransArray[i][j] = trans[j];
					}
				}
			}
		} else if (string.equalsIgnoreCase("shopping")) {
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					cTransArray[i][j] = TransArray[i][j];
					if ((ShoppingTransProb[i][j] > 0)
							&& (cTransArray[i][j] == null)) {
						cTransArray[i][j] = trans[j];
					}
				}
			}

		} else if (string.equalsIgnoreCase("ordering")) {
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					cTransArray[i][j] = TransArray[i][j];
					if ((OrderingTransProb[i][j] > 0)
							&& (cTransArray[i][j] == null)) {
						cTransArray[i][j] = trans[j];
					}
				}
			}

		} else {
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					cTransArray[i][j] = TransArray[i][j];
					if ((BrowsingTransProb[i][j] > 0)
							&& (cTransArray[i][j] == null)) {
						cTransArray[i][j] = trans[j];
					}
				}
			}

		}
		return cTransArray;
	}

	/**
	 * @param string
	 * @return Trans Prob metrix
	 */
	public int[][] getTransProb(String string) {
		if (string.equalsIgnoreCase("browsing")) {
			return BrowsingTransProb;
		} else if (string.equalsIgnoreCase("shopping")) {
			return ShoppingTransProb;
		} else if (string.equalsIgnoreCase("ordering")) {
			return OrderingTransProb;
		} else {
			return ShoppingTransProb;
		}
	}

}
