package distributions;

import java.math.BigDecimal;

public abstract class Function {

	BigDecimal mu;
	BigDecimal sigma;
	public abstract BigDecimal pdf(BigDecimal x);
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
