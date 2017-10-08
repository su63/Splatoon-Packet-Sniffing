package splatoon;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class MapPrinter {
	Image orange;
	Image blue;
	MapPrinter(){
		orange = new Image("file:orange-squid.png");
		blue = new Image("file:blue-squid.png");
	}
	public void Print(ArrayList<Player> players, GraphicsContext gc){
		for(Player player: players){
			gc.save();
			gc.translate(640-(player.x/512), 360-(player.z/512));
			gc.rotate((double)player.camRotH*360*Math.PI/(double)0xFFFF);
			if(player.team==1){
				gc.drawImage(orange,-8,-8, 16, 16);
				gc.setFill(Color.ORANGE);
			}else{
				gc.drawImage(blue, -8, -8,16,16);
				gc.setFill(Color.BLUE);
			}
			gc.restore();
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setFont(new Font("Cooper Std",16));
			gc.fillText(player.name, 620-player.x/1024, 360-player.y/1024);
			
		}
	}
}
