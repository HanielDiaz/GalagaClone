package spaceship;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import spaceship.enemy.enemyType;
/* NEED TO CHANGE FROM RESOURCE LOADER TO REGULAR FILE
 * TO BE ABLE TO RUN ON ECLIPSE
 */
public class Game extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 500, HEIGHT = 500;
	private static List<Point> stars = new ArrayList<Point>();
	private static Iterator<Point> it;
	private static Action moveR = new MoveR();
	private static Action moveL = new MoveL();
	private static Action noR = new NoR();
	private static Action noL = new NoL();
	private static Action space = new Space();
	private static Action nospace = new NoSpace();
	private static Random random = new Random();
	private static boolean fire = false,ShipSelector = false;
	private int fireLag = 0;
	private static int Frames = 0;
	private static BufferedImage HeroMenu = null, Menu = null, Start = null;
	private static Point HeroFrames[] = {new Point(17,0),new Point(197,227), new Point(232,0),new Point(232+180,230),
			  new Point(447,0),new Point(447+197,230), new Point(679,0),new Point(679 + 180,230)};

	//public spaceship Spaceship = new spaceship(new File("lib/RedSpaceShip.png"), new Point(WIDTH/2,HEIGHT/2));
	public static spaceship Spaceship = null;
	public static List<enemy> enemies = new ArrayList<enemy>();
	private Iterator<enemy> iter;
	private enum Ship{
		Red,
		Blue,
		Orange,
		None
	}
	public enum dir{
		Left,
		Right,
		None
	}
	public enum GameState{
		Pause,
		Menu,
		SpaceShip,
		Hero
	}
	private static dir Direction = dir.None;
	private static GameState state = GameState.Menu;
	private static Ship ship = Ship.None;

	public Game() {
		JFrame j = new JFrame("SpaceShip");
		Timer timer = new Timer(10,this);

		j.setVisible(true);
		j.setSize(WIDTH, HEIGHT);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setLocationRelativeTo(null);
		j.setFocusable(true);
		j.requestFocusInWindow();
		
		// KEY BINDINGS // 
		this.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "MoveR");
		this.getActionMap().put("MoveR", moveR);
		this.getInputMap().put(KeyStroke.getKeyStroke("released RIGHT"), "NoR");
		this.getActionMap().put("NoR", noR);
		this.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "MoveL");
		this.getActionMap().put("MoveL", moveL);
		this.getInputMap().put(KeyStroke.getKeyStroke("released LEFT"), "NoL");
		this.getActionMap().put("NoL", noL);
		this.getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "Space");
		this.getActionMap().put("Space", space);
		this.getInputMap().put(KeyStroke.getKeyStroke("released SPACE"), "NoSpace");
		this.getActionMap().put("NoSpace", nospace);

		
		
		timer.start();	
		j.add(this);
		this.setFocusable(true);
		this.requestFocusInWindow();
		}

	public static void main(String args[]){
	new Game();	
	
	}
	private void ShipSelector(Graphics g){
		switch(ship){
		case Blue:
			break;
		case Orange:
			break;
		case Red:
			//setup(ResourceLoader.load("lib/RedSpaceShip.png"));
			setup(new File("lib/REdSpaceShip.png"));
			break;
		case None:
			try {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, WIDTH, HEIGHT);
				g.drawImage(ImageIO.read(new File("lib/RedShip.png")), 50, 50, 100, 100, null);
				//g.drawImage(ImageIO.read(ResourceLoader.load("lib/RedShip.png")), 50, 50, 100, 100, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			break;
		
		}
	}
	private void renderMenu(Graphics g){
		if(Menu == null){
			try {
				//Menu = ImageIO.read(ResourceLoader.load("lib/Menu.png"));
				Menu = ImageIO.read(new File("lib/Menu.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		g.drawImage(Menu, 0, 0,WIDTH,HEIGHT, null);
		if(!ShipSelector){
			if(HeroMenu == null){
				try {
					HeroMenu = ImageIO.read(new File("lib/Hero.png"));
					//HeroMenu = ImageIO.read(ResourceLoader.load("lib/Hero.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			g.setColor(Color.BLACK);
			if(Frames <= 20){
				g.drawImage(HeroMenu, 360, 320, 315, 368, HeroFrames[0].x, HeroFrames[0].y,HeroFrames[1].x, HeroFrames[1].y, null);
			}
			else if(Frames <= 40){
				g.drawImage(HeroMenu, 359, 320, 315, 368, HeroFrames[2].x, HeroFrames[2].y,HeroFrames[3].x, HeroFrames[3].y, null);
			}
			else if(Frames <= 60 ){
				g.drawImage(HeroMenu, 363, 320, 315, 368,  HeroFrames[4].x, HeroFrames[4].y,HeroFrames[5].x, HeroFrames[5].y, null);
			}
				else if(Frames > 60 ){
				g.drawImage(HeroMenu, 358, 320, 315, 368,  HeroFrames[6].x, HeroFrames[6].y,HeroFrames[7].x, HeroFrames[7].y, null);
			}	
			if(Start == null){
				/*try {
					Start = ImageIO.read(new File("/Start.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
			Frames++;
			if(Frames >= 80)
				Frames = 1;
		}
		else
			ShipSelector(g);

	}
	private void renderStars(Graphics g){
				if(stars.isEmpty())
					for(int i = 0; i < 200; i++)
						stars.add(new Point(random.nextInt(490) + 5, random.nextInt(490) + 5));
				it = stars.iterator();
				int iterations = random.nextInt(10)+ 10;
				for(int i = 0; i < iterations; i++){
					while(it.hasNext())
					if((it.next().y += 7) >=500)
						it.remove();
				}
				it = stars.iterator();
				
				while(it.hasNext()){
					Point point = it.next();
					int alpha = 100;
					for(int j = 0; j < 9;j++){
						g.setColor(new Color(209, 207, 198, alpha));
						alpha -= 10;
						
						if(alpha <= 10)
							alpha = 10;
						g.fillRect(point.x,point.y - (5*j), 5, 6);
					}
				}
				stars.add(new Point(random.nextInt(490) + 5, 0));		
	}
	
	private static void setup(File file){
		Spaceship = new spaceship(file,new Point(WIDTH/2,HEIGHT/2));
	}
	private void render(Graphics g){
		
		switch(state){
		case Hero:
			break;
		case Menu:
			g.setColor(Color.gray);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			renderMenu(g);
			break;
		case Pause:
			break;
		case SpaceShip:
			g.setColor(Color.blue.darker().darker().darker().darker().darker().darker());
			g.fillRect(0, 0, WIDTH, HEIGHT);
			renderStars(g);
			Spaceship.render((Graphics2D)g);
			iter = enemies.iterator();
			while(iter.hasNext()){
				iter.next().render((Graphics2D)g);
			}
			break;
		default:
			break;
			
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		render(g);
	}
	static class MoveR extends AbstractAction{
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			Direction = dir.Right;
			System.out.println("Moved Right!\n");  		
		}
	}
	static class NoR extends AbstractAction{
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			Direction = dir.None;
			System.out.println("You released right!\n");  		
		}
	}
	static class MoveL extends AbstractAction{
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			Direction = dir.Left;
			System.out.println("Move Left!\n");  		
		}
	}
	static class NoL extends AbstractAction{
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			Direction = dir.None;
			System.out.println("You released left!\n");  		
		}
	}
	static class Space extends AbstractAction{
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			switch(state){
			case Hero:
				break;
			case Menu:
				//ShipSelector = true;
				//setup(ResourceLoader.load("lib/RedSpaceShip.png"));
				setup(new File("lib/RedSpaceShip.png"));
				Game.state = GameState.SpaceShip;
				break;
			case Pause:
				break;
			case SpaceShip:
				fire = true;
				break;
			default:
				break;
			
			}
			
		}
	}
	static class NoSpace extends AbstractAction{
		private static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e) {
			System.out.println("You released Space!\n");  		
			fire = false;
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(state){
		case Hero:
			break;
		case Menu:
			break;
		case Pause:
			break;
		case SpaceShip:
			Spaceship.Move(Direction);
			if(fireLag++ >=  10){
				if(fire)
					Spaceship.Fire();
				fireLag = 0;
			}
			if(enemies.isEmpty()){
				enemies.add(new enemy(enemyType.Grunt));
			}
			break;
		default:
			break;
		
		}
		this.repaint();
	}
}
