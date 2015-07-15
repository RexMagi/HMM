package distributions;

import java.math.BigDecimal;



public class MixtureDistribution extends Distribution {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5450593777253743240L;
	BigDecimal C[];//mixture coefficient 
	Function[] s;//univariate probability distribution functions
	
	public MixtureDistribution(BigDecimal[] c, Function[] s) {
		super();
		C = c;
		this.s = s;
		this.numMixtures = s.length;
	}
	@Override
	// sum of the mixture states
	public BigDecimal pdf(Observation x) {
		BigDecimal sumO = new BigDecimal(0); 
		for(int m = 0 ; m < s.length; m++){
			sumO =sumO.add(s[m].eta(x).multiply(C[m]));
		}
		return sumO;
	}
	@Override
	// product of a specific observation and mixture coefficient 
	public BigDecimal pdf(Observation x, int m) {
		 
		return s[m].eta(x).multiply(C[m]);
	}
	//Returns the mean of all the pdfs as an array
	public BigDecimal[] getMu(int y) {
		 BigDecimal[] x=  {s[y].getMu()}; 
		return x ;
	}
	//updates the parameters of the Mixture Distribution
	public void update(BigDecimal updateC, BigDecimal[] updateMu,
			BigDecimal[] updateSigma, int m) {
		C[m] = updateC;
		s[m].setMu(updateMu[0]);
		s[m].setSigma(updateSigma[0]);
		
	}

}
