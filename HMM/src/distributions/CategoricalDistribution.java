package distributions;


import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Set;

public class CategoricalDistribution extends Distribution {
	/**
	 * An implementation of a multivariate binomial distribution 
	 * 
	 */
	private static final long serialVersionUID = -7850650208150283858L;//Id used to generate object file for serialization

	/**
	 * @return String view of the probabilities of each category 
	 * 
	 */
	@Override
	public String toString() {
		String dVals = "";
		for(Observation x:p.keySet())
			dVals+=String.valueOf((char)(x.getData().get(0).doubleValue()))+
			":"+p.get(x).doubleValue()+" ";
		return "[p=" + dVals + "]";
	}

	HashMap<Observation,BigDecimal> p;//hashmap that stores the probabilities of Observing category x
	/**
	 * @param hashmap that stores the probabilities of Observing category x
	 * 	creates instance of CategoricalDistribution
	 * */
	public CategoricalDistribution(HashMap<Observation, BigDecimal> p) {
		super();
		this.p = p;
		this.numMixtures = p.keySet().iterator().next().data.size();
	}

	@Override
	/**
	 * @returns the probability of observing category x
	 */
	public BigDecimal pdf(Observation x) {
		return p.get(x);
	}
	/**
	 * updates the probability of observing category y
	 * 
	 * */
	public void update(Observation y,BigDecimal x){
		p.remove(y);
		p.put(y, x);
	}
	/**
	 * Normalizes all the categories so that they all sum to one
	 * 
	 * */
	public void Normilize(BigDecimal sumB){
		HashMap<Observation,BigDecimal> p = new HashMap<Observation,BigDecimal>() ;
		for(Observation x:this.p.keySet())
		p.put(x, this.p.get(x).divide(sumB,MathContext.DECIMAL128));
		this.p = p;
	}

	/**
	 * @return the hashmap that stores probability of observing category y
	 */
	public HashMap<Observation, BigDecimal> getP() {
		return p;
	}

	/**
	 * changes the current probability table to x
	 * @param A hashmap that stores probability of observing category y
	 */
	public void setP(HashMap<Observation, BigDecimal> x) {
		this.p = x;
	}

	@Override
	public BigDecimal pdf(Observation x, int m) {
		return null;
	}
//returns  an iterator of the categories in the table
	public Set<Observation> getObservations() {
		return p.keySet();
	}
	

}
