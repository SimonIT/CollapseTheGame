package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Piece;

public class CollapsePiece extends Piece {
	private Player owner;
	private int dotAmount;

	public CollapsePiece(Player owner) {
		super(owner.getDefaultPieceTexture());
		this.owner = owner;
		this.dotAmount = 1;
	}

	public CollapsePiece(Player owner, int dotAmount) {
		super(owner.getPieceTexture(dotAmount));
		this.owner = owner;
		this.dotAmount = dotAmount;
	}

	public CollapsePiece clone() {
		return new CollapsePiece(this.owner, this.dotAmount);
	}

	public int getDotAmount() {
		return this.dotAmount;
	}

	public void setDotAmount(int dotAmount) {
		if (dotAmount < 1) throw new IllegalArgumentException("A piece must have at least 1 dot");
		if (this.dotAmount != dotAmount) {
			this.dotAmount = dotAmount;
			setTexture(owner.getPieceTexture(dotAmount));
		}
	}

	public void increaseDotAmount() {
		setDotAmount(this.dotAmount + 1);
	}

	public Player getOwner() {
		return this.owner;
	}

	public void setOwner(Player owner) {
		if (this.owner != owner) {
			this.owner = owner;
			setTexture(owner.getPieceTexture(dotAmount));
		}
	}
}
