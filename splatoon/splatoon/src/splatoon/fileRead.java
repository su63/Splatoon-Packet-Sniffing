package splatoon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;

public class fileRead {
	public static ArrayList<UDPPacket> Read(File f){
		ArrayList<UDPPacket> p = new ArrayList<UDPPacket>();
		try(BufferedReader br = new BufferedReader(new FileReader(f))) {
			int i = 0;
			String time="";
			int timei=0;
		    for(String line; (line = br.readLine()) != null; i=(i+1)%4 ) {
		    	if(i==0||i==3)continue;
		    	if(i==1){
		    		time = line.substring(0, 16);
		    		int hour = Integer.valueOf(time.substring(0, 2))*60*60*1000;
		    		int minuts = Integer.valueOf(time.substring(4, 5))*60*1000;
		    		int sec = Integer.valueOf(time.substring(6, 8))*1000;
		    		int milisec = Integer.valueOf(time.substring(9, 12));
		    		timei=hour+minuts+sec+milisec;
		    	}
		    	if(i==2){
		    		String s =line.substring(6).replaceAll("\\|", "");
		    	    byte[] data =DatatypeConverter.parseHexBinary(s);
		    	    if(data[0x17]!=0x11)continue;
		    		UDPPacket pack = new UDPPacket(data);
		    		pack.time=timei;
		    		p.add(pack);
		    	}
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
}
