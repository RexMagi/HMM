package marcovModel;


import java.math.BigDecimal;
import java.util.ArrayList;


public class BaumWelch<O> extends ForwardBackward<O> {

	public BaumWelch(ArrayList<O> trainningSet, HMM model) {
		super(trainningSet, model);

	}
	private BigDecimal updateA(int i, int j){
		BigDecimal temp=new BigDecimal(0),temp2=new BigDecimal(0);
		for(int x=0;x<trainingSet.size()-1;x++){
			temp=temp.add(b(i,j,x));
			
			temp2=temp2.add(gamma(i,x));
			
		}
		return temp.divide(temp2,BigDecimal.ROUND_HALF_EVEN);
	}
	private BigDecimal updateB(int i,int t ){
		BigDecimal temp=new BigDecimal(0),temp2=new BigDecimal(0);;
		for(int x=0;x<trainingSet.size();x++){
			if(trainingSet.get(x).equals(trainingSet.get(t)) )
				temp=temp.add(gamma(i,x));
			temp2=temp2.add(gamma(i,x));
		}
		return temp.divide(temp2,BigDecimal.ROUND_HALF_EVEN);
	}
	public void updateDiscrete(){
		BigDecimal[] tpi=new BigDecimal[Model.getNumStates()];
		BigDecimal ttrans[][]=new BigDecimal[Model.getNumStates()][Model.getNumStates()];

		for(int x = 0;x<Model.getNumStates();x++ )
			tpi[x]=gamma(x,1);

		for(int x=0;x<Model.getNumStates();x++)
			for(int y=0;y<Model.getNumStates();y++){
				ttrans[x][y]=updateA(x,y);
			}	
		for(int x=0;x<Model.getNumStates();x++)
			for(int y=0;y<trainingSet.size();y++){
				Model.updateEpsilon(x,trainingSet.get(y),updateB(x,y));
			}	
		Model.setPi(tpi);
		Model.setA(ttrans);
	}
	//Continuous 
	public void updateContinuous(){
		BigDecimal[] tpi=new BigDecimal[Model.getNumStates()];
		for(int x = 0;x<=Model.getNumStates();x++ )
			tpi[x]=gamma(x,1);
		BigDecimal ttrans[][]=new BigDecimal[Model.getNumStates()][Model.getNumStates()];
		for(int x=0;x<Model.getNumStates();x++)
			for(int y=0;y<Model.getNumStates();y++){
				ttrans[x][y]=updateA(x,y);
			}
		//fuck if i know how to do this
		for(int x=0;x<Model.getNumStates();x++)
			for(int y=0;y<trainingSet.size()-1;y++){
				Model.updateEpsilon();
			}	
		Model.setPi(tpi);
		Model.setA(ttrans);

	}
	@SuppressWarnings("unchecked")
	public void learn(){
		for(int x=0;x<=Data.size();x+=(Data.size()+1)/10){
		
			if(x+Data.size()/10>=Data.size())
				this.trainingSet=new ArrayList(Data.subList(x-(Data.size()+1)/10,x));
			else
				this.trainingSet=new ArrayList(Data.subList(x, x+(Data.size()+1)/10));
			alpha=new BigDecimal[Model.getNumStates()][trainingSet.size()];
			beta=new BigDecimal[Model.getNumStates()][trainingSet.size()];	
			for(int i =0;i<Model.getNumStates();i++)
				for(int j=0;j<=trainingSet.size()-1;j++){
					alpha[i][j] =new BigDecimal(0);
					beta[i][j] =new BigDecimal(0);
				}

			forward();
			back();
			updateDiscrete();
			System.out.println(Model);
		}

	}

}
