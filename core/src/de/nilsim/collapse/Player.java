package de.nilsim.collapse;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Player {
	private int id;
	private Color color;
	private String name;
	private long points;
	private boolean alive;
	private boolean firstMove;
	private Texture[] pieceTextures = new Texture[3];

	public Player(int id, Color color, String name) {
		this.id = id;
		this.color = color;
		this.name = name;
		this.alive = true;
		this.firstMove = true;
	}

	void generateTextures(int diameter) {
		for (int i = 1; i < 4; i++) {
			Pixmap pieceDrawer = new Pixmap(diameter, diameter, Pixmap.Format.RGBA8888);
			pieceDrawer.setColor(color);
			pieceDrawer.fillCircle(diameter / 2, diameter / 2, diameter / 2);
			pieceDrawer.setColor(Color.WHITE);
			switch (i) {
				case 1:
					pieceDrawer.fillCircle(diameter / 2, diameter / 2, diameter / 8);
					break;
				case 2:
					pieceDrawer.fillCircle(diameter / 3, diameter / 2, diameter / 8);
					pieceDrawer.fillCircle(2 * diameter / 3, diameter / 2, diameter / 8);
					break;
				case 3:
					pieceDrawer.fillCircle(diameter / 3, 2 * diameter / 3, diameter / 8);
					pieceDrawer.fillCircle(2 * diameter / 3, 2 * diameter / 3, diameter / 8);
					pieceDrawer.fillCircle(diameter / 2, diameter / 3, diameter / 8);
			}

			this.pieceTextures[i - 1] = new Texture(pieceDrawer);
		}
	}

	Texture getDefaultPieceTexture() {
		return getPieceTexture(1);
	}

	Texture getPieceTexture(int pieceAmount) {
		if (pieceAmount > 3) {
			return getDefaultPieceTexture();
		}
		return this.pieceTextures[pieceAmount - 1];
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

	public void addPoints(CollapsePiece piece) {
		this.points += piece.getDotAmount();
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

	public boolean getFirstMove() {
		return this.firstMove;
	}

	public void setFirstMove(boolean firstMove) {
		this.firstMove = firstMove;
	}
}
