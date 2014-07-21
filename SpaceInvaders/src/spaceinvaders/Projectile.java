package spaceinvaders;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Projectile {

	private int centerX, centerY, speed;
	private boolean visible;
	private Rectangle r;
	
	public Projectile(int x, int y, int speed) {
		centerX = x;
		centerY = y;
		this.speed = speed;
		visible = true;
		
		r = new Rectangle(0, 0, 0, 0);
	}
	
	public void update() {
		centerY += speed;
		r.setBounds(centerX - 1, centerY, 3, 8);
		
		// make sure is on screen
		if (centerY < 0) {
			visible = false;
			r = null;
		}
		if (centerY > 0) {
			checkCollision();
		}
	}
	
	private void checkCollision() {
		if (r.intersects(Main.getUfo().getR())) {
			visible = false;
			Main.score += 100;
			Main.getUfo().setAlive(false);
		}
		for (ArrayList<Enemy> enemy : Main.enemies) {
			for (Enemy e : enemy) {
				if (e.isAlive()) {
					if (r.intersects(e.getR())) {
						visible = false;
						switch (e.getType()) {
						case 1:
							Main.score += 10;
							break;
						case 2:
							Main.score += 20;
							break;
						case 3:
							Main.score += 30;
							break;
						}
						e.die();
						e.setR(null);
					}
				}
			}
		}
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public int getSpeed() {
		return speed;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Rectangle getR() {
		return r;
	}

	public void setR(Rectangle r) {
		this.r = r;
	}
}
