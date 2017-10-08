package splatoon;

public class rainmaker {
	float teamA;
	float teamB;
	float rainmakerLocation;
	int rainmakerCloserTo;
	int playerHasRainmaker;
	int rainmakerCondition;
	public void setData(String data){
		if(data.startsWith("302E")){
			rainmakerCondition = Integer.valueOf(data.substring(data.length()-1),16);
			playerHasRainmaker = Integer.valueOf(data.substring(data.length()-3,data.length()-2),16);
		}

		if(data.startsWith("1018")){
			char r = data.charAt(46);
			/*if(r == '8'){//1000
				rainmakerCloserTo = 2;
			}else if(r == '0'){//0000
				rainmakerCloserTo = 3;
			}else if(r == '3'){//0011
				rainmakerCloserTo = 0;
				rainmakerLocation = Integer.parseInt(data.substring(47,48)+data.substring(44,45),16);
			}else if(r == 'B'){//1011
				rainmakerCloserTo = 1;
				rainmakerLocation = Integer.parseInt(data.substring(47,48)+data.substring(44,45),26);
			}else{
				
				
			}*/
			

			rainmakerLocation = Float.intBitsToFloat(Integer.parseInt(data.substring(40,48),16));
			teamA = Float.intBitsToFloat(Integer.parseInt(data.substring(32,40),16));
			teamB = Float.intBitsToFloat(Integer.parseInt(data.substring(24,32),16));
		}
		
	}
	public String getScore(){
		return Math.round(teamA*10000.0)/100.0+":"+Math.round(teamB*10000.0)/100.0;
	}
}
