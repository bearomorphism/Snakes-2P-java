package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

public class Snake {

	private ArrayList<Cell> cells;
	private Color headColor, bodyColor;
	public int dir;
	public boolean accelerating;
	public boolean alive;
	private String name;
	private static final int FOODFULL = 500;
	private static final int HUNGER = 8;
	private static final int PUNISH = 5;
	private int foodBar;
	private int foodBarPosX;
	private Color foodBarColor;
	private int player;

	private double updateRate() {
		return cells.size() / 30.0 + 0.187;
	}

	private double updateCurrent;
	private static final double UPDATE = 1;
	private int[] dx = { 0, 1, 0, -1 }, dy = { -1, 0, 1, 0 };
	private int grow;

	public Snake(int player) {
		this.player = player;
		cells = new ArrayList<Cell>();
		grow = 0;
		dir = 2;
		accelerating = false;
		alive = true;
		name = "Player " + player;
		foodBar = FOODFULL;
		if (player == 1) {

			for (int i = 0; i < 6; i++) {
				cells.add(new Cell(6 - i, 0));
			}
			headColor = Color.GREEN;
			// bodyColor = Color.BLUE;

			foodBarPosX = 0;
			foodBarColor = Color.YELLOW;
		} else if (player == 2) {

			for (int i = 0; i < 6; i++) {
				cells.add(new Cell(Sketch.W - 7 + i, 0));
			}
			headColor = Color.ORANGE;
			foodBarPosX = Sketch.WIDTH / 2;
			foodBarColor = Color.CYAN;
		}
	}

	public void show(Graphics g) {

		Iterator<Cell> it = cells.iterator();
		if (player == 1) {
			bodyColor = new Color(0, 0, 255, 255 - (FOODFULL - foodBar) / 2);
//			bodyColor = new Color(0, 0, 255, 255);

		} else if (player == 2) {
			bodyColor = new Color(255, 0, 0, 255 - (FOODFULL - foodBar) / 2);
		}

		if (cells.size() > 0) {
			Cell c;
			c = it.next();
			g.setColor(bodyColor);
			while (it.hasNext()) {
				Cell d = it.next();
				d.show(g);
			}
			g.setColor(headColor);
			c.show(g);
		}
		g.setColor(foodBarColor);
		g.fillRect(foodBarPosX, 0, foodBar, 10);
	}

	private void move(Sketch s) {
		if(!alive)return;
		Cell head = cells.get(0);
		cells.add(0, new Cell((head.x + dx[dir] + Sketch.W) % Sketch.W, (head.y + dy[dir] + Sketch.H) % Sketch.H));
		for (Snake snake : s.snakes) {
			if (this != snake && snake.isOverlap(head)) {
				alive = false;
				return;
			}
		}

		for (Cell food : s.foods) {
			if (head.equals(food)) {
				s.summonFood(food);
				grow += 3;
				foodBar = FOODFULL;
			}
		}

		if (grow <= 0) {
			cells.remove(cells.size() - 1);

		} else {
			grow--;
		}
	}

	public void update(Sketch s) {
		updateCurrent -= updateRate();
		foodBar -= HUNGER;
		if (foodBar <= 0) {
			foodBar = 250;
			grow -= PUNISH;
		}

		if (grow < 0 && alive) {
			cells.remove(cells.size() - 1);
			grow++;
			if (cells.size() == 0)
				alive = false;
		}
		if (accelerating)
			move(s);
		if (updateCurrent < 0) {
			move(s);
			updateCurrent += UPDATE;
		}
	}

	public boolean isOverlap(Cell c) {
		for (Cell x : cells) {
			if (x.equals(c))
				return true;
		}
		return false;
	}

	public String toString() {
		return name;
	}
}
