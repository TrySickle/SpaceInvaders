package spaceinvaders;

import java.awt.Rectangle;

public class AlienProjectile {

	private int centerX, centerY, speed;
	private boolean visible;
	private Rectangle r;
	
	public AlienProjectile(int x, int y, int speed) {
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
		if (centerY > 465) {
			visible = false;
			r = null;
		}
		if (centerY < 465) {
			checkCollision();
		}

	}

	public void checkCollision() {
		if (r.intersects(Main.getShip().getR())) {
			visible = false;
			Main.getShip().setLives(Main.getShip().getLives() - 1);
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

	public boolean isVisible() {
		return visible;
	}

	public Rectangle getR() {
		return r;
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

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setR(Rectangle r) {
		this.r = r;
	}

}
