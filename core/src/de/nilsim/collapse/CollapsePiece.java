package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Piece;
import com.badlogic.gdx.graphics.Texture;

public class CollapsePiece extends Piece {
	private Player owner;
	private int dotAmount;

	public CollapsePiece(Player owner, Texture texture) {
		super(texture);
		this.owner = owner;
		this.dotAmount = 1;
	}

	public CollapsePiece(Player owner, Texture texture, int dotAmount) {
		super(texture);
		this.owner = owner;
		this.dotAmount = dotAmount;
	}

	public CollapsePiece clone() {
		return new CollapsePiece(this.owner, this.getTexture(), this.dotAmount);
	}

	public int getDotAmount() {
		return this.dotAmount;
	}

	public void setDotAmount(int dotAmount) {
		if (dotAmount < 1) throw new IllegalArgumentException("A piece must have at least 1 dot");
		if (this.dotAmount != dotAmount) {
			this.dotAmount = dotAmount;
		}
	}

	public void increaseDotAmount() {
		setDotAmount(this.dotAmount + 1);
	}

	public Player getOwner() {
		return this.owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
}
