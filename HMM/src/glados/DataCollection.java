package glados;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class DataCollection {
	 
	static FileWriter output ;
	static BufferedWriter write ;
	
	
	public static void main(String[] args) throws IOException {
		String fileName = "SCAN_merge";
		 PcapReader read = new PcapReader("C:\\Users\\Lab User\\Desktop\\"+fileName+".pcap");
		output = new FileWriter("C:\\Users\\Lab User\\Desktop\\"+fileName+".csv");
		write =  new BufferedWriter(output);
		int loop = 2147483647;
		write.write("SrcIP");
		write.write(",");
		write.write("DesIP");
		write.write(",");
		write.write("length");
		write.write(",");
		write.write("SrcPort");
		write.write(",");
		write.write("DesPort");
		write.write(",");
		write.newLine();
		System.out.println("Opening file to read data");
		read.loop(loop);
		orgTypes(read.getPacketData());
		write.close();
		System.out.println("File closed");
		System.out.println(read.count);
	}
	
	public static void orgTypes(ArrayList<ArrayList<Integer>>  arr) throws IOException {
		
		for (int i = 0; i < arr.size() ; i++) {
			write.write(((Integer) arr.get(i).get(0)).toString());
			write.write(",");
			write.write(((Integer) arr.get(i).get(1)).toString());
			write.write(",");
			write.write(
					((Integer) arr.get(i).get(2)).toString());
			write.write(",");
			write.write(
					((Integer) arr.get(i).get(3)).toString());
			write.write(",");
			write.write(
					((Integer) arr.get(i).get(4)).toString());
			write.write(",");
			write.newLine();


		}
	}
}
