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
package org.bench4Q.agent.rbe.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.text.html.MinimalHTMLWriter;

/**
 * @author duanzhiquan
 * 
 */
public class URLUtil {

	/**
	 * bug fixed. Bench4Q version 1.2.1
	 * 
	 * public static int numItem = 10000; // Number of items for sale.
	 */
	public static int numItem = 1000;

	/**
	 * Used for search strings. See TPC-W Spec. 2.10.5.1
	 */
	public static int numItemA = 511;

	/**
	 * Used for generating random CIDs. See TPC-W spec Clause 2.3.2
	 */
	public static int cidA = 1023;

	/**
	 * Number of initial customers
	 */
	public static int numCustomer = 1000;

	/**
	 * 
	 */
	public static final StrStrPattern yourCID = new StrStrPattern("C_ID=");
	/**
	 * 
	 */
	public static final StrStrPattern yourShopID = new StrStrPattern(
			"SHOPPING_ID=");
	/**
	 * 
	 */
	public static final StrStrPattern yourSessionID = new StrStrPattern(
			";jsessionid=");
	/**
	 * 
	 */
	public static final StrStrPattern endSessionID = new StrStrPattern("?");

	/**
	 * 
	 */
	public static final String field_cid = "C_ID";
	/**
	 * 
	 */
	public static final String field_sessionID = ";jsessionid=";
	/**
	 * 
	 */
	public static final String field_shopID = "SHOPPING_ID";
	/**
	 * 
	 */
	public static final String field_uname = "UNAME";
	/**
	 * 
	 */
	public static final String field_passwd = "PASSWD";
	/**
	 * 
	 */
	public static final String field_srchType = "search_type";
	/**
	 * 
	 */
	public static final String authorType = "author";
	/**
	 * 
	 */
	public static final String subjectType = "subject";
	/**
	 * 
	 */
	public static final String titleType = "title";
	/**
	 * 
	 */
	public static final String field_srchStr = "search_string";
	/**
	 * 
	 */
	public static final String field_addflag = "ADD_FLAG";
	/**
	 * 
	 */
	public static final String field_iid = "I_ID";
	/**
	 * 
	 */
	public static final String field_qty = "qty";
	/**
	 * 
	 */
	public static final String field_subject = "subject";
	/**
	 * 
	 */
	public static final String field_retflag = "RETURNING_FLAG";
	/**
	 * 
	 */
	public static final String field_fname = "FNAME";
	/**
	 * 
	 */
	public static final String field_lname = "LNAME";
	/**
	 * 
	 */
	public static final String field_street1 = "STREET_1";
	/**
	 * 
	 */
	public static final String field_street2 = "STREET_2";
	/**
	 * 
	 */
	public static final String field_city = "CITY";
	/**
	 * 
	 */
	public static final String field_state = "STATE";
	/**
	 * 
	 */
	public static final String field_zip = "ZIP";
	/**
	 * 
	 */
	public static final String field_country = "COUNTRY";
	/**
	 * 
	 */
	public static final String field_phone = "PHONE";
	/**
	 * 
	 */
	public static final String field_email = "EMAIL";
	/**
	 * 
	 */
	public static final String field_birthdate = "BIRTHDATE";
	/**
	 * 
	 */
	public static final String field_data = "DATA";
	/**
	 * 
	 */
	public static final String field_cctype = "CC_TYPE";
	/**
	 * 
	 */
	public static final String field_ccnumber = "CC_NUMBER";
	/**
	 * 
	 */
	public static final String field_ccname = "CC_NAME";
	/**
	 * 
	 */
	public static final String field_ccexp = "CC_EXPIRY";
	/**
	 * 
	 */
	public static final String field_shipping = "SHIPPING";
	/**
	 * 
	 */
	public static final String field_newimage = "I_NEW_IMAGE";
	/**
	 * 
	 */
	public static final String field_newthumb = "I_NEW_THUMBNAIL";
	/**
	 * 
	 */
	public static final String field_newcost = "I_NEW_COST";

	/**
	 * 
	 */
	public static final String[] nchars = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9" };

	/**
	 * 
	 */
	public static final String[] achars = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
			"k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w",
			"x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
			"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
			"X", "Y", "Z",

			"%21", // "!"
			"%40", // "@"
			"%23", // "#"
			"%24", // "$"
			"%25", // "%"
			"%5E", // "^"
			"%26", // "&"
			"%2A", // "*"
			"%28", // "("
			"%29", // ")"
			// "_", // "_"
			"%2D", // "-"
			"%3D", // "="
			"%2B", // "+"
			"%7B", // "{"
			"%7D", // "}"
			"%5B", // "["
			"%5D", // "]"
			"%7C", // "|"
			"%3A", // ":"
			"%3B", // ";"
			"%2C", // ","
			".", // "."
			"%3F", // "?"
			// "%2F", // "/"
			"%7E", // "~"
			"+", // " "
			"%2A" };

	/**
	 * Subject list. See TPC-W Spec. 4.6.2.11
	 */
	public static final String[] subjects = { "ARTS", "BIOGRAPHIES",
			"BUSINESS", "CHILDREN", "COMPUTERS", "COOKING", "HEALTH",
			"HISTORY", "HOME", "HUMOR", "LITERATURE", "MYSTERY", "NON-FICTION",
			"PARENTING", "POLITICS", "REFERENCE", "RELIGION", "ROMANCE",
			"SELF-HELP", "SCIENCE-NATURE", "SCIENCE-FICTION", "SPORTS",
			"YOUTH", "TRAVEL" };

	/**
	 * @param rand
	 * @param min
	 * @param max
	 * @return
	 */
	public static String astring(Random rand, int min, int max) {
		return (rstring(rand, min, max, achars));
	}

	/**
	 * @param rand
	 * @param min
	 * @param max
	 * @return
	 */
	public static String nstring(Random rand, int min, int max) {
		return (rstring(rand, min, max, nchars));
	}

	private static String rstring(Random rand, int min, int max, String[] cset) {
		int r = rand.nextInt(max - min + 1) + min;
		String s;

		for (s = ""; s.length() < r; s = s + cset[rand.nextInt(1)])
			;
		return s;
	}

	/**
	 * 
	 */
	public static final String[] countries = { "United+States",
			"United+Kingdom", "Canada", "Germany", "France", "Japan",
			"Netherlands", "Italy", "Switzerland", "Australia", "Algeria",
			"Argentina", "Armenia", "Austria", "Azerbaijan", "Bahamas",
			"Bahrain", "Bangla+Desh", "Barbados", "Belarus", "Belgium",
			"Bermuda", "Bolivia", "Botswana", "Brazil", "Bulgaria",
			"Cayman+Islands", "Chad", "Chile", "China", "Christmas+Island",
			"Colombia", "Croatia", "Cuba", "Cyprus", "Czech+Republic",
			"Denmark", "Dominican+Republic", "Eastern+Caribbean", "Ecuador",
			"Egypt", "El+Salvador", "Estonia", "Ethiopia", "Falkland+Island",
			"Faroe+Island", "Fiji", "Finland", "Gabon", "Gibraltar", "Greece",
			"Guam", "Hong+Kong", "Hungary", "Iceland", "India", "Indonesia",
			"Iran", "Iraq", "Ireland", "Israel", "Jamaica", "Jordan",
			"Kazakhstan", "Kuwait", "Lebanon", "Luxembourg", "Malaysia",
			"Mexico", "Mauritius", "New+Zealand", "Norway", "Pakistan",
			"Philippines", "Poland", "Portugal", "Romania", "Russia",
			"Saudi+Arabia", "Singapore", "Slovakia", "South+Africa",
			"South+Korea", "Spain", "Sudan", "Sweden", "Taiwan", "Thailand",
			"Trinidad", "Turkey", "Venezuela", "Zambia", };

	/**
	 * @param rand
	 * @return
	 */
	public static String unifCountry(Random rand) {
		return (countries[rand.nextInt(countries.length)]);
	}

	/**
	 * 
	 */
	public static final Calendar c = new GregorianCalendar(1880, 1, 1);
	/**
	 * 
	 */
	public static final long dobStart = c.getTime().getTime();;
	/**
	 * 
	 */
	public static final long dobEnd = System.currentTimeMillis();;

	/**
	 * @param rand
	 * @return
	 */
	public static String unifDOB(Random rand) {
		long t = ((long) (rand.nextDouble() * (dobEnd - dobStart))) + dobStart;
		Date d = new Date(t);
		Calendar c = new GregorianCalendar();
		c.setTime(d);

		return ("" + c.get(Calendar.DAY_OF_MONTH) + "%2f"
				+ c.get(Calendar.DAY_OF_WEEK) + "%2f" + c.get(Calendar.YEAR));
	}

	/**
	 * 
	 */
	public static final String[] ccTypes = { "VISA", "MASTERCARD", "DISCOVER",
			"DINERS", "AMEX" };

	/**
	 * @param rand
	 * @return
	 */
	public static String unifCCType(Random rand) {
		return (ccTypes[rand.nextInt(ccTypes.length)]);
	}

	/**
	 * @param rand
	 * @return
	 */
	public static String unifExpDate(Random rand) {
		Date d = new Date(System.currentTimeMillis()
				+ ((long) (rand.nextInt(730)) + 1) * 24L * 60L * 60L * 1000L);
		Calendar c = new GregorianCalendar();
		c.setTime(d);

		return ("" + c.get(Calendar.DAY_OF_MONTH) + "%2f"
				+ c.get(Calendar.DAY_OF_WEEK) + "%2f" + c.get(Calendar.YEAR));

	}

	/**
	 * @param rand
	 * @return
	 */
	public static int unifDollars(Random rand) {
		return rand.nextInt(9999) + 1;
	}

	/**
	 * @param rand
	 * @return
	 */
	public static int unifCents(Random rand) {
		return rand.nextInt(100);
	}

	/**
	 * 
	 */
	public static final String[] shipTypes = { "AIR", "UPS", "FEDEX", "SHIP",
			"COURIER", "MAIL" };

	/**
	 * @param rand
	 * @return
	 */
	public static String unifShipping(Random rand) {
		int i = rand.nextInt(shipTypes.length);
		return (shipTypes[i]);
	}

	/**
	 * @param i
	 * @param f
	 * @param v
	 * @return
	 */
	public static String addSession(String i, String f, String v) {
		StringTokenizer tok = new StringTokenizer(i, "?");
		String return_val = null;
		try {
			return_val = tok.nextToken();
			return_val = return_val + f + v;
			return_val = return_val + "?" + tok.nextToken();
		} catch (NoSuchElementException e) {
		}

		return (return_val);
	}

	/**
	 * Adds a field to a HTTP request.
	 * 
	 * @param i
	 * @param f
	 *            field name
	 * @param v
	 *            value
	 * @return
	 */
	public static String addField(String i, String f, String v) {
		if (i.indexOf((int) '?') == -1) {
			// First field
			i = i + '?';
		} else {
			// Another additional field.
			i = i + '&';
		}
		i = i + f + "=" + v;

		return (i);
	}

	/**
	 * Defined in TPC-W Spec Clause 2.1.13 the term non-uniform random function
	 * (NURand) is used in this specification to refer to the function used for
	 * generating C_ID and the targets for the Search Request web interactions.
	 * This function generates an independently selected and non-uniformly
	 * distributed random number over the specified range of values [x .. y],
	 * and is specified as follows: NURand(A, x, y) = ((random(0, A) | random(x,
	 * y)) % (y - x + 1)) + x Where: expr1 | expr2 stands for the bitwise
	 * logical OR operation between expr1 and expr2 expr1 % expr2 stands for
	 * expr1 modulo expr2 random(x, y) stands for randomly selected within [x ..
	 * y]
	 * 
	 * @param rand
	 * @param A
	 *            is a constant chosen according to the size of the range [x ..
	 *            y] called by eb.cid = URLUtil.NURand(eb.rand, URLUtil.cidA,
	 *            1,URLUtil.numCustomer) in trans;
	 * @param x
	 * @param y
	 * @return
	 */
	public static final int NURand(Random rand, int A, int x, int y) {
		return ((((rand.nextInt(A + 1)) | (rand.nextInt(y - x + 1) + x)) % (y
				- x + 1)) + x);
	}

	/**
	 * @param html
	 * @param tag
	 * @param etag
	 * @return
	 */
	public static String findSessionID(String html, StrStrPattern tag,
			StrStrPattern etag) {
		// Find the tag string.
		int i = tag.find(html);
		if (i == -1)
			return null;
		i = i + tag.length();
		// Find end of the digits.
		StrStrPattern yin = new StrStrPattern("\"");
		int j = yin.find(html, i);
		
		if (j == -1)
			return null;
		String mid = html.substring(i,j);
		if(mid.contains("?")){
			int k = etag.find(mid);
			mid = mid.substring(0,k);
		}
		return mid;
	}

	// Defined in TPC-W Spec Clause 4.6.2.8
	private static final String[] digS = { "BA", "OG", "AL", "RI", "RE", "SE",
			"AT", "UL", "IN", "NG" };

	/**
	 * @param d
	 * @param n
	 * @return
	 */
	public static String digSyl(int d, int n) {
		String s = "";

		if (n == 0)
			return (digSyl(d));

		for (; n > 0; n--) {
			int c = d % 10;
			s = digS[c] + s;
			d = d / 10;
		}

		return (s);
	}

	/**
	 * @param d
	 * @return
	 */
	public static String digSyl(int d) {
		String s = "";

		for (; d != 0; d = d / 10) {
			int c = d % 10;
			s = digS[c] + s;
		}

		return (s);
	}

	/**
	 * Gets the username and password fields according to TPC-W Spec. 4.6.2.9
	 * ff.
	 * 
	 * @param cid
	 * @return
	 */
	public static String unameAndPass(int cid) {
		String un = digSyl(cid);
		return (field_uname + "=" + un + "&" + field_passwd + "=" + un
				.toLowerCase());
	}

	/**
	 * Select a subject string randomly and uniformly from above list. See TPC-W
	 * Spec. 2.10.5.1
	 * 
	 * @param rand
	 * @return
	 */
	public static String unifSubject(Random rand) {
		return (subjects[rand.nextInt(subjects.length)]);
	}

	/**
	 * Select a subject string randomly and uniformly from above list. See TPC-W
	 * Spec. 2.10.5.1 NOTE: The "YOUTH" and "TRAVEL" subjects are missing from
	 * the home page. I believe this to be an error, but cannot be sure. Change
	 * this function if this is determined to not be an error.
	 * 
	 * @param rand
	 * @return
	 */
	public static String unifHomeSubject(Random rand) {
		return (unifSubject(rand));
	}

	/**
	 *Finds any non-URLable characters and converts them to URL form. All alpha
	 * numerics are left alone.<space> goes to '+' All others go to %xx
	 * (hexadecimal representation.)
	 * 
	 * @param url
	 * @return
	 */
	public static String mungeURL(String url) {
		int i;
		String mURL = "";
		for (i = 0; i < url.length(); i++) {
			char ch = url.charAt(i);
			if (((ch >= '0') && (ch <= '9')) || ((ch >= 'a') && (ch <= 'z'))
					|| ((ch >= 'A') && (ch <= 'Z'))
					|| ((ch == '.') || (ch == '/'))) {
				mURL = mURL + ch;
			} else if (ch == ' ') {
				mURL = mURL + '+';
			} else {
				int d = ch;
				int d1 = d >> 4;
				int d2 = d & 0xf;
				char c1 = (char) ((d1 > 9) ? ('A' + d1 - 10) : '0' + d1);
				char c2 = (char) ((d2 > 9) ? ('A' + d2 - 10) : '0' + d2);
				mURL = mURL + "%" + c1 + c2;
			}
		}
		return mURL;
	}

	/**
	 * @param rand
	 * @return
	 */
	public static String unifImage(Random rand) {
		int i = rand.nextInt(numItem) + 1;
		return (mungeURL("item_" + i + ".jpg"));
	}

	/**
	 * @param rand
	 * @return
	 */
	public static String unifThumbnail(Random rand) {
		int i = rand.nextInt(numItem) + 1;
		return (mungeURL("thumb_" + i + ".jpg"));
	}

}
