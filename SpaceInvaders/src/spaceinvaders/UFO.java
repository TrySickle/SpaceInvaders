package spaceinvaders;

import java.awt.Rectangle;

public class UFO {
	
	private int x, y, speed;
	private Rectangle r = new Rectangle(0, 0, 0, 0);
	private boolean isAlive = false;
	
	public UFO() {
		x = 0;
		y = 0;
		r.setBounds(x, y, 26, 26);
	}

	public void update() {
		if (x < 448)
			x += 6;
			r.setBounds(x, y, 26, 26);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSpeed() {
		return speed;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Rectangle getR() {
		return r;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setR(Rectangle r) {
		this.r = r;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

}
