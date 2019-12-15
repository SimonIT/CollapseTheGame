package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.ui.Alignment;
import ch.asynk.gdx.boardgame.ui.Label;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import de.nilsim.collapse.ui.HVBox;

public class BoardScreen extends AbstractScreen {
	private CollapseBoard board;
	private ObjectMap<Player, Label> playerLabel = new ObjectMap<>();
	private Vector2 vector;
	private BitmapFont deadFont;

	BoardScreen(CollapseTheGame collapseTheGame, Array<Player> players) {
		super(collapseTheGame);
		deadFont = app.assets.get(AssetNames.font);
		deadFont.setColor(Color.RED);
		vector = new Vector2();
		HVBox scoreBox = new HVBox(players.size);
		scoreBox.setType(HVBox.TYPE.VBox);
		scoreBox.setAlignment(Alignment.MIDDLE_LEFT);
		scoreBox.setSpacing(20);
		for (Player player : players) {
			Label label = new Label(app.assets.getFont(AssetNames.font));
			label.write(player.getName() + ": " + player.getPoints());
			scoreBox.add(label);
			playerLabel.put(player, label);
		}
		root.add(scoreBox);
		board = new CollapseBoard(5, 7, playerLabel.keys().toArray());
		board.setAlignment(Alignment.MIDDLE_CENTER);
		board.setPointChangeListener(player -> {
			Label label = playerLabel.get(player);
			if (!player.getAlive()) {
				root.remove(label);
				label = playerLabel.put(player, new Label(deadFont));
				root.add(label);
			}
			label.write(player.getName() + ": " + player.getPoints());
		});
		root.add(board);
	}

	@Override
	protected void onTouch(int x, int y) {
		if (board.touch(x, y)) {
			board.toBoard(touch.x, touch.y, vector);
			if (board.increaseDotAmount((int) vector.x, (int) vector.y)) {
				board.nextPlayer();
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
