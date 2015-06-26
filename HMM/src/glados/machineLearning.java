package glados;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


import marcovModel.*;

public class machineLearning {
	public static ArrayList<String> dstArr = new ArrayList<String>();
	public static ArrayList<String> srcArr = new ArrayList<String>();
	public static ArrayList<Integer> tlArr = new ArrayList<Integer>();
	public static ArrayList<Integer> portDstArr = new ArrayList<Integer>();
	public static ArrayList<Integer> portSrcArr = new ArrayList<Integer>();
	
	static PcapReader read = new PcapReader("C:\\Users\\trae\\Google Drive\\Cybersecurity - FIU 2015\\stuffs.pcap");
    
	public static void main(String[] args) {
		Scanner eyes=new Scanner(System.in);
		
		int loop;
		System.out.print("Enter number of packets to read in: ");
		loop=eyes.nextInt();
		read.loop(loop);
		orgTypes(read.getPacketData());
		ArrayList<HMM> Bot=new ArrayList<HMM>();
		BigDecimal [][] a= {
				{new BigDecimal(.8),new BigDecimal(.15),new BigDecimal(.05)},
				{new BigDecimal(.15),new BigDecimal(.75),new BigDecimal(.1)},
				{new BigDecimal(.1),new BigDecimal(.2),new BigDecimal(.7)}		
		};
		BigDecimal [] pi={new BigDecimal(.7),new BigDecimal(.2),new BigDecimal(.1)
		};
		HashMap<Object,BigDecimal> epsilon= new HashMap<Object,BigDecimal> ();
		for(int x=0;x<65535;x++)
			epsilon.put(x+1, new BigDecimal((1.0/65535.0)));	
			
		
		final ArrayList<HashMap<Object,BigDecimal>> x1= new ArrayList<HashMap<Object,BigDecimal>>();
		x1.add(new HashMap<Object,BigDecimal>(epsilon));
		x1.add(new HashMap<Object,BigDecimal>(epsilon));
		x1.add(new HashMap<Object,BigDecimal>(epsilon));
		
		final ArrayList<HashMap<Object,BigDecimal>> x2= new ArrayList<HashMap<Object,BigDecimal>>();
		x2.add(new HashMap<Object,BigDecimal> (epsilon));
		x2.add(new HashMap<Object,BigDecimal> (epsilon));
		x2.add(new HashMap<Object,BigDecimal> (epsilon));
		
		Bot.add(new HMM(a.clone(),pi.clone(),x1));
		Bot.add(new HMM(a.clone(),pi.clone(),x2));
		
		BaumWelch<Integer> learn1=new BaumWelch<Integer>(portDstArr,Bot.get(0));
		BaumWelch<Integer> learn2=new BaumWelch<Integer>(portSrcArr,Bot.get(1));
		System.out.println("HMM1:");
		System.out.println(Bot.get(0));
		learn1.learn();
		System.out.println("HMM2:");
		System.out.println(Bot.get(1));
		learn2.learn();
		
		
		
		
		
		eyes.close();
	}
	public static void orgTypes(ArrayList<ArrayList<Object>> arr) {
		for (int i = 0; i < arr.size() - 1; i++) {
			try{
				dstArr.add((String) arr.get(i).get(0));
				srcArr.add((String) arr.get(i).get(1));
				tlArr.add(new Integer(
						(int) arr.get(i).get(2)));
				portDstArr.add(new Integer(
						(int) arr.get(i).get(3)));
				portSrcArr.add(new Integer(
						(int) arr.get(i).get(4)));
			}catch(IndexOutOfBoundsException e){
				arr.remove(i);
			}
		}
	}

}
