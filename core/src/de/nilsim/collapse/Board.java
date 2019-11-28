package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Assets;
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
	private int pieceSpace = 2;

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

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float width;
		float height;

		float fieldSize;
		if (getWidth() < getHeight()) {
			width = getWidth();
			fieldSize = (width - (2 * borderBoard) - ((x - 1f) * borderFields)) / x;
			height = 2 * borderBoard + y * fieldSize;
		} else {
			height = getHeight();
			fieldSize = (height - (2 * borderBoard) - ((y - 1f) * borderFields)) / y;
			width = 2 * borderBoard + x * fieldSize;
		}
		float pieceSize = fieldSize - (2 * pieceSpace);

		ensureLoadedAsset("board");

		batch.draw(this.assets.getTexture("board"), getX(), getY(), width, height);

		ensureLoadedAsset("field");
		ensureLoadedAsset("piece1");
		ensureLoadedAsset("piece2");
		ensureLoadedAsset("piece3");

		for (int i = 0; i < this.pieces.length; i++) {
			for (int j = 0; j < this.pieces[i].length; j++) {
				float fieldX = getX() + borderBoard + (j * fieldSize);
				float fieldY = getY() + borderBoard + (i * fieldSize);
				batch.draw(this.assets.getTexture("field"), fieldX, fieldY, fieldSize, fieldSize);
				batch.draw(this.pieces[i][j], fieldX + pieceSpace, fieldY + pieceSpace, pieceSize, pieceSize);
			}
		}
	}

	private void ensureLoadedAsset(String assetName) {
		if (this.assets.isLoaded(assetName)) {
			this.assets.finishLoadingAsset(assetName);
		}
	}
}
