package glados;

import java.io.File;
import java.io.IOException;
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
	public static ArrayList<Observation> timeStamp = new ArrayList<Observation>();
	static PcapReader read = new PcapReader("C:\\Users\\trae\\Google Drive\\Cybersecurity - FIU 2015\\stuffs.pcap");
	static File test = new File("C:\\Users\\trae\\Desktop\\test.txt");

	//static FileWriter output ;
	//static BufferedWriter write ;
	public static void main(String[] args) throws IOException {
		Scanner eyes=new Scanner(test);
		//	output = new FileWriter("data.csv");
		//write =  new BufferedWriter(output);
		int loop = 20;
		ArrayList<Distribution> x1;
		ArrayList<HMM> Bot=new ArrayList<HMM>();
		System.out.println("Enter number of packets to read in: ");
		//loop=eyes.nextInt();
		read.loop(loop);
		orgTypes(read.getPacketData());
		//	write.close();
		
		System.out.println();
		BigDecimal [][] a = {
				{new BigDecimal(0.47468,MathContext.DECIMAL32),new BigDecimal(0.52532,MathContext.DECIMAL32)},
				{new BigDecimal(0.51656,MathContext.DECIMAL32),new BigDecimal(0.48344,MathContext.DECIMAL32)}
		};
		
		BigDecimal [] pi = {new BigDecimal(0.51316,MathContext.DECIMAL32),new BigDecimal(0.48684,MathContext.DECIMAL32)};//,new BigDecimal(.1)};

		x1= new ArrayList<Distribution>();
		HashMap<Observation, BigDecimal> temp = new HashMap<Observation, BigDecimal>();
		/*
		for(int y = 0 ;y < a.length; y++){
			HashMap<Observation, BigDecimal> temp = new HashMap<Observation, BigDecimal>();
			for(int x = -1 ;x < 65535; x++)
				temp.put(new Observation(x+1), new BigDecimal(1/65535.));
			x1.add(new EnumeratedDistribution(temp));
		}
		 */
		temp.put(new Observation('a'), new BigDecimal(.03735,MathContext.DECIMAL32));
		temp.put(new Observation('b'), new BigDecimal(.03408,MathContext.DECIMAL32));
		temp.put(new Observation('c'), new BigDecimal(.03455,MathContext.DECIMAL32));
		temp.put(new Observation('d'), new BigDecimal(0.03828,MathContext.DECIMAL32));
		temp.put(new Observation('e'), new BigDecimal(0.03782,MathContext.DECIMAL32));
		temp.put(new Observation('f'), new BigDecimal(0.03922,MathContext.DECIMAL32));
		temp.put(new Observation('g'), new BigDecimal(0.03688,MathContext.DECIMAL32));
		temp.put(new Observation('h'), new BigDecimal(0.03408,MathContext.DECIMAL32));
		temp.put(new Observation('i'), new BigDecimal(0.03875,MathContext.DECIMAL32));
		temp.put(new Observation('j'), new BigDecimal(0.04062,MathContext.DECIMAL32));
		temp.put(new Observation('k'), new BigDecimal(0.03735,MathContext.DECIMAL32));
		temp.put(new Observation('l'), new BigDecimal(0.03968,MathContext.DECIMAL32));
		temp.put(new Observation('m'), new BigDecimal(0.03548,MathContext.DECIMAL32));
		temp.put(new Observation('n'), new BigDecimal(0.03735,MathContext.DECIMAL32));
		temp.put(new Observation('o'), new BigDecimal(0.04062,MathContext.DECIMAL32));
		temp.put(new Observation('p'), new BigDecimal(0.03595,MathContext.DECIMAL32));
		temp.put(new Observation('q'), new BigDecimal(0.03641,MathContext.DECIMAL32));
		temp.put(new Observation('r'), new BigDecimal(0.03408,MathContext.DECIMAL32));
		temp.put(new Observation('s'), new BigDecimal(0.04062,MathContext.DECIMAL32));
		temp.put(new Observation('t'), new BigDecimal(0.03548,MathContext.DECIMAL32));
		temp.put(new Observation('u'), new BigDecimal(0.03922,MathContext.DECIMAL32));
		temp.put(new Observation('v'), new BigDecimal(0.04062,MathContext.DECIMAL32));
		temp.put(new Observation('w'), new BigDecimal(0.03455,MathContext.DECIMAL32));
		temp.put(new Observation('x'), new BigDecimal(0.03595,MathContext.DECIMAL32));
		temp.put(new Observation('y'), new BigDecimal(0.03408,MathContext.DECIMAL32));
		temp.put(new Observation('z'), new BigDecimal(0.03408,MathContext.DECIMAL32));
		temp.put(new Observation(' '), new BigDecimal(0.03688,MathContext.DECIMAL32));

		x1.add(new CategoricalDistribution(temp));

		temp = new HashMap<Observation, BigDecimal>();

		temp.put(new Observation('a'), new BigDecimal(0.03909,MathContext.DECIMAL32));
		temp.put(new Observation('b'), new BigDecimal(0.03537,MathContext.DECIMAL32));
		temp.put(new Observation('c'), new BigDecimal(0.03537,MathContext.DECIMAL32));
		temp.put(new Observation('d'), new BigDecimal(0.03909,MathContext.DECIMAL32));
		temp.put(new Observation('e'), new BigDecimal(0.03583,MathContext.DECIMAL32));
		temp.put(new Observation('f'), new BigDecimal(0.03630,MathContext.DECIMAL32));
		temp.put(new Observation('g'), new BigDecimal(0.04048,MathContext.DECIMAL32));
		temp.put(new Observation('h'), new BigDecimal(0.03537,MathContext.DECIMAL32));
		temp.put(new Observation('i'), new BigDecimal(0.03816,MathContext.DECIMAL32));
		temp.put(new Observation('j'), new BigDecimal(0.03909,MathContext.DECIMAL32));
		temp.put(new Observation('k'), new BigDecimal(0.03490,MathContext.DECIMAL32));
		temp.put(new Observation('l'), new BigDecimal(0.03723,MathContext.DECIMAL32));
		temp.put(new Observation('m'), new BigDecimal(0.03537,MathContext.DECIMAL32));
		temp.put(new Observation('n'), new BigDecimal(0.03909,MathContext.DECIMAL32));
		temp.put(new Observation('o'), new BigDecimal(0.03397,MathContext.DECIMAL32));
		temp.put(new Observation('p'), new BigDecimal(0.03397,MathContext.DECIMAL32));
		temp.put(new Observation('q'), new BigDecimal(0.03816,MathContext.DECIMAL32));
		temp.put(new Observation('r'), new BigDecimal(0.03676,MathContext.DECIMAL32));
		temp.put(new Observation('s'), new BigDecimal(0.04048,MathContext.DECIMAL32));
		temp.put(new Observation('t'), new BigDecimal(0.03443,MathContext.DECIMAL32));
		temp.put(new Observation('u'), new BigDecimal(0.03537,MathContext.DECIMAL32));
		temp.put(new Observation('v'), new BigDecimal(0.03955,MathContext.DECIMAL32));
		temp.put(new Observation('w'), new BigDecimal(0.03816,MathContext.DECIMAL32));
		temp.put(new Observation('x'), new BigDecimal(0.03723,MathContext.DECIMAL32));
		temp.put(new Observation('y'), new BigDecimal(0.03769,MathContext.DECIMAL32));
		temp.put(new Observation('z'), new BigDecimal(0.03955,MathContext.DECIMAL32));
		temp.put(new Observation(' '), new BigDecimal(0.03397,MathContext.DECIMAL32));

		x1.add(new CategoricalDistribution(temp));

		
		String temp2;
		ArrayList<Observation> egg = new ArrayList<Observation>();
		while(eyes.hasNextLine()&&egg.size()!=50000){
		temp2 = eyes.nextLine().toLowerCase();
			for(int s = 0; s < temp2.length(); s++)
				if(egg.size() != 50000)
			egg.add(new Observation(temp2.charAt(s)));
		}
		Bot.add(new HMM(a.clone(),pi.clone(),x1));
		BaumWelch learn1=new BaumWelch(egg,Bot.get(0));

		x1= new ArrayList<Distribution>();
		for(int y = 0 ;y < a.length; y++){
			BigDecimal c[] = {new BigDecimal(.25),new BigDecimal(.25),
					new BigDecimal(.25),new BigDecimal(.25)};
			Function [] x = {new Gaussian(new BigDecimal(30),new BigDecimal(25)),
					new Gaussian(new BigDecimal(1280),new BigDecimal(80))};	
			x1.add(new MixtureDistribution(c,x));
		}

		Bot.add(new HMM(a.clone(),pi.clone(),x1));
		BaumWelch learn2=new BaumWelch(tlArr,Bot.get(1));

		//System.out.println("HMM1:");
		//System.out.println(Bot.get(0));
		//learn1.learn();

		System.out.println("HMM2:");
		System.out.println(Bot.get(1));
		learn2.learn();





		eyes.close();
	}
	public static void orgTypes(ArrayList<ArrayList<Object>>  arr) {
		for (int i = 0; i < arr.size() ; i++) {
			try{

				//dstArr.add(new Observation((String) arr.get(i).get(0)));
				//srcArr.add(new Observation((String) arr.get(i).get(1)));
				tlArr.add(new Observation(
						(int) arr.get(i).get(2)));
				portDstArr.add(new Observation(
						((Integer) arr.get(i).get(3)).intValue()));
				portSrcArr.add(new Observation (
						((Integer) arr.get(i).get(4)).intValue()));

				timeStamp.add(new Observation (
						(Date) arr.get(i).get(5)));

			}catch(IndexOutOfBoundsException  e){

				arr.remove(i);
			}
		}
	}

}
