package splatoon;

public class splatzone {
	//69,70
	int zoneA=2;
	int zoneB=2;
	int teamAR;
	int teamBR;
	int teamAP;
	int teamBP;
	boolean endGame = false;
	
	public void setData(String data){
		if(data.startsWith("201A")){
			if(data.substring(7, 8).contentEquals("1")){
				teamAR = Integer.valueOf(data.substring(28, 32), 16);
				teamBR = Integer.valueOf(data.substring(32, 36), 16);
				teamAP = Integer.valueOf(data.substring(36, 40), 16);
				teamBP = Integer.valueOf(data.substring(40, 44), 16);
			}
		}
		if(data.startsWith("302A")){
			System.out.println(data);
			if(data.endsWith("00")){
				endGame = false;
				return;
			}
			if(data.endsWith("03")){
				endGame = true;
				return;
			}
			if(data.endsWith("05")){
				int z = Integer.valueOf(data.substring(69, 70), 16);
				if((z&0b1000)==0){//
					 zoneA=(z&0b0100)==0?0:1;
				}else{
					zoneA=2;
				}
				if((z&0b0010)==0){//
					 zoneB=(z&0b0001)==0?0:1;
				}else{
					zoneB=2;
				}
			}
			if(data.endsWith("01")){
				int z = Integer.valueOf(data.substring(67, 68), 16);
				if(z!=2){//
					 zoneA=z==0?0:1;
				}else{
					zoneA=2;
				}
			}
		}
	}
}
