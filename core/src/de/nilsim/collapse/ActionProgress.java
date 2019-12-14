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

	public void addPiece(CollapsePiece piece, Point<Integer> start, Point<Integer> end) {
		pieces.add(new ActionProgressPiece(piece, start, end));
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
