package de.nilsim.collapse;

public class ActionProgressPiece {
	public CollapsePiece piece;
	private Point<Integer> p0;
	private Point<Integer> p1;
	private Point<Float> pNow;

	public ActionProgressPiece(CollapsePiece piece, Point<Integer> p0, Point<Integer> p1) {
		this.piece = piece;
		this.p0 = p0;
		this.p1 = p1;
		pNow = new Point<>((float) p0.x, (float) p0.y);
	}

	public Point<Float> getPosition() {
		return pNow;
	}

	public void step(float progress) {
		pNow.x = p0.x + (p1.x - p0.x) * progress;
		pNow.y = p0.y + (p1.y - p0.y) * progress;
	}

	public CollapsePiece getPiece() {
		return piece;
	}

	public Point<Integer> getP0() {
		return p0;
	}

	public Point<Integer> getP1() {
		return p1;
	}
}
