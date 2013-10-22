package src;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MixTest {

	Mix mix;
	@Before
	public void setUp() throws Exception {
		mix=new Mix();
		Mix.initialize();
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testGetTransProb() {
		assertEquals(mix.getTransProb("shopping")[1][7],9952);
		assertEquals(mix.getTransProb("browsing")[1][7],9877);
		assertEquals(mix.getTransProb("ordering")[1][7],8348);
	}

}
