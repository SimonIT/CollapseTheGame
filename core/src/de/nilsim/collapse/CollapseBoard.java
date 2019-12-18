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
	ActionProgress actionProgress = null;
	private CollapsePiece[][] pieces;
	private int width, height;
	private int borderBoard = 10;
	private int borderFields = 2;
	private int pieceSpace = 3;
	private float fieldSize, pieceSize;
	private Texture board, field;
	private Array<Player> players;
	private int[] playersPieceCount;
	private int currentPlayerIndex = 0;
	private boolean wrapWorld = false;
	private PointChangeListener pointChangeListener;
	private CurrentPlayerChangeListener currentPlayerChangeListener;
	private boolean wasJustInAction;

	public CollapseBoard(int width, int height, Array<Player> players) {
		super();
		this.players = players;
		playersPieceCount = new int[players.size];
		pieces = new CollapsePiece[height][width];
		this.width = width;
		this.height = height;
		wasJustInAction = false;

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.valueOf("c85e2c"));
		pixmap.fillRectangle(0, 0, 1, 1);
		board = new Texture(pixmap);

		pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.valueOf("fbf297"));
		pixmap.fillRectangle(0, 0, 1, 1);
		field = new Texture(pixmap);
	}

	public boolean isWrapWorld() {
		return wrapWorld;
	}

	public void setWrapWorld(boolean wrapWorld) {
		this.wrapWorld = wrapWorld;
	}

	void nextPlayer() {
		Player oldCurrentPlayer = getCurrentPlayer();
		do {
			currentPlayerIndex = (currentPlayerIndex + 1) % players.size;
		} while (!players.get(currentPlayerIndex).getAlive());
		this.currentPlayerChangeListener.playerChanged(oldCurrentPlayer, getCurrentPlayer());
	}

	boolean onGrid(int x, int y) {
		return x > -1 && x < width && y > -1 && y < height;
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
		Player currentPlayer = getCurrentPlayer();
		CollapsePiece currentPiece = getPiece(position);

		if (currentPiece == null) {
			if (!currentPlayer.getFirstMove()) {
				return false;
			}
			playersPieceCount[currentPlayerIndex]++;
			setPiece(position, new CollapsePiece(currentPlayer));
		} else if (currentPiece.getOwner() != currentPlayer) {
			return false;
		} else {
			currentPiece.increasePoints();
		}
		currentPlayer.setFirstMove(false);
		wasJustInAction = true;
		return true;
	}

	@Override
	public void computeGeometry() {
		float widthBoard = getWidth() == 0 ? parent.getWidth() : getWidth();
		float heightBoard = getHeight() == 0 ? parent.getHeight() : getHeight();

		if (widthBoard < heightBoard) {
			fieldSize = (widthBoard - (2f * borderBoard) - ((width + 1f) * borderFields)) / width;
			heightBoard = 2 * borderBoard + height * (borderFields + fieldSize);
		} else {
			fieldSize = (heightBoard - (2f * borderBoard) - ((height - 1f) * borderFields)) / height;
			widthBoard = 2 * borderBoard + width * (borderFields + fieldSize);
		}
		pieceSize = fieldSize - (2 * pieceSpace);
		int diameter = MathUtils.roundPositive(pieceSize);
		for (Player player : players) {
			player.generateTextures(diameter);
		}
		rect.setSize(widthBoard, heightBoard);
		super.computeGeometry();
	}

	@Override
	public int[] getAngles() {
		return new int[0];
	}

	@Override
	public void centerOf(int x, int y, Vector2 vector) {

	}

	@Override
	public void toBoard(float x, float y, Vector2 vector) {
		vector.x = (x - getX() - borderBoard) / (fieldSize + borderFields);
		vector.y = (y - getY() - borderBoard) / (fieldSize + borderFields);
	}

	@Override
	public float distance(int x0, int y0, int x1, int y1, Geometry geometry) {
		return 0;
	}

	void setPiece(int x, int y, CollapsePiece piece) {
		pieces[y][x] = piece;
	}

	void setPiece(Point<Integer> position, CollapsePiece piece) {
		setPiece(position.x, position.y, piece);
	}

	Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	Player getPlayerById(int id) {
		for (Player player : players)
			if (player.getId() == id)
				return player;
		throw new IllegalArgumentException(String.valueOf(id));
	}

	@Override
	public void draw(Batch batch) {
		if (!visible) return;
		if (tainted) computeGeometry();

		batch.draw(board, getX(), getY(), getWidth(), getHeight());

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float fieldX = getX() + borderBoard + (x * (borderFields + fieldSize));
				float fieldY = getY() + borderBoard + (y * (borderFields + fieldSize));
				batch.draw(field, fieldX, fieldY, fieldSize, fieldSize);

				Point<Integer> position = new Point<>(x, y);
				CollapsePiece piece = getPiece(position);
				if (piece != null) {
					Player player = piece.getOwner();
					if (piece.getPoints() > 3) {
						wasJustInAction = true;
						for (int side = 0; side < 4; ++side) {
							Point<Integer> positionNew = new Point<>(
									position.x + neighbors[side][0],
									position.y + neighbors[side][1]
							);
							if (actionProgress == null) {
								actionProgress = new ActionProgress();
								blocked = true;
							}
							actionProgress.addPiece(new CollapsePiece(player), position, positionNew);
						}
						playersPieceCount[piece.getOwner().getId()]--;
						setPiece(position, null);
					} else {
						batch.draw(piece, fieldX + pieceSpace, fieldY + pieceSpace, pieceSize, pieceSize);
					}
				}
			}
		}

		if (actionProgress != null) {
			for (ActionProgressPiece actionProgressPiece : actionProgress.getPieces()) {
				Point<Float> position = actionProgressPiece.getCurrent();
				float fieldX = getX() + borderBoard + (position.x * (borderFields + fieldSize));
				float fieldY = getY() + borderBoard + (position.y * (borderFields + fieldSize));
				batch.draw(actionProgressPiece.getPiece(), fieldX + pieceSpace, fieldY + pieceSpace, pieceSize, pieceSize);

				if (wrapWorld) {
					if (!actionProgressPiece.getEnd().equals(new Point<>(
							(actionProgressPiece.getEnd().x + width) % width,
							(actionProgressPiece.getEnd().y + height) % height
					))) {
						position = new Point<>(
								position.x > width - 1 ? position.x - width : (position.x + width) % width,
								position.y > height - 1 ? position.y - height : (position.y + height) % height
						);
						fieldX = getX() + borderBoard + (position.x * (borderFields + fieldSize));
						fieldY = getY() + borderBoard + (position.y * (borderFields + fieldSize));
						batch.draw(actionProgressPiece.getPiece(), fieldX + pieceSpace, fieldY + pieceSpace, pieceSize, pieceSize);
					}
				}
			}
			actionProgress.step();
			if (actionProgress.isDone()) {
				for (ActionProgressPiece actionProgressPiece : actionProgress.getPieces()) {
					Point<Integer> positionNew = actionProgressPiece.getEnd();
					CollapsePiece piece = actionProgressPiece.getPiece();
					if (wrapWorld || onGrid(positionNew)) {
						if (wrapWorld) {
							positionNew = new Point<>(
									(positionNew.x + width) % width,
									(positionNew.y + height) % height
							);
						}
						CollapsePiece pieceNew = getPiece(positionNew);
						if (pieceNew == null) {
							playersPieceCount[piece.getOwner().getId()]++;
							setPiece(positionNew, piece);
						} else {
							Player originalOwner = pieceNew.getOwner();
							Player owner = piece.getOwner();
							if (owner != originalOwner) {
								playersPieceCount[owner.getId()]++;
								if (--playersPieceCount[originalOwner.getId()] == 0) {
									originalOwner.setAlive(false);
								}
								owner.addPoints(pieceNew);
								if (pointChangeListener != null) pointChangeListener.pointsChanged(owner);
								pieceNew.setOwner(owner);
							}
							pieceNew.increasePoints();
						}
					}
				}
				actionProgress = null;
				blocked = false;
			}
			wasJustInAction = true;
		} else if (wasJustInAction) {
			nextPlayer();
			wasJustInAction = false;
		}
	}

	public void setPointChangeListener(PointChangeListener pointChangeListener) {
		this.pointChangeListener = pointChangeListener;
	}

	public void setCurrentPlayerChangeListener(CurrentPlayerChangeListener currentPlayerChangeListener) {
		this.currentPlayerChangeListener = currentPlayerChangeListener;
	}

	public interface PointChangeListener {
		void pointsChanged(Player player);
	}

	public interface CurrentPlayerChangeListener {
		void playerChanged(Player oldCurrentPlayer, Player newCurrentPlayer);
	}
}
