package de.nilsim.collapse;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class Player {
	private Color color;
	private String name;
	private Array<CollapsePiece> pieces = new Array<>();
	private long points;

	public Player(Color color, String name) {
		this.color = color;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void addPiece(CollapsePiece piece) {
		if (!this.pieces.contains(piece, true)) {
			this.pieces.add(piece);
		}
	}

	public void removePiece(CollapsePiece piece) {
		this.pieces.removeValue(piece, true);
	}

	public boolean ownsPiece(CollapsePiece piece) {
		return this.pieces.contains(piece, true);
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
}
