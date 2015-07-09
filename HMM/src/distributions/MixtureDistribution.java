package distributions;

import java.math.BigDecimal;

public class MixtureDistribution extends Distribution {
	BigDecimal C[];
	Function[] s;
	
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
			sumO =sumO.add(	s[m].eta(x).multiply(C[m]));
		}
		return sumO;
	}
	@Override
	// product of a specific observation and an integer
	public BigDecimal pdf(Observation x, int m) {
		 
		return s[m].eta(x).multiply(C[m]);
	}
	public BigDecimal[] getMu(int y) {
		 BigDecimal[] x=  {s[y].getMu()}; 
		return x ;
	}
	public void update(BigDecimal updateC, BigDecimal[] updateMu,
			BigDecimal[] updateSigma, int m) {
		C[m] = updateC;
		s[m].setMu(updateMu[0]);
		s[m].setSigma(updateSigma[m]);
		
	}

}
