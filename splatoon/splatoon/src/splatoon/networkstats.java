package splatoon;

public class networkstats {
	/**
	 * 0-1 bits up 
	 * 2-3 bits down
	 * 4-5 packets up
	 * 6-7 packets down
	 * 8-15 packet size up
	 * 16-23 packet size down
	 * 24 tick-tock
	 * 25 packet up pose
	 * 26 packet down pose
	 * 27-28 player movement up;
	 * 29-30 player movement down;
	 */
	int[] data = new int[100];
	int bitrateup;
	int bitratedown;
	int packetup;
	int packetdown;
	int packetsizeup;
	int packetsizedown;
	int movementup;
	int movementdown;
	public void setPacketTo(int packetSize){
		int vs = (int) ((System.currentTimeMillis()%1000)/500);
		if(data[24]!=vs){
			bitrateup=data[1]+data[0];
			data[vs]=0;
			bitratedown=data[2]+data[3];
			data[vs+2]=0;
			packetup=data[4]+data[5];
			data[4+vs]=0;
			packetdown=data[6]+data[7];
			data[6+vs]=0;
			movementup=data[27]+data[28];
			data[27+vs]=0;
			movementdown=data[29]+data[30];
			data[29+vs]=0;
			data[24]=vs;
		}
		data[vs+0]+= packetSize*8;
		data[4+vs]++;
		packetsizeup = packetSize;
	}
	public void setPacketFrom(int packetSize){
		int vs = (int) ((System.currentTimeMillis()%1000)/500);
		if(data[24]!=vs){
			bitrateup=data[1]+data[0];
			data[vs]=0;
			bitratedown=data[2]+data[3];
			data[vs+2]=0;
			packetup=data[4]+data[5];
			data[4+vs]=0;
			packetdown=data[6]+data[7];
			data[6+vs]=0;
			movementup=data[27]+data[28];
			data[27+vs]=0;
			movementdown=data[29]+data[30];
			data[29+vs]=0;
			data[24]=vs;
		}
		data[vs+2]+= packetSize*8;
		data[6+vs]++;
		packetsizedown = packetSize;
	}
	public void setMovementTo(int num){
		int vs = (int) ((System.currentTimeMillis()%1000)/500);
		data[vs+27]+=num;		
	}
	public void setMovementFrom(int num){
		int vs = (int) ((System.currentTimeMillis()%1000)/500);
		data[vs+29]+=num;
	}
}