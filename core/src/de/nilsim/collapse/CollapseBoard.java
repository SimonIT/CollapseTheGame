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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CollapseBoard extends Element implements Board {
	private static int[][] neighbors = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
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
		super();
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
	}

	void nextPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size;
	}


	boolean on_grid(Vector2 v) {
		return v.x > -1 && v.x < this.width && v.y > -1 && v.y < this.height;
	}

	boolean increaseDotAmount(Vector2 v) {
		if (!on_grid(v)) {
			return false;
		}
		Player currentPlayer = players.get(this.currentPlayerIndex);
		CollapsePiece currentPiece = pieces[(int) v.y][(int) v.x];

		if (currentPiece == null) {
			if (!hasNoPiece(currentPlayer)) {
				return false;
			}
			pieces[(int) v.y][(int) v.x] = new CollapsePiece(currentPlayer.getId(), currentPlayer.getColor());
			currentPiece = pieces[(int) v.y][(int) v.x];
		} else if (currentPiece.getOwnerId() != currentPlayer.getId()) {
			return false;
		} else {
			currentPiece.increaseDotAmount();
		}

		if (currentPiece.getDotAmount() > 3) {
			Set<Vector2> frontier = new HashSet<>();
			frontier.add(v);
			while (frontier.size() > 0) {
				Set<Vector2> newFrontier = new HashSet<>();
				for (Vector2 pos : frontier) {
					CollapsePiece piece = pieces[(int) pos.y][(int) pos.x];

					for (int i = 0; i < 4; ++i) {
						Vector2 newPos = new Vector2(pos.x + neighbors[i][0], pos.y + neighbors[i][1]);
						if (wrapWorld || on_grid(newPos)) {
							newPos.x = (newPos.x + this.width) % this.width;
							newPos.y = (newPos.y + this.height) % this.height;
							if (pieces[(int) newPos.y][(int) newPos.x] == null) {
								pieces[(int) newPos.y][(int) newPos.x] = new CollapsePiece(currentPlayer.getId(), currentPlayer.getColor());
							} else {
								if (pieces[(int) newPos.y][(int) newPos.x].getOwnerId() != currentPlayer.getId()) {
									pieces[(int) newPos.y][(int) newPos.x].setOwnerId(currentPlayer.getId());
									pieces[(int) newPos.y][(int) newPos.x].setColor(currentPlayer.getColor());
									currentPlayer.addPoints(pieces[(int) newPos.y][(int) newPos.x].getDotAmount());
								}
								pieces[(int) newPos.y][(int) newPos.x].increaseDotAmount();
							}

							if (pieces[(int) newPos.y][(int) newPos.x].getDotAmount() > 3) {
								newFrontier.add(newPos);
							}
						}
					}
					pieces[(int) pos.y][(int) pos.x] = null;
				}
				frontier = newFrontier;
			}
		}
		return true;
	}

	private boolean hasNoPiece(Player player) {
		for (CollapsePiece[] row : this.pieces) {
			for (CollapsePiece piece : row) {
				if (piece != null && piece.getOwnerId() == player.getId()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected void computeGeometry() {
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
		CollapsePiece.diameter = MathUtils.roundPositive(this.pieceSize);
		this.rect.setSize(widthBoard, heightBoard);
		super.computeGeometry();
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
		v.x = (x - getX() - borderBoard) / (fieldSize + borderFields);
		v.y = (y - getY() - borderBoard) / (fieldSize + borderFields);
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
