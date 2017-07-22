package snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

public class Sketch extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int W = 30, H = 30;
	public static final int SIZE = 45;
	public static final int WIDTH = W * SIZE, HEIGHT = W * SIZE;

	private static Sketch s;
	private static final int FIX = 60; // It seems that title covers snakes.
	private Color backgroundColor = Color.GRAY;
	private Color foodColor = Color.PINK;
	public Snake snakes[];
	public Cell[] foods = new Cell[] { new Cell(), new Cell(), new Cell() };
	private long frameRate = 87;

	private Image offScreen;
	private Graphics gOffScreen;
	private int FWidth, FHeight;
	private KeyListener kl = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent e) {
			int k = e.getKeyCode();
			// System.out.println("Key Pressed" + k);
			switch (k) {
			case KeyEvent.VK_W:
				snakes[0].dir = 0;
				break;
			case KeyEvent.VK_D:
				snakes[0].dir = 1;
				break;
			case KeyEvent.VK_S:
				snakes[0].dir = 2;
				break;
			case KeyEvent.VK_A:
				snakes[0].dir = 3;
				break;
			case KeyEvent.VK_UP:
				snakes[1].dir = 0;
				break;
			case KeyEvent.VK_RIGHT:
				snakes[1].dir = 1;
				break;
			case KeyEvent.VK_DOWN:
				snakes[1].dir = 2;
				break;
			case KeyEvent.VK_LEFT:
				snakes[1].dir = 3;
				break;
			case KeyEvent.VK_SLASH:
				snakes[1].accelerating = true;
				break;
			case KeyEvent.VK_Q:
				snakes[0].accelerating = true;
				break;
			case KeyEvent.VK_ENTER:
				if (!checkAlive()) {
					gameSet();
				}
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			default:
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int k = e.getKeyCode();
			// System.out.println(k);
			switch (k) {
			case KeyEvent.VK_SLASH:
				snakes[1].accelerating = false;
				break;
			case KeyEvent.VK_Q:
				snakes[0].accelerating = false;
				break;
			default:
				break;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	};

	public static void main(String[] args) {
		s = new Sketch();
		s.init();
		s.gameSet();
		s.run();
	}

	public Sketch() {
	}

	public void init() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		FHeight = FWidth = Math.min((int) screenSize.getHeight(), (int) screenSize.getWidth()) - 187;
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(FWidth, FHeight + FIX);
		setLocationRelativeTo(null);
		setTitle("Snake 2P");
		addKeyListener(kl);

		offScreen = createImage(WIDTH, HEIGHT);
		gOffScreen = offScreen.getGraphics();
		gOffScreen.setColor(backgroundColor);

		description(gOffScreen);
		repaint();
		try {
			Thread.sleep(2787);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private boolean checkAlive() {
		for (Snake snake : snakes) {
			if (!snake.alive)
				return false;
		}
		return true;
	}

	public void gameSet() {
		snakes = new Snake[2];
		for (int i = 0; i < 2; i++) {
			snakes[i] = new Snake(i + 1);
		}

		for (int i = 0; i < 3; i++) {
			summonFood(foods[i]);
			// System.out.println(foods[i]);
		}
	}

	public void run() {

		while (true) {
			gUpdate(gOffScreen);
			repaint();
			try {
				Thread.sleep(frameRate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void gUpdate(Graphics g) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		g.setColor(backgroundColor);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		for (Snake snake : snakes) {
			snake.show(g);
		}
		g.setColor(foodColor);
		for (Cell c : foods) {
			c.show(g);
		}
		if (checkAlive()) {
			for (Snake snake : snakes) {
				snake.update(this);
			}
		} else { // game over
			g.setFont(f);
			g.setColor(Color.RED);
			g.drawString("Game Over", WIDTH / 2 - 200, HEIGHT / 2);
			String winner = "Draw!";
			for (Snake s : snakes) {
				if (s.alive) {
					winner = s.toString();
					break;
				}
			}
			g.drawString("Winner: " + winner, WIDTH / 2 - 200, HEIGHT / 2 + 100);
		}
	}

	public void paint(Graphics g) {
		g.translate(0, FIX);
		g.drawImage(offScreen, 0, 0, FWidth, FHeight, this);

	}

	public void summonFood(Cell f) {
		boolean ok = false;
		while (!ok) {
			f.random();
			ok = true;

			for (Snake s : snakes) {
				if (s.isOverlap(f)) {
					ok = false;
					break;
				}
			}

			for (Cell c : foods) {
				if (f != c && f.equals(c)) {
					ok = false;
					break;
				}
			}
		}
	}

	private final Font f = new Font("Hack", Font.PLAIN, 72);
	private final Font f2 = new Font("Hack", Font.PLAIN, 36);
	public void description(Graphics g) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		g.setColor(backgroundColor);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.RED);
		g.setFont(f);
		g.drawString("Control: WASD arrows\n", WIDTH / 2 - 300, HEIGHT / 2);
		g.drawString("Accelerate: Q slash(/)\n", WIDTH / 2 - 300, HEIGHT / 2 + 100);
		g.drawString("Restart: Enter", WIDTH / 2 - 300, HEIGHT / 2 + 200);
		g.setFont(f2);
		g.setColor(Color.BLUE);
		g.drawString("Made by Mr. Bear", WIDTH / 2 - 300, HEIGHT / 2 + 300);

	}

}


