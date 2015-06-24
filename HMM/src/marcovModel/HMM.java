package marcovModel;


import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.math3.distribution.RealDistribution;



public class HMM {
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String piValue="[";
		String aValue="[";
		for(BigDecimal x:pi)
			piValue+=(x.round(MathContext.DECIMAL32).doubleValue()+",");
		
		for(int x =0;x<numStates;x++)
			for(int y=0;y<numStates;y++)
			aValue+=(a[x][y].round(MathContext.DECIMAL32).doubleValue()+",");
		
		return "HMM [a=" + aValue+"]" + ", pi=" +piValue+"]"
				+  ", numStates=" + numStates + "]";
	}
	private BigDecimal a[][];
	private BigDecimal pi[];
	/**
	 * @return the numEmmison
	 */
	public int getNumEmmison() {
		return numEmmison;
	}
	/**
	 * @param numEmmison the numEmmison to set
	 */
	public void setNumEmmison(int numEmmison) {
		this.numEmmison = numEmmison;
	}
	private ArrayList<HashMap<Object,BigDecimal>> epsilon;
	private int numStates;
	private int numEmmison;
	public HMM(BigDecimal[][] a, BigDecimal[] pi,ArrayList<HashMap<Object,BigDecimal>> epsilon) {
		super();
		this.a = a;
		this.pi = pi;
		this.numStates=pi.length;
		this.epsilon = epsilon;
		this.numEmmison=epsilon.get(0).size();
	}
	/**
	 * @return the a
	 */
	public BigDecimal getA(int x, int y) {
		return a[x][y];
	}
	/**
	 * @param a the a to set
	 */
	public void setA(BigDecimal[][] a) {
		this.a = a;
	}
	/**
	 * @return the pi
	 */
	public BigDecimal getPi(int x) {
		return pi[x];
	}
	/**
	 * @param pi the pi to set
	 */
	public void setPi(BigDecimal[] pi) {
		this.pi = pi;
	}
	/**
	 * @return the epsilon
	 */
	public BigDecimal getEpsilon(int y, Object x) {
		return epsilon.get(y).get(x);
	}
		public HashMap<Object, BigDecimal> getEpsilon(int y) {
			return epsilon.get(y);
		}
	/**
	 * @param epslon the epsilon to set
	 */
	public void setEpsilon(ArrayList<HashMap<Object,BigDecimal>> Epsilon) {
		this.epsilon = Epsilon;
	}
	/**
	 * @return the numStates
	 */
	public int getNumStates() {
		return numStates;
	}
	/**
	 * @param numStates the numStates to set
	 */
	public void setNumStates(int numStates) {
		this.numStates = numStates;
	}
	public void updateEpsilon(){
		
		
		
		
	}
	public void updateEpsilon(int i,Object Y,BigDecimal x){
		
		epsilon.get(i).remove(Y);
		epsilon.get(i).put(Y, x);
		
		
		
	}


}
