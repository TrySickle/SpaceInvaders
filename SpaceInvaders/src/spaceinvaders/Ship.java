package spaceinvaders;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Ship {

	// constants
	final int MOVESPEED = 5; 
	final int DELAY = 500;
	
	private int timer = 0; // missile firing delay
	private int centerX = 224;
	private int centerY = 450;
	private int speedRight = 0;
	private int speedLeft = 0;
	private int lives = 3;
	
	private boolean movingRight = false;
	private boolean movingLeft = false;
	
	private Rectangle r =  new Rectangle(0, 0, 0, 0);
	
	// list of projectiles
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	public void update() {
		if (movingRight == true && centerX < 434) { // makes sure is in bounds
			centerX += speedRight;
		} else if (movingLeft == true && centerX > 15) {
			centerX += speedLeft;
		}
		r.setBounds(centerX - 13, centerY - 8, 26, 16);
		timer += 20; // increments timer by 20, updates every 17 milliseconds
	}
	
	public void shoot() {
		if (timer > DELAY) { // checks for timer vs delay
			projectiles.add(new Projectile(centerX - 2, centerY - 8, -7)); // create missile
			timer = 0;
		}
	}
	
	public void moveRight() {
		movingRight = true;
		speedRight = MOVESPEED;
	}
	
	public void moveLeft() {
		movingLeft = true;
		speedLeft = -MOVESPEED;
	}
	
	public void stopRight() {
		movingRight = false;
		speedRight = 0;
	}
	
	public void stopLeft() {
		movingLeft = false;
		speedLeft = 0;
	}
	
	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
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

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public Rectangle getR() {
		return r;
	}

	public void setR(Rectangle r) {
		this.r = r;
	}

}
