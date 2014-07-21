package spaceinvaders;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Enemy {

	private int x, y, speed, type, timer;
	private static int delay;
	private boolean alive, movingRight, movingLeft;
	private Rectangle r;
	private static ArrayList<AlienProjectile> alienProjectiles = new ArrayList<AlienProjectile>();
	
	public Enemy(int x, int y, int t, int d) {
		this.x = x;
		this.y = y;
		type = t;
		speed = 5;
		alive = true;
		delay = d;
		movingRight = true;
		r = new Rectangle(0, 0, 0, 0);
	}
	
	public void update() {
		// move
		// maybe speed checking
		timer += 17;
		move();
		r.setBounds(x, y, 26, 26);
		if (y > 480) {
			Main.getShip().setLives(Main.getShip().getLives() - 1);
		}
	}
	
	public void move() {
		if (timer > delay && movingRight == true) {
			x += speed;
			timer = 0;
		} else if (timer > delay && movingLeft == true) {
			x += -speed;
			timer = 0;
		}
		// check if moving left or right based on end of each row's position
		// somehow, rows should be coordinated
		// check x position, increment, if this, then descend
	}

	public void shoot() {
		alienProjectiles.add(new AlienProjectile(x + 13, y, 5)); // create missile
	}
	
	public void die() {
		// trigger death animation?
		setAlive(false);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getType() {
		return type;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getTimer() {
		return timer;
	}

	public static int getDelay() {
		return delay;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public static void setDelay(int d) {
		delay = d;
	}
	
	public static void reduceDelay(int curDelay) {
		delay = curDelay - 100;
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

	public Rectangle getR() {
		return r;
	}

	public void setR(Rectangle r) {
		this.r = r;
	}

	public ArrayList<AlienProjectile> getAlienProjectiles() {
		return alienProjectiles;
	}

	public void setAlienProjectiles(ArrayList<AlienProjectile> alienProj) {
		alienProjectiles = alienProj;
	}

}
