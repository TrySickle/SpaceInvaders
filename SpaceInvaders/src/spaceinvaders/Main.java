package spaceinvaders;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")
public class Main extends Applet implements Runnable, KeyListener {

	enum GameState {
		Running, Dead, Loading
	}

	GameState state = GameState.Running;

	private static Ship ship; // the ship
	private static UFO ufo;
	private Image image, shipImage, missileImage, alien1Image, alien2Image,
			alien3Image, ufoImage, alienMissile; // various images
	private Graphics second; // used for magic double buffering
	private URL base; // used for image loading
	public static ArrayList<Projectile> projectiles; // holds all missiles fired
														// by
	// ship
	private ArrayList<AlienProjectile> alienProjectiles; // holds alien missiles
	public static ArrayList<ArrayList<Enemy>> enemies = new ArrayList<ArrayList<Enemy>>(); // 2D
																							// arraylist
																							// holds
																							// the
																							// aliens
	public static int score;
	private int alienTimer = 250;
	private int ufoTimer = 0;
	private boolean respawning = false;
	private int baseDelay = 1000;

	@Override
	public void init() {
		// applet initialization
		setSize(448, 520);
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Space Invaders");
		frame.setResizable(false);

		// for image loading
		try {
			base = getDocumentBase();
		} catch (Exception e) {
			// TODO: handle exception
		}

		// initializes enemy arrayList
		createEnemies();

		// images
		shipImage = getImage(base, "assets/ship.png");
		alien1Image = getImage(base, "assets/alien1.png");
		alien2Image = getImage(base, "assets/alien2.png");
		alien3Image = getImage(base, "assets/alien3.png");
		ufoImage = getImage(base, "assets/ufo.png");
		missileImage = getImage(base, "assets/missile.png");
		alienMissile = getImage(base, "assets/alienMissile.png");
		score = 0;

	}

	@Override
	public void start() {
		// create ship
		ship = new Ship();

		fillEnemies();

		ufo = new UFO();

		// create and start thread
		Thread thread = new Thread(this);
		thread.start();

	}

	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void run() {
		if (state == GameState.Running) {
			while (true) {
				if (ship.getLives() < 1) {
					state = GameState.Dead;
				}
				ufoTimer += 17;
				ship.update(); // updates ship's position

				if (ufoTimer > 10000) {
					ufo = new UFO();
					ufo.setAlive(true);
					ufoTimer = 0;
				}
				if (ufo.isAlive()) {
					ufo.update();
				}

				// initialize list of projectiles
				projectiles = ship.getProjectiles();
				for (int i = 0; i < projectiles.size(); i++) {
					Projectile p = projectiles.get(i);
					if (p.isVisible() == true) { // only update projectile if
													// visible/on screen
						p.update();
					} else {
						projectiles.remove(i);
					}
				}

				checkAlive();
				if (respawning == false) {
					updateAliens();
				} else {
					respawn();
				}

				alienProjectiles = enemies.get(0).get(0).getAlienProjectiles();
				for (int i = 0; i < alienProjectiles.size(); i++) {
					AlienProjectile p = alienProjectiles.get(i);
					if (p.isVisible() == true) { // only update projectile if
													// visible/on screen
						p.update();
					} else {
						alienProjectiles.remove(i);
					}
				}

				repaint();

				try {
					Thread.sleep(17); // thread will sleep for 17 milliseconds,
										// basically 60 fps
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void paint(Graphics g) {
		if (state == GameState.Running) {
			g.drawImage(shipImage, ship.getCenterX() - 13,
					ship.getCenterY() - 8, this); // draw ship, compensates for
													// using center coordinates

			// draws all projectiles
			for (Projectile p : projectiles) {
				g.drawImage(missileImage, p.getCenterX(), p.getCenterY(), this);
			}

			for (AlienProjectile p : alienProjectiles) {
				g.drawImage(alienMissile, p.getCenterX(), p.getCenterY(), this);
			}

			// draws all enemies
			for (ArrayList<Enemy> en : enemies) {
				for (Enemy e : en) {
					if (e.isAlive()) {
						g.drawImage(getAlienImage(e.getType()), e.getX(),
								e.getY(), this);
					}
				}
			}

			if (ufo.isAlive()) {
				g.drawImage(ufoImage, ufo.getX(), ufo.getY(), this);
			}

			// bottom of screen, scoring/lives
			g.setColor(Color.GREEN);
			g.drawLine(0, 480, 448, 480);
			g.drawString("SCORE: " + score, 15, 500);
			g.drawString(Integer.toString(ship.getLives()), 300, 500);
			switch (ship.getLives()) {
			case 3:
				g.drawImage(shipImage, 330, 485, this);
				g.drawImage(shipImage, 365, 485, this);
				g.drawImage(shipImage, 400, 485, this);
				break;
			case 2:
				g.drawImage(shipImage, 330, 485, this);
				g.drawImage(shipImage, 365, 485, this);
				break;
			case 1:
				g.drawImage(shipImage, 330, 485, this);
				break;
			}
			// g.setFont(new Font("", Font.PLAIN, 20));
		} else if (state == GameState.Dead) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 448, 520);
			g.setColor(Color.GREEN);
			g.drawString("GAME OVER", 195, 200);
			g.drawString("PRESS ENTER TO RESTART", 150, 260);

		}
	}

	// handles keys
	@Override
	public void keyPressed(KeyEvent e) {
		if (state == GameState.Running) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				ship.moveLeft();
				break;
			case KeyEvent.VK_RIGHT:
				ship.moveRight();
				break;
			case KeyEvent.VK_SPACE:
				ship.shoot();
				break;
			case KeyEvent.VK_CONTROL:
				for (int x = 1; x <= 4; x++) {
					for (int y = 0; y <= 11; y++) {
						enemies.get(x).get(y).die();
					}
				}
			}
		} else if (state == GameState.Dead) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				restart();
			}
		}

	}

	private void restart() {
		// create ship
		ship = new Ship();
		enemies.clear();
		createEnemies();
		fillEnemies();
		baseDelay = 1000;
		score = 0;
		ufoTimer = 0;
		respawning = false;
		ufo = new UFO();
		state = GameState.Running;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (state == GameState.Running) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				ship.stopLeft();
				break;
			case KeyEvent.VK_RIGHT:
				ship.stopRight();
				break;
			case KeyEvent.VK_SPACE:
				break;
			}
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	// magic double buffering
	@Override
	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);
	}

	// initializes list of enemies, creates 5 rows
	public void createEnemies() {
		ArrayList<Enemy> row1 = new ArrayList<Enemy>();
		ArrayList<Enemy> row2 = new ArrayList<Enemy>();
		ArrayList<Enemy> row3 = new ArrayList<Enemy>();
		ArrayList<Enemy> row4 = new ArrayList<Enemy>();
		ArrayList<Enemy> row5 = new ArrayList<Enemy>();

		enemies.add(row1);
		enemies.add(row2);
		enemies.add(row3);
		enemies.add(row4);
		enemies.add(row5);
	}

	public void updateAliens() {
		alienTimer += 17;

		if (enemies.get(0).get(0).isMovingRight()) {
			if (getRightMost() > 405) {
				Enemy.setDelay(Enemy.getDelay() - 100);
				for (ArrayList<Enemy> en : enemies) {
					for (Enemy e : en) {
						e.setMovingRight(false);
						e.setMovingLeft(true);
						e.setY(e.getY() + 20);

					}
				}
			}
		} else if (enemies.get(0).get(0).isMovingLeft()) {
			if (getLeftMost() < 15) {
				Enemy.setDelay(Enemy.getDelay() - 100);
				for (ArrayList<Enemy> en : enemies) {
					for (Enemy e : en) {
						e.setMovingRight(true);
						e.setMovingLeft(false);
						e.setY(e.getY() + 20);

					}
				}
			}
		}

		if (alienTimer > 500) {
			alienTimer = 0;
			ArrayList<Enemy> bottom = getBottom();
			Random r = new Random();
			bottom.get(r.nextInt(bottom.size())).shoot();

		}

	}

	public void respawn() {
		baseDelay -= 200;
		enemies.clear();
		createEnemies();
		fillEnemies();

		respawning = false;

	}

	public void fillEnemies() {
		int eX = 20; // the x coordinate of the alien
		int eY = 20; // the y coordinate of the alien
		for (int x = 0; x < 12; x++) { // 10 aliens per row
			enemies.get(0).add(new Enemy(eX, eY, 3, baseDelay)); // type 3
			eX += 30; // aliens are placed 40 pixels away from each other
		}

		// row 2 & 3
		eX = 20; // reset coordinates
		eY = 50;
		for (int y = 1; y <= 2; y++) { //
			for (int x = 0; x < 12; x++) {
				enemies.get(y).add(new Enemy(eX, eY, 2, baseDelay)); // type 2
				eX += 30;
			}
			eX = 20;
			eY = 80;
		}

		// row 3 & 4
		eX = 20; // reset coordinates
		eY = 110;
		for (int y = 3; y <= 4; y++) {
			for (int x = 0; x < 12; x++) {
				enemies.get(y).add(new Enemy(eX, eY, 1, baseDelay)); // type 1
				eX += 30;
			}
			eX = 20;
			eY = 140;
		}

	}

	// uses type of alien to determine correct image
	public Image getAlienImage(int type) {
		switch (type) {
		case 1:
			return alien1Image;
		case 2:
			return alien2Image;
		case 3:
			return alien3Image;
		}
		return null;
	}

	public int getRightMost() {
		ArrayList<Enemy> right = new ArrayList<Enemy>();
		for (ArrayList<Enemy> e : enemies) {
			for (int x = 11; x >= 0; x--) {
				if (e.get(x).isAlive()) {
					right.add(e.get(x));
					break;
				}
			}
		}

		if (right.size() > 0) {
			int rightMost = right.get(0).getX();
			for (Enemy e : right) {
				rightMost = Math.max(rightMost, e.getX());
			}
			return rightMost;
		} else {
			return 0;
		}
	}

	public int getLeftMost() {
		ArrayList<Enemy> left = new ArrayList<Enemy>();
		for (ArrayList<Enemy> e : enemies) {
			for (int x = 0; x < 12; x++) {
				if (e.get(x).isAlive()) {
					left.add(e.get(x));
					break;
				}
			}
		}

		if (left.size() > 0) {
			int leftMost = left.get(0).getX();
			for (Enemy e : left) {
				leftMost = Math.min(leftMost, e.getX());
			}

			return leftMost;
		} else {
			return 300;
		}
	}

	public ArrayList<Enemy> getBottom() {
		ArrayList<Enemy> bottom = new ArrayList<Enemy>();

		for (int y = 0; y <= 11; y++) {
			for (int x = 4; x >= 0; x--) {
				if (enemies.get(x).get(y).isAlive()) {
					bottom.add(enemies.get(x).get(y));
					break;
				}
			}
		}

		return bottom;
	}

	public void checkAlive() {
		boolean allAlive = false;
		for (ArrayList<Enemy> en : enemies) {
			for (Enemy e : en) {
				if (e.isAlive()) {
					e.update();
					allAlive = true;
				}
			}
		}

		if (allAlive == false) {
			respawning = true;
		}
	}

	// maybe useful?
	public static Ship getShip() {
		return ship;
	}

	public static ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public static void setProjectiles(ArrayList<Projectile> projectiles) {
		Main.projectiles = projectiles;
	}

	public static UFO getUfo() {
		return ufo;
	}

	public void setUfo(UFO u) {
		ufo = u;
	}

}
