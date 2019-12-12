package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Piece;
import com.badlogic.gdx.math.Vector2;

public class ActionProgress {
	private float progress;
	private float  stepSize;
	private Piece piece;
	private int x0;
	private int y0;
	private int x1;
	private int y1;

	public ActionProgress(CollapsePiece piece, int x0, int y0, int x1, int y1) {
		this.progress = 0f;
		this.stepSize = 0.01f;
		this.piece = piece;
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
	}

	public void step() {
		this.progress += this.stepSize;
	}

	public float getX() {
		return this.x0 + (this.x1 - this.x0) * this.progress;
	}

	public float getY() {
		return this.y0 + (this.y1 - this.y0) * this.progress;
	}

	public Piece getPiece() {
		return this.piece;
	}

	public boolean done() {
		return this.progress >= 1f;
	}
}
