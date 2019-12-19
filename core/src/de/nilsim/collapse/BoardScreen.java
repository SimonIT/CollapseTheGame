package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.ui.Alignment;
import ch.asynk.gdx.boardgame.ui.Label;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import de.nilsim.collapse.ui.ColorElement;
import de.nilsim.collapse.ui.FlexibleBox;
import de.nilsim.collapse.ui.HorizontalBox;
import de.nilsim.collapse.ui.VerticalBox;

public class BoardScreen extends AbstractScreen {
	private CollapseBoard board;
	private ObjectMap<Player, Label> playerLabel = new ObjectMap<>();
	private Vector2 vector = new Vector2();
	private BitmapFont normalFont;
	private BitmapFont deadFont;
	private BitmapFont shadowFont;

	BoardScreen(CollapseTheGame collapseTheGame, Array<Player> players) {
		super(collapseTheGame);
		normalFont = app.assets.getFont(AssetNames.font);
		deadFont = app.assets.get(AssetNames.redFont);
		shadowFont = app.assets.get(AssetNames.shadowFont);
		FlexibleBox scoreBox = new VerticalBox(players.size);
		scoreBox.setAlignment(Alignment.MIDDLE_LEFT);
		scoreBox.setSpacing(20);
		for (Player player : players) {
			Label label = new Label(normalFont);
			label.write(player.getName() + ": " + player.getPoints());
			FlexibleBox colorPlayer = new HorizontalBox(new ColorElement(player.getColor()), label);
			colorPlayer.setSpacing(10);
			scoreBox.add(colorPlayer);
			playerLabel.put(player, label);
		}
		root.add(scoreBox);
		board = new CollapseBoard(5, 7, players);
		playerLabel.get(board.getCurrentPlayer()).setFont(shadowFont);
		board.setAlignment(Alignment.MIDDLE_CENTER);
		board.setPointChangeListener(player -> {
			Label label = playerLabel.get(player);
			label.write(player.getName() + ": " + player.getPoints());
		});
		board.setCurrentPlayerChangeListener((oldCurrentPlayer, newCurrentPlayer) -> {
			playerLabel.get(oldCurrentPlayer).setFont(normalFont);
			playerLabel.get(newCurrentPlayer).setFont(shadowFont);
			for (Player player : players) {
				if (!player.getAlive()) {
					playerLabel.get(player).setFont(deadFont);
				}
			}
		});
		root.add(board);
	}

	@Override
	protected void onTouch(int x, int y) {
		if (board.touch(x, y)) {
			board.toBoard(touch.x, touch.y, vector);
			board.increaseDotAmount((int) vector.x, (int) vector.y);
		}
	}

	@Override
	protected void onZoom(float dz) {
	}

	@Override
	protected void onDragged(int dx, int dy) {
	}
}
