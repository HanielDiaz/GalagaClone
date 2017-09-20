package spaceship;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

public class enemy {
	public static enum enemyType {
		Grunt,
		Soldier,
		Officer,
		Captain,
		Lord,
		God
	}
	private enemyType enemy;
	private int health;
	private Random rand;
	public Point Coord;
	public int size = 50;
	
	public enemy(enemyType enemy) {
		rand = new Random();
		switch(enemy){
		case God:
			health = 6;
			break;
		case Lord:
			health = 5;
			break;
		case Captain:
			health = 4;
			break;
		case Officer:
			health = 3;
			break;
		case Soldier:
			health = 2;
			break;
		case Grunt:
			health = 1;
			Coord = new Point(rand.nextInt(400) + 50,rand.nextInt(200) + 50);
			break;
		default:
			health = 0;
			break;
		
		}
		this.enemy = enemy;
	}
	
	public void render(Graphics2D g2D){
		switch(enemy){
		case God:
			break;
		case Lord:
			break;
		case Captain:
			break;
		case Officer:
			break;
		case Soldier:
			break;
		case Grunt:
			g2D.setColor(Color.GRAY);
			g2D.fillRect(Coord.x, Coord.y, 50, 50);
			break;
		default:
			g2D.setColor(Color.GRAY);
			g2D.fillRect(Coord.x, Coord.y, 50, 50);
			break;
		}
		
	}
	public int collision(int damage){
		this.health -= damage;
		return this.health;
	}

}
	