package glados;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;















import org.jnetpcap.Pcap;

import distributions.Distribution;
import distributions.EnumeratedDistribution;
import distributions.Function;
import distributions.Gaussian;
import distributions.MixtureDistribution;
import distributions.Observation;
import marcovModel.*;

public class machineLearning {
	public static ArrayList<Observation> dstArr = new ArrayList<Observation>();
	public static ArrayList<Observation> srcArr = new ArrayList<Observation>();
	public static ArrayList<Observation> tlArr = new ArrayList<Observation>();
	public static ArrayList<Observation> portDstArr = new ArrayList<Observation>();
	public static ArrayList<Observation> portSrcArr = new ArrayList<Observation>();
	public static ArrayList<Observation> timeStamp = new ArrayList<Observation>();
	static PcapReader read = new PcapReader("C:\\Users\\trae\\Google Drive\\Cybersecurity - FIU 2015\\stuffs.pcap");
	//static FileWriter output ;
	//static BufferedWriter write ;
	public static void main(String[] args) throws IOException {
		Scanner eyes=new Scanner(System.in);
		//	output = new FileWriter("data.csv");
		//write =  new BufferedWriter(output);
		int loop = 100;
		ArrayList<Distribution> x1;
		ArrayList<HMM> Bot=new ArrayList<HMM>();
		HashMap<Observation,BigDecimal> epsilon;
		System.out.print("Enter number of packets to read in: ");
		//loop=eyes.nextInt();
		read.loop(loop);
		orgTypes(read.getPacketData());
		//	write.close();
		BigDecimal [][] a = {
				{new BigDecimal(.8),new BigDecimal(.15),new BigDecimal(.05)},
				{new BigDecimal(.15),new BigDecimal(.75),new BigDecimal(.1)},
				{new BigDecimal(.1),new BigDecimal(.2),new BigDecimal(.7)}		
		};

		BigDecimal [] pi = {new BigDecimal(.7),new BigDecimal(.2),new BigDecimal(.1)
		};

		x1= new ArrayList<Distribution>();
		for(int y = 0 ;y < 3; y++){
			BigDecimal c[] = {new BigDecimal(.25),new BigDecimal(.25),
					new BigDecimal(.25),new BigDecimal(.25)};
			Function [] x = {new Gaussian(new BigDecimal(100),new BigDecimal(200)),
					new Gaussian(new BigDecimal(100),new BigDecimal(200)),
					new Gaussian(new BigDecimal(100),new BigDecimal(200))
			,new Gaussian(new BigDecimal(100),new BigDecimal(200))};	
			x1.add(new MixtureDistribution(c,x));
		}
		
		Bot.add(new HMM(a.clone(),pi.clone(),x1));
		BaumWelch learn1=new BaumWelch(portDstArr,Bot.get(0));

		x1= new ArrayList<Distribution>();
		for(int y = 0 ;y < 3; y++){
			BigDecimal c[] = {new BigDecimal(.25),new BigDecimal(.25),
					new BigDecimal(.25),new BigDecimal(.25)};
			Function [] x = {new Gaussian(new BigDecimal(100),new BigDecimal(200)),
					new Gaussian(new BigDecimal(100),new BigDecimal(200)),
					new Gaussian(new BigDecimal(100),new BigDecimal(200))
			,new Gaussian(new BigDecimal(100),new BigDecimal(200))};	
			x1.add(new MixtureDistribution(c,x));
		}
		
		Bot.add(new HMM(a.clone(),pi.clone(),x1));
		BaumWelch learn2=new BaumWelch(portSrcArr,Bot.get(1));

		System.out.println("HMM1:");
		System.out.println(Bot.get(0));
		learn1.learn();

		System.out.println("HMM2:");
		System.out.println(Bot.get(1));
		learn2.learn();





		eyes.close();
	}
	public static void orgTypes(ArrayList<ArrayList<Object>>  arr) {
		for (int i = 0; i < arr.size() ; i++) {
			try{
				System.err.println((Integer) arr.get(i).get(3));
				dstArr.add(new Observation((String) arr.get(i).get(0)));
				srcArr.add(new Observation((String) arr.get(i).get(1)));
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
