package de.nilsim.collapse;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class Player {
	private int id;
	private Color color;
	private String name;
	private long points;
	private boolean alive;
	private boolean firstMove;

	public Player(int id, Color color, String name) {
		this.id = id;
		this.color = color;
		this.name = name;
		this.alive = true;
		this.firstMove = true;
	}

	public String getName() {
		return this.name;
	}

	public Color getColor() {
		return color;
	}

	public void addPoints(long points) {
		this.points += points;
	}

	public long getPoints() {
		return this.points;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getAlive() {
		return this.alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public boolean getFirstMove() {return this.firstMove;}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}
}
