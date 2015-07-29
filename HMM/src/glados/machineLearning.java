package glados;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;

import marcovModel.BaumWelch;
import marcovModel.HMM;
import distributions.CategoricalDistribution;
import distributions.Distribution;
import distributions.Observation;

public class machineLearning {
	public static ArrayList<Observation> dstArr = new ArrayList<Observation>();
	public static ArrayList<Observation> srcArr = new ArrayList<Observation>();
	public static ArrayList<Observation> tlArr = new ArrayList<Observation>();
	public static ArrayList<Observation> portDstArr = new ArrayList<Observation>();
	public static ArrayList<Observation> portSrcArr = new ArrayList<Observation>();
	//	static PcapReader Bad = new PcapReader("C:\\Users\\Lab User\\Downloads\\Bad.pcap");
	//static PcapReader both = new PcapReader("C:\\Users\\Lab User\\Downloads\\Mix.pcap");

	public static void main(String[] args) throws IOException {


		File Maps;
		ArrayList<Distribution> distributionSet = new ArrayList<Distribution>();
		HMM Bot =null;
		BaumWelch teacher;
		BigDecimal [][] a = {
				{new BigDecimal(0.6,MathContext.DECIMAL32),new BigDecimal(0.25,MathContext.DECIMAL32),new BigDecimal(0.15,MathContext.DECIMAL32)},
				{new BigDecimal(0.35,MathContext.DECIMAL32),new BigDecimal(0.5,MathContext.DECIMAL32),new BigDecimal(0.15,MathContext.DECIMAL32)},
				{new BigDecimal(0.15,MathContext.DECIMAL32),new BigDecimal(0.33,MathContext.DECIMAL32),new BigDecimal(0.55,MathContext.DECIMAL32)}
		};
		
		HashMap<Observation,BigDecimal> Table;
		BigDecimal [] pi = {new BigDecimal(0.7),new BigDecimal(0.2),new BigDecimal(.1)};
		String x[] ={"stuffs500"};
		
		System.out.println("Starting to learn "+x[0]);
	
			int r = 3; 
			PcapReader Good = new PcapReader("C:\\Users\\Lab User\\Desktop\\"+x[0] +".pcap");
			//PcapReader Good = new PcapReader("C:\\Users\\trae\\Downloads\\"+x[s] +".pcap");
			Good.loop(500);
			orgTypes(Good.getPacketData());
			Good.packetData=null;
//			Bot = reLoad(r);
//		
//				//sets up the distribution for dstArr and than has it learn and write results to file 
				Table = new HashMap<Observation,BigDecimal>();
				distributionSet = new ArrayList<Distribution>();
				Table.put(new Observation(1),BigDecimal.valueOf(.5));
				Table.put(new Observation(2),BigDecimal.valueOf(.5));

				distributionSet.add(new CategoricalDistribution(Table));
				distributionSet.add(new CategoricalDistribution(Table));
				distributionSet.add(new CategoricalDistribution(Table));
				Bot = new HMM(a.clone(),pi.clone(),distributionSet);
			
			teacher = new BaumWelch(srcArr,Bot);

			teacher.learn(100);

			Maps = new File(x[0]+(r++)+".txt");
			try
			{
				FileOutputStream fileOut =
						new FileOutputStream(Maps);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(Bot);
				out.close();
				fileOut.close();
				System.out.println("Serialized data is saved ");
			}catch(IOException i)
			{
				i.printStackTrace();
			}

//			Bot = reLoad(r);
		
				//sets up the distribution for srcArr and than has it learn and write results to file 
				Table = new HashMap<Observation,BigDecimal>(); 
				distributionSet = new ArrayList<Distribution>();
				Table.put(new Observation(1),BigDecimal.valueOf(.5));
				Table.put(new Observation(2),BigDecimal.valueOf(.5));

				distributionSet.add(new CategoricalDistribution(Table));
				distributionSet.add(new CategoricalDistribution(Table));
				distributionSet.add(new CategoricalDistribution(Table));
				Bot = new HMM(a.clone(),pi.clone(),distributionSet);
			

			teacher = new BaumWelch(dstArr,Bot);
			teacher.learn(100);

			Maps = new File(x[0]+(r++)+".txt");
			try
			{
				FileOutputStream fileOut =
						new FileOutputStream(Maps);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(Bot);
				out.close();
				fileOut.close();
				System.out.println("Serialized data is saved ");
			}catch(IOException i)
			{
				i.printStackTrace();
			}

////			Bot = reLoad(r);
	
//				//sets up the distribution for portDstArr and than has it learn and write results to file 
//				Table = new HashMap<Observation,BigDecimal>(); 
//				distributionSet = new ArrayList<Distribution>();
//				for(int y = 0; y <= 3000;y++)
//					Table.put(new Observation(y),BigDecimal.valueOf(1./3000));
//				//BigDecimal[] C={new BigDecimal(.5),new BigDecimal(.5)};
//				//Function[] F={new Gaussian(new BigDecimal(20),new BigDecimal(300)),
//				//		new Gaussian(new BigDecimal(1280),new BigDecimal(350))};
//				//MixtureDistribution wave =new MixtureDistribution(C,F);
//
//				//	distributionSet.add(wave);
//				//	distributionSet.add(wave);
//				//	distributionSet.add(wave);
//
//				distributionSet.add(new CategoricalDistribution(Table));
//				distributionSet.add(new CategoricalDistribution(Table));
//				distributionSet.add(new CategoricalDistribution(Table));
//				Bot = new HMM(a.clone(),pi.clone(),distributionSet);
//			
//
//			teacher = new BaumWelch(tlArr,Bot);
//			teacher.learn(100);
//
//			Maps = new File(x[0]+(r++)+".txt");
//			try
//			{
//				FileOutputStream fileOut =
//						new FileOutputStream(Maps);
//				ObjectOutputStream out = new ObjectOutputStream(fileOut);
//				out.writeObject(Bot);
//				out.close();
//				fileOut.close();
//				System.out.println("Serialized data is saved ");
//			}catch(IOException i)
//			{
//				i.printStackTrace();
//			}
//
//			Bot = reLoad(r);
		
				//sets up the distribution for portSrcArr and than has it learn and write results to file 
				Table = new HashMap<Observation,BigDecimal>(); 
				distributionSet = new ArrayList<Distribution>();
				Table.put(new Observation(1),BigDecimal.valueOf(.333));
				Table.put(new Observation(2),BigDecimal.valueOf(.333));
				Table.put(new Observation(3),BigDecimal.valueOf(.333));

				distributionSet.add(new CategoricalDistribution(Table));
				distributionSet.add(new CategoricalDistribution(Table));
				distributionSet.add(new CategoricalDistribution(Table));
				Bot = new HMM(a.clone(),pi.clone(),distributionSet);
				teacher = new BaumWelch(portSrcArr,Bot);
			
			teacher.learn(100);

			Maps = new File(x[0]+(r++)+".txt");
			try
			{
				FileOutputStream fileOut =
						new FileOutputStream(Maps);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(Bot);
				out.close();
				fileOut.close();
				System.out.println("Serialized data is saved ");
			}catch(IOException i)
			{
				i.printStackTrace();
			}

//			Bot = reLoad(r);
			
				//sets up the distribution for tlArr and than has it learn and write results to file 
				Table = new HashMap<Observation,BigDecimal>(); 
				distributionSet = new ArrayList<Distribution>();
				Table.put(new Observation(1),BigDecimal.valueOf(.333));
				Table.put(new Observation(2),BigDecimal.valueOf(.333));
				Table.put(new Observation(3),BigDecimal.valueOf(.333));

				distributionSet.add(new CategoricalDistribution(Table));
				distributionSet.add(new CategoricalDistribution(Table));
				distributionSet.add(new CategoricalDistribution(Table));
				Bot = new HMM(a.clone(),pi.clone(),distributionSet);
			

			teacher = new BaumWelch(portDstArr,Bot);
			teacher.learn(100);


			Maps = new File(x[0]+(r++)+".txt");
			try
			{
				FileOutputStream fileOut =
						new FileOutputStream(Maps);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(Bot);
				out.close();
				fileOut.close();
				System.out.println("Serialized data is saved ");
			}catch(IOException i)
			{
				i.printStackTrace();
			}


		
		System.out.println("learning Complete");

	}
	public static void orgTypes(ArrayList<ArrayList<Integer>>  arr) {		

		for (int i = 0; i < arr.size() ; i++) {
			try{

				dstArr.add(new Observation((Integer) arr.get(i).get(0)));
				srcArr.add(new Observation((Integer) arr.get(i).get(1)));
				tlArr.add(new Observation(
						(int) arr.get(i).get(2)));
				portDstArr.add(new Observation(
						((Integer) arr.get(i).get(3)).intValue()));
				portSrcArr.add(new Observation (
						((Integer) arr.get(i).get(4)).intValue()));
				
			}catch(IndexOutOfBoundsException  e){

				arr.remove(i);
			}
		}


	}
//	public static HMM reLoad(int x){
//		HMM temp = null;
//		try{
//			FileInputStream fileIn = new FileInputStream(x[0]+x+".txt");
//			ObjectInputStream in = new ObjectInputStream(fileIn);
//			temp = (HMM) in.readObject();
//			in.close();
//			fileIn.close();
//		}catch(IOException|ClassNotFoundException i)
//		{
//			return temp;
//		}
//		return temp;
//
//	}
}
