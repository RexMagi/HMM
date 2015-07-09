package marcovModel;

import java.math.MathContext;
import java.util.ArrayList;

import distributions.Observation;


public class ForwardBackward  {
	double alpha[][];//should be set to [number of states][size of training set-1]
	double beta[][];//should be set to [number of states][size of training set-1]
	double gamma[][];
	double xi[][][];
	ArrayList<Observation> trainingSet;
	ArrayList<Observation>  Data;
	HMM Model;
	double sum[];

	public ForwardBackward(ArrayList<Observation> Observation, HMM model) {

		this.Data = Observation;
		Model = model;


	}
	public void forward(){
		sum = new double[trainingSet.size()];
		for(int x = 0; x < trainingSet.size(); x++)
			sum[x] = 0;
		//loops through all states and caches alpha values
		for(int i = 0;i < Model.getNumStates();i++)
			for(int t = 0;t < trainingSet.size();t++){
				if(t == 0){
					//sets alpha at time 1 to pi for that state times the probability of
					//y1 given state i
					alpha[i][t] = Model.getPi(i)*(Model.pdf(i,trainingSet.get(t)));
				}else {	
					
					double temp = 0;
					for(int j = 0; j < Model.getNumStates(); j++){
						temp += (alpha[j][t-1]*(Model.getA(j,i)));
					}
					//sets alpha for state i at time x to  emmison for state i 
					//for observation x times the sum of the previous alpha in all states times the 
					//probibility all preveous states leads to the current state
					alpha[i][t] = Model.pdf(i,trainingSet.get(t))*(temp);
				}
				System.err.println(trainingSet.get(t));
				sum[t] = sum[t] + (alpha[i][t]);				
			} 
		for(int i = 0; i < Model.getNumStates();i++){
			for(int t = 0; t < trainingSet.size();t++){
				
				alpha[i][t] = alpha[i][t] / sum[t];
			}
		}
	}
	public void back(){

		//loops through all states and caches the beta values for those states
		for(int i=0;i<Model.getNumStates();i++)
			for(int t=trainingSet.size()-1;t>=0;t--){
				//sets the beta value to 1 for the final state
				if (t == trainingSet.size()-1 ){
					beta[i][t]= 1;
				}
				else{
					double temp = 0;
					for(int j=0;j<Model.getNumStates();j++){
						temp= temp + (beta[j][t+1]*(Model.getA(i,j)
								*(Model.pdf(j,trainingSet.get(t+1)))));
					}
					//sets beta for state i at time x to 
					//the sum of all the next beta values for all states
					//times the probility all states transition to this state
					//times the probility that all states emmit the next observed data
					beta[i][t]=temp;		
				}

			}

		for(int i=0;i<Model.getNumStates();i++)
			for(int t=0;t<trainingSet.size();t++){
				beta[i][t]=beta[i][t]/sum[t];
			}
	}
	public void gamma(){}
	public void xi(){}
	public double gamma(int i,int t){
		double temp = alpha[i][t].multiply(beta[i][t]);
		double temp2=new double(0.);
		//sums alpha time beta for all states given t
		for(int x=0;x<Model.getNumStates();x++){
			temp2=temp2.add(alpha[x][t].multiply(beta[x][t]));
		}

		return temp.divide(temp2,MathContext.DECIMAL32);
	}
	public double gamma(int i, int j ,int t){
		double temp = alpha[i][t].multiply(beta[i][t]);
		double temp2 = new double(0); 
		double temp3 = Model.pdf(i,j,trainingSet.get(t));
		double temp4=new double(0); 
		//sums alpha time beta for all states given t
		for(int x=0;x<Model.getNumStates();x++){
			temp2=temp2.add(alpha[x][t].multiply(beta[x][t]));

		}
		for(int x=0;x<Model.getNumMixtureComponents();x++)
			temp4=temp4.add(Model.pdf(i,x,trainingSet.get(t)));

		temp= temp.divide(temp2,MathContext.DECIMAL32);
		temp3=temp3.divide(temp4,MathContext.DECIMAL32);



		return temp.multiply(temp3);
	}
	public double xi(int i,int j,int t){
		double temp2 = new double(0.);
		double temp=alpha[i][t].multiply(
				Model.getA(i,j).multiply(
						beta[j][t+1].multiply(
								Model.pdf(j,trainingSet.get(t+1)))));

		for(int i2 = 0; i2 <Model.getNumStates(); i2++)
			for(int j2 = 0; j2 < Model.getNumStates(); j2++){
				temp2=temp2.add(alpha[i2][t].multiply(
						Model.getA(i2,j2).multiply(
								Model.pdf(j2,trainingSet.get(t+1)).multiply(
										beta[j2][t+1]))));
			}

		return temp.divide(temp2,MathContext.DECIMAL32);
	}

}
