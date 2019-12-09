package de.nilsim.collapse;

import ch.asynk.gdx.boardgame.ui.Button;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class PlayerScreen extends MenuScreen {

	int playerId = 0;
	private Button addButton, playButton;
	private Array<Player> players = new Array<>();

	public PlayerScreen(CollapseTheGame app) {
		super(app, "Player", new String[]{});
		this.players.add(new Player(1, Color.valueOf("eb3935"), "Simon"), new Player(2, Color.valueOf("679ed7"), "Nils")); // Todo remove
		updatePlayerNames();
		this.addButton = new Button(app.assets.getFont(AssetNames.font), app.assets.getNinePatch(AssetNames.button, 16, 17, 40, 40));
		this.addButton.write("Neuer");
		this.root.add(this.addButton);
		this.playButton = new Button(app.assets.getFont(AssetNames.font), app.assets.getNinePatch(AssetNames.button, 16, 17, 40, 40));
		this.playButton.write("Spielen");
		this.playButton.setPosition(500, this.playButton.getY());
		this.root.add(this.playButton);
	}

	private void updatePlayerNames() {
		Array<String> names = new Array<>();
		for (Player player : this.players) {
			names.add(player.getName());
		}
		this.menu.setEntries(app.assets.getFont(AssetNames.font), names.toArray(String.class));
	}

	@Override
	protected void onTouch(int x, int y) {
		if (this.addButton.touch(this.touch.x, this.touch.y)) {
			Gdx.input.getTextInput(new Input.TextInputListener() {
				@Override
				public void input(String text) {
					players.add(new Player(playerId, new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), MathUtils.random()), text));
					playerId++;
					updatePlayerNames();
				}

				@Override
				public void canceled() {

				}
			}, "Player Name", "", "");
		}
		if (this.playButton.touch(this.touch.x, this.touch.y)) {
			app.boardScreen = new BoardScreen(app, this.players);
			app.setScreen(app.boardScreen);
		}
	}

	@Override
	protected void onZoom(float dz) {

	}

	@Override
	protected void onDragged(int dx, int dy) {

	}
}
