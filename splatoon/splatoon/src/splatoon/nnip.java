package splatoon;

import javax.xml.bind.DatatypeConverter;

public class nnip {
	public String sub;
	public String id;
	public String counter;
	public String msa;
	public String msb;
	public String flags;
	public String slot;
	public String size;
	public String unknownOne;
	public String nnid;
	public String type;
	public String unknownTwo;
	public String data;
	nnip(byte[] data){
		sub =DatatypeConverter.printHexBinary(new byte[]{data[4]});
		id = DatatypeConverter.printHexBinary(new byte[]{data[5]});
		counter = DatatypeConverter.printHexBinary(new byte[]{data[6],data[7]});
		msa = DatatypeConverter.printHexBinary(new byte[]{data[8],data[9]});
		msb = DatatypeConverter.printHexBinary(new byte[]{data[10],data[11]});
		flags = DatatypeConverter.printHexBinary(new byte[]{data[12]});
		slot = DatatypeConverter.printHexBinary(new byte[]{data[13]});
		size =DatatypeConverter.printHexBinary(new byte[]{data[14],data[15]});
		unknownOne = DatatypeConverter.printHexBinary(new byte[]{data[16],data[17],data[18],data[19]});
		nnid = DatatypeConverter.printHexBinary(new byte[]{data[20],data[21],data[22],data[23]});
		type =  DatatypeConverter.printHexBinary(new byte[]{data[24]});
		unknownTwo = DatatypeConverter.printHexBinary(new byte[]{data[25],data[26],data[27],data[28],data[29],data[30],data[31],data[32]});
		byte[] hold = new byte[data.length-32];
		System.arraycopy(data,33,hold,0,data.length-34);
		this.data = DatatypeConverter.printHexBinary(hold);;
		
	}
}
