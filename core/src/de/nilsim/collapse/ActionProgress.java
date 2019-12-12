package de.nilsim.collapse;

import java.util.ArrayList;

public class ActionProgress {
	private float progress;
	private float stepSize;
	private ArrayList<ActionProgressPiece> pieces = new ArrayList<>();

	public ActionProgress() {
		this.progress = 0f;
		this.stepSize = 0.01f;
	}

	public void addPiece(CollapsePiece piece, Point<Integer> p0, Point<Integer> p1) {
		this.pieces.add(new ActionProgressPiece(piece, p0, p1));
	}

	public void step() {
		this.progress += this.stepSize;
		for (ActionProgressPiece piece : this.pieces) {
			piece.step(this.progress);
		}
	}

	public ArrayList<ActionProgressPiece> getPieces() {
		return this.pieces;
	}

	public boolean isDone() {
		return this.progress >= 1f;
	}
}
