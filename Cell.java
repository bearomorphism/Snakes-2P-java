package snake;

import java.awt.Graphics;
import java.util.Random;

public class Cell {
	public int x;
	public int y;

	public Cell() {
		Random r = new Random();

		this.x = r.nextInt(Sketch.W);
		this.y = r.nextInt(Sketch.H);

	}
	
	public void random() {
		Random r = new Random();

		this.x = r.nextInt(Sketch.W);
		this.y = r.nextInt(Sketch.H);
	}
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean equals(Cell c) {
		return c != null && x == c.x && y == c.y;
	}

	public void show(Graphics g) {
		g.fillRect(x * Sketch.SIZE, y * Sketch.SIZE, Sketch.SIZE, Sketch.SIZE);
	}

	public String toString() {
		return x + " " + y;
	}
}
