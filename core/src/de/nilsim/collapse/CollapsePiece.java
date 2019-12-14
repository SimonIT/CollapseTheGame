package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Piece;

public class CollapsePiece extends Piece {
	private Player owner;
	private int points;

	public CollapsePiece(Player owner) {
		super(owner.getDefaultPieceTexture());
		this.owner = owner;
		points = 1;
	}

	public CollapsePiece(Player owner, int points) {
		super(owner.getPieceTexture(points));
		this.owner = owner;
		this.points = points;
	}

	public CollapsePiece clone() {
		return new CollapsePiece(owner, points);
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		if (points < 1) throw new IllegalArgumentException("A piece must have at least 1 dot");
		if (this.points != points) {
			this.points = points;
			setTexture(owner.getPieceTexture(points));
		}
	}

	public void increasePoints() {
		setPoints(points + 1);
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		if (this.owner != owner) {
			this.owner = owner;
			setTexture(owner.getPieceTexture(points));
		}
	}
}
