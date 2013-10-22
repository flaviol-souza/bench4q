/**
 * =========================================================================
 * 					Bench4Q version 1.0.0
 * =========================================================================
 * 
 * Bench4Q is available on the Internet at http://forge.ow2.org/projects/jaspte
 * You can find latest version there.
 * If you have any problem, you can  
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
package org.bench4Q.OrclPopulate;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Random;

class OrclPopulate {
	private static Connection con;

	private static Random rand;

	// These variables are dependent on the JDBC database driver used.
	//private static final String driverName = "com.ibm.db2.jcc.DB2Driver";
	private static final String driverName = "oracle.jdbc.driver.OracleDriver";

	private static String dbName;

	private static String username;

	private static String password;

	private static int NUM_EBS;

	private static int NUM_ITEMS;

	private static int NUM_CUSTOMERS;

	private static int NUM_ADDRESSES;

	private static int NUM_AUTHORS;

	private static int NUM_ORDERS;
	
	private static int state = 0;
	
	private static int concurrency;
	
	private static int Customer_block_size;
	
	private static int Customer_extra;
	
	private static int Address_block_size;
	
	private static int Address_extra;
	
	private static int batch_of_customer;
	
	private static int batch_of_address;

	public static void main(String[] args) {

		OrclPopulate populate = new OrclPopulate();
		populate.initiate();

		System.out.println("Beginning Bench4Q Database population.");
		System.out.println("destination database is " + dbName);
		rand = new Random();
		getConnection();
		deleteTables();
		createTables();
		
		ArrayList<Thread> createTable = new ArrayList<Thread>(); 

		createTable.add(new Thread(new PopulateAuthorTable()));
		createTable.add(new Thread(new PopulateCountryTable()));
		createTable.add(new Thread(new PopulateItemTable()));
		
		
		
		Customer_block_size = NUM_CUSTOMERS / concurrency;
		Customer_extra = NUM_CUSTOMERS - Customer_block_size * concurrency;
		for(int i = 0; i < concurrency; i++)
			createTable.add(new Thread(new PopulateCustomerTable(Customer_block_size)));
		if(Customer_extra != 0)
			createTable.add(new Thread(new PopulateCustomerTable(Customer_extra)));
		
		
		Address_block_size = NUM_ADDRESSES / concurrency;
		Address_extra = NUM_ADDRESSES - Address_block_size * concurrency;
		for (int i = 0; i < concurrency; i++)
			createTable.add(new Thread(new PopulateAddressTable(Address_block_size)));
		if(Address_extra != 0)
			createTable.add(new Thread(new PopulateAddressTable(Address_extra)));
		
		for (Thread thread : createTable) {
			thread.setDaemon(true);
			thread.start();
		}
		// populateOrdersAndCC_XACTSTable();
		
		int thread_num = createTable.size();
		
		while(state != thread_num){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(state);
		System.out.println(thread_num);
		addIndexes();
		System.out.println("Done");
		closeConnection();
	}

	private void initiate() {
		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream("db.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Properties p = new Properties();
		try {
			p.load(inputStream);
			dbName = p.getProperty("dbname");
			username = p.getProperty("username");
			password = p.getProperty("password");
			concurrency = Integer.parseInt(p.getProperty("concurrency"));
			NUM_EBS = Integer.parseInt(p.getProperty("ebnumber"));
			NUM_ITEMS = Integer.parseInt(p.getProperty("itemnumber"));
			batch_of_address = Integer.parseInt(p.getProperty("batch_of_address"));
			batch_of_customer = Integer.parseInt(p.getProperty("batch_of_customer"));
		} catch (IOException e1) {
			System.out.println("Property file ERROR");
			e1.printStackTrace();
		}
		NUM_CUSTOMERS = NUM_EBS * 2880;
		NUM_ADDRESSES = 2 * NUM_CUSTOMERS;
		NUM_AUTHORS = (int) (.25 * NUM_ITEMS);
		NUM_ORDERS = (int) (.9 * NUM_CUSTOMERS);
	}

	private static void addIndexes() {
		System.out.println("Adding Indexes");
		try {
			PreparedStatement statement1 = con
					.prepareStatement("create index author_a_lname on author(a_lname)");
			statement1.executeUpdate();
			PreparedStatement statement2 = con
					.prepareStatement("create index address_addr_co_id on address(addr_co_id)");
			statement2.executeUpdate();
			PreparedStatement statement3 = con
					.prepareStatement("create index addr_zip on address(addr_zip)");
			statement3.executeUpdate();
			PreparedStatement statement4 = con
					.prepareStatement("create index customer_c_addr_id on customer(c_addr_id)");
			statement4.executeUpdate();
			PreparedStatement statement5 = con
					.prepareStatement("create index customer_c_uname on customer(c_uname)");
			statement5.executeUpdate();
			PreparedStatement statement6 = con
					.prepareStatement("create index item_i_title on item(i_title)");
			statement6.executeUpdate();
			PreparedStatement statement7 = con
					.prepareStatement("create index item_i_subject on item(i_subject)");
			statement7.executeUpdate();
			PreparedStatement statement8 = con
					.prepareStatement("create index item_i_a_id on item(i_a_id)");
			statement8.executeUpdate();
			PreparedStatement statement9 = con
					.prepareStatement("create index order_line_ol_i_id on order_line(ol_i_id)");
			statement9.executeUpdate();
			PreparedStatement statement10 = con
					.prepareStatement("create index order_line_ol_o_id on order_line(ol_o_id)");
			statement10.executeUpdate();
			PreparedStatement statement11 = con
					.prepareStatement("create index country_co_name on country(co_name)");
			statement11.executeUpdate();
			PreparedStatement statement12 = con
					.prepareStatement("create index orders_o_c_id on orders(o_c_id)");
			statement12.executeUpdate();
			PreparedStatement statement13 = con
					.prepareStatement("create index scl_i_id on shopping_cart_line(scl_i_id)");
			statement13.executeUpdate();
			con.commit();
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to add indexes");
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public static class PopulateCustomerTable implements Runnable {
		
		private int MAX_NUM;
		
		public PopulateCustomerTable(int number){
			MAX_NUM = number;
//			System.out.println("CREATE CUSTOMERS");
//			System.out.println(MAX_NUM);
		}


		public void run() {
			String C_UNAME, C_PASSWD, C_LNAME, C_FNAME;
			int C_ADDR_ID, C_PHONE;
			String C_EMAIL;
			java.sql.Date C_SINCE, C_LAST_LOGIN;
			java.sql.Timestamp C_LOGIN, C_EXPIRATION;
			double C_DISCOUNT, C_BALANCE, C_YTD_PMT;
			java.sql.Date C_BIRTHDATE;
			String C_DATA;
			int i;
			try {
				PreparedStatement statement = con
						.prepareStatement("INSERT INTO CUSTOMER (C_ID,C_UNAME,C_PASSWD,C_FNAME,C_LNAME,C_ADDR_ID,C_PHONE,C_EMAIL,C_SINCE,C_LAST_LOGIN,C_LOGIN,C_EXPIRATION,C_DISCOUNT,C_BALANCE,C_YTD_PMT,C_BIRTHDATE,C_DATA) VALUES (CUSTOMER_SEQ.nextval,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");
				int j = 0;
//				System.out.println("before customer");
//				System.out.println(MAX_NUM);
				for (i = 1; i <= MAX_NUM; i++) {
//					System.out.println("inside cycle");
					if (i % 10000 == 0)
						System.out.println("Populating CUSTOMER Table with " + i + " customers");
					C_UNAME = DigSyl(i, 0);
					C_PASSWD = C_UNAME.toLowerCase();
					C_LNAME = getRandomAString(8, 15);
					C_FNAME = getRandomAString(8, 15);
					C_ADDR_ID = getRandomInt(1, 2 * NUM_CUSTOMERS);
					C_PHONE = getRandomNString(9, 16);
					C_EMAIL = C_UNAME + "@" + getRandomAString(2, 9) + ".com";

					GregorianCalendar cal = new GregorianCalendar();
					cal.add(Calendar.DAY_OF_YEAR, -1 * getRandomInt(1, 730));
					C_SINCE = new java.sql.Date(cal.getTime().getTime());
					cal.add(Calendar.DAY_OF_YEAR, getRandomInt(0, 60));
					if (cal.after(new GregorianCalendar()))
						cal = new GregorianCalendar();

					C_LAST_LOGIN = new java.sql.Date(cal.getTime().getTime());
					C_LOGIN = new java.sql.Timestamp(System.currentTimeMillis());
					cal = new GregorianCalendar();
					cal.add(Calendar.HOUR, 2);
					C_EXPIRATION = new java.sql.Timestamp(cal.getTime().getTime());

					C_DISCOUNT = (double) getRandomInt(0, 10);
					C_BALANCE = 0.00;
					C_YTD_PMT = (double) getRandomInt(0, 99999) / 100.0;
					int year = getRandomInt(1880, 2000);
					int month = getRandomInt(0, 11);
					int maxday = 31;
					int day;
					if (month == 3 | month == 5 | month == 8 | month == 10)
						maxday = 30;
					else if (month == 1)
						maxday = 28;
					day = getRandomInt(1, maxday);
					cal = new GregorianCalendar(year, month, day);
					C_BIRTHDATE = new java.sql.Date(cal.getTime().getTime());

					C_DATA = getRandomAString(100, 500);

					try {// Set parameter
						statement.setString(1, C_UNAME);
						statement.setString(2, C_PASSWD);
						statement.setString(3, C_FNAME);
						statement.setString(4, C_LNAME);
						statement.setInt(5, C_ADDR_ID);
						statement.setInt(6, C_PHONE);
						statement.setString(7, C_EMAIL);
						statement.setDate(8, C_SINCE);
						statement.setDate(9, C_LAST_LOGIN);
						statement.setTimestamp(10, C_LOGIN);
						statement.setTimestamp(11, C_EXPIRATION);
						statement.setDouble(12, C_DISCOUNT);
						statement.setDouble(13, C_BALANCE);
						statement.setDouble(14, C_YTD_PMT);
						statement.setDate(15, C_BIRTHDATE);
						statement.setString(16, C_DATA);
						statement.addBatch();
//						statement.executeUpdate();
						if (i % batch_of_customer == 0) {
							statement.executeBatch();
							con.commit();
//							statement.clearBatch();
						}
					} catch (java.lang.Exception ex) {
						System.err.println("Unable to populate CUSTOMER table");
						System.out.println("C_ID=" + i + " C_UNAME=" + C_UNAME + " C_PASSWD="
								+ C_PASSWD + " C_FNAME=" + C_FNAME + " C_LNAME=" + C_LNAME
								+ " C_ADDR_ID=" + C_ADDR_ID + " C_PHONE=" + C_PHONE + " C_EMAIL="
								+ C_EMAIL + " C_SINCE=" + C_SINCE + " C_LAST_LOGIN=" + C_LAST_LOGIN
								+ " C_LOGIN= " + C_LOGIN + " C_EXPIRATION=" + C_EXPIRATION
								+ " C_DISCOUNT=" + C_DISCOUNT + " C_BALANCE=" + C_BALANCE
								+ " C_YTD_PMT" + C_YTD_PMT + "C_BIRTHDATE=" + C_BIRTHDATE + "C_DATA="
								+ C_DATA);
						ex.printStackTrace();
						System.exit(1);
					}
				}
				System.out.print("\n");
				statement.executeBatch();
				con.commit();
				state++;
//				System.out.println("customer" + state);
			} catch (java.lang.Exception ex) {
				System.err.println("Unable to populate CUSTOMER table");
				ex.printStackTrace();
				System.exit(1);
			}
			
		}
		
	}
//	private static void populateCustomerTable() {
//		String C_UNAME, C_PASSWD, C_LNAME, C_FNAME;
//		int C_ADDR_ID, C_PHONE;
//		String C_EMAIL;
//		java.sql.Date C_SINCE, C_LAST_LOGIN;
//		java.sql.Timestamp C_LOGIN, C_EXPIRATION;
//		double C_DISCOUNT, C_BALANCE, C_YTD_PMT;
//		java.sql.Date C_BIRTHDATE;
//		String C_DATA;
//		int i;
//		System.out.println("Populating CUSTOMER Table with " + NUM_CUSTOMERS + " customers");
//		System.out.print("Complete (in 10,000's): ");
//		try {
//			PreparedStatement statement = con
//					.prepareStatement("INSERT INTO CUSTOMER (C_UNAME,C_PASSWD,C_FNAME,C_LNAME,C_ADDR_ID,C_PHONE,C_EMAIL,C_SINCE,C_LAST_LOGIN,C_LOGIN,C_EXPIRATION,C_DISCOUNT,C_BALANCE,C_YTD_PMT,C_BIRTHDATE,C_DATA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)");
//			int j = 0;
//			for (i = 1; i <= NUM_CUSTOMERS; i++) {
//				if (i % 10000 == 0)
//					System.out.print(i / 10000 + " ");
//				C_UNAME = DigSyl(i, 0);
//				C_PASSWD = C_UNAME.toLowerCase();
//				C_LNAME = getRandomAString(8, 15);
//				C_FNAME = getRandomAString(8, 15);
//				C_ADDR_ID = getRandomInt(1, 2 * NUM_CUSTOMERS);
//				C_PHONE = getRandomNString(9, 16);
//				C_EMAIL = C_UNAME + "@" + getRandomAString(2, 9) + ".com";
//
//				GregorianCalendar cal = new GregorianCalendar();
//				cal.add(Calendar.DAY_OF_YEAR, -1 * getRandomInt(1, 730));
//				C_SINCE = new java.sql.Date(cal.getTime().getTime());
//				cal.add(Calendar.DAY_OF_YEAR, getRandomInt(0, 60));
//				if (cal.after(new GregorianCalendar()))
//					cal = new GregorianCalendar();
//
//				C_LAST_LOGIN = new java.sql.Date(cal.getTime().getTime());
//				C_LOGIN = new java.sql.Timestamp(System.currentTimeMillis());
//				cal = new GregorianCalendar();
//				cal.add(Calendar.HOUR, 2);
//				C_EXPIRATION = new java.sql.Timestamp(cal.getTime().getTime());
//
//				C_DISCOUNT = (double) getRandomInt(0, 10);
//				C_BALANCE = 0.00;
//				C_YTD_PMT = (double) getRandomInt(0, 99999) / 100.0;
//				int year = getRandomInt(1880, 2000);
//				int month = getRandomInt(0, 11);
//				int maxday = 31;
//				int day;
//				if (month == 3 | month == 5 | month == 8 | month == 10)
//					maxday = 30;
//				else if (month == 1)
//					maxday = 28;
//				day = getRandomInt(1, maxday);
//				cal = new GregorianCalendar(year, month, day);
//				C_BIRTHDATE = new java.sql.Date(cal.getTime().getTime());
//
//				C_DATA = getRandomAString(100, 500);
//
//				try {// Set parameter
//					statement.setString(1, C_UNAME);
//					statement.setString(2, C_PASSWD);
//					statement.setString(3, C_FNAME);
//					statement.setString(4, C_LNAME);
//					statement.setInt(5, C_ADDR_ID);
//					statement.setInt(6, C_PHONE);
//					statement.setString(7, C_EMAIL);
//					statement.setDate(8, C_SINCE);
//					statement.setDate(9, C_LAST_LOGIN);
//					statement.setTimestamp(10, C_LOGIN);
//					statement.setTimestamp(11, C_EXPIRATION);
//					statement.setDouble(12, C_DISCOUNT);
//					statement.setDouble(13, C_BALANCE);
//					statement.setDouble(14, C_YTD_PMT);
//					statement.setDate(15, C_BIRTHDATE);
//					statement.setString(16, C_DATA);
//					statement.executeUpdate();
//					if (i % 50 == 0) {
//						con.commit();
//					}
//				} catch (java.lang.Exception ex) {
//					System.err.println("Unable to populate CUSTOMER table");
//					System.out.println("C_ID=" + i + " C_UNAME=" + C_UNAME + " C_PASSWD="
//							+ C_PASSWD + " C_FNAME=" + C_FNAME + " C_LNAME=" + C_LNAME
//							+ " C_ADDR_ID=" + C_ADDR_ID + " C_PHONE=" + C_PHONE + " C_EMAIL="
//							+ C_EMAIL + " C_SINCE=" + C_SINCE + " C_LAST_LOGIN=" + C_LAST_LOGIN
//							+ " C_LOGIN= " + C_LOGIN + " C_EXPIRATION=" + C_EXPIRATION
//							+ " C_DISCOUNT=" + C_DISCOUNT + " C_BALANCE=" + C_BALANCE
//							+ " C_YTD_PMT" + C_YTD_PMT + "C_BIRTHDATE=" + C_BIRTHDATE + "C_DATA="
//							+ C_DATA);
//					ex.printStackTrace();
//					System.exit(1);
//				}
//			}
//			System.out.print("\n");
//			con.commit();
//		} catch (java.lang.Exception ex) {
//			System.err.println("Unable to populate CUSTOMER table");
//			ex.printStackTrace();
//			System.exit(1);
//		}
//	}
	
	public static class PopulateAddressTable implements Runnable {
		
		private int MAX_NUM;
		
		public PopulateAddressTable(int number){
			MAX_NUM = number;
			
		}


		public void run() {
			String ADDR_STREET1, ADDR_STREET2, ADDR_CITY, ADDR_STATE;
			String ADDR_ZIP;
			int ADDR_CO_ID;
			try {
				PreparedStatement statement = con
						.prepareStatement("INSERT INTO ADDRESS(ADDR_ID,ADDR_STREET1,ADDR_STREET2,ADDR_CITY,ADDR_STATE,ADDR_ZIP,ADDR_CO_ID) VALUES (ADDRESS_SEQ.nextval,?, ?, ?, ?, ?, ?)");
				for (int i = 1; i <= MAX_NUM; i++) {
					if (i % 10000 == 0)
						System.out.println("Populating ADDRESS Table with " + i + " addresses");
					ADDR_STREET1 = getRandomAString(15, 40);
					ADDR_STREET2 = getRandomAString(15, 40);
					ADDR_CITY = getRandomAString(4, 30);
					ADDR_STATE = getRandomAString(2, 20);
					ADDR_ZIP = getRandomAString(5, 10);
					ADDR_CO_ID = getRandomInt(1, 92);

					// Set parameter
					statement.setString(1, ADDR_STREET1);
					statement.setString(2, ADDR_STREET2);
					statement.setString(3, ADDR_CITY);
					statement.setString(4, ADDR_STATE);
					statement.setString(5, ADDR_ZIP);
					statement.setInt(6, ADDR_CO_ID);
					statement.addBatch();
//					statement.executeUpdate();
					if (i % batch_of_address == 0){
						statement.executeBatch();
						con.commit();
//						statement.clearBatch();
					}
				}
				statement.executeBatch();
				con.commit();
				state++;
//				System.out.println("address" + state);
			} catch (java.lang.Exception ex) {
				System.err.println("Unable to populate ADDRESS table");
				ex.printStackTrace();
				System.exit(1);
			}
			System.out.print("\n");

		}
		
	}
//	private static void populateAddressTable() {
//		System.out.println("Populating ADDRESS Table with " + NUM_ADDRESSES + " addresses");
//		System.out.print("Complete (in 10,000's): ");
//		String ADDR_STREET1, ADDR_STREET2, ADDR_CITY, ADDR_STATE;
//		String ADDR_ZIP;
//		int ADDR_CO_ID;
//		try {
//			PreparedStatement statement = con
//					.prepareStatement("INSERT INTO ADDRESS(ADDR_STREET1,ADDR_STREET2,ADDR_CITY,ADDR_STATE,ADDR_ZIP,ADDR_CO_ID) VALUES (?, ?, ?, ?, ?, ?)");
//			for (int i = 1; i <= NUM_ADDRESSES; i++) {
//				if (i % 10000 == 0)
//					System.out.print(i / 10000 + " ");
//				ADDR_STREET1 = getRandomAString(15, 40);
//				ADDR_STREET2 = getRandomAString(15, 40);
//				ADDR_CITY = getRandomAString(4, 30);
//				ADDR_STATE = getRandomAString(2, 20);
//				ADDR_ZIP = getRandomAString(5, 10);
//				ADDR_CO_ID = getRandomInt(1, 92);
//
//				// Set parameter
//				statement.setString(1, ADDR_STREET1);
//				statement.setString(2, ADDR_STREET2);
//				statement.setString(3, ADDR_CITY);
//				statement.setString(4, ADDR_STATE);
//				statement.setString(5, ADDR_ZIP);
//				statement.setInt(6, ADDR_CO_ID);
//				statement.executeUpdate();
//				if (i % 100 == 0)
//					con.commit();
//			}
//			con.commit();
//		} catch (java.lang.Exception ex) {
//			System.err.println("Unable to populate ADDRESS table");
//			ex.printStackTrace();
//			System.exit(1);
//		}
//		System.out.print("\n");
//	}

	public static class PopulateAuthorTable implements Runnable {


		public void run() {
			String A_FNAME, A_MNAME, A_LNAME, A_BIO;
			java.sql.Date A_DOB;
			GregorianCalendar cal;

			

			try {
				PreparedStatement statement = con
						.prepareStatement("INSERT INTO AUTHOR(A_ID,A_FNAME,A_LNAME,A_MNAME,A_DOB,A_BIO) VALUES (AUTHOR_SEQ.nextval,?, ?, ?, ?, ?)");
				for (int i = 1; i <= NUM_AUTHORS; i++) {
					if(i % 10 == 0)
						System.out.println("Populating AUTHOR Table with " + i + " authors");
					int month, day, year, maxday;
					A_FNAME = getRandomAString(3, 20);
					A_MNAME = getRandomAString(1, 20);
					A_LNAME = getRandomAString(1, 20);
					year = getRandomInt(1800, 1990);
					month = getRandomInt(0, 11);
					maxday = 31;
					if (month == 3 | month == 5 | month == 8 | month == 10)
						maxday = 30;
					else if (month == 1)
						maxday = 28;
					day = getRandomInt(1, maxday);
					cal = new GregorianCalendar(year, month, day);
					A_DOB = new java.sql.Date(cal.getTime().getTime());
					A_BIO = getRandomAString(125, 500);
					statement.setString(1, A_FNAME);
					statement.setString(2, A_LNAME);
					statement.setString(3, A_MNAME);
					statement.setDate(4, A_DOB);
					statement.setString(5, A_BIO);
					statement.executeUpdate();
					if (i % 100 == 0)
						con.commit();

				}
				con.commit();
				state++;
//				System.out.println("author" + state);
			} catch (java.lang.Exception ex) {
				System.err.println("Unable to populate AUTHOR table");
				ex.printStackTrace();
				System.exit(1);
			}

		}

	}
//	private static void populateAuthorTable() {
//		String A_FNAME, A_MNAME, A_LNAME, A_BIO;
//		java.sql.Date A_DOB;
//		GregorianCalendar cal;
//
//		System.out.println("Populating AUTHOR Table with " + NUM_AUTHORS + " authors");
//
//		try {
//			PreparedStatement statement = con
//					.prepareStatement("INSERT INTO AUTHOR(A_FNAME,A_LNAME,A_MNAME,A_DOB,A_BIO) VALUES (?, ?, ?, ?, ?)");
//			for (int i = 1; i <= NUM_AUTHORS; i++) {
//				int month, day, year, maxday;
//				A_FNAME = getRandomAString(3, 20);
//				A_MNAME = getRandomAString(1, 20);
//				A_LNAME = getRandomAString(1, 20);
//				year = getRandomInt(1800, 1990);
//				month = getRandomInt(0, 11);
//				maxday = 31;
//				if (month == 3 | month == 5 | month == 8 | month == 10)
//					maxday = 30;
//				else if (month == 1)
//					maxday = 28;
//				day = getRandomInt(1, maxday);
//				cal = new GregorianCalendar(year, month, day);
//				A_DOB = new java.sql.Date(cal.getTime().getTime());
//				A_BIO = getRandomAString(125, 500);
//				statement.setString(1, A_FNAME);
//				statement.setString(2, A_LNAME);
//				statement.setString(3, A_MNAME);
//				statement.setDate(4, A_DOB);
//				statement.setString(5, A_BIO);
//				statement.executeUpdate();
//				if (i % 100 == 0)
//					con.commit();
//
//			}
//			con.commit();
//		} catch (java.lang.Exception ex) {
//			System.err.println("Unable to populate AUTHOR table");
//			ex.printStackTrace();
//			System.exit(1);
//		}
//	}

	public static class PopulateCountryTable implements Runnable {


		public void run() {
			String[] countries = { "United States", "United Kingdom", "Canada", "Germany", "France",
					"Japan", "Netherlands", "Italy", "Switzerland", "Australia", "Algeria",
					"Argentina", "Armenia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
					"Bangla Desh", "Barbados", "Belarus", "Belgium", "Bermuda", "Bolivia", "Botswana",
					"Brazil", "Bulgaria", "Cayman Islands", "Chad", "Chile", "China",
					"Christmas Island", "Colombia", "Croatia", "Cuba", "Cyprus", "Czech Republic",
					"Denmark", "Dominican Republic", "Eastern Caribbean", "Ecuador", "Egypt",
					"El Salvador", "Estonia", "Ethiopia", "Falkland Island", "Faroe Island", "Fiji",
					"Finland", "Gabon", "Gibraltar", "Greece", "Guam", "Hong Kong", "Hungary",
					"Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Jamaica",
					"Jordan", "Kazakhstan", "Kuwait", "Lebanon", "Luxembourg", "Malaysia", "Mexico",
					"Mauritius", "New Zealand", "Norway", "Pakistan", "Philippines", "Poland",
					"Portugal", "Romania", "Russia", "Saudi Arabia", "Singapore", "Slovakia",
					"South Africa", "South Korea", "Spain", "Sudan", "Sweden", "Taiwan", "Thailand",
					"Trinidad", "Turkey", "Venezuela", "Zambia" };

			double[] exchanges = { 1, .625461, 1.46712, 1.86125, 6.24238, 121.907, 2.09715, 1842.64,
					1.51645, 1.54208, 65.3851, 0.998, 540.92, 13.0949, 3977, 1, .3757, 48.65, 2,
					248000, 38.3892, 1, 5.74, 4.7304, 1.71, 1846, .8282, 627.1999, 494.2, 8.278,
					1.5391, 1677, 7.3044, 23, .543, 36.0127, 7.0707, 15.8, 2.7, 9600, 3.33771, 8.7,
					14.9912, 7.7, .6255, 7.124, 1.9724, 5.65822, 627.1999, .6255, 309.214, 1, 7.75473,
					237.23, 74.147, 42.75, 8100, 3000, .3083, .749481, 4.12, 37.4, 0.708, 150, .3062,
					1502, 38.3892, 3.8, 9.6287, 25.245, 1.87539, 7.83101, 52, 37.8501, 3.9525, 190.788,
					15180.2, 24.43, 3.7501, 1.72929, 43.9642, 6.25845, 1190.15, 158.34, 5.282, 8.54477,
					32.77, 37.1414, 6.1764, 401500, 596, 2447.7 };

			String[] currencies = { "Dollars", "Pounds", "Dollars", "Deutsche Marks", "Francs", "Yen",
					"Guilders", "Lira", "Francs", "Dollars", "Dinars", "Pesos", "Dram", "Schillings",
					"Manat", "Dollars", "Dinar", "Taka", "Dollars", "Rouble", "Francs", "Dollars",
					"Boliviano", "Pula", "Real", "Lev", "Dollars", "Franc", "Pesos", "Yuan Renmimbi",
					"Dollars", "Pesos", "Kuna", "Pesos", "Pounds", "Koruna", "Kroner", "Pesos",
					"Dollars", "Sucre", "Pounds", "Colon", "Kroon", "Birr", "Pound", "Krone",
					"Dollars", "Markka", "Franc", "Pound", "Drachmas", "Dollars", "Dollars", "Forint",
					"Krona", "Rupees", "Rupiah", "Rial", "Dinar", "Punt", "Shekels", "Dollars",
					"Dinar", "Tenge", "Dinar", "Pounds", "Francs", "Ringgit", "Pesos", "Rupees",
					"Dollars", "Kroner", "Rupees", "Pesos", "Zloty", "Escudo", "Leu", "Rubles",
					"Riyal", "Dollars", "Koruna", "Rand", "Won", "Pesetas", "Dinar", "Krona",
					"Dollars", "Baht", "Dollars", "Lira", "Bolivar", "Kwacha" };

			int NUM_COUNTRIES = 92;

			

			try {
				PreparedStatement statement = con
						.prepareStatement("INSERT INTO COUNTRY(CO_ID,CO_NAME,CO_EXCHANGE,CO_CURRENCY) VALUES (COUNTRY_SEQ.nextval,?,?,?)");
				for (int i = 1; i <= NUM_COUNTRIES; i++) {
					// Set parameter
					if(i % 10 == 0)
						System.out.println("Populating COUNTRY with " + i + " countries");
					statement.setString(1, countries[i - 1]);
					statement.setDouble(2, exchanges[i - 1]);
					statement.setString(3, currencies[i - 1]);
					statement.executeUpdate();
				}
				con.commit();
				state++;
//				System.out.println("country" + state);
			} catch (java.lang.Exception ex) {
				System.err.println("Unable to populate COUNTRY table");
				ex.printStackTrace();
				System.exit(1);
			}

		}

	}
//	private static void populateCountryTable() {
//		String[] countries = { "United States", "United Kingdom", "Canada", "Germany", "France",
//				"Japan", "Netherlands", "Italy", "Switzerland", "Australia", "Algeria",
//				"Argentina", "Armenia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
//				"Bangla Desh", "Barbados", "Belarus", "Belgium", "Bermuda", "Bolivia", "Botswana",
//				"Brazil", "Bulgaria", "Cayman Islands", "Chad", "Chile", "China",
//				"Christmas Island", "Colombia", "Croatia", "Cuba", "Cyprus", "Czech Republic",
//				"Denmark", "Dominican Republic", "Eastern Caribbean", "Ecuador", "Egypt",
//				"El Salvador", "Estonia", "Ethiopia", "Falkland Island", "Faroe Island", "Fiji",
//				"Finland", "Gabon", "Gibraltar", "Greece", "Guam", "Hong Kong", "Hungary",
//				"Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Jamaica",
//				"Jordan", "Kazakhstan", "Kuwait", "Lebanon", "Luxembourg", "Malaysia", "Mexico",
//				"Mauritius", "New Zealand", "Norway", "Pakistan", "Philippines", "Poland",
//				"Portugal", "Romania", "Russia", "Saudi Arabia", "Singapore", "Slovakia",
//				"South Africa", "South Korea", "Spain", "Sudan", "Sweden", "Taiwan", "Thailand",
//				"Trinidad", "Turkey", "Venezuela", "Zambia" };
//
//		double[] exchanges = { 1, .625461, 1.46712, 1.86125, 6.24238, 121.907, 2.09715, 1842.64,
//				1.51645, 1.54208, 65.3851, 0.998, 540.92, 13.0949, 3977, 1, .3757, 48.65, 2,
//				248000, 38.3892, 1, 5.74, 4.7304, 1.71, 1846, .8282, 627.1999, 494.2, 8.278,
//				1.5391, 1677, 7.3044, 23, .543, 36.0127, 7.0707, 15.8, 2.7, 9600, 3.33771, 8.7,
//				14.9912, 7.7, .6255, 7.124, 1.9724, 5.65822, 627.1999, .6255, 309.214, 1, 7.75473,
//				237.23, 74.147, 42.75, 8100, 3000, .3083, .749481, 4.12, 37.4, 0.708, 150, .3062,
//				1502, 38.3892, 3.8, 9.6287, 25.245, 1.87539, 7.83101, 52, 37.8501, 3.9525, 190.788,
//				15180.2, 24.43, 3.7501, 1.72929, 43.9642, 6.25845, 1190.15, 158.34, 5.282, 8.54477,
//				32.77, 37.1414, 6.1764, 401500, 596, 2447.7 };
//
//		String[] currencies = { "Dollars", "Pounds", "Dollars", "Deutsche Marks", "Francs", "Yen",
//				"Guilders", "Lira", "Francs", "Dollars", "Dinars", "Pesos", "Dram", "Schillings",
//				"Manat", "Dollars", "Dinar", "Taka", "Dollars", "Rouble", "Francs", "Dollars",
//				"Boliviano", "Pula", "Real", "Lev", "Dollars", "Franc", "Pesos", "Yuan Renmimbi",
//				"Dollars", "Pesos", "Kuna", "Pesos", "Pounds", "Koruna", "Kroner", "Pesos",
//				"Dollars", "Sucre", "Pounds", "Colon", "Kroon", "Birr", "Pound", "Krone",
//				"Dollars", "Markka", "Franc", "Pound", "Drachmas", "Dollars", "Dollars", "Forint",
//				"Krona", "Rupees", "Rupiah", "Rial", "Dinar", "Punt", "Shekels", "Dollars",
//				"Dinar", "Tenge", "Dinar", "Pounds", "Francs", "Ringgit", "Pesos", "Rupees",
//				"Dollars", "Kroner", "Rupees", "Pesos", "Zloty", "Escudo", "Leu", "Rubles",
//				"Riyal", "Dollars", "Koruna", "Rand", "Won", "Pesetas", "Dinar", "Krona",
//				"Dollars", "Baht", "Dollars", "Lira", "Bolivar", "Kwacha" };
//
//		int NUM_COUNTRIES = 92;
//
//		System.out.println("Populating COUNTRY with " + NUM_COUNTRIES + " countries");
//
//		try {
//			PreparedStatement statement = con
//					.prepareStatement("INSERT INTO COUNTRY(CO_NAME,CO_EXCHANGE,CO_CURRENCY) VALUES (?,?,?)");
//			for (int i = 1; i <= NUM_COUNTRIES; i++) {
//				// Set parameter
//				statement.setString(1, countries[i - 1]);
//				statement.setDouble(2, exchanges[i - 1]);
//				statement.setString(3, currencies[i - 1]);
//				statement.executeUpdate();
//			}
//			con.commit();
//		} catch (java.lang.Exception ex) {
//			System.err.println("Unable to populate COUNTRY table");
//			ex.printStackTrace();
//			System.exit(1);
//		}
//	}

	public static class PopulateItemTable implements Runnable {

		public void run() {
			String I_TITLE;
			GregorianCalendar cal;
			int I_A_ID;
			java.sql.Date I_PUB_DATE;
			String I_PUBLISHER, I_SUBJECT, I_DESC;
			int I_RELATED1, I_RELATED2, I_RELATED3, I_RELATED4, I_RELATED5;
			String I_THUMBNAIL, I_IMAGE;
			double I_SRP, I_COST;
			java.sql.Date I_AVAIL;
			int I_STOCK;
			String I_ISBN;
			int I_PAGE;
			String I_BACKING;
			String I_DIMENSIONS;

			String[] SUBJECTS = { "ARTS", "BIOGRAPHIES", "BUSINESS", "CHILDREN", "COMPUTERS",
					"COOKING", "HEALTH", "HISTORY", "HOME", "HUMOR", "LITERATURE", "MYSTERY",
					"NON-FICTION", "PARENTING", "POLITICS", "REFERENCE", "RELIGION", "ROMANCE",
					"SELF-HELP", "SCIENCE-NATURE", "SCIENCE_FICTION", "SPORTS", "YOUTH", "TRAVEL" };
			int NUM_SUBJECTS = 24;

			String[] BACKINGS = { "HARDBACK", "PAPERBACK", "USED", "AUDIO", "LIMITED-EDITION" };
			int NUM_BACKINGS = 5;

			
			try {
				PreparedStatement statement = con
						.prepareStatement("INSERT INTO ITEM (I_ID,I_TITLE , I_A_ID, I_PUB_DATE, I_PUBLISHER, I_SUBJECT, I_DESC, I_RELATED1, I_RELATED2, I_RELATED3, I_RELATED4, I_RELATED5, I_THUMBNAIL, I_IMAGE, I_SRP, I_COST, I_AVAIL, I_STOCK, I_ISBN, I_PAGE, I_BACKING, I_DIMENSIONS) VALUES (ITEM_SEQ.nextval,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				for (int i = 1; i <= NUM_ITEMS; i++) {
					if(i % 100 == 0 )
						System.out.println("Populating ITEM table with " + i + " items");
					int month, day, year, maxday;
					I_TITLE = getRandomAString(14, 60);
					if (i <= (NUM_ITEMS / 4))
						I_A_ID = i;
					else
						I_A_ID = getRandomInt(1, NUM_ITEMS / 4);

					year = getRandomInt(1983, 2009);
					month = getRandomInt(0, 11);
					maxday = 31;
					if (month == 3 | month == 5 | month == 8 | month == 10)
						maxday = 30;
					else if (month == 1)
						maxday = 28;
					day = getRandomInt(1, maxday);
					cal = new GregorianCalendar(year, month, day);
					I_PUB_DATE = new java.sql.Date(cal.getTime().getTime());

					I_PUBLISHER = getRandomAString(14, 60);
					I_SUBJECT = SUBJECTS[getRandomInt(0, NUM_SUBJECTS - 1)];
					I_DESC = getRandomAString(100, 500);

					I_RELATED1 = getRandomInt(1, NUM_ITEMS);
					do {
						I_RELATED2 = getRandomInt(1, NUM_ITEMS);
					} while (I_RELATED2 == I_RELATED1);
					do {
						I_RELATED3 = getRandomInt(1, NUM_ITEMS);
					} while (I_RELATED3 == I_RELATED1 || I_RELATED3 == I_RELATED2);
					do {
						I_RELATED4 = getRandomInt(1, NUM_ITEMS);
					} while (I_RELATED4 == I_RELATED1 || I_RELATED4 == I_RELATED2
							|| I_RELATED4 == I_RELATED3);
					do {
						I_RELATED5 = getRandomInt(1, NUM_ITEMS);
					} while (I_RELATED5 == I_RELATED1 || I_RELATED5 == I_RELATED2
							|| I_RELATED5 == I_RELATED3 || I_RELATED5 == I_RELATED4);

					I_THUMBNAIL = new String("thumb_" + i + ".jpg");
					I_IMAGE = new String("item_" + i + ".jpg");

					int tem = getRandomInt(1, 10000);
					if (tem < 9500) {
						I_SRP = tem / 100.0 + 100;
					} else {
						I_SRP = tem / 100.0 + 900;
					}

					I_COST = I_SRP * 0.9;

					cal.add(Calendar.DAY_OF_YEAR, getRandomInt(1, 30));
					I_AVAIL = new java.sql.Date(cal.getTime().getTime());
					I_STOCK = getRandomInt(10, 30);
					I_ISBN = getRandomAString(13);
					I_PAGE = getRandomInt(20, 9999);
					I_BACKING = BACKINGS[getRandomInt(0, NUM_BACKINGS - 1)];
					I_DIMENSIONS = ((double) getRandomInt(1, 9999) / 100.0) + "x"
							+ ((double) getRandomInt(1, 9999) / 100.0) + "x"
							+ ((double) getRandomInt(1, 9999) / 100.0);

					// Set parameter
					statement.setString(1, I_TITLE);
					statement.setInt(2, I_A_ID);
					statement.setDate(3, I_PUB_DATE);
					statement.setString(4, I_PUBLISHER);
					statement.setString(5, I_SUBJECT);
					statement.setString(6, I_DESC);
					statement.setInt(7, I_RELATED1);
					statement.setInt(8, I_RELATED2);
					statement.setInt(9, I_RELATED3);
					statement.setInt(10, I_RELATED4);
					statement.setInt(11, I_RELATED5);
					statement.setString(12, I_THUMBNAIL);
					statement.setString(13, I_IMAGE);
					statement.setDouble(14, I_SRP);
					statement.setDouble(15, I_COST);
					statement.setDate(16, I_AVAIL);
					statement.setInt(17, I_STOCK);
					statement.setString(18, I_ISBN);
					statement.setInt(19, I_PAGE);
					statement.setString(20, I_BACKING);
					statement.setString(21, I_DIMENSIONS);

					statement.executeUpdate();
					if (i % 100 == 0)
						con.commit();
				}
				con.commit();
				state++;
//				System.out.println("item" + state);
			} catch (java.lang.Exception ex) {
				System.err.println("Unable to populate ITEM table");
				ex.printStackTrace();
				System.exit(1);
			}

		}
		
	}
//	private static void populateItemTable() {
//		String I_TITLE;
//		GregorianCalendar cal;
//		int I_A_ID;
//		java.sql.Date I_PUB_DATE;
//		String I_PUBLISHER, I_SUBJECT, I_DESC;
//		int I_RELATED1, I_RELATED2, I_RELATED3, I_RELATED4, I_RELATED5;
//		String I_THUMBNAIL, I_IMAGE;
//		double I_SRP, I_COST;
//		java.sql.Date I_AVAIL;
//		int I_STOCK;
//		String I_ISBN;
//		int I_PAGE;
//		String I_BACKING;
//		String I_DIMENSIONS;
//
//		String[] SUBJECTS = { "ARTS", "BIOGRAPHIES", "BUSINESS", "CHILDREN", "COMPUTERS",
//				"COOKING", "HEALTH", "HISTORY", "HOME", "HUMOR", "LITERATURE", "MYSTERY",
//				"NON-FICTION", "PARENTING", "POLITICS", "REFERENCE", "RELIGION", "ROMANCE",
//				"SELF-HELP", "SCIENCE-NATURE", "SCIENCE_FICTION", "SPORTS", "YOUTH", "TRAVEL" };
//		int NUM_SUBJECTS = 24;
//
//		String[] BACKINGS = { "HARDBACK", "PAPERBACK", "USED", "AUDIO", "LIMITED-EDITION" };
//		int NUM_BACKINGS = 5;
//
//		System.out.println("Populating ITEM table with " + NUM_ITEMS + " items");
//		try {
//			PreparedStatement statement = con
//					.prepareStatement("INSERT INTO ITEM (I_TITLE , I_A_ID, I_PUB_DATE, I_PUBLISHER, I_SUBJECT, I_DESC, I_RELATED1, I_RELATED2, I_RELATED3, I_RELATED4, I_RELATED5, I_THUMBNAIL, I_IMAGE, I_SRP, I_COST, I_AVAIL, I_STOCK, I_ISBN, I_PAGE, I_BACKING, I_DIMENSIONS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//			for (int i = 1; i <= NUM_ITEMS; i++) {
//				int month, day, year, maxday;
//				I_TITLE = getRandomAString(14, 60);
//				if (i <= (NUM_ITEMS / 4))
//					I_A_ID = i;
//				else
//					I_A_ID = getRandomInt(1, NUM_ITEMS / 4);
//
//				year = getRandomInt(1983, 2009);
//				month = getRandomInt(0, 11);
//				maxday = 31;
//				if (month == 3 | month == 5 | month == 8 | month == 10)
//					maxday = 30;
//				else if (month == 1)
//					maxday = 28;
//				day = getRandomInt(1, maxday);
//				cal = new GregorianCalendar(year, month, day);
//				I_PUB_DATE = new java.sql.Date(cal.getTime().getTime());
//
//				I_PUBLISHER = getRandomAString(14, 60);
//				I_SUBJECT = SUBJECTS[getRandomInt(0, NUM_SUBJECTS - 1)];
//				I_DESC = getRandomAString(100, 500);
//
//				I_RELATED1 = getRandomInt(1, NUM_ITEMS);
//				do {
//					I_RELATED2 = getRandomInt(1, NUM_ITEMS);
//				} while (I_RELATED2 == I_RELATED1);
//				do {
//					I_RELATED3 = getRandomInt(1, NUM_ITEMS);
//				} while (I_RELATED3 == I_RELATED1 || I_RELATED3 == I_RELATED2);
//				do {
//					I_RELATED4 = getRandomInt(1, NUM_ITEMS);
//				} while (I_RELATED4 == I_RELATED1 || I_RELATED4 == I_RELATED2
//						|| I_RELATED4 == I_RELATED3);
//				do {
//					I_RELATED5 = getRandomInt(1, NUM_ITEMS);
//				} while (I_RELATED5 == I_RELATED1 || I_RELATED5 == I_RELATED2
//						|| I_RELATED5 == I_RELATED3 || I_RELATED5 == I_RELATED4);
//
//				I_THUMBNAIL = new String("thumb_" + i + ".jpg");
//				I_IMAGE = new String("item_" + i + ".jpg");
//
//				int tem = getRandomInt(1, 10000);
//				if (tem < 9500) {
//					I_SRP = tem / 100.0 + 100;
//				} else {
//					I_SRP = tem / 100.0 + 900;
//				}
//
//				I_COST = I_SRP * 0.9;
//
//				cal.add(Calendar.DAY_OF_YEAR, getRandomInt(1, 30));
//				I_AVAIL = new java.sql.Date(cal.getTime().getTime());
//				I_STOCK = getRandomInt(10, 30);
//				I_ISBN = getRandomAString(13);
//				I_PAGE = getRandomInt(20, 9999);
//				I_BACKING = BACKINGS[getRandomInt(0, NUM_BACKINGS - 1)];
//				I_DIMENSIONS = ((double) getRandomInt(1, 9999) / 100.0) + "x"
//						+ ((double) getRandomInt(1, 9999) / 100.0) + "x"
//						+ ((double) getRandomInt(1, 9999) / 100.0);
//
//				// Set parameter
//				statement.setString(1, I_TITLE);
//				statement.setInt(2, I_A_ID);
//				statement.setDate(3, I_PUB_DATE);
//				statement.setString(4, I_PUBLISHER);
//				statement.setString(5, I_SUBJECT);
//				statement.setString(6, I_DESC);
//				statement.setInt(7, I_RELATED1);
//				statement.setInt(8, I_RELATED2);
//				statement.setInt(9, I_RELATED3);
//				statement.setInt(10, I_RELATED4);
//				statement.setInt(11, I_RELATED5);
//				statement.setString(12, I_THUMBNAIL);
//				statement.setString(13, I_IMAGE);
//				statement.setDouble(14, I_SRP);
//				statement.setDouble(15, I_COST);
//				statement.setDate(16, I_AVAIL);
//				statement.setInt(17, I_STOCK);
//				statement.setString(18, I_ISBN);
//				statement.setInt(19, I_PAGE);
//				statement.setString(20, I_BACKING);
//				statement.setString(21, I_DIMENSIONS);
//
//				statement.executeUpdate();
//				if (i % 100 == 0)
//					con.commit();
//			}
//			con.commit();
//		} catch (java.lang.Exception ex) {
//			System.err.println("Unable to populate ITEM table");
//			ex.printStackTrace();
//			System.exit(1);
//		}
//	}

	private static void populateOrdersAndCC_XACTSTable() {
		GregorianCalendar cal;
		String[] credit_cards = { "VISA", "MASTERCARD", "DISCOVER", "AMEX", "DINERS" };
		int num_card_types = 5;
		String[] ship_types = { "AIR", "UPS", "FEDEX", "SHIP", "COURIER", "MAIL" };
		int num_ship_types = 6;

		String[] status_types = { "PROCESSING", "SHIPPED", "PENDING", "DENIED" };
		int num_status_types = 4;

		// Order variables
		int O_C_ID;
		java.sql.Date O_DATE;
		double O_SUB_TOTAL;
		double O_TAX;
		double O_TOTAL;
		String O_SHIP_TYPE;
		java.sql.Date O_SHIP_DATE;
		int O_BILL_ADDR_ID, O_SHIP_ADDR_ID;
		String O_STATUS;

		String CX_TYPE;
		int CX_NUM;
		String CX_NAME;
		java.sql.Date CX_EXPIRY;
		String CX_AUTH_ID;
		double CX_XACT_AMT;
		int CX_CO_ID;

		System.out.println("Populating ORDERS, ORDER_LINES, CC_XACTS with " + NUM_ORDERS
				+ " orders");

		System.out.print("Complete (in 10,000's): ");
		try {
			PreparedStatement statement = con
					.prepareStatement("INSERT INTO ORDERS(O_ID, O_C_ID, O_DATE, O_SUB_TOTAL, O_TAX, O_TOTAL, O_SHIP_TYPE, O_SHIP_DATE, O_BILL_ADDR_ID, O_SHIP_ADDR_ID, O_STATUS) VALUES (ORDERS_SEQ.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			PreparedStatement statement2 = con
					.prepareStatement("INSERT INTO ORDER_LINE (OL_ID, OL_O_ID, OL_I_ID, OL_QTY, OL_DISCOUNT, OL_COMMENTS) VALUES (?, ?, ?, ?, ?, ?)");
			PreparedStatement statement3 = con
					.prepareStatement("INSERT INTO CC_XACTS(CX_O_ID,CX_TYPE,CX_NUM,CX_NAME,CX_EXPIRE,CX_AUTH_ID,CX_XACT_AMT,CX_XACT_DATE,CX_CO_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

			for (int i = 1; i <= NUM_ORDERS; i++) {
				if (i % 10000 == 0)
					System.out.print(i / 10000 + " ");
				int num_items = getRandomInt(1, 5);
				O_C_ID = getRandomInt(1, NUM_CUSTOMERS);
				cal = new GregorianCalendar();
				cal.add(Calendar.DAY_OF_YEAR, -1 * getRandomInt(1, 60));
				O_DATE = new java.sql.Date(cal.getTime().getTime());
				O_SUB_TOTAL = (double) getRandomInt(1000, 999999) / 100;
				O_TAX = O_SUB_TOTAL * 0.0825;
				O_TOTAL = O_SUB_TOTAL + O_TAX + 3.00 + num_items;
				O_SHIP_TYPE = ship_types[getRandomInt(0, num_ship_types - 1)];
				cal.add(Calendar.DAY_OF_YEAR, getRandomInt(0, 7));
				O_SHIP_DATE = new java.sql.Date(cal.getTime().getTime());

				O_BILL_ADDR_ID = getRandomInt(1, 2 * NUM_CUSTOMERS);
				O_SHIP_ADDR_ID = getRandomInt(1, 2 * NUM_CUSTOMERS);
				O_STATUS = status_types[getRandomInt(0, num_status_types - 1)];

				// Set parameter
				statement.setInt(1, i);
				statement.setInt(2, O_C_ID);
				statement.setDate(3, O_DATE);
				statement.setDouble(4, O_SUB_TOTAL);
				statement.setDouble(5, O_TAX);
				statement.setDouble(6, O_TOTAL);
				statement.setString(7, O_SHIP_TYPE);
				statement.setDate(8, O_SHIP_DATE);
				statement.setInt(9, O_BILL_ADDR_ID);
				statement.setInt(10, O_SHIP_ADDR_ID);
				statement.setString(11, O_STATUS);
				statement.executeUpdate();

				for (int j = 1; j <= num_items; j++) {
					int OL_ID = j;
					int OL_O_ID = i;
					int OL_I_ID = getRandomInt(1, NUM_ITEMS);
					int OL_QTY = getRandomInt(1, 300);
					double OL_DISCOUNT = (double) getRandomInt(0, 30) / 100;
					String OL_COMMENTS = getRandomAString(20, 100);
					statement2.setInt(1, OL_ID);
					statement2.setInt(2, OL_O_ID);
					statement2.setInt(3, OL_I_ID);
					statement2.setInt(4, OL_QTY);
					statement2.setDouble(5, OL_DISCOUNT);
					statement2.setString(6, OL_COMMENTS);
					statement2.executeUpdate();
				}

				CX_TYPE = credit_cards[getRandomInt(0, num_card_types - 1)];
				CX_NUM = getRandomNString(16);
				CX_NAME = getRandomAString(14, 30);
				cal = new GregorianCalendar();
				cal.add(Calendar.DAY_OF_YEAR, getRandomInt(10, 730));
				CX_EXPIRY = new java.sql.Date(cal.getTime().getTime());
				CX_AUTH_ID = getRandomAString(15);
				CX_CO_ID = getRandomInt(1, 92);
				statement3.setInt(1, i);
				statement3.setString(2, CX_TYPE);
				statement3.setInt(3, CX_NUM);
				statement3.setString(4, CX_NAME);
				statement3.setDate(5, CX_EXPIRY);
				statement3.setString(6, CX_AUTH_ID);
				statement3.setDouble(7, O_TOTAL);
				statement3.setDate(8, O_SHIP_DATE);
				statement3.setInt(9, CX_CO_ID);
				statement3.executeUpdate();

				if (i % 100 == 0)
					con.commit();
			}
			con.commit();
		} catch (java.lang.Exception ex) {
			System.err.println("Unable to populate CC_XACTS table");
			ex.printStackTrace();
			System.exit(1);
		}
		System.out.print("\n");
	}

	private static void getConnection() {
		try {
			Class.forName(driverName);
			/*Properties info = new Properties();
			info.put("user", "SYS");
			info.put("password", "onceas");
			info.put("internal_logon", "sysdba");*/
			con= DriverManager.getConnection(dbName.trim(),username.trim(),password.trim());
			con.setAutoCommit(false);
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void closeConnection() {
		try {
			con.close();
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void deleteTables() {
		int i;
		String[] tables = { "ADDRESS", "AUTHOR", "CC_XACTS", "COUNTRY", "CUSTOMER", "ITEM",
				"ORDER_LINE", "ORDERS", "SHOPPING_CART", "SHOPPING_CART_LINE" };
		int numTables = 10;

		for (i = 0; i < numTables; i++) {
			try {
				// Delete each table listed in the tables array
				PreparedStatement statement = con.prepareStatement("DROP TABLE " + tables[i]);
				statement.executeUpdate();
				con.commit();
				System.out.println("Dropped table " + tables[i]);
			} catch (java.lang.Exception ex) {
				System.out.println("Unable to drop table " + tables[i]);
			}
			try {
				// Delete each table sequence in the tables array
				PreparedStatement statement = con.prepareStatement("DROP SEQUENCE " + tables[i]+"_SEQ");
				statement.executeUpdate();
				con.commit();
				System.out.println("Dropped table sequence " + tables[i]);
			} catch (java.lang.Exception ex) {
				System.out.println("Unable to drop table sequence " + tables[i]);
			}
		}
		System.out.println("Done deleting tables!");
	}

	private static void createTables() {
		int i;

		try {
			PreparedStatement statement = con
					//.prepareStatement("CREATE TABLE address ( ADDR_ID int GENERATED ALWAYS AS IDENTITY, ADDR_STREET1 varchar(40),ADDR_STREET2 varchar(40), ADDR_CITY varchar(30), ADDR_STATE varchar(20),ADDR_ZIP varchar(10), ADDR_CO_ID int, PRIMARY KEY(ADDR_ID))");
					.prepareStatement("CREATE TABLE address ( ADDR_ID int PRIMARY KEY, ADDR_STREET1 varchar(40),ADDR_STREET2 varchar(40), ADDR_CITY varchar(30), ADDR_STATE varchar(20),ADDR_ZIP varchar(10), ADDR_CO_ID int)");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table ADDRESS");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create table: ADDRESS");
			ex.printStackTrace();
			System.exit(1);
		}

		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE SEQUENCE ADDRESS_SEQ minvalue 1 nomaxvalue start with 1 increment by 1 nocycle nocache");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table ADDRESS'S SEQUENCE");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create sequence: ADDRESS");
			ex.printStackTrace();
			System.exit(1);
		}
		
		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE author ( A_ID int PRIMARY KEY, A_FNAME varchar(20), A_LNAME varchar(20), A_MNAME varchar(20), A_DOB date, A_BIO varchar(500))");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table AUTHOR");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create table: AUTHOR");
			ex.printStackTrace();
			System.exit(1);
		}
		
		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE SEQUENCE AUTHOR_SEQ minvalue 1 nomaxvalue start with 1 increment by 1 nocycle nocache");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table AUTHOR'S SEQUENCE");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create sequence: AUTHOR");
			ex.printStackTrace();
			System.exit(1);
		}
		
		try {
			PreparedStatement statement = con
					//.prepareStatement("CREATE TABLE cc_xacts ( CX_O_ID int PRIMARY KEY, CX_TYPE varchar(10), CX_NUM varchar(20), CX_NAME varchar(30), CX_EXPIRE date, CX_AUTH_ID char(15), CX_XACT_AMT double, CX_XACT_DATE date, CX_CO_ID int)");
					.prepareStatement("CREATE TABLE cc_xacts ( CX_O_ID int not null PRIMARY KEY, CX_TYPE varchar(10), CX_NUM varchar(20), CX_NAME varchar(30), CX_EXPIRE date, CX_AUTH_ID char(15), CX_XACT_AMT double Precision, CX_XACT_DATE date, CX_CO_ID int)");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table CC_XACTS");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create table: CC_XACTS");
			ex.printStackTrace();
			System.exit(1);
		}

		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE country ( CO_ID int PRIMARY KEY, CO_NAME varchar(50), CO_EXCHANGE double Precision, CO_CURRENCY varchar(18))");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table COUNTRY");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create table: COUNTRY");
			ex.printStackTrace();
			System.exit(1);
		}
		
		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE SEQUENCE COUNTRY_SEQ minvalue 1 nomaxvalue start with 1 increment by 1 nocycle nocache");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table COUNTRY'S SEQUENCE");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create sequence: COUNTRY");
			ex.printStackTrace();
			System.exit(1);
		}
		
		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE customer ( C_ID int PRIMARY KEY, C_UNAME varchar(20), C_PASSWD varchar(20), C_FNAME varchar(17), C_LNAME varchar(17), C_ADDR_ID int, C_PHONE varchar(18), C_EMAIL varchar(50), C_SINCE date, C_LAST_LOGIN date, C_LOGIN timestamp, C_EXPIRATION timestamp, C_DISCOUNT double Precision, C_BALANCE double Precision, C_YTD_PMT double Precision, C_BIRTHDATE date, C_DATA varchar(510))");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table CUSTOMER");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create table: CUSTOMER");
			ex.printStackTrace();
			System.exit(1);
		}

		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE SEQUENCE CUSTOMER_SEQ minvalue 1 nomaxvalue start with 1 increment by 1 nocycle nocache");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table CUSTOMER'S SEQUENCE");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create sequence: CUSTOMER");
			ex.printStackTrace();
			System.exit(1);
		}
		
		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE item ( I_ID int PRIMARY KEY, I_TITLE varchar(60), I_A_ID int, I_PUB_DATE date, I_PUBLISHER varchar(60), I_SUBJECT varchar(60), I_DESC varchar(500), I_RELATED1 int, I_RELATED2 int, I_RELATED3 int, I_RELATED4 int, I_RELATED5 int, I_THUMBNAIL varchar(40), I_IMAGE varchar(40), I_SRP double Precision, I_COST double Precision, I_AVAIL date, I_STOCK int, I_ISBN char(13), I_PAGE int, I_BACKING varchar(15), I_DIMENSIONS varchar(25))");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table ITEM");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create table: ITEM");
			ex.printStackTrace();
			System.exit(1);
		}

		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE SEQUENCE ITEM_SEQ minvalue 1 nomaxvalue start with 1 increment by 1 nocycle nocache");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table ITEM'S SEQUENCE");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create sequence: ITEM");
			ex.printStackTrace();
			System.exit(1);
		}
		
		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE order_line ( OL_ID int not null, OL_O_ID int not null, OL_I_ID int, OL_QTY int, OL_DISCOUNT double Precision, OL_COMMENTS varchar(110), PRIMARY KEY(OL_ID, OL_O_ID))");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table ORDER_LINE");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create table: ORDER_LINE");
			ex.printStackTrace();
			System.exit(1);
		}

		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE orders ( O_ID int, O_C_ID int, O_DATE date, O_SUB_TOTAL double Precision, O_TAX double Precision, O_TOTAL double Precision, O_SHIP_TYPE varchar(10), O_SHIP_DATE date, O_BILL_ADDR_ID int, O_SHIP_ADDR_ID int, O_STATUS varchar(15), PRIMARY KEY(O_ID))");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table ORDERS");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create table: ORDERS");
			ex.printStackTrace();
			System.exit(1);
		}

		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE SEQUENCE ORDERS_SEQ minvalue 1 nomaxvalue start with 1 increment by 1 nocycle nocache");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table ORDERS'S SEQUENCE");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create sequence: ORDERS");
			ex.printStackTrace();
			System.exit(1);
		}
		
		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE shopping_cart ( SC_ID  int, SC_TIME timestamp, PRIMARY KEY(SC_ID))");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table SHOPPING_CART");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create table: SHOPPING_CART");
			ex.printStackTrace();
			System.exit(1);
		}
		
		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE SEQUENCE SHOPPING_CART_SEQ minvalue 1 nomaxvalue start with 1 increment by 1 nocycle nocache");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table SHOPPING_CART'S SEQUENCE");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create sequence: SHOPPING_CART");
			ex.printStackTrace();
			System.exit(1);
		}
		
		try {
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE shopping_cart_line ( SCL_SC_ID int not null, SCL_QTY int, SCL_I_ID int not null, PRIMARY KEY(SCL_SC_ID, SCL_I_ID))");
			statement.executeUpdate();
			con.commit();
			System.out.println("Created table SHOPPING_CART_LINE");
		} catch (java.lang.Exception ex) {
			System.out.println("Unable to create table: SHOPPING_CART_LINE");
			ex.printStackTrace();
			System.exit(1);
		}

		System.out.println("Done creating tables!");

	}

	// UTILITY FUNCTIONS BEGIN HERE
	private static String getRandomAString(int min, int max) {
		String newstring = new String();
		int i;
		final char[] chars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
				'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C',
				'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '@', '#', '$', '%', '^', '&', '*', '(',
				')', '_', '-', '=', '+', '{', '}', '[', ']', '|', ':', ';', ',', '.', '?', '/',
				'~', ' ' }; // 79
		// characters
		int strlen = (int) Math.floor(rand.nextDouble() * ((max - min) + 1));
		strlen += min;
		for (i = 0; i < strlen; i++) {
			char c = chars[(int) Math.floor(rand.nextDouble() * 79)];
			newstring = newstring.concat(String.valueOf(c));
		}
		return newstring;
	}

	private static String getRandomAString(int length) {
		String newstring = new String();
		int i;
		final char[] chars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
				'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C',
				'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '@', '#', '$', '%', '^', '&', '*', '(',
				')', '_', '-', '=', '+', '{', '}', '[', ']', '|', ':', ';', ',', '.', '?', '/',
				'~', ' ' }; // 79
		// characters
		for (i = 0; i < length; i++) {
			char c = chars[(int) Math.floor(rand.nextDouble() * 79)];
			newstring = newstring.concat(String.valueOf(c));
		}
		return newstring;
	}

	private static int getRandomNString(int num_digits) {
		int return_num = 0;
		for (int i = 0; i < num_digits; i++) {
			return_num += getRandomInt(0, 9) * (int) java.lang.Math.pow(10.0, (double) i);
		}
		return return_num;
	}

	private static int getRandomNString(int min, int max) {
		int strlen = (int) Math.floor(rand.nextDouble() * ((max - min) + 1)) + min;
		return getRandomNString(strlen);
	}

	private static int getRandomInt(int lower, int upper) {

		int num = (int) Math.floor(rand.nextDouble() * ((upper + 1) - lower));
		if (num + lower > upper || num + lower < lower) {
			System.out.println("ERROR: Random returned value of of range!");
			System.exit(1);
		}
		return num + lower;
	}

	private static String DigSyl(int D, int N) {
		int i;
		String resultString = new String();
		String Dstr = Integer.toString(D);

		if (N > Dstr.length()) {
			int padding = N - Dstr.length();
			for (i = 0; i < padding; i++)
				resultString = resultString.concat("BA");
		}

		for (i = 0; i < Dstr.length(); i++) {
			if (Dstr.charAt(i) == '0')
				resultString = resultString.concat("BA");
			else if (Dstr.charAt(i) == '1')
				resultString = resultString.concat("OG");
			else if (Dstr.charAt(i) == '2')
				resultString = resultString.concat("AL");
			else if (Dstr.charAt(i) == '3')
				resultString = resultString.concat("RI");
			else if (Dstr.charAt(i) == '4')
				resultString = resultString.concat("RE");
			else if (Dstr.charAt(i) == '5')
				resultString = resultString.concat("SE");
			else if (Dstr.charAt(i) == '6')
				resultString = resultString.concat("AT");
			else if (Dstr.charAt(i) == '7')
				resultString = resultString.concat("UL");
			else if (Dstr.charAt(i) == '8')
				resultString = resultString.concat("IN");
			else if (Dstr.charAt(i) == '9')
				resultString = resultString.concat("NG");
		}

		return resultString;
	}
}
