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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

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
	private Queue<ActionProgress> actionProgresses = new LinkedList<>();

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

	boolean increaseDotAmount(int x, int y) {
		if (!onGrid(x, y)) {
			return false;
		}
		this.blocked = true;
		Player currentPlayer = this.getCurrentPlayer();
		CollapsePiece currentPiece = pieces[y][x];

		if (currentPiece == null) {
			if (!currentPlayer.getFirstMove()) {
				return false;
			}
			pieces[y][x] = new CollapsePiece(currentPlayer, currentPlayer.getDefaultPieceTexture());
			currentPiece = pieces[y][x];
		} else if (currentPiece.getOwner() != currentPlayer) {
			return false;
		} else {
			currentPiece.increaseDotAmount();
		}

		// "deep copy" -> TODO: make this better, it's terrible :/
		CollapsePiece[][] piecesCopy = new CollapsePiece[this.height][this.width];
		for (int i = 0; i < this.height; ++i) {
			for (int j = 0; j < this.width; ++j) {
				if (pieces[i][j] != null) {
					piecesCopy[i][j] = pieces[i][j].clone();
				}
			}
		}

		if (currentPiece.getDotAmount() > 3) {
			Set<Point<Integer>> frontier = new HashSet<>();
			frontier.add(new Point<>(x, y));
			while (frontier.size() > 0) {
				Set<Point<Integer>> newFrontier = new HashSet<>();
				ActionProgress actionProgress = new ActionProgress();
				for (Point<Integer> pos : frontier) {
					for (int i = 0; i < 4; ++i) {
						int newX = pos.x + neighbors[i][0];
						int newY = pos.y + neighbors[i][1];

						actionProgress.addPiece(new CollapsePiece(currentPlayer, currentPlayer.getDefaultPieceTexture()), new Point<>(pos.x, pos.y), new Point<>(newX, newY));

						if (wrapWorld || onGrid(newX, newY)) {
							newX = (newX + this.width) % this.width;
							newY = (newY + this.height) % this.height;
							if (piecesCopy[newY][newX] == null) {
								piecesCopy[newY][newX] = new CollapsePiece(currentPlayer, currentPlayer.getDefaultPieceTexture());
							} else {
								if (piecesCopy[newY][newX].getOwner() != currentPlayer) {
									piecesCopy[newY][newX].setOwner(currentPlayer);
									int dots = piecesCopy[newY][newX].getDotAmount();
									piecesCopy[newY][newX].setTexture(currentPlayer.getPieceTexture(dots));
									currentPlayer.addPoints(dots);
								}
								piecesCopy[newY][newX].increaseDotAmount();
							}

							if (piecesCopy[newY][newX].getDotAmount() > 3) {
								newFrontier.add(new Point<>(newX, newY));
							} else {
								piecesCopy[newY][newX].setTexture(currentPlayer.getPieceTexture(piecesCopy[newY][newX].getDotAmount()));
							}
						}
					}
					piecesCopy[pos.y][pos.x] = null;
				}
				actionProgresses.add(actionProgress);
				frontier = newFrontier;
			}
		} else {
			currentPiece.setTexture(currentPlayer.getPieceTexture(currentPiece.getDotAmount()));
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

	void addPiece(int x, int y, CollapsePiece piece) {
		this.pieces[y][x] = piece;
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

		for (int i = 0; i < this.height; i++) {
			for (int j = 0; j < this.width; j++) {
				float fieldX = getX() + this.borderBoard + (j * (this.borderFields + this.fieldSize));
				float fieldY = getY() + this.borderBoard + (i * (this.borderFields + this.fieldSize));
				batch.draw(this.field, fieldX, fieldY, this.fieldSize, this.fieldSize);
				if (this.pieces[i][j] != null) {
					batch.draw(this.pieces[i][j], fieldX + this.pieceSpace, fieldY + this.pieceSpace, pieceSize, pieceSize);
				}
			}
		}

		if (!actionProgresses.isEmpty()) {
			ActionProgress a = actionProgresses.peek();
			for (ActionProgressPiece i : a.getPieces()) {
				this.pieces[i.getP0().y][i.getP0().x] = null;
				float fieldX = getX() + this.borderBoard + (i.getPosition().x * (this.borderFields + this.fieldSize));
				float fieldY = getY() + this.borderBoard + (i.getPosition().y * (this.borderFields + this.fieldSize));
				batch.draw(i.getPiece(), fieldX + this.pieceSpace, fieldY + this.pieceSpace, pieceSize, pieceSize);
			}
			a.step();
			if (a.isDone()) {
				for (ActionProgressPiece i : a.getPieces()) {
					Point<Integer> finalPos = new Point<>(i.getP1().x, i.getP1().y);
					if (!onGrid(finalPos.x, finalPos.y)) {
						if (wrapWorld) {
							finalPos.x = (finalPos.x + this.width) % this.width;
							finalPos.y = (finalPos.y + this.height) % this.height;
						} else {
							continue;
						}
					}
					if (this.pieces[finalPos.y][finalPos.x] == null) {
						this.pieces[finalPos.y][finalPos.x] = i.getPiece();
					} else {
						int d = this.pieces[finalPos.y][finalPos.x].getDotAmount() + 1;
						i.getPiece().increaseDotAmount();
						this.pieces[finalPos.y][finalPos.x] = i.getPiece();	// this is important to change the owner
						if (d < 4)
							this.pieces[finalPos.y][finalPos.x].setTexture(getPlayerById(i.getPiece().getOwner().getId()).getPieceTexture(d));
					}
				}
				actionProgresses.remove();
			}
		} else {
			this.blocked = false;
		}
	}
}
