package splatoon;

public class tower {
	float tower = 0;
	float speed = 0;
	float teamA = 0;
	float teamB = 0;
	public void setData(String data){
		if(data.startsWith("1024")){
			tower = Float.intBitsToFloat(Integer.parseUnsignedInt(data.substring(16,24),16));
			speed = Float.intBitsToFloat(Integer.parseUnsignedInt(data.substring(24,32),16));
			teamA = Float.intBitsToFloat(Integer.parseUnsignedInt(data.substring(40,48),16));
			teamB = Float.intBitsToFloat(Integer.parseUnsignedInt(data.substring(32,40),16));
		}
		
	}
	
}
