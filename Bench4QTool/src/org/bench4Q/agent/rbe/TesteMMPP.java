package org.bench4Q.agent.rbe;

public class TesteMMPP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MMPP mmpp_tt = new MMPP();

		for (int i = 0; i < 100; i++) {

			/****************************************************
			 * generate the user think times from a 2-state MAP e.g., the MAP
			 * has mean = 7second, so r=1000*mean(ms)
			 ***************************************************/
			// long r = rbe.negExp(rand, 7000L, 0.36788, 70000L, 4.54e-5,
			// 7000.0);
			double r = mmpp_tt.gen_interval();

			r = (long) 50 / (r); // tt_scale = 1 if think=7second

			System.out.println("MMPP: " + (long) r);

		}

	}

}
