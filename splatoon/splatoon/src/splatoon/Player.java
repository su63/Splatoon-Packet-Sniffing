package splatoon;

import java.util.ArrayList;

public class Player {
	public int x;
	public int y;
	public int z;
	public int surface;
	public int camRotH;
	public int camRotV;
	public int charRotH;
	public int charRotV;
	public String name;
	public int wepon;
	public int subwepon;
	public int specal;
	public int tank;
	public int health;
	public int specalMeter;
	public int hat;
	public int shirt;
	public int shose;
	public boolean host;
	public String ip;
	public String location;
	public int port;
	public int ping;
	public int pong;
	public int bestPing;
	public int fmt;
	public int team;
	public int nice;
	public int splats;
	public int splated;
	public int snsplat;
	public int snsplate;
	public int comon;
	public boolean hasRainmaker; 
	public int[] htrack = new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
	public ArrayList<Float> cord = new ArrayList<Float>();
	public ArrayList<String> ipn = new ArrayList<String>(); 
	public String slot;
	public networkstats net = new networkstats();
	public String action;
	public boolean left = false;
	public int at;
	public String two = "";
	public int playerCount;
	public int status = 0;
	public int splattedby = 0;
	public String LiveToJson(){
		return "{"
				+ "surface:"+surface+","
				+ "\"x\":"+x+","
				+ "\"y\":"+y+","
				+ "\"z\":"+z+","
				+ "\"ping\":"+bestPing+","
				+ "}";
	}
	public String InfoToJson(){
		return "{}";
	}
	public String GetStatusString(){
		switch(status){
		case 0:return "splatted(1)";
		case 2:return "in water(1)";
		case 3:return "respond(1)";
		case 4:return "respond(2)";
		case 5:return "jump";
		case 7:return "land";
		case 8:return "bubble";
		case 0x0a:return "tracked";
		case 0x0F:return "in water(2)";
		case 0x16:return "splatted(2)";
		case 0x17:return "inkstrike";
		default:return Integer.toString(status);
		}
	}
}
