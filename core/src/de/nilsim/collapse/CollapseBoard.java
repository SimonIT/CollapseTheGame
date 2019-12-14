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
	private static final int[][] neighbors = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
	private CollapsePiece[][] pieces;
	private int width, height;
	private int borderBoard = 10;
	private int borderFields = 2;
	private int pieceSpace = 3;
	private float fieldSize, pieceSize;
	private Texture board, field;
	private Array<Player> players;
	private int currentPlayerIndex = 0;
	private boolean wrapWorld = false;
	ActionProgress actionProgress = null;

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
		do {
			currentPlayerIndex = (currentPlayerIndex + 1) % players.size;
		} while (!this.players.get(currentPlayerIndex).getAlive());
	}

	boolean onGrid(int x, int y) {
		return x > -1 && x < this.width && y > -1 && y < this.height;
	}

	boolean onGrid(Point<Integer> position) {
		return onGrid(position.x, position.y);
	}

	CollapsePiece getPiece(Point<Integer> position) {
		return pieces[position.y][position.x];
	}

	boolean increaseDotAmount(int x, int y) {
		return increaseDotAmount(new Point<>(x, y));
	}

	boolean increaseDotAmount(Point<Integer> position) {
		if (!onGrid(position)) {
			return false;
		}
		Player currentPlayer = this.getCurrentPlayer();
		CollapsePiece currentPiece = getPiece(position);

		if (currentPiece == null) {
			if (!currentPlayer.getFirstMove()) {
				return false;
			}
			setPiece(position, new CollapsePiece(currentPlayer));
		} else if (currentPiece.getOwner() != currentPlayer) {
			return false;
		} else {
			currentPiece.increaseDotAmount();
		}
		currentPlayer.setFirstMove(false);
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
		int diameter = MathUtils.roundPositive(this.pieceSize);
		for (Player player : this.players) {
			player.generateTextures(diameter);
		}
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

	void setPiece(int x, int y, CollapsePiece piece) {
		this.pieces[y][x] = piece;
	}

	void setPiece(Point<Integer> position, CollapsePiece piece) {
		setPiece(position.x, position.y, piece);
	}

	Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	Player getPlayerById(int id) {
		for (Player player : this.players)
			if (player.getId() == id)
				return player;
		throw new IllegalArgumentException(String.valueOf(id));
	}

	@Override
	public void draw(Batch batch) {
		if (!visible) return;
		if (tainted) computeGeometry();

		batch.draw(this.board, getX(), getY(), getWidth(), getHeight());

		for (int y = 0; y < this.height; y++) {
			for (int x = 0; x < this.width; x++) {
				float fieldX = getX() + this.borderBoard + (x * (this.borderFields + this.fieldSize));
				float fieldY = getY() + this.borderBoard + (y * (this.borderFields + this.fieldSize));
				batch.draw(this.field, fieldX, fieldY, this.fieldSize, this.fieldSize);

				Point<Integer> position = new Point<>(x, y);
				CollapsePiece piece = getPiece(position);
				if (piece != null) {
					Player player = piece.getOwner();
					if (piece.getDotAmount() > 3) {
						for (int side = 0; side < 4; ++side) {
							Point<Integer> positionNew = new Point<>(
									position.x + neighbors[side][0],
									position.y + neighbors[side][1]
							);
							if (actionProgress == null) {
								actionProgress = new ActionProgress();
								this.blocked = true;
							}
							actionProgress.addPiece(new CollapsePiece(player), position, positionNew);
						}
						setPiece(position, null);
					} else {
						batch.draw(piece, fieldX + this.pieceSpace, fieldY + this.pieceSpace, pieceSize, pieceSize);
					}
				}
			}
		}

		if (actionProgress != null) {
			for (ActionProgressPiece actionProgressPiece : actionProgress.getPieces()) {
				float fieldX = getX() + this.borderBoard + (actionProgressPiece.getPosition().x * (this.borderFields + this.fieldSize));
				float fieldY = getY() + this.borderBoard + (actionProgressPiece.getPosition().y * (this.borderFields + this.fieldSize));
				batch.draw(actionProgressPiece.getPiece(), fieldX + this.pieceSpace, fieldY + this.pieceSpace, pieceSize, pieceSize);
			}
			actionProgress.step();
			if (actionProgress.isDone()) {
				for (ActionProgressPiece actionProgressPiece : actionProgress.getPieces()) {
					Point<Integer> positionNew = actionProgressPiece.getP1();
					if (wrapWorld || onGrid(positionNew)) {
						if (wrapWorld) {
							positionNew = new Point<>(
									(positionNew.x + this.width) % this.width,
									(positionNew.y + this.height) % this.height
							);
						}
						CollapsePiece pieceNew = getPiece(positionNew);
						if (pieceNew == null) {
							setPiece(positionNew, actionProgressPiece.getPiece());
						} else {
							pieceNew.increaseDotAmount();
							pieceNew.setOwner(actionProgressPiece.getPiece().getOwner());
						}
					}
				}
				actionProgress = null;
				this.blocked = false;
			}
		}
	}
}
