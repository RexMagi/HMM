package distributions;

import java.math.BigDecimal;


public abstract class Function implements java.io.Serializable{

	/**
	 * class used to generalize a variety of univariate probability distribution functions 
	 */
	private static final long serialVersionUID = 528226415651885251L;//Id used to generate object file for serialization
	BigDecimal mu;//the mean of the function
	BigDecimal sigma;// the standard deviation of the function
	//Returns the probability of observing x
	public abstract BigDecimal eta(Observation x);
	public Function(BigDecimal mu, BigDecimal sigma) {
		super();
		this.mu = mu;
		this.sigma = sigma;
	}
	/**
	 * @return the mu
	 */
	public BigDecimal getMu() {
		return mu;
	}
	/**
	 * @param mu the mu to set
	 */
	public void setMu(BigDecimal mu) {
		this.mu = mu;
	}
	/**
	 * @return the sigma
	 */
	public BigDecimal getSigma() {
		return sigma;
	}
	/**
	 * @param sigma the sigma to set
	 */
	public void setSigma(BigDecimal sigma) {
		this.sigma = sigma;
	}
	
	
}
