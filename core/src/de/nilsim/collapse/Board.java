package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
	private float fieldSize, pieceSize;
	private Texture board, field;
	private Player currentPlayer = new Player(Color.BLUE, "Simon"); // TODO dynamic

	public Board(Assets assets, int x, int y) {
		this.assets = assets;
		this.pieces = new Piece[y][x];
		this.x = x;
		this.y = y;

		Pixmap bp = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		bp.setColor(Color.valueOf("c85e2c"));
		bp.fillRectangle(0, 0, 1, 1);
		this.board = new Texture(bp);
		Pixmap fp = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		fp.setColor(Color.valueOf("fbf297"));
		fp.fillRectangle(0, 0, 1, 1);
		this.field = new Texture(fp);
		initialize();
	}

	private void initialize() {
		setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		setTouchable(Touchable.enabled);
		addListener(new ActorGestureListener() {
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				int yPiece = MathUtils.floorPositive((y - borderBoard) / (fieldSize + borderFields));
				int xPiece = MathUtils.floorPositive((x - borderBoard) / (fieldSize + borderFields));
				increaseDotAmount(xPiece, yPiece, true);
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				return true;
			}
		});
	}

	private void increaseDotAmount(int x, int y) {
		increaseDotAmount(x, y, false);
	}

	private void increaseDotAmount(int x, int y, boolean touch) {
		if (x < this.x && x > -1 && y < this.y && y > -1) {
			Piece piece = pieces[y][x];
			if (piece != null) {
				if (!piece.hasMaximumDots()) {
					piece.setColor(currentPlayer.getColor());
					piece.increaseDotAmount();
				} else {
					pieces[y][x] = null;
					increaseDotAmount(x + 1, y);
					increaseDotAmount(x, y + 1);
					increaseDotAmount(x - 1, y);
					increaseDotAmount(x, y - 1);
				}
			} else if (!touch) {
				pieces[y][x] = new Piece(currentPlayer.getColor());
			}
		}
	}

	@Override
	protected void sizeChanged() {
		float widthBoard = getWidth();
		float heightBoard = getHeight();

		if (widthBoard < heightBoard) {
			fieldSize = (widthBoard - (2f * this.borderBoard) - ((this.x + 1f) * this.borderFields)) / this.x;
			heightBoard = 2 * this.borderBoard + this.y * (this.borderFields + fieldSize);
		} else {
			fieldSize = (heightBoard - (2f * this.borderBoard) - ((this.y - 1f) * this.borderFields)) / this.y;
			widthBoard = 2 * this.borderBoard + this.x * (this.borderFields + fieldSize);
		}
		this.pieceSize = this.fieldSize - (2 * this.pieceSpace);
		setSize(widthBoard, heightBoard);
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
		batch.draw(this.board, getX(), getY(), getWidth(), getHeight());

		for (int i = 0; i < this.pieces.length; i++) {
			for (int j = 0; j < this.pieces[i].length; j++) {
				float fieldX = getX() + this.borderBoard + (j * (this.borderFields + this.fieldSize));
				float fieldY = getY() + this.borderBoard + (i * (this.borderFields + this.fieldSize));
				batch.draw(this.field, fieldX, fieldY, this.fieldSize, this.fieldSize);
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
