package glados;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;

import distributions.Observation;
import marcovModel.BaumWelch;
import marcovModel.HMM;

public class Test2 {
	static FileWriter output ;
	static BufferedWriter write;
	
	public static ArrayList<Observation> dstArr = new ArrayList<Observation>();
	public static ArrayList<Observation> srcArr = new ArrayList<Observation>();
	public static ArrayList<Observation> tlArr = new ArrayList<Observation>();
	public static ArrayList<Observation> portDstArr = new ArrayList<Observation>();
	public static ArrayList<Observation> portSrcArr = new ArrayList<Observation>();

	public static void main(String[] args) throws IOException {
		String fileName = "EXPLOIT_MS_Office_Outlook_Recipient_Control_(ole32.dll)_Denial_of_Service_Exploit_EvilFingers";
		//PcapReader read = new PcapReader("C:\\Users\\Lab User\\Desktop\\"+fileName+".pcap");
		PcapReader read = new PcapReader("C:\\Users\\trae\\Google Drive\\Cybersecurity - FIU 2015\\"+fileName+".pcap");
		//output = new FileWriter("C:\\Users\\Lab User\\Desktop\\"+fileName+".csv");
		//write =  new BufferedWriter(output);
		read.loop(2147483647);
		orgTypes(read.getPacketData());
		ArrayList<HMM> Bot = new ArrayList<>();
		ArrayList<Boolean> tally = new ArrayList<>();
		BaumWelch inf;
	
		  for(int s = 0; s < 5;s++)
			Bot.add(reLoad(s));
			
		int start = 0, end = 5;
		while(end != dstArr.size()){
			
			inf= new BaumWelch(new ArrayList<Observation>(dstArr.subList(start, end)),Bot.get(0));
			tally.add(inf.inferance()>=.60);
			inf= new BaumWelch(new ArrayList<Observation>(srcArr.subList(start, end)),Bot.get(0));
			tally.add(inf.inferance()>=.60);
			inf= new BaumWelch(new ArrayList<Observation>(tlArr.subList(start, end)),Bot.get(0));
			tally.add(inf.inferance()>=.60);
			inf= new BaumWelch(new ArrayList<Observation>(portSrcArr.subList(start, end)),Bot.get(0));
			tally.add(inf.inferance()>=.60);
			inf= new BaumWelch(new ArrayList<Observation>(portDstArr.subList(start, end)),Bot.get(0));
			tally.add(inf.inferance()>=.60);
			
			
			if(Collections.frequency(tally, true)>=3){
			System.out.println("You are Entering the Danger Zone");
				break;
			}
			
			start = end;
			end += 5;
			
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
	public static HMM reLoad(int x){
		HMM temp = null;
		try{
			FileInputStream fileIn = new FileInputStream("HMM.5"+x+".txt");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			temp = (HMM) in.readObject();
			in.close();
			fileIn.close();
		}catch(IOException|ClassNotFoundException i)
		{
			return temp;
		}
		return temp;

	}
}
