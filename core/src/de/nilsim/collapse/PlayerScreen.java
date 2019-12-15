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
	private Color[] standardColors = {Color.valueOf("eb3935"), Color.valueOf("679ed7"), Color.valueOf("bbcc00"), Color.valueOf("005c31"), Color.valueOf("c20088"), Color.valueOf("990000"), Color.valueOf("55cc55"), Color.valueOf("003380"), Color.valueOf("993f00"), Color.valueOf("00998f"), Color.valueOf("8F7C00"), Color.valueOf("4C005C"), Color.valueOf("ff5005"), Color.valueOf("ff33ff"), Color.valueOf("ff7799"), Color.valueOf("8888dd")};

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
			Color playerColor = playerId < standardColors.length ? standardColors[playerId] : new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1);
			players.add(new Player(playerId, playerColor, field.getText()));
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
