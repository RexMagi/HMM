package marcovModel;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;

import distributions.Distribution;
import distributions.CategoricalDistribution;
import distributions.MixtureDistribution;
import distributions.Observation;


public class BaumWelch extends ForwardBackward {
	 BigDecimal[] sumA = new BigDecimal[Model.getNumStates()];//hold the total probability of going from state i to j used to average so that all sets sum to one
	 BigDecimal[] sumB = new BigDecimal[Model.getNumStates()];//holds the total probability of observing all the symbols used to average so that all sets sum to one
	 BigDecimal ttrans[][];
	 ArrayList <Distribution> b;
	 static int q = 0;
	PrintWriter states;
	PrintWriter LikelyHood;

	public BaumWelch(HMM model){
		super(null, model);


	}

	public BaumWelch(ArrayList<Observation> trainingSet, HMM model) {
		super(trainingSet, model);
		q++;

		try {
			states = new PrintWriter("StatesT"+q+".txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}



	}
	public BaumWelch(ArrayList<Observation> trainingSet, HMM model,char x) {
		super(trainingSet, model);


	}

	public void init(){

		alpha = new BigDecimal[Model.getNumStates()][trainingSet.size()];
		beta = new BigDecimal[Model.getNumStates()][trainingSet.size()];	

		for(int i = 0;i < Model.getNumStates();i++)
			for(int t = 0;t < trainingSet.size();t++){
				alpha[i][t] = new BigDecimal(0.);
				beta[i][t] = new BigDecimal(0.);

			}		
	}

	public double inferance(){
		BigDecimal risk ;
		double tally = 0;
		init();
		Thread forward = new Thread(this);
		Thread back = new Thread(this);
		job = 0;

		forward.start();;
		try {
			Thread.sleep(1);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		job = 1;

		back.start();

		try {
			forward.join();
			back.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int t = 0; t < trainingSet.size(); t++){
			risk = new BigDecimal(0);
			for (int i = 0;i < Model.getNumStates(); i++)
				risk = risk.add(gamma(i,t)).multiply(BigDecimal.valueOf(i/2.));
			if(risk.compareTo(BigDecimal.valueOf(.5)) == 1)
				tally++;
		}
		return tally/this.trainingSet.size();
	}


	//updates the probability form going form state i to j
	//equation 
	//a_i_j = expected number of transitions form state i to j/ expected number of transitions from state i
	private void updateA(){
		for(int i = 0; i < Model.getNumStates();i++)
			for(int j = 0; j < Model.getNumStates();j++){
				BigDecimal num = new BigDecimal(0.);
				for(int t = 0; t < trainingSet.size() - 1 ;t++){
					num = num.add(xi[i][j][t]);
				}
				ttrans[i][j] =num.divide(gammaSum[i].subtract(gammaDiscrete[i][trainingSet.size() - 1]),MathContext.DECIMAL128);
				sumA[i] = sumA[i].add(ttrans[i][j]);
			}

	}
	//updates the probability of observing x in state i 
	//Equation
	//b_i(V_k) = the expected number of times in state i and observing symbol v_k/the expected number of times in state i  
	private void updateBDiscrte(){
		for(int i = 0; i < Model.getNumStates();i++){
			HashMap<Observation,BigDecimal> x = new HashMap<Observation,BigDecimal>();
			for(Observation vk: ((CategoricalDistribution)Model.getB(i)).getObservations()){
				BigDecimal num = new BigDecimal(0.);
				for(int t = 0;t < trainingSet.size()  ;t++)
					if(trainingSet.get(t).equals(vk))
						num = num.add(this.gammaDiscrete[i][t]);

				x.put(vk,num.divide(gammaSum[i],MathContext.DECIMAL128) ) ;
				sumB[i] = sumB[i].add(x.get(vk));
			}
			b.add(new CategoricalDistribution(x));
		}

	}
	private BigDecimal[] updateMu(int j, int k){
		BigDecimal[] Nu = new BigDecimal[trainingSet.get(0).getData().size()];
		BigDecimal temp = new BigDecimal(0);
		for(int y=0;y<Nu.length;y++){
			Nu[y] = new BigDecimal(0);}
		for(int y=0;y<Nu.length;y++)
			for(int x=0;x<trainingSet.size() - 1;x++){
				Nu[y] = Nu[y].add(BigDecimal.valueOf(
						trainingSet.get(x).getData().get(y)).multiply(gamma(j,k,x)));
				temp = temp.add(gammaContinuous[j][k][x]);
			}

		for(BigDecimal x:Nu)
			x = x.divide(temp,MathContext.DECIMAL128);

		return Nu;
	}

	private BigDecimal[] updateSigma(int j, int k,BigDecimal[] mu){
		BigDecimal[] newSigma = new BigDecimal[trainingSet.get(0).getData().size()];
		BigDecimal[] temp1 = new BigDecimal[mu.length];
		BigDecimal temp2 = new BigDecimal(0);
		for(int m = 0;m < mu.length;m++)
			temp1[m] = mu[m].multiply(new BigDecimal(1));
		for(int y = 0;y < newSigma.length;y++)
			newSigma[y] = new BigDecimal(0);
		for(int t = 0;t < trainingSet.size();t++)
			for(int x = 0;x < mu.length;x++){
				temp1[x] = temp1[x].subtract(BigDecimal.valueOf(
						trainingSet.get(t).getData().get(x)));
			}
		for(int y = 0;y < mu.length;y++)
			for(int x = 0;x < mu.length;x++){
				temp1[y] = temp1[y].add(temp1[y].multiply(temp1[x]));
			}
		for(int y = 0;y < newSigma.length;y++)
			for(int x = 0;x < trainingSet.size();x++){
				newSigma[y] = newSigma[y].add(temp1[y].multiply(gamma(j,k,x)));
				temp2 = temp2.add(gamma(j,k,x));
			}
		for(BigDecimal x:newSigma)
			x = x.divide(temp2,MathContext.DECIMAL128);

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
		return c.divide(temp,MathContext.DECIMAL128);
	}
	//updates each of the major properties of the hidden Markov model 

	public void updateDiscrete(){
		BigDecimal[] tpi = new BigDecimal[Model.getNumStates()];//holds the temporary pi
		ttrans = new BigDecimal[Model.getNumStates()][Model.getNumStates()];//holds the temporary transition matrix 
		b = new ArrayList<Distribution>();//holds the temporary emission functions
		//sets pi for all states to the probability of observing the first element in the training sequence 
		//initializes the normalizing coefficient for all states 
		for(int x = 0;x < Model.getNumStates();x++ ){
			tpi[x] =this.gammaDiscrete[x][0];
			sumA[x] = new BigDecimal(0);
			sumB[x] = new BigDecimal(0);
		}
		//updates the transition matrix

		job = 4;

		Thread trans = new Thread(this);

		trans.start();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//updates the transition matrix 
		job = 5;

		Thread emit = new Thread(this);

		emit.start();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Model.setPi(tpi);
		Model.setA(ttrans);
		Model.setB(b);
		Model.normalize(sumA,sumB);
	}
	// main difference from updateDiscrete is the call to MixtureDistribution.
	// check differing gamma() calls, this one is of 2 args, might have to be 3.
	public void updateContinuous(){
		BigDecimal[] tpi = new BigDecimal[Model.getNumStates()];
		ttrans = new BigDecimal[Model.getNumStates()][Model.getNumStates()];

		for(int x = 0;x < Model.getNumStates();x++ ){
			tpi[x] = gamma(x,0);
			sumA[x] = new BigDecimal(0);
			sumB[x] = new BigDecimal(0);
		}

		job = 4;
		Thread trans = new Thread(this);
		trans.start();


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

	public void learn(int itr){
		for(int x = 0;x < itr; x++){
			alpha = new BigDecimal[Model.getNumStates()][trainingSet.size()];
			beta = new BigDecimal[Model.getNumStates()][trainingSet.size()];	
			gammaSum = new BigDecimal[Model.getNumStates()];
			gammaDiscrete = new BigDecimal[Model.getNumStates()][trainingSet.size()];
			gammaContinuous =  new BigDecimal[Model.getNumStates()][trainingSet.size()][Model.getNumMixtureComponents()];
			xi = new BigDecimal[Model.getNumStates()][Model.getNumStates()][trainingSet.size()];
			alphaTimesBeta  = new BigDecimal[trainingSet.size()];

			for(int i = 0;i < Model.getNumStates();i++)
				for(int t = 0;t < trainingSet.size();t++){
					alpha[i][t] = new BigDecimal(0.);
					beta[i][t] = new BigDecimal(0.);
					gammaSum[i] = new BigDecimal(0.);
					alphaTimesBeta[t] = new BigDecimal(0.);
				}

			job = 0;

			Thread forward = new Thread(this);

			forward.start();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			job = 1;
			Thread back = new Thread(this);
			back.start();

			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			try {
				forward.join();
				back.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			job = 2;
			denomSum();
			Thread gamma = new Thread(this);
			gamma.start();
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			job = 3;

			Thread xi = new Thread(this);
			xi.start();

			try {
				gamma.join();
				xi.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			if(Model.getB(0) instanceof CategoricalDistribution)
				updateDiscrete();
			else 
				updateContinuous();

			//LikelyHood.println(((logLikelyHood.doubleValue())) );
			states.println(Model);
			System.out.println(Model);
		}
		states.flush();
		states.close();
	}

	@Override
	public void run() {

		super.run();		
		switch(job){
		case 4:
			updateA();
			break;
		case 5:
			updateBDiscrte();
			break;

		}
	}

}

