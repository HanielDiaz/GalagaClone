package spaceship;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;


public class spaceship  {
	private boolean error = false;
	private BufferedImage Buffimage;
	private int Frame = 1, xPos = 218;
	public List<Point> missiles = new ArrayList<Point>();
	private Iterator<Point> it;
	private Point topLeft, ShipFrame1[] = {new Point(8,0), new Point(176,156)}, 
						missile, ShipFrame2[] = {new Point(178,0), new Point(178+170,162)},
							 ShipFrame3[] = {new Point(350,0), new Point(350+168,162)};
	
	public spaceship(File file,Point topLeft) {
       this.topLeft = topLeft;
		try {
			Buffimage = ImageIO.read(file);
			error = false;
		} catch (IOException e) {
			error = true;
			e.printStackTrace();
		}
	}

	public void render(Graphics2D g2D){
		if(error == false){	
			if(!missiles.isEmpty()){
				it = missiles.iterator();
				while(it.hasNext()){
					missile = it.next();
					g2D.drawImage(Buffimage, missile.x, missile.y, missile.x + 5, missile.y + 16, 0, 0,5, 16, null);
				}
				it = missiles.iterator();
				while(it.hasNext()){
					missile = it.next();
					if(!Game.enemies.isEmpty()){
						Iterator<enemy> iter = Game.enemies.iterator();
						while(iter.hasNext()){
							enemy enemy = iter.next();
							if(missile.x > enemy.Coord.x && missile.x < enemy.Coord.x + enemy.size && 
							   missile.y > enemy.Coord.y && missile.y < enemy.Coord.y + enemy.size){
								if(enemy.collision(1) <= 0)
									iter.remove();
								
							}
						}
					}
					if((missile.y -= 5)<=0)
						it.remove();
				}
			}
			if(Frame++ <= 10)
				g2D.drawImage(Buffimage, xPos, 346, topLeft.x + 40, topLeft.y + 180, ShipFrame1[0].x, ShipFrame1[0].y,ShipFrame1[1].x, ShipFrame1[1].y, null);
			else if(Frame++ <= 20)
				g2D.drawImage(Buffimage, xPos, 344, topLeft.x + 40, topLeft.y + 180, ShipFrame2[0].x, ShipFrame2[0].y,ShipFrame2[1].x, ShipFrame2[1].y, null);
			else if(Frame <= 30 ||Frame >= 29){
				g2D.drawImage(Buffimage, xPos, 344, topLeft.x + 40, topLeft.y + 180,  ShipFrame3[0].x, ShipFrame3[0].y,ShipFrame3[1].x, ShipFrame3[1].y, null);
				if(Frame >= 30)
					Frame = 1;
			}	
		}
		else
			System.out.println("Image was not loaded");	
	}
	public void Move(Game.dir Direction){
		switch(Direction){
		case Left:
			xPos -= 5;
			topLeft.x -= 5;
			break;
		case None:
			break;
		case Right:
			xPos += 5;
			topLeft.x += 5;
			break;
		default:
			break;
		}
			
	}
	public void Fire(){
		missiles.add(new Point(xPos + 170/4 - 10,350));
	}
	
	
}
