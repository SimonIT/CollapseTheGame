package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Piece;
import com.badlogic.gdx.graphics.Texture;

public class CollapsePiece extends Piece {
	private int ownerId;
	private int dotAmount;

	public CollapsePiece(int ownerId, Texture texture) {
		super(texture);
		this.ownerId = ownerId;
		this.dotAmount = 1;
	}

	public CollapsePiece(int ownerId, Texture texture, int dotAmount) {
		super(texture);
		this.ownerId = ownerId;
		this.dotAmount = dotAmount;
	}

	public CollapsePiece clone() {
		return new CollapsePiece(this.ownerId, this.getTexture(), this.dotAmount);
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

	public int getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
}
