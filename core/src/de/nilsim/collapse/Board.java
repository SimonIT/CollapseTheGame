package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Assets;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class Board extends Actor implements ch.asynk.gdx.boardgame.boards.Board {

	private Assets assets;
	private Piece[][] pieces;
	/**
	 * amount of pieces x
	 */
	private int x;
	/**
	 * amount of pieces y
	 */
	private int y;

	private int borderBoard = 10;
	private int borderFields = 2;
	private int pieceSpace = 3;

	private Texture board, field;

	public Board(Assets assets, int x, int y) {
		this.assets = assets;
		this.pieces = new Piece[y][x];
		this.x = x;
		this.y = y;

		this.addListener(new ActorGestureListener() {
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				super.tap(event, x, y, count, button);
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				return super.longPress(actor, x, y);
			}
		});
		Pixmap bp = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		bp.setColor(Color.valueOf("c85e2c"));
		bp.fillRectangle(0, 0, 1, 1);
		this.board = new Texture(bp);
		Pixmap fp = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		fp.setColor(Color.valueOf("fbf297"));
		fp.fillRectangle(0, 0, 1, 1);
		this.field = new Texture(fp);
	}

	@Override
	public int[] getAngles() {
		return new int[0];
	}

	@Override
	public void centerOf(int x, int y, Vector2 v) {

	}

	@Override
	public void toBoard(float x, float y, Vector2 v) {

	}

	void addPiece(int x, int y, Piece piece) {
		this.pieces[y][x] = piece;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float widthBoard;
		float heightBoard;

		float fieldSize;
		if (getWidth() < getHeight()) {
			widthBoard = getWidth();
			fieldSize = (widthBoard - (2f * this.borderBoard) - ((this.x + 1f) * this.borderFields)) / this.x;
			heightBoard = 2 * this.borderBoard + this.y * (this.borderFields + fieldSize);
		} else {
			heightBoard = getHeight();
			fieldSize = (heightBoard - (2f * this.borderBoard) - ((this.y - 1f) * this.borderFields)) / this.y;
			widthBoard = 2 * this.borderBoard + this.x * (this.borderFields + fieldSize);
		}
		float pieceSize = fieldSize - (2 * this.pieceSpace);

		batch.draw(this.board, getX(), getY(), widthBoard, heightBoard);

		for (int i = 0; i < this.pieces.length; i++) {
			for (int j = 0; j < this.pieces[i].length; j++) {
				float fieldX = getX() + this.borderBoard + (j * (this.borderFields + fieldSize));
				float fieldY = getY() + this.borderBoard + (i * (this.borderFields + fieldSize));
				batch.draw(this.field, fieldX, fieldY, fieldSize, fieldSize);
				if (this.pieces[i][j] != null) {
					batch.draw(this.pieces[i][j], fieldX + this.pieceSpace, fieldY + this.pieceSpace, pieceSize, pieceSize);
				}
			}
		}
	}

	private void ensureLoadedAsset(String assetName) {
		if (this.assets.isLoaded(assetName)) {
			this.assets.finishLoadingAsset(assetName);
		}
	}
}
