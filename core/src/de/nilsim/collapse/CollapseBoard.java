package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Piece;
import ch.asynk.gdx.boardgame.Tile;
import ch.asynk.gdx.boardgame.boards.Board;
import ch.asynk.gdx.boardgame.ui.Element;
import ch.asynk.gdx.boardgame.ui.Sizing;
import ch.asynk.gdx.boardgame.utils.Collection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

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
	private List<Player> players;
	private int[] playersPieceCount;
	private int currentPlayerIndex = 0;
	private boolean wrapWorld = false;
	private PointChangeListener pointChangeListener;
	private CurrentPlayerChangeListener currentPlayerChangeListener;
	private boolean wasJustInAction;

	public CollapseBoard(int width, int height, List<Player> players) {
		super();
		this.players = players;
		playersPieceCount = new int[players.size()];
		pieces = new CollapsePiece[height][width];
		this.width = width;
		this.height = height;
		wasJustInAction = false;

		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.valueOf("c85e2c"));
		pixmap.fillRectangle(0, 0, 1, 1);
		board = new Texture(pixmap);
		pixmap.dispose();

		pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.valueOf("fbf297"));
		pixmap.fillRectangle(0, 0, 1, 1);
		field = new Texture(pixmap);
		pixmap.dispose();
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
			currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
		} while (!players.get(currentPlayerIndex).getAlive());
		this.currentPlayerChangeListener.playerChanged(oldCurrentPlayer, getCurrentPlayer());
	}

	boolean isOnMap(Point<Integer> position) {
		return isOnMap(position.x, position.y);
	}

	CollapsePiece getPiece(Point<Integer> position) {
		return pieces[position.y][position.x];
	}

	float getAspectRatio() {
		return 1f * width / height;
	}

	boolean increaseDotAmount(int x, int y) {
		return increaseDotAmount(new Point<>(x, y));
	}

	boolean increaseDotAmount(Point<Integer> position) {
		if (!isOnMap(position)) {
			return false;
		}
		Player currentPlayer = getCurrentPlayer();
		CollapsePiece currentPiece = getPiece(position);

		if (currentPiece == null) {
			if (!currentPlayer.getFirstMove()) {
				return false;
			}
			playersPieceCount[currentPlayerIndex]++;
			CollapsePiece piece = new CollapsePiece(currentPlayer);
			float fieldX = getX() + borderBoard + (position.x * (borderFields + fieldSize));
			float fieldY = getY() + borderBoard + (position.y * (borderFields + fieldSize));
			piece.setPosition(fieldX + pieceSpace, fieldY + pieceSpace);
			setPiece(position, piece);
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
	public void computeDimensions() {
		super.computeDimensions();
		float widthBoard = getWidth();
		float heightBoard = getHeight();

		if (Sizing.contains(this.sizing, Sizing.FILL_X)) {
			fieldSize = (widthBoard - (2f * borderBoard) - ((width + 1f) * borderFields)) / width;
			heightBoard = 2 * borderBoard + height * (borderFields + fieldSize);
		} else if (Sizing.contains(this.sizing, Sizing.FILL_Y)) {
			fieldSize = (heightBoard - (2f * borderBoard) - ((height - 1f) * borderFields)) / height;
			widthBoard = 2 * borderBoard + width * (borderFields + fieldSize);
		}
		pieceSize = fieldSize - (2 * pieceSpace);
		int diameter = MathUtils.roundPositive(pieceSize);
		for (Player player : players) {
			player.generateTextures(diameter);
		}
		rect.setSize(widthBoard, heightBoard);
	}

	@Override
	public int[] getAngles() {
		return new int[0];
	}

	@Override
	public Tile getTile(int x, int y) {
		return null;
	}

	@Override
	public boolean isOnBoard(int x, int y) {
		return false;
	}

	@Override
	public int size() {
		return width * height;
	}

	@Override
	public int genKey(int x, int y) {
		return y * width + x;
	}

	public boolean isOnMap(int x, int y) {
		return x > -1 && x < width && y > -1 && y < height;
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
	public Tile[] getAdjacents() {
		return new Tile[0];
	}

	@Override
	public void buildAdjacents(int x, int y) {

	}

	@Override
	public int possibleMoves(Piece piece, Tile from, Collection<Tile> tiles) {
		return 0;
	}

	@Override
	public int shortestPath(Piece piece, Tile from, Tile to, Collection<Tile> tiles) {
		return 0;
	}

	@Override
	public boolean lineOfSight(int x0, int y0, int x1, int y1, Collection<Tile> tiles, Vector2 v) {
		return false;
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
	public void drawReal(Batch batch) {
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
						piece.draw(batch);
					}
				}
			}
		}

		if (actionProgress != null) {
			for (ActionProgressPiece actionProgressPiece : actionProgress.getPieces()) {
				Point<Float> position = actionProgressPiece.getCurrent();
				float fieldX = getX() + borderBoard + (position.x * (borderFields + fieldSize));
				float fieldY = getY() + borderBoard + (position.y * (borderFields + fieldSize));
				actionProgressPiece.getPiece().setPosition(fieldX + pieceSpace, fieldY + pieceSpace);
				actionProgressPiece.getPiece().draw(batch);

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
						actionProgressPiece.getPiece().setPosition(fieldX + pieceSpace, fieldY + pieceSpace);
						actionProgressPiece.getPiece().draw(batch);
					}
				}
			}
			actionProgress.step();
			if (actionProgress.isDone()) {
				for (ActionProgressPiece actionProgressPiece : actionProgress.getPieces()) {
					Point<Integer> positionNew = actionProgressPiece.getEnd();
					CollapsePiece piece = actionProgressPiece.getPiece();
					if (wrapWorld || isOnMap(positionNew)) {
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
