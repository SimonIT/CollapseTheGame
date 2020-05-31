package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.ui.Alignment;
import ch.asynk.gdx.boardgame.ui.Container;
import ch.asynk.gdx.boardgame.ui.Label;
import ch.asynk.gdx.boardgame.ui.Sizing;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import de.nilsim.collapse.ui.ColorElement;

import java.util.List;

public class BoardScreen extends AbstractScreen {
	private CollapseBoard board;
	private Container scoreBox = new Container();
	private ObjectMap<Player, Label> playerLabel = new ObjectMap<>();
	private Vector2 vector = new Vector2();
	private BitmapFont normalFont;
	private BitmapFont deadFont;
	private BitmapFont shadowFont;

	BoardScreen(CollapseTheGame collapseTheGame, List<Player> players) {
		super(collapseTheGame);
		normalFont = app.assets.getFont(AssetNames.font);
		deadFont = app.assets.get(AssetNames.redFont);
		shadowFont = app.assets.get(AssetNames.shadowFont);
		board = new CollapseBoard(5, 7, players);
		computeBoardSizing(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		scoreBox.setSpacing(20);
		for (Player player : players) {
			Label label = new Label(normalFont);
			label.write(player.getName() + ": " + player.getPoints());
			Container colorPlayer = new Container();
			colorPlayer.setDirection(Container.Direction.HORIZONTAL);
			colorPlayer.add(new ColorElement(player.getColor()));
			colorPlayer.add(label);
			colorPlayer.setSpacing(10);
			scoreBox.add(colorPlayer);
			playerLabel.put(player, label);
		}
		root.add(scoreBox);
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
	public void resize(int width, int height) {
		super.resize(width, height);
		computeBoardSizing(width, height);
	}

	private void computeBoardSizing(int width, int height) {
		if (board.getAspectRatio() < 1f * width / height) {
			if (scoreBox.getDirection() != Container.Direction.VERTICAL) {
				scoreBox.setDirection(Container.Direction.VERTICAL);
				scoreBox.setAlignment(Alignment.MIDDLE_LEFT);
				board.setSizing(Sizing.FILL_Y);
			}
		} else {
			if (scoreBox.getDirection() != Container.Direction.HORIZONTAL) {
				scoreBox.setDirection(Container.Direction.HORIZONTAL);
				scoreBox.setAlignment(Alignment.TOP_CENTER);
				board.setSizing(Sizing.FILL_X);
			}
		}
	}

	@Override
	protected void onTouch(int x, int y) {
		if (board.touch(x, y) != null) {
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
