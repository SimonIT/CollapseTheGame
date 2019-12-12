package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.Piece;

import java.util.ArrayList;

public class ActionProgress {
	private float progress;
	private float stepSize;
	private ArrayList<ActionProgressPiece> pieces = new ArrayList<>();
	private ActionKind actionKind;

	public ActionProgress(ActionKind actionKind) {
		this.progress = 0f;
		this.stepSize = 0.01f;
		this.actionKind = actionKind;
	}

	public void addPiece(CollapsePiece piece, Point p0, Point p1) {
		this.pieces.add(new ActionProgressPiece(piece, p0, p1));
	}

	public void step() {
		this.progress += this.stepSize;
		for (ActionProgressPiece piece: this.pieces) {
			piece.step(this.progress);
		}
	}

	public ArrayList<ActionProgressPiece> getPieces() {
		return this.pieces;
	}

	public boolean isDone() {
		return this.progress >= 1f;
	}

	public ActionKind getActionKind() {
		return actionKind;
	}
}
