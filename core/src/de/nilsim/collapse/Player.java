package de.nilsim.collapse;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class Player {
	private int id;
	private Color color;
	private String name;
	private long points;

	public Player(int id, Color color, String name) {
		this.id = id;
		this.color = color;
		this.name = name;
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
}
