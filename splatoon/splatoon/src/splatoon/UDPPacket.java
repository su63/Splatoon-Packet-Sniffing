package splatoon;

import javax.xml.bind.DatatypeConverter;

public class UDPPacket {
	public String src_addr;
	public String dis_addr;
	public int src_port;
	public int dis_port;
	public int time;
	public byte[] data;
	public NNPacket nip;
	public int rawSize;
	UDPPacket(byte[] input){
		rawSize = input.length;
		byte[] b = new byte[4]; 
		System.arraycopy(input, 0x1A, b, 0, 4);
		this.src_addr = (b[0]&0xFF)+"."+(b[1]&0xFF)+"."+(b[2]&0xFF)+"."+(b[3]&0xFF);
		System.arraycopy(input, 0x1E, b, 0, 4);
		this.dis_addr = (b[0]&0xFF)+"."+(b[1]&0xFF)+"."+(b[2]&0xFF)+"."+(b[3]&0xFF);
		
		System.arraycopy(input, 0x22, b, 0, 2);
		this.src_port = ((b[0]&0xFF)<<8)+(b[1]&0xFF);
		System.arraycopy(input, 0x24, b, 0, 2);
		this.dis_port = ((b[0]&0xFF)<<8)+(b[1]&0xFF);
		this.data = new byte[input.length-0x2A];
		System.arraycopy(input, 0x2A, this.data, 0, input.length-0x2A);
		byte[] h = new byte[2];
		if(data.length<2)return;
		System.arraycopy(this.data, 0, h, 0, 2);
		if(DatatypeConverter.printHexBinary(h).compareTo(DatatypeConverter.printHexBinary(new byte[]{0x32, (byte) 0xAB}))==0){
			nip = new NNPacket(DatatypeConverter.printHexBinary(data));
		}
	}
	public String toJSON(){
		return
		"{"
			+ "\"src_addr\":\""+this.src_addr+"\","
			+ "\"dis_addr\":\""+this.dis_addr+"\","
			+ "\"src_port\":\""+this.src_port+"\","
			+ "\"dis_port\":\""+this.dis_port+"\","
			+ "\"data\":\""+data+"\""
			+ "\"time\":"+time
		+ "}";
	}
}
