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
		this.pNow = new Point<>((float) p0.x, (float) p0.y);
	}

	public Point<Float> getPosition() {
		return this.pNow;
	}

	public void step(float progress) {
		this.pNow.x = this.p0.x + (this.p1.x - this.p0.x) * progress;
		this.pNow.y = this.p0.y + (this.p1.y - this.p0.y) * progress;
	}

	public CollapsePiece getPiece() {
		return this.piece;
	}

	public Point<Integer> getP0() {
		return this.p0;
	}

	public Point<Integer> getP1() {
		return this.p1;
	}
}
