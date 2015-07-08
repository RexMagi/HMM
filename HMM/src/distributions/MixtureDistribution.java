package distributions;

import java.math.BigDecimal;

public class MixtureDistribution extends Distribution {
	BigDecimal C[];
	Function[] s;
	@Override
	// sum of the mixture states
	public BigDecimal pdf(Observation x) {
		BigDecimal sumO = null; 
		s[1].eta(x);
		return sumO;
	}

	@Override
	// product of a specific observation and an integer
	public BigDecimal pdf(Observation x, int m) {
		BigDecimal scaleO = x * C[m]; 
		return scaleO;
	}

	public BigDecimal[] getMu(int y) {
		// TODO Auto-generated method stub
		return null;
	}
	public BigDecimal[] getSigma(int y) {
		return null; 
	}

	public void update(BigDecimal updateC, BigDecimal[] updateMu,
			BigDecimal[] updateSigma) {
		// TODO Auto-generated method stub
		
	}

}
