package splatoon;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.xml.bind.DatatypeConverter;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class handelbid {//netsh wlan start hostednetwork

	ArrayList<Player> players = new ArrayList<Player>();
	Player dummy = new Player();
	ArrayList<NNPacket> holdPacket = new ArrayList<NNPacket>();
	int frame = 0;
	boolean show_sent_data = false;
	boolean show_game_data = false;
	ArrayList<String> gamedata= new ArrayList<String>();
	int fmi = 0;
	rainmaker rainMaker = new rainmaker();
	splatzone splatZone = new splatzone();
	tower tower = new tower();
	public static void main(String[]a){
		 splatlive.init(a);
	}
	public handelbid(){
		dummy.bestPing = 1000;
		dummy.tank = 512;
		dummy.name = "NoSquidKid";
	}
	public String loadFile(){
		//return "C:\\Users\\super\\Documents\\splatoon21.txt";
		return "";
	}
	public void onNewPacket(UDPPacket packet){
		ArrayList<Player> players = this.players;//
		/*
        int size = packet.size();  
        JBuffer buffer = packet; 
        byte[] array = buffer.getByteArray(0, size); 
        UDPPacket a = new UDPPacket(array);*/
        if(packet.nip==null)return;
        if(packet.nip.NNData==null)return;
        Player player = null;
		for(int i = 0;i < players.size();i++){
			player = players.get(i);
			if(player.ip.compareTo(packet.src_addr)==0 && player.port == packet.src_port)break;
			player = null;
		}
        if(player==null){
			player = new Player();
			player.ip = packet.src_addr;
			player.port = packet.src_port;
			player.name = "New Player!";
			if(packet.src_addr.startsWith("192.168")||
					packet.src_addr.startsWith("10.")||
					packet.src_addr.startsWith("172.16.")||
					packet.src_addr.startsWith("172.17.")||
					packet.src_addr.startsWith("172.18.")||
					packet.src_addr.startsWith("172.19.")||
					packet.src_addr.startsWith("172.20.")||
					packet.src_addr.startsWith("172.21.")||
					packet.src_addr.startsWith("172.22.")||
					packet.src_addr.startsWith("172.23.")||
					packet.src_addr.startsWith("172.24.")||
					packet.src_addr.startsWith("172.25.")||
					packet.src_addr.startsWith("172.26.")||
					packet.src_addr.startsWith("172.27.")||
					packet.src_addr.startsWith("172.28.")||
					packet.src_addr.startsWith("172.29.")||
					packet.src_addr.startsWith("172.30.")||
					packet.src_addr.startsWith("172.31.")||
					packet.src_addr.startsWith("172.32.")
						)players.add(0,player);
				else players.add(player);
			ping(player);
        }
        for(NNData NNData:packet.nip.NNData){
        	if(NNData==null)continue;
        	if(NNData.data==null)continue;
	        if(NNData.type.compareTo("01")==0&&NNData.data.startsWith("0200050330")&&player.name.compareTo("New Player!")==0){//A player has appeared!!!
				//Gets player's info
				String name = new String(DatatypeConverter.parseHexBinary(NNData.data.substring(72,112)),Charset.forName("UTF-16"));
				name = name.split("u\00")[0];
				player.name = name;
				player.bestPing = 9999;
				//add player into the players list

				System.out.println("Hi, "+name+"!  ^x^/");
				return;
			}
	        if(player!=null)player.slot = NNData.slot;
	        else return;
			if(NNData.type.compareTo("24")==0){
				if(NNData.data.startsWith("03D3")){
					String def = deflate(NNData.data.substring(24));
					if(def.compareTo("00")==0)continue;
					String[] dem = Demess(def);
					for(String fff:dem){
						if(fff.startsWith("303A")){
							player.two = fff.substring(108,110);
							if(
									fff.substring(108,110).compareTo("00")==0||
									fff.substring(108,110).compareTo("16")==0
							)
							player.splattedby = Integer.parseInt(fff.substring(110,112),16);
						}
						if(fff.startsWith("2012")){
							//something...
						}
						if(fff.startsWith("103C")){//players location and status
							int x = (int) ((Long.parseLong(fff.substring(34,36)+fff.substring(32,34)+fff.substring(30,32)+fff.substring(28,30),16)&0x07FFFFF8)>>3);
							x-=0x800000;
							int y = (int) ((Long.parseLong(fff.substring(28,30)+fff.substring(26,28)+fff.substring(24,26)+fff.substring(22,24),16)&0x03FFFFFc)>>2);
							y-=0x800000;
							int z = (int) ((Long.parseLong(fff.substring(22,24)+fff.substring(20,22)+fff.substring(18,20)+fff.substring(16,18),16)&0x01FFFFFe)>>1);
							z-=0x800000;
							int camRotH = Integer.parseInt(fff.substring(88,90)+fff.substring(86,88)/*+fff.substring(84,86)+fff.substring(82,84)*/,16);
							int camRotV = Integer.parseInt(fff.substring(92,94)+fff.substring(90,92),16)&0x0FFF;
							int tank = Integer.parseInt(fff.substring(102,104)+fff.substring(100,102),16);
							int damage = (Integer.parseInt(fff.substring(98,100)+fff.substring(96,98),16)& 0x1ff8)>>3;
							int specalMeter = (Integer.parseInt(fff.substring(96,98)+fff.substring(94,96),16)& 0x07f0)>>4;
							int surface = (Integer.parseInt(fff.substring(36,38)+fff.substring(34,36),16)&0x01f8)>>3;
							String p = fff.substring(0,16)+reverseHex(fff.substring(16));

							x = (int) ((Long.parseLong(p.substring(100,108),16)&0x07FFFFF8)>>3);
							x-=0x800000;
							x+=512;
							y = (int) ((Long.parseLong(p.substring(106,114),16)&0x03FFFFFc)>>2);
							y-=0x800000;
							y+=512;
							z = (int) ((Long.parseLong(p.substring(112,120),16)&0x01FFFFFe)>>1);
							z-=0x800000;
							z+=512;
							
							player.x=x;
							player.y=y;
							player.z=z;
							player.camRotV=camRotV;
							player.camRotH=camRotH;
							player.specalMeter=specalMeter;
							player.tank = tank;
							player.health = damage;
							player.specalMeter = specalMeter;
							
							player.surface = surface;
									

						}
						if(fff.startsWith("1024")){
							//tower control
							tower.setData(fff);
						}
						if(fff.startsWith("3062")){
							player.host=true;
						}
						if(fff.startsWith("201A")){
							if(fff.substring(28).startsWith("000")){
								player.at = Integer.parseInt(fff.charAt(39)+"", 16);
								player.playerCount = Integer.parseInt(fff.charAt(43)+"", 16);
								if(player == players.get(0))if(player.at==1)for(Player vb:players)vb.host=false;
							}else{
								
							}
						}
						if(fff.startsWith("301B")){
							if(fff.endsWith("2")){
								player.nice = 300;
								player.comon = 0;
							}
							if(fff.endsWith("0")){
								player.nice = 0;
								player.comon = 300;
							}
						}
						if(fff.startsWith("302D")){
							//something...
						}
						if(fff.startsWith("302E")||fff.startsWith("1018")){
							//rainmaker stuff
							rainMaker.setData(fff);
						}
						if(fff.startsWith("201A")||fff.startsWith("302A")){
							//rainmaker / end game?
							splatZone.setData(fff);
						}
						if(fff.startsWith("3031")){
							//not sure, maybe ink placement
							String p = fff.substring(0,52)+reverseHex(fff.substring(52));
							int x = (int) ((Long.parseLong(p.substring(72, 80),16) & 0x01FFFFFE)>>1);
							x-=0x800000;
							int y = (int) (Long.parseLong(p.substring(80, 86),16));
							y -= 0x800000;
							int z = (int) ((Long.parseLong(p.substring(86, 94),16) & 0x7FFFFF80)>>7);
							z -= 0x800000;
						}
						if(fff.startsWith("3032")){
							// I hit someone!!! (mostly with main wepon)
							int pnum = Integer.parseInt(fff.substring(10,14),16);
							boolean found = false;
							for(int i = 0;i <20;i++){
								if(pnum==player.htrack[i])found = true;
							}
							if(!found){
								System.arraycopy(player.htrack, 0, player.htrack, 1, 19);
								player.htrack[0]= pnum;
								if(fff.substring(52).startsWith("41")){//splats
									player.splats++;
									player.snsplat = 300;
									player.snsplate++;
								}else{//hit
									Integer.parseInt(fff.substring(67,68),16);
									
								}
							}
						}
						if(fff.startsWith("3031")&&false){ // still needs to find out with this
							String p = fff.substring(0,52)+reverseHex(fff.substring(52));
							int numb = Integer.parseInt(p.substring(114),16);
							switch (numb){
							case 0xc4:
								player.action = "Jumping";
								break;
							case 0x43:
								player.action = "Splatted";
								break;
							case 0xC3:
								player.action = "Splatted";
								break;
							}
						}
					}
				}
			}
        }
		if((show_sent_data || show_game_data )&& players.size()>2){// this let's me see hex data made from the game
			Player h = null;
			for(Player sv:players)if(sv.host)h = sv;
			if(h!=null){
				if(player==h)holdPacket.add(packet.nip);
			}else{
				if(packet.src_addr.compareToIgnoreCase(players.get(0).ip)==0&&packet.dis_addr.compareToIgnoreCase(players.get(1).ip)==0)
					holdPacket.add(packet.nip);;
			}
		}
		
    	//Reset Timeout
    	player.fmt = Math.min(30, Math.max(-6, player.fmt-6));
    	//Bitrate tracker
    	player.net.setPacketFrom(packet.rawSize);
    	for(Player r:players){
	    	if(r.ip.compareTo(packet.dis_addr)==0&&r.port==packet.dis_port){//bitrate tracker
	    		r.net.setPacketTo(packet.rawSize);
	    	}
    	}
	}
	public void onScreenRefresh(GraphicsContext gc){
		int sf = 6;
		if (this.fmi++%sf!=0)return;
		ArrayList<Player> players = this.players;
		int top = 20;
		int fontSize = 15;
		int textHeight = 13;
		int square = 80;

		int middle = 600;

		//clear screen per refresh
    	gc.setFill(Color.web("000000"));
    	gc.fillRect(0, 0, 1280, 720);

		gc.setTextAlign(TextAlignment.LEFT);
		gc.setFont(Font.font("Cooper Std", FontWeight.BOLD, fontSize));
    	gc.setFill(Color.WHITE);
    	
    	// Splat Zone Display (mostly done)
    	gc.fillText("Splat Zone", 500, 20);
		gc.setTextAlign(TextAlignment.RIGHT);
    	gc.setFill(Color.ORANGE);
    	gc.fillText(Integer.toString(splatZone.teamBR/72), 590, 40);
    	gc.fillText(Integer.toString(splatZone.teamBP/72), 590, 55);
    	gc.setFill(Color.BLUE);
    	gc.fillText(Integer.toString(splatZone.teamAR/72), 630, 40);
    	gc.fillText(Integer.toString(splatZone.teamAP/72), 630, 55);

		gc.setTextAlign(TextAlignment.LEFT);
    	if(splatZone.zoneA == 2){
    		gc.setFill(Color.WHITE);
    		gc.fillText("Zone A",500, 40);
    	}
    	if(splatZone.zoneA == 0){
    		gc.setFill(Color.BLUE);
    		gc.fillText("Blue",500, 40);
    	}
    	if(splatZone.zoneA == 1){
    		gc.setFill(Color.ORANGE);
    		gc.fillText("Orange",500, 40);
    	}
    	if(splatZone.zoneB == 2){
    		gc.setFill(Color.WHITE);
    		gc.fillText("Zone B",500, 55);
    	}
    	if(splatZone.zoneB == 0){
    		gc.setFill(Color.BLUE);
    		gc.fillText("Blue",500, 55);
    	}
    	if(splatZone.zoneB == 1){
    		gc.setFill(Color.ORANGE);
    		gc.fillText("Orange",500, 55);
    	}
    	
    	//Tower Controll Display (mostly done)
		gc.setTextAlign(TextAlignment.LEFT);
    	gc.fillText("Tower Control", 700, 20);
    	gc.fillText("Twr Loc", 700, 35);
    	gc.fillText("Twr Vol", 700, 50);
		gc.setTextAlign(TextAlignment.RIGHT);
    	gc.fillText(Float.toString(tower.tower), 850, 35);//tower location-ish
    	if(tower.speed <-.01)gc.setFill(Color.BLUE);
    	else if(tower.speed <.01)gc.setFill(Color.WHITE);
    	else gc.setFill(Color.ORANGE);
    	gc.fillText(Float.toString(tower.speed), 850, 50);
    	gc.setFill(Color.ORANGE);
    	gc.fillText(Float.toString(Math.round(tower.teamA*10000.0f)/100.0f), 850, 65);
    	gc.setFill(Color.BLUE);
    	gc.fillText(Float.toString(Math.round(tower.teamB*10000.0f)/100.0f), 850, 80);


    	gc.setFill(Color.WHITE);
    	// Rain Maker Display (needs work)
		gc.setTextAlign(TextAlignment.LEFT);
    	gc.fillText("Rain Maker", 900, 20);
		gc.setTextAlign(TextAlignment.RIGHT);
    	gc.fillText(rainMaker.rainmakerCondition+"", 960, 35);
    	gc.fillText(rainMaker.playerHasRainmaker+"", 960, 50);
    	gc.fillText(rainMaker.rainmakerLocation+"", 960, 65);
    	gc.fillText(rainMaker.teamA+"", 960, 80);
    	gc.fillText(rainMaker.teamB+"", 960, 95);
    	
    	// for each player display
    	for(int i = 0;i<8||i<players.size();i++){
    		Player s = (players.size()>i)?players.get(i):dummy;
    		Color v = Color.WHITE;
    		if(players.size()>i){
    			int sig = s.fmt;
    			s.fmt=sig+sf;
    			if(sig >30)v = Color.YELLOW;
    			if(sig >=60)v = Color.RED;
    			if(sig>300){//player timed out after 5 sec.
	    			System.out.println("I guess "+players.get(i).name+" left. :/");
	    			players.remove(i).left=true;
	    			return;
    			}
    		}
    		gc.setFill(v);
    		gc.setFont(Font.font("Cooper Std", FontWeight.BOLD, fontSize));
    		gc.setTextAlign(TextAlignment.RIGHT);
    		
    		//player's position and stats
			gc.fillText((s.host?"Hosting ":"")+s.name, 180, (i*square)+(0*textHeight)+top);
    		gc.fillText(Integer.toString(s.x/0x1)+"rx", 90, (i*square)+(1*textHeight)+top);
    		gc.fillText(Integer.toString(s.y/0x1)+"ry", 160, (i*square)+(1*textHeight)+top);
    		gc.fillText(Integer.toString(s.z/0x1)+"rz", 230, (i*square)+(1*textHeight)+top);
    		gc.fillText(String.format("%.3f",s.camRotH/(((double)0xFFFF)/360.0d))+"�", 140, (i*square)+(2*textHeight)+top);
    		gc.fillText(Integer.toString(s.camRotV), 230, (i*square)+(2*textHeight)+top);
    		gc.fillText(Long.toString(Math.round((float)s.tank/5.120))+"%", 130, (i*square)+(3*textHeight)+top);
    		gc.fillText(String.format("%.1f",(float)s.health/10.240), 230, (i*square)+(3*textHeight)+top);
    		gc.fillText(Integer.toString(s.specalMeter)+"%", 180, (i*square)+(3*textHeight)+top);
    		gc.fillText(Integer.toString(s.surface), 230, (i*square)+(4*textHeight)+top);
    		gc.fillText(s.playerCount+"", 90, (i*square)+(4*textHeight)+top);
    		gc.fillText(at(s.at), 190, (i*square)+(4*textHeight)+top);
    		String[] mo = s.two.split("(?<=\\G.{32})");
    		gc.setTextAlign(TextAlignment.LEFT);
    		for(int tv = 0;tv<mo.length;tv++){
	    		gc.fillText(mo[tv], 250, (i*square)+(tv*textHeight)+top);
    		}

			gc.setTextAlign(TextAlignment.LEFT);
			gc.setFont(Font.font("Cooper Std", FontWeight.BOLD, fontSize*4));
			if(s.comon>0){
				s.comon-=sf;
				if(s.comon>290)gc.setFill(Color.YELLOW);
				else gc.setFill(Color.WHITE);
				gc.fillText("TO ME!", 300, (i*square)+(3*textHeight)+top);
				if(s.comon>250)gc.setFill(Color.WHITE);
			}
			if(s.nice>0){
				s.nice-=sf;
				if(s.nice>290)gc.setFill(Color.GREEN);
				else gc.setFill(Color.WHITE);
				gc.fillText("NICE!", 300, (i*square)+(3*textHeight)+top);
			}
			switch(s.snsplate){
			case 2:
				gc.fillText("Double!", 300, (i*square)+(4*textHeight)+top);
				break;
			case 3:
				gc.fillText("Triple!", 300, (i*square)+(4*textHeight)+top);
				break;
			case 4:
				gc.setFill(Color.LIGHTGREEN);
				gc.fillText("QUAD!!!", 300, (i*square)+(4*textHeight)+top);
				gc.setFill(Color.WHITE);
				break;
			default:
			}
			if(s.snsplate>0){
				s.snsplat-=sf;
				if(s.snsplat<=0)s.snsplate=0;
			}
			// Network info-ish
    		gc.setFont(Font.font("Cooper Std", FontWeight.BOLD, fontSize));
    		gc.setTextAlign(TextAlignment.RIGHT);
    		gc.setFill(v);
    		gc.fillText(i!=0?(s.host?"Hosting ":"")+s.name:"Total", 380, (i*square)+(0*textHeight)+top);
    		gc.fillText("From", 300, (i*square)+(1*textHeight)+top);
    		gc.fillText("To", 400, (i*square)+(1*textHeight)+top);
    		gc.fillText(((i!=0?s.net.bitratedown:s.net.bitrateup)/1000)+" kbps", 311, (i*square)+(2*textHeight)+top);
    		gc.fillText(((i!=0?s.net.bitrateup:s.net.bitratedown)/1000)+" kbps", 411, (i*square)+(2*textHeight)+top);
    		gc.fillText(((i!=0?s.net.packetdown:s.net.packetup))+" p/s", 300, (i*square)+(3*textHeight)+top);
    		gc.fillText(((i!=0?s.net.packetup:s.net.packetdown))+" p/s", 400, (i*square)+(3*textHeight)+top);
    		gc.fillText(((i!=0?s.net.packetsizedown+" bytes":"")), 316, (i*square)+(4*textHeight)+top);
    		gc.fillText(((i!=0?s.net.packetsizeup+" bytes":"")), 416, (i*square)+(4*textHeight)+top);
			gc.setTextAlign(TextAlignment.LEFT);
    		gc.fillText("ping:"+s.bestPing+"ms IP:"+s.ip + ":"+s.port, 260, (i*square)+(5*textHeight)+top);
    		
    		gc.setTextAlign(TextAlignment.RIGHT);
    		//gc.fillText(((i!=0?s.net.movementdown+" md/s":"")), 100, (i*square)+(4*textHeight)+top);
    		//gc.fillText(((i!=0?s.net.movementup+" md/s":"")), 200, (i*square)+(4*textHeight)+top);
    	}
		gc.setFill(Color.WHITE);
		if(show_sent_data){ // used to organized hex data made from the game
        	gc.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
			gc.setTextAlign(TextAlignment.LEFT);
			ArrayList<NNPacket> holdPacket = this.holdPacket;
	    	for(int lv = 0,v = 0,z = 0;holdPacket.size()>0 && lv==0;v++,z++){
	    		gc.setFill(Color.WHITE);
	    		
	    		NNPacket mm = holdPacket.remove(0);
	    		if(mm.data.length()<12)continue;	
	    		if(mm.NNData!=null)for(NNData nndata:mm.NNData){
	    			if(nndata==null){
	    				gc.fillText("null",  600+(lv*500), 100+(12*v++));
	    				continue;
	    			}
	    			gc.fillText("--"+nndata.slot+" "+nndata.size+" "+nndata.nnid+" "+nndata.type+" "+ nndata.unknownTwo,  600+(lv*500), 100+(12*v++));
	    			if(nndata.data==null){
	    				gc.fillText("----null",  600+(lv*500), 100+(12*v++));
	    				continue;
	    			}
		    			
	    			if(nndata.data.length()<4){
	    				continue;
	    			}
	    			gc.fillText("-----" +nndata.data.substring(0, 4),  700+(lv*500), 100+(12*v++));
	    			if(nndata.type.matches("24")){
	    				if(nndata.data.startsWith("03D3")){
	    					String def = deflate(nndata.data.substring(24));
	    					if(def.compareTo("00")==0){
	    						gc.fillText("----e " +nndata.data.substring(0, Math.min(32, nndata.data.length())),  600+(lv*500), 100+(12*v++));
	    					}else{
	    						String[] dem = Demess(def);
	    						for(String ins:dem){
	    							String[] vp = ins.split("(?<=\\G.{32})");
	    							for(String vi:vp)gc.fillText("------" +String.join(" ",vi.split("(?<=\\G.{16})")),  600+(lv*500), 100+(12*v++));
	    							gc.fillText("------",  600+(lv*500), 100+(12*v++));
	    						}
	    					}
	    				}
	    			}else{
						String[] vp = nndata.data.split("(?<=\\G.{32})");
						for(String vi:vp)gc.fillText("------" +String.join(" ",vi.split("(?<=\\G.{16})")),  600+(lv*500), 100+(12*v++));
	    			}
	    		}else{
					gc.fillText("--ept",  600+(lv*500), 32+(200*v++));
	    		}
	    		if(v>30){
	    			v=-1;
	    			lv++;
	    		}
	    	}
		}
		if(show_game_data){// holds and display what's useful hex data from host.
        	gc.setFont(Font.font("Consolas", FontWeight.BOLD, 18));
			gc.setTextAlign(TextAlignment.LEFT);
	    	gc.setFill(Color.WHITE);
			ArrayList<NNPacket> holdPacket = this.holdPacket;
	    	while(holdPacket.size()>0){
	    		NNPacket mm = holdPacket.remove(0);
	    		if(mm.NNData==null)continue;
	    		for(NNData nndata:mm.NNData){
	    			if(nndata==null)continue;
	    			if(nndata.data==null)continue;
	    			if(nndata.data.length()<4)continue;
	    			if(!nndata.type.matches("24"))continue;
	    			if(!nndata.data.startsWith("03D3"))continue;
					String def = deflate(nndata.data.substring(24));
					if(def.compareTo("00")==0)continue;
					String[] dem = Demess(def);
					found:for(String ins:dem){
						String p = ins;
						if(gamedata.size()==0){
							gamedata.add(ins);
							continue;
						}
						for(int i = 0;i<gamedata.size();i++){
							if(gamedata.get(i).startsWith(p.substring(0,4))){
								gamedata.set(i, p);
								continue found;
							}
						}
						gamedata.add(p);
					}
	    		}
	    	}int v=0;
	    	for(String s : gamedata){
	    		String[] vp = s.split("(?<=\\G.{32})");
				for(String vi:vp)gc.fillText(String.join(" ",vi.split("(?<=\\G.{16})")),  600, 100+(12*v++));
				v++;
	    	}
		}
    }

	public static String deflate(String input){try {
		byte[] data  = DatatypeConverter.parseHexBinary(input);
		Inflater inflater = new Inflater();   
		inflater.setInput(data);  
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
		byte[] buffer = new byte[1024];
		while (true) {  
			int count;
			count = inflater.inflate(buffer, 0, buffer.length);
			outputStream.write(buffer, 0, count);
			if(count<=0)break;
		}  
		outputStream.close();
		return DatatypeConverter.printHexBinary(outputStream.toByteArray());
	} catch (IllegalArgumentException | DataFormatException | IOException e){e.printStackTrace();System.out.println(input);return "00";} }
	public String[] Demess(String data){
		int a = 0;
		int b =data.length()/2;
		ArrayList<String> c = new ArrayList<String>();
		while(a<b-4){
			int s = Integer.parseInt(data.substring((a*2)+2,(a*2)+4),16);
			if(b<a+s){
				//System.out.println(data.substring((a*2)+2));
				break;
			}
			c.add(data.substring(a*2,a*2+s*2));
			a+=s;
		}
		String[] out = new String[c.size()];
		int i = 0;
		for(String v:c)out[i++]=v;
		return out;
	}
	public static String reverseHex(String originalHex) {
	    int lengthInBytes = originalHex.length() / 2;
	    char[] chars = new char[lengthInBytes * 2];
	    for (int index = 0; index < lengthInBytes; index++) {
	        int reversedIndex = lengthInBytes - 1 - index;
	        chars[reversedIndex * 2] = originalHex.charAt(index * 2);
	        chars[reversedIndex * 2 + 1] = originalHex.charAt(index * 2 + 1);
	    }
	    return new String(chars);
	}
	public void onKeyPressed(KeyEvent ke) {
		if(ke.getCode().equals(KeyCode.DIGIT1)){
			show_sent_data = true;
			show_game_data = false;
		}
		if(ke.getCode().equals(KeyCode.DIGIT2)){
			show_sent_data = false;
			show_game_data = true;
		}
		
		if(ke.getCode().equals(KeyCode.E)){
			while(gamedata.size()>0)gamedata.remove(0);
		}
	}
	public static String at(int pos){
		switch(pos){
		case 0: return "not ready";
		case 1: return "In lobby";
		case 4: return "not playing";
		case 5: return "Ready";
		case 6: return "Let's battle!";
		case 7: return "Getting ready";
		case 8: return "Ready...";
		case 9: return "GO!";
		case 10: return "1 minet Left";
		case 11: return "OVERTIME!";
		case 12: return "Results";
		case 13: return "Battle again?";
		case 14: return "Back to Lobby";
		default: return Integer.toHexString(pos);
		}
	}
	public static void ping(Player player){
		Thread r  = new Thread(){
			@Override public void run(){
			Pattern findIP = Pattern.compile("(\\d*\\.\\d*\\.\\d*\\.\\d*)");
			Pattern findMS = Pattern.compile("(\\d+) ms");
				while(!player.left){
					try {
						Process proc;
							proc = Runtime.getRuntime().exec("tracert -d -w 1200 -h 20 "+player.ip);
						
						BufferedReader r = new BufferedReader(new InputStreamReader(proc.getInputStream()));
						String res = "";
						while (true) {
							String line = r.readLine();
							if (line == null) break;
							res+=line;
							//System.out.println(line);
						}
						Matcher matcher = findIP.matcher(res);
						ArrayList<String> nodes = new ArrayList<String>();
						while (matcher.find())for (int i = 1; i <= matcher.groupCount(); i++) nodes.add(matcher.group(i));
						nodes.remove(0);
						nodes.remove(0);
						nodes.remove(nodes.size()-1);
				
						 matcher = findMS.matcher(res);
						ArrayList<Integer> pings = new ArrayList<Integer>();
						while (matcher.find())for (int i = 1; i <= matcher.groupCount(); i++) pings.add(Integer.parseInt(matcher.group(i)));
				
						player.bestPing = (pings.get(pings.size()-1)+pings.get(pings.size()-2)+pings.get(pings.size()-3))/3;
						while(player.cord.size()>0)player.cord.remove(0);
						while(player.ipn.size()>0)player.ipn.remove(0);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try{sleep(1000);}catch(Exception e){}
				}
			}
		};
		r.start();
	}
}
