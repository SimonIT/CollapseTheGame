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
		Player currentPlayer = this.players.get(this.currentPlayerIndex);
		CollapsePiece currentPiece = pieces[y][x];

		if (currentPiece == null) {
			if (!currentPlayer.getFirstMove()) {
				return false;
			}
			pieces[y][x] = new CollapsePiece(currentPlayer.getId(), currentPlayer.getColor());
			currentPiece = pieces[y][x];
		} else if (currentPiece.getOwnerId() != currentPlayer.getId()) {
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
			Set<Vector2> frontier = new HashSet<>();
			frontier.add(new Vector2(x, y));
			while (frontier.size() > 0) {
				Set<Vector2> newFrontier = new HashSet<>();
				ActionProgress actionProgressSplit = new ActionProgress(ActionKind.SPLIT);
				ActionProgress actionProgressDotIncrease = new ActionProgress(ActionKind.DOT_INCREASE);

				for (Vector2 pos : frontier) {
					for (int i = 0; i < 4; ++i) {
						int newX = (int) (pos.x + neighbors[i][0]);
						int newY = (int) (pos.y + neighbors[i][1]);

						actionProgressSplit.addPiece(new CollapsePiece(currentPlayer.getId(), currentPlayer.getColor()), new Point((int) pos.x, (int) pos.y), new Point(newX, newY));

						if (wrapWorld || onGrid(newX, newY)) {
							newX = (newX + this.width) % this.width;
							newY = (newY + this.height) % this.height;
							if (piecesCopy[newY][newX] == null) {
								piecesCopy[newY][newX] = new CollapsePiece(currentPlayer.getId(), currentPlayer.getColor());
								actionProgressDotIncrease.addPiece(new CollapsePiece(currentPlayer.getId(), currentPlayer.getColor(), 0), new Point<>(newX, newY), new Point<>(newX, newY));
							} else {
								if (piecesCopy[newY][newX].getOwnerId() != currentPlayer.getId()) {
									piecesCopy[newY][newX].setOwnerId(currentPlayer.getId());
									piecesCopy[newY][newX].setColor(currentPlayer.getColor());
									currentPlayer.addPoints(piecesCopy[newY][newX].getDotAmount());
								}
								actionProgressDotIncrease.addPiece(new CollapsePiece(currentPlayer.getId(), currentPlayer.getColor(), piecesCopy[newY][newX].getDotAmount()), new Point<>(newX, newY), new Point<>(newX, newY));
								piecesCopy[newY][newX].increaseDotAmount();
							}

							if (piecesCopy[newY][newX].getDotAmount() > 3) {
								newFrontier.add(new Vector2(newX, newY));
							}
						}
					}
					piecesCopy[(int) pos.y][(int) pos.x] = null;
				}
				actionProgresses.add(actionProgressSplit);
				actionProgresses.add(actionProgressDotIncrease);
				frontier = newFrontier;
			}
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
			if (a.getActionKind() == ActionKind.DOT_INCREASE) {
				for (ActionProgressPiece i : a.getPieces()) {
					CollapsePiece p = i.getPiece();
					if (p.getDotAmount() < 3) {
						p.increaseDotAmount();    // TODO: animation (just 4 fun)
					}
					this.pieces[i.getP1().y][i.getP1().x] = p;
				}
				actionProgresses.remove();
			} else {
				a.step();
				if (a.isDone()) {
					actionProgresses.remove();
				} else {
					for (ActionProgressPiece i : a.getPieces()) {
						this.pieces[i.getP0().y][i.getP0().x] = null; // TODO: is this always correct?
						float fieldX = getX() + this.borderBoard + (i.getPosition().x * (this.borderFields + this.fieldSize));
						float fieldY = getY() + this.borderBoard + (i.getPosition().y * (this.borderFields + this.fieldSize));
						batch.draw(i.getPiece(), fieldX + this.pieceSpace, fieldY + this.pieceSpace, pieceSize, pieceSize);
					}
				}
			}
		} else {
			if (this.blocked) this.blocked = false;
		}
	}
}
