package glados;
import java.util.ArrayList;
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

		if (packet.hasHeader(Ip6.ID)) {  

			/* 
			 * Now get our tcp header definition (accessor) peered with actual 
			 * memory that holds the tcp header within the packet. 
			 */  
			packet.getHeader(ipv6);  
			packet.getHeader(tcp); 

			StringBuffer result = new StringBuffer();
			for(int x=0;x<ipv6.destination().length;x++)
				result.append(String.format("%X"+((x!=15)?":":""), ipv6.destination()[x]));
			pckt.add(result.toString());
			
			result = new StringBuffer();
			for(int x=0;x<ipv6.source().length;x++)
				result.append(String.format("%02X"+((x!=15)?":":""), ipv6.source()[x]));
			pckt.add(result.toString());

			pckt.add(new Integer(ipv6.getPayload().length));    
			pckt.add(new Integer(tcp.destination()));  
			pckt.add(new Integer(tcp.source()));
		}   
		packetData.add(pckt);
		
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
