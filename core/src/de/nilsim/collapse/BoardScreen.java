package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.ui.Alignment;
import ch.asynk.gdx.boardgame.ui.Label;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class BoardScreen extends AbstractScreen {
	private CollapseBoard board;
	private ObjectMap<Player, Label> playerLabel = new ObjectMap<>();
	private Vector2 v;

	BoardScreen(CollapseTheGame collapseTheGame, Array<Player> players) {
		super(collapseTheGame);
		this.v = new Vector2();
		for (Player player : players) {
			Label label = new Label(app.assets.getFont(AssetNames.font));
			this.root.add(label);
			this.playerLabel.put(player, label);
		}
		this.board = new CollapseBoard(5, 7, playerLabel.keys().toArray());
		this.board.setAlignment(Alignment.MIDDLE_CENTER);
		this.root.add(this.board);
	}

	@Override
	protected void onTouch(int x, int y) {
		this.board.toBoard(this.touch.x, this.touch.y, this.v);
		if (this.board.increaseDotAmount(this.v)) {
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
