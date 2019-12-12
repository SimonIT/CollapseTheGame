package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.ui.Alignment;
import ch.asynk.gdx.boardgame.ui.Label;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class BoardScreen extends AbstractScreen {
	private CollapseBoard board;
	private ObjectMap<Player, Label> playerLabel = new ObjectMap<>();
	private Vector2 v;
	private BitmapFont deadFont;

	BoardScreen(CollapseTheGame collapseTheGame, Array<Player> players) {
		super(collapseTheGame);
		this.deadFont = this.app.assets.get(AssetNames.font);
		this.deadFont.setColor(Color.RED);
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
		if (this.board.touch(x, y)) {
			this.board.toBoard(this.touch.x, this.touch.y, this.v);
			if (this.board.increaseDotAmount((int) this.v.x, (int) this.v.y)) {
				Player player = this.board.getCurrentPlayer();
				Label label = this.playerLabel.get(player);
				if (!player.getAlive()) {
					this.root.remove(label);
					label = this.playerLabel.put(player, new Label(this.deadFont));
					this.root.add(label);
				}
				label.write(String.valueOf(player.getPoints()));
				this.board.nextPlayer();
			}
		}
	}

	@Override
	protected void onZoom(float dz) {
	}

	@Override
	protected void onDragged(int dx, int dy) {
	}
}
