package distributions;

import java.math.BigDecimal;

public abstract class Distribution {
	int numMixtures;
	/**
	 * @return the numMixtures
	 */
	public int getNumMixtures() {
		return numMixtures;
	}
	/**
	 * @param numMixtures the numMixtures to set
	 */
	public void setNumMixtures(int numMixtures) {
		this.numMixtures = numMixtures;
	}
	public abstract BigDecimal pdf(Observation x);
	public abstract BigDecimal pdf(Observation x,int m);
	
}
