package de.nilsim.collapse;

import java.util.ArrayList;

public class ActionProgress {
	private float progress;
	private float stepSize;
	private ArrayList<ActionProgressPiece> pieces = new ArrayList<>();

	public ActionProgress() {
		progress = 0f;
		stepSize = 0.01f;
	}

	public void addPiece(CollapsePiece piece, Point<Integer> p0, Point<Integer> p1) {
		pieces.add(new ActionProgressPiece(piece, p0, p1));
	}

	public void step() {
		progress += stepSize;
		for (ActionProgressPiece piece : pieces) {
			piece.step(progress);
		}
	}

	public ArrayList<ActionProgressPiece> getPieces() {
		return pieces;
	}

	public boolean isDone() {
		return progress >= 1f;
	}
}
