package marcovModel;


import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import distributions.Distribution;
import distributions.EnumeratedDistribution;
import distributions.Observation;



public class HMM {
	private double a[][];
	private double pi[];
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
		for(double x:pi)
			piValue+=(x + ",");

		for(int x = 0; x < numStates; x++)
			for(int y = 0; y < numStates;y++)
				aValue+=(a[x][y] + ",");

		return "HMM [a=" + aValue+"]" + ", pi=" +piValue+"]"
		+  ", numStates=" + numStates + "]";
	}
	public HMM(double[][] a, double[] pi
			,ArrayList<Distribution> epsilon) {
		this.a = a;
		this.pi = pi;
		this.b = epsilon;
		this.numStates=pi.length;
		if(epsilon.get(0) instanceof EnumeratedDistribution)
			this.numEmision = ((EnumeratedDistribution)epsilon.get(0)).getP().size();
		this.numMixtureComponents = b.get(0).getNumMixtures();
	}
	/**
	 * @return the a
	 */
	public double getA(int x, int y) {
		return a[x][y];
	}
	/**
	 * @param a the a to set
	 */
	public void setA(double[][] a) {
		this.a = a;
	}
	/**
	 * @return the pi
	 */
	public double getPi(int x) {
		return pi[x];
	}
	public double pdf(int x,Observation y) {
		
		return b.get(x).pdf(y);
	}
	public double pdf(int i,int m,Observation y) {
		return b.get(i).pdf(y,m);
	}
	/**
	 * @param tpi the pi to set
	 */
	public void setPi(double[] tpi) {
		this.pi = tpi;
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
	public void normalizeA(double[] sumA) {
		for(int i=0;i<numStates;i++)
			for(int j=0;j<numStates;j++){
				a[i][j]=a[i][j] / (sumA[i]);
			}
	}
	public int getNumMixtureComponents() {
		return numMixtureComponents;
	}
	public void setNumMixtureComponents(int numMixtureComponents) {
		this.numMixtureComponents = numMixtureComponents;
	}
	public void normalize(double[] sumA, double[] sumB) {
		for(int i = 0; i < numStates; i++)
			for(int j = 0; j < numStates; j++){
				a[i][j] = a[i][j] / (sumA[i]);
			}
		for(int i = 0; i < numStates; i++)
			for(Distribution x : b){
				EnumeratedDistribution y = ((EnumeratedDistribution)x);
				y.Normalize(sumB[i]);
			}	
	}


}
