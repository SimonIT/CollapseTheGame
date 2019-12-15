package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.ui.Alignment;
import ch.asynk.gdx.boardgame.ui.Button;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import de.nilsim.collapse.ui.TextField;

public class PlayerScreen extends MenuScreen {

	private int playerId = 0;
	private Button addButton, playButton;
	private TextField field;
	private Array<Player> players = new Array<>();

	public PlayerScreen(CollapseTheGame app) {
		super(app, "Player", new String[]{});
		updatePlayerNames();
		addButton = new Button(app.assets.getFont(AssetNames.font), app.assets.getNinePatch(AssetNames.button, 16, 17, 40, 40), 30);
		addButton.setAlignment(Alignment.BOTTOM_LEFT);
		addButton.write("Neuer Spieler");
		root.add(addButton);
		field = new TextField(app.assets.getNinePatch(AssetNames.button, 16, 17, 40, 40), app.assets.getFont(AssetNames.font), 30);
		field.setPlaceholder("<Spielername>");
		field.setAlignment(Alignment.BOTTOM_CENTER);
		root.add(field);
		playButton = new Button(app.assets.getFont(AssetNames.font), app.assets.getNinePatch(AssetNames.button, 16, 17, 40, 40), 30);
		playButton.setAlignment(Alignment.BOTTOM_RIGHT);
		playButton.write("Spielen");
		root.add(playButton);
	}

	private void updatePlayerNames() {
		Array<String> names = new Array<>();
		for (Player player : players) {
			names.add(player.getName());
		}
		menu.setEntries(app.assets.getFont(AssetNames.font), names.toArray(String.class));
	}

	@Override
	protected void onTouch(int x, int y) {
		if (addButton.touch(touch.x, touch.y)) {
			players.add(new Player(playerId, new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1), field.getText()));
			playerId++;
			updatePlayerNames();
			field.setText("");
		}
		if (playButton.touch(touch.x, touch.y)) {
			app.boardScreen = new BoardScreen(app, players);
			app.setScreen(app.boardScreen);
		}
		field.touch(touch.x, touch.y);
	}

	@Override
	protected InputMultiplexer getMultiplexer() {
		InputMultiplexer multiplexer = super.getMultiplexer();
		multiplexer.addProcessor(field);
		return multiplexer;
	}

	@Override
	protected void onZoom(float dz) {

	}

	@Override
	protected void onDragged(int dx, int dy) {

	}
}
