package distributions;

import java.math.BigDecimal;



public abstract class Distribution implements java.io.Serializable  {
	/**
	 * class used to generalize a variety of probability distribution functions 
	 */
	private static final long serialVersionUID = 5966922397939992548L;//Id used to generate object file for serialization
	int numMixtures;// the number of Mixture states in the Gaussian distribution
	/**
	 * @return the numMixtures
	 */
	public int getNumMixtures() {
		return numMixtures;
	}
	/**
	 * @param number of Mixture states to set numMixtures to
	 */
	public void setNumMixtures(int numMixtures) {
		this.numMixtures = numMixtures;
	}
	//returns the probability of Observing x in a given distribution
	public abstract BigDecimal pdf(Observation x);
	//returns the probability of Observing x in a specified mixture state in a given distribution
	public abstract BigDecimal pdf(Observation x,int m);
	
}
