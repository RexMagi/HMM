package marcovModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

import distributions.Observation;


public class ForwardBackward  {
	BigDecimal alpha[][];//should be set to [number of states][size of training set-1]
	BigDecimal beta[][];//should be set to [number of states][size of training set-1]
	ArrayList<ArrayList<BigDecimal>> memoryArr;
	ArrayList<Observation> trainingSet;//the set being iterated over
	ArrayList<Observation>  Data;//the total set of observations
	HMM Model;//the hidden markov model
	BigDecimal c[];//the scaling coefficient used to scale the alpha and beta values when they might become to small 
	BigDecimal logLikelyHood;//used to store how likely the given sequence is logged to improve scale 
	public ForwardBackward(ArrayList<Observation> Observation, HMM model) {
		this.Data = Observation;
		Model = model;
	}
	public void forward(){
		c = new BigDecimal[trainingSet.size()];	
		//loops through all states and caches alpha values
		for(int t = 0;t < trainingSet.size() ;t++)
			for(int i = 0;i < Model.getNumStates();i++)	{
				if(c[t] == null){
					c[t] = new BigDecimal(0);
				}
				if( t!= 0){
					logLikelyHood = logLikelyHood.add(c[t-1],MathContext.DECIMAL128);

					//alpha[i][t-1] = alpha[i][t-1].divide(c[t-1],MathContext.DECIMAL128);
				}
				if(t == 0){
					//sets alpha at time 1 to pi for that state times the probability of
					//y1 given state i
					alpha[i][t] = Model.getPi(i).multiply
							(Model.pdf(i,trainingSet.get(t)));
				}else {	
					BigDecimal temp = new BigDecimal(0.);
					for(int j = 0;j < Model.getNumStates();j++){
						temp = temp.add(alpha[j][t-1].multiply(Model.getA(j,i),MathContext.DECIMAL128));
					}
					//sets alpha for state i at time x to  emission for state i 
					//for observation x times the sum of the previous alpha in all states times the 
					//Probability all previous states leads to the current state
					alpha[i][t] = Model.pdf(i,trainingSet.get(t)).multiply(
							temp);
				}

				c[t] = c[t].add(alpha[i][t]);	
			} 
		//for(int i = 0;i < Model.getNumStates();i++)
		//alpha[i][trainingSet.size() - 1 ] = alpha[i][trainingSet.size() - 1]
		//	.divide(c[trainingSet.size() - 1],MathContext.DECIMAL128);
	}
	public void back(){
		//loops through all states and caches the beta values for those states
		for(int t = trainingSet.size() - 1;t >= 0;t--)
			for(int i = 0;i < Model.getNumStates();i++){
				//if( t!= trainingSet.size()-1)
				//beta[i][t + 1]=beta[i][t + 1].divide(c[t + 1],MathContext.DECIMAL128);	
				//sets the beta value to 1 for the final state
				if (t == trainingSet.size()-1 ){
					beta[i][t] = new BigDecimal(1);//BigDecimal.valueOf(1).divide(
					//c[t],MathContext.DECIMAL128);
				}
				else{
					BigDecimal temp = new BigDecimal(0.);
					for(int j = 0;j < Model.getNumStates();j++){
						temp = temp.add(beta[j][t+1].multiply(
								Model.getA(i,j).multiply(
										Model.pdf(j,trainingSet.get(t+1)),MathContext.DECIMAL128),MathContext.DECIMAL128));
					}
					//sets beta for state i at time x to 
					//the sum of all the next beta values for all states
					//times the probility all states transition to this state
					//times the probility that all states emmit the next observed data
					beta[i][t] = temp;	
				}
			}
		//	for(int i = 0;i < Model.getNumStates();i++)
		//	beta[i][0] = beta[i][0]
		//		.divide(c[0],MathContext.DECIMAL128);

	}
	//public void gamma(){}
	//public void xi(){}
	public BigDecimal gamma(int i,int t){
		BigDecimal temp = alpha[i][t].multiply(beta[i][t]);
		BigDecimal temp2 = new BigDecimal(0.);
		//sums alpha time beta for all states given t
		for(int x = 0;x < Model.getNumStates();x++){
			temp2=temp2.add(alpha[x][t].multiply(beta[x][t]));
		}
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

	public void alpha(Observation x){
		ArrayList<BigDecimal> memAlphas = new ArrayList<BigDecimal>();
		if (memoryArr == null) {
			memoryArr = new ArrayList<ArrayList<BigDecimal>>();
			//sets alpha at time 1 to pi for that state times the probability of
			//y1 given state i
			for (int i = 0; i < 3; i++) 
				memAlphas.add( Model.getPi(i).multiply(Model.pdf(i,x)));
		}else {	
			BigDecimal temp = new BigDecimal(0.);
			for(int i = 0; i < 3; i++) {
				for(int j = 0;j < Model.getNumStates();j++){
					temp = temp.add(memAlphas.get(memAlphas.size() - 1)
							.multiply(Model.getA(j,i)));
				}
				memAlphas.add( Model.pdf(i,x).multiply(temp));
			}
			//sets alpha for state i at time x to  emission for state i 
			//for observation x times the sum of the previous alpha in all states times the 
			//Probability all previous states leads to the current state

		}
		memoryArr.add(memAlphas);
	}
	public ArrayList<BigDecimal> getLastMemArr() {
		return memoryArr.get(memoryArr.size() - 1);
	}
}
