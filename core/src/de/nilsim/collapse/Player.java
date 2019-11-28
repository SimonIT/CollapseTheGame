package de.nilsim.collapse;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class Player {
	private Color color;
	private String name;
	private Array<Piece> pieces;
	private long points;

	public boolean ownsPiece(Piece piece) {
		return this.pieces.contains(piece, true);
	}
}
