package org.bench4Q.agent.rbe;

import static org.junit.Assert.*;

import org.junit.Test;

public class NormalDistributionTest {

	@Test
	public void testNormalDistribution() {
		NormalDistribution ND = new NormalDistribution(4,1,2);
		int count = 10;
		int num = 0;
		while(count > 0 ){
			double m = ND.nextNormNum();
			count--;		
			System.out.println(m);
			if(m < 0)
				num++;
		}
		System.out.println(num);
	}

	@Test
	public void testGetNormNum() {
		fail("Not yet implemented");
	}

}
