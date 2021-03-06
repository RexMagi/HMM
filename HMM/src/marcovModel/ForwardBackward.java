package marcovModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import distributions.MixtureDistribution;
import distributions.Observation;



public class ForwardBackward implements Runnable  {
	 BigDecimal alpha[][];//should be set to [number of states][size of training set-1]
	 BigDecimal beta[][];//should be set to [number of states][size of training set-1]
	 BigDecimal gammaDiscrete[][];//should be set to [number of states][size of training set-1]
	 BigDecimal gammaContinuous[][][];
	 BigDecimal xi[][][];//should be set to [number of states][size of training set-1]
	 int job;
	 BigDecimal gammaSum[];
	 BigDecimal alphaTimesBeta[];
	 ArrayList<Observation> trainingSet;//the set being iterated over
	 HMM Model;//the hidden markov model


	public ForwardBackward(ArrayList<Observation> Observation, HMM model) {
		this.trainingSet = Observation;
		Model = model;
	}
	
	public void forward(){
		//loops through all states and caches alpha values
		for(int t = 0;t < trainingSet.size() ;t++)
			for(int i = 0;i < Model.getNumStates();i++)	{
				if(t == 0){
					//sets alpha at time 1 to pi for that state times the probability of
					//y1 given state i
					//System.out.println(trainingSet.get(t));
					//System.out.println(Model.pdf(i,trainingSet.get(t)));
					alpha[i][t] = Model.getPi(i).multiply
							(Model.pdf(i,trainingSet.get(t)));
				}else {	
					BigDecimal temp = new BigDecimal(0);
					for(int j = 0;j < Model.getNumStates();j++){
						temp = temp.add(alpha[j][t-1].multiply(Model.getA(j,i)));
					}
					//sets alpha for state i at time x to  emission for state i 
					//for observation x times the sum of the previous alpha in all states times the 
					//Probability all previous states leads to the current state
//					System.out.println(t);
//					System.out.println(alpha[0].length);
//					System.out.println(beta[0].length);
//					System.out.println(i);
//					System.out.println(trainingSet.size()); 
//					System.out.println("------------------------");
//					
//					System.out.println("------------------------");
//					System.out.println(alpha.length);
//					System.out.println(alpha[i].length);
//					System.out.println("------------------------");
//					System.out.println(alpha[i][t]);
//					System.out.println(trainingSet.get(t));
					alpha[i][t] = Model.pdf(i,trainingSet.get(t)).multiply(
							temp);
					
				}

			} 

	}
	
	public void back(){
		//loops through all states and caches the beta values for those states
		for(int t = trainingSet.size() - 1;t >= 0;t--)
			for(int i = 0;i < Model.getNumStates();i++){


				//sets the beta value to 1 for the final state
				if (t == trainingSet.size()-1 ){
					beta[i][t] = new BigDecimal(1);

				}
				else{
					BigDecimal temp = new BigDecimal(0);
					for(int j = 0;j < Model.getNumStates();j++){
						temp = temp.add(beta[j][t+1].multiply(
								Model.getA(i,j).multiply(
										Model.pdf(j,trainingSet.get(t+1)))));
					}
					//sets beta for state i at time x to 
					//the sum of all the next beta values for all states
					//times the probability all states transition to this state
					//times the probability that all states emit the next observed data
					beta[i][t] = temp;	

				}

			}
	}

	public void denomSum(){
		for(int t = trainingSet.size() - 1;t >= 0;t--)
			for(int i = 0;i < Model.getNumStates();i++)
				alphaTimesBeta[t] = alphaTimesBeta[t].add(beta[i][t].multiply(alpha[i][t]));
	}

	public void gamma(){
		for(int i = 0; i < Model.getNumStates();i++)
			for(int t = 0; t < trainingSet.size(); t++){
				gammaDiscrete[i][t] = gamma(i,t); 
				gammaSum[i] = gammaSum[i].add(gamma(i,t));
			}
		if(Model.getB(0) instanceof MixtureDistribution)	
			for(int i = 0; i < Model.getNumStates();i++)
				for(int t = 0; t < trainingSet.size(); t++)
					for(int m = 0; m < Model.getNumMixtureComponents();m++)
						gammaContinuous[i][m][t]= gamma(i,m,t);

	}
	public void xi(){
		for(int i = 0; i < Model.getNumStates();i++)
			for(int t = 0; t < trainingSet.size(); t++)
				for(int j = 0; j < Model.getNumStates();j++)
					xi[i][j][t]= xi(i,j,t);
	}
	public BigDecimal gamma(int i,int t){
		BigDecimal temp = alpha[i][t].multiply(beta[i][t]);
		BigDecimal temp2 = new BigDecimal(0.);
		//sums alpha time beta for all states given t
		for(int x = 0;x < Model.getNumStates();x++){
//		System.out.println(beta[x][t]);
			temp2=temp2.add(alpha[x][t].multiply(beta[x][t]));
		}
		return temp.divide(temp2,MathContext.DECIMAL32);
	}

	public BigDecimal gamma(int i){
		int t =  trainingSet.size() - 1;
		BigDecimal temp = new BigDecimal(0);
		
	
			temp = alpha[i][t];	

		BigDecimal temp2 = new BigDecimal(0.);
		//sums alpha time beta for all states given t
		for(int x = 0;x < Model.getNumStates();x++){
			temp2=temp2.add(alpha[x][t]);
		}

		if(temp.compareTo(BigDecimal.valueOf(0))==0)
			return temp.divide(BigDecimal.valueOf(.000000000001),MathContext.DECIMAL128);
		return temp.divide(temp2,MathContext.DECIMAL128);
	}
	public BigDecimal gamma(int i, int j ,int t){
		BigDecimal temp = alpha[i][t].multiply(beta[i][t]);
		BigDecimal temp2 = new BigDecimal(0); 
		BigDecimal temp3 = Model.pdf(i,j,trainingSet.get(t));
		BigDecimal temp4=new BigDecimal(0); 
		//sums alpha time beta for all states given t
		for(int x=0;x<Model.getNumStates();x++){
			temp2=temp2.add(alpha[x][t].multiply(beta[x][t]));

		}

		for(int x=0;x<Model.getNumMixtureComponents();x++)
			temp4=temp4.add(Model.pdf(i,x,trainingSet.get(t)));

		temp= temp.divide(temp2,MathContext.DECIMAL128);
		temp3=temp3.divide(temp4,MathContext.DECIMAL128);

		return temp.multiply(temp3);
	}
	public BigDecimal xi(int i,int j,int t){
		BigDecimal temp2 = new BigDecimal(0.);
		BigDecimal temp;
		if(t != trainingSet.size() - 1 )
			temp=alpha[i][t].multiply(
					Model.getA(i,j).multiply(
							beta[j][t+1].multiply(
									Model.pdf(j,trainingSet.get(t+1)))));
		else {
			temp=alpha[i][t].multiply(
					Model.getA(i,j));
		}
		/*
		for(int i2 = 0; i2 <Model.getNumStates(); i2++)
			for(int j2 = 0; j2 < Model.getNumStates(); j2++){
				temp2=temp2.add(alpha[i2][t].multiply(
						Model.getA(i2,j2).multiply(
								Model.pdf(j2,trainingSet.get(t+1)).multiply(
										beta[j2][t+1]))));
			}*/

		for(int x=0;x<Model.getNumStates();x++){
			temp2=temp2.add(alpha[x][t].multiply(beta[x][t]));
		}

		//System.out.println(temp.divide(temp2,MathContext.DECIMAL128));

		return temp.divide(temp2,MathContext.DECIMAL128);
	}


//	public void alpha(Observation x){
//		ArrayList<BigDecimal> memAlphas = new ArrayList<BigDecimal>();
//		if (memoryArrAlpha == null) {
//			memoryArrAlpha = new ArrayList<ArrayList<BigDecimal>>();
//			//sets alpha at time 1 to pi for that state times the probability of
//			//y1 given state i
//			for (int i = 0; i < 3; i++) 
//				memAlphas.add( Model.getPi(i).multiply(Model.pdf(i,x)));
//		}
//		else {	
//			BigDecimal temp = new BigDecimal(0.);
//			for(int i = 0; i < 3; i++) {
//				for(int j = 0;j < Model.getNumStates();j++){
//					temp = temp.add(memAlphas.get(memAlphas.size() - 1)
//							.multiply(Model.getA(j,i)));
//				}
//				memAlphas.add( Model.pdf(i,x).multiply(temp));
//			}
//		}
//		memoryArrAlpha.add(memAlphas);
//	}
//
//	public ArrayList<BigDecimal> getLastMemArr() {
//		return memoryArrAlpha.get(memoryArrAlpha.size() - 1);
//	}

	@Override
	public void run() {

		switch(job){
		case 0: 
			forward();
			break;
		case 1:
			back();
			break;
		case 2:
			gamma();
			break;
		case 3:
			xi();
			break;
		}
	}
}
