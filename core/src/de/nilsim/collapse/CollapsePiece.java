package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Piece;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class CollapsePiece extends Piece {
	public static int diameter;
	private int dotAmount = 1;
	private Color color;
	private Pixmap pieceDrawer;

	public CollapsePiece(Color color) {
		super(new Texture(new Pixmap(0, 0, Pixmap.Format.RGBA8888)));
		this.pieceDrawer = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
		this.color = color;
		updateTexture();
	}

	public CollapsePiece(Color color, int dotAmount) {
		super(new Texture(new Pixmap(0, 0, Pixmap.Format.RGBA8888)));
		this.pieceDrawer = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
		this.color = color;
		this.dotAmount = dotAmount;
		updateTexture();
	}

	public void setColor(Color color) {
		if (!this.color.equals(color)) {
			this.color = color;
			updateTexture();
		}
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
				this.pieceDrawer.fillCircle(diameter / 3, diameter / 2, diameter / 8);
				this.pieceDrawer.fillCircle(2 * diameter / 3, diameter / 2, diameter / 8);
				break;
			case 3:
				this.pieceDrawer.fillCircle(diameter / 3, 2 * diameter / 3, diameter / 8);
				this.pieceDrawer.fillCircle(2 * diameter / 3, 2 * diameter / 3, diameter / 8);
				this.pieceDrawer.fillCircle(diameter / 2, diameter / 3, diameter / 8);
		}

		Texture t = new Texture(this.pieceDrawer);
		setTexture(t);
		setRegion(0, 0, t.getWidth(), t.getHeight());
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
