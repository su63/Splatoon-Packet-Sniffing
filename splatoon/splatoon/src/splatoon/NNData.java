package splatoon;

public class NNData {
	public String slot;
	public String size;
	public String unknownOne;
	public String nnid;
	public String type;
	public String unknownTwo;
	public String hash;
	public String data;

	public NNData(String hex) {
		slot = hex.substring(2, 4);
		size = hex.substring(4, 8);
		nnid = hex.substring(8, 24);
		type = hex.substring(24, 26);
		unknownTwo = hex.substring(26, 40);
		if(size.compareTo("0000")==0)return;
		data = hex.substring(40);
		//hash = hex.substring(hex.length()-8);
	}

}
