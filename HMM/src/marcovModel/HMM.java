package marcovModel;


import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import distributions.Distribution;
import distributions.EnumeratedDistribution;
import distributions.Observation;



public class HMM {
	private BigDecimal a[][];
	private BigDecimal pi[];
	private ArrayList<Distribution> b;
	/**
	 * @return the b
	 */
	public ArrayList<Distribution> getB() {
		return b;
	}
	public Distribution getB(int i) {
		return b.get(i);
	}
	/**
	 * @param b the b to set
	 */
	public void setB(ArrayList<Distribution> b) {
		this.b = b;
	}
	private int numStates;
	private int numEmision;
	private int numMixtureComponents;
	@Override
	public String toString() {
		String piValue="[";
		String aValue="[";
		for(BigDecimal x:pi)
			piValue+=(x.doubleValue()+",");

		for(int x =0;x<numStates;x++)
			for(int y=0;y<numStates;y++)
				aValue+=(a[x][y].doubleValue()+",");

		return "HMM [a=" + aValue+"]" + ", pi=" +piValue+"]"
		+  ", numStates=" + numStates + "]";
	}
	public HMM(BigDecimal[][] a, BigDecimal[] pi
			,ArrayList<Distribution> epsilon) {
		this.a = a;
		this.pi = pi;
		this.b = epsilon;
		this.numStates=pi.length;
		if(epsilon.get(0) instanceof EnumeratedDistribution)
			this.numEmision=((EnumeratedDistribution)epsilon.get(0)).getP().size();
		this.numMixtureComponents=b.get(0).getNumMixtures();
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
	public BigDecimal pdf(int x,Observation y) {
		return b.get(x).pdf(y);
	}
	public BigDecimal pdf(int i,int m,Observation y) {
		return b.get(i).pdf(y,m);
	}
	/**
	 * @param pi the pi to set
	 */
	public void setPi(BigDecimal[] pi) {
		this.pi = pi;
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
	public void normalizeA(BigDecimal[] sumA) {
		for(int i=0;i<numStates;i++)
			for(int j=0;j<numStates;j++){
				a[i][j]=a[i][j].divide(sumA[i],MathContext.DECIMAL32);
			}
	}
	public int getNumMixtureComponents() {
		return numMixtureComponents;
	}
	public void setNumMixtureComponents(int numMixtureComponents) {
		this.numMixtureComponents = numMixtureComponents;
	}
	public void normalize(BigDecimal[] sumA, BigDecimal[] sumB) {
		for(int i=0;i<numStates;i++)
			for(int j=0;j<numStates;j++){
				a[i][j]=a[i][j].divide(sumA[i],MathContext.DECIMAL32);
			}
		for(int i=0;i<numStates;i++)
			for(Distribution x:b){
				EnumeratedDistribution y = ((EnumeratedDistribution)x);
				y.Normilize(sumB[i]);
			}	
	}


}
