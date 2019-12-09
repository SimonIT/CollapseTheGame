package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.ui.Alignment;
import ch.asynk.gdx.boardgame.ui.Label;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class BoardScreen extends AbstractScreen {
	private CollapseBoard board;
	private ObjectMap<Player, Label> playerLabel = new ObjectMap<>();
	private Vector2 v;

	BoardScreen(CollapseTheGame collapseTheGame) {
		super(collapseTheGame);
		this.v = new Vector2();
		this.playerLabel.put(new Player(1, Color.valueOf("eb3935"), "Simon"), new Label(app.assets.getFont(AssetNames.font)));
		this.playerLabel.put(new Player(2, Color.valueOf("679ed7"), "Nils"), new Label(app.assets.getFont(AssetNames.font)));
		for (Label label : playerLabel.values()) {
			this.root.add(label);
		}
		this.board = new CollapseBoard(5, 7, playerLabel.keys().toArray());
		this.board.setAlignment(Alignment.MIDDLE_CENTER);
		this.root.add(this.board);
	}

	@Override
	public void show() {
		super.show();
		CollapsePiece.diameter = Gdx.graphics.getWidth(); // TODO better
	}

	@Override
	protected void onTouch(int x, int y) {
		this.board.toBoard(this.touch.x, this.touch.y, this.v);
		if (this.board.increaseDotAmount(this.v, true)) {
			Player player = this.board.getCurrentPlayer();
			this.playerLabel.get(player).write(String.valueOf(player.getPoints()));
			this.board.nextPlayer();
		}
	}

	@Override
	protected void onZoom(float dz) {
	}

	@Override
	protected void onDragged(int dx, int dy) {
	}
}
