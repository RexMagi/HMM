package marcovModel;


import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import distributions.MixtureDistribution;
import distributions.Observation;


public class BaumWelch extends ForwardBackward {
	public BaumWelch(ArrayList<Observation> trainingSet, HMM model) {
		super(trainingSet, model);

	}
	private BigDecimal updateA(int i, int j){
		BigDecimal temp=new BigDecimal(0.),temp2=new BigDecimal(0.);
		for(int t=0;t<trainingSet.size()-1;t++){
			temp=temp.add(xi(i,j,t));
			temp2=temp2.add(gamma(i,t));	
		}
		return temp.divide(temp2,MathContext.DECIMAL32);
	}
	private BigDecimal updateB(int i,int x ){
		BigDecimal temp=new BigDecimal(0.),temp2=new BigDecimal(0.);;
		for(int t = 0;t < trainingSet.size();t++){
			if(trainingSet.get(t).equals(trainingSet.get(x)) )
				temp=temp.add(gamma(i,t));
			temp2=temp2.add(gamma(i,t));
		}
		return temp.divide(temp2,MathContext.DECIMAL32);
	}	
	private BigDecimal[] updateMu(int j, int k){
		BigDecimal[] Nu = new BigDecimal[trainingSet.get(0).getData().size()];
		BigDecimal temp = new BigDecimal(0);
		for(BigDecimal x:Nu){
			x = new BigDecimal(0);}
		for(int y=0;y<Nu.length;y++)
			for(int x=0;x<trainingSet.size();x++){
				Nu[y] = Nu[y].add(BigDecimal.valueOf(
						trainingSet.get(x).getData().get(y)).multiply(gamma(j,k,x)));
				temp = temp.add(gamma(j,k,x));
			}
		for(BigDecimal x:Nu)
			x = x.divide(temp,MathContext.DECIMAL32);

		return Nu;
	}
	private BigDecimal[] updateSigma(int j, int k,BigDecimal[] mu){
		BigDecimal[] newSigma = new BigDecimal[trainingSet.get(0).getData().size()];
		BigDecimal[] temp1 =new BigDecimal[mu.length];
		BigDecimal temp2 = new BigDecimal(0);
		for(int v=0;v<mu.length;v++)
			temp1[v]=mu[v].multiply(new BigDecimal(1));
		for(BigDecimal x:newSigma)
			x = new BigDecimal(0);
		for(int t=0;t<trainingSet.size();t++)
			for(int x=0;x<mu.length;x++){
				temp1[x] = temp1[x].subtract(BigDecimal.valueOf(
						trainingSet.get(t).getData().get(x)));
			}
		for(int y=0;y<mu.length;y++)
			for(int x=0;x<mu.length;x++){
				temp1[y] = temp1[y].add(temp1[y].multiply(temp1[x]));
			}
		for(int y=0;y<newSigma.length;y++)
			for(int x=0;x<trainingSet.size();x++){
				newSigma[y] = newSigma[y].add(temp1[y].multiply(gamma(j,k,x)));
				temp2 = temp2.add(gamma(j,k,x));
			}
		for(BigDecimal x:newSigma)
			x = x.divide(temp2,MathContext.DECIMAL32);

		return newSigma;

	}
	private BigDecimal updateC(int i, int m, int mCount){
		BigDecimal c = new BigDecimal(0);
		BigDecimal temp = new BigDecimal(0);

		for(int t = 0; t < trainingSet.size(); t++){
			c = c.add(gamma(i,m,t));
			for(int l = 0; l < Model.getNumMixtureComponents(); l++)
				temp = temp.add(gamma(i,l,t));
		}
		return c.divide(temp,MathContext.DECIMAL32);
	}
	public void updateDiscrete(){
		BigDecimal[] tpi=new BigDecimal[Model.getNumStates()];
		BigDecimal ttrans[][]=new BigDecimal[Model.getNumStates()][Model.getNumStates()];
		BigDecimal[] sumA = new BigDecimal[Model.getNumStates()];
		BigDecimal[] sumB = new BigDecimal[Model.getNumStates()];
		for(int x = 0;x<Model.getNumStates();x++ ){
			tpi[x]=gamma(x,1);
			sumA[x] = new BigDecimal(0);
			sumB[x] = new BigDecimal(0);
		}
		for(int i=0;i<Model.getNumStates();i++)
			for(int j=0;j<Model.getNumStates();j++){
				ttrans[i][j]=updateA(i,j);
				sumA[i]=sumA[i].add(ttrans[i][j]);
			}

		for(int i=0;i<Model.getNumStates();i++)
			for(int t=0;t<trainingSet.size();t++){
				BigDecimal temp=updateB(i,t);
				sumB[i]=sumB[i].add(temp);

			}	

		Model.setPi(tpi);
		Model.setA(ttrans);
		Model.normalize(sumA,sumB);
	}
	
	// main difference from updateDiscrete is the call to MixtureDistribution.
	// check differing gamma() calls, this one is of 2 args, might have to be 3.
	public void updateContinuous(){
		BigDecimal[] tpi=new BigDecimal[Model.getNumStates()];
		BigDecimal ttrans[][]=new BigDecimal[Model.getNumStates()][Model.getNumStates()];
		BigDecimal[] sumA = new BigDecimal[Model.getNumStates()];
		BigDecimal[] sumB = new BigDecimal[Model.getNumStates()];
		for(int x = 0;x<Model.getNumStates();x++ ){
			tpi[x]=gamma(x,1);
			sumA[x] = new BigDecimal(0);
			sumB[x] = new BigDecimal(0);
		}
		for(int i=0;i<Model.getNumStates();i++)
			for(int j=0;j<Model.getNumStates();j++){
				ttrans[i][j]=updateA(i,j);
				sumA[i]=sumA[i].add(ttrans[i][j]);
			}

		for(int x = 0;x < Model.getNumStates();x++)
			for(int y = 0; y < Model.getNumMixtureComponents();y++){
				MixtureDistribution z = (MixtureDistribution)Model.getB(x);
				z.update(updateC(x,y,Model.getNumMixtureComponents()),
						updateMu(x,y),updateSigma(x,y,z.getMu(y)));
			}	
		Model.setPi(tpi);
		Model.setA(ttrans);
		Model.normalizeA(sumA);

	}
	// make conditional for learn() for udateDiscrete() vs updateContinuous().
	public void learn(){
		for(int x=0;x<=Data.size();x+=(Data.size()+1)/10){
			if(x+Data.size()/10>=Data.size())
				this.trainingSet=new ArrayList<Observation>(Data.subList(x-(Data.size()+1)/10,x));
			else
				this.trainingSet=new ArrayList<Observation>(Data.subList(x, x+(Data.size()+1)/10));

			alpha=new BigDecimal[Model.getNumStates()][trainingSet.size()];
			beta=new BigDecimal[Model.getNumStates()][trainingSet.size()];	
			for(int i =0;i<Model.getNumStates();i++)
				for(int j=0;j<trainingSet.size();j++){
					alpha[i][j] =new BigDecimal(0.);
					beta[i][j] =new BigDecimal(0.);
				}

			forward();
			back();
			updateDiscrete();
			System.out.println(Model);
		}

	}

}
