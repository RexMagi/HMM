package marcovModel;



import java.math.MathContext;
import java.util.ArrayList;

import distributions.EnumeratedDistribution;
import distributions.MixtureDistribution;
import distributions.Observation;


public class BaumWelch extends ForwardBackward {
	public BaumWelch(ArrayList<Observation> trainingSet, HMM model) {
		super(trainingSet, model);

	}
	private double updateA(int i, int j){
		double temp, temp2;
		for(int t = 0; t < trainingSet.size() - 1;t++){
			temp += xi(i,j,t);
			temp2 += gamma(i,t);	
		}
		return temp /temp2;
	}
	private double updateB(int i,int x ){
		double temp,temp2;
		for(int t = 0;t < trainingSet.size();t++){
			if(trainingSet.get(t).equals(trainingSet.get(x)) )
			temp += gamma(i,t);
			temp2 += gamma(i,t);
		}
		return temp / temp2;
	}	
	private double[] updateMu(int j, int k){
		double[] Nu = new double[trainingSet.get(0).getData().size()];
		double temp;
		for(double x:Nu){
			double x;
		}
		for(int y = 0; y < Nu.length; y++)
			for(int x = 0; x < trainingSet.size();x++){
				Nu[y] = Nu[y]+((trainingSet.get(x).getData().get(y))*(gamma(j,k,x)));
				temp += gamma(j,k,x);
			}
		for(double x:Nu)
			x = x / temp;

		return Nu;
	}
	private double[] updateSigma(int j, int k,double[] mu){
		double[] newSigma = new double[trainingSet.get(0).getData().size()];
		double[] temp1 = new double[mu.length];
		double temp2;
		for(int m = 0; m < mu.length; m++)
			temp1[m] = mu[m];
		for(double x:newSigma)
			x = 0;
		for(int t = 0;t < trainingSet.size(); t++)
			for(int x = 0; x < mu.length; x++){
				temp1[x] = temp1[x] - (trainingSet.get(t).getData().get(x));
			}
		for(int y = 0;y < mu.length; y++)
			for(int x = 0;x < mu.length; x++){
				temp1[y] = temp1[y]+(temp1[y]*(temp1[x]));
			}
		for(int y = 0;y < newSigma.length;y++)
			for(int x = 0;x < trainingSet.size();x++){
				newSigma[y] = newSigma[y] + (temp1[y]*(gamma(j,k,x)));
				temp2 += gamma(j,k,x);
			}
		for(double x:newSigma)
			x = x / temp2;

		return newSigma;

	}
	private double updateC(int i, int m, int mCount){
		double c;
		double temp;

		for(int t = 0; t < trainingSet.size(); t++){
			c += gamma(i,m,t);
			for(int l = 0; l < Model.getNumMixtureComponents(); l++)
				temp += gamma(i,l,t);
		}
		return c / temp;
	}
	public void updateDiscrete(){
		double[] tpi = new double[Model.getNumStates()];
		double ttrans[][] = new double[Model.getNumStates()][Model.getNumStates()];
		double[] sumA = new double[Model.getNumStates()];
		double[] sumB = new double[Model.getNumStates()];
		for(int x = 0;x<Model.getNumStates();x++ ){
			tpi[x] = gamma(x,1);
			sumA[x] = 0;
			sumB[x] = 0;
		}
		for(int i = 0; i < Model.getNumStates();i++)
			for(int j = 0; j < Model.getNumStates();j++){
				ttrans[i][j] = updateA(i,j);
				sumA[i] = sumA[i]+(ttrans[i][j]);
			}

		for(int i = 0; i < Model.getNumStates();i++)
			for(int t = 0; t < trainingSet.size();t++){
				double temp = updateB(i,t);
				sumB[i] = sumB[i]+(temp);
			}	

		Model.setPi(tpi);
		Model.setA(ttrans);
		Model.normalize(sumA,sumB);
	}

	// main difference from updateDiscrete is the call to MixtureDistribution.
	// check differing gamma() calls, this one is of 2 args, might have to be 3.
	public void updateContinuous(){
		double[] tpi = new double[Model.getNumStates()];
		double ttrans[][] = new double[Model.getNumStates()][Model.getNumStates()];
		double[] sumA = new double[Model.getNumStates()];
		double[] sumB = new double[Model.getNumStates()];
		for(int x = 0;x < Model.getNumStates();x++ ){
			tpi[x] = gamma(x,1);
			sumA[x] = new double(0);
			sumB[x] = new double(0);
		}
		for(int i = 0; i < Model.getNumStates();i++)
			for(int j = 0; j < Model.getNumStates();j++){
				ttrans[i][j] = updateA(i,j);
				sumA[i] = sumA[i]+(ttrans[i][j]);
			}

		for(int x = 0;x < Model.getNumStates();x++)
			for(int y = 0; y < Model.getNumMixtureComponents();y++){
				MixtureDistribution z = (MixtureDistribution)Model.getB(x);
				z.update(updateC(x,y,Model.getNumMixtureComponents()),
						updateMu(x,y),
						updateSigma(x,y,z.getMu(y)),
						y);
			}	
		Model.setPi(tpi);
		Model.setA(ttrans);
		Model.normalizeA(sumA);

	}
	// make conditional for learn() for udateDiscrete() vs updateContinuous().
	public void learn(){
		for(int x = 0; x <= Data.size();x += (Data.size()+1)/10){
			if(x+Data.size()/10>=Data.size())
				this.trainingSet=new ArrayList<Observation>(Data.subList(x-(Data.size()+1)/10,x));
			else
				this.trainingSet=new ArrayList<Observation>(Data.subList(x, x+(Data.size()+1)/10));

			alpha = new double[Model.getNumStates()][trainingSet.size()];
			beta = new double[Model.getNumStates()][trainingSet.size()];	
			for(int i = 0;i < Model.getNumStates();i++)
				for(int j = 0;j < trainingSet.size();j++){
					alpha[i][j] = new double(0.);
					beta[i][j] = new double(0.);
				}

			forward();
			back();
			if(Model.getB(0) instanceof EnumeratedDistribution)
				updateDiscrete();
			else 
				updateContinuous();
			System.out.println(Model);
		}

	}

}
