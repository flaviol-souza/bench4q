package org.bench4Q.agent.rbe;

import java.util.Random;

public class NormalDistribution {
	private Random rand;
	private double m_mean;
	private double m_dev1;
	private double m_dev2;
	private double U1;
	private double U2;
	private double R;
	private double sita;
	
	
	public NormalDistribution(double mean, double deviation1, double deviation2){
		rand = new Random();
		m_mean = mean;
		m_dev1 = deviation1;
		m_dev2 = deviation2;
	}
	
	public double nextNormNum(){
		U1 = rand.nextDouble();
		U2 = rand.nextDouble();
		
		R = Math.sqrt(-2 * Math.log(U2));
		sita = 2 * Math.PI * U1;
		
		double result = R * Math.cos(sita);
		
		if(result < 0)
			result = m_mean + result * m_dev1;
		else {
			result = m_mean + result * m_dev2;
		}
		
		return result;
	}
	

}
