package glados;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import marcovModel.BaumWelch;
import marcovModel.HMM;
import distributions.Distribution;
import distributions.CategoricalDistribution;
import distributions.Function;
import distributions.Gaussian;
import distributions.MixtureDistribution;
import distributions.Observation;

public class machineLearning {
	public static ArrayList<Observation> dstArr = new ArrayList<Observation>();
	public static ArrayList<Observation> srcArr = new ArrayList<Observation>();
	public static ArrayList<Observation> tlArr = new ArrayList<Observation>();
	public static ArrayList<Observation> portDstArr = new ArrayList<Observation>();
	public static ArrayList<Observation> portSrcArr = new ArrayList<Observation>();
	static PcapReader Good = new PcapReader("C:\\Users\\Lab User\\Downloads\\Good.pcap");
	static PcapReader Bad = new PcapReader("C:\\Users\\Lab User\\Downloads\\Bad.pcap");
	static PcapReader both = new PcapReader("C:\\Users\\Lab User\\Downloads\\Mix.pcap");
	public static void main(String[] args) throws IOException {

		int r = 0; 
		File Maps;
		ArrayList<Distribution> distributionSet = new ArrayList<Distribution>();
		HMM Bot;
		BaumWelch teacher;
		BigDecimal [][] a = {
				{new BigDecimal(0.6,MathContext.DECIMAL32),new BigDecimal(0.25,MathContext.DECIMAL32),new BigDecimal(0.15,MathContext.DECIMAL32)},
				{new BigDecimal(0.35,MathContext.DECIMAL32),new BigDecimal(0.5,MathContext.DECIMAL32),new BigDecimal(0.15,MathContext.DECIMAL32)},
				{new BigDecimal(0.15,MathContext.DECIMAL32),new BigDecimal(0.33,MathContext.DECIMAL32),new BigDecimal(0.55,MathContext.DECIMAL32)}
		};

		BigDecimal [] pi = {new BigDecimal(0.7,MathContext.DECIMAL32),new BigDecimal(0.2,MathContext.DECIMAL32),new BigDecimal(.1)};

		Good.loop(100000);
		orgTypes(Good.getPacketData());
		Good.packetData=null;

		//sets up the distribution for dstArr and than has it learn and write results to file 
		HashMap<Observation,BigDecimal> Table = new HashMap<Observation,BigDecimal>();
		distributionSet = new ArrayList<Distribution>();
		Table.put(new Observation(1),BigDecimal.valueOf(.5));
		Table.put(new Observation(2),BigDecimal.valueOf(.5));

		distributionSet.add(new CategoricalDistribution(Table));
		distributionSet.add(new CategoricalDistribution(Table));
		distributionSet.add(new CategoricalDistribution(Table));
		Bot = new HMM(a.clone(),pi.clone(),distributionSet);
		teacher = new BaumWelch(srcArr,Bot);
		teacher.learn();
		Maps = new File("HMMG"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

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
		teacher.learn();
		Maps = new File("HMMG"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

		//sets up the distribution for portDstArr and than has it learn and write results to file 
		//Table = new HashMap<Observation,BigDecimal>(); 
		distributionSet = new ArrayList<Distribution>();
		//Table.put(new Observation(1),BigDecimal.valueOf(.333));
		//Table.put(new Observation(2),BigDecimal.valueOf(.333));
		//Table.put(new Observation(3),BigDecimal.valueOf(.333));
		BigDecimal[] C={new BigDecimal(.5),new BigDecimal(.5)};
		Function[] F={new Gaussian(new BigDecimal(20),new BigDecimal(300)),
				new Gaussian(new BigDecimal(1280),new BigDecimal(350))};
		MixtureDistribution wave =new MixtureDistribution(C,F);

		distributionSet.add(wave);
		distributionSet.add(wave);
		distributionSet.add(wave);

		//distributionSet.add(new CategoricalDistribution(Table));
		//distributionSet.add(new CategoricalDistribution(Table));
		//distributionSet.add(new CategoricalDistribution(Table));
		Bot = new HMM(a.clone(),pi.clone(),distributionSet);
		teacher = new BaumWelch(tlArr,Bot);
		teacher.learn();
		Maps = new File("HMMG"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

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
		teacher.learn();
		Maps = new File("HMMG"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

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

		Bot = new HMM(a.clone(),pi.clone(),distributionSet);
		teacher = new BaumWelch(portDstArr,Bot);
		teacher.learn();
		Maps = new File("HMMG"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

		Bad.loop(100000);
		orgTypes(Bad.getPacketData());
		Bad.packetData=null;
		r = 0;

		//sets up the distribution for dstArr and than has it learn and write results to file 
		Table = new HashMap<Observation,BigDecimal>();
		distributionSet = new ArrayList<Distribution>();
		Table.put(new Observation(1),BigDecimal.valueOf(.5));
		Table.put(new Observation(2),BigDecimal.valueOf(.5));

		distributionSet.add(new CategoricalDistribution(Table));
		distributionSet.add(new CategoricalDistribution(Table));
		distributionSet.add(new CategoricalDistribution(Table));
		Bot = new HMM(a.clone(),pi.clone(),distributionSet);
		teacher = new BaumWelch(srcArr,Bot);
		teacher.learn();
		Maps = new File("HMMB"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

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
		teacher.learn();
		Maps = new File("HMMB"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

		//sets up the distribution for portDstArr and than has it learn and write results to file 
		//Table = new HashMap<Observation,BigDecimal>(); 
		distributionSet = new ArrayList<Distribution>();
		//Table.put(new Observation(1),BigDecimal.valueOf(.333));
		//Table.put(new Observation(2),BigDecimal.valueOf(.333));
		//Table.put(new Observation(3),BigDecimal.valueOf(.333));

		//		distributionSet.add(new CategoricalDistribution(Table));
		//	distributionSet.add(new CategoricalDistribution(Table));
		//distributionSet.add(new CategoricalDistribution(Table));
		BigDecimal[] C2={new BigDecimal(.5),new BigDecimal(.5)};
		Function[] F2={new Gaussian(new BigDecimal(25),new BigDecimal(300)),
				new Gaussian(new BigDecimal(540),new BigDecimal(250)),
				new Gaussian(new BigDecimal(1480),new BigDecimal(250))};
		wave =new MixtureDistribution(C2,F2);
		
		distributionSet.add(wave);
		distributionSet.add(wave);
		distributionSet.add(wave);
		
		Bot = new HMM(a.clone(),pi.clone(),distributionSet);
		teacher = new BaumWelch(tlArr,Bot);
		teacher.learn();
		Maps = new File("HMMB"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

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
		teacher.learn();
		Maps = new File("HMMB"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}


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
		teacher.learn();
		Maps = new File("HMMB"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

		both.loop(100000);
		orgTypes(both.getPacketData());
		both.packetData=null;
		r = 0;
		//sets up the distribution for dstArr and than has it learn and write results to file 
		Table = new HashMap<Observation,BigDecimal>();
		distributionSet = new ArrayList<Distribution>();
		Table.put(new Observation(1),BigDecimal.valueOf(.5));
		Table.put(new Observation(2),BigDecimal.valueOf(.5));

		distributionSet.add(new CategoricalDistribution(Table));
		distributionSet.add(new CategoricalDistribution(Table));
		distributionSet.add(new CategoricalDistribution(Table));
		Bot = new HMM(a.clone(),pi.clone(),distributionSet);
		teacher = new BaumWelch(srcArr,Bot);
		teacher.learn();
		Maps = new File("HMMm"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

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
		teacher.learn();
		Maps = new File("HMMm"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

		//sets up the distribution for portDstArr and than has it learn and write results to file 
		//Table = new HashMap<Observation,BigDecimal>(); 
		distributionSet = new ArrayList<Distribution>();
	//	Table.put(new Observation(1),BigDecimal.valueOf(.333));
		//Table.put(new Observation(2),BigDecimal.valueOf(.333));
	//Table.put(new Observation(3),BigDecimal.valueOf(.333));
		BigDecimal[] C3={new BigDecimal(.5),new BigDecimal(.5)};
		Function[] F3={new Gaussian(new BigDecimal(25),new BigDecimal(200)),
				new Gaussian(new BigDecimal(510),new BigDecimal(300)),
				new Gaussian(new BigDecimal(1290),new BigDecimal(350))};
		wave =new MixtureDistribution(C3,F3);
		
		distributionSet.add(wave);
		distributionSet.add(wave);
		distributionSet.add(wave);
		//distributionSet.add(new CategoricalDistribution(Table));
		//distributionSet.add(new CategoricalDistribution(Table));
		//distributionSet.add(new CategoricalDistribution(Table));
		Bot = new HMM(a.clone(),pi.clone(),distributionSet);
		teacher = new BaumWelch(tlArr,Bot);
		teacher.learn();
		Maps = new File("HMMm"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

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
		teacher.learn();
		Maps = new File("HMMm"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}

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
		teacher.learn();
		Maps = new File("HMMm"+(r++)+".txt");
		try
		{
			FileOutputStream fileOut =
					new FileOutputStream(Maps);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(Bot);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in /tmp/employee.ser");
		}catch(IOException i)
		{
			i.printStackTrace();
		}




	}
	public static void orgTypes(ArrayList<ArrayList<Object>>  arr) {
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

}
