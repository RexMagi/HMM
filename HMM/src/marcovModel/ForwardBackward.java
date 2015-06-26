package marcovModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;


public class ForwardBackward<O>  {
	BigDecimal alpha[][];//should be set to [number of states][size of training set-1]
	BigDecimal beta[][];//should be set to [number of states][size of training set-1]
	ArrayList<O> trainingSet;
	ArrayList<O>  Data;
	HMM Model;
	public ForwardBackward(ArrayList<O> trainingSet, HMM model) {

		this.Data = trainingSet;
		Model = model;
	}
	public void forward(){
		
		//loops through all states and caches alpha values
		for(int i=0;i<Model.getNumStates();i++)
			for(int x=0;x<trainingSet.size();x++){
				if(x==0){
					//sets alpha at time 1 to pi for that state times the propability of
					//y1 given state i
					alpha[i][x]=Model.getPi(i).multiply
							(Model.getEpsilon(i,trainingSet.get(x)));
				}else {
					//sets alpha for state i at time x to  emmison for state i 
					//for observation x times the sum of the previous alpha in all states times the 
					//probibility all preveous states leads to the current state
					alpha[i][x]=Model.getEpsilon(i,trainingSet.get(x)).multiply(
							AlphaTrans(i,x-1));
				}
				
			} 
	
	
	}
	public void back(){
		//loops through all states and caches the beta values for those states
		for(int i=0;i<Model.getNumStates();i++)
			for(int x=trainingSet.size()-1;x>=0;x--){
				//sets the beta value to 1 for the final state
				if (x == trainingSet.size()-1 ){
					beta[i][x]=new BigDecimal(1);
				}
				else{
					//sets beta for state i at time x to 
					//the sum of all the next beta values for all states
					//times the probility all states transition to this state
					//times the probility that all states emmit the next observed data
					beta[i][x]=TransEpslon(i,x+1);		
				}
	
		}
		
		
	
	}
	public void appendObservation(O x){
		Data.add(x);
		BigDecimal temp[][]=new BigDecimal[Model.getNumStates()][Data.size()];
		
		for(int y=0; y<Model.getNumStates();y++)
			for(int s=0;s<Data.size()-1;s++)
				temp[y][s]=alpha[y][s];
		for(int y=0; y<Model.getNumStates();y++)
		alpha[y][Data.size()-1] = Model.getEpsilon(y,Data.get(Data.size()-1)).multiply(
				AlphaTrans(y,Data.size()-2));
		
		alpha=temp;
	}
	public BigDecimal AlphaTrans(int x,int train){
		BigDecimal temp = new BigDecimal(0);
		for(int y = 0;y<Model.getNumStates();y++){

			temp=temp.add(alpha[y][train].
					multiply(Model.getA(y,x)));
		}
		return temp;
	}
	public BigDecimal TransEpslon(int i,int t){
		BigDecimal temp=new BigDecimal(0);
		for(int y=0;y<Model.getNumStates();y++){
			temp=temp.add(beta[y][t].multiply(
					Model.getA(i,y).multiply(
							Model.getEpsilon(y,trainingSet.get(t)))));
		}
		return temp;}
	public BigDecimal AlphaBeta(int t){
		BigDecimal temp=new BigDecimal(0);
		//sums alpha time beta for all states given t
		for(int x=0;x<Model.getNumStates();x++){
			temp=temp.add(alpha[x][t].multiply(beta[x][t]));
		}
		return temp;
	}
	public BigDecimal gamma(int i,int t){
		BigDecimal temp = alpha[i][t].multiply(beta[i][t]);
		temp=temp.divide(AlphaBeta(t),MathContext.DECIMAL32);
		return temp;
	}
	public BigDecimal AlphaTransEpBeta(int t){
		BigDecimal temp = new BigDecimal(0);
		for(int i = 0; i <Model.getNumStates(); i++)
			for(int j = 0; j < Model.getNumStates(); j++){
				temp=temp.add(alpha[i][t].multiply(
						Model.getA(i,j).multiply(
								Model.getEpsilon(j,trainingSet.get(t+1)).multiply(
										beta[j][t+1]))));
			}return temp;
	}
	public BigDecimal b(int i,int j,int t){
		BigDecimal temp=alpha[i][t].multiply(Model.getA(i,j).multiply(beta[j][t+1].multiply(
				Model.getEpsilon(j,trainingSet.get(t+1)))));
		BigDecimal adder=AlphaTransEpBeta(t);
		return temp.divide(adder,MathContext.DECIMAL32);
	}

}
