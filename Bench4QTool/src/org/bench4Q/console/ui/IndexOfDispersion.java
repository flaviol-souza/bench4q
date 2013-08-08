package org.bench4Q.console.ui;

public class IndexOfDispersion {
	
	double m_lambda_short;
	double m_lambda_long;
	double m_IndexOfDispersion;
	double Exp = 7000.0;


	double p_l_to_s;
	double p_s_to_l;
	double I_value;
	int m_f=1;
	
	public IndexOfDispersion(double lambda_short, double lambda_long, double IndexOfDispersion){
		m_lambda_short = lambda_short;
		m_lambda_long = lambda_long;
		m_IndexOfDispersion = IndexOfDispersion;
	}
	
	public void init(){
		
		
		
		while(true){
			m_lambda_short = m_lambda_short / m_f;
			m_lambda_long = m_lambda_long * m_f;
			
			p_s_to_l = Math.random();
			
			double dif = (m_lambda_long - Exp)
			/ (Exp - m_lambda_short);
			p_l_to_s = p_s_to_l * dif;
			
			double n = (1/m_lambda_short - 1/m_lambda_long)
			* (1/m_lambda_short - 1/m_lambda_long);
			
			double Numerator = 2 * p_s_to_l * p_l_to_s
					* n;
			double Denominator = (p_s_to_l + p_l_to_s)
					* ( p_s_to_l / m_lambda_short +  p_l_to_s / m_lambda_long)
					* ( p_s_to_l / m_lambda_short +  p_l_to_s / m_lambda_long);
			
			I_value = 1 + Numerator / Denominator;
			double delta = m_IndexOfDispersion - I_value;

			p_s_to_l = 2
					* p_s_to_l
					* dif
					* n
					/ (2 * dif * n + p_s_to_l * delta * (1 + dif)
							* Math.pow(1/m_lambda_short + dif / m_lambda_long, 2));
			
			p_l_to_s = p_s_to_l  * dif;
			
			if(p_s_to_l < 0 || p_s_to_l > 1 || p_l_to_s < 0 || p_l_to_s > 1)
			{
				m_f++;
				System.out.println(m_f);
				continue;
			}
			
			
			
			double ex2 = 2 * (p_l_to_s * Math.pow(m_lambda_short, 2)
					/ (p_l_to_s + p_s_to_l) + p_s_to_l
					* Math.pow(m_lambda_long, 2) / (p_l_to_s + p_s_to_l));
			double rho = 0.5 * (1 - p_l_to_s - p_s_to_l)
					* (1 - Math.pow(Exp, 2) / (ex2 - Math.pow(Exp, 2)));
			
			if(rho >= 0.4){
				Numerator = 2 * p_s_to_l * p_l_to_s
				* n;
				Denominator = (p_s_to_l + p_l_to_s)
				* ( p_s_to_l / m_lambda_short +  p_l_to_s / m_lambda_long)
				* ( p_s_to_l / m_lambda_short +  p_l_to_s / m_lambda_long);
				I_value = 1 + Numerator / Denominator;
				System.out.println(rho + "	" + I_value);
				break;
				
			}
				
			m_f++;
			System.out.println(m_f);
		}
		
		
		
	}
	public double getP_l_to_s() {
		return p_l_to_s;
	}

	public void setP_l_to_s(double pLToS) {
		p_l_to_s = pLToS;
	}

	public double getP_s_to_l() {
		return p_s_to_l;
	}

	public void setP_s_to_l(double pSToL) {
		p_s_to_l = pSToL;
	}

	public int getM_f() {
		return m_f;
	}

	public void setM_f(int mF) {
		m_f = mF;
	}
}
