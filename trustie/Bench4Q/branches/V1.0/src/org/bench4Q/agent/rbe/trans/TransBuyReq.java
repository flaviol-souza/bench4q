package org.bench4Q.agent.rbe.trans;

import org.bench4Q.agent.rbe.EB;
import org.bench4Q.agent.rbe.RBE;
import org.bench4Q.agent.rbe.communication.EBStats;
import org.bench4Q.agent.rbe.util.URLUtil;

public class TransBuyReq extends Transition {

	public String request(EB eb, String html) {

		String url = EB.buyReqURL;

		if (eb.cid != EB.ID_UNKNOWN) {
			url = url + "?" + URLUtil.field_retflag + "=Y&" + URLUtil.unameAndPass(eb.cid);
		} else {
			url = url + "?" + URLUtil.field_retflag + "=N";
			eb.fname = URLUtil.astring(eb.rand, 8, 15);
			eb.lname = URLUtil.astring(eb.rand, 8, 15);
			url = url + "&" + URLUtil.field_fname + "=" + eb.fname;
			url = url + "&" + URLUtil.field_lname + "=" + eb.lname;
			url = url + "&" + URLUtil.field_street1 + "=" + URLUtil.astring(eb.rand, 15, 40);
			url = url + "&" + URLUtil.field_street2 + "=" + URLUtil.astring(eb.rand, 15, 40);
			url = url + "&" + URLUtil.field_city + "=" + URLUtil.astring(eb.rand, 10, 30);
			url = url + "&" + URLUtil.field_state + "=" + URLUtil.astring(eb.rand, 2, 20);
			url = url + "&" + URLUtil.field_zip + "=" + URLUtil.astring(eb.rand, 5, 10);
			url = url + "&" + URLUtil.field_country + "=" + URLUtil.unifCountry(eb.rand);
			url = url + "&" + URLUtil.field_phone + "=" + URLUtil.nstring(eb.rand, 9, 16);
			// For e-mail, see TPC-W spec clause 4.2.6.13
			// However, user name is not known, because CID is not known.
			// Useing random a-string [8, 15] instead.
			// %40 == @ sign
			url = url + "&" + URLUtil.field_email + "=" + URLUtil.astring(eb.rand, 8, 15) + "%40"
					+ URLUtil.astring(eb.rand, 2, 9) + ".com";
			// This is a little ambiguous in the TPC-W Spec.
			// This definition is taken from Clause 4.7.1, C_BIRTHDATE
			url = url + "&" + URLUtil.field_birthdate + "=" + URLUtil.unifDOB(eb.rand);
			url = url + "&" + URLUtil.field_data + "=" + URLUtil.astring(eb.rand, 100, 500);

		}

		return (eb.addIDs(url));
	}

	/* Find C_ID and SHOPPING_ID, if not already known. */
	public void postProcess(EB eb, String html) {
		if (eb.cid == EB.ID_UNKNOWN) {
			eb.cid = eb.findID(html, URLUtil.yourCID);
			if (eb.cid == EB.ID_UNKNOWN) {
				EBStats.getEBStats().error(5, "Unable to find C_ID in buy request page page.", "<???>");
			}
		}
	}
}
