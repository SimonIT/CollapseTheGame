package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.boards.Board;
import ch.asynk.gdx.boardgame.ui.Element;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CollapseBoard extends Element implements Board {
	private CollapsePiece[][] pieces;
	private int width, height;
	private int borderBoard = 10;
	private int borderFields = 2;
	private int pieceSpace = 3;
	private float fieldSize, pieceSize;
	private Texture board, field;
	private Array<Player> players;
	private int currentPlayerIndex = 0;
	private boolean wrapWorld = true;

	public CollapseBoard(int width, int height, Array<Player> players) {
		this.players = players;
		this.pieces = new CollapsePiece[height][width];
		this.width = width;
		this.height = height;

		Pixmap bp = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		bp.setColor(Color.valueOf("c85e2c"));
		bp.fillRectangle(0, 0, 1, 1);
		this.board = new Texture(bp);
		Pixmap fp = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		fp.setColor(Color.valueOf("fbf297"));
		fp.fillRectangle(0, 0, 1, 1);
		this.field = new Texture(fp);
		System.out.println(getX());
	}

	void nextPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size;
	}

	boolean increaseDotAmount(Vector2 v) {
		return increaseDotAmount(v, false);
	}

	boolean increaseDotAmount(Vector2 v, boolean touch) {
		if (v.x < 0 || v.x >= this.width || v.y < 0 || v.y >= this.height) {
			if (!wrapWorld) return false;
			v.x = (v.x + this.width) % this.width;
			v.y = (v.y + this.height) % this.height;
		}

		CollapsePiece piece = pieces[(int) v.y][(int) v.x];
		Player currentPlayer = players.get(this.currentPlayerIndex);
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
					pieces[(int) v.y][(int) v.x] = null;
					increaseDotAmount(new Vector2(v.x + 1, v.y));
					increaseDotAmount(new Vector2(v.x, v.y + 1));
					increaseDotAmount(new Vector2(v.x - 1, v.y));
					increaseDotAmount(new Vector2(v.x, v.y - 1));
				}
				return true;
			}
		} else if (!touch || hasNoPiece(currentPlayer)) {
			CollapsePiece newPiece = new CollapsePiece(currentPlayer.getColor());
			pieces[(int) v.y][(int) v.x] = newPiece;
			currentPlayer.addPiece(newPiece);
			return true;
		}
		return false;
	}

	private void removePieceFromPlayers(CollapsePiece piece) {
		for (Player player : this.players) {
			player.removePiece(piece);
		}
	}

	private boolean hasNoPiece(Player player) {
		for (CollapsePiece[] row : this.pieces) {
			for (CollapsePiece piece : row) {
				if (piece != null && player.ownsPiece(piece)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected void computeGeometry() {
		super.computeGeometry();
		float widthBoard = getWidth() == 0 ? parent.getWidth() : getWidth();
		float heightBoard = getHeight() == 0 ? parent.getHeight() : getHeight();

		if (widthBoard < heightBoard) {
			this.fieldSize = (widthBoard - (2f * this.borderBoard) - ((this.width + 1f) * this.borderFields)) / this.width;
			heightBoard = 2 * this.borderBoard + this.height * (this.borderFields + fieldSize);
		} else {
			this.fieldSize = (heightBoard - (2f * this.borderBoard) - ((this.height - 1f) * this.borderFields)) / this.height;
			widthBoard = 2 * this.borderBoard + this.width * (this.borderFields + fieldSize);
		}
		this.pieceSize = this.fieldSize - (2 * this.pieceSpace);
		this.rect.setSize(widthBoard, heightBoard);
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
		v.set(MathUtils.floorPositive((x - getX() - borderBoard) / (fieldSize + borderFields)), MathUtils.floorPositive((y - getY() - borderBoard) / (fieldSize + borderFields)));
	}

	@Override
	public float distance(int x0, int y0, int x1, int y1, Geometry geometry) {
		return 0;
	}

	void addPiece(int x, int y, CollapsePiece piece) {
		this.pieces[y][x] = piece;
	}

	Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	@Override
	public void draw(Batch batch) {
		if (!visible) return;
		if (tainted) computeGeometry();

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
}
