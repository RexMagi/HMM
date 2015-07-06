package distributions;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;

public class EnumeratedDistribution extends Distribution {
	HashMap<Observation,BigDecimal> p;
	
	public EnumeratedDistribution(HashMap<Observation, BigDecimal> p) {
		super();
		this.p = p;
		String x;
	}

	@Override
	public BigDecimal pdf(Observation x) {
		// TODO Auto-generated method stub
		return p.get(x);
	}
	public void update(Observation Y,BigDecimal x){
		p.remove(Y);
		p.put(Y, x);
	}
	public void Normilize(BigDecimal sumB){
		HashMap<Observation,BigDecimal> p = new HashMap<Observation,BigDecimal>() ;
		for(Observation x:this.p.keySet())
		p.put(x, this.p.get(x).divide(sumB,MathContext.DECIMAL32));
		this.p = p;
	}

	/**
	 * @return the p
	 */
	public HashMap<Observation, BigDecimal> getP() {
		return p;
	}

	/**
	 * @param p the p to set
	 */
	public void setP(HashMap<Observation, BigDecimal> p) {
		this.p = p;
	}

	@Override
	public BigDecimal pdf(Observation x, int m) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
