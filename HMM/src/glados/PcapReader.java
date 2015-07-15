package glados;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.JPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.tcpip.Tcp;

public class PcapReader implements JPacketHandler<StringBuilder>{
	String fileName;// name of file that contains data
	Pcap pcap  ; //instance of pcap data manipulator
	final Tcp tcp = new Tcp();  
	final Ip4 ipv4 = new Ip4();
	final Ip6 ipv6 = new Ip6();	
	final StringBuilder errbuf = new StringBuilder();// error buffer
	ArrayList<ArrayList<Object>> packetData= new ArrayList<ArrayList<Object>>() ;
	
	//takes in a file and opens a file reader stream from pcap manipulator
	public PcapReader(String fileName){
		this.fileName=fileName;
		HashMap<Integer, Integer> portMap = new HashMap<Integer, Integer>();
		//risk
		portMap.put(19,3);
		portMap.put(1214,3);
		portMap.put(23,3);
		portMap.put(1214,3);
		portMap.put(42,3);
		portMap.put(12345,3);
		portMap.put(70,3);
		portMap.put(79,3);
		portMap.put(161,3);
		portMap.put(162,3);
		portMap.put(177,3);
		portMap.put(381,3);
		portMap.put(382,3);
		portMap.put(383,3);
		portMap.put(2745,3);
		portMap.put(3127,3);
		portMap.put(8866,3);
		portMap.put(4444,3);
		portMap.put(9898,3);
		portMap.put(9988,3);
		portMap.put(1080,3);
		portMap.put(15118,3);
		portMap.put(5554,3);
		portMap.put(27374,3);
		portMap.put(31337,3);
		portMap.put(6257,3);
		portMap.put(6500,3);
		//uncommon
		portMap.put(6112,2);
		portMap.put(27015,2);
		portMap.put(5900,2);
		portMap.put(25999,2);
		portMap.put(5800,2);
		portMap.put(1725,2);
		portMap.put(20000,2);
		portMap.put(19638,2);
		portMap.put(5500,2);
		portMap.put(19226,2);
		portMap.put(5432,2);
		portMap.put(14567,2);
		portMap.put(12035,2);
		portMap.put(12036,2);
		portMap.put(10113,2);
		portMap.put(10114,2);
		portMap.put(10115,2);
		portMap.put(10116,2);
		portMap.put(5001,2);
		portMap.put(5000,2);
		portMap.put(10000,2);
		portMap.put(4899,2);
		portMap.put(9999,2);
		portMap.put(4672,2);
		portMap.put(9800,2);
		portMap.put(4333,2);
		portMap.put(9119,2);
		portMap.put(3784,2);
		portMap.put(3785,2);
		portMap.put(3724,2);
		portMap.put(3689,2);
		portMap.put(8767,2);
		portMap.put(3306,2);
		portMap.put(8118,2);
		portMap.put(3128,2);
		portMap.put(3124,2);
		portMap.put(7648,2);
		portMap.put(7649,2);
		portMap.put(3074,2);
		portMap.put(7212,2);
		portMap.put(560,2);
		portMap.put(512,2);
		portMap.put(497,2);
		portMap.put(264,2);
		portMap.put(137,2);
		portMap.put(138,2);
		portMap.put(139,2);
		portMap.put(69,2);
		portMap.put(49,2);
		portMap.put(7,2);
		portMap.put(2302,2);
		portMap.put(2049,2);
		portMap.put(6346,2);
		portMap.put(6347,2);
		portMap.put(33434,2);
		portMap.put(28960,2);
		//common
		portMap.put(20, 1);
		portMap.put(21,1);
		portMap.put(22,1);
		portMap.put(25,1);
		portMap.put(43,1);
		portMap.put(53,1);
		portMap.put(67,1);
		portMap.put(68,1);
		portMap.put(80,1);
		portMap.put(88,1);
		portMap.put(102,1);
		portMap.put(110,1);
		portMap.put(113,1);
		portMap.put(119,1);
		portMap.put(123,1);
		portMap.put(135,1);
		portMap.put(143,1);
		portMap.put(201,1);
		portMap.put(318,1);
		portMap.put(389,1);
		portMap.put(411,1);
		portMap.put(412,1);
		portMap.put(443,1);
		portMap.put(445,1);
		portMap.put(464,1);
		portMap.put(465,1);
		portMap.put(500,1);
		portMap.put(513,1);
		portMap.put(514,1);
		portMap.put(515,1);
		portMap.put(520,1);
		portMap.put(521,1);
		portMap.put(540,1);
		portMap.put(554,1);
		portMap.put(546,1);
		portMap.put(547,1);
		portMap.put(563,1);
		portMap.put(587,1);
		portMap.put(591,1);
		portMap.put(593,1);
		portMap.put(631,1);
		portMap.put(636,1);
		portMap.put(639,1);
		portMap.put(646,1);
		portMap.put(691,1);
		portMap.put(860,1);
		portMap.put(873,1);
		portMap.put(902,1);
		portMap.put(989,1);
		portMap.put(990,1);
		portMap.put(993,1);
		portMap.put(995,1);
		portMap.put(1025,1);
		portMap.put(1026,1);
		portMap.put(1027,1);
		portMap.put(1028,1);
		portMap.put(1029,1);
		portMap.put(1194,1);
		portMap.put(1311,1);
		portMap.put(1512,1);
		portMap.put(1589,1);
		portMap.put(1701,1);
		portMap.put(1723,1);
		portMap.put(1741,1);
		portMap.put(1755,1);
		portMap.put(1812,1);
		portMap.put(1813,1);
		portMap.put(1863,1);
		portMap.put(1985,1);
		portMap.put(2000,1);
		portMap.put(2002,1);
		portMap.put(2082,1);
		portMap.put(2083,1);
		portMap.put(2100,1);
		portMap.put(2222,1);
		portMap.put(2483,1);
		portMap.put(2484,1);
		portMap.put(2967,1);
		portMap.put(3050,1);
		portMap.put(3222,1);
		portMap.put(3260,1);
		portMap.put(3389,1);
		portMap.put(3690,1);
		portMap.put(4664,1);
		portMap.put(5004,1);
		portMap.put(5005,1);
		portMap.put(5050,1);
		portMap.put(5060,1);
		portMap.put(5190,1);
		portMap.put(5222,1);
		portMap.put(5223,1);
		portMap.put(5631,1);
		portMap.put(5632,1);
		portMap.put(6000,1);
		portMap.put(6001,1);
		portMap.put(6129,1);
		portMap.put(6566,1);
		portMap.put(6970,1);
		portMap.put(8000,1);
		portMap.put(8080,1);
		portMap.put(8086,1);
		portMap.put(8087,1);
		portMap.put(8200,1);
		portMap.put(8500,1);
		portMap.put(9100,1);
		portMap.put(9101,1);
		portMap.put(9102,1);
		portMap.put(9103,1);
		portMap.put(11371,1);
		portMap.put(13720,1);
		portMap.put(13721,1);
		portMap.put(24800,1);
		}
	//loops x number of times to collect data
	public void loop(int x){
		pcap = Pcap.openOffline(fileName, errbuf);
		if (pcap == null) {  
			System.err.println(errbuf); // Error is stored in errbuf if any  
			return;  
		}  		
		pcap.loop(x,this,errbuf);
		pcap.close();
	}
	@Override
	public void nextPacket(JPacket packet, StringBuilder errbuf) {
		ArrayList<Object> pckt = new ArrayList<Object>() ;
		/* 
		 * Here we receive 1 packet at a time from the capture file. We are 
		 * going to check if we have a tcp packet and do something with tcp 
		 * header. We are actually going to do this twice to show 2 different 
		 * ways how we can check if a particular header exists in the packet and 
		 * then get that header (peer header definition instance with memory in 
		 * the packet) in 2 separate steps. 
		 */  
		
		if (packet.hasHeader(Ip4.ID)) {  

			/* 
			 * Now get our tcp header definition (accessor) peered with actual 
			 * memory that holds the tcp header within the packet. 
			 */  
			packet.getHeader(ipv4);
			packet.getHeader(tcp); 
			String temp="";



			for(int x=0;x<4;x++)
				temp+=Integer.toString(ipv4.destination()[x]&0xFF )+((x!=3)?".":"");
			pckt.add(temp);
			temp="";


			for(int x=0;x<4;x++)
				temp+=Integer.toString(ipv4.source()[x]&0xFF )+((x!=3)?".":"");
			pckt.add(temp);


			pckt.add(new Integer(ipv4.getPayload().length));    
			pckt.add(new Integer(tcp.destination()));  
			pckt.add(new Integer(tcp.source()));
		}
		

	}
	/**
	  @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public ArrayList<ArrayList<Object>> getPacketData() {
		return packetData;
	}
	public void setPacketData(ArrayList<ArrayList<Object>> packetData) {
		this.packetData = packetData;
	}
	
}
