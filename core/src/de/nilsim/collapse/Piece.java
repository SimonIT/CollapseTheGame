package de.nilsim.collapse;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Piece extends ch.asynk.gdx.boardgame.Piece {
	private int dotAmount = 1;
	private Color color;
	private int diameter;
	private Pixmap pieceDrawer;

	public Piece(Color color, int diameter) {
		super(new Texture(new Pixmap(0, 0, Pixmap.Format.RGBA8888)));
		this.pieceDrawer = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
		this.color = color;
		this.diameter = diameter;
		updateTexture();
	}

	public Piece(Color color, int diameter, int dotAmount) {
		super(new Texture(new Pixmap(0, 0, Pixmap.Format.RGBA8888)));
		this.pieceDrawer = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
		this.color = color;
		this.diameter = diameter;
		this.dotAmount = dotAmount;
		updateTexture();
	}

	@Override
	public void setSize(float width, float height) {
		super.setSize(width, height);
	}

	void updateTexture() {
		this.pieceDrawer.setColor(color);
		this.pieceDrawer.fillCircle(diameter / 2, diameter / 2, diameter / 2);
		this.pieceDrawer.setColor(Color.WHITE);
		switch (dotAmount) {
			case 1:
				this.pieceDrawer.fillCircle(diameter / 2, diameter / 2, diameter / 8);
				break;
			case 2:
				this.pieceDrawer.fillCircle(diameter / 4, diameter / 2 - diameter / 8, diameter / 8);
				this.pieceDrawer.fillCircle(3 * diameter / 4, diameter / 2 - diameter / 8, diameter / 8);
				break;
			case 3:
				// Todo
		}
		setTexture(new Texture(this.pieceDrawer));
	}

	public int getDotAmount() {
		return this.dotAmount;
	}

	public void setDotAmount(int dotAmount) {
		if (dotAmount > 3) throw new IllegalArgumentException("A piece can have only 3 dots");
		if (dotAmount < 1) throw new IllegalArgumentException("A piece must have min. 1 dot");
		if (this.dotAmount != dotAmount) {
			this.dotAmount = dotAmount;
			updateTexture();
		}
	}

	public void increaseDotAmount() {
		setDotAmount(this.dotAmount + 1);
	}

	public boolean hasMaximumDots() {
		return this.dotAmount == 3;
	}
}
