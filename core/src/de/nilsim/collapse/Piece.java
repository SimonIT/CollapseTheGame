package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Assets;
import com.badlogic.gdx.graphics.Color;

public class Piece extends ch.asynk.gdx.boardgame.Piece {
	private int dotAmount = 1;
	private Assets assets;

	public Piece(Assets assets, Color color) {
		super(assets.getTexture("piece1"));
		this.assets = assets;
		setColor(color);
	}

	public int getDotAmount() {
		return this.dotAmount;
	}

	public void setDotAmount(int dotAmount) {
		if (dotAmount > 3) throw new IllegalArgumentException("A piece can have only 3 dots");
		if (dotAmount < 1) throw new IllegalArgumentException("A piece must have min. 1 dot");
		this.dotAmount = dotAmount;
		setTexture(assets.getTexture("piece" + dotAmount));
	}

	public void increaseDotAmount() {
		setDotAmount(this.dotAmount + 1);
	}

	public boolean hasMaximumDots() {
		return this.dotAmount == 3;
	}
}
