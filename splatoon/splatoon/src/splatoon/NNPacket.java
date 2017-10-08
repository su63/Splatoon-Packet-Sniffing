package splatoon;

import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;

public class NNPacket {
	NNData[] NNData;
	NNBin[] NNBin;
	public String sub;
	public String id;
	public String counter;
	public String msa;
	public String msb;
	public String data;
	public String hash;
	NNPacket(String hex){
		if(!hex.startsWith("32AB9864"))return;
		sub = hex.substring(8, 10);
		id = hex.substring(10, 12);
		counter = hex.substring(12, 16);
		msa = hex.substring(16, 20);
		msb = hex.substring(20,24);
		data = hex.substring(24,hex.length());//.replaceAll("00*$", "");
		hash = hex.substring(hex.length()-32);
		int pos = 0,i = 1;
		int[] posx = new int[100];
		while(pos<data.length() && i < 100){
			//if(!data.substring(pos, pos+2).startsWith("00"))break;
			/*if(data.substring(pos+2, pos+4).startsWith("00")){
				pos+=6;
				continue;
			};*/
			pos+= (Integer.parseInt(data.substring(pos+4, pos+8), 16)*2)+40;
			if(pos>data.length()-32)break;
			posx[i++] = pos;
		}
		//System.out.println(data);
		//System.out.println(hash);
		//System.out.println("--");
		NNData = new NNData[i-1];
		for(int ii=0;ii<i-1;ii++)NNData[ii]=new NNData(data.substring(posx[ii],posx[ii+1]));
	}
}
