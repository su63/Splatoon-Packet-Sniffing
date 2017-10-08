package splatoon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import javax.swing.JOptionPane;
import javax.xml.bind.DatatypeConverter;

import org.jnetpcap.*;
import org.jnetpcap.nio.JBuffer;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class splatlive extends Application {
    int fpt=0;
    handelbid ff = new handelbid();
    public static void init(String[] arg){launch(arg);};
	@Override public void start(Stage primaryStage) {
		String file = ff.loadFile();
		if(file.isEmpty()){
			List<PcapIf> alldevs = null;
	        StringBuilder errbuf = null;
			try{
				alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs  
				errbuf = new StringBuilder(); // For any error msgs  
		        /*************************************************************************** 
		         * First get a list of devices on this system 
		         **************************************************************************/  
		        int r = Pcap.findAllDevs(alldevs, errbuf);  
		        if (r == Pcap.NOT_OK || alldevs.isEmpty()) {  
		        	JOptionPane.showMessageDialog(null,"Can't read list of devices, "+errbuf.toString()+" This App Will Close.","WinPCap Error - Nentendo Network Packet Analyser",JOptionPane.ERROR_MESSAGE);  
		            System.exit(0); 
		        }  
			}catch(Exception e){
				JOptionPane.showMessageDialog(null,"Cannot List Network Devices. Is WinPCap or Wireshark Installed? This App Will Close.","Nentendo Network Packet Analyser",JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			
			
	  
	        int i = 0;
	        String[] list = new String[alldevs.size()];
	        for (PcapIf device : alldevs) {
	        	list[i] =  "#"+i+": "+(
	                (device.getDescription() != null) ? device.getDescription()  
	                    : "No description available"   );
	        	i++;
	        }  
	        String network = (String) JOptionPane.showInputDialog(null, "Select Network Device", "Nentendo Network Packet Analyser", JOptionPane.PLAIN_MESSAGE, null, list, list[0]);
	        if(network == null){
				JOptionPane.showMessageDialog(null,"Nothing Was Selected. This App Will Close.","Nentendo Network Packet Analyser",JOptionPane.PLAIN_MESSAGE);
	        	System.exit(0);
	        }
	        PcapIf device = null; 
	        for(i = 0;i < list.length;i++){
	        	if(network.equals(list[i]))device = alldevs.get(i);
	        }
	        if(device == null){
				JOptionPane.showMessageDialog(null,"Something Whent Wrong here. This app will close.","Nentendo Network Packet Analyser",JOptionPane.ERROR_MESSAGE);
				System.exit(0);        	
	        }
	  
	        /*************************************************************************** 
	         * Second we open up the selected device 
	         **************************************************************************/  
	        int snaplen = 64 * 1024;           // Capture all packets, no trucation  
	        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets  
	        int timeout = 10 * 1000;           // 10 seconds in millis  
	        Pcap pcap =  
	            Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);  
	  
	        if (pcap == null) {  
	            System.err.printf("Error while opening device for capture: "  
	                + errbuf.toString());  
				JOptionPane.showMessageDialog(null,"Error while opening device for capture: "  
		                + errbuf.toString()+ " This app will close.","WinPCap Error - Nentendo Network Packet Analyser",JOptionPane.ERROR_MESSAGE);
				System.exit(0);   
	        }
	
	        /*************************************************************************** 
	         * Third we create a packet handler which will receive packets from the 
	         * libpcap loop. 
	         **************************************************************************/  
	        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {  
	            public void nextPacket(PcapPacket packet, String user) {
	                int size = packet.size();  
	                JBuffer buffer = packet; 
	                byte[] array = buffer.getByteArray(0, size); 
	                UDPPacket a = new UDPPacket(array);
	            	onNewPacket(a);
	            }  
	        };
	        Thread t = new Thread(){
	        	@Override public void run(){	
	        		pcap.loop(Integer.MAX_VALUE, jpacketHandler,"");
	        	}
	        };
	        t.start();
		}else{
			final ArrayList<UDPPacket> packets = fileRead.Read(new File(file));
			 Thread sf = new Thread(){
				 @Override public void run(){
					 int size = packets.size();
					 int StartTime = packets.get(0).time;
					 int poz = 0;
					 int time = StartTime;
					 long curTime = System.currentTimeMillis();
						try {sleep(2000);}
					 	catch (InterruptedException e) {}
					 while(true){
						 if(poz>=size){
							 poz = 0;
							 time = StartTime;
						 }
						 UDPPacket packet = packets.get(poz);
						 long snooze = (packet.time-time)-(System.currentTimeMillis()-curTime);
						 if(snooze>0)
							try {sleep(snooze);}
						 	catch (InterruptedException e) {}
						 onNewPacket(packet);
						 time = packet.time;
						 curTime=System.currentTimeMillis();
						 poz++;
					 }
				 }
			 };
			sf.start();
		}
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(final WindowEvent event) {
            	System.exit(0);
            }
        });
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,new EventHandler<KeyEvent>() {
            @Override
            public void handle(final KeyEvent event) {
            	onKeyPressed(event);
            }
        });
        primaryStage.setTitle("Nentendo Network Packet Analyser");
        Group root = new Group();
        Canvas canvas = new Canvas(1280, 720);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(Font.font("Consolas"));
        AnimationTimer frameRateMeter = new AnimationTimer() {
            @Override public void handle(long now) {
            	onScreenRefresh(gc);
            }
        };
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        frameRateMeter.start();

	}

    public static String deflate(String input){try {
    	byte[] data  = DatatypeConverter.parseHexBinary(input);
    	Inflater inflater = new Inflater();   
    	inflater.setInput(data);  
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
    	byte[] buffer = new byte[1024];
    	int loop = 0;
    	while (!inflater.finished()) {  
    		int count;
			count = inflater.inflate(buffer);
    		outputStream.write(buffer, 0, count);
    		loop++;
    		if(loop>10)return"00";
    	}  
    	outputStream.close();
    	return DatatypeConverter.printHexBinary(outputStream.toByteArray());
	} catch (DataFormatException | IOException e){return "00";} }
    public void onNewPacket(UDPPacket packet){ff.onNewPacket(packet);}
    public void onScreenRefresh(GraphicsContext gc){ff.onScreenRefresh(gc);}
    public void onKeyPressed(KeyEvent ke){ff.onKeyPressed(ke);}
}