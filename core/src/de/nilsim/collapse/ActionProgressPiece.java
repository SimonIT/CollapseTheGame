package de.nilsim.collapse;

public class ActionProgressPiece {
	public CollapsePiece piece;
	private Point<Integer> start;
	private Point<Integer> end;
	private Point<Float> current;

	public ActionProgressPiece(CollapsePiece piece, Point<Integer> start, Point<Integer> end) {
		this.piece = piece;
		this.start = start;
		this.end = end;
		current = new Point<>((float) start.x, (float) start.y);
	}

	public Point<Float> getCurrent() {
		return current;
	}

	public void step(float progress) {
		current.x = start.x + (end.x - start.x) * progress;
		current.y = start.y + (end.y - start.y) * progress;
	}

	public CollapsePiece getPiece() {
		return piece;
	}

	public Point<Integer> getStart() {
		return start;
	}

	public Point<Integer> getEnd() {
		return end;
	}
}
