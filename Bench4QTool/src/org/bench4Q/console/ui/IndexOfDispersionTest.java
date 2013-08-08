package org.bench4Q.console.ui;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class IndexOfDispersionTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testIndexOfDispersion() {
		IndexOfDispersion IOD = new IndexOfDispersion(5000, 90000, 4000);
		IOD.init();
		System.out.println(IOD.getP_l_to_s());
		System.out.println(IOD.getP_s_to_l());
//		System.out.println(IOD.getM_f());
		
	}

	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetP_l_to_s() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetP_l_to_s() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetP_s_to_l() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetP_s_to_l() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetM_f() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetM_f() {
		fail("Not yet implemented");
	}

}
