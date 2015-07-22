package glados;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class DataCollection {
	static String x = "Mix";
	static PcapReader read = new PcapReader("C:\\Users\\trae\\Desktop\\"+x+".pcap");
	static FileWriter output ;
	static BufferedWriter write ;
	
	
	public static void main(String[] args) throws IOException {
		Scanner eyes=new Scanner(System.in);
		output = new FileWriter("C:\\Users\\trae\\Desktop\\"+x+".csv");
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
		read.loop(loop);
		orgTypes(read.getPacketData());
		write.close();
		eyes.close();
	
	}
	
	
	
	public static void orgTypes(ArrayList<ArrayList<Object>>  arr) throws IOException {
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
