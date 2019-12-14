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
		this.addButton = new Button(app.assets.getFont(AssetNames.font), app.assets.getNinePatch(AssetNames.button, 16, 17, 40, 40), 30);
		this.addButton.setAlignment(Alignment.BOTTOM_LEFT);
		this.addButton.write("Neuer Spieler");
		this.root.add(this.addButton);
		this.field = new TextField(app.assets.getNinePatch(AssetNames.button, 16, 17, 40, 40), app.assets.getFont(AssetNames.font), 30);
		this.field.setPlaceholder("<Spielername>");
		this.field.setAlignment(Alignment.BOTTOM_CENTER);
		this.root.add(field);
		this.playButton = new Button(app.assets.getFont(AssetNames.font), app.assets.getNinePatch(AssetNames.button, 16, 17, 40, 40), 30);
		this.playButton.setAlignment(Alignment.BOTTOM_RIGHT);
		this.playButton.write("Spielen");
		this.root.add(this.playButton);
	}

	private void updatePlayerNames() {
		Array<String> names = new Array<>();
		for (Player player : this.players) {
			names.add(player.getName());
		}
		this.menu.setEntries(this.app.assets.getFont(AssetNames.font), names.toArray(String.class));
	}

	@Override
	protected void onTouch(int x, int y) {
		if (this.addButton.touch(this.touch.x, this.touch.y)) {
			this.players.add(new Player(this.playerId, new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), MathUtils.random()), this.field.getText()));
			this.playerId++;
			updatePlayerNames();
			this.field.setText("");
		}
		if (this.playButton.touch(this.touch.x, this.touch.y)) {
			this.app.boardScreen = new BoardScreen(this.app, this.players);
			this.app.setScreen(this.app.boardScreen);
		}
		this.field.touch(this.touch.x, this.touch.y);
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
