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
import com.badlogic.gdx.utils.Array;

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
	private Array<Player> players = new Array<>();
	private int currentPlayerIndex = 0;

	public Board(Assets assets, int x, int y) {
		this.players.add(new Player(Color.valueOf("eb3935"), "Simon"), new Player(Color.valueOf("679ed7"), "Nils")); // TODO dynamic
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
				if (increaseDotAmount(xPiece, yPiece, true)) {
					Player player = players.get(currentPlayerIndex);
					System.out.println(player.getName() + " " + player.getPoints());
					if (currentPlayerIndex < players.size - 1) {
						currentPlayerIndex++;
					} else {
						currentPlayerIndex = 0;
					}
				}
			}

			@Override
			public boolean longPress(Actor actor, float x, float y) {
				return true;
			}
		});
	}

	private boolean increaseDotAmount(int x, int y) {
		return increaseDotAmount(x, y, false);
	}

	private boolean increaseDotAmount(int x, int y, boolean touch) {
		if (x < this.x && x > -1 && y < this.y && y > -1) {
			Piece piece = pieces[y][x];
			Player currentPlayer = players.get(currentPlayerIndex);
			if (piece != null) {
				boolean ownPiece = currentPlayer.ownsPiece(piece);
				if (!touch || ownPiece) {
					if (!ownPiece) {
						currentPlayer.addPoints(piece.getDotAmount());
					}
					removePieceFromPlayers(piece);
					if (!piece.hasMaximumDots()) {
						piece.setColor(currentPlayer.getColor());
						piece.increaseDotAmount();
						currentPlayer.addPiece(piece);
					} else {
						pieces[y][x] = null;
						increaseDotAmount(x + 1, y);
						increaseDotAmount(x, y + 1);
						increaseDotAmount(x - 1, y);
						increaseDotAmount(x, y - 1);
					}
					return true;
				}
			} else if (!touch || hasNoPiece(currentPlayer)) {
				Piece newp = new Piece(currentPlayer.getColor());
				pieces[y][x] = newp;
				currentPlayer.addPiece(newp);
				return true;
			}
		}
		return false;
	}

	void removePieceFromPlayers(Piece piece) {
		for (Player player : this.players) {
			player.removePiece(piece);
		}
	}

	boolean hasNoPiece(Player player) {
		for (Piece[] row : this.pieces) {
			for (Piece piece : row) {
				if (piece != null && player.ownsPiece(piece)) {
					return false;
				}
			}
		}
		return true;
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

	@Override
	public float distance(int x0, int y0, int x1, int y1, Geometry geometry) {
		return 0;
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
